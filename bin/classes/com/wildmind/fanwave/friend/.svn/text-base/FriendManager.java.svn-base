package com.wildmind.fanwave.friend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.xmpp.roster.FWRosterManager.FWFriendListener;
import com.wildmind.fanwave.xmpp.FWXMPPManager;

public class FriendManager {

	/**
	 * Broadcast actions.
	 */
	public static String BROADCAST_REQUEST_CHANGED = "broadcast_request_list_changed";
	
	private static FriendListener friend_listener = new FriendListener();
	private static FriendReceiver friend_receiver = null;
	
	/**
	 * Key : username  Value : TVUser
	 */
	private static ConcurrentHashMap<String, TVUser> online_friend_list = new ConcurrentHashMap<String, TVUser>();
	private static ConcurrentHashMap<String, TVUser> offline_friend_list = new ConcurrentHashMap<String, TVUser>();
	private static ConcurrentHashMap<String, TVUser> request_list = new ConcurrentHashMap<String, TVUser>();
	private static ConcurrentHashMap<String ,TVUser> inviting_list = new ConcurrentHashMap<String ,TVUser>();
	
	private static boolean friend_updated = false;
	private static boolean request_updated = false;
	
	public static ConcurrentHashMap<String, TVUser> getOnlineFriendList () {
		return online_friend_list;
	}
	
	public static ConcurrentHashMap<String, TVUser> getOfflineFriendList () {
		return offline_friend_list;
	}
	
	public static ConcurrentHashMap<String, TVUser> getRequestList () {
		return request_list;
	}
	
	public static ConcurrentHashMap<String, TVUser> getInvitingList () {
		return inviting_list;
	}
	
	/**
	 * Get all friends.
	 * @return ArrayList<TVUser>
	 */
	public static ArrayList<TVUser> getFriends () {
		ArrayList<TVUser> friends = new ArrayList<TVUser>();
		
		Collection<TVUser> users = (Collection<TVUser>) online_friend_list.values();
		for (TVUser user:users)
			friends.add(user);
			
		users = (Collection<TVUser>) offline_friend_list.values();
		for (TVUser user:users)
			friends.add(user);
				
		Collections.sort(friends);
				
		return friends;
	}
	
	/**
	 * Get data of a friend.
	 * @param username
	 * @return TVUser
	 */
	public static TVUser getFriend (String username) {
		TVUser user = online_friend_list.get(username);
		if (user == null)
			user = offline_friend_list.get(username);
		return user;
	}
	
	/**
	 * Clear all resources.
	 */
	public static void clear () {
		online_friend_list.clear();
		offline_friend_list.clear();
		request_list.clear();
		friend_updated = false;
		request_updated = false;
		
		broadcastRequestListChanged();
	}
	
	/**
	 * Initialize receiver.
	 */
	public static void initReceiver () {
		friend_receiver = new FriendReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(FWXMPPManager.BROADCAST_USER_WILL_CONNECT_XMPP);
		intentFilter.addAction(AccountManager.BROADCAST_USER_LOGOUT);
		ApplicationManager.getAppContext().registerReceiver(friend_receiver, intentFilter);
	}
	
	/**
	 * Broadcast request list has been changed.
	 */
	private static void broadcastRequestListChanged() {
		Intent i = new Intent();
		i.setAction(BROADCAST_REQUEST_CHANGED);
		ApplicationManager.getAppContext().sendBroadcast(i);
	}
	
	/**
	 * Listening a roster manager.
	 */
	public static void listenRoster () {
		FWXMPPManager.getRosterManager().addFriendListener(friend_listener);
	}
	
	/**
	 * List Update Record Methods
	 */
	public static void resetFriendUpdated () {
		friend_updated = false;
	}
	
	public static void resetRequestUpdated () {
		request_updated = false;
	}
	
	public static boolean isFriendListUpdated () {
		return friend_updated;
	}
	
	public static boolean isRequestListUpdated () {
		return request_updated;
	}
	
	/**
	 * Invite a friend.
	 * @param user
	 * @return
	 */
	public static boolean inviteFriend (TVUser user) {
		boolean success = FWXMPPManager.getRosterManager().subscribeUser(user.getJid());
		if (success)
			inviting_list.put(user.getUsername(), user);
		return success;
	}
	
