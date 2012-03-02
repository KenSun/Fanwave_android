package com.wildmind.fanwave.feed;

import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.program.TVProgram;

public class TopfanFeed extends Feed {

	public static final String 	TYPE = "topfan_feed";
	
	private String badge_id = "";
	
	// constructor
	//
	public TopfanFeed(JSONObject obj) {
		try {
			this.type = TYPE;
			this.id 			= obj.has("uuid") ? obj.getString("uuid") : "";
			this.owner 			= obj.has("owner") ? obj.getString("owner") : "";
			this.nickname 		= obj.has("nickname") ? obj.getString("nickname") : "";
			this.created_time 	= obj.has("create_at") ? obj.getString("create_at") : "";
			
			String pgid 	= obj.has("pgid") ? obj.getString("pgid") : "";
			String title = obj.has("title") ? obj.getString("title") : "";
			String country = obj.has("country") ? obj.getString("country") : "";
			this.program = new TVProgram(title, "", pgid, "", country, "", "", "", "", false, false, false, false);
			
			this.badge_id 	= obj.has("pgid") ? "topFan_" + pgid + "_1" : "";
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// instance variable getters
	//
	public String getBadgeId () {
		return this.badge_id;
	}
	public TVProgram getProgram () {
		return this.program;
	}
}
