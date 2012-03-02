package com.wildmind.fanwave.notification;

import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.vendor.VendorManager;

public class SystemNotification extends FWNotification {

	public static final String TYPE_MESSAGE = "msg";
	public static final String TYPE_LINK 	= "link";
	public static final String TYPE_PROGRAM = "pgid";
	
	private String link = null;
	private TVProgram program = null;
	
	/**
	 * Constructor
	 * @param obj
	 */
	public SystemNotification (JSONObject obj) {
		super(obj);
		try {
			this.link = obj.has("link") ? obj.getString("link") : null;
			if (obj.has("pgid")) {
				this.program = new TVProgram();
				this.program.setPgid(obj.getString("pgid"));
				this.program.setTitle(StringGenerator.stringFromHexString(obj.getString("pgid")));
				this.program.setCountry(VendorManager.getCountry());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// getters
	//
	public String getLink() {
		return link;
	}
	public TVProgram getProgram() {
		return program;
	}

	// setters
	public void setLink(String link) {
		this.link = link;
	}
	public void setProgram(TVProgram program) {
		this.program = program;
	}
	
	
}
