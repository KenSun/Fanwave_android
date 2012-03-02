package com.wildmind.fanwave.profile;

import java.util.HashMap;

import android.content.Context;

public class ReminderProfile {

	private Context context;

	// constructor
	//
	public ReminderProfile(Context _context) {
		this.context = _context;
	}
	
	// profile getters
	//
	public String getReminderId (String key) {
		return context.getSharedPreferences("Reminder", 0).getString(key, "");
	}
	
	// profile setters
	//
	public void setReminderId (String key, String id) {
		context.getSharedPreferences("Reminder", 0).edit().putString(key, id).commit();
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, String> getAll () {
		return (HashMap<String, String>) context.getSharedPreferences("Reminder", 0).getAll();
	}
	
	/**
	 * Whether is reminder id set.
	 * @param key
	 * @return
	 */
	public boolean containsReminder (String key) {
		return getReminderId(key).length() > 0;
	}
	
	/**
	 * Remove a reminder id.
	 * @param key
	 */
	public void removeReminder (String key) {
		 context.getSharedPreferences("Reminder", 0).edit().remove(key).commit();
	}
	
	/**
	 * Remove all reminder id.
	 */
	public void removeAll () {
		context.getSharedPreferences("Reminder", 0).edit().clear().commit();
	}
}
