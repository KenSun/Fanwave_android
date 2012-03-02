package com.wildmind.fanwave.media;

import java.io.File;

import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.activity.R;

import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;

public class AttachImageManager {
	
	/**
	 * Draw attach image for an image view.
	 * @param iv
	 * @param token
	 * @param is_thumb
	 * @return boolean
	 */
	public static boolean drawAttach (ImageView iv, String token, boolean is_thumb) {
		boolean success = false;
		
		if (isAttachImageExistInStorage(token, is_thumb)) {
			Bitmap bmp = getAttachImageFromStorage(token, is_thumb);
			
			if (bmp != null && bmp.getWidth() > 10 && bmp.getHeight() > 10) {
				iv.setImageBitmap(bmp);
				success = true;
			} else {
				iv.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.attach_default));
				MediaFileManager.deleteAttachImageFile(token, is_thumb);
			}
			bmp = null;
		}
		
		return success;
	}

	/**
	 * Get attach bitmap on the caller's thread.
	 * @param token
	 * @param is_thumb
	 * @return
	 */
	public static Bitmap getAttachBitmap (String token, boolean is_thumb) {
		Bitmap bmp = null;
    	if (isAttachImageExistInStorage(token, is_thumb))
    		bmp = getAttachImageFromStorage(token, is_thumb);
    	else
    		bmp = downloadAttachImage(token, is_thumb);
    	
    	return bmp;
	}
	
	/**
	 * Check if attach exists in storage.
	 * @param token
	 * @param is_thumb
	 * @return boolean
	 */
	public static boolean isAttachImageExistInStorage (String token, boolean is_thumb) {
		return MediaFileManager.isAttachImageFileExist(token, is_thumb);
	}
	
	/**
	 * Get attach image by token from storage.
	 * @param token
	 * @param is_thumb
	 * @return Bitmap
	 */
	public static Bitmap getAttachImageFromStorage (String token, boolean is_thumb) {
		return MediaFileManager.getAttachImageFile(token, is_thumb);
	}
	
	/**
	 * Download attach image from server. If image not null, save it to storage.
	 * @param token
	 * @param is_thumb
	 * @return Bitmap
	 */
	public static Bitmap downloadAttachImage (String token, boolean is_thumb) {
		// download from server
		Bitmap image = NetworkManager.getAttachImage(token, is_thumb);
		if (image != null) {
			// save to storage
			MediaFileManager.saveAttachImageFile(token, image, is_thumb);
		}
		
		return image;
	}
	
	/**
	 * Build attach image key.
	 * @param token
	 * @param is_thumb
	 * @return
	 */
	public static String buildAttachImageKey (String token, boolean is_thumb) {
		return token + (is_thumb ? "_thumb" : "");
	}
	
	/**
	 * Generate a file.
	 * @return
	 */
	public static File generaterFile () {
		// create directory if not exist
		String dirPath = Environment.getExternalStorageDirectory() + "/Fanwave/Attach/User";
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		return new File(Environment.getExternalStorageDirectory(), 
						"/Fanwave/Attach/User/" + System.currentTimeMillis() + ".jpg");
	}
}
