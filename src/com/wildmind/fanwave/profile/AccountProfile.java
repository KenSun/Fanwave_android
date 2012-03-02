package com.wildmind.fanwave.profile;

import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.user.TVUserExtraInfo;
import com.wildmind.fanwave.user.TVUserPrivacy;
import com.wildmind.fanwave.user.TVUserScore;

import android.content.Context;

public class AccountProfile {

	private Context context;
	
	// constructor
	//
	public AccountProfile (Context _context) {
		this.context = _context;
	}
	
	// profile getters
	//
	public TVUser getUser () {
		TVUser user = new TVUser();
		user.setUsername(getUsername());
		user.setPassword(getPassword());
		user.setNickname(getNickname());
		user.setEmail(getEmail());
		user.setJid(getJid());
		user.setJidPassword(getJidPassword());
		user.setFbid(getFbid());
		user.setBadgeId(getBadgeId());
		user.setPrivacy(getPrivacy());
		user.setExtraInfo(getExtraInfo());
		user.setScores(getScores());

		return user;
	}
	public String getUsername () {
		return context.getSharedPreferences("Account", 0).getString("username", "");
	}
	public String getPassword () {
		return context.getSharedPreferences("Account", 0).getString("password", "");
	}
	public String getNickname () {
		return context.getSharedPreferences("Account", 0).getString("nickname", "");
	}
	public String getEmail () {
		return context.getSharedPreferences("Account", 0).getString("email", "");
	}
	public String getJid () {
		return context.getSharedPreferences("Account", 0).getString("jid", "");
	}
	public String getJidPassword () {
		return context.getSharedPreferences("Account", 0).getString("jid_password", "");
	}
	public String getFbid () {
		return context.getSharedPreferences("Account", 0).getString("fbid", "");
	}
	public String getBadgeId () {
		return context.getSharedPreferences("Account", 0).getString("badge_id", "");
	}
	public boolean isFirstLogin () {
		return context.getSharedPreferences("Account", 0).getBoolean("first_login", false);
	}
	public TVUserPrivacy getPrivacy () {
		String splash 	=  context.getSharedPreferences("Account_Privacy", 0).getString("splash", TVUserPrivacy.PRIVACY_FRIEND);
		String reminder =  context.getSharedPreferences("Account_Privacy", 0).getString("reminder", TVUserPrivacy.PRIVACY_FRIEND);
		String email 	=  context.getSharedPreferences("Account_Privacy", 0).getString("email", TVUserPrivacy.PRIVACY_FRIEND);
		String gender 	=  context.getSharedPreferences("Account_Privacy", 0).getString("gender", TVUserPrivacy.PRIVACY_FRIEND);
		String birth 	=  context.getSharedPreferences("Account_Privacy", 0).getString("birth", TVUserPrivacy.PRIVACY_NO_YEAR);
		String facebook =  context.getSharedPreferences("Account_Privacy", 0).getString("facebook", TVUserPrivacy.PRIVACY_FRIEND);
		return new TVUserPrivacy(splash, reminder, email, gender, birth, facebook);
	}
	public TVUserExtraInfo getExtraInfo () {
		String mood 		= context.getSharedPreferences("Account_Extra_Info", 0).getString("mood", "");
		String gender 		= context.getSharedPreferences("Account_Extra_Info", 0).getString("gender", "");
		String birthday		= context.getSharedPreferences("Account_Extra_Info", 0).getString("birthday", "");
		String bio 			= context.getSharedPreferences("Account_Extra_Info", 0).getString("bio", "");
		String website 		= context.getSharedPreferences("Account_Extra_Info", 0).getString("website", "");
		String blog 		= context.getSharedPreferences("Account_Extra_Info", 0).getString("blog", "");
		String youtube 		= context.getSharedPreferences("Account_Extra_Info", 0).getString("youtube", "");
		String city 		= context.getSharedPreferences("Account_Extra_Info", 0).getString("location_city", "");
		String latitude 	= context.getSharedPreferences("Account_Extra_Info", 0).getString("location_latitude", "");
		String longitude 	= context.getSharedPreferences("Account_Extra_Info", 0).getString("location_longitude", "");
		return new TVUserExtraInfo(mood, gender, birthday, bio, blog, website, youtube, city, latitude, longitude);
	}
	public TVUserScore getScores () {
		int follow 		= context.getSharedPreferences("Account_Score", 0).getInt("follow", 0);
		int rate 		= context.getSharedPreferences("Account_Score", 0).getInt("rate", 0);
		int friend 		= context.getSharedPreferences("Account_Score", 0).getInt("friend", 0);
		int invite 		= context.getSharedPreferences("Account_Score", 0).getInt("friend_invite", 0);
		int accept 		= context.getSharedPreferences("Account_Score", 0).getInt("friend_accpet", 0);
		int login 		= context.getSharedPreferences("Account_Score", 0).getInt("login", 0);
		int like 		= context.getSharedPreferences("Account_Score", 0).getInt("like", 0);
		int liked 		= context.getSharedPreferences("Account_Score", 0).getInt("liked", 0);
		int dislike  	= context.getSharedPreferences("Account_Score", 0).getInt("dislike", 0);
		int disliked 	= context.getSharedPreferences("Account_Score", 0).getInt("disliked", 0);
		int comment 	= context.getSharedPreferences("Account_Score", 0).getInt("comment", 0);
		int wavein 		= context.getSharedPreferences("Account_Score", 0).getInt("wavein", 0);
		int badge 		= context.getSharedPreferences("Account_Score", 0).getInt("badge", 0);
		int points 		= context.getSharedPreferences("Account_Score", 0).getInt("points", 0);
		return new TVUserScore(follow, rate, friend, invite, accept, login, like, liked, dislike, disliked, comment, wavein, badge, points);
	}
	
