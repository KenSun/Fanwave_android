package com.wildmind.fanwave.program;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class TVProgramImage implements Parcelable {

	private String image_url = "";
	private String thumbnail_url = "";
	private String title = "";
	private String description = "";
	
	// constructor
	//
	public TVProgramImage () {}
	
	public TVProgramImage (JSONObject obj) {
		try {
			this.image_url 		= obj.has("image_url") ? obj.getString("image_url"): "";
			this.thumbnail_url	= obj.has("thumbnail_url") ? obj.getString("thumbnail_url") : "";
			this.title 			= obj.has("title") ? obj.getString("title") : "";
			this.description	= obj.has("description") ? obj.getString("description") : "";
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public TVProgramImage (String image_url, String thumb_url, String title, String descr) {
		this.image_url = image_url;
		this.thumbnail_url = thumb_url;
		this.title = title;
		this.description = descr;
	}
		
	// instance variable getters
	//
	public String getImageUrl() {
		return image_url;
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
	public void setImageUrl(String image_url) {
		this.image_url = image_url;
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
		
		String[] stringArgs = { this.image_url, this.thumbnail_url, this.title, this.description };
		out.writeStringArray(stringArgs);
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TVProgramImage> CREATOR = new Parcelable.Creator<TVProgramImage>() {
        public TVProgramImage createFromParcel(Parcel in) {
            return new TVProgramImage(in);
        }

        public TVProgramImage[] newArray(int size) {
            return new TVProgramImage[size];
        }
    };
    
 // constructor that takes a Parcel and gives you an object populated with it's values
    private TVProgramImage(Parcel in) {
    	String[] stringArgs = {"","","",""};
    	in.readStringArray(stringArgs);
    	
    	this.image_url		= stringArgs[0];
    	this.thumbnail_url 	= stringArgs[1];
    	this.title 			= stringArgs[2];
    	this.description 	= stringArgs[3];
    }
}
