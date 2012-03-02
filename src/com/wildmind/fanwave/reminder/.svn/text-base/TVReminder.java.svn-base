package com.wildmind.fanwave.reminder;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.util.DateGenerator;
import com.wildmind.fanwave.util.StringGenerator;

public class TVReminder extends TVProgram {

	public static final int REMINDER_TYPE_ONCE 		= 0;
	public static final int REMINDER_TYPE_WEEKLY 	= 1;
	public static final int REMINDER_TYPE_WEEKDAY 	= 2;
	
	private String key = "";
	private String owner = "";
	private String reminder_time = "000000000000";
	private String id = "";
	private int reminder_type = REMINDER_TYPE_ONCE;
	private int minutes_before = 0;
	
	/**
	 * Constructor
	 */
	public TVReminder () {}
	
	public TVReminder (JSONObject obj) {
		super();
		try {
			this.title			= obj.has("title") ? obj.getString("title") : title;
			this.sub_title		= obj.has("sub_title") ? obj.getString("sub_title") : sub_title;
			this.channel_code	= obj.has("channel") ? obj.getString("channel") : channel_code;
			this.pgid			= obj.has("pgid") ? obj.getString("pgid") : pgid;
			this.country		= obj.has("country") ? obj.getString("country") : country;
			this.start_time		= obj.has("start_time") ? obj.getString("start_time") : start_time;
			this.end_time		= obj.has("end_time") ? obj.getString("end_time") : end_time;
			this.owner 			= obj.has("owner") ? obj.getString("owner") : "";
			this.reminder_time 	= obj.has("reminder_time") ? obj.getString("reminder_time") : reminder_time;
			this.reminder_type 	= obj.has("reminder_type") ? obj.getInt("reminder_type") : reminder_type;
			this.icon_url 		= obj.has("pgicon") ? obj.getString("pgicon") : "";
			
			setRemindTimeString(this.reminder_time);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		this.key = channel_code + start_time + AccountManager.getCurrentUser().getUsername();
		
		if (pgid.length() == 0)
			pgid = StringGenerator.hexStringFromString(title);
	}
	
	public TVReminder (TVProgram program, String owner, String reminder_time, int type, String icon_url,
						int minutes) {
		super(program);
		this.owner = owner != null ? owner : this.owner;
		this.reminder_time = reminder_time != null ? reminder_time : this.reminder_time;
		this.reminder_type = type != -1 ? type : this.reminder_type;
		this.icon_url = icon_url != null ? icon_url : this.icon_url;
		this.minutes_before = minutes;
		
		this.key = channel_code + start_time + AccountManager.getCurrentUser().getUsername();
	}

	// getters
	//
	public String getKey() {
		return key;
	}
	public String getOwner() {
		return owner;
	}
	public String getReminderTime() {
		return reminder_time;
	}
	public String getId() {
		return id;
	}
	public int getReminderType() {
		return reminder_type;
	}
	public int getMinutesBefore() {
		return minutes_before;
	}

	// setters
	//
	public void setKey (String key) {
		this.key = key;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setReminderType(int type) {
		this.reminder_type = type;
	}
	
	/**
	 * Get reminder description.
	 * @return
	 */
	public String getDescription () {
		return title + " " + sub_title + " will plays at " + StringGenerator.playTimeStringFromTimeString(reminder_time); 
	}
	
	/**
	 * Set remind time before minutes. Synchronize reminder_time at same time.
	 * @param minutes
	 */
	public void setRemindTimeBefore (int minutes) {
		this.minutes_before = minutes;
		this.reminder_time = StringGenerator.timeStringShiftWithMinutes(start_time, 0 - minutes);
	}
	
	/**
	 * Set remind time string. Synchronize minutes_before at same time.
	 * @param timeString
	 */
	public void setRemindTimeString (String timeString) {
		reminder_time = timeString;
		Date start = DateGenerator.dateFromTimeString(start_time);
		Date remind = DateGenerator.dateFromTimeString(reminder_time);
		
		long interval = start.getTime() - remind.getTime();
		minutes_before = (int) (interval/1000) / 60;
	}
}
