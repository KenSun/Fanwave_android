package com.wildmind.fanwave.follow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.xmpp.FWXMPPManager;

public class FollowManager {
	
	private static ConcurrentHashMap<String, TVProgram> follow_list = new ConcurrentHashMap<String, TVProgram>();
	private static FollowReceiver follow_receiver = null;
	
	public static void initReceiver () {
		follow_receiver = new FollowReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(FWXMPPManager.BROADCAST_USER_WILL_CONNECT_XMPP);
		intentFilter.addAction(AccountManager.BROADCAST_USER_LOGOUT);
		ApplicationManager.getAppContext().registerReceiver(follow_receiver, intentFilter);
	}
	
	/**
	 * Follow a program.
	 * @param program
	 * @return boolean
	 */
	public static boolean followProgram (TVProgram program) {
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/follow/follow";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("pgid", program.getPgid());
		bodyMap.put("country", program.getCountry());
		bodyMap.put("username", AccountManager.getCurrentUser().getUsername());
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		boolean success = response.get("code").equals("200");
		if (success)
			follow_list.put(program.getPgid(), program);
		
		return success;
	}
	
	/**
	 * Unfollow a program.
	 * @param program
	 * @return boolean
	 */
	public static boolean unfollowProgram (TVProgram program) {
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/follow/unfollow";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("pgid", program.getPgid());
		bodyMap.put("country", program.getCountry());
		bodyMap.put("username", AccountManager.getCurrentUser().getUsername());
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		boolean success = response.get("code").equals("200");
		if (success)
			follow_list.remove(program.getPgid());
		
		return success;
	}
	
	/**
	 * Whether is following program.
	 * @param pgid
	 * @return boolean
	 */
	public static boolean isFollowing (String pgid) {
		return follow_list.containsKey(pgid);
	}
	
	/**
	 * Synchronize follow list.
	 */
	public static void syncFollows () {
		ArrayList<TVFollow> follows = getFollowList(AccountManager.getCurrentUser().getUsername());
		
		follow_list.clear();
		for (TVFollow follow:follows)
			follow_list.put(follow.getPgid(), follow);
	}
	
	/**
	 * Get user's follow list.
	 * @param username
	 * @return ArrayList<TVFollow>
	 */
	public static ArrayList<TVFollow> getFollowList (String username) {
		ArrayList<TVFollow> follows = new ArrayList<TVFollow>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/follow/getfollowlist";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("username", username);
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++)
					follows.add(new TVFollow(array.getJSONObject(i)));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return follows;
	}
	
	/**
	 * FollowManager Broadcast Receiver class
	 * @author Kencool
	 *
	 */
	private static class FollowReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// get action
			String action = intent.getAction();
			
			if (action.equals(FWXMPPManager.BROADCAST_USER_WILL_CONNECT_XMPP)) {
				// synchronize follow list
				new Thread (new Runnable () {
					public void run () {
						syncFollows();
					}
				}).start();
			} else if (action.equals(AccountManager.BROADCAST_USER_LOGOUT)) {
				follow_list.clear();
			}
		}
	}
}
