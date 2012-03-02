package com.wildmind.fanwave.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.device.DeviceManager;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.util.ImageProcessor;

public class ProgramIconManager {
	
	/**
	 * Draw program icon for an image view.
	 * @param iv
	 * @param title
	 * @param sampleBase
	 * @return boolean
	 */
	public static boolean drawProgramIcon (ImageView iv, String title, int sampleBase) {
		boolean success = false;
		
		if (isProgramIconExistInStorage(title, sampleBase)) {
			Bitmap bmp = getProgramIconFromStorage(title, sampleBase);
			
			if (bmp != null && bmp.getWidth() > 10 && bmp.getHeight() > 10) {
				if (iv != null)
					iv.setImageBitmap(bmp);
				success = true;
			} else {
				MediaFileManager.deleteProgramIconFile(title, sampleBase);
			}
			bmp = null;
		}
		return success;
	}
	
	/**
	 * Draw program icon for an image view. If no such program icon, draw with default.
	 * @param iv
	 * @param title
	 * @param sampleBase
	 * @param rounded
	 */
	public static void drawProgramIcon (ImageView iv, String title, int sampleBase, boolean rounded) {
		Bitmap bmp = getProgramIconFromStorage(title, sampleBase);
		if (bmp == null) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 2;
			bmp =  BitmapFactory.decodeResource(ApplicationManager.getAppContext().getResources(),
												R.drawable.program_default, opts);
		}
		if (rounded)
			bmp = ImageProcessor.getRoundedCornerBitmap(bmp, (int)(DeviceManager.pixelsFromDpForX(15)));
		iv.setImageBitmap(bmp);
	}
	
	/**
	 * Get program icon bitmap. If no such progarm icon, return default.
	 * @param title
	 * @param sampleBase
	 * @param rounded
	 * @return
	 */
	public static Bitmap getProgramIconBitmap (String title, int sampleBase, boolean rounded) {
		Bitmap bmp = getProgramIconFromStorage(title, sampleBase);
		if (bmp == null) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inSampleSize = 2;
			bmp = BitmapFactory.decodeResource( ApplicationManager.getAppContext().getResources(),
												R.drawable.program_default, opts );
		}
		return rounded 
				   ? ImageProcessor.getRoundedCornerBitmap(bmp, (int)(DeviceManager.pixelsFromDpForX(15)))
				   : bmp;
	}

	/**
	 * Get program icon bitmap on the caller's thread.
	 * @param title
	 * @param icon_url
	 * @param sampleBase
	 * @return
	 */
	public static Bitmap getProgramIconBitmap (String title, String icon_url, int sampleBase) {
		Bitmap bmp = null;
    	if (isProgramIconExistInStorage(title, sampleBase))
    		bmp = getProgramIconFromStorage(title, sampleBase);
    	else
    		bmp = downloadProgramIcon(title, icon_url, sampleBase);
    	
    	return bmp;
	}
	
	/**
	 * Check if program icon exists in storage.
	 * @param title
	 * @param sampleBase
	 * @return boolean
	 */
	public static boolean isProgramIconExistInStorage (String title, int sampleBase) {
		return MediaFileManager.isProgramIconFileExist(title, sampleBase);
	}
	
	/**
	 * Get user's program icon by program title from storage.
	 * @param title
	 * @param sampleBase
	 * @return Bitmap
	 */
	public static Bitmap getProgramIconFromStorage (String title, int sampleBase) {
		return MediaFileManager.getProgramIconFile(title, sampleBase);
	}
	
	/**
	 * Download program icon from server. If program icon not null, save it to storage.
	 * @param title
	 * @param icon_url
	 * @param sampleBase
	 * @return Bitmap
	 */
	public static Bitmap downloadProgramIcon (String title, String icon_url, int sampleBase) {
		// download from server
		Bitmap image = NetworkManager.downloadImage(icon_url, sampleBase);
		if (image != null) {
			// save to storage
			MediaFileManager.saveProgramIconFile(title, image, sampleBase);
		}
		
		return image;
	}
	
	/**
	 * Build program icon key for title and sample base.
	 * @param title
	 * @param sampleBase
	 * @return
	 */
	public static String buildProgramIconKey (String title, int sampleBase) {
		return title + "_" + sampleBase;
	}
}