package com.wildmind.fanwave.feed;

import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.program.TVProgram;

public class GainBadgeFeed extends Feed {

	public static final String TYPE = "gain_badge_feed";
	
	private String badge_id = "";
	private String badge_title = "";
	
	public GainBadgeFeed(JSONObject obj) {
		try {
			this.type = TYPE;
			this.id 			= obj.has("uuid") ? obj.getString("uuid") : "";
			this.owner 			= obj.has("owner") ? obj.getString("owner") : "";
			this.nickname 		= obj.has("nickname") ? obj.getString("nickname") : "";
			this.created_time 	= obj.has("create_at") ? obj.getString("create_at") : "";
			
			String title = obj.has("programTitle") ? obj.getString("programTitle") : "";
			String pgid = obj.has("pgid") ? obj.getString("pgid") : "";
			String country = obj.has("country") ? obj.getString("country") : "";
			this.program = new TVProgram(title, "", pgid, "", country, "", "", "", "", false, false, false, false);
			
			this.badge_id = obj.has("badgeID") ? obj.getString("badgeID") : "";
			this.badge_title = obj.has("title") ? obj.getString("title") : "";
			
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
	public String getBadgeTitle () {
		return this.badge_title;
	}
	public TVProgram getProgram () {
		return this.program;
	}
}
