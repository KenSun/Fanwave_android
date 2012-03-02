package com.wildmind.fanwave.follow;

import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.program.TVProgram;

public class TVFollow extends TVProgram {

	private String icon_url = "";
	private double total_rating = 0;
	
	/**
	 * Constructor
	 */
	public TVFollow () {}
	
	public TVFollow (JSONObject obj) {
		super(obj);
		try {
			this.icon_url = obj.has("pgicon") ? obj.getString("pgicon") : "";
			this.total_rating = obj.has("pg_rating") ? obj.getDouble("pg_rating") : 0;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// getters
	//
	public String getIconUrl() {
		return icon_url;
	}
	public double getTotalRating() {
		return total_rating;
	}

	// setters
	//
	public void setIconUrl(String icon_url) {
		this.icon_url = icon_url;
	}
	public void setTotalRating(double total_rating) {
		this.total_rating = total_rating;
	}
	
}
