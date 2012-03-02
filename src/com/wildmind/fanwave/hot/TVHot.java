package com.wildmind.fanwave.hot;

import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.vendor.VendorManager;

public class TVHot extends TVProgram {

	private String 	hits = "";
	private String 	topfan = "";

	// constructor
	//
	public TVHot () {}
	
	public TVHot (JSONObject obj) {
		super(obj, VendorManager.getCountry());
		try {
			this.title 			= obj.has("name") ? obj.getString("name") : "";
			this.channel_code	= obj.has("channel") ? (obj.getString("channel").equals("N/A") ? "none" : obj.getString("channel")) 
								: "none";
			this.start_time 	= obj.has("start_time") ? (obj.getString("start_time").equals("N/A") ? "000000000000" 
									: obj.getString("start_time")) : start_time;
			this.end_time 		= obj.has("end_time") ? (obj.getString("end_time").equals("N/A") ? "000000000000" 
									: obj.getString("end_time")) : end_time;			
			this.hits 			= obj.has("hits") ? obj.getString("hits") : "";
			this.topfan 		= obj.has("pg_topfan") ? obj.getString("pg_topfan") : "";

			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		if (pgid.length() == 0)
			pgid = StringGenerator.hexStringFromString(title);
	}
	
	// instance variable getters
	//

	public String getHits () {
		return hits;
	}
	public String getTopfan () {
		return topfan;
	}

	// instance variable setters
	//
	public void setHits (String hits) {
		this.hits = hits;
	}
	public void setTopfan (String topfan) {
		this.topfan = topfan;
	}

}
