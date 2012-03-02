package com.wildmind.fanwave.program;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class TVProgramVideo implements Parcelable {

	private String source_type = "";
	private String token = "";
	private String thumbnail_url = "";
	private String title = "";
	private String description = "";
	
	// constructor
	//
	public TVProgramVideo (JSONObject obj) {
		try {
			this.source_type 	= obj.has("type") ? obj.getString("type") : "";
			this.token 			= obj.has("token") ? obj.getString("token") : "";
			this.thumbnail_url 	= obj.has("thumbnail") ? obj.getString("thumbnail") : "";
			this.title 			= obj.has("title") ? obj.getString("title") : "";
			this.description 	= obj.has("description") ? obj.getString("description") : "";
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// instance variable getters
	//
	public String getSourceType() {
		return source_type;
	}
	public String getToken() {
		return token;
	}
	public String getThumbnailUrl() {
		return thumbnail_url;
	}
	public String getTitle() {
		return title;
	}
	public String getDescription() {
		return description;
	}
	
	// instance variable setters
	//
	public void setSourceType(String source_type) {
		this.source_type = source_type;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public void setThumbnailUrl(String thumbnail_url) {
		this.thumbnail_url = thumbnail_url;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	// Functionality Methods
	//
	public String getVideoUri () {
		if (this.source_type.equals("youtube")) {
			return "http://m.youtube.com/watch?v=" + this.token;
		} else {
			return "";
		}
	}

	
	/**
	 * Parcelable Implementation Methods
	 */
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		
		String[] stringArgs = { this.source_type, this.token, this.thumbnail_url, this.title, this.description };
		out.writeStringArray(stringArgs);
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TVProgramVideo> CREATOR = new Parcelable.Creator<TVProgramVideo>() {
        public TVProgramVideo createFromParcel(Parcel in) {
            return new TVProgramVideo(in);
        }

        public TVProgramVideo[] newArray(int size) {
            return new TVProgramVideo[size];
        }
    };
    
    // constructor that takes a Parcel and gives you an object populated with it's values
    private TVProgramVideo(Parcel in) {
    	String[] stringArgs = {"","","","",""};
    	in.readStringArray(stringArgs);
    	
    	this.source_type 	= stringArgs[0];
    	this.token 			= stringArgs[1];
    	this.thumbnail_url 	= stringArgs[2];
    	this.title			= stringArgs[3];
    	this.description	= stringArgs[4];
    }
}
