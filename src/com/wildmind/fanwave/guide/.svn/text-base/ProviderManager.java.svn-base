package com.wildmind.fanwave.guide;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.wildmind.fanwave.network.NetworkManager;


public class ProviderManager  {

	/**
	 * Get Region List.
	 * @return 
	 */
	public static ArrayList<HashMap<String, String>> getSupportCountry () {
		ArrayList<HashMap<String, String>> datalist = new ArrayList<HashMap<String, String>>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/supportcountry/getall";
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByGet(URI, null);
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				if (len != 0)
				for (int i = 0; i < len; i++){
					JSONObject obj = array.getJSONObject(i);
					HashMap<String, String> hashmap = new HashMap<String, String>();
					hashmap.put("countryCode", obj.getString("countryCode"));
					hashmap.put("countryFull", obj.getString("countryFull"));
					datalist.add(hashmap);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return  datalist;
	}
	
	/**
	 * Get City List.
	 * @return 
	 */
	public static ArrayList<HashMap<String, String>> getRegionName(String country) {
		
		ArrayList<HashMap<String, String>> datalist = new ArrayList<HashMap<String, String>>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/region/" + country + "/get";
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByGet(URI, null);
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				if (len != 0)
					for (int i = 0; i < len; i++){
						JSONObject obj = array.getJSONObject(i);
						HashMap<String, String> hashmap = new HashMap<String, String>();
						hashmap.put("regionName", obj.getString("regionName"));
						hashmap.put("zip", obj.getString("zip"));
						datalist.add(hashmap);
					}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return  datalist;
	}
	
	
	/**
	 * Get Provider List.
	 * @return 
	 */
	public static ArrayList<HashMap<String, String>> getProvider(String postcode, String country) {
		
		ArrayList<HashMap<String, String>> datalist = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("country", country);
		bodyMap.put("postcode", postcode);
		// set http request URI, body
		//
		HashMap<String, String> response = NetworkManager.connectByPost(NetworkManager.getMsoListUrl(), null, bodyMap);		

		if (response.get("code").equals("200")) {
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				JSONArray array = obj.getJSONArray("providers");
				int len = array.length();
				if (len != 0)
					for (int i = 0; i < len; i++){
						HashMap<String, String> hashmap = new HashMap<String, String>();
						JSONObject objfromarray = array.getJSONObject(i);
						hashmap.put("msoid",		objfromarray.getString("msoid") );
						hashmap.put("msoname",		objfromarray.getString("msoname") );
						hashmap.put("msolastupdate",objfromarray.getString("msolastupdate"));
						datalist.add(hashmap);
					}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return  datalist;
	}

}
	
