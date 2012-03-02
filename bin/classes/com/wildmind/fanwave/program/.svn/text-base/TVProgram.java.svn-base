package com.wildmind.fanwave.program;

import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.vendor.VendorManager;

import android.os.Parcel;
import android.os.Parcelable;

public class TVProgram implements Parcelable {

	protected String 	title = "";
	protected String 	sub_title = "none";
	protected String 	pgid = "";
	protected String 	channel_code = "none";
	protected String	channel_name = "";
	protected String 	country = VendorManager.getCountry();
	protected String	icon_url = "";
	protected String 	start_time = "000000000000";
	protected String 	end_time = "000000000000";
	protected boolean 	has_video = false;
	protected boolean 	is_hot = false;
	protected boolean 	is_recommend = false;
	protected boolean 	has_event = false;
	
	// constructor
	//
	public TVProgram () {}
	
	public TVProgram (TVProgram tp) {
		this.title 			= tp.getTitle();
		this.sub_title 		= tp.getSubTitle();
		this.pgid 			= tp.getPgid();
		this.channel_code 	= tp.getChannelCode();
		this.channel_name	= tp.getChannelName();
		this.country 		= tp.getCountry();
		this.icon_url		= tp.getIconUrl();
		this.start_time 	= tp.getStartTime();
		this.end_time 		= tp.getEndTime();
		this.has_video 		= tp.hasVideo();
		this.is_hot 		= tp.isHot();
		this.is_recommend 	= tp.isRecommend();
		this.has_event 		= tp.hasEvent();
		
	}
	
	public TVProgram (JSONObject obj) {
		this(obj, "");		
		try {
			this.country = obj.has("country") ? obj.getString("country") : this.country;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public TVProgram (JSONObject obj, String country) {
		this.country = country;
		try {
			this.title		 	= obj.has("pgname") ? obj.getString("pgname") : "";
			this.sub_title 		= obj.has("pgsubname") ? (obj.getString("pgsubname").length() == 0 ? sub_title : obj.getString("pgsubname")) : sub_title;
			this.pgid 			= obj.has("pgid") ? obj.getString("pgid") : "";
			this.channel_code 	= obj.has("chcode") ? (obj.getString("chcode").length() == 0 ? channel_code : obj.getString("chcode")) : channel_code;
			this.channel_name	= obj.has("chname") ? obj.getString("chname") : "";
			this.icon_url		= obj.has("pgicon") ? obj.getString("pgicon") : "";
			this.start_time 	= obj.has("start") ? obj.getString("start") : start_time;
			this.end_time 		= obj.has("end") ? obj.getString("end") : end_time;
			this.has_video 		= obj.has("video") ? obj.getInt("video") == 1 : false;
			this.is_hot 		= obj.has("hot") ? obj.getInt("hot") == 1 : false;
			this.is_recommend 	= obj.has("recommend") ? obj.getInt("recommend") == 1 : false;
			this.has_event 		= obj.has("event") ? obj.getString("event").length() > 0 : false;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if (pgid.length() == 0)
			pgid = StringGenerator.hexStringFromString(title);
	}
	
	public TVProgram (String title, String sub_title, String pgid, String channel_code, 
					  String channel_name, String country, String icon_url, String start_time, String end_time, 
					  boolean has_video, boolean is_hot, boolean is_recommend, boolean has_event) {
		this.title = title;
		this.sub_title = sub_title == null || sub_title.length() == 0 ? this.sub_title : sub_title;
		this.pgid = pgid;
		this.channel_code = channel_code == null || channel_code.length() == 0 ? this.channel_code : channel_code;
		this.channel_name = channel_name;
		this.country = country;
		this.icon_url = icon_url;
		this.start_time = start_time == null || start_time.length() == 0 ? this.start_time : start_time;
		this.end_time = end_time == null || end_time.length() == 0 ? this.end_time : end_time;
		this.has_video = has_video;
		this.is_hot = is_hot;
		this.is_recommend = is_recommend;
		this.has_event = has_event;
		
		if (pgid.length() == 0)
			pgid = StringGenerator.hexStringFromString(title);
	}
	
	// instance variable getters
	//
	public String getTitle () {
		return this.title;
	}
	public String getSubTitle () {
		return this.sub_title;
	}
	public String getPgid () {
		return this.pgid;
	}
	public String getChannelCode () {
		return this.channel_code;
	}
	public String getChannelName () {
		return this.channel_name;
	}
	public String getCountry () {
		return this.country;
	}
	public String getIconUrl () {
		return this.icon_url;
	}
	public String getStartTime () {
		return this.start_time;
	}
	public String getEndTime () {
		return this.end_time;
	}
	public boolean hasVideo () {
		return this.has_video;
	}
	public boolean isHot () {
		return this.is_hot;
	}
	public boolean isRecommend () {
		return this.is_recommend;
	}
	public boolean hasEvent () {
		return this.has_event;
	}
	
	// instance variable setters
	//
	public void setTitle(String title) {
		this.title = title;
	}
	public void setSubTitle(String sub_title) {
		this.sub_title = sub_title;
	}
	public void setPgid(String pgid) {
		this.pgid = pgid;
	}
	public void setChannelCode (String channel_code) {
		this.channel_code = channel_code;
	}
	public void setChannelName (String channel_name) {
		this.channel_name = channel_name;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setIconUrl(String icon_url) {
		this.icon_url = icon_url;
	}
	public void setStartTime(String start_time) {
		this.start_time = start_time;
	}
	public void setEndTime(String end_time) {
		this.end_time = end_time;
	}
	public void setHasVideo(boolean has_video) {
		this.has_video = has_video;
	}
	public void setIsHot(boolean is_hot) {
		this.is_hot = is_hot;
	}
	public void setIsRecommend(boolean is_recommend) {
		this.is_recommend = is_recommend;
	}
	public void setHasEvent(boolean has_event) {
		this.has_event = has_event;
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
		
		String[] stringArgs = { this.title, this.sub_title, this.pgid, this.channel_code, 
								this.channel_name, this.country, this.icon_url, this.start_time, this.end_time };
		out.writeStringArray(stringArgs);
		
		boolean[] booleanArgs = { this.has_video, this.is_hot, this.is_recommend, this.has_event };
		out.writeBooleanArray(booleanArgs);
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TVProgram> CREATOR = new Parcelable.Creator<TVProgram>() {
        public TVProgram createFromParcel(Parcel in) {
            return new TVProgram(in);
        }

        public TVProgram[] newArray(int size) {
            return new TVProgram[size];
        }
    };
    
    // constructor that takes a Parcel and gives you an object populated with it's values
    private TVProgram(Parcel in) {
    	String[] stringArgs = {"","","","","","","","",""};
    	in.readStringArray(stringArgs);
    	boolean[] booleanArgs = {false,false,false,false};
    	in.readBooleanArray(booleanArgs);
    	
    	this.title 			= stringArgs[0];
    	this.sub_title 		= stringArgs[1];
    	this.pgid 			= stringArgs[2];
    	this.channel_code	= stringArgs[3];
    	this.channel_name	= stringArgs[4];
    	this.country		= stringArgs[5];
    	this.icon_url		= stringArgs[6];
    	this.start_time 	= stringArgs[7];
    	this.end_time 		= stringArgs[8];
    	this.has_video		= booleanArgs[0];
    	this.is_hot			= booleanArgs[1];
    	this.is_recommend 	= booleanArgs[2];
    	this.has_event		= booleanArgs[3];
    }
}
