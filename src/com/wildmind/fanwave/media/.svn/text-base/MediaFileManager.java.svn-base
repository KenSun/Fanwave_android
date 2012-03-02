package com.wildmind.fanwave.media;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class MediaFileManager {

	public static final String FANWAVE_DIRECTORY = "/.Fanwave";
	
	static {
		// create Fanwave folder
		String dirPath = Environment.getExternalStorageDirectory() + FANWAVE_DIRECTORY;
		File dir = new File(dirPath);
		if (!dir.exists())
			dir.mkdir();
		
		// create .nomedia for hiding files from the Media Scanner
		String filePath = Environment.getExternalStorageDirectory() + FANWAVE_DIRECTORY + "/.nomedia";
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				byte[] data = new byte[0];
				OutputStream os = new FileOutputStream(file);
				os.write(data);
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void cleanMediaFiles () {
		deleteAvatarDirectory();
		deleteBadgeImageDirectory();
		deleteAttachImageDirectory();
		deleteProgramIconDirectory();
		deleteUrlImageDirectory();
	}
	
	/**
	 * Recursively delete directory.
	 * @param dirPath
	 * @return boolean
	 */
	public static boolean deleteDirectory (String dirPath) {
		File dir = new File(dirPath);
		if (dir.isDirectory()) {
			String[] filenames = dir.list();
			for (int i = 0; i < filenames.length; i++) {
				if (!deleteDirectory(dirPath + "/" + filenames[i]))
					return false;
			}
		}
		return dir.delete();
	}
	
	/**
	 * Delete image file.
	 * @param filePath
	 * @return
	 */
	public static boolean deleteImageFile (String filePath) {
		File file = new File(filePath);
		if (file.isFile()) {
			return file.delete();
		} else
			return false;
	}
	
	/**
	 * Save image file to internal storage with filePath.
	 * @param filePath
	 * @param image
	 */
	public static void saveImageFile (String filePath, Bitmap image) {
		File file = new File(filePath);
		if (file.exists())
			file.delete();
		try {
			FileOutputStream fos = new FileOutputStream(file, true);
			image.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get image file from internal storage with filePath. If file not exists, return null.
	 * @param filePath
	 * @param sampleSize
	 * @return Bitmap
	 */
	public static Bitmap getImageFile (String filePath, int sampleSize) {
		File file = new File(filePath);
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = sampleSize;
		return file.exists() ? BitmapFactory.decodeStream(bis, null, opts) : null;
	}
	
	/**
	 * Check if image file exists.
	 * @param filePath
	 * @return boolean
	 */
	public static boolean isImageFileExist (String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
//Avatar File Process Methods
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void deleteAvatarFile (String username) {
		deleteImageFile(getAvatarFilePath(username));
	}
	
	/**
	 * Save avatar image to internal storage with username.
	 * @param username
	 * @param image
	 */
	public static void saveAvatarFile (String username, Bitmap image) {
		saveImageFile(getAvatarFilePath(username), image);
	}
	
	/**
	 * Get avatar image file from internal storage with username. If avatar not exists, return null.
	 * @param username
	 * @return Bitmap
	 */
	public static Bitmap getAvatarFile (String username) {
		return getImageFile(getAvatarFilePath(username), 1);
	}
	
	/**
	 * Check if avatar file exists with username.
	 * @param username
	 * @return boolean
	 */
	public static boolean isAvatarFileExist (String username) {
		return isImageFileExist(getAvatarFilePath(username));
	}
	
	/**
	 * Get avatar directory absolute path.
	 * @return String
	 */
	public static String getAvatarDirectoryPath () {
		String dirPath = Environment.getExternalStorageDirectory() + FANWAVE_DIRECTORY + "/Avatar";
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		return dirPath;
	}
	
	/**
	 * get avatar file absolute path.
	 * @param username
	 * @return String
	 */
	public static String getAvatarFilePath (String username) {
		return getAvatarDirectoryPath() + "/" + username + ".png";
	}
	
	/**
	 * Delete avatar directory.
	 * @return boolean
	 */
	public static boolean deleteAvatarDirectory () {
		return deleteDirectory(getAvatarDirectoryPath());
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
//	Attach Image File Process Methods
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void deleteAttachImageFile (String token, boolean is_thumb) {
		deleteImageFile(getAttachImageFilePath(token, is_thumb));
	}
	
	/**
	 * Save attach image to internal storage with token.
	 * @param token
	 * @param image
	 * @param is_thumb
	 */
	public static void saveAttachImageFile (String token, Bitmap image, boolean is_thumb) {
		saveImageFile(getAttachImageFilePath(token, is_thumb), image);
	}
	
	/**
	 * Get attach image file from internal storage with token. If attach not exists, return null.
	 * @param token
	 * @return Bitmap
	 */
	public static Bitmap getAttachImageFile (String token, boolean is_thumb) {
		return getImageFile(getAttachImageFilePath(token, is_thumb), 1);
	}
	
	/**
	 * Check if attach image file exists with token.
	 * @param token
	 * @param is_thumb
	 * @return boolean
	 */
	public static boolean isAttachImageFileExist (String token, boolean is_thumb) {
		return isImageFileExist(getAttachImageFilePath(token, is_thumb));
	}
	
	/**
	 * Get attach image directory absolute path.
	 * @return String
	 */
	public static String getAttachImageDirectoryPath () {
		String dirPath = Environment.getExternalStorageDirectory() + FANWAVE_DIRECTORY + "/Attach/Image";
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dirPath;
	}
	
	/**
	 * get attach image file absolute path.
	 * @param token
	 * @param is_thumb
	 * @return String
	 */
	public static String getAttachImageFilePath (String token, boolean is_thumb) {
		return getAttachImageDirectoryPath() + "/" + token + (is_thumb ? "_thumb" : "") + ".png";
	}
	
	/**
	 * Delete attach image directory.
	 * @param _context
	 * @return boolean
	 */
	public static boolean deleteAttachImageDirectory () {
		return deleteDirectory(getAttachImageDirectoryPath());
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
//	Badge Image File Process Methods
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void deleteBadgeImageFile (String badge_id) {
		deleteImageFile(getBadgeImageFilePath(badge_id));
	}
	
	/**
	 * Save badge image to internal storage with badge_id.
	 * @param username
	 * @param image
	 */
	public static void saveBadgeImageFile (String badge_id, Bitmap image) {
		saveImageFile(getBadgeImageFilePath(badge_id), image);
	}
	
	/**
	 * Get badge image file from internal storage with badge_id. If badge image not exists, return null.
	 * @param badge_id
	 * @return Bitmap
	 */
	public static Bitmap getBadgeImageFile (String badge_id) {
		return getImageFile(getBadgeImageFilePath(badge_id), 1);
	}
	
	/**
	 * Check if badge file exists with badge_id.
	 * @param badge_id
	 * @return boolean
	 */
	public static boolean isBadgeImageFileExist (String badge_id) {
		return isImageFileExist(getBadgeImageFilePath(badge_id));
	}
	
	/**
	 * Get badge image directory absolute path.
	 * @return String
	 */
	public static String getBadgeImageDirectoryPath () {
		String dirPath = Environment.getExternalStorageDirectory() + FANWAVE_DIRECTORY + "/Badge/Image";
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dirPath;
	}
	
	/**
	 * get badge image file absolute path.
	 * @param badge_id
	 * @return String
	 */
	public static String getBadgeImageFilePath (String badge_id) {
		return getBadgeImageDirectoryPath() + "/" + badge_id + ".png";
	}
	
	/**
	 * Delete badge image directory.
	 * @return boolean
	 */
	public static boolean deleteBadgeImageDirectory () {
		return deleteDirectory(getBadgeImageDirectoryPath());
	}
	
//////////////////////////////////////////////////////////////////////////////////////////////////
// Scaled Badge Image File Process Methods
//////////////////////////////////////////////////////////////////////////////////////////////////

	public static void deleteScaledBadgeImageFile (String badge_id, int width, int height) {
		deleteImageFile(getScaledBadgeImageFilePath(badge_id, width, height));
	}
	
	/**
	 * Save scaled badge image to internal storage with badge_id.
	 * @param username
	 * @param image
	 * @param width
	 * @param height
	 */
	public static void saveScaledBadgeImageFile (String badge_id, Bitmap image, int width, int height) {
		saveImageFile(getScaledBadgeImageFilePath(badge_id, width, height), image);
	}
	
	/**
	 * Get scaled badge image file from internal storage with badge_id. 
	 * If badge image not exists, return null.
	 * @param badge_id
	 * @param width
	 * @param height
	 * @return Bitmap
	 */
	public static Bitmap getScaledBadgeImageFile (String badge_id, int width, int height) {
		return getImageFile(getScaledBadgeImageFilePath(badge_id, width, height), 1);
	}
	
	/**
	 * Check if badge file exists with badge_id.
	 * @param badge_id
	 * @param width
	 * @param height
	 * @return boolean
	 */
	public static boolean isScaledBadgeImageFileExist (String badge_id, int width, int height) {
		return isImageFileExist(getScaledBadgeImageFilePath(badge_id, width, height));
	}
	
	/**
	 * Get scaled badge image directory absolute path.
	 * @return String
	 */
	public static String getScaledBadgeImageDirectoryPath () {
		String dirPath = Environment.getExternalStorageDirectory() + FANWAVE_DIRECTORY + "/Badge/Scaled";
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dirPath;
	}
	
	/**
	 * get scaled badge image file absolute path.
	 * @param badge_id
	 * @param width
	 * @param height
	 * @return String
	 */
	public static String getScaledBadgeImageFilePath (String badge_id, int width, int height) {
		return getScaledBadgeImageDirectoryPath() + "/" + badge_id + "_" + width + "x" + height + ".png";
	}
	
	/**
	 * Delete scaled badge image directory.
	 * @return boolean
	 */
	public static boolean deleteScaledBadgeImageDirectory () {
		return deleteDirectory(getScaledBadgeImageDirectoryPath());
	}

//////////////////////////////////////////////////////////////////////////////////////////////////
//Pgicon Image File Process Methods
//////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Delete program icon.
	 * @param title
	 * @param sampleBase
	 */
	public static void deleteProgramIconFile (String title, int sampleBase) {
		deleteImageFile(getProgramIconFilePath(title, sampleBase));
	}
	
	/**
	* Save program icon to internal storage with program title.
	* @param title
	* @param image
	* @param sampleBase
	*/
	public static void saveProgramIconFile (String title, Bitmap image, int sampleBase) {
		saveImageFile(getProgramIconFilePath(title, sampleBase), image);
	}
	
	/**
	* Get program icon file from internal storage with program title. If program icon not exists, return null.
	* @param title
	* @param sampleBase
	* @return Bitmap
	*/
	public static Bitmap getProgramIconFile (String title, int sampleBase) {
		return getImageFile(getProgramIconFilePath(title, sampleBase), 1);
	}
	
	/**
	* Check if program icon file exists with program title.
	* @param title
	* @param sampleBase
	* @return boolean
	*/
	public static boolean isProgramIconFileExist (String title, int sampleBase) {
		return isImageFileExist(getProgramIconFilePath(title, sampleBase));
	}
	
	/**
	* Get program icon directory absolute path.
	* @return String
	*/
	public static String getProgramIconDirectoryPath () {
		String dirPath = Environment.getExternalStorageDirectory() + FANWAVE_DIRECTORY + "/Program/Icon";
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dirPath;
	}
	
	/**
	* get program icon file absolute path.
	* @param title
	* @param sampleBase
	* @return String
	*/
	public static String getProgramIconFilePath (String title, int sampleBase) {
		return getProgramIconDirectoryPath() + "/" + title + "_" + sampleBase + ".png";
	}
	
	/**
	* Delete program icon directory.
	* @return boolean
	*/
	public static boolean deleteProgramIconDirectory () {
		return deleteDirectory(getProgramIconDirectoryPath());
	}	
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////
//Chicon Image File Process Methods
//////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Delete channel icon.
	 * @param chcode
	 */
	public static void deleteChannelIconFile (String chcode) {
		deleteImageFile(getChannelIconFilePath(chcode));
	}
	
	/**
	* Save channel icon to internal storage with program title.
	* @param chcode
	* @param image
	*/
	public static void saveChannelIconFile (String chcode, Bitmap image) {
		saveImageFile(getChannelIconFilePath(chcode), image);
	}
	
	/**
	* Get channel icon file from internal storage with program title. If channel icon not exists, return null.
	* @param chcode
	* @return Bitmap
	*/
	public static Bitmap getChannelIconFile (String chcode) {
		return getImageFile(getChannelIconFilePath(chcode), 1);
	}
	
	/**
	* Check if channel icon file exists with program title.
	* @param chcode
	* @return boolean
	*/
	public static boolean isChannelIconFileExist (String chcode) {
		return isImageFileExist(getChannelIconFilePath(chcode));
	}
	
	/**
	* Get channel icon directory absolute path.
	* @return String
	*/
	public static String getChannelIconDirectoryPath () {
		String dirPath = Environment.getExternalStorageDirectory() + FANWAVE_DIRECTORY + "/Channel/Icon";
		File dir = new File(dirPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dirPath;
	}
	
	/**
	* get channel icon file absolute path.
	* @param chcode
	* @return String
	*/
	public static String getChannelIconFilePath (String chcode) {
		return getChannelIconDirectoryPath() + "/" + chcode + ".png";
	}
	
	/**
	* Delete channel icon directory.
	* @return boolean
	*/
	public static boolean deleteChannelIconDirectory () {
		return deleteDirectory(getChannelIconDirectoryPath());
	}		
	
//////////////////////////////////////////////////////////////////////////////////////////////////
//Url Image File Process Methods
//////////////////////////////////////////////////////////////////////////////////////////////////

	public static void deleteUrlImageFile (String imageUrl, int sampleBase) {
		deleteImageFile(getUrlImageFilePath(imageUrl, sampleBase));
	}

	public static void saveUrlImageFile (String imageUrl, int sampleBase, Bitmap image) {
		saveImageFile(getUrlImageFilePath(imageUrl, sampleBase), image);
	}
	
	public static Bitmap getUrlImageFile (String imageUrl, int sampleBase) {
		return getImageFile(getUrlImageFilePath(imageUrl, sampleBase), 1);
	}
	
	public static boolean isUrlImageFileExist (String imageUrl, int sampleBase) {
		return isImageFileExist(getUrlImageFilePath(imageUrl, sampleBase));
	}
	
	public static String getUrlImageDirectoryPath () {
		String dirPath = Environment.getExternalStorageDirectory() + FANWAVE_DIRECTORY + "/UrlImage";
		File dir = new File(dirPath);
		if (!dir.exists())
			dir.mkdir();
	
		return dirPath;
	}
	
	public static String getUrlImageDirectoryPath (int sampleBase) {
		String dirPath = getUrlImageDirectoryPath() + "/SampleBase_" + sampleBase;
		File dir = new File(dirPath);
		if (!dir.exists())
			dir.mkdir();
	
		return dirPath;
	}
	
	public static String getUrlImageFilePath (String imageUrl, int sampleBase) {
		imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf('.'));
		imageUrl = imageUrl.replace('.', '_');
		imageUrl = imageUrl.replace('/', '_');
		imageUrl = imageUrl.replace(':', '_');
		return getUrlImageDirectoryPath(sampleBase) + "/" + imageUrl + ".png";
	}
	
	public static boolean deleteUrlImageDirectory () {
		return deleteDirectory(getUrlImageDirectoryPath());
	}
	
}	

