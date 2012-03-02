package com.wildmind.fanwave.media;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.wildmind.fanwave.media.ImageManager.SampleBase;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.vendor.VendorManager;

public class ChannelIconManager {
	
	/**
	 * Draw channel icon for an image view.
	 * @param iv
	 * @param chcode
	 * @return boolean
	 */
	public static boolean drawChannelIcon (ImageView iv, String chcode) {
		boolean success = false;
		
		if (isChannelIconExistInStorage(chcode)) {
			Bitmap bmp = getChannelIconFromStorage(chcode);
			
			if (bmp != null && bmp.getWidth() > 10 && bmp.getHeight() > 10) {
				iv.setImageBitmap(bmp);
				success = true;
			} else {
				MediaFileManager.deleteChannelIconFile(chcode);
			}
			bmp = null;
		}
		return success;
	}

	/**
	 * Get channel icon bitmap on the caller's thread.
	 * @param chcode
	 * @return Bitmap
	 */
	public static Bitmap getChannelIconBitmap (String chcode) {
		Bitmap bmp = null;
    	if (isChannelIconExistInStorage(chcode))
    		bmp = getChannelIconFromStorage(chcode);
    	else
    		bmp = downloadChannelIcon(chcode);
    	
    	return bmp;
	}
	
	/**
	 * Check if channel icon exists in disk.
	 * @param chcode
	 * @return boolean
	 */
	public static boolean isChannelIconExistInStorage (String chcode) {
		return MediaFileManager.isChannelIconFileExist(chcode);
	}
	
	/**
	 * Get user's channel icon by program chcode from storage.
	 * @param chcode
	 * @return Bitmap
	 */
	public static Bitmap getChannelIconFromStorage (String chcode) {
		return MediaFileManager.getChannelIconFile(chcode);
	}
	
	/**
	 * Download channel icon from server. If program icon not null, save it to storage and insert to cache.
	 * @param chcode
	 * @return Bitmap
	 */
	public static Bitmap downloadChannelIcon (String chcode) {
		// download from server
		Bitmap image = NetworkManager.downloadImage(buildChannelIconUrl(chcode), SampleBase.UNSAMPLED);
		if (image != null) {
			// save to storage
			MediaFileManager.saveChannelIconFile(chcode, image);
		}
		
		return image;
	}
	
	public static String buildChannelIconUrl (String channel_code) {
		return "http://fanwave.tv/chlogo/" + VendorManager.getCountry() + "/" + channel_code.split("_")[1] + ".png";
	}
}