	// profile setters
	//
	public void setUsername (String username) {
		context.getSharedPreferences("Account", 0).edit().putString("username", username).commit();
	}
	public void setPassword (String password) {
		context.getSharedPreferences("Account", 0).edit().putString("password", password).commit();
	}
	public void setNickname (String nickname) {
		context.getSharedPreferences("Account", 0).edit().putString("nickname", nickname).commit();
	}
	public void setEmail (String email) {
		context.getSharedPreferences("Account", 0).edit().putString("email", email).commit();
	}
	public void setJid (String jid) {
		context.getSharedPreferences("Account", 0).edit().putString("jid", jid).commit();
	}
	public void setJidPassword (String jid_password) {
		context.getSharedPreferences("Account", 0).edit().putString("jid_password", jid_password).commit();
	}
	public void setFbid (String fbid) {
		context.getSharedPreferences("Account", 0).edit().putString("fbid", fbid).commit();
	}
	public void setBadgeId (String badge_id) {
		context.getSharedPreferences("Account", 0).edit().putString("badge_id", badge_id).commit();
	}
	public void setFirstLogin (boolean first_login) {
		context.getSharedPreferences("Account", 0).edit().putBoolean("first_login", first_login).commit();
	}
	public void setPrivacy (TVUserPrivacy privacy) {
		context.getSharedPreferences("Account_Privacy", 0).edit().putString("splash", privacy.getSplash()).commit();
		context.getSharedPreferences("Account_Privacy", 0).edit().putString("reminder", privacy.getReminder()).commit();
		context.getSharedPreferences("Account_Privacy", 0).edit().putString("email", privacy.getEmail()).commit();
		context.getSharedPreferences("Account_Privacy", 0).edit().putString("gender", privacy.getGender()).commit();
		context.getSharedPreferences("Account_Privacy", 0).edit().putString("birth", privacy.getBirth()).commit();
		context.getSharedPreferences("Account_Privacy", 0).edit().putString("facebook", privacy.getFacebook()).commit();
	}
	public void setExtraInfo (TVUserExtraInfo extra_info) {
		context.getSharedPreferences("Account_Extra_Info", 0).edit().putString("mood", extra_info.getMood()).commit();
		context.getSharedPreferences("Account_Extra_Info", 0).edit().putString("gender", extra_info.getGender()).commit();
		context.getSharedPreferences("Account_Extra_Info", 0).edit().putString("birthday", extra_info.getBirthday()).commit();
		context.getSharedPreferences("Account_Extra_Info", 0).edit().putString("bio", extra_info.getBio()).commit();
		context.getSharedPreferences("Account_Extra_Info", 0).edit().putString("website", extra_info.getWebsite()).commit();
		context.getSharedPreferences("Account_Extra_Info", 0).edit().putString("blog", extra_info.getBlog()).commit();
		context.getSharedPreferences("Account_Extra_Info", 0).edit().putString("youtube", extra_info.getYoutube()).commit();
		context.getSharedPreferences("Account_Extra_Info", 0).edit().putString("location_city", extra_info.getLocationCity()).commit();
		context.getSharedPreferences("Account_Extra_Info", 0).edit().putString("location_latitude", extra_info.getLocationLatitude()).commit();
		context.getSharedPreferences("Account_Extra_Info", 0).edit().putString("location_longitude", extra_info.getLocationLongitude()).commit();
	}
	public void setScores (TVUserScore score) {
		context.getSharedPreferences("Account_Score", 0).edit().putInt("follow", score.getFollow()).commit();
		context.getSharedPreferences("Account_Score", 0).edit().putInt("rate", score.getRate()).commit();
		context.getSharedPreferences("Account_Score", 0).edit().putInt("friend", score.getFriend()).commit();
		context.getSharedPreferences("Account_Score", 0).edit().putInt("friend_invite", score.getFriendInvite()).commit();
		context.getSharedPreferences("Account_Score", 0).edit().putInt("friend_accpet", score.getFriendAccept()).commit();
		context.getSharedPreferences("Account_Score", 0).edit().putInt("login", score.getLogin()).commit();
		context.getSharedPreferences("Account_Score", 0).edit().putInt("like", score.getLike()).commit();
		context.getSharedPreferences("Account_Score", 0).edit().putInt("liked", score.getLiked()).commit();
		context.getSharedPreferences("Account_Score", 0).edit().putInt("dislike", score.getDislike()).commit();
		context.getSharedPreferences("Account_Score", 0).edit().putInt("disliked", score.getDisliked()).commit();
		context.getSharedPreferences("Account_Score", 0).edit().putInt("comment", score.getComment()).commit();
		context.getSharedPreferences("Account_Score", 0).edit().putInt("wavein", score.getWavein()).commit();
		context.getSharedPreferences("Account_Score", 0).edit().putInt("badge", score.getBadge()).commit();
		context.getSharedPreferences("Account_Score", 0).edit().putInt("points", score.getPoints()).commit();
	}
}
