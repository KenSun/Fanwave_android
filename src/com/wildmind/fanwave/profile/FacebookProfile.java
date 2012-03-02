package com.wildmind.fanwave.profile;

import android.content.Context;

public class FacebookProfile {

	private Context context;
	
	// constructor
	//
	public FacebookProfile (Context _context) {
		this.context = _context;
	}
	
	// getters
	//
	public boolean isFacebookAccount () {
		return context.getSharedPreferences("Facebook", 0).getBoolean("account_login", false);
	}
	public boolean isFacebookActive () {
		return context.getSharedPreferences("Facebook", 0).getBoolean("account_active", false);
	}
	public String getAccessToken () {
		return context.getSharedPreferences("Facebook", 0).getString("access_token", null);
	}
	public Long getAccessExpires () {
		return context.getSharedPreferences("Facebook", 0).getLong("access_expires", 0);
	}
	public String getUid () {
		return context.getSharedPreferences("Facebook", 0).getString("uid", null);
	}
	public String getEmail () {
		return context.getSharedPreferences("Facebook", 0).getString("email", null);
	}
	public String getName () {
		return context.getSharedPreferences("Facebook", 0).getString("name", null);
	}
	
	// setters
	//
	public void setFacebookAccount (boolean login) {
		context.getSharedPreferences("Facebook", 0).edit().putBoolean("account_login", login).commit();
	}
	public void setFacebookActive (boolean active) {
		context.getSharedPreferences("Facebook", 0).edit().putBoolean("account_active", active).commit();
	}
	public void setAccessToken (String token) {
		context.getSharedPreferences("Facebook", 0).edit().putString("access_token", token).commit();
	}
	public void setAccessExpires (long expires) {
		context.getSharedPreferences("Facebook", 0).edit().putLong("access_expires", expires).commit();
	}
	public void setUid (String uid) {
		context.getSharedPreferences("Facebook", 0).edit().putString("uid", uid).commit();
	}
	public void setEmail (String email) {
		context.getSharedPreferences("Facebook", 0).edit().putString("email", email).commit();
	}
	public void setName (String name) {
		context.getSharedPreferences("Facebook", 0).edit().putString("name", name).commit();
	}
	
	/**
	 * Clear Facebook profiles.
	 */
	public void clear () {
		context.getSharedPreferences("Facebook", 0).edit().clear().commit();
	}
	
	/**
	 * Friend Invitation Methods
	 * @param uid
	 * @return
	 */
	
	public boolean isFriendInvited (String uid) {
		return context.getSharedPreferences("Facebook_Invitation", 0).getBoolean(uid, false);
	}
	
	public void setFriendInvited (String uid, boolean invited) {
		context.getSharedPreferences("Facebook_Invitation", 0).edit().putBoolean(uid, invited).commit();
	}
}
