package com.wildmind.fanwave.app;

import java.util.ArrayList;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.media.MediaFileManager;
import com.wildmind.fanwave.notification.FWNotificationManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ApplicationManager {

	public static final String PLATFORM_FANWAVE 	= "Fanwave";
	public static final String PLATFORM_FACEBOOK 	= "Facebook";
	
	private static Context 	app_context = null;
	private static Intent 	pending_intent = null;
	private static String 	latest_activity_name = null;
	
	public static Context getAppContext () {
		return app_context;
	}
	public static Intent getPendingIntent () {
		return pending_intent;
	}
	public static String getLatestActivityName () {
		return latest_activity_name;
	}
	
	public static void setAppContext (Context _context) {
		app_context = _context;
	}
	public static void setPendingIntent (Intent intent) {
		pending_intent = intent;
	}
	public static void setLatestActivityName (Activity activity) {
		latest_activity_name = activity.getClass().getName();
	}
	
	public static ArrayList<String> getInterRosterPlatforms () {
		ArrayList<String> platforms = new ArrayList<String>();
		platforms.add(PLATFORM_FANWAVE);
		platforms.add(PLATFORM_FACEBOOK);
		
		return platforms;
	}
	
	public static void startApplication () {
		if (!AccountManager.isLogin())
			FWNotificationManager.disableNotification();
	}
	
	public static void stopApplication () {
		// clear media files
		MediaFileManager.cleanMediaFiles();
		
		// refer notify to Urban Airship if user is login
		if (AccountManager.isLogin())
			FWNotificationManager.referNotifyToUrbanAirship();
		else
			FWNotificationManager.disableNotification();
		
		// kill process after 1 seconds for referring notify
		new Thread( new Runnable () {
			public void run () {
				try {
					 Thread.sleep(1000);
					 android.os.Process.killProcess(android.os.Process.myPid());
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
