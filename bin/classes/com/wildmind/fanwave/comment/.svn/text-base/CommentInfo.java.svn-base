package com.wildmind.fanwave.comment;

import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.program.TVProgram;

public class CommentInfo {

	private String last_like_user = "";
	private String last_like_nick = "";
	private boolean is_rated = false;
	private Comment comment = null;
	private TVProgram program = null;
	
	public CommentInfo () {
		this.comment = new Comment();
		this.program = new TVProgram();
	};
	
	/**
	 * Constructor
	 * @param obj
	 */
	public CommentInfo (JSONObject obj) {
		try {
			this.last_like_user = obj.has("last_likeuser") ? obj.getString("last_likeuser") : "";
			this.last_like_nick = obj.has("last_likeuser_nickname") ? obj.getString("last_likeuser_nickname") : "";
			this.is_rated		= obj.has("isRated") ? obj.getString("isRated").equals("1") ? true : false : false;
			this.comment = new Comment(obj);
			
			// set program
			//
			TVProgram tp = new TVProgram(obj);
			tp.setChannelCode(obj.has("channel") ? obj.getString("channel") : tp.getChannelCode());
			tp.setCountry(obj.has("country") ? obj.getString("country") : tp.getCountry());
			tp.setStartTime(obj.has("star_time") ? obj.getString("star_time") : tp.getStartTime());
			tp.setEndTime(obj.has("end_time") ? obj.getString("end_time") : tp.getEndTime());
			tp.setPgid(obj.has("pgid") ? obj.getString("pgid") : tp.getPgid());
			tp.setTitle(obj.has("title") ? obj.getString("title") : tp.getTitle());
			tp.setSubTitle(obj.has("sub_title") ? obj.getString("sub_title") : tp.getSubTitle());
			this.program = tp;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// getters
	//
	public String getLastLikeUser() {
		return last_like_user;
	}
	public String getLastLikeNick() {
		return last_like_nick;
	}
	public boolean isRated() {
		return is_rated;
	}
	public Comment getComment() {
		return comment;
	}
	public TVProgram getProgram() {
		return program;
	}

	// setters
	//
	public void setLastLikeUser(String last_like_user) {
		this.last_like_user = last_like_user;
	}
	public void setLastLikeNick(String last_like_nick) {
		this.last_like_nick = last_like_nick;
	}
	public void setRated(boolean is_rated) {
		this.is_rated = is_rated;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	public void setProgram(TVProgram program) {
		this.program = program;
	}
}
