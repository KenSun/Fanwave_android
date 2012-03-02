package com.wildmind.fanwave.program;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.util.StringGenerator;

import android.os.Parcel;
import android.os.Parcelable;

public class TVProgramExtraInfo implements Parcelable {

	private String  title = "";
	private String	pgid = "";
	private String	topfan = "";
	private	String	show_option = "";
	private String	description = "";
	private String	icon_url = "";
	private String  link = "";
	private int		visitors = 0;
	private int 	user_rating = 0;
	private	int		wavein_count = 0;
	private int		follower_count = 0;
	private int		comment_count = 0;
	private int		total_rating_count = 0;
	private double	total_rating = 0;
	private boolean following = false;
	private boolean	wavein = false;
	private ArrayList<TVProgramImage> 	images = new ArrayList<TVProgramImage> ();
	private ArrayList<TVProgramVideo> 	videos = new ArrayList<TVProgramVideo> ();
	private ArrayList<TVProgramApp>		apps = new ArrayList<TVProgramApp>();
	
	// constructor
	//
	public TVProgramExtraInfo () {}
	
	public TVProgramExtraInfo (JSONObject obj) {
		try {
			this.pgid 				= obj.has("pgid") ? obj.getString("pgid") : "";
			this.topfan 			= obj.has("pg_topfan") ? obj.getString("pg_topfan") : "";
			this.show_option		= obj.has("show") ? obj.getString("show") : "";
			this.visitors			= obj.has("visitors") ? obj.getInt("visitors") : 0;
			this.user_rating		= obj.has("user_rating") ? obj.getInt("user_rating") : 0;
			this.wavein_count		= obj.has("pg_wavins") ? obj.getInt("pg_wavins") : 0;
			this.follower_count		= obj.has("pg_followers") ? obj.getInt("pg_followers") : 0;
			this.comment_count		= obj.has("pg_comments") ? obj.getInt("pg_comments") : 0;
			this.total_rating_count = obj.has("pg_rating_count") ? obj.getInt("pg_rating_count") : 0;
			this.total_rating		= obj.has("pg_rating") ? obj.getDouble("pg_rating") : 0;
			this.following			= obj.has("following") ? obj.getBoolean("following") : false;
			this.wavein				= obj.has("isWavein") ? obj.getInt("isWavein") == 1 : false;
			
			if (obj.has("media")) {
				this.description = obj.getJSONObject("media").has("pgdesc") ? obj.getJSONObject("media").getString("pgdesc") : "";
				this.icon_url	 = obj.getJSONObject("media").has("pgicon") ? obj.getJSONObject("media").getString("pgicon") : "";
				this.link		 = obj.getJSONObject("media").has("website") ? obj.getJSONObject("media").getString("website") : "";
				
				if (obj.getJSONObject("media").has("images")) {
					JSONArray array = obj.getJSONObject("media").getJSONArray("images");
					int len = array.length();
					for (int i = 0; i < len; i++) {
						this.images.add(new TVProgramImage(array.getJSONObject(i)));
					}
				}
				if (obj.getJSONObject("media").has("videos")) {
					JSONArray array = obj.getJSONObject("media").getJSONArray("videos");
					int len = array.length();
					for (int i = 0; i < len; i++) {
						this.videos.add(new TVProgramVideo(array.getJSONObject(i)));
					}
				}
				if (obj.getJSONObject("media").has("apps")) {
					JSONArray array = obj.getJSONObject("media").getJSONArray("apps");
					int len = array.length();
					for (int i = 0; i < len; i++) {
						this.apps.add(new TVProgramApp(array.getJSONObject(i)));
					}
				}
			}
			
			this.title = StringGenerator.stringFromHexString(this.pgid);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// instance variable getters
	//
	public String getTitle() {
		return this.title;
	}
	public String getPgid() {
		return pgid;
	}
	public String getTopfan() {
		return topfan;
	}
	public String getShowOption() {
		return show_option;
	}
	public String getDescription() {
		return description;
	}
	public String getIconUrl() {
		return icon_url;
	}
	public String getLink () {
		return link;
	}
	public int getVisitors() {
		return visitors;
	}
	public int getUserRating() {
		return user_rating;
	}
	public int getWaveinCount() {
		return wavein_count;
	}
	public int getFollowerCount() {
		return follower_count;
	}
	public int getCommentCount() {
		return comment_count;
	}
	public int getTotalRatingCount() {
		return total_rating_count;
	}
	public double getTotalRating() {
		return total_rating;
	}
	public boolean isFollowing() {
		return following;
	}
	public boolean isWavein() {
		return wavein;
	}
	public ArrayList<TVProgramImage> getImages() {
		return images;
	}
	public ArrayList<TVProgramVideo> getVideos() {
		return videos;
	}
	public ArrayList<TVProgramApp> getApps() {
		return apps;
	}
	
	// instance variable setters
	//
	public void setTitle(String title) {
		this.title = title;
	}
	public void setPgid(String pgid) {
		this.pgid = pgid;
	}
	public void setTopfan(String topfan) {
		this.topfan = topfan;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setShowOption(String show_option) {
		this.show_option = show_option;
	}
	public void setIconUrl(String icon_url) {
		this.icon_url = icon_url;
	}
	public void setLink (String link) {
		this.link = link;
	}
	public void setVisitors(int visitors) {
		this.visitors = visitors;
	}
	public void setUserRating(int user_rating) {
		this.user_rating = user_rating;
	}
	public void setWaveinCount(int wavein_count) {
		this.wavein_count = wavein_count;
	}
	public void setFollowerCount(int follower_count) {
		this.follower_count = follower_count;
	}
	public void setCommentCount(int comment_count) {
		this.comment_count = comment_count;
	}
	public void setTotalRatingCount(int total_rating_count) {
		this.total_rating_count = total_rating_count;
	}
	public void setTotalRating(double total_rating) {
		this.total_rating = total_rating;
	}
	public void setFollowing(boolean following) {
		this.following = following;
	}
	public void setWavein(boolean wavein) {
		this.wavein = wavein;
	}
	public void setImages(ArrayList<TVProgramImage> images) {
		this.images = images;
	}
	public void setVideos(ArrayList<TVProgramVideo> videos) {
		this.videos = videos;
	}
	public void setApps(ArrayList<TVProgramApp> apps) {
		this.apps = apps;
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
	public void writeToParcel(Parcel out, int flag) {
		String[] stringArgs = { this.title, this.pgid, this.topfan, this.show_option, this.description, 
								this.icon_url, this.link };
		out.writeStringArray(stringArgs);
		
		int[] intArgs = {this.visitors, this.user_rating, this.wavein_count, this.follower_count, this.comment_count, this.total_rating_count };
		out.writeIntArray(intArgs);
		
		double[] doubleArgs = { this.total_rating };
		out.writeDoubleArray(doubleArgs);
		
		boolean[] booleanArgs = { this.following, this.wavein };
		out.writeBooleanArray(booleanArgs);
		
		out.writeTypedList(images);
		out.writeTypedList(videos);
		out.writeTypedList(apps);
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<TVProgramExtraInfo> CREATOR = new Parcelable.Creator<TVProgramExtraInfo>() {
        public TVProgramExtraInfo createFromParcel(Parcel in) {
            return new TVProgramExtraInfo(in);
        }

        public TVProgramExtraInfo[] newArray(int size) {
            return new TVProgramExtraInfo[size];
        }
    };
    
    // constructor that takes a Parcel and gives you an object populated with it's values
    private TVProgramExtraInfo(Parcel in) {
    	String[] stringArgs = {"","","","","","",""};
    	in.readStringArray(stringArgs);
    	int[] intArgs = {0,0,0,0,0,0};
    	in.readIntArray(intArgs);
    	double[] doubleArgs = {0};
    	in.readDoubleArray(doubleArgs);
    	boolean[] booleanArgs = {false,false};
    	in.readBooleanArray(booleanArgs);
    	
    	this.title				= stringArgs[0];
    	this.pgid 				= stringArgs[1];
    	this.topfan 			= stringArgs[2];
    	this.show_option 		= stringArgs[3];
    	this.description 		= stringArgs[4];
    	this.icon_url 			= stringArgs[5];
    	this.link				= stringArgs[6];
    	this.visitors			= intArgs[0];
    	this.user_rating		= intArgs[1];
    	this.wavein_count		= intArgs[2];
    	this.follower_count 	= intArgs[3];
    	this.comment_count		= intArgs[4];
    	this.total_rating_count = intArgs[5];
    	this.total_rating		= doubleArgs[0];
    	this.following			= booleanArgs[0];
    	this.wavein				= booleanArgs[1];
    	in.readTypedList(this.images, TVProgramImage.CREATOR);
    	in.readTypedList(this.videos, TVProgramVideo.CREATOR);
    	in.readTypedList(this.apps, TVProgramApp.CREATOR);
    }
}
