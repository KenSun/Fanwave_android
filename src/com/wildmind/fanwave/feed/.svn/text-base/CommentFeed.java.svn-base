package com.wildmind.fanwave.feed;

import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.comment.Comment;
import com.wildmind.fanwave.program.TVProgram;

public class CommentFeed extends Feed {

	public static final String TYPE = "comment_feed";
	
	private Comment comment = null;
	
	// constructor
	//
	public CommentFeed(JSONObject obj) {
		try {
			// super class instance variables
			//
			this.type = TYPE;
			this.id 			= obj.has("uuid") ? obj.getString("uuid") : "";
			this.owner 			= obj.has("owner") ? obj.getString("owner") : "";
			this.nickname 		= obj.has("nickname") ? obj.getString("nickname") : "";
			this.created_time 	= obj.has("create_at") ? obj.getString("create_at") : "";
			
			String title 		= obj.has("title") ? obj.getString("title") : "";
			String subTitle 	= obj.has("sub_title") ? obj.getString("sub_title") : "";
			String pgid 		= obj.has("pgid") ? obj.getString("pgid") : "";
			String country 		= obj.has("country") ? obj.getString("country") : "";
			String channel 		= obj.has("channel") ? obj.getString("channel") : "";
			String startTime 	= obj.has("start_time") ? obj.getString("start_time") : "";
			String endTime 		= obj.has("end_time") ? obj.getString("end_time") : "";
			this.program = new TVProgram(title, subTitle, pgid, channel, "", country,
										 "", startTime, endTime, false, false, false, false);
			
			// extensive instance variables
			//
			this.comment = new Comment(obj);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// instance variable getters
	// 
	public Comment getComment () {
		return this.comment;
	}
	public TVProgram getProgram () {
		return this.program;
	}
}
