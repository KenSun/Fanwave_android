package com.wildmind.fanwave.profile;

import android.content.Context;

public class SoundProfile {

	private Context context;
	
	// constructor
	//
	public SoundProfile (Context _context) {
		this.context = _context;
	}
	
	// profile getters
	//
	public Boolean isNotificationOn () {
		return context.getSharedPreferences("Sound", 0).getBoolean("notification", true);
	}
	public Boolean isCreditOn () {
		return context.getSharedPreferences("Sound", 0).getBoolean("gain_credits", true);
	}
	
	// profile setters
	//
	public void setNotification (Boolean on) {
		context.getSharedPreferences("Sound", 0).edit().putBoolean("notification", on).commit();
	}
	public void setGainCredit (Boolean on) {
		context.getSharedPreferences("Sound", 0).edit().putBoolean("gain_credits", on).commit();
	}

}
