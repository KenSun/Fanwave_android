package com.wildmind.fanwave.user;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.wildmind.fanwave.app.ApplicationManager;

public class TVUser implements Parcelable, Comparable<TVUser> {

	private String username = "";
	private String password = "";
	private String nickname = "";
	private String email = "";
	private String jid = "";
	private String jid_password = "";
	private String fbid = "";
	private String udid = "";
	private String badge_id = "";
	private TVUserPrivacy privacy = null;
	private TVUserExtraInfo extra_info = null;
	private TVUserScore scores = null;
	
	// constructor
	//
	public TVUser () {
		privacy = new TVUserPrivacy();
		extra_info = new TVUserExtraInfo();
		scores = new TVUserScore();
	}
	
	public TVUser(JSONObject obj) {
		try {
			this.username = obj.has("username") ? obj.getString("username")	: "";
			this.password = obj.has("password") ? obj.getString("password") : "";
			this.nickname = obj.has("nickname") ? obj.getString("nickname") : "";
			this.email = obj.has("email") ? obj.getString("email") : "";
			this.jid = obj.has("fanwaveJID") ? obj.getString("fanwaveJID") : "";
			this.jid_password = obj.has("fanwavePassword") ? obj.getString("fanwavePassword") : "";
			this.fbid = obj.has("facebookUid") ? obj.getString("facebookUid") : "";
			this.udid = obj.has("udid") ? obj.getString("udid") : "";
			this.badge_id = obj.has("badgeID") ? (obj.getJSONObject("badgeID").has("badgeID") ? obj.getJSONObject("badgeID").getString("badgeID") : "") : "";
			this.privacy = obj.has("privacy") ? new TVUserPrivacy(obj.getJSONObject("privacy")) : new TVUserPrivacy(obj);
			this.extra_info = obj.has("extraInfo") ? new TVUserExtraInfo(obj.getJSONObject("extraInfo")) : new TVUserExtraInfo();
			this.scores = obj.has("score") ? new TVUserScore(obj.getJSONObject("score")) : new TVUserScore();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public TVUser(TVUser user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.nickname = user.getNickname();
		this.email = user.getEmail();
		this.jid = user.getJid();
		this.jid_password = user.getJidPassword();
		this.fbid = user.getFbid();
		this.udid = user.getUdid();
		this.badge_id = user.getBadgeId();
		this.privacy = new TVUserPrivacy(user.getPrivacy());
		this.extra_info = new TVUserExtraInfo(user.getExtraInfo());
		this.scores = new TVUserScore(user.getScores());
	}
	
	// instance variable getters
	//
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getNickname() {
		return nickname;
	}
	public String getEmail() {
		return email;
	}
	public String getJid() {
		return jid;
	}
	public String getJidPassword() {
		return jid_password;
	}
	public String getFbid() {
		return fbid;
	}
	public String getUdid() {
		return udid;
	}
	public String getBadgeId() {
		return badge_id;
	}
	public TVUserPrivacy getPrivacy() {
		return privacy;
	}
	public TVUserExtraInfo getExtraInfo() {
		return extra_info;
	}
	public TVUserScore getScores() {
		return scores;
	}
	
	// instance variable setters
	//
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public void setJidPassword(String jid_password) {
		this.jid_password = jid_password;
	}
	public void setFbid(String fbid) {
		this.fbid = fbid;
	}
	public void setUdid(String udid) {
		this.udid = udid;
	}
	public void setBadgeId(String badge_id) {
		this.badge_id = badge_id;
	}
	public void setPrivacy(TVUserPrivacy privacy) {
		this.privacy = privacy;
	}
	public void setExtraInfo(TVUserExtraInfo extrainfo) {
		this.extra_info = extrainfo;
	}
	public void setScores(TVUserScore scores) {
		this.scores = scores;
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
		
		String[] stringArgs = { this.username, this.password, this.nickname, this.email, this.jid, this.jid_password, 
								this.fbid, this.udid, this.badge_id };
		out.writeStringArray(stringArgs);
		
		out.writeParcelable(privacy, flags);
		out.writeParcelable(extra_info, flags);
		out.writeParcelable(scores, flags);
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TVUser> CREATOR = new Parcelable.Creator<TVUser>() {
        public TVUser createFromParcel(Parcel in) {
            return new TVUser(in);
        }

        public TVUser[] newArray(int size) {
            return new TVUser[size];
        }
    };
    
    // constructor that takes a Parcel and gives you an object populated with it's values
    private TVUser(Parcel in) {
    	String[] stringArgs = {"","","","","","","","",""};
    	in.readStringArray(stringArgs);
    	
    	this.username 		= stringArgs[0];
    	this.password		= stringArgs[1];
    	this.nickname 		= stringArgs[2];
    	this.email 			= stringArgs[3];
    	this.jid			= stringArgs[4];
    	this.jid_password	= stringArgs[5];
    	this.fbid			= stringArgs[6];
    	this.udid			= stringArgs[7];
    	this.badge_id		= stringArgs[8];
    	
    	this.privacy 		= in.readParcelable(ApplicationManager.getAppContext().getClassLoader());
    	this.extra_info		= in.readParcelable(ApplicationManager.getAppContext().getClassLoader());
    	this.scores			= in.readParcelable(ApplicationManager.getAppContext().getClassLoader());
    }

    /**
     * Comparable Implementation Methods.
     */

	@Override
	public int compareTo(TVUser another) {
		return this.nickname.compareTo(another.getNickname());
	}
}
