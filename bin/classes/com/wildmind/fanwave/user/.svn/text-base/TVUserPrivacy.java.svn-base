package com.wildmind.fanwave.user;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class TVUserPrivacy implements Parcelable {

	public static final String PRIAVCY_ALL = "ALL";
	public static final String PRIVACY_FRIEND = "FRIEND";
	public static final String PRIVACY_NONE = "NONE";
	public static final String PRIVACY_NO_YEAR = "WITHOUTYEAR";
	public static final String PRIVACY_SHOW_YEAR = "SHOWALL";
	
	private String splash = PRIVACY_FRIEND;
	private String reminder = PRIVACY_FRIEND;
	private String email = PRIVACY_FRIEND;
	private String gender = PRIVACY_FRIEND;
	private String birth = PRIVACY_NO_YEAR;
	private String facebook = PRIVACY_FRIEND;
	
	// constructor
	//
	public TVUserPrivacy () {}
	
	public TVUserPrivacy (TVUserPrivacy privacy) {
		this.splash 	= privacy.getSplash();
		this.reminder 	= privacy.getReminder();
		this.email 		= privacy.getEmail();
		this.gender 	= privacy.getGender();
		this.birth	 	= privacy.getBirth();
		this.facebook 	= privacy.getFacebook();
	}
	
	public TVUserPrivacy (JSONObject obj) {
		try {
			this.splash 	= obj.has("pokeme") ? obj.getString("pokeme") : PRIVACY_FRIEND;
			this.reminder 	= obj.has("seereminder") ? obj.getString("seereminder") : PRIVACY_FRIEND;
			this.email 		= obj.has("seeemail") ? obj.getString("seeemail") : PRIVACY_FRIEND;
			this.gender 	= obj.has("seesex") ? obj.getString("seesex") : PRIVACY_FRIEND;
			this.birth	 	= obj.has("seebirth") ? obj.getString("seebirth") :PRIVACY_NO_YEAR;
			this.facebook 	= obj.has("seefacebook") ? obj.getString("seefacebook") : PRIVACY_FRIEND;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public TVUserPrivacy (String splash, String reminder, String email, String gender, String birth, String facebook) {
		this.splash = splash;
		this.reminder = reminder;
		this.email = email;
		this.gender = gender;
		this.birth = birth;
		this.facebook = facebook;
	}
	
	// instance variable getters
	//
	public String getSplash() {
		return splash;
	}
	public String getReminder() {
		return reminder;
	}
	public String getEmail() {
		return email;
	}
	public String getGender() {
		return gender;
	}
	public String getBirth() {
		return birth;
	}
	public String getFacebook() {
		return facebook;
	}
	
	// instance variable setters
	//
	public void setSplash(String splash) {
		this.splash = splash;
	}
	public void setReminder(String reminder) {
		this.reminder = reminder;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public void setFacebook(String facebook) {
		this.facebook = facebook;
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
		
		String[] stringArgs = { this.splash, this.reminder, this.email, this.gender, this.birth, this.facebook };
		out.writeStringArray(stringArgs);
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TVUserPrivacy> CREATOR = new Parcelable.Creator<TVUserPrivacy>() {
        public TVUserPrivacy createFromParcel(Parcel in) {
            return new TVUserPrivacy(in);
        }

        public TVUserPrivacy[] newArray(int size) {
            return new TVUserPrivacy[size];
        }
    };
    
    // constructor that takes a Parcel and gives you an object populated with it's values
    private TVUserPrivacy(Parcel in) {
    	String[] stringArgs = {"","","","","",""};
    	in.readStringArray(stringArgs);
    	
    	this.splash 	= stringArgs[0];
    	this.reminder 	= stringArgs[1];
    	this.email		= stringArgs[2];
    	this.gender		= stringArgs[3];
    	this.birth 		= stringArgs[4];
    	this.facebook 	= stringArgs[5];
    }
}
