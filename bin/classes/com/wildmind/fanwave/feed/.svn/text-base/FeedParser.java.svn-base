package com.wildmind.fanwave.feed;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FeedParser {

	/**
	 * Parse feeds with a feeds JSON array. You should cast them to the exact sub-feed by type while using.
	 * @param feedData
	 * @return ArrayList<Feed>
	 */
	public static ArrayList<Feed> parse (JSONArray feedData) {
		ArrayList<Feed> feeds = new ArrayList<Feed>();
		
		int len = feedData.length();
		for (int i = 0; i < len; i++) {
			try {
				Feed feed = parseFeed(feedData.getJSONObject(i));
				if (feed != null)
					feeds.add(feed);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return feeds;
	}
	
	/**
	 * Parse feed with a feed JSON object. You should cast it to the exact sub-feed by type while using.
	 * @param obj
	 * @return Feed
	 */
	public static Feed parseFeed (JSONObject obj) {
		Feed feed = null;
		try {
			String type = obj.has("activity") ? obj.getString("activity") : "";
			if (obj.has("info")) {
				JSONObject info_obj = obj.getJSONObject("info");
				
				if (type.equals("checkin")) {
					feed = new WaveinFeed(info_obj);
				} else if (type.equals("comment")) {
					feed = new CommentFeed(info_obj);
				} else if (type.equals("like")) {
					feed = new ThumbCommentFeed(info_obj, true);
				} else if (type.equals("dislike")) {
					feed = new ThumbCommentFeed(info_obj, false);
				} else if (type.equals("reminder")) {
					feed = new SetReminderFeed(info_obj);
				} else if (type.equals("badge")) {
					feed = new GainBadgeFeed(info_obj);
				} else if (type.equals("topfan")) {
					feed = new TopfanFeed(info_obj);
				} else {
					feed = null;
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			feed = null;
		}
		
		return feed;
	}
}
