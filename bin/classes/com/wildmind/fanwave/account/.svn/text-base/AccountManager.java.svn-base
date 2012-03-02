package com.wildmind.fanwave.account;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Build;

import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.device.DeviceManager;
import com.wildmind.fanwave.facebook.FacebookManager;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.profile.AccountProfile;
import com.wildmind.fanwave.profile.FacebookProfile;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.user.TVUserExtraInfo;
import com.wildmind.fanwave.user.TVUserPrivacy;
import com.wildmind.fanwave.xmpp.FWXMPPManager;

public class AccountManager {
	
	/**
	 * Broadcast actions.
	 */
	public static String BROADCAST_USER_LOGIN 		= "broadcast_user_login";
	public static String BROADCAST_USER_LOGOUT 		= "broadcast_user_logout";
	public static String BROADCAST_USER_CHANGE_NAME = "broadcast_user_changename";
	
	// static variables
	//
	private static TVUser current_user = new TVUser();
	
	// static variable getters
	//
	public static TVUser getCurrentUser () {
		return current_user;
	}
	
	/**
	 * Sign up a Fanwave account.
	 * @param email
	 * @param password
	 * @param nickname
	 */
    public static SignResult signUpFanwave(String email, String password, String nickname) {
    	SignResult result = SignResult.failure;
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("username", email);
		bodyMap.put("password", password);
		bodyMap.put("nickname", nickname);
		bodyMap.put("email", 	email);
		HashMap<String, String> response = NetworkManager.connectByPost(NetworkManager.getBaseUrl()+"/member/user/signup", null, bodyMap);
		
		int responseCode = Integer.valueOf(response.get("code"));
		if (responseCode == 200) {
			result = SignResult.success;
		}else if (responseCode == 409) {
			result = SignResult.incorrect;
		}else{
			result = SignResult.failure;
		}
		return result;
	}
   
    /**
     * Request password.
     * @param email
     * @return SignResult
     */
    public static SignResult requestPassword (String email) {
    	SignResult result = SignResult.failure;
    	
    	// set http request URI, body
    	//
    	String URI = NetworkManager.getBaseUrl() + "/member/reminder/password";
    	HashMap<String, String> bodyMap = new HashMap<String, String>();
    	bodyMap.put("email", email);
    	bodyMap.put("language", DeviceManager.getLanguage());
    	bodyMap.put("app", "fanwave");
    	
    	// send http request
    	//
    	HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
    	
    	int responseCode = Integer.valueOf(response.get("code"));
		if (responseCode == 200)
			result = SignResult.success;
		else if (responseCode == 409)
			result = SignResult.incorrect;
		
		return result;
    }
	
	/**
	 * Login Fanwave service.
	 * @return SignResult
	 */
	public static SignResult login () {
		SignResult result = SignResult.failure;
		
		AccountProfile ap = new AccountProfile(ApplicationManager.getAppContext());
		String username = ap.getUsername();
		String password = ap.getPassword();
		if (username.length() == 0 || password.length() == 0)
			result = SignResult.incorrect;
		else {
			if ((result = loginFanwave(username, password)) == SignResult.success)
				loginXMPP();
		}
		
		return result;
	}
	
