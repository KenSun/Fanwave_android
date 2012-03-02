package com.wildmind.fanwave.user;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class TVUserScore implements Parcelable {

	private int follow = 0;
	private int rate = 0;
	private int friend = 0;
	private int friend_invite = 0;
	private int friend_accept = 0;
	private int login = 0;
	private int like = 0;
	private int liked = 0;
	private int dislike = 0;
	private int disliked = 0;
	private int comment = 0;
	private int wavein = 0;
	private int badge = 0;
	private int points = 0;
	
	// constructor
	//
	public TVUserScore () {}
	
	public TVUserScore (JSONObject obj) {
		try {
			this.follow 		= obj.has("followNum") ? obj.getInt("followNum") : 0;
			this.rate 			= obj.has("rateNum") ? obj.getInt("rateNum") : 0;
			this.friend 		= obj.has("friendNum") ? obj.getInt("friendNum") : 0;
			this.friend_invite 	= obj.has("friendInviteNum") ? obj.getInt("friendInviteNum") : 0;
			this.friend_accept 	= obj.has("friendAcceptNum") ? obj.getInt("friendAcceptNum") : 0;
			this.login 			= obj.has("loginNum") ? obj.getInt("loginNum") : 0;
			this.like 			= obj.has("likeNum") ? obj.getInt("likeNum") : 0;
			this.liked 			= obj.has("likedNum") ? obj.getInt("likedNum") : 0;
			this.dislike 		= obj.has("dislikeNum") ? obj.getInt("dislikeNum") : 0;
			this.disliked 		= obj.has("dislikedNum") ? obj.getInt("dislikedNum") : 0;
			this.comment 		= obj.has("commentNum") ? obj.getInt("commentNum") : 0;
			this.wavein 		= obj.has("checkInNum") ? obj.getInt("checkInNum") : 0;
			this.badge 			= obj.has("badgeNum") ? obj.getInt("badgeNum") : 0;
			this.points 		= obj.has("points") ? obj.getInt("points") : 0;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public TVUserScore (TVUserScore scores) {
		this.follow 		= scores.getFollow();
		this.rate 			= scores.getRate();
		this.friend 		= scores.getFriend();
		this.friend_invite 	= scores.getFriendInvite();
		this.friend_accept 	= scores.getFriendAccept();
		this.login 			= scores.getLogin();
		this.like 			= scores.getLike();
		this.liked 			= scores.getLiked();
		this.dislike 		= scores.getDislike();
		this.disliked 		= scores.getDisliked();
		this.comment 		= scores.getComment();
		this.wavein 		= scores.getWavein();
		this.badge 			= scores.getBadge();
		this.points 		= scores.getPoints();
	}
	
	public TVUserScore (int follow, int rate, int friend, int invite, int accept, int login, int like, int liked, int dislike,
						int disliked, int comment, int wavein, int badge, int points) {
		this.follow = follow;
		this.rate = rate;
		this.friend = friend;
		this.friend_invite = invite;
		this.friend_accept = accept;
		this.login = login;
		this.like = like;
		this.liked = liked;
		this.dislike = dislike;
		this.disliked = disliked;
		this.comment = comment;
		this.wavein = wavein;
		this.badge = badge;
		this.points = points;
	}

	// instance variable getters
	//
	public int getFollow() {
		return follow;
	}
	public int getRate() {
		return rate;
	}
	public int getFriend() {
		return friend;
	}
	public int getFriendInvite() {
		return friend_invite;
	}
	public int getFriendAccept() {
		return friend_accept;
	}
	public int getLogin() {
		return login;
	}
	public int getLike() {
		return like;
	}
	public int getLiked() {
		return liked;
	}
	public int getDislike() {
		return dislike;
	}
	public int getDisliked() {
		return disliked;
	}
	public int getComment() {
		return comment;
	}
	public int getWavein() {
		return wavein;
	}
	public int getBadge() {
		return badge;
	}
	public int getPoints() {
		return points;
	}

	// instance variable setters
	//
	public void setFollow(int follow) {
		this.follow = follow;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public void setFriend(int friend) {
		this.friend = friend;
	}
	public void setFriendInvite(int friend_invite) {
		this.friend_invite = friend_invite;
	}
	public void setFriendAccept(int friend_accept) {
		this.friend_accept = friend_accept;
	}
	public void setLogin(int login) {
		this.login = login;
	}
	public void setLike(int like) {
		this.like = like;
	}
	public void setLiked(int liked) {
		this.liked = liked;
	}
	public void setDislike(int dislike) {
		this.dislike = dislike;
	}
	public void setDisliked(int disliked) {
		this.disliked = disliked;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public void setWavein(int wavein) {
		this.wavein = wavein;
	}
	public void setBadge(int badge) {
		this.badge = badge;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	
	
	/**
	 * Parcelable Implementation Methods
	 */
//	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

//	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		
		int[] intArgs = { this.follow, this.rate, this.friend, this.friend_invite, this.friend_accept, this.login, this.like,
						  this.liked, this.dislike, this.disliked, this.comment, this.wavein, this.badge, this.points };
		out.writeIntArray(intArgs);
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TVUserScore> CREATOR = new Parcelable.Creator<TVUserScore>() {
        public TVUserScore createFromParcel(Parcel in) {
            return new TVUserScore(in);
        }

        public TVUserScore[] newArray(int size) {
            return new TVUserScore[size];
        }
    };
    
    // constructor that takes a Parcel and gives you an object populated with it's values
    private TVUserScore(Parcel in) {
    	int[] intArgs = {0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    	in.readIntArray(intArgs);
    	
    	this.follow 		= intArgs[0];
    	this.rate			= intArgs[1];
    	this.friend 		= intArgs[2];
    	this.friend_invite 	= intArgs[3];
    	this.friend_accept	= intArgs[4];
    	this.login			= intArgs[5];
    	this.like			= intArgs[6];
    	this.liked			= intArgs[7];
    	this.dislike		= intArgs[8];
    	this.disliked		= intArgs[9];
    	this.comment		= intArgs[10];
    	this.wavein			= intArgs[11];
    	this.badge			= intArgs[12];
    	this.points			= intArgs[13];
    }
}
