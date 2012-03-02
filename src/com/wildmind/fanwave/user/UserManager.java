package com.wildmind.fanwave.user;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import com.wildmind.fanwave.facebook.FBUser;
import com.wildmind.fanwave.network.NetworkManager;

public class UserManager {

	/**
	 * Get TVUser by username.
	 * @param username
	 * @return TVUser
	 */
	public static TVUser getUserByUsername (String username) {
		if (username == null || username.length() == 0)
			return new TVUser();
		
		ArrayList<String> usernames = new ArrayList<String>();
		usernames.add(username);
		ArrayList<TVUser> users = getUsersByUsername(usernames);
		
		return users.size() > 0 ? users.get(0) : new TVUser();
	}
	
	/**
	 * Get ArrayList of TVUser by username array list.
	 * @param usernames
	 * @return ArrayList<TVUser>
	 */
	public static ArrayList<TVUser> getUsersByUsername (ArrayList<String> usernames) {
		ArrayList<TVUser> users = new ArrayList<TVUser>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/member/retrieve/getprofiles";
		String bodyStr = usernames != null ? new JSONArray(usernames).toString() 
										   : new JSONArray().toString();
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyStr);
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++) {
					TVUser user = new TVUser(array.getJSONObject(i));
					users.add(user);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return users;
	}
	
	/**
	 * Get user list by nickname.
	 * @param nickname
	 * @return ArrayList<TVUser>
	 */
	public static ArrayList<TVUser> getUsersByNickname (String nickname) {
		ArrayList<TVUser> users = new ArrayList<TVUser>();
		JSONArray nicknameArray = new JSONArray();
		if (nickname != null)
			nicknameArray.put(nickname);
		
		// set http request URI
		//
		String URI = NetworkManager.getBaseUrl() + "/member/retrieve/nicknamesearch";
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, nicknameArray.toString());
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++) {
					TVUser user = new TVUser(array.getJSONObject(i));
					users.add(user);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return users;
	}
	
	/**
	 * Get user list by facebook ids.
	 * @param fbUsers
	 * @return ArrayList<TVUser>
	 */
	public static ArrayList<TVUser> getUsersByFacebookIds (ArrayList<FBUser> fbUsers) {
		ArrayList<TVUser> users = new ArrayList<TVUser>();
		
		JSONArray fbidArray = new JSONArray();
		for (FBUser fbUser:fbUsers)
			fbidArray.put(fbUser.getUid());
		
		// set http request URI
		//
		String URI = NetworkManager.getBaseUrl() + "/member/retrieve/getprofilesbyuid";
				
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, fbidArray.toString());
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++)
					users.add(new TVUser(array.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return users;
	}
	
	/**
	 * Get friend list by jid.
	 * @param jid
	 * @return ArrayList<TVUser>
	 */
	public static ArrayList<TVUser> getFriendsByJid (String jid) {
		ArrayList<TVUser> users = new ArrayList<TVUser>();
		
		// set http request URI
		//
		String URI = NetworkManager.getBaseUrl() + "/friend/" + jid + "/get";
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByGet(URI, null);
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++) {
					TVUser user = new TVUser(array.getJSONObject(i));
					users.add(user);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return users;
	}
}
