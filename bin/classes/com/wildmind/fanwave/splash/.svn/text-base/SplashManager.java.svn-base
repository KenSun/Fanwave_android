package com.wildmind.fanwave.splash;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.network.NetworkManager;

public class SplashManager {
	
	private boolean more_splash = false;
	
	/**
	 * Whether if more splash exist.
	 * @return
	 */
	public boolean isMoreSplash () {
		return more_splash;
	}
	
	/**
	 * Get previous splash history with user from splash_id. If splash_id is null, get latest history.
	 * @param splash_id
	 * @param username
	 * @return
	 */
	public ArrayList<Splash> getHistoryWithUser (String splash_id, String username) {
		ArrayList<Splash> splashes = new ArrayList<Splash>();
		ArrayList<String> list = new ArrayList<String>();
		list.add(username);
		
		// set http request URI, body
		//
		String URI = splash_id == null 
				   ? NetworkManager.getBaseUrl() + "/splash/20/get"
				   : NetworkManager.getBaseUrl() + "/splash/" + splash_id + "/20/prev";
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json; charset=utf-8");
		String bodyStr = new JSONArray(list).toString();
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, headerMap, bodyStr);
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++) {
					JSONObject obj = array.getJSONObject(i);
					more_splash = obj.has("more") ? obj.getInt("more") == 1 : false;
					
					JSONArray splashArray = obj.has("splashes") ? obj.getJSONArray("splashes"): null;
					if (splashArray != null) {
						int jlen = splashArray.length();
						for (int j = 0; j < jlen; j++) {
							Splash splash = new Splash(splashArray.getJSONObject(j));
							splash.setSender(username);
							splashes.add(splash);	
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return splashes;
	}
	
	/**
	 * Get splash list.
	 * @return
	 */
	public static ArrayList<Splash> getSplashList () {
		ArrayList<Splash> splashes = new ArrayList<Splash>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/splash/userlist/get";

		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByGet(URI, null);
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++)
					splashes.add(new Splash(array.getJSONObject(i)));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return splashes;
	}
	
	public static String getHistoryNotificationId (String fromUser) {
		String id = null;
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/notification/getlatest";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("fromuser", fromUser);
		bodyMap.put("touser", AccountManager.getCurrentUser().getUsername());
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		if (response.get("code").equals("200")) {
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				id = obj.has("uuid") ? obj.getString("uuid") : null;
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return id;
	}
	
	/**
	 * Splash a user.
	 * @param username
	 * @return
	 */
	public static boolean splashUser (String username, String message) {
		ArrayList<String> usernames = new ArrayList<String>();
		usernames.add(username);
		return splashUsers(usernames, message);
	}
	
	/**
	 * Splash users.
	 * @param usernames
	 * @return
	 */
	public static boolean splashUsers (ArrayList<String> usernames, String message) {
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/notification/splash";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("body", message);
		bodyMap.put("nickname", AccountManager.getCurrentUser().getNickname());
		bodyMap.put("usernames", new JSONArray(usernames).toString());
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		return response.get("code").equals("200");
	}
	
	/**
	 * Splash users.
	 * @param usernames
	 * @return
	 */
	public static boolean splashFollowers (ArrayList<String> usernames, String message) {
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/notification/topsplash";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("body", message);
		bodyMap.put("nickname", AccountManager.getCurrentUser().getNickname());
		bodyMap.put("usernames", new JSONArray(usernames).toString());
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		return response.get("code").equals("200");
	}
}
