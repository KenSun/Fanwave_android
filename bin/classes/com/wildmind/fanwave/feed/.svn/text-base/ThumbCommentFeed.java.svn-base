package com.wildmind.fanwave.feed;

import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.comment.Comment;
import com.wildmind.fanwave.program.TVProgram;

public class ThumbCommentFeed extends Feed {

	public static final String TYPE = "thumb_comment_feed";
	
	private boolean is_like = false;
	private Comment comment = null;
	
	// constructor
	//
	public ThumbCommentFeed(JSONObject obj, boolean is_like) {
		this.is_like = is_like;
		try {
			this.type = TYPE;
			this.id 			= obj.has("uuid") ? obj.getString("uuid") : "";
			this.owner 			= obj.has("owner") ? obj.getString("owner") : "";
			this.nickname 		= obj.has("nickname") ? obj.getString("nickname") : "";
			this.created_time 	= obj.has("create_at") ? obj.getString("create_at") : "";
			
			JSONObject comment_obj = obj.has("comment") ? obj.getJSONObject("comment") : null;
			if (comment_obj != null) {
				String title 		= comment_obj.has("title") ? comment_obj.getString("title") : "";
				String subTitle 	= comment_obj.has("sub_title") ? comment_obj.getString("sub_title") : "";
				String pgid 		= comment_obj.has("pgid") ? comment_obj.getString("pgid") : "";
				String country 		= comment_obj.has("country") ? comment_obj.getString("country") : "";
				String channel 		= comment_obj.has("channel") ? comment_obj.getString("channel") : "";
				String startTime 	= comment_obj.has("start_time") ? comment_obj.getString("start_time") : "";
				String endTime 		= comment_obj.has("end_time") ? comment_obj.getString("end_time") : "";
				this.program = new TVProgram(title, subTitle, pgid, channel, "", country, "", startTime, endTime, false, false, false, false);
				
				this.comment = new Comment (comment_obj);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// instance variable getters
	//
	public boolean isLike() {
		return is_like;
	}

	public Comment getComment() {
		return comment;
	}

	public TVProgram getProgram() {
		return program;
	}

}
