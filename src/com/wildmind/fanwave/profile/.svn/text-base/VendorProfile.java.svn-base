package com.wildmind.fanwave.profile;

import android.content.Context;

public class VendorProfile {

	private Context context;
	
	// constructor
	//
	public VendorProfile (Context _context) {
		this.context = _context;
	}
	
	// profile getters
	//
	public String getCountry () {
		return context.getSharedPreferences("Vendor", 0).getString("country", "tw");
	}
	public String getCountryName () {
		return context.getSharedPreferences("Vendor", 0).getString("country_name", "");
	}
	public String getCityName () {
		return context.getSharedPreferences("Vendor", 0).getString("city_name", "");
	}
	public String getVendorName () {
		return context.getSharedPreferences("Vendor", 0).getString("vendor_name", "");
	}
	public String getVendorId () {
		return context.getSharedPreferences("Vendor", 0).getString("vendor_id", "");
	}
	public String getPostcode () {
		return context.getSharedPreferences("Vendor", 0).getString("postcode", "");
	}
	public String getPostname () {
		return context.getSharedPreferences("Vendor", 0).getString("postname", "");
	}
	
	// profile setters
	//
	public void setCountry (String country) {
		context.getSharedPreferences("Vendor", 0).edit().putString("country", country).commit();
	}
	public void setCountryName (String CountryName) {
		context.getSharedPreferences("Vendor", 0).edit().putString("country_name", CountryName).commit();
	}
	public void setCityName (String city_name) {
		context.getSharedPreferences("Vendor", 0).edit().putString("city_name", city_name).commit();
	}
	public void setVendorName (String vendorName) {
		context.getSharedPreferences("Vendor", 0).edit().putString("vendor_name", vendorName).commit();
	}
	public void setVendorId (String VendorId) {
		context.getSharedPreferences("Vendor", 0).edit().putString("vendor_id", VendorId).commit();
	}
	public void setPostcode (String postcode) {
		context.getSharedPreferences("Vendor", 0).edit().putString("postcode", postcode).commit();
	}
	public void setPostname (String postname) {
		context.getSharedPreferences("Vendor", 0).edit().putString("postname", postname).commit();
	}
}
