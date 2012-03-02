package com.wildmind.fanwave.comment;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Attachment implements Parcelable {

	public static final String ATTACH_LINK = "link";
	public static final String ATTACH_IMAGE = "image";
	
	private String type = "";
	private String token = "";
	private String url = "";
	private String description = "";
	private String created_time = "";

	// constructor
	//
	public Attachment () {}
	
	public Attachment (JSONObject obj) {
		try {
			this.type = obj.has("type") ? obj.getString("type") : "";
			this.token = obj.has("token") ? obj.getString("token") : "";
			this.url = obj.has("url") ? obj.getString("url") : "";
			this.description = obj.has("desc") ? obj.getString("desc") : "";
			this.created_time = obj.has("create_at") ? obj.getString("create_at") : "";
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Attachment (String type) {
		this.type = type;
	}
	
	public Attachment (String type, String token, String url, String descr, String createTime) {
		this.type = type;
		this.token = token;
		this.url = url;
		this.description = descr;
		this.created_time = createTime;
	}
	
	// instance variable getters
	//
	public String getType() {
		return type;
	}

	public String getToken() {
		return token;
	}

	public String getUrl() {
		return url;
	}

	public String getDescription() {
		return description;
	}

	public String getCreatedTime() {
		return created_time;
	}
	
	// instance variable setters
	//
	public void setType(String type) {
		this.type = type;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCreatedTime(String created_time) {
		this.created_time = created_time;
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
		
		String[] stringArgs = { this.type, this.token, this.url, this.description, this.created_time };
		out.writeStringArray(stringArgs);
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Attachment> CREATOR = new Parcelable.Creator<Attachment>() {
        public Attachment createFromParcel(Parcel in) {
            return new Attachment(in);
        }

        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };
    
    // constructor that takes a Parcel and gives you an object populated with it's values
    private Attachment(Parcel in) {
    	String[] stringArgs = {"","","","",""};
    	in.readStringArray(stringArgs);
    	
    	this.type 			= stringArgs[0];
    	this.token 			= stringArgs[1];
    	this.url 			= stringArgs[2];
    	this.description	= stringArgs[3];
    	this.created_time	= stringArgs[4];

    }
}
