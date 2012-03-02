package com.wildmind.fanwave.guide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.device.DeviceManager;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.vendor.VendorManager;

public class ChannelManager {

	private static ArrayList<TVChannel> default_channellist = new ArrayList<TVChannel>();
	private static ArrayList<TVChannel> user_channellist = new ArrayList<TVChannel>();
	private static ArrayList<TVChannel> merge_channellist = new ArrayList<TVChannel>();
	private static HashMap<String, TVChannel> merge_map = new HashMap<String, TVChannel>();

	private static boolean haveupdate = false;
	
	private static boolean chlistsuccess = false;
	private static boolean usermsosuccess = false;

	public static void sethaveupdate(boolean update){
		haveupdate = update ;
	}
	
	public static boolean gethaveupdate(){
		return haveupdate ;
	}
	
	private static ChannelReceiver channel_receiver = null;
	
	public static void initReceiver () {
		channel_receiver = new ChannelReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AccountManager.BROADCAST_USER_LOGOUT);
		ApplicationManager.getAppContext().registerReceiver(channel_receiver, intentFilter);
	}
	
	
	/**
	 * Get new TVChannel from server
	 */
	public static void getNewChannelList(){
		int i = 0;
		chlistsuccess = false;
		while(i < 2) {
			if (downloadChannel().equals("200")) {
				chlistsuccess = true;
			}
			if (chlistsuccess)
				break;
			i++;
		}
		
		if(chlistsuccess){
			merge_channellist = new ArrayList<TVChannel>(default_channellist);
			SavetoMap();			
		}
		
		ChannelManager.UpdatetoServer();
	}

	/**
	 * Get new TVChannel from server and merge default_channellist, user_channellist 
	 * Save to Map
	 */
	public static void getChannelList(){
		int i = 0;
		chlistsuccess = false;
		while(i < 2) {
			if (downloadChannel().equals("200")) {
				chlistsuccess = true;
			}
			if (chlistsuccess)
				break;
			i++;
		}
		if(chlistsuccess){
			 mergeList();
			 SavetoMap();			
		}
	}
	public static void getUserMso(boolean once){
		int i = 0;
		if(once)
			i = 1;
		usermsosuccess = false;
		while(i < 2) {
			if (downloadUserMso().equals("200")) {
				usermsosuccess = true;
			}
			if (usermsosuccess)
				break;
			i++;
		}
	}
	/**
	 * Get new TVChannel from server and merge default_channellist, user_channellist
	 */
	public static void mergeList(){
		if(merge_channellist == null||merge_channellist.size() == 0){
			
			merge_channellist = new ArrayList<TVChannel>();
			ArrayList<TVChannel> arraylist = default_channellist;
			for(TVChannel tvc : arraylist){
				for(TVChannel utvc : user_channellist){
					if(tvc.getChcode().equals(utvc.getChcode())){
						tvc.setIsVisible(utvc.getIsVisible());
						tvc.setOrder(utvc.getOrder());
					}
				}
				merge_channellist.add(tvc);
			}
			
			if(default_channellist !=null)
				default_channellist.clear();
			if(user_channellist !=null)
				user_channellist.clear();
		}
	}
	
	/**
	 * Save to Map
	 */
	public static void SavetoMap(){
		if(merge_map!=null)
			merge_map.clear();
		
		for(TVChannel tvc : merge_channellist)
			merge_map.put(tvc.getChcode(), tvc);
	}
	
	public static boolean isChange(ArrayList<TVChannel> tvclist){
		for(TVChannel tvc : merge_channellist)
			for(TVChannel usertvc : tvclist){
				if(tvc.getChcode().equals(usertvc.getChcode())){
					if(tvc.getIsVisible() != usertvc.getIsVisible())
						return true;
					if(Integer.valueOf(tvc.getOrder()) != Integer.valueOf(usertvc.getOrder()))
						return true;
				}
			}
		return false;		
	}
	
	
	/**
	 * Save isVisible for chcode
	 * @param ArrayList<TVChannel> Hidderchannellist
	 */
	public static void setHidder(ArrayList<TVChannel> Hidderchannellist){
		 for(TVChannel tvc : merge_channellist){
			for(TVChannel rtvc : Hidderchannellist){
				if(tvc.getChcode().equals(rtvc.getChcode()))
					tvc.setIsVisible(rtvc.getIsVisible());
			}
		  }
	}
	

	
	/**
	 * Save Reorder for all
	 * @param ArrayList<TVChannel> reorderchannellist
	 */
	public static void setReorder(ArrayList<TVChannel> reorderchannellist){
		 for(TVChannel tvc : merge_channellist){
			for(TVChannel rtvc : reorderchannellist){
				if(tvc.getChcode().equals(rtvc.getChcode())){
					tvc.setOrder(rtvc.getOrder());
				}
			}
		 }
	}
	
	/**
	 * Get TVChannel for chcode
	 * @param chcode
	 * @return TVChannel
	 */
	public static TVChannel getChannel(String chcode){
        return merge_map.get(chcode);		
	}
	
	/**
	 * Get sort Channellist By Num
	 * @return List<TVChannel>
	 */
	public static ArrayList<TVChannel> getSortChannelListByNum(){
		ArrayList<TVChannel> LRVC = new ArrayList<TVChannel>();
		ArrayList<TVChannel> sortchannelbylike = getSortChannelsByLike();
		ArrayList<Integer> nums = new ArrayList<Integer>();
        for(TVChannel TVC : sortchannelbylike){
        	nums.add(Integer.valueOf(TVC.getChnum()));
	    }
        // Sort
        Collections.sort(nums);
	    for(int num : nums){
	        for(TVChannel TVC : sortchannelbylike){
                if(TVC.getChnum().equals(num+"")){
                    LRVC.add(TVC);
                }
            }
        }
        return LRVC;	  
	       
	}
	
	/**
	 * Get sort UserChannelList By Older
	 * @return List<TVChannel>
	 */
	public static ArrayList<TVChannel> getSortChannelListByOrder(){
		ArrayList<TVChannel> LRVC = new ArrayList<TVChannel>();
		ArrayList<TVChannel> sortchannelbylike = getSortChannelsByLike();
		ArrayList<Integer> Orders = new ArrayList<Integer>();
        for(TVChannel TVC : sortchannelbylike){
        	if(!TVC.getOrder().equals("0"))
        		Orders.add(Integer.valueOf(TVC.getOrder()));
	    }
	    // Sort
	    Collections.sort(Orders);
	    for(int order : Orders){
	        for(TVChannel TVC : sortchannelbylike){
	           if(order!=0&&Integer.valueOf(TVC.getOrder()) == order){
	        	LRVC.remove(TVC);
                LRVC.add(TVC);
	           }
	        }
	    }
	    return LRVC;
	}
	
	/**
	 * Get sort UserChannelList By default
	 * @return List<TVChannel>
	 */
	public static ArrayList<TVChannel> getSortChannelListBydefault(){
		ArrayList<TVChannel> LRVC = new ArrayList<TVChannel>();
		ArrayList<Integer> nums = new ArrayList<Integer>();
        for(TVChannel TVC : merge_channellist){
        	nums.add(Integer.valueOf(TVC.getChnum()));
	    }
        // Sort
        Collections.sort(nums);
	    for(int num : nums){
	        for(TVChannel TVC : merge_channellist){
                if(TVC.getChnum().equals(num+"")){
                	TVChannel ntve = new TVChannel();
                	ntve.setChcode(TVC.getChcode());
                	ntve.setChname(TVC.getChname());
                	ntve.setChnum(TVC.getChnum());
                	ntve.setIsVisible(TVC.getIsVisible());
                	ntve.setOrder(TVC.getOrder());
                    LRVC.add(ntve);
                }
            }
        }

        return LRVC;	
  
	}
	
	/**
	 * Get sort Channels By Like
	 * @return List<TVChannel>
	 */
	public static ArrayList<TVChannel> getSortChannelsByLike(){
		ArrayList<TVChannel> channellist_like = new  ArrayList<TVChannel>();
		for(TVChannel TVC : merge_channellist){
			if(TVC.getIsVisible()){
				TVChannel ntve = new TVChannel();
            	ntve.setChcode(TVC.getChcode());
            	ntve.setChname(TVC.getChname());
            	ntve.setChnum(TVC.getChnum());
            	ntve.setIsVisible(TVC.getIsVisible());
            	ntve.setOrder(TVC.getOrder());
				channellist_like.add(ntve);
			}
		}
		return channellist_like;
	}	
	
	
	/**
	 * Get User mso 
	 * @return int		200 : success
	 */
	public static String downloadUserMso(){
		
		HashMap<String, String> response = null;
		if(user_channellist != null)
			user_channellist.clear();
		else
			user_channellist = new ArrayList<TVChannel>();
		
		response = NetworkManager.connectByGet(NetworkManager.getBaseUrl()+"/userchannel/get", null);
		
		if(response.get("code").equals("200")){
			try {
				JSONObject Channel_obj = new JSONObject(response.get("data"));
				if(Channel_obj.has("country")){
							VendorManager.saveVendorSettings(Channel_obj.getString("country"), 
															 Channel_obj.getString("countryName"), 
															 Channel_obj.getString("cityName"), 
															 Channel_obj.getString("msoName"), 
															 Channel_obj.getString("msoid"),
															 Channel_obj.getString("regionName"), 
															 Channel_obj.getString("postcode"));
				}
				
				if(Channel_obj.has("channels")){
					JSONArray Channels = Channel_obj.getJSONArray("channels");

					for (int i = 0; i < Channels.length(); i++) {
						JSONObject listChannels = Channels.getJSONObject(i);
						TVChannel TVC = new TVChannel(listChannels);
						TVC.setChname(TVC.getChname());
						user_channellist.add(TVC);
					}
				}else{
					user_channellist = default_channellist;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return response.get("code");
	}
		
	/**
	 * download Channel list 
	 * @return int		200 : success
	 */
	public static String downloadChannel(){
		
		HashMap<String, String> response = null;
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		if(default_channellist != null)
			default_channellist.clear();
		else
			default_channellist = new ArrayList<TVChannel>();
		
		bodyMap.put("msoid", VendorManager.getVendorId());
		response = NetworkManager.connectByPost(NetworkManager.getChlistUrl(), null, bodyMap);
		if(response.get("code").equals("200")){
			try {
				JSONObject Channel_obj = new JSONObject(response.get("data"));
				if(Channel_obj.has("channels")){
					JSONArray Channels = Channel_obj.getJSONArray("channels");
					for (int i = 0; i < Channels.length(); i++) {
						JSONObject listChannels = Channels.getJSONObject(i);
						TVChannel TVC = new TVChannel(listChannels);
						default_channellist.add(TVC);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
			}
		}
		return response.get("code");
	}
	
	/**
	 * Upate to Server
	 * @return int		200 : success
	 * 
	 * 
	 * if JSONObject obj = new JSONObject();
					obj.put("channel_code", "none");
					obj.put("channel", 		"none");
					obj.put("channel_num",  -1);
					obj.put("isVisible", 	-1);
					obj.put("order", 	 	-1);
					
					arrays.put(obj);
					
					it was not need update in  
	 * 
	 * 
	 * 
	 * 
	 */
	public static String UpdatetoServer(){
		try {
			HashMap<String, String> response = null;
			HashMap<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("Content-Type", "application/json; charset=utf-8");
			JSONObject bodyObj = new JSONObject();
	
			bodyObj.put("country", 		VendorManager.getCountry());
			bodyObj.put("countryName",  VendorManager.getCountryName());
			bodyObj.put("cityName",		VendorManager.getCityName());
			bodyObj.put("regionName", 	VendorManager.getPostname());
			bodyObj.put("postcode", 	VendorManager.getPostcode());			
			bodyObj.put("msoName", 		VendorManager.getVendorName());
			bodyObj.put("msoid", 		VendorManager.getVendorId());
			
			
			JSONArray arrays = new JSONArray();
			for(TVChannel TVC : merge_channellist){
					
				JSONObject obj = new JSONObject();
					
				obj.put("channel_code", TVC.getChcode());
				obj.put("channel", 		TVC.getChname());
				obj.put("channel_num",  Integer.valueOf(TVC.getChnum()));
				obj.put("isVisible", 	Integer.valueOf(TVC.getIsVisible()? 1:0));
				obj.put("order", 	 	Integer.valueOf(TVC.getOrder()));
					
				arrays.put(obj);
			}					

			bodyObj.put("channels", arrays);	
			response = NetworkManager.connectByPost(NetworkManager.getBaseUrl()+"/userchannel/set", headerMap, bodyObj.toString());		
			
		return response.get("code");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		}
	}
	
	
	/**
	 * download Channel list 
	 * @return int		200 : success
	 */
	public static ArrayList<TVProgram> DownloadProgramlistforChannel(String chcode, String Start, String End){

	 	Start = StringGenerator.timeStringShiftWithHours(Start, -Integer.valueOf(DeviceManager.getTimezone())); 
		End   = StringGenerator.timeStringShiftWithHours(End  , -Integer.valueOf(DeviceManager.getTimezone())); 
		
		
		
		ArrayList<TVProgram> listTVProgram = new ArrayList<TVProgram>();
		HashMap<String, String> response = null;
		HashMap<String, String> bodyMap = new HashMap<String, String>();
		bodyMap.put("chcode", chcode);
		bodyMap.put("start" , Start);
		bodyMap.put("end"	, End);
		bodyMap.put("msoid", VendorManager.getVendorId());
		response = NetworkManager.connectByPost(NetworkManager.getPgByChUrl(), null, bodyMap);
		if(response.get("code").equals("200")){
			try {
				JSONObject obj = new JSONObject(response.get("data"));
				if(obj.has("programs")){
					JSONArray Programs = obj.getJSONArray("programs");
					for (int i = 0; i < Programs.length(); i++) {
						JSONObject objj = Programs.getJSONObject(i);
						TVProgram tvpg = new TVProgram(objj, VendorManager.getCountry());
						if(listTVProgram!=null)
						listTVProgram.add(tvpg);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				return null;
			}
		}
		return listTVProgram;
		
	}
	

	/**
	 * ChannelManager Broadcast Receiver class
	 * @author Kencool
	 *
	 */
	private static class ChannelReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// get action
			String action = intent.getAction();
			
		if (action.equals(AccountManager.BROADCAST_USER_LOGOUT)) {
			default_channellist.clear();
			user_channellist.clear();
			merge_channellist.clear();
			merge_map.clear();
			}
		}
	}
	
	
}
