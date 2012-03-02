package com.wildmind.fanwave.notification;

import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.vendor.VendorManager;

public class FWNotification {
	
	/**
	 * Common variables
	 */
	private String id = null;
	private String content = null;
	private String type = null;

	/**
	 * Announce program, Topfan splash, Post rating
	 */
	private TVProgram program = null;
	
	/**
	 * Announce web site
	 */
	private String link = null;
	
	/**
	 * Private splash, Post rating, Friend accept, Friend invite
	 */
	private String username = null;
	private String nickname = null;
	
	/**
	 * Post rating
	 */
	private String comment_id = null;
	
	/**
	 * Constructor
	 * @param builder
	 */
	private FWNotification (Builder builder) {
		this.type 		= builder.type;
		this.program 	= builder.program;
		this.link 		= builder.link;
		this.username 	= builder.username;
		this.nickname 	= builder.nickname;
		this.id 		= builder.id;
	}
	
	/**
	 * Constructor
	 * @param obj
	 */
	public FWNotification (JSONObject obj) {
		try {
			this.id			= obj.has("id") ? obj.getString("id") : "";
			this.type 		= obj.has("type") ? obj.getString("type") : "";
			this.content 	= obj.has("content") ? obj.getString("content") : "";
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	// getters
	//
	public String getId () {
		return id;
	}
	public String getType () {
		return type;
	}
	public String getContent () {
		return content;
	}
	public TVProgram getProgram () {
		return program;
	}
	public String getLink () {
		return link;
	}
	public String getUsername () {
		return username;
	}
	public String getNickname () {
		return nickname;
	}
	
	/**
	 * FWNotification Builder class
	 * @author Kencool
	 *
	 */
	public static class Builder {
		
		private String id			= null;
		private String type 		= null;
		private String link 		= null;
		private String username 	= null;
		private String nickname 	= null;
		private TVProgram program 	= null;
		
		/**
		 * Constructor
		 * @param type
		 */
		public Builder (String type, String id) {
			this.type = type != null ? type : "";
			this.id   = id != null ? id : "";
		}
		
		/**
		 * Set program with pgid
		 * @param pgid
		 * @return Builder
		 */
		public Builder setProgram (String pgid) {
			TVProgram tp = new TVProgram();
			tp.setPgid(pgid);
			tp.setTitle(StringGenerator.stringFromHexString(pgid));
			tp.setCountry(VendorManager.getCountry());
			this.program = tp;
			return this;
		}
		
		/**
		 * Set program with pgid and country
		 * @param pgid
		 * @param country
		 * @return Builder
		 */
		public Builder setProgram (String pgid, String country) {
			TVProgram tp = new TVProgram();
			tp.setPgid(pgid);
			tp.setTitle(StringGenerator.stringFromHexString(pgid));
			tp.setCountry(country);
			this.program = tp;
			return this;
		}
		
		/**
		 * Set link
		 * @param link
		 * @return Builder
		 */
		public Builder setLink (String link) {
			this.link = link;
			return this;
		}
		
		/**
		 * Set user
		 * @param username
		 * @param nickname
		 * @return Builder
		 */
		public Builder setUser (String username, String nickname) {
			this.username = username;
			this.nickname = nickname;
			return this;
		}
		
		/**
		 * Build notification
		 * @return FWNotification
		 */
		public FWNotification build () {
			return new FWNotification(this);
		}
	}
	
	/**
	 * Notification Type class
	 */
	public static class Type {
		public static final String TYPE_ANNOUNCE_PROGRAM 	= "pgid";
		public static final String TYPE_ANNOUNCE_LINK	 	= "link";
		public static final String TYPE_ANNOUNCE_MESSAGE	= "msg";
		public static final String TYPE_PRIVATE_SPLASH 		= "splash";
		public static final String TYPE_TOPFAN_SPLASH 		= "topfan";
		public static final String TYPE_POST_RATING			= "postrating";
		public static final String TYPE_FRIEND_INVITE		= "invite";
		public static final String TYPE_FRIEND_ACCEPT		= "accept";
	}
}
