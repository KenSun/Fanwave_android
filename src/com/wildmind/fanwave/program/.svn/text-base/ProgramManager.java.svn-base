package com.wildmind.fanwave.program;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.vendor.VendorManager;

public class ProgramManager {
	
	/**
	 * Wave in a program. If user has already wave in this program, we take it as success.
	 * @param program
	 * @return
	 */
	public static boolean waveinProgram (TVProgram program) {
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/checkin/post";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("country", program.getCountry());
		bodyMap.put("title", program.getTitle());
		bodyMap.put("sub_title", program.getSubTitle());
		bodyMap.put("pgid", program.getPgid());
		bodyMap.put("channel", program.getChannelCode());
		bodyMap.put("start_time", program.getStartTime());
		bodyMap.put("end_time", program.getEndTime());
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);

		return response.get("code").equals("200") || response.get("code").equals("409");
	}
	
	/**
	 * Rate a program with rating.
	 * 
	 * @param program
	 * @param rating
	 * @return boolean
	 */
	public static boolean rateProgram (TVProgram program, int rating) {
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/program/rating/rate";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("pgid", program.getPgid());
		bodyMap.put("country", program.getCountry());
		bodyMap.put("title", program.getTitle());
		bodyMap.put("sub_title", program.getSubTitle());
		bodyMap.put("rating", String.valueOf(rating));

		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);

		return response.get("code").equals("200");
	}

	/**
	 * Get program rating with pgid.
	 * 
	 * @param pgid
	 * @return double
	 */
	public static double getProgramRating(String pgid) {
		double rating = 0;

		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/program/rating/get";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("pgid", pgid);

		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);

		if (response.get("code").equals("200")) {
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				rating = obj.getDouble("rating");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return rating;
	}

	/**
	 * Get program infos with pgid and country from start time utcTime.
	 * 
	 * @param pgid
	 * @param country user's current country
	 * @param utcTime the time replays start from
	 * @param msoid
	 * @return ArrayList<TVProgram>
	 */
	public static ArrayList<TVProgram> getProgramReplays(String pgid, String country, String utcTime, String msoid) {
		ArrayList<TVProgram> replaylist = new ArrayList<TVProgram>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getPginfoUrl();
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("pgid", pgid);
		bodyMap.put("country", country);
		bodyMap.put("time", utcTime);
		bodyMap.put("msoid", msoid);

		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		
		if (response.get("code").equals("200")) {
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				JSONArray array = obj.getJSONArray("programInfo");
				int len = array.length();
				for (int i = 0; i < len; i++) {
					TVProgram tp = new TVProgram(array.getJSONObject(i), country);
					replaylist.add(tp);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return replaylist;
	}

	/**
	 * Get program extra info with country and pgid.
	 * 
	 * @param country
	 * @param pgid
	 * @return TVProgramExtraInfo
	 */
	public static TVProgramExtraInfo getProgramExtraInfo(String country, String pgid) {
		if (country == null || country.length() == 0 ||
			pgid == null || pgid.length() == 0)
			return null;
		
		TVProgramExtraInfo extrainfo = null;

		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/program/extrainfo/get";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("pgid", pgid);
		bodyMap.put("country", country);

		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);

		if (response.get("code").equals("200")) {
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				extrainfo = new TVProgramExtraInfo(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return extrainfo;
	}

	/**
	 * Get program extra infos with jsonArray. Each JSONObject of jsonArray must have keys "country" and "pgid".
	 * @param jsonArray
	 * @return ArrayList<TVProgramExtraInfo>
	 */
	public static ArrayList<TVProgramExtraInfo> getProgramExtraInfos(JSONArray jsonArray) {
		ArrayList<TVProgramExtraInfo> infolist = new ArrayList<TVProgramExtraInfo> ();
		
		// set http request URI, header, body
		//
		String URI = NetworkManager.getBaseUrl() + "/program/extrainfo/multi/get";
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Content-Type", "application/json; charset=utf-8");
		String bodyStr = jsonArray != null ? jsonArray.toString() : new JSONArray().toString();

		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, headerMap, bodyStr);

		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++) {
					TVProgramExtraInfo extrainfo = new TVProgramExtraInfo(array.getJSONObject(i));
					infolist.add(extrainfo);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return infolist;
	}

	public static String getProgramFans(String username, String pgid, int number){		
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("username", username);
		bodyMap.put("pgid", pgid);
		bodyMap.put("number", (number+1)+"");
		
		// set http request URI
		//
		String URI = NetworkManager.getBaseUrl() + "/scoringsystem/program/get/rank";

		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);

		if (response.get("code").equals("200")) 
			return response.get("data");
			
		return "";
	}
	
	/**
	 * Get program followers.
	 * @param pgid
	 * @return ArrayList<TVUser>
	 */
	public static ArrayList<TVUser> getProgramFollowers (String pgid) {
		ArrayList<TVUser> followers = new ArrayList<TVUser>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/follow/getprogramfollower";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("pgid", pgid);
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		if (response.get("code").equals("200")) {
			try {
				JSONArray array = new JSONArray(response.get("data"));
				int len = array.length();
				for (int i = 0; i < len; i++)
					followers.add(new TVUser(array.getJSONObject(i)));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return followers;
	}
	
	/**
	 * Get MUC jid of program.
	 * @param title
	 * @param country
	 * @return String or null if failed
	 */
	public static String getProgramJid (String title, String country) {
		String jid = null;
		
		// set http request URI, body
		//
		String URI = NetworkManager.getBaseUrl() + "/chatroom/getChatroomJID";
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("title", title);
		bodyMap.put("country", country);
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		if (response.get("code").equals("200")) {
			try {
				JSONObject data = new JSONObject(response.get("data"));
				jid = data.has("jid") ? data.getString("jid") : "";
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return jid;
	}
	
	/**
	 * Search programs.
	 * @param searchTerm
	 * @return ArrayList<TVProgram>
	 */
	public static ArrayList<TVProgram> searchPrograms (String searchTerm) {
		ArrayList<TVProgram> programs = new ArrayList<TVProgram>();
		
		// set http request URI, body
		//
		String URI = NetworkManager.getSearchPgUrl();
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("keyword", searchTerm);
		bodyMap.put("country", VendorManager.getCountry());
		bodyMap.put("time", StringGenerator.UTCFromLocal(StringGenerator.getCurrentTimeString()));
		bodyMap.put("msoid", VendorManager.getVendorId());
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.connectByPost(URI, null, bodyMap);
		if (response.get("code").equals("200")) {
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				JSONArray array = obj.getJSONArray("result");
				int len = array.length();
				for (int i = 0; i < len; i++)
					programs.add(new TVProgram(array.getJSONObject(i), VendorManager.getCountry()));
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return programs;
	}
}
