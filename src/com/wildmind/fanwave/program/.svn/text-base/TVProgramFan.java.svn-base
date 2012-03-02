package com.wildmind.fanwave.program;

import org.json.JSONException;
import org.json.JSONObject;

public class TVProgramFan {

	private String 	username = "";
	private String 	nickname = "";
	private int		point = 0;
	private int		comment_count = 0;
	private int		wavein_count = 0;
	private int		like_count = 0;
	private int		liked_count = 0;
	private int		dislike_count = 0;
	private int		disliked_count = 0;
	private int		rank = 0;
	private boolean	rated = false;
	private boolean	following = false;
	
	// constructor
	//
	public TVProgramFan (JSONObject obj) {
		try {
			this.username 		= obj.has("username") ? obj.getString("username") : "";
			this.nickname 		= obj.has("nickname") ? obj.getString("nickname") : "";
			this.point 			= obj.has("points") ? Integer.parseInt(obj.getString("points")) : 0;
			this.comment_count	= obj.has("commentNum") ? Integer.parseInt(obj.getString("commentNum")) : 0;
			this.wavein_count 	= obj.has("checkInNum") ? Integer.parseInt(obj.getString("checkInNum")) : 0;
			this.like_count 	= obj.has("likeNum") ? Integer.parseInt(obj.getString("likeNum")) : 0;
			this.liked_count	= obj.has("likedNum") ? Integer.parseInt(obj.getString("likedNum")) : 0;
			this.dislike_count 	= obj.has("dislikeNum") ? Integer.parseInt(obj.getString("dislikeNum")) : 0;
			this.disliked_count = obj.has("dislikedNum") ? Integer.parseInt(obj.getString("dislikedNum")) : 0;
			this.rank 			= obj.has("rank") ? Integer.parseInt(obj.getString("rank")) : 0;
			this.rated 			= obj.has("rateNum") ? obj.getString("rateNum").equals("1") : false;
			this.following 		= obj.has("followNum") ? obj.getString("followNum").equals("1") : false;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	// instance variable getters
	//
	public String getUsername() {
		return username;
	}
	public String getNickname() {
		return nickname;
	}
	public int getPoint() {
		return point;
	}
	public int getCommentCount() {
		return comment_count;
	}
	public int getWaveinCount() {
		return wavein_count;
	}
	public int getLikeCount() {
		return like_count;
	}
	public int getLikedCount() {
		return liked_count;
	}
	public int getDislikeCount() {
		return dislike_count;
	}
	public int getDislikedCount() {
		return disliked_count;
	}
	public int getRank() {
		return rank;
	}
	public boolean isRated() {
		return rated;
	}
	public boolean isFollowing() {
		return following;
	}
	
	// instance variable setters
	//
	public void setUsername(String username) {
		this.username = username;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public void setCommentCount(int comment_count) {
		this.comment_count = comment_count;
	}
	public void setWaveinCount(int wavein_count) {
		this.wavein_count = wavein_count;
	}
	public void setLikeCount(int like_count) {
		this.like_count = like_count;
	}
	public void setLikedCount(int liked_count) {
		this.liked_count = liked_count;
	}
	public void setDislikeCount(int dislike_count) {
		this.dislike_count = dislike_count;
	}
	public void setDislikedCount(int disliked_count) {
		this.disliked_count = disliked_count;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public void setRated(boolean rated) {
		this.rated = rated;
	}
	public void setFollowing(boolean following) {
		this.following = following;
	}
}
