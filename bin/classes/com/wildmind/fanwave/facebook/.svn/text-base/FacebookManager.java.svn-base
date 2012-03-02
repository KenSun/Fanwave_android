package com.wildmind.fanwave.facebook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.media.AvatarManager;
import com.wildmind.fanwave.media.MediaFileManager;
import com.wildmind.fanwave.profile.FacebookProfile;

public class FacebookManager {

	public static final String FACEBOOK_APP_ID = "118272654878874";
	public static final String[] permissions = new String[] {"publish_stream","email","offline_access"};
	
	private static Facebook facebook = new Facebook(FACEBOOK_APP_ID);
	
	static {
		retrieveFacebookSettings();
	}
	
	// getters
	//
	public static Facebook getFacebook () {
		return facebook;
	}
	public static String getEmail () {
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		return fp.getEmail();
	}
	public static String getName () {
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		return fp.getName();
	}
	public static String getUid () {
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		return fp.getUid();
	}
	public static String getAccessToken () {
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		return fp.getAccessToken();
	}
	
	// setters
	//
	public static void setEmail (String email) {
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		fp.setEmail(email);
	}
	
	/**
	 * Whether is using Facebook account.
	 * @return
	 */
	public static boolean isFacebookAccount () {
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		return fp.isFacebookAccount();
	}
	
	/**
	 * Whether is Facebook active.
	 * @return
	 */
	public static boolean isFacebookActive () {
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		return fp.isFacebookActive();
	}
	
	public static boolean isFacebookLink () {
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		return !fp.isFacebookAccount() && fp.isFacebookActive();
	}
	
	/**
	 * Login Facebook account.
	 * @param activity
	 * @param resultCode
	 * @param listener
	 * @return
	 */
	public static boolean login (Activity activity, int resultCode, DialogListener listener) {
		if (!facebook.isSessionValid()) {
			facebook.authorize(activity, permissions, resultCode, listener);
			return false;
		} 
		return true;
	}
	
	/**
	 * Logout Facebook account.
	 * @param context
	 */
	public static void logout () {
		facebook.setAccessToken(null);
		facebook.setAccessExpires(0);
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		fp.clear();
	}
	
	/**
	 * Check whether Facebook link is valid. If not, log out Facebook.
	 */
	public static void checkFacebookLink () {
		if (!facebook.isSessionValid())
			logout();
	}
	
	/**
	 * Set Facebook profile as linked.
	 */
	public static void setLinkProfile () {
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		fp.setFacebookAccount(false);
		fp.setFacebookActive(true);
		AccountManager.getCurrentUser().setFbid(fp.getUid());
	}
	
	/**
	 * Invite a friend to use Fanwave.
	 * @param user
	 */
	public static boolean inviteFriend (FBUser user) {
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		if (!fp.isFriendInvited(user.getUid())) {
			postInvitation(user.getUid());
			fp.setFriendInvited(user.getUid(), true);
			return true;
		}
		return false;
	}
	
	/**
	 * Load Facebook service.
	 */
	public static void loadFacebookService () {
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		fp.setAccessToken(facebook.getAccessToken());
		fp.setAccessExpires(facebook.getAccessExpires());
		
		// get user profile
		getUserProfile();
	}
	
