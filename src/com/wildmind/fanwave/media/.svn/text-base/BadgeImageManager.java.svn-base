package com.wildmind.fanwave.media;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.wildmind.fanwave.network.NetworkManager;

public class BadgeImageManager {
	
	private static final int BADGE_EDGE_LEN = 180;
	
	/**
	 * Draw badge image for an image view.
	 * @param iv
	 * @param badge_id
	 * @return boolean
	 */
	public static boolean drawBadge (ImageView iv, String badge_id) {
		boolean success = false;
		
		if (isBadgeImageExistInStorage(badge_id)) {
			Bitmap bmp = getBadgeImageFromStorage(badge_id);
			
			if (bmp != null && bmp.getWidth() > 10 && bmp.getHeight() > 10) {
				iv.setImageBitmap(bmp);
				success = true;
			} else {
				MediaFileManager.deleteBadgeImageFile(badge_id);
			}
			bmp = null;
		}
		
		return success;
	}
	
	/**
	 * Draw scaled badge image for an image view.
	 * @param iv
	 * @param badge_id
	 * @return boolean
	 */
	public static boolean drawScaledBadge (ImageView iv, String badge_id) {
		boolean success = false;
		
		if (isScaledBadgeImageExistInStorage(badge_id)) {
			Bitmap bmp = getScaledBadgeImageFromStorage(badge_id);
			
			if (bmp != null && bmp.getWidth() > 10 && bmp.getHeight() > 10) {
				iv.setImageBitmap(bmp);
				success = true;
			} else {
				MediaFileManager.deleteScaledBadgeImageFile(badge_id, BADGE_EDGE_LEN, BADGE_EDGE_LEN);
			}
			bmp = null;
		}
		
		return success;
	}

	/**
	 * Get badge bitmap on the caller's thread.
	 * @param badge_id
	 * @return
	 */
	public static Bitmap getBadgeBitmap (String badge_id) {
		Bitmap bmp = null;
    	if (isBadgeImageExistInStorage(badge_id))
    		bmp = getBadgeImageFromStorage(badge_id);
    	else
    		bmp = downloadBadgeImage(badge_id);
    	
    	return bmp;
	}
	
	/**
	 * Get scaled badge bitmap on the caller's thread.
	 * @param badge_id
	 * @return
	 */
	public static Bitmap getScaledBadgeBitmap (String badge_id) {
		Bitmap bmp = null;
    	if (isScaledBadgeImageExistInStorage(badge_id))
    		bmp = getScaledBadgeImageFromStorage(badge_id);
    	else
    		bmp = downloadScaledBadgeImage(badge_id);
    	
    	return bmp;
	}
	
	/**
	 * Check if badge exists in storage.
	 * @param badge_id
	 * @return boolean
	 */
	public static boolean isBadgeImageExistInStorage (String badge_id) {
		return MediaFileManager.isBadgeImageFileExist(badge_id);
	}
	
	/**
	 * Check if scaled badge exists in storage.
	 * @param badge_id
	 * @return boolean
	 */
	public static boolean isScaledBadgeImageExistInStorage (String badge_id) {
		return MediaFileManager.isScaledBadgeImageFileExist(badge_id, BADGE_EDGE_LEN, BADGE_EDGE_LEN);
	}
	
	/**
	 * Get badge image by badge_id from storage.
	 * @param badge_id
	 * @return Bitmap
	 */
	public static Bitmap getBadgeImageFromStorage (String badge_id) {
		return MediaFileManager.getBadgeImageFile(badge_id);
	}
	
	/**
	 * Get sacled badge image by badge_id from storage.
	 * @param badge_id
	 * @return Bitmap
	 */
	public static Bitmap getScaledBadgeImageFromStorage (String badge_id) {
		return MediaFileManager.getScaledBadgeImageFile(badge_id, BADGE_EDGE_LEN, BADGE_EDGE_LEN);
	}
	
	/**
	 * Download badge from server. If badge not null, save it to storage.
	 * @param badge_id
	 * @return Bitmap
	 */
	public static Bitmap downloadBadgeImage (String badge_id) {
		// download from server
		Bitmap image = NetworkManager.getBadgeImage(badge_id);
		if (image != null) {
			// save to storage
			MediaFileManager.saveBadgeImageFile(badge_id, image);
		}
		
		return image;
	}
	
	/**
	 * Download scaled badge from server. If badge not null, save it to storage.
	 * @param badge_id
	 * @return Bitmap
	 */
	public static Bitmap downloadScaledBadgeImage (String badge_id) {
		// download from server
		Bitmap image = NetworkManager.getBadgeImage(badge_id, BADGE_EDGE_LEN, BADGE_EDGE_LEN);
		if (image != null) {
			// save to storage
			MediaFileManager.saveScaledBadgeImageFile(badge_id, image, BADGE_EDGE_LEN, BADGE_EDGE_LEN);
		}
		
		return image;
	}
}