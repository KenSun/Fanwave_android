package com.wildmind.fanwave.feed;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.network.NetworkManager;

public class FeedManager {

	private static boolean more_public_feed = false;
	private static boolean more_friend_feed = false;
	private static boolean more_follow_feed = false;
	private boolean more_user_feed = false;
	
	/**
	 * Clear records of more feed.
	 */
	public static void clear () {
		more_public_feed = false;
		more_friend_feed = false;
		more_follow_feed = false;
	}
	
	/**
	 * Check if more public feeds exist.
	 * @return boolean
	 */
	public static boolean isMorePublic () {
		return more_public_feed;
	}
	
	/**
	 * Check if more friend feeds exist.
	 * @return
	 */
	public static boolean isMoreFriend () {
		return more_friend_feed;
	}
	
	/**
	 * Check if more follow feeds exist.
	 * @return
	 */
	public static boolean isMoreFollow () {
		return more_follow_feed;
	}
	
	/**
	 * Check if more user feeds exist.
	 * @return
	 */
	public boolean isMoreUser () {
		return this.more_user_feed;
	}
	
	/**
	 * Get previous public feeds from feed_id. If feed_id is null, get latest feeds.
	 * @param feed_id
	 * @return ArrayList<Feed>
	 */
	public static ArrayList<Feed> getPublicFeed (String feed_id) {
		ArrayList<Feed> feeds = new ArrayList<Feed>();
		// set http request URI, body
		//
		String URI = feed_id == null
				   ? NetworkManager.getBaseUrl() + "/feed/all/get"
				   : NetworkManager.getBaseUrl() + "/feed/all/" + feed_id + "/prev";
				
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByGet(URI, null);
		
		if (response.get("code").equals("200")) {
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				more_public_feed = obj.has("more") ? obj.getInt("more") == 1 : false;

				if (obj.has("feeds")) {
					feeds = FeedParser.parse(obj.getJSONArray("feeds"));
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return feeds;
	}
	
	/**
	 * Get previous friend feeds from feed_id. If feed_id is null, get latest feeds.
	 * @param feed_id
	 * @return ArrayList<Feed>
	 */
	public static ArrayList<Feed> getFriendFeed (String feed_id) {
		ArrayList<Feed> feeds = new ArrayList<Feed>();
		// set http request URI, body
		//
		String URI = feed_id == null
				   ? NetworkManager.getBaseUrl() + "/feed/friend/get"
				   : NetworkManager.getBaseUrl() + "/feed/friend/" + feed_id + "/prev";
				
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByGet(URI, null);
		
		if (response.get("code").equals("200")) {
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				more_friend_feed = obj.has("more") ? obj.getInt("more") == 1 : false;
				
				JSONArray array = obj.has("feeds") ? obj.getJSONArray("feeds") : null;
				if (array != null) {
					feeds = FeedParser.parse(array);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return feeds;
	}
	
	/**
	 * Get previous follow feeds with username from feed_id. If feed_id is null, get latest feeds.
	 * @param feed_id
	 * @return ArrayList<Feed>
	 */
	public static ArrayList<Feed> getFollowFeed (String feed_id, String username) {
		ArrayList<Feed> feeds = new ArrayList<Feed>();
		// set http request URI, body
		//
		String URI = feed_id == null
				   ? NetworkManager.getBaseUrl() + "/follow/programfeed/20/get"
				   : NetworkManager.getBaseUrl() + "/follow/programfeed/" + feed_id + "/20/next";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("username", username);
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		if (response.get("code").equals("200")) {
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				more_follow_feed = obj.has("more") ? obj.getInt("more") == 1 : false;
				
				JSONArray array = obj.has("feeds") ? obj.getJSONArray("feeds") : null;
				if (array != null) {
					feeds = FeedParser.parse(array);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return feeds;
	}
	
	/**
	 * Get previous user feeds with username from feed_id. If feed_id is null, get latest feeds.
	 * @param feed_id
	 * @return ArrayList<Feed>
	 */
	public ArrayList<Feed> getUserFeed (String feed_id, String username) {
		ArrayList<Feed> feeds = new ArrayList<Feed>();
		// set http request URI, body
		//
		String URI = feed_id == null
				   ? NetworkManager.getBaseUrl() + "/feed/user/get"
				   : NetworkManager.getBaseUrl() + "/feed/user/" + feed_id + "/prev";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("username", username);
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		if (response.get("code").equals("200")) {
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				this.more_user_feed = obj.has("more") ? obj.getInt("more") == 1 : false;
				
				JSONArray array = obj.has("feeds") ? obj.getJSONArray("feeds") : null;
				if (array != null) {
					feeds = FeedParser.parse(array);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return feeds;
	}
}
