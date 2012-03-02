package com.wildmind.fanwave.media;

import java.util.HashMap;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.device.DeviceManager;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.util.ImageProcessor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class AvatarManager {
	
	/**
	 * Broadcast actions.
	 */
	public static String BROADCAST_REFRESH_AVATAR = "broadcast_refresh_avatar";
	
	private static final int AVATAR_EDGE_LEN = 180;
	
	/**
	 * Draw avatar image for an image view.
	 * @param iv
	 * @param username
	 * @return boolean
	 */
	public static boolean drawAvatar (ImageView iv, String username) {
		boolean success = false;
		
		if (isAvatarExistInStorage(username)) {
			Bitmap bmp = getAvatarFromStorage(username);
			
			if (bmp != null && bmp.getWidth() > 10 && bmp.getHeight() > 10) {
				iv.setImageBitmap(bmp);
				success = true;
			} else {
				MediaFileManager.deleteAvatarFile(username);
			}
			bmp = null;
		}
		
		return success;
	}
	
	/**
	 * Draw avatar image for an image view. If no such avatar image, draw with default.
	 * @param iv
	 * @param username
	 * @param rounded
	 */
	public static void drawAvatar (ImageView iv, String username, boolean rounded) {
		Bitmap bmp = getAvatarFromStorage(username);
		if (bmp == null) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 2;
			bmp = BitmapFactory.decodeResource( ApplicationManager.getAppContext().getResources(),
					  							R.drawable.avatar_default, opts );
		}
		if (rounded)
			bmp = ImageProcessor.getRoundedCornerBitmap(bmp, (int)(DeviceManager.pixelsFromDpForX(15)));
		iv.setImageBitmap(bmp);
	}
	
	/**
	 * Get avatar bitmap. If no such avatar image, return default.
	 * @param username
	 * @param rounded
	 * @return
	 */
	public static Bitmap getAvatarBitmap (String username, boolean rounded) {
		Bitmap bmp = getAvatarFromStorage(username);
		if (bmp == null) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 2;
			bmp = BitmapFactory.decodeResource(ApplicationManager.getAppContext().getResources(),
											  R.drawable.avatar_default, opts);
		}
		return rounded 
			   ? ImageProcessor.getRoundedCornerBitmap(bmp, (int)(DeviceManager.pixelsFromDpForX(15)))
			   : bmp;
	}
	
	/**
	 * Get avatar bitmap on the caller's thread.
	 * @param username
	 * @return
	 */
	public static Bitmap getAvatarBitmap (String username) {
		Bitmap bmp = null;
    	if (isAvatarExistInStorage(username))
    		bmp = getAvatarFromStorage(username);
    	else
    		bmp = downloadAvatar(username);
    	
    	return bmp;
	}
	
	/**
	 * Check if avatar exists in storage.
	 * @param username
	 * @return boolean
	 */
	public static boolean isAvatarExistInStorage (String username) {
		return MediaFileManager.isAvatarFileExist(username);
	}
	
	/**
	 * Get user's avatar image by username from storage.
	 * @param username
	 * @return Bitmap
	 */
	public static Bitmap getAvatarFromStorage (String username) {
		return MediaFileManager.getAvatarFile(username);
	}
	
	/**
	 * Download avatar from server on the caller's thread. If avatar not null, save it to storage.
	 * @param username
	 * @return Bitmap
	 */
	public static Bitmap downloadAvatar (String username) {
		// download from server
		Bitmap image = NetworkManager.getAvatar(username, AVATAR_EDGE_LEN, AVATAR_EDGE_LEN);
		if (image != null) {
			// save to storage
			MediaFileManager.saveAvatarFile(username, image);
		}
		
		return image;
	}
	
	/**
	 * Upload avatar.
	 * @param filePath
	 * @return
	 */
	public static boolean uploadAvatar (String filePath) {
		// set http request URI
		//
		String URI = NetworkManager.getBaseUrl() + "/member/user/avatarupload/" + 
					 AccountManager.getCurrentUser().getUsername();
		
		// send http request
		//
		HashMap<String, String> response = NetworkManager.uploadImage(URI, filePath);
		
		return response.get("code").equals("200");
	}
	
	public static void deleteUserAvatar () {
		// delete avatar file
		MediaFileManager.deleteAvatarFile(AccountManager.getCurrentUser().getUsername());
		
		// broadcast refresh avatar
		Intent i = new Intent();
		i.setAction(BROADCAST_REFRESH_AVATAR);
		ApplicationManager.getAppContext().sendBroadcast(i);
	}
}
	