package com.wildmind.fanwave.c2dm;

import com.urbanairship.push.PushManager;
import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.notification.FWNotificationManager;
import com.wildmind.fanwave.xmpp.FWXMPPManager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class C2DMManager {

	public static final String BROADCAST_C2DM_REGISTER = "com.google.android.c2dm.intent.REGISTER";
	public static final String BROADCAST_C2DM_UNREGISTER = "com.google.android.c2dm.intent.UNREGISTER";
	
	private static String registration_id = null;
	private static InAppReceiver in_app_receiver = null;
	
	public static String getRegistrationId () {
		return registration_id;
	}
	
	/**
	 * Initialize receiver.
	 */
	public static void initReceiver () {
		in_app_receiver = new InAppReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(FWXMPPManager.BROADCAST_USER_WILL_CONNECT_XMPP);
		intentFilter.addAction(AccountManager.BROADCAST_USER_LOGOUT);
		ApplicationManager.getAppContext().registerReceiver(in_app_receiver, intentFilter);
	}
	
	/**
	 * Register C2DM service.
	 */
	public static void register () {
		Intent i = new Intent(BROADCAST_C2DM_REGISTER);
		i.putExtra("sender", "fanwave.tv@gmail.com");
		i.putExtra("app", PendingIntent.getBroadcast(ApplicationManager.getAppContext(), 0, new Intent(), 0));
		ApplicationManager.getAppContext().startService(i);
	}
	
	/**
	 * Unregister C2DM service.
	 */
	public static void unregister () {
		Intent i = new Intent(BROADCAST_C2DM_UNREGISTER);
		i.putExtra("app", PendingIntent.getBroadcast(ApplicationManager.getAppContext(), 0, new Intent(), 0));
		ApplicationManager.getAppContext().startService(i);
	}
	
	/**
	 * Process registration.
	 * @param id
	 */
	protected static void processRegistration (final String id) {
		registration_id = id;

		new Thread (new Runnable () {
			public void run () {
				boolean success = false;
				do {
					String apid = PushManager.shared().getPreferences().getPushId();
					if (apid != null)
						success = FWNotificationManager.registerFanwaveNotification(id, apid);
					else {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} while (!success);
			}
		}).start();
	}
	
	/**
	 * Handle registration error.
	 * @param error
	 */
	protected static void handleRegistrationError (String error) {
	}
	
	private static class InAppReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// get action
			String action = intent.getAction();
			
			if (action.equals(FWXMPPManager.BROADCAST_USER_WILL_CONNECT_XMPP)) {
				FWNotificationManager.retrieveNotifyFromUrbanAirship();
				register();
			} else if (action.equals(AccountManager.BROADCAST_USER_LOGOUT)) {
				FWNotificationManager.disableNotification();
				NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				mgr.cancelAll();
			}
		}
	}
}
