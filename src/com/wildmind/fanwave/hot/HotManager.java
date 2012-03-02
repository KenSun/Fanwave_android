package com.wildmind.fanwave.hot;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.vendor.VendorManager;


public class HotManager  {

	public static int Hot = 0;
	public static int Event = 1;
	
	/**
	 * Get now hot.
	 * @return ArrayList<TVHot>
	 */
	public static ArrayList<TVHot> getNowHot () {
		ArrayList<TVHot> hots = new ArrayList<TVHot>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/top/crowded/" + VendorManager.getCountry() + "/20";
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByGet(URI, null);
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++)
					hots.add(new TVHot(array.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return  hots;
	}
	
	/**
	 * Get upcoming hot.
	 * @return ArrayList<TVHot>
	 */
	public static ArrayList<TVHot> getUpcomingHot () {
		ArrayList<TVHot> hots = new ArrayList<TVHot>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/top/reminder/" + VendorManager.getCountry() + "/20";
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByGet(URI, null);
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++)
					hots.add(new TVHot(array.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return  hots;
	}
	
	/**
	 * Get weekly hot.
	 * @return ArrayList<TVHot>
	 */
	public static ArrayList<TVHot> getWeeklyHot () {
		ArrayList<TVHot> hots = new ArrayList<TVHot>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/top/mixedprograms/" + VendorManager.getCountry() + "/7/20";
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByGet(URI, null);
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++)
					hots.add(new TVHot(array.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return  hots;
	}
	
	/**
	 * Get hot Events
	 * @return List<TVHot>
	 */
	public static ArrayList<TVEvent> getHotEvents(){
		ArrayList<TVEvent> events = new ArrayList<TVEvent>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/top/event/" + VendorManager.getCountry() + "/20";
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByGet(URI, null);
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++)
					events.add(new TVEvent(array.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return  events;
	}
	
	/**
	 * Get Recommendation
	 * @return List<TVHot>
	 */
	public static ArrayList<TVHot> getRecommendation () {
		ArrayList<TVHot> recommendation = new ArrayList<TVHot>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/top/recommendation/" + VendorManager.getCountry() + "/20";
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByGet(URI, null);
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++)
					recommendation.add(new TVHot(array.getJSONObject(i)));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return recommendation;
	}
}
	
