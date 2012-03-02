package com.wildmind.fanwave.splash;

import org.json.JSONException;
import org.json.JSONObject;

public class Splash {

	public static final String TYPE_SEND 	= "send";
	public static final String TYPE_RECEIVE = "receive";
	
	private String id = "";
	private String type = "";
	private String sender = "";
	private String nickname = "";
	private String message = "";
	private String created_time = "";
	
	public Splash () {}
	
	public Splash (JSONObject obj) {
		try {
			this.id 			= obj.has("uuid") ? obj.getString("uuid") : "";
			this.type 			= obj.has("type") ? obj.getString("type") : "";
			this.sender 		= obj.has("user") ? obj.getString("user") : "";
			this.nickname		= obj.has("nickname") ? obj.getString("nickname") : "";
			this.message 		= obj.has("message") ? obj.getString("message") : "";
			this.created_time 	= obj.has("create_at") ? obj.getString("create_at") : "";
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// getters
	//
	public String getId() {
		return id;
	}
	public String getType() {
		return type;
	}
	public String getSender() {
		return sender;
	}
	public String getNickname() {
		return nickname;
	}
	public String getMessage() {
		return message;
	}
	public String getCreatedTime() {
		return created_time;
	}
	
	// setters
	//
	public void setId(String id) {
		this.id = id;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setCreatedTime(String created_time) {
		this.created_time = created_time;
	}
}