	/**
	 * Login with http request. If succeed, update user account data with response data.
	 * @param username
	 * @param password
	 * @return SignResult
	 */
	public static SignResult loginFanwave (String username, String password) {
		SignResult result = SignResult.failure;
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/member/user/login";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("username", username);
		bodyMap.put("password", password);
		bodyMap.put("language", DeviceManager.getLanguage());
		bodyMap.put("device", Build.MODEL);
		bodyMap.put("os",  "Android "+ Build.VERSION.RELEASE);
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		int responseCode = Integer.valueOf(response.get("code"));
		if (responseCode == 200) {
			try {
				result = SignResult.success;
				
				// update account profile
				JSONObject obj = new JSONObject(response.get("data"));
				updateAccount(obj);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (responseCode == 409)
			result = SignResult.incorrect;
		
		return result;
	}
	
	/**
	 * Login with Facebook account.
	 * @param email
	 * @param nickname
	 * @param uid
	 * @param token
	 * @return FacebookResult
	 */
	public static FacebookResult loginFanwaveWithFacebook (String email, String nickname, String uid, String token) {
		FacebookResult result = FacebookResult.failure;
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/member/facebookuser/loginnewversion";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("email", email);
		bodyMap.put("nickname", nickname);
		bodyMap.put("uid", uid);
		bodyMap.put("token", token);
		bodyMap.put("udid", DeviceManager.getUdid());
		bodyMap.put("language", DeviceManager.getLanguage());
		bodyMap.put("app", "fanwave");
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		int responseCode = Integer.valueOf(response.get("code"));
		if (responseCode == 200) {
			try {
				result = FacebookResult.success;
				
				// update account profile
				JSONObject obj = new JSONObject(response.get("data"));
				updateAccount(obj);
				
				// update Facebook profile
				FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
				fp.setFacebookAccount(true);
				fp.setFacebookActive(true);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (responseCode == 201)
			result = FacebookResult.firstLogin;
		else if (responseCode == 409)
			result = FacebookResult.expire;
		
		return result;
	}
	
	/**
	 * Check if Facebook email is valid.
	 * @param email
	 * @return
	 */
	public static FacebookResult checkFacebookEmailValid (String email) {
		FacebookResult result = FacebookResult.failure;
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/member/facebookuser/facebookmailcheck";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("username", email);
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		int responseCode = Integer.valueOf(response.get("code"));
		if (responseCode == 200) 
			result = FacebookResult.success;
		else if (responseCode == 400)
			result = FacebookResult.emailInvalid;
		
		return result;
	}
	
	/**
	 * Sign up for Facebook account.
	 * @param email
	 * @param nickname
	 * @param uid
	 * @param token
	 * @return
	 */
	public static boolean signUpForFacebookAccount (String email, String nickname, String uid, String token) {
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/member/facebookuser/postuser";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("username", email);
		bodyMap.put("email", email);
		bodyMap.put("nickname", nickname);
		bodyMap.put("uid", uid);
		bodyMap.put("token", token);
		bodyMap.put("udid", DeviceManager.getUdid());
		bodyMap.put("language", DeviceManager.getLanguage());
		bodyMap.put("app", "fanwave");
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		return response.get("code").equals("200");
	}
	
	/**
	 * Login XMPP
	 * @return
	 */
	public static boolean loginXMPP () {
		// disconnect first if is already connected
		if (FWXMPPManager.isConnected())
			FWXMPPManager.disconnect();
		
		boolean success = FWXMPPManager.connect(NetworkManager.getXmppHost(),
									 			current_user.getJid().split("@")[0], 
									 			current_user.getJidPassword(),
									 			"Android_" + DeviceManager.getUdid(),
									 			current_user.getExtraInfo().getMood());
		// if success, broadcast login
		if (success) {
			Intent i = new Intent();
			i.setAction(BROADCAST_USER_LOGIN);
			ApplicationManager.getAppContext().sendBroadcast(i);
		}
		
		return success;
	}
	
	/**
	 * Logout
	 */
	public static void logout () {
		// disconnect XMPP
		FWXMPPManager.disconnect();
		
		// logout Facebook
		FacebookManager.logout();
		
		// clear account password
		AccountProfile ap = new AccountProfile(ApplicationManager.getAppContext());
		ap.setPassword("");
		ap.setJidPassword("");
		
		// clear current user
		current_user = new TVUser();
		
		// broadcast user logout
		Intent i = new Intent();
		i.setAction(BROADCAST_USER_LOGOUT);
		ApplicationManager.getAppContext().sendBroadcast(i);
	}
	
	
	/**
	 * Check if user is already login.
	 * @return
	 */
	public static boolean isLogin () {
		AccountProfile ap = new AccountProfile(ApplicationManager.getAppContext());
		return ap.getPassword().length() > 0 && ap.getJidPassword().length() > 0;
	}
	
	/**
	 * Check if user is first login.
	 * @return
	 */
	public static boolean isFirstLogin () {
		AccountProfile ap = new AccountProfile(ApplicationManager.getAppContext());
		return ap.isFirstLogin();
	}
	
	/**
	 * Update account profile and current user with data retrieved from login request.
	 * @param obj
	 */
	private static void updateAccount (JSONObject obj) {
		
		// update current user
		//
		current_user = new TVUser(obj);
		current_user.setUdid(DeviceManager.getUdid());
		
		// update account profile
		//
		AccountProfile ap = new AccountProfile(ApplicationManager.getAppContext());
		ap.setUsername(current_user.getUsername());
		ap.setPassword(current_user.getPassword());
		ap.setNickname(current_user.getNickname());
		ap.setEmail(current_user.getEmail());
		ap.setJid(current_user.getJid());
		ap.setJidPassword(current_user.getJidPassword());
		ap.setBadgeId(current_user.getBadgeId());
		ap.setPrivacy(current_user.getPrivacy());
		ap.setExtraInfo(current_user.getExtraInfo());
		ap.setScores(current_user.getScores());
		
		// update facebook profile
		//
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		current_user.setFbid(fp.getUid());
		
		// update first login
		//
		try {
			ap.setFirstLogin(obj.has("firstLogin") ? obj.getBoolean("firstLogin") : false);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Update Password.
	 * @param password
	 * @return boolean
	 */
    public static boolean updatePassword(String password) {
    	// set http request URI, body
    	//
    	String URI = NetworkManager.getBaseUrl() + "/member/user/update";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("password", password);
		bodyMap.put("nickname", AccountManager.getCurrentUser().getNickname());
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);

		if (response.get("code").equals("200")) {
      		current_user.setPassword(password);
      		AccountProfile ap = new AccountProfile(ApplicationManager.getAppContext());
    		ap.setPassword(AccountManager.getCurrentUser().getPassword());
    		
			return true;
		}
		
		return false;
	}
    
	/**
	 * Update nickname.
	 * @param nickname
	 * @return boolean
	 */
    public static boolean updateNickname(String nickname) {
    	// set http request URI, body
    	//
    	String URI = NetworkManager.getBaseUrl() + "/member/user/update";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("password", AccountManager.getCurrentUser().getPassword());
		bodyMap.put("nickname", nickname);
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);

		if (response.get("code").equals("200")) {
      		current_user.setNickname(nickname);
      		AccountProfile ap = new AccountProfile(ApplicationManager.getAppContext());
    		ap.setPassword(AccountManager.getCurrentUser().getPassword());
    		
    		Intent i = new Intent();
    		i.setAction(BROADCAST_USER_CHANGE_NAME);
    		ApplicationManager.getAppContext().sendBroadcast(i);
    		
			return true;
		}
		
		return false;
	}
    
    
    /**
	 * Update privacy.
	 * @param privacy
	 * @return boolean
	 */
    public static boolean updatePrivacy(TVUserPrivacy privacy) {
    	// set http request URI, body
    	//
    	String URI = NetworkManager.getBaseUrl() + "/member/user/update";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("password", 	AccountManager.getCurrentUser().getPassword());
		bodyMap.put("nickname", 	AccountManager.getCurrentUser().getNickname());
		bodyMap.put("pokeme", 		privacy.getSplash());
		bodyMap.put("seeemail", 	privacy.getEmail());
		bodyMap.put("seereminder",  privacy.getReminder());
		bodyMap.put("seefacebook",  privacy.getFacebook());
		bodyMap.put("seesex", 		privacy.getGender());
		bodyMap.put("seebirth", 	privacy.getBirth());
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		if (response.get("code").equals("200")) {
			current_user.setPrivacy(privacy);	
			AccountProfile ap = new AccountProfile(ApplicationManager.getAppContext());
    		ap.setPrivacy(privacy);
			
    		return true;
		}
		
		return false;
	}
    
    /**
     * Update extra info.
     * @param extrainfo
     * @return boolean
     */
    public static boolean updateExtraInfo (TVUserExtraInfo extrainfo) {
    	// set http request URI, body
    	//
    	String URI = NetworkManager.getBaseUrl() + "/extrainfo/update";
    	HashMap<String, String> bodyMap = new HashMap<String, String>();
    	bodyMap.put("username", current_user.getUsername());
    	bodyMap.put("mood", extrainfo.getMood());
    	bodyMap.put("sex", extrainfo.getGender());
    	bodyMap.put("birthday", extrainfo.getBirthday());
    	bodyMap.put("bio", extrainfo.getBio());
    	bodyMap.put("website", extrainfo.getWebsite());
    	bodyMap.put("blog", extrainfo.getBlog());
    	bodyMap.put("youtube", extrainfo.getYoutube());
    	bodyMap.put("city", extrainfo.getLocationCity());
    	bodyMap.put("longitude", extrainfo.getLocationLongitude());
    	bodyMap.put("latitude", extrainfo.getLocationLatitude());
    	
    	// send http request
    	//
    	HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
    	
		if (response.get("code").equals("200")) {
			current_user.setExtraInfo(extrainfo);
			AccountProfile ap = new AccountProfile(ApplicationManager.getAppContext());
    		ap.setExtraInfo(current_user.getExtraInfo());
			
    		return true;
		}
		
		return false;
    }
    
    /**
     * Update mood.
     * @param mood
     * @return boolean
     */
    public static boolean updateMood (String mood) {
    	TVUserExtraInfo extrainfo = new TVUserExtraInfo(current_user.getExtraInfo());
    	extrainfo.setMood(mood);
    	return updateExtraInfo(extrainfo);
    }
	
	public static enum SignResult {
		success,
		incorrect,
		failure
	}
	
	public static enum FacebookResult {
		success,
		expire,
		firstLogin,
		emailInvalid,
		failure
	}
}