	/**
	 * Get user profile.
	 * @return
	 */
	public static boolean getUserProfile () {
		boolean success = false;
		try {
			JSONObject obj = new JSONObject(facebook.request("me"));
			
			FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
			if (obj.has("id"))
				fp.setUid(obj.getString("id"));
			if (obj.has("email"))
				fp.setEmail(obj.getString("email"));
			if (obj.has("name"))
				fp.setName(obj.getString("name"));
			success = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return success;
	}
	
	/**
	 * Get user picture.
	 */
	public static boolean getUserPicture () {
		boolean success = false;
		
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		String facebookId = fp.getUid();
		InputStream inputStream = null;
		try {
			inputStream = new URL("https://graph.facebook.com/" + facebookId + "/picture?type=large").openStream();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = false;
			opts.inSampleSize = 1;
			Bitmap bmp = BitmapFactory.decodeStream(inputStream, null, opts);
			
			String username = AccountManager.getCurrentUser().getUsername();
			MediaFileManager.saveAvatarFile(username, bmp);
			AvatarManager.uploadAvatar(MediaFileManager.getAvatarFilePath(username));
			success = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	/**
	 * Get user's friends who also use Fanwave.
	 */
	public static ArrayList<FBUser> getAppFriends () {
		ArrayList<FBUser> users = new ArrayList<FBUser>();
		
		Bundle params = new Bundle();
		params.putString("method", "fql.query");
		params.putString("query", "SELECT uid, name, pic_square FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1 = me() ) AND is_app_user");
		try {
			JSONArray array = new JSONArray(facebook.request(params));
			int len = array.length();
			for (int i = 0; i < len; i++)
				users.add(new FBUser(array.getJSONObject(i)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	/**
	 * Get user's friends who don't use Fanwave.
	 */
	public static ArrayList<FBUser> getNonAppFriends () {
		ArrayList<FBUser> users = new ArrayList<FBUser>();
		
		Bundle params = new Bundle();
		params.putString("method", "fql.query");
		params.putString("query", "SELECT uid, name, pic_square FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1 = me() ) AND Not is_app_user");
		try {
			JSONArray array = new JSONArray(facebook.request(params));
			int len = array.length();
			for (int i = 0; i < len; i++)
				users.add(new FBUser(array.getJSONObject(i)));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return users;
	}
	
	/**
	 * Post Fanwave invitation to user's wall of uid.
	 * @param uid
	 */
	public static void postInvitation (String uid) {
		String message = ApplicationManager.getAppContext().getString(R.string.fb_post_invite);
		postFeed(uid, message, null);
	}
	
	/**
	 * Post wave in to user's wall.
	 * @param programTitle
	 */
	public static void postWavein (String programTitle) {
		String wavein = ApplicationManager.getAppContext().getString(R.string.fb_post_wave_in);
		String message = wavein + " \"" + programTitle + "\"";
		postFeed("me", message, null);
	}
	
	/**
	 * Post comment to user's wall.
	 * @param programTitle
	 * @param content
	 * @param link
	 * @param bmp
	 */
	public static void postComment (String programTitle, String content, String link, Bitmap bmp) {
		String in = ApplicationManager.getAppContext().getString(R.string.fb_post_comment_in);
		String say = ApplicationManager.getAppContext().getString(R.string.fb_post_comment_say);
		String message = in + " \"" + programTitle + "\" " + say + ":\n" + content;
		
		if (bmp != null)
			postPhoto(message, bmp);
		else
			postFeed("me", message, link);
	}
	
	/**
	 * Post feed to a wall.
	 * @param uid	owner's uid of the wall
	 * @param message
	 * @param link
	 */
	private static void postFeed (String uid, String message, String link) {
		Bundle params = new Bundle();
		
		// message
		params.putString("message", message);
		
		// link
		if (link != null && link.length() > 0)
			params.putString("link", link);
				
		// actions
		try {
			JSONArray array = new JSONArray();
			JSONObject action = new JSONObject();
			action.put("name", "Fanwave");
			action.put("link", "http://fanwave.tv");
			array.put(action);
			params.putString("actions", array.toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		try {
			JSONObject obj = new JSONObject(facebook.request(uid + "/feed", params, "POST"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Post photo to user's wall and album.
	 * @param message
	 * @param content
	 * @param bmp
	 */
	public static void postPhoto (String message, Bitmap bmp) {
		Bundle params = new Bundle();
		
		// picture
		if (bmp != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] ImageBytes = baos.toByteArray();
			params.putByteArray("picture", ImageBytes);
		}
		
		try {
			JSONObject obj = new JSONObject(facebook.request("me/photos", params, "POST"));
			String photoId = obj.has("id") ? obj.getString("id") : null;
			
			if (photoId == null)
				return;
			
			// message
			params.putString("message", message);
			
			// actions
			try {
				JSONArray array = new JSONArray();
				JSONObject action = new JSONObject();
				action.put("name", "Fanwave");
				action.put("link", "http://fanwave.tv");
				array.put(action);
				params.putString("actions", array.toString());
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			obj = new JSONObject(facebook.request(photoId + "/comments", params, "POST"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieve last facebook settings.
	 */
	public static void retrieveFacebookSettings () {
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		String accessToken = fp.getAccessToken();
		long accessExpires = fp.getAccessExpires();
		if (accessToken != null)
			facebook.setAccessToken(accessToken);
		if (accessExpires != 0)
			facebook.setAccessExpires(accessExpires);
	}
}
