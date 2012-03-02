package com.wildmind.fanwave.badge;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class TVBadge implements Parcelable {

	private String type = "";
	private String id = "";
	private String title = "";
	private String description = "";
	private int level = 0;
	private double progress = 0;
	
	public TVBadge () {}
	
	/**
	 * Constructor
	 * @param obj
	 */
	public TVBadge (JSONObject obj) {
		try {
			this.type = obj.has("badgeType") ? obj.getString("badgeType") : "";
			this.id = obj.has("badgeID") ? obj.getString("badgeID") : "";
			this.title = obj.has("title") ? obj.getString("title") : "";
			this.description = obj.has("description") ? obj.getString("description") : "";
			this.level = obj.has("level") ? obj.getInt("level") : 0;
			this.progress = obj.has("progress") ? obj.getDouble("progress") : 0;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor
	 * @param type
	 * @param id
	 * @param title
	 * @param descr
	 * @param level
	 * @param progress
	 */
	public TVBadge (String type, String id, String title, String descr, int level, double progress) {
		this.type = type;
		this.id = id;
		this.title = title;
		this.description = descr;
		this.level = level;
		this.progress = progress;
	}

	// getters
	//
	public String getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public int getLevel() {
		return level;
	}
	
	public double getProgress () {
		return progress;
	}

	// setters
	//
	public void setType(String type) {
		this.type = type;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setProgress (double progress) {
		this.progress = progress;
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
		String[] stringArgs = { type, id, title, description };
		out.writeStringArray(stringArgs);
		
		int[] intArgs = { level };
		out.writeIntArray(intArgs);
		
		double[] doubleArgs = { progress };
		out.writeDoubleArray(doubleArgs);
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TVBadge> CREATOR = new Parcelable.Creator<TVBadge>() {
        public TVBadge createFromParcel(Parcel in) {
            return new TVBadge(in);
        }

        public TVBadge[] newArray(int size) {
            return new TVBadge[size];
        }
    };
	
    // constructor that takes a Parcel and gives you an object populated with it's values
	private TVBadge(Parcel in) {
		String[] stringArgs = {"","","",""};
		in.readStringArray(stringArgs);
		int[] intArgs = {0};
		in.readIntArray(intArgs);
		double[] doubleArgs = {0};
		in.readDoubleArray(doubleArgs);
		
		this.type 			= stringArgs[0];
		this.id 			= stringArgs[1];
		this.title 			= stringArgs[2];
		this.description 	= stringArgs[3];
		this.level 			= intArgs[0];
		this.progress 		= doubleArgs[0];
	}
}
