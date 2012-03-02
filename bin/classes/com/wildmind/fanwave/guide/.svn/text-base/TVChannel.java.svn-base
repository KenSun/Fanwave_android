package com.wildmind.fanwave.guide;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class TVChannel implements Parcelable {

	private String 	chnum = "";
	private String 	chcode = "";
	private String 	chname = "";
	private Boolean isVisible = true;
	private String 	order = "";
	
	// constructor
	//
	public TVChannel () {}
	
	public TVChannel (JSONObject obj) {
		try {
			this.chnum	= obj.has("chnum") ? obj.getString("chnum") : obj.has("channel_num")? obj.getString("channel_num") : "";
			this.chcode = obj.has("chcode") ? obj.getString("chcode") : obj.has("channel_code")? obj.getString("channel_code") : "";
			this.chname = obj.has("chname") ? obj.getString("chname") : obj.has("channel")? obj.getString("channel") : "";
			this.isVisible = obj.has("isVisible") ? obj.getString("isVisible").equals("1") ? true : false : true;
			this.order = obj.has("order") ? obj.getString("order") : obj.getString("chnum");
				
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public TVChannel (String chnum, String chcode, String chname, Boolean isVisible, String order) {
		this.chnum = chnum;
		this.chcode = chcode;
		this.chname = chname;
		this.isVisible = isVisible;
		this.order = order.equals("") ? "0" : order;
	}
	
	// instance variable getters
	//
	public String getChnum () {
		return this.chnum;
	}
	public String getChcode () {
		return this.chcode;
	}
	public String getChname () {
		return this.chname;
	}
	public Boolean getIsVisible () {
		return this.isVisible;
	}
	public String getOrder () {
		return this.order;
	}
	
	// instance variable setters
	//
	public void setChnum(String chnum) {
		this.chnum = chnum;
	}
	public void setChcode(String chcode) {
		this.chcode = chcode;
	}
	public void setChname(String chname) {
		this.chname = chname;
	}	
	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}
	public void setOrder(String order) {
		this.order = order;
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
		
		String[] stringArgs = { this.chnum, this.chcode, this.chname, this.order};
		out.writeStringArray(stringArgs);
		
		boolean[] booleanArgs = { this.isVisible};
		out.writeBooleanArray(booleanArgs);
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TVChannel> CREATOR = new Parcelable.Creator<TVChannel>() {
        public TVChannel createFromParcel(Parcel in) {
            return new TVChannel(in);
        }

        public TVChannel[] newArray(int size) {
            return new TVChannel[size];
        }
    };
    
    // constructor that takes a Parcel and gives you an object populated with it's values
    private TVChannel(Parcel in) {
    	String[] stringArgs = {"","","",""};
    	in.readStringArray(stringArgs);
    	boolean[] booleanArgs = {false};
    	in.readBooleanArray(booleanArgs);
    	this.chnum 			= stringArgs[0];
    	this.chcode			= stringArgs[1];
    	this.chname 		= stringArgs[2];
    	this.order  		= stringArgs[3];
    	this.isVisible		= booleanArgs[0];

    }
}
