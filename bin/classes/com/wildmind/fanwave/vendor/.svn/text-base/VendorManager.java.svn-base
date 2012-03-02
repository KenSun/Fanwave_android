package com.wildmind.fanwave.vendor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.profile.VendorProfile;


public class VendorManager {
	
	
	private static VendorReceiver vendor_receiver = null;
	
	public static void initReceiver () {
		vendor_receiver = new VendorReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AccountManager.BROADCAST_USER_LOGOUT);
		ApplicationManager.getAppContext().registerReceiver(vendor_receiver, intentFilter);
	}

	/**
	 * ChannelManager Broadcast Receiver class
	 * @author Kencool
	 *
	 */
	private static class VendorReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// get action
			String action = intent.getAction();
			
		if (action.equals(AccountManager.BROADCAST_USER_LOGOUT)) {
			country_name = "";
			cityName = "";
			vendor_name = "";
			vendor_id = "";
			postcode = "";
			postname = "";
			VendorProfile vp = new VendorProfile(ApplicationManager.getAppContext());
			vp.setCountryName(country_name);
			vp.setCityName(cityName);
			vp.setVendorName(vendor_name);
			vp.setVendorId(vendor_id);
			vp.setPostcode(postcode);
			vp.setPostname(postname);
			}
		}
	}
	
	// static variables
	//
	private static String country = "";
	private static String country_name = "";
	private static String cityName = "";
	private static String vendor_name = "";
	private static String vendor_id = "";
	private static String postcode = "";
	private static String postname = "";
	
	static {
		retrieveVendorSettings();
	}
	
	// static variable getters
	//
	public static String getCountry () {
		return country;
	}
	public static String getCountryName () {
		return country_name;
	}
	public static String getCityName () {
		return cityName;
	}
	public static String getVendorName () {
		return vendor_name;
	}
	public static String getVendorId () {
		return vendor_id;
	}
	public static String getPostcode () {
		return postcode;
	}
	public static String getPostname () {
		return postname;
	}
	
	/**
	 * Check if vendor is selected.
	 * @return
	 */
	public static boolean isVendorSelected () {
		return vendor_id.length() > 0;
	}
	
	/**
	 * Save current vendor settings.
	 * @param countryStr
	 * @param countrynameStr
	 * @param cityStr
	 * @param vendorName
	 * @param vendorId
	 * @param postcodeStr
	 * @param postnameStr
	 */
	public static void saveVendorSettings (String countryStr, String countryname, String cityNameStr, String vendorName, String vendorId, String postnameStr, String postcodeStr) {
		country = countryStr;
		country_name = countryname;
		cityName = cityNameStr;
		vendor_name = vendorName;
		vendor_id = vendorId;
		postcode = postcodeStr;
		postname = postnameStr;	
		VendorProfile vp = new VendorProfile(ApplicationManager.getAppContext());
		vp.setCountry(country);
		vp.setCountryName(country_name);
		vp.setCityName(cityName);
		vp.setVendorName(vendor_name);
		vp.setVendorId(vendor_id);
		vp.setPostcode(postcode);
		vp.setPostname(postname);
	}

	/**
	 * Retrieve last vendor settings.
	 */
	public static void retrieveVendorSettings () {
		VendorProfile vp = new VendorProfile(ApplicationManager.getAppContext());
		country = vp.getCountry();
		country_name = vp.getCountryName();
		cityName = vp.getCityName();
		vendor_name = vp.getVendorName();
		vendor_id = vp.getVendorId();
		postcode = vp.getPostcode();
		postname = vp.getPostname();
		
	}

}