	/**
	 * Accept a friend. 
	 * @param user
	 * @return
	 */
	public static boolean acceptFriend (TVUser user) {
		return FWXMPPManager.getRosterManager().acceptUser(user);
	}
	
	/**
	 * Reject a friend.
	 * @param user
	 */
	public static void rejectFriend (TVUser user) {
		FWXMPPManager.getRosterManager().rejectUser(user.getJid());
	}
	
	/**
	 * Remove a friend.
	 * @param user
	 */
	public static void removeFriend (TVUser user) {
		FWXMPPManager.getRosterManager().removeUser(user.getJid());
	}
	
	/**
	 * Whether user is a friend.
	 * @param username
	 * @return
	 */
	public static boolean isFriend (String username) {
		return online_friend_list.containsKey(username) || 
			   offline_friend_list.containsKey(username);
	}
	
	/**
	 * Whether user is a requesting user.
	 * @param username
	 * @return
	 */
	public static boolean isRequest (String username) {
		return request_list.containsKey(username);
	}
	
	public static boolean isInviting (String username) {
		return inviting_list.containsKey(username);
	}
	
	/**
	 * Listener for listening friend events.
	 * @author Kencool
	 *
	 */
	private static class FriendListener implements FWFriendListener {

		@Override
		public void receiveFriends(ArrayList<TVUser> users) {
			for (TVUser user:users) {
				if (user.getUsername() == null)
					continue;
				
				if (!online_friend_list.containsKey(user.getUsername()) &&
					!offline_friend_list.containsKey(user.getUsername()))
					offline_friend_list.put(user.getUsername(), user);
				
				if (request_list.remove(user.getUsername()) != null)
					broadcastRequestListChanged();
				inviting_list.remove(user.getUsername());
			}
		}
		
		@Override
		public void receiveRequest(TVUser user) {
			if (user.getUsername() == null) 
				return;
			
			request_list.put(user.getUsername(), user);
			broadcastRequestListChanged();
			request_updated = true;
		}

		@Override
		public void receiveAccept(TVUser user) {
			if (user.getUsername() == null) 
				return;
			
			if (FWXMPPManager.getRosterManager().isUserOnline(user.getJid()))
				online_friend_list.put(user.getUsername(), user);
			else
				offline_friend_list.put(user.getUsername(), user);
			
			inviting_list.remove(user.getUsername());
			friend_updated = true;
		}

		@Override
		public void receiveRemoves(ArrayList<TVUser> users) {
			for (TVUser user:users) {
				if (online_friend_list.containsKey(user.getUsername())) {
					online_friend_list.remove(user.getUsername());
					friend_updated = true;
				}
				if (offline_friend_list.containsKey(user.getUsername())) {
					offline_friend_list.remove(user.getUsername());
					friend_updated = true;
				}
				if (request_list.containsKey(user.getUsername())) {
					request_list.remove(user.getUsername());
					broadcastRequestListChanged();
					request_updated = true;
				}
				
				inviting_list.remove(user.getUsername());
			}
		}

		@Override
		public void receiveOnline(TVUser user) {
			if (offline_friend_list.containsKey(user.getUsername())) {
				offline_friend_list.remove(user.getUsername());
				online_friend_list.put(user.getUsername(), user);
				friend_updated = true;
			}
		}

		@Override
		public void receiveOffline(TVUser user) {
			if (online_friend_list.containsKey(user.getUsername())) {
				online_friend_list.remove(user.getUsername());
				offline_friend_list.put(user.getUsername(), user);
				friend_updated = true;
			}
		}
	}
	
	/**
	 * FriendManager Broadcast Receiver class
	 * @author Kencool
	 *
	 */
	private static class FriendReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// get action
			String action = intent.getAction();
			
			if (action.equals(FWXMPPManager.BROADCAST_USER_WILL_CONNECT_XMPP)) {
				listenRoster();
				
			} else if (action.equals(AccountManager.BROADCAST_USER_LOGOUT)) {
				clear();
			}
		}
		
	}
}
