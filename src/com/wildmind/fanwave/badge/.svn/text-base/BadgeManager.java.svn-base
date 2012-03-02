package com.wildmind.fanwave.badge;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.user.TVUser;

public class BadgeManager {

	/**
	 * Instance variables
	 */
	private String username = "";
	private ArrayList<TVBadge> system_badges = new ArrayList<TVBadge>();
	private ArrayList<TVBadge> event_badges = new ArrayList<TVBadge>();
	private ArrayList<TVBadge> program_badges = new ArrayList<TVBadge>();
	
	/**
	 * Constructor
	 * @param username
	 */
	public BadgeManager (String username) {
		this.username = username;
	}
	
	// getters
	//
	public ArrayList<TVBadge> getSystemBadges () {
		return system_badges;
	}
	
	public ArrayList<TVBadge> getEventBadges () {
		return event_badges;
	}
	
	public ArrayList<TVBadge> getProgramBadges () {
		return program_badges;
	}
	
	/**
	 * Get badge summary of user.
	 */
	public void getBadgeSummary () {
		// set http request URI, header, body
		//
		String URI = NetworkManager.getBaseUrl() + "/badge/summary/get";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("username",this.username);
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		if (response.get("code").equals("200")) {
			try {
				JSONObject data = new JSONObject(response.get("data"));
				parseSystemBadges(data.getJSONArray("system"));
				parseEventBadges(data.getJSONArray("event"));
				parseProgramBadges(data.getJSONArray("program"));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Parse system badges.
	 * @param array
	 */
	private void parseSystemBadges (JSONArray array) {
		int len = array.length();
		try {
			for (int i = 0; i < len; i++)
				system_badges.add(new TVBadge (array.getJSONObject(i)));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parse event badges.
	 * @param array
	 */
	private void parseEventBadges (JSONArray array) {
		int len = array.length();
		try {
			for (int i = 0; i < len; i++)
				event_badges.add(new TVBadge (array.getJSONObject(i)));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Parse program badges.
	 * @param array
	 */
	private void parseProgramBadges (JSONArray array) {
		int len = array.length();
		try {
			for (int i = 0; i < len; i++)
				program_badges.add(new TVBadge (array.getJSONObject(i)));
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get details of type of user.
	 * @param type
	 * @param username
	 * @return
	 */
	public static ArrayList<TVBadge> getTypeDetailBadges (String type, String username) {
		ArrayList<TVBadge> badges = new ArrayList<TVBadge>();
		
		// set http request URI, header, body
		//
		String URI = NetworkManager.getBaseUrl() + "/badge/detail/" + type + "/get";
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
					badges.add(new TVBadge(array.getJSONObject(i)));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return badges;
	}
	
	/**
	 * Get titles of program level.
	 * @param level
	 * @param username
	 * @return
	 */
	public static ArrayList<String> getProgramLevelTitles (int level, String username) {
		ArrayList<String> titles = new ArrayList<String>();
		
		// set http request URI, header, body
		//
		String URI = NetworkManager.getBaseUrl() + "/badge/get/program/title/byLevel";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("username", username);
		bodyMap.put("level", String.valueOf(level));
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++)
					titles.add(array.getString(i));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return titles;
	}
	
	/**
	 * Get selected badge ids.
	 * @param users
	 * @return HashMap<String, String>	username : badgeId
	 */
	public static HashMap<String, String> getSelectedBadges (ArrayList<TVUser> users) {
		HashMap<String, String> badgeIds = new HashMap<String, String>();
		ArrayList<String> list = new ArrayList<String>();
		for (TVUser user:users)
			list.add(user.getUsername());
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/selected/badges";
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
					String username = obj.has("username") ? obj.getString("username") : null;
					String badgeId = obj.has("badgeID") ? obj.getString("badgeID") : null;
					if (username != null && badgeId != null)
						badgeIds.put(username, badgeId);
				}	
					
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return badgeIds;
	}
	
	/**
	 * Get badge info.
	 * @param badge_id
	 * @return TVBadge
	 */
	public static TVBadge getBadge (String badge_id) {
		TVBadge badge = null;
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/badge/info/" + badge_id;
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, "");
		
		if (response.get("code").equals("200")) {
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				badge = new TVBadge(obj);
				badge.setId(badge_id);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return badge;
	}
	
	/**
	 * Set selected badge.
	 * @param username
	 * @param badge_id
	 * @return
	 */
	public static boolean setSelectedBadge (String username, String badge_id) {
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/badge/post/user/select/badge";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("username", username);
		bodyMap.put("badgeID", badge_id);
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);

		if (response.get("code").equals("200")) 
			AccountManager.getCurrentUser().setBadgeId(badge_id);
		
		return response.get("code").equals("200");
	}
}
