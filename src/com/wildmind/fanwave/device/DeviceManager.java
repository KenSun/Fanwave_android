package com.wildmind.fanwave.device;

import java.util.TimeZone;

import com.wildmind.fanwave.activity.BaseActivity;
import com.wildmind.fanwave.app.ApplicationManager;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class DeviceManager {

	// static variables
	//
	private static String language = "";
	private static String timezone = "";
	private static String udid = "";
	private static float xdpi = 0;
	private static float ydpi = 0;
	
	static {
		retrieveDeviceAttributes();
	}
	
	// static variable getters
	//
	public static String getLanguage () {
		return language;
	}
	public static String getTimezone () {
		return timezone;
	}
	public static String getUdid () {
		return udid;
	}
	
	/**
	 * Get device attributes and set to DeviceManager.
	 */
	public static void retrieveDeviceAttributes () {
		TelephonyManager tm = (TelephonyManager) ApplicationManager.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
		udid= tm.getDeviceId();
		
		language = ApplicationManager.getAppContext().getResources().getConfiguration().locale.getLanguage();
		timezone = String.valueOf(TimeZone.getDefault().getOffset(System.currentTimeMillis()) / 3600000);
		
		if (BaseActivity.getCurrentActivity() != null)
			getDeviceDpi();
	}
	
	/**
	 * Get device dpi.
	 */
	private static void getDeviceDpi () {
		DisplayMetrics metrics = new DisplayMetrics();
		BaseActivity.getCurrentActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		xdpi= metrics.xdpi;
		ydpi = metrics.ydpi;
	}
	
	public static float pixelsFromDpForX (float dp) {
		return dp * (xdpi / 160);
	}
	
	public static float pixelsFromDpForY (float dp) {
		return dp * (ydpi / 160);
	}
}
