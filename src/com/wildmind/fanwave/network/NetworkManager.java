package com.wildmind.fanwave.network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.activity.BaseActivity;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.device.DeviceManager;
import com.wildmind.fanwave.media.ImageManager.SampleBase;
import com.wildmind.fanwave.vendor.VendorManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkManager {

	// singleton instance
	private static NetworkManager _instance;
	
	// singleton constructor
	private NetworkManager () {}
	
	// singleton instance getter
	public static NetworkManager getInstance() {
		if (_instance == null) {
			_instance = new NetworkManager();
		}
		return _instance;
	}
	
	// static variables
	//
	private static String xmppHost = "";
	private static String baseUrl = "";
	private static String pnsUrl = "";
	private static String msolistUrl = "";
	private static String pglistUrl = "";
	private static String chlistUrl = "";
	private static String pgByChUrl = "";
	private static String pginfoUrl = "";
	private static String searchPgUrl = "";
	private static boolean network_available = false;
	
	// static variable getters
	//
	public static String getXmppHost() {
		return xmppHost;
	}
	public static String getBaseUrl() {
		return baseUrl;
	}
	public static String getMsoListUrl() {
		return msolistUrl;
	}
	public static String getPglistUrl() {
		return pglistUrl;
	}
	public static String getChlistUrl() {
		return chlistUrl;
	}
	public static String getPgByChUrl() {
		return pgByChUrl;
	}
	public static String getPginfoUrl() {
		return pginfoUrl;
	}
	public static String getSearchPgUrl() {
		return searchPgUrl;
	}
	public static String getNotificationUrl() {
		return pnsUrl;
	}
	
	/**
	 * Check if internet connection is available.
	 * @param context
	 * @return
	 */
	public static boolean isInternetAvailable (Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		
		return network_available = (info != null) && (info.isConnected()) && (info.isAvailable());
	}
	
	/**
	 * Monitor network state.
	 */
	public static void monitorNetworkState () {
		BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent == null)
					return;
				network_available = !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
				if (!network_available) {
					 Activity activity = BaseActivity.getCurrentActivity();
					 if (activity != null)
						 new AlertDialog.Builder(activity)
						 				.setTitle(R.string.app_name)
						 				.setMessage(R.string.app_close_no_internet)
						 				.setPositiveButton(R.string.action_confirm, null)
						 				.show();
				 }
			}
		};
		
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);        
		ApplicationManager.getAppContext().registerReceiver(networkStateReceiver, filter);
	}
	
	/**
	 * Get base urls.
	 */
	public static void downloadBaseUrls () {
		HashMap<String, String> response;
		do {
			response = connectByGet("http://wildmind.info/baseurl/fanwave/", null);
			//response = connectByGet("http://wildmind.info/devbaseurl/fanwave/", null);
		}
		while (!response.get("code").equals("200"));
		
		try {
			JSONObject obj = new JSONObject(response.get("data"));
			setBaseUrls(obj);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Set base urls.
	 * @param obj
	 */
	private static void setBaseUrls (JSONObject obj) {
		try {
			xmppHost	= obj.has("xmpp") ? obj.getString("xmpp") : "";
			baseUrl 	= obj.has("fanwave") ? "http://" + obj.getString("fanwave") : "";
			pnsUrl		= obj.has("pns") ? "http://" + obj.getString("pns") : "";
			
			if (obj.has("epg")) {
				msolistUrl 	= obj.getJSONObject("epg").has("provider") ? "http://" + obj.getJSONObject("epg").getString("provider") :"";
				pglistUrl 	= obj.getJSONObject("epg").has("program") ? "http://" + obj.getJSONObject("epg").getString("program") : "";
				chlistUrl 	= obj.getJSONObject("epg").has("channel") ? "http://" + obj.getJSONObject("epg").getString("channel") : "";
				pgByChUrl	= obj.getJSONObject("epg").has("program_by_ch") ? "http://" + obj.getJSONObject("epg").getString("program_by_ch") : "";
				pginfoUrl	= obj.getJSONObject("epg").has("program_info") ? "http://"	+ obj.getJSONObject("epg").getString("program_info") : "";
				searchPgUrl	= obj.getJSONObject("epg").has("search_program") ? "http://" + obj.getJSONObject("epg").getString("search_program") : "";
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create common header fields. 
	 * @param conn
	 */
	private static void createCommonHeader (HttpURLConnection conn) {
		conn.setRequestProperty("os", "Android "+ Build.VERSION.RELEASE);
		conn.setRequestProperty("system", "Android "+ Build.VERSION.RELEASE);
		conn.setRequestProperty("model", Build.MODEL);
		conn.setRequestProperty("timezone", DeviceManager.getTimezone());
		conn.setRequestProperty("language", DeviceManager.getLanguage());
		conn.setRequestProperty("udid", DeviceManager.getUdid());
		conn.setRequestProperty("country", VendorManager.getCountry());
		conn.setRequestProperty("username", AccountManager.getCurrentUser().getUsername());
		conn.setRequestProperty("password", AccountManager.getCurrentUser().getPassword());
		conn.setRequestProperty("jid", AccountManager.getCurrentUser().getJid());
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	}
	
	/**
	 * Create a http GET request and connect. The second parameter headerMap could be null if no specific header fields you want 
	 * to add or replace with in addition to the common header fields. If headerMap is not null, it will be added to the request
	 * after common header fields are created.
	 * @param URI
	 * @param headerMap
	 * @return HashMap<String, String>	code: 	 response code
	 * 									data: 	 response data
	 */
	public static HashMap<String, String> connectByGet (String URI, HashMap<String, String> headerMap) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("code", "0");
		responseMap.put("data", "");
		HttpURLConnection conn = null;
		BufferedReader buffer = null;
		
		try {
			// create connection
			//
			URL url = new URL(URI);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(15000);
			
			// set header fields
			//
			createCommonHeader(conn);
			if (headerMap != null) {
				Set<String> set = headerMap.keySet();
				Iterator<String> iterator = set.iterator();
				while (iterator.hasNext()) {
					String key = iterator.next().toString();
					conn.setRequestProperty(key, headerMap.get(key).toString());
				}
			}
			
			// read response data
			//
			buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String readInStr, responseStr = "";
			while ((readInStr = buffer.readLine()) != null) {
				responseStr += readInStr;
			}
			responseMap.put("data", responseStr);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				responseMap.put("code", String.valueOf(conn.getResponseCode()));
				if (buffer != null) {
					buffer.close();
					buffer = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return responseMap;
	}

	/**
	 * Create a http POST request and connect. The second parameter headerMap could be null if no specific header fields you want 
	 * to add or replace with in addition to the common header fields. If headerMap is not null, it will be added to the request
	 * after common header fields are created.
	 * @param URI
	 * @param headerMap
	 * @param bodyMap
	 * @return HashMap<String, String>	code: 	 response code
	 * 									data: 	 response data
	 */
	public static HashMap<String, String> connectByPost (String URI, HashMap<String, String> headerMap, HashMap<String, String> bodyMap) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("code", "0");
		responseMap.put("data", "");
		HttpURLConnection conn = null;
		DataOutputStream outputStream = null;
		BufferedReader buffer = null;
		try {
			// create connection
			//
			URL url = new URL(URI);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(15000);
			
			// set header fields
			//
			createCommonHeader(conn);
			if (headerMap != null) {
				Set<String> set = headerMap.keySet();
				Iterator<String> iterator = set.iterator();
				while (iterator.hasNext()) {
					String key = iterator.next().toString();
					conn.setRequestProperty(key, headerMap.get(key).toString());
				}
			}
			
			outputStream = new DataOutputStream(conn.getOutputStream());
			 
			// set body fields
			//
			String bodyStr = "";
			Set<String> keys = bodyMap.keySet();
			Iterator<String> iterator = keys.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next().toString();
				bodyStr += key + "=" + URLEncoder.encode(bodyMap.get(key), "UTF-8");
				if (iterator.hasNext()) {
					bodyStr += "&";
				}
			}
			//Log.i("NetworkManager", "request: " + URI);
			//Log.i("NetworkManager", "body: " + bodyStr);
			outputStream.writeBytes(bodyStr);
			
			// read response data
			//
			buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String readInStr, responseStr = "";
			while ((readInStr = buffer.readLine()) != null) {
				responseStr += readInStr;
			}
			
			responseMap.put("data", responseStr);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				//Log.i("NetworkManager", URI + " " + conn.getResponseCode());
				responseMap.put("code", String.valueOf(conn.getResponseCode()));
				if (outputStream != null) {
					outputStream.close();
					outputStream = null;
				}
				if (buffer != null) {
					buffer.close();
					buffer = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return responseMap;
	}
	
	/**
	 * Create a http POST request and connect. The second parameter headerMap could be null if no specific header fields you want 
	 * to add or replace with in addition to the common header fields. If headerMap is not null, it will be added to the request
	 * after common header fields are created.
	 * @param URI
	 * @param headerMap
	 * @param bodyString
	 * @return Map<String, String>	code: 	 response code
	 * 								data: 	 response data
	 */
	public static HashMap<String, String> connectByPost (String URI, HashMap<String, String> headerMap, String bodyString) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("code", "0");
		responseMap.put("data", "");
		HttpURLConnection conn = null;
		DataOutputStream outputStream = null;
		BufferedReader buffer = null;
		try {
			// create connection
			//
			URL url = new URL(URI);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setConnectTimeout(15000);
			
			// set header fields
			//
			createCommonHeader(conn);
			if (headerMap != null) {
				Set<String> set = headerMap.keySet();
				Iterator<String> iterator = set.iterator();
				while (iterator.hasNext()) {
					String key = iterator.next().toString();
					conn.setRequestProperty(key, headerMap.get(key).toString());
				}
			}
			outputStream = new DataOutputStream(conn.getOutputStream());
			 
			outputStream.write(bodyString.getBytes("UTF-8"));
			
			// read response data
			//
			buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String readInStr, responseStr = "";
			while ((readInStr = buffer.readLine()) != null) {
				responseStr += readInStr;
			}
			responseMap.put("data", responseStr);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				responseMap.put("code", String.valueOf(conn.getResponseCode()));
				if (outputStream != null) {
					outputStream.close();
					outputStream = null;
				}
				if (buffer != null) {
					buffer.close();
					buffer = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return responseMap;
	}

	
	
	/**
	 * Create a http POST request to get avatar data from server and form the data to Bitmap.
	 * @param username
	 * @param width
	 * @param height
	 * @return Bitmap or null
	 */
	public static Bitmap getAvatar (String username, int width, int height) {
		Bitmap image = null;
		HttpURLConnection conn = null;
		DataOutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			// create connection
			URL url = new URL(baseUrl + "/member/user/avatardownload/" + width + "/" + height);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(15000);
			
			createCommonHeader(conn);
			String bodyStr = "username=" + URLEncoder.encode(username, "UTF-8");
			outputStream = new DataOutputStream(conn.getOutputStream());
			outputStream.writeBytes(bodyStr);
			
			// form image from input stream
			//
			inputStream = conn.getInputStream();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 1;
			image = BitmapFactory.decodeStream(inputStream, null, opts);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
					outputStream = null;
				}
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return image;
	}
	
	/**
	 * Create a http POST request to get attach image data from server and form the data to Bitmap.
	 * @param token
	 * @param is_thumb
	 * @return
	 */
	public static Bitmap getAttachImage (String token, boolean is_thumb) {
		Bitmap image = null;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		try {
			// create connection
			URL url = new URL(baseUrl + "/comment/image/" + token + (is_thumb ? "/thumbnail" : "/get"));
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setConnectTimeout(15000);
			createCommonHeader(conn);
			
			// form image from input stream
			//
			inputStream = conn.getInputStream();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 1;
			image = BitmapFactory.decodeStream(inputStream, null, opts);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return image;
	}
	
	/**
	 * Create a http POST request to get badge image data from server and form the data to Bitmap.
	 * @param badge_id
	 * @return
	 */
	public static Bitmap getBadgeImage (String badge_id) {
		Bitmap image = null;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		try {
			// create connection
			URL url = new URL(baseUrl + "/badge/image/" + badge_id);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setConnectTimeout(15000);
			createCommonHeader(conn);
			
			// form image from input stream
			//
			inputStream = conn.getInputStream();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 1;
			image = BitmapFactory.decodeStream(inputStream, null, opts);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return image;
	}
	
	/**
	 * Create a http POST request to get badge image data from server and form the data to Bitmap.
	 * @param badge_id
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBadgeImage (String badge_id, int width, int height) {
		Bitmap image = null;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		try {
			// create connection
			URL url = new URL(baseUrl + "/badge/scaledimage/" + badge_id + "/" + width + "/" + height);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setConnectTimeout(15000);
			createCommonHeader(conn);
			
			// form image from input stream
			//
			inputStream = conn.getInputStream();
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 1;
			image = BitmapFactory.decodeStream(inputStream, null, opts);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return image;
	}
	
	/**
	 * Download image with url and form it as a bitmap.
	 * @param urlString
	 * @return Bitmap
	 */
	public static Bitmap downloadImage (String urlString, int sampleBase) {
		Bitmap image = null;
		BufferedInputStream inputStream = getHTTPConnectionInputStream(urlString);
		if (inputStream == null)
			return null;
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		if (sampleBase == SampleBase.IGNORE_SAMPLED ||
			sampleBase == SampleBase.UNSAMPLED) {
			opts.inSampleSize = 1;
			image = BitmapFactory.decodeStream(inputStream, null, opts);
		} else {
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(inputStream, null, opts);
			opts.inSampleSize = getFitSampleSize(opts, sampleBase);
			opts.inJustDecodeBounds = false;
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			inputStream = getHTTPConnectionInputStream(urlString);
			image = BitmapFactory.decodeStream(inputStream, null, opts);
		}
		
		try {
			if (inputStream != null)
				inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}
	
	/**
	 * Upload image to url with from file path.
	 * @param urlString
	 * @param filePath
	 * @return Map<String, String>	code: 	 response code
	 * 								data: 	 response data
	 */
	public static HashMap<String, String> uploadImage (String urlString, String filePath) {
		HashMap<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("code", "0");
		responseMap.put("data", "");
		
		BufferedInputStream fileInputStream = getFileInputStream(filePath);
		if (fileInputStream == null)
			return responseMap;
		
		// set content header and footer
		//
		String boundary = "----WILDMINDCORP";
		String newLine = "\r\n";
		String contentHeader = "--" + boundary + newLine + 
							   "Content-Disposition: form-data; name=\"photo-file\"" + 
							   "Content-Type: image/png" + newLine + newLine;
		String contentFooter = newLine + "--" + boundary + "--" + newLine;
		
		HttpURLConnection conn = null;
		DataOutputStream outputStream = null;
		BufferedReader buffer = null;
		try {
			URL url = new URL(urlString);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			
			outputStream = new DataOutputStream(conn.getOutputStream());
			
			// write content header
			outputStream.writeBytes(contentHeader);
			
			// calculate sample size
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(fileInputStream, null, opts);
			opts.inSampleSize = getFitSampleSize(opts, SampleBase.MEDIUM_SAMPLED);
			
			// get sampled image
			opts.inJustDecodeBounds = false;
			fileInputStream = getFileInputStream(filePath);
			Bitmap bmp = BitmapFactory.decodeStream(fileInputStream, null, opts);
			
			// write image data
			bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
			bmp.recycle();
			bmp = null;
			
			// write content footer
			outputStream.writeBytes(contentFooter);
			
			// read response data
			//
			buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String readInStr, responseStr = "";
			while ((readInStr = buffer.readLine()) != null) {
				responseStr += readInStr;
			}
			responseMap.put("data", responseStr);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				responseMap.put("code", String.valueOf(conn.getResponseCode()));
				if (fileInputStream != null) {
					fileInputStream.close();
					fileInputStream = null;
				}
				if (outputStream != null) {
					outputStream.close();
					outputStream = null;
				}
				if (buffer != null) {
					buffer.close();
					buffer = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return responseMap;
	}
	
	/**
	 * Get fit sample size.
	 * @param opts
	 * @param sampleBase
	 * @return int sampleSize
	 */
	public static int getFitSampleSize (BitmapFactory.Options opts, int sampleBase) {
		int width_sample = opts.outWidth / sampleBase + 1;
		int height_sample = opts.outHeight / sampleBase + 1;
		
		return height_sample >= width_sample ? height_sample : width_sample;
	}
	
	/**
	 * Get HTTP connection input stream.
	 * @param urlString
	 * @return BufferedInputStream
	 */
	public static BufferedInputStream getHTTPConnectionInputStream (String urlString) {
		InputStream inputStream = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(15000);
			inputStream = conn.getInputStream();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return inputStream != null ? new BufferedInputStream(inputStream) : null;
	}
	
	/**
	 * Get file input stream.
	 * @param filePath
	 * @return BufferInputStream
	 */
	public static BufferedInputStream getFileInputStream (String filePath) {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return inputStream != null ? new BufferedInputStream(inputStream) : null;
	}
}
