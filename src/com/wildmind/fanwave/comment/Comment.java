package com.wildmind.fanwave.comment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {

	private String 	id = "";
	private String 	owner = "";
	private String 	nickname = "";
	private String 	program_title = "";
	private String 	created_time = "00000000000000";
	private String 	content = "";
	private int 	like_count = 0;
	private int 	dislike_count = 0;
	private ArrayList<Attachment> attaches = new ArrayList<Attachment>();
	
	// constructor
	//
	public Comment () {}
	
	public Comment (String id, String owner, String nickname, String content) {
		this.id = id;
		this.owner = owner;
		this.nickname = nickname;
		this.content = content;
	}
	
	public Comment (JSONObject obj) {
		try {
			this.id = obj.has("uuid") ? obj.getString("uuid") : "";
			this.owner = obj.has("owner") ? obj.getString("owner") : "";
			this.nickname = obj.has("nickname") ? obj.getString("nickname") : "";
			this.program_title = obj.has("title") ? obj.getString("title") : "";
			this.created_time = obj.has("create_at") ? obj.getString("create_at") : created_time;
			this.content = obj.has("content") ? obj.getString("content") : "";
			this.like_count = obj.has("like") ? obj.getInt("like") : like_count;
			this.dislike_count = obj.has("dislike") ? obj.getInt("dislike") : dislike_count;
			
			JSONArray array = obj.has("attachments") ? obj.getJSONArray("attachments") : null;
			if (array != null) {
				int len = array.length();
				for (int i = 0; i < len; i++) {
					attaches.add(new Attachment(array.getJSONObject(i)));
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// instance variable getters
	//
	public String getId() {
		return id;
	}
	public String getOwner() {
		return owner;
	}
	public String getNickname() {
		return nickname;
	}
	public String getProgramTitle() {
		return program_title;
	}
	public String getCreatedTime() {
		return created_time;
	}
	public String getContent() {
		return content;
	}
	public int getLikeCount() {
		return like_count;
	}
	public int getDislikeCount() {
		return dislike_count;
	}
	public ArrayList<Attachment> getAttaches() {
		return attaches;
	}

	// instance variable setters
	//
	public void setCreatedTime (String created_time) {
		this.created_time = created_time;
	}
	public void setLikeCount(int like_count) {
		this.like_count = like_count;
	}
	public void setDislikeCount(int dislike_count) {
		this.dislike_count = dislike_count;
	}
	public void setAttaches(ArrayList<Attachment> attaches) {
		this.attaches = attaches;
	}
	
	// Functionality Methods
	//
	public void addLike (int count) {
		this.like_count += count;
	}
	public void addDislike (int count) {
		this.dislike_count += count;
	}
	public void minusLike (int count) {
		this.like_count -= count;
	}
	public void minusDislike (int count) {
		this.dislike_count -= count;
	}
	public void addAttach (Attachment attach) {
		if (attaches != null)
			attaches.add(attach);
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
		
		String[] stringArgs = { this.id, this.owner, this.nickname, this.program_title, this.created_time,
								this.content };
		out.writeStringArray(stringArgs);
		
		int[] intArgs = { this.like_count, this.dislike_count };
		out.writeIntArray(intArgs);
		
		out.writeTypedList(this.attaches);
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
    
    // constructor that takes a Parcel and gives you an object populated with it's values
    private Comment(Parcel in) {
    	String[] stringArgs = {"","","","","",""};
    	in.readStringArray(stringArgs);
    	int[] intArgs = {0,0};
    	in.readIntArray(intArgs);
    	
    	this.id 			= stringArgs[0];
    	this.owner 			= stringArgs[1];
    	this.nickname 		= stringArgs[2];
    	this.program_title	= stringArgs[3];
    	this.created_time	= stringArgs[4];
    	this.content 		= stringArgs[5];
    	this.like_count		= intArgs[0];
    	this.dislike_count	= intArgs[1];
    	in.readTypedList(this.attaches, Attachment.CREATOR);
    }
}
