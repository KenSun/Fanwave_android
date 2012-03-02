package com.wildmind.fanwave.media;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.wildmind.fanwave.network.NetworkManager;

public class UrlImageManager {

	/**
	 * Draw image from url for an image view.
	 * @param iv
	 * @param imageUrl
	 * @param sampleBase
	 * @return boolean
	 */
	public static boolean drawUrlImage (ImageView iv, String imageUrl, int sampleBase) {
		boolean success = false;
		
		if (isUrlImageExistInStorage(imageUrl, sampleBase)) {
			Bitmap bmp = getUrlImageFromStorage(imageUrl, sampleBase);
			
			if (bmp != null && bmp.getWidth() > 10 && bmp.getHeight() > 10) {
				iv.setImageBitmap(bmp);
				success = true;
			} else {
				MediaFileManager.deleteUrlImageFile(imageUrl, sampleBase);
			}
			bmp = null;
		}
		
		return success;
	}
	
	/**
	 * Get bitmap on the caller's thread.
	 * @param imageUrl
	 * @param sampleBase
	 * @return Bitmap
	 */
	public static Bitmap getUrlImageBitmap (String imageUrl, int sampleBase) {
		Bitmap bmp = null;
    	if (isUrlImageExistInStorage(imageUrl, sampleBase))
    		bmp = getUrlImageFromStorage(imageUrl, sampleBase);
    	else
    		bmp = downloadUrlImage(imageUrl, sampleBase);
    	
    	return bmp;
	}
	
	/**
	 * Check if url image exists in storage.
	 * @param imageUrl
	 * @param sampleBase
	 * @return
	 */
	public static boolean isUrlImageExistInStorage (String imageUrl, int sampleBase) {
		return MediaFileManager.isUrlImageFileExist(imageUrl, sampleBase);
	}
	
	/**
	 * Get url image from storage.
	 * @param imageUrl
	 * @param sampleBase
	 * @return
	 */
	public static Bitmap getUrlImageFromStorage (String imageUrl, int sampleBase) {
		return MediaFileManager.getUrlImageFile(imageUrl, sampleBase);
	}
	
	/**
	 * Download url image from server on the caller's thread. If image is not null, save it to storage.
	 * @param imageUrl
	 * @param sampleBase
	 * @return
	 */
	public static Bitmap downloadUrlImage (String imageUrl, int sampleBase) {
		// download from server
		Bitmap image = NetworkManager.downloadImage(imageUrl, sampleBase);
		if (image != null) {
			// save to storage
			MediaFileManager.saveUrlImageFile(imageUrl, sampleBase, image);
		}
				
		return image;
	}
}
