package com.wildmind.fanwave.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.urbanairship.push.PushManager;
import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.profile.SoundProfile;

public class FWNotificationManager {
	
	public static final String UNNOTIFIED_CATEGORY_DEFAULT 	= "default_category";
	public static final String UNNOTIFIED_CATEGORY_SPLASH	= "splash_category";
	
	private static int block_notify_count = 0;
	private static boolean ignore_notify = true;
	
	private static ArrayList<FWNotification> notification_list = new ArrayList<FWNotification>();
	private static NotificationListener listener = null;
	
	/**
	 * Unnotified list.
	 */
	private static ConcurrentHashMap<String, CopyOnWriteArrayList<String>> unnotified_list = 
			new ConcurrentHashMap<String, CopyOnWriteArrayList<String>>();
	
	/**
	 * Set notification listener.
	 * @param notifListener
	 */
	public static void setListener (NotificationListener notifListener) {
		listener = notifListener;
	}
	
	/**
	 * Add an unnotified notification id to category list.
	 * @param category
	 * @param id
	 * @param unique	whether this category always has only one unnotified id.
	 */
	public static void addUnnotifiedId (String category, String id, boolean unique) {
		if (id == null)
			return;
		
		if (category == null || category.length() == 0)
			category = UNNOTIFIED_CATEGORY_DEFAULT;
		
		CopyOnWriteArrayList<String> ids = unnotified_list.get(category);
		if (ids == null) {
			ids = new CopyOnWriteArrayList<String>();
			unnotified_list.put(category, ids);
		}
		if (unique)
			ids.clear();
		
		ids.add(id);
	}
	
	/**
	 * Remove an unnotified notification id from category list.
	 * @param category
	 * @param id
	 */
	public static void removeUnnotifiedId (String category, String id) {
		if (id == null)
			return;
		
		if (category == null || category.length() == 0)
			category = UNNOTIFIED_CATEGORY_DEFAULT;
		
		CopyOnWriteArrayList<String> ids = unnotified_list.get(category);
		if (ids != null)
			ids.remove(id);
	}
	
	/**
	 * Check if id is in unnotified ids.
	 * @param id
	 * @return boolean
	 */
	public static boolean isUnnotifiedId (String id) {
		if (id == null|| id.length() == 0)
			return false;
		
		Set<String> set = unnotified_list.keySet();
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String category = iterator.next().toString();
			CopyOnWriteArrayList<String> unnotified_ids = unnotified_list.get(category);
			
			for (String unnotified_id:unnotified_ids)
				if (id.equals(unnotified_id))
					return true;
		}
		return false;
	}
	
	public static void blockNotify () {
		block_notify_count++;
	}
	public static void unblockNotify () {
		block_notify_count--;
		if (block_notify_count < 0)
			block_notify_count = 0;
	}
	public static boolean isNotifiable () {
		return !ignore_notify && block_notify_count == 0;
	}
	
	/**
	 * Disable notification.
	 */
	public static void disableNotification () {
		PushManager.disablePush();
		ignore_notify = true;
	}
	
	/**
	 * Refer notify to Urban Airship.
	 */
	public static void referNotifyToUrbanAirship () {
		PushManager.enablePush();
		ignore_notify = true;
	}
	
	/**
	 * Retrieve notify from Urban Airship.
	 */
	public static void retrieveNotifyFromUrbanAirship () {
		PushManager.disablePush();
		ignore_notify = false;
	}
	
	/**
	 * Check if notification is enabled.
	 * @return
	 */
	public static boolean isEnabled () {
		return !ignore_notify;
	}
	
	public static ArrayList<FWNotification> getNotificationList () {
		return notification_list;
	}
	
	public static void receiveNotification (String id, String content) {
		
		// give listener a chance to handle notification
		if (listener != null)
			listener.onReceiveNotification(id, content);
		
		// notify system if notifiable
		if (!isUnnotifiedId(id) && isNotifiable()) {
			notifySystem(content);
		}
	}
	
	/**
	 * Notify system notification bar.
	 * @param notif
	 */
	private static void notifySystem (String content) {
		Context context = ApplicationManager.getAppContext();
		
		if (context == null)
			return;
		
		// get new notifier info
		CharSequence title = context.getString(R.string.app_name);
		
		// set intent and pending intent
		//Intent i = new Intent(context, NotificationActivity.class);
		Intent i = new Intent();
		PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
		
		// set new notifier info
		Notification notifier = Notifier.getNotifier();
		notifier.tickerText = content;
		notifier.setLatestEventInfo(context, title, content, pi);
		
		// notify
		NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		//mgr.notify(Notifier.NOTIFIER_ID, notifier);
		mgr.notify(Notifier.generateId(), notifier);
	}
	
	/**
	 * Register Fanwave notification service.
	 * @param rgid
	 * @param apid
	 * @return
	 */
	public static boolean registerFanwaveNotification (String rgid, String apid) {
		// set http request URI, body
		//
		String URI = NetworkManager.getNotificationUrl() + "/notification/api";
		JSONObject obj = new JSONObject();
		try {
			obj.put("active", "regist");
			obj.put("os", "Android");
			
			JSONObject android_obj = new JSONObject();
			android_obj.put("apid", apid);
			android_obj.put("alias", AccountManager.getCurrentUser().getUsername());
			android_obj.put("tags", new JSONArray());
			android_obj.put("c2dm_registration_id", rgid);
			obj.put("Android", android_obj);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, obj.toString());
		
		return response.get("code").equals("200");
	}
	
	/**
	 * Get latest notification list.
	 * @return ArrayList<FWNotification>
	 */
	public static ArrayList<FWNotification> getLastestNotification () {
		ArrayList<FWNotification> notifications = new ArrayList<FWNotification>();
		notification_list = notifications;
		
		return notification_list;
	}
	
	/**
	 * Get system notification list.
	 * @return ArrayList<SystemNotification>
	 */
	public static ArrayList<SystemNotification> getSystemNotification () {
		ArrayList<SystemNotification> notifications = new ArrayList<SystemNotification>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/announce/get";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("platform", "Android");
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++) 
					notifications.add(new SystemNotification(array.getJSONObject(i)));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return notifications;
	}
	
	/**
	 * Notifier class
	 * @author Kencool
	 *
	 */
	private static class Notifier {
		
		public static final int NOTIFIER_ID = 1;
		
		private static Notification notification = null;
		private static int notification_id = NOTIFIER_ID;
		
		public static Notification getNotifier () {
			if (notification == null) {
				notification = new Notification(R.drawable.icon, 
						ApplicationManager.getAppContext().getString(R.string.app_name), System.currentTimeMillis());
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notification.defaults = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;
			}
			
			// set sound
			SoundProfile sp = new SoundProfile(ApplicationManager.getAppContext());
			notification.defaults = Notification.DEFAULT_LIGHTS |
									(sp.isNotificationOn() ? Notification.DEFAULT_SOUND : 0);
			return notification;
		}
		
		public static int generateId () {
			return notification_id++;
		}
	}
}
