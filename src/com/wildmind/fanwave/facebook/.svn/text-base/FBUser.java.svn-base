package com.wildmind.fanwave.facebook;

import org.json.JSONException;
import org.json.JSONObject;

public class FBUser {

	private String uid = "";
	private String name = "";
	private String pic_square = "";
	
	/**
	 * Constructor
	 * @param obj
	 */
	public FBUser (JSONObject obj) {
		try {
			this.uid 		= obj.has("uid") ? obj.getString("uid") : "";
			this.name 		= obj.has("name") ? obj.getString("name") : "";
			this.pic_square = obj.has("pic_square") ? obj.getString("pic_square") : "";
			
		} catch (JSONException e) {
			e.printStackTrace();
		}	
	}
	
	// getters
	//
	public String getUid () {
		return uid;
	}
	public String getName () {
		return name;
	}
	public String getPicSquare () {
		return pic_square;
	}
}
