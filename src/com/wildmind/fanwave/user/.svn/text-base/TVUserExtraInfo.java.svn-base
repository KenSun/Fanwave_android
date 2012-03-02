package com.wildmind.fanwave.user;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class TVUserExtraInfo implements Parcelable {

	public static final String GENDER_MALE 		= "male";
	public static final String GENDER_FEMALE 	= "female";
	
	private String mood = "";
	private String gender = "";
	private String birthday = "";
	private String bio = "";
	private String website = "";
	private String blog = "";
	private String youtube = "";
	private String location_city = "";
	private String location_longitude = "";
	private String location_latitude = "";
	
	// constructor
	//
	public TVUserExtraInfo () {}
	
	public TVUserExtraInfo (TVUserExtraInfo extrainfo) {
		this.mood 		= extrainfo.getMood();
		this.gender 	= extrainfo.getGender();
		this.birthday 	= extrainfo.getBirthday();
		this.bio 		= extrainfo.getBio();
		this.website 	= extrainfo.getWebsite();
		this.blog		= extrainfo.getBlog();
		this.youtube 	= extrainfo.getYoutube();
		this.location_city 		= extrainfo.getLocationCity();
		this.location_latitude 	= extrainfo.getLocationLatitude();
		this.location_longitude = extrainfo.getLocationLongitude();
	}
	
	public TVUserExtraInfo (JSONObject obj) {
		try {
			this.mood	 	= obj.has("mood") ? obj.getString("mood") : "";
			this.gender	 	= obj.has("sex") ? obj.getString("sex") : "";
			this.birthday 	= obj.has("birthday") ? obj.getString("birthday") : "";
			this.bio 	 	= obj.has("bio") ? obj.getString("bio") : "";
			this.website 	= obj.has("website") ? obj.getString("website") : "";
			this.blog	 	= obj.has("blog") ? obj.getString("blog") : "";
			this.youtube 	= obj.has("youtube") ? obj.getString("youtube") : "";
			
			if (obj.has("location")) {
				JSONObject location 	= obj.getJSONObject("location");
				this.location_city 		= location.has("city") ? location.getString("city") : "";
				this.location_latitude 	= location.has("latitude") ? location.getString("latitude") : "";
				this.location_longitude = location.has("longitude") ? location.getString("longitude") : "";
			}
			
		} catch (JSONException e) {
		}
	}
	
	public TVUserExtraInfo (String mood, String gender, String birthday, String bio, String website,
			String blog, String youtube, String city, String latitude, String longitude) {
		this.mood = mood;
		this.gender = gender;
		this.birthday = birthday;
		this.bio = bio;
		this.website = website;
		this.blog = blog;
		this.youtube = youtube;
		this.location_city = city;
		this.location_latitude = latitude;
		this.location_longitude = longitude;
	}

	// instance variable getters
	//
	public String getMood() {
		return mood;
	}
	public String getGender() {
		return gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public String getBio() {
		return bio;
	}
	public String getWebsite() {
		return website;
	}
	public String getBlog() {
		return blog;
	}
	public String getYoutube() {
		return youtube;
	}
	public String getLocationCity() {
		return location_city;
	}
	public String getLocationLongitude() {
		return location_longitude;
	}
	public String getLocationLatitude() {
		return location_latitude;
	}

	// instance variable setters
	//
	public void setMood(String mood) {
		this.mood = mood;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public void setBlog(String blog) {
		this.blog = blog;
	}
	public void setYoutube(String youtube) {
		this.youtube = youtube;
	}
	public void setLocationCity(String location_city) {
		this.location_city = location_city;
	}
	public void setLocationLongitude(String location_longitude) {
		this.location_longitude = location_longitude;
	}
	public void setLocationLatitude(String location_latitude) {
		this.location_latitude = location_latitude;
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
		
		String[] stringArgs = { this.mood, this.gender, this.birthday, this.bio, this.website, this.blog, 
					this.youtube, this.location_city, this.location_longitude, this.location_latitude };
		out.writeStringArray(stringArgs);
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TVUserExtraInfo> CREATOR = new Parcelable.Creator<TVUserExtraInfo>() {
        public TVUserExtraInfo createFromParcel(Parcel in) {
            return new TVUserExtraInfo(in);
        }

        public TVUserExtraInfo[] newArray(int size) {
            return new TVUserExtraInfo[size];
        }
    };
    
    // constructor that takes a Parcel and gives you an object populated with it's values
    private TVUserExtraInfo(Parcel in) {
    	String[] stringArgs = {"","","","","","","","","",""};
    	in.readStringArray(stringArgs);
    	
    	this.mood 				= stringArgs[0];
    	this.gender				= stringArgs[1];
    	this.birthday			= stringArgs[2];
    	this.bio 				= stringArgs[3];
    	this.website 			= stringArgs[4];
    	this.blog				= stringArgs[5];
    	this.youtube			= stringArgs[6];
    	this.location_city		= stringArgs[7];
    	this.location_longitude	= stringArgs[8];
    	this.location_latitude	= stringArgs[9];
    }
}
