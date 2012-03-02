package com.wildmind.fanwave.program;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class TVProgramApp implements Parcelable {

	private String image_link = "";
	private String pgid = "";
	private String type = "";
	private String url = "";
	
	// constructor
	//
	public TVProgramApp (JSONObject obj) {
		try {
			this.image_link = obj.has("imagelink") ? obj.getString("imagelink") : "";
			this.pgid 		= obj.has("pgid") ? obj.getString("pgid") : "";
			this.type 		= obj.has("type") ? obj.getString("type") : "";
			this.url 		= obj.has("url") ? obj.getString("url") : "";
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// variable getters
	//
	public String getImageLink() {
		return image_link;
	}
	public String getPgid() {
		return pgid;
	}
	public String getType() {
		return type;
	}
	public String getUrl() {
		return url;
	}

	// variable setters
	//
	public void setImageLink(String image_link) {
		this.image_link = image_link;
	}
	public void setPgid(String pgid) {
		this.pgid = pgid;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setUrl(String url) {
		this.url = url;
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
		
		String[] stringArgs = { this.image_link, this.pgid, this.type, this.url };
		out.writeStringArray(stringArgs);
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TVProgramApp> CREATOR = new Parcelable.Creator<TVProgramApp>() {
        public TVProgramApp createFromParcel(Parcel in) {
            return new TVProgramApp(in);
        }

        public TVProgramApp[] newArray(int size) {
            return new TVProgramApp[size];
        }
    };
    
    // constructor that takes a Parcel and gives you an object populated with it's values
    private TVProgramApp(Parcel in) {
    	String[] stringArgs = {"","","",""};
    	in.readStringArray(stringArgs);
    	
    	this.image_link = stringArgs[0];
    	this.pgid 		= stringArgs[1];
    	this.type 		= stringArgs[2];
    	this.url		= stringArgs[3];
    }
}
