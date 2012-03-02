package com.wildmind.fanwave.hot;

import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.vendor.VendorManager;

public class TVEvent extends TVProgram {


	private String 	event = "";
	private String 	imagelink = "";
	private String 	event_starttime = "";
	private String 	event_endtime = "";

	
	// constructor
	//
	public TVEvent () {}
	
	public TVEvent (JSONObject obj) {
		super(obj, VendorManager.getCountry());
		try {
			this.title 			= obj.has("name") ? obj.getString("name") : obj.has("pgname") ?obj.getString("pgname") : "";
			this.channel_code	= obj.has("channel") 
								? (obj.getString("channel").equals("N/A") ? "none" : obj.getString("channel")) 
								: "none";
			this.event 			= obj.has("event") ? obj.getString("event") : "";
			this.imagelink 		= obj.has("imagelink") ? obj.getString("imagelink") : "";

			this.event_starttime 	= obj.has("starttime") ? obj.getString("starttime") : "000000000000" ;
			this.event_endtime 		= obj.has("endtime") ? obj.getString("endtime") : "000000000000" ;			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if (pgid.length() == 0)
			pgid = StringGenerator.hexStringFromString(title);
	}
	
	// instance variable getters
	//

	public String getEvent () {
		return event;
	}
	public String getEventStartTime () {
		return event_starttime;
	}
	public String getEventEndTime () {
		return event_endtime;
	}
	public String getImageLink () {
		return imagelink;
	}
	// instance variable setters
	//

	public void setEvent (String event) {
		this.event = event;
	}
	public void setEventStartTime (String event_starttime) {
		this.event_starttime = event_starttime;
	}
	public void setEventEndTime (String event_endtime) {
		this.event_endtime = event_endtime;
	}
	public void setImageLink (String imagelink) {
		this.imagelink = imagelink;
	}
}
