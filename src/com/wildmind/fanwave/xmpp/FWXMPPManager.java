package com.wildmind.fanwave.xmpp;

import java.util.ArrayList;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.util.StringUtils;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.xmpp.badge.FWXMPPBadgeManager;
import com.wildmind.fanwave.xmpp.muc.FWMucManager;
import com.wildmind.fanwave.xmpp.roster.FWRosterManager;

public class FWXMPPManager {
	
	/**
	 * Broadcast actions.
	 */
	public static String BROADCAST_USER_WILL_CONNECT_XMPP 	= "broadcast_user_will_connect_xmpp";
	public static String BROADCAST_USER_WILL_RECONNECT_XMPP = "broadcast_user_will_reconnect_xmpp";
	
	private static XMPPConnection connection = null;
	private static FWRosterManager roster_manager = null;
	private static FWMucManager muc_manager = null;
	private static FWXMPPBadgeManager badge_manager = null;
	
	private static String hostname = null;
	private static String user = null;
	private static String password = null;
	private static String resource = null;
	private static String status = null;
	
	public static FWRosterManager getRosterManager () {
		ensureXMPPConnection();
		return roster_manager;
	}
	
	public static FWMucManager getMucManager () {
		ensureXMPPConnection();
		return muc_manager;
	}
	
	public static FWXMPPBadgeManager getBadgeManager () {
		ensureXMPPConnection();
		return badge_manager;
	}
	
	private static void ensureXMPPConnection () {

	}
	
	private static void initConnection () {
		ConnectionConfiguration config = new ConnectionConfiguration(hostname, 5222);
		config.setSASLAuthenticationEnabled(false);
		config.setReconnectionAllowed(true);
		config.setDebuggerEnabled(false);
		Roster.setDefaultSubscriptionMode(SubscriptionMode.manual);
		connection = new XMPPConnection(config);
	}
	
	public static boolean connect (String hostname, String user, String password, String resource, String status) {
		boolean success = false;
		
		FWXMPPManager.hostname = hostname;
		FWXMPPManager.user = user;
		FWXMPPManager.password = password;
		FWXMPPManager.resource = resource;
		FWXMPPManager.status = status;
		
		if (connection == null || !connection.getHost().equals(hostname))
			initConnection();
		roster_manager = new FWRosterManager(connection);
		muc_manager = new FWMucManager(connection);
		badge_manager = new FWXMPPBadgeManager(connection);
		
		// broadcast will connect xmpp
		Intent i = new Intent();
		i.setAction(BROADCAST_USER_WILL_CONNECT_XMPP);
		ApplicationManager.getAppContext().sendBroadcast(i);
		
		// connect
		try {
			ArrayList<RosterListener> listeners = new ArrayList<RosterListener>();
			listeners.add(roster_manager.getRosterListener());
			
			connection.connect();
			connection.login(user, password, resource, status, listeners);
			
			// monitor XMPP connection, if first starting, new a handler thread
			if (monitor_handler == null)
				new Thread (new Runnable () {
					public void run () {
						Looper.prepare();
						startMonitorXMPP();
						Looper.loop();
					}
				}).start();
			else
				startMonitorXMPP();
			
			success = true;
			
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  success;
	}
	
	public static void reconnect () {
		// broadcast will reconnect xmpp
		Intent i = new Intent();
		i.setAction(BROADCAST_USER_WILL_RECONNECT_XMPP);
		ApplicationManager.getAppContext().sendBroadcast(i);
		
		if (connection == null) {
			connect(hostname, user, password, resource, status);
		} else {
			try {
				connection.connect();

				// reconnect to all muc
				muc_manager.reconnectMucs();
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
		reconnecting = false;
	}
	
	public static void disconnect () {
		if (connection == null)
			return;
		
		stopMonitorXMPP();
		
		connection.disconnect();
		roster_manager.clear();
		roster_manager = null;
		muc_manager.clear();
		muc_manager = null;
		badge_manager.clear();
		badge_manager = null;
	}
	
	public static boolean isConnected () {
		return connection != null && connection.isConnected();
	}
	
	public static String getUserFullJid () {
		return connection.getUser();
	}
	
	public static String getUserBareJid () {
		return StringUtils.parseBareAddress(connection.getUser());
	}
	
	public static String getUserResource () {
		return StringUtils.parseResource(connection.getUser());
	}
	
	/**
	 * XMPP Monitor Methods
	 */
	
	private static boolean reconnecting = false;
	private static Handler monitor_handler = null;
	private static Runnable monitor = new Runnable () {
		public void run () {
			if (NetworkManager.isInternetAvailable(ApplicationManager.getAppContext())) {
				if (!reconnecting) {
					if (connection == null ||
						(!connection.isConnected() && !connection.isAuthenticated())) {
						reconnecting = true;
						reconnect();
					}
				}
			}
			
			monitor_handler.postDelayed(monitor, 2000);
		}
	};
	
	public static void startMonitorXMPP () {
		if (monitor_handler == null) 
			monitor_handler = new Handler();
		else
			monitor_handler.removeCallbacks(monitor);
		monitor_handler.postDelayed(monitor, 2000);
	}
	
	public static void stopMonitorXMPP () {
		if (monitor_handler != null)
			monitor_handler.removeCallbacks(monitor);
	}
}
