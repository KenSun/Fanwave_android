package com.wildmind.fanwave.media;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.wildmind.fanwave.app.ApplicationManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class ImageManager {
	
	private static final int LOADER_THREAD_COUNT = 5;
	
	private static ArrayList<ImageLoader> image_loader_threads = new ArrayList<ImageLoader>();
	private static Stack<ImageRequest> image_requests = new Stack<ImageRequest>();
	
	private static ConcurrentHashMap<String, String> avatar_request_list = new ConcurrentHashMap<String, String>();
	private static ConcurrentHashMap<String, String> badge_request_list = new ConcurrentHashMap<String, String>();
	private static ConcurrentHashMap<String, String> attach_request_list = new ConcurrentHashMap<String, String>();
	private static ConcurrentHashMap<String, String> pgicon_request_list = new ConcurrentHashMap<String, String>();
	private static ConcurrentHashMap<String, String> chicon_request_list = new ConcurrentHashMap<String, String>();
	private static CopyOnWriteArrayList<ImageListener> image_listeners = new CopyOnWriteArrayList<ImageListener>();
	
	public static enum ImageType {
		avatar,
		badge,
		attach,
		pgicon,
		chicon
	}
	
	static {
		for (int i = 0; i < LOADER_THREAD_COUNT; i++) {
			ImageLoader il = new ImageLoader();
			il.setPriority(Thread.NORM_PRIORITY-1);
			image_loader_threads.add(il);
			il.start();
		}
	}
	
	/**
	 * Add an image listener.
	 * @param listener
	 */
	public static void addImageListener (ImageListener listener) {
		image_listeners.add(listener);
	}
	
	/**
	 * Remove an image listener.
	 * @param listener
	 */
	public static void removeImageListener (ImageListener listener) {
		image_listeners.remove(listener);
	}
	
	/**
	 * Get bitmap from resources.
	 * @param resourceId
	 * @return Bitmap
	 */
	public static Bitmap getBitmapFromResources (int resourceId) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = false;
		opts.inPurgeable = true;
		opts.inInputShareable = true;
		InputStream is = ApplicationManager.getAppContext().getResources().openRawResource(resourceId);
		
		return BitmapFactory.decodeStream(is, null, opts);
	}
	
	/**
	 * Draw avatar image. If failed, enqueue the request.
	 * @param iv
	 * @param username
	 * @return
	 */
	public static boolean drawAvatarImage (ImageView iv, String username) {
		boolean success = AvatarManager.drawAvatar(iv, username);
		if (!success)
			queueImageRequest(ImageType.avatar, username, null);
		
		return success;
	}
	
	/**
	 * Draw badge image. If failed, enqueue the request.
	 * @param iv
	 * @param badge_id
	 * @param scaled
	 * @return
	 */
	public static boolean drawBadgeImage (ImageView iv, String badge_id, boolean scaled) {
		boolean success = scaled ? BadgeImageManager.drawScaledBadge(iv, badge_id) 
								 : BadgeImageManager.drawBadge(iv, badge_id);
		if (!success)
			queueImageRequest(ImageType.badge, badge_id, Boolean.valueOf(scaled));
		
		return success;
	}
	
	/**
	 * Draw attach image. If failed, enqueue the request.
	 * @param iv
	 * @param token
	 * @param is_thumb
	 * @return
	 */
	public static boolean drawAttachImage (ImageView iv, String token, boolean is_thumb) {
		boolean success = AttachImageManager.drawAttach(iv, token, is_thumb);
		if (!success)
			queueImageRequest(ImageType.attach, token, Boolean.valueOf(is_thumb));
		
		return success;
	}
	
	/**
	 * Draw program icon. If failed, enqueue the request.
	 * @param iv	if null, do all procedure without drawing image view
	 * @param title
	 * @param icon_url
	 * @param sampleBase
	 * @return
	 */
	public static boolean drawProgramIcon (ImageView iv, String title, String icon_url, int sampleBase) {
		boolean success = ProgramIconManager.drawProgramIcon(iv, title, sampleBase);
		if (!success)
			queueImageRequest(ImageType.pgicon, title, icon_url, sampleBase);
		
		return success;
	}
	
	/**
	 * Draw channel icon. If failed, enqueue the request.
	 * @param iv
	 * @param chcode
	 * @return
	 */
	public static boolean drawChannelIcon (ImageView iv, String chcode) {
		boolean success = ChannelIconManager.drawChannelIcon(iv, chcode);
		if (!success)
			queueImageRequest(ImageType.chicon, chcode, null);
		
		return success;
	}
	
	
	/**
	 * Trigger retrieve avatar event.
	 * @param username
	 * @param bmp
	 */
	private static void fireRetrieveAvatarEvent (String username, Bitmap bmp) {
		for (ImageListener listener:image_listeners) {
			if (listener != null)
				listener.retrieveAvatar(username, bmp);
		}
	}
	

	
	/**
	 * Trigger retrieve badge event.
	 * @param badge_id
	 * @param bmp
	 */
	private static void fireRetrieveBadgeEvent (String badge_id, boolean scaled, Bitmap bmp) {
		for (ImageListener listener:image_listeners) {
			if (listener != null)
				listener.retrieveBadge(badge_id, scaled, bmp);
		}
	}
	
	/**
	 * Trigger retrieve attach event.
	 * @param token
	 * @param is_thumb
	 * @param bmp
	 */
	private static void fireRetrieveAttachEvent (String token, boolean is_thumb, Bitmap bmp) {
		for (ImageListener listener:image_listeners) {
			if (listener != null)
				listener.retrieveAttach(token, is_thumb, bmp);
		}
	}
	
	
	/**
	 * Trigger retrieve program icon event.
	 * @param title
	 * @param sampleBase
	 * @param bmp
	 */
	private static void fireRetrieveProgramIconEvent ( String title, int sampleBase, Bitmap bmp) {
		for (ImageListener listener:image_listeners) {
			if (listener != null)
				listener.retrieveProgramIcon(title, sampleBase, bmp);
		}
	}
	
	
	/**
	 * Trigger retrieve channel icon event.
	 * @param title
	 * @param sampleBase
	 * @param bmp
	 */
	private static void fireRetrieveChannelIconEvent ( String chcode, Bitmap bmp) {
		for (ImageListener listener:image_listeners) {
			if (listener != null)
				listener.retrieveChannelIcon(chcode, bmp);
		}
	}
	
	
//////////////////////////////////////////////////////////////////////////////
//Queue Methods
//////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Enqueue an image request.
	 * @param type
	 * @param key
	 * @param tag
	 */
	private static void queueImageRequest (ImageType type, String key, Object tag) {
		queueImageRequest(type, key, tag, SampleBase.IGNORE_SAMPLED);
	}
	
	/**
	 * Enqueue an image request.
	 * @param type
	 * @param key
	 * @param tag
	 * @param sampleBase
	 */
	private static void queueImageRequest (ImageType type, String key, Object tag, int sampleBase) {
		if (type == ImageType.avatar)
			queueAvatarRequest(type, key, sampleBase);
		else if (type == ImageType.badge)
			queueBadgeRequest(type, key, (Boolean) tag, sampleBase);
		else if (type == ImageType.attach)
			queueAttachRequest(type, key, (Boolean) tag, sampleBase);
		else if (type == ImageType.pgicon)
			queueProgramIconRequest(type, key, (String) tag, sampleBase);
		else if (type == ImageType.chicon)
			queueChannelIconRequest(type, key, sampleBase);
	}
	
	/**
	 * Enqueue an avatar request.
	 * @param type
	 * @param username
	 */
	private static void queueAvatarRequest (ImageType type, String username, int sampleBase) {
		if (avatar_request_list.containsKey(username))
			return;
		
		ImageRequest ir = new ImageRequest(type, username, null, sampleBase);
		
        synchronized(image_requests){
        	avatar_request_list.put(username, "");
        	image_requests.push(ir);
        	image_requests.notify();
        }
	}
	
	/**
	 * Enqueue a badge request.
	 * @param type
	 * @param badge_id
	 */
	private static void queueBadgeRequest (ImageType type, String badge_id, Boolean scaled, int sampleBase) {
		if (badge_request_list.containsKey(badge_id))
			return;
		
		ImageRequest ir = new ImageRequest(type, badge_id, scaled, sampleBase);
		
        synchronized(image_requests){
        	badge_request_list.put(badge_id, "");
        	image_requests.push(ir);
        	image_requests.notify();
        }
	}
	
	/**
	 * Enqueue an attach request.
	 * @param type
	 * @param token
	 * @param is_thumb
	 */
	private static void queueAttachRequest (ImageType type, String token, Boolean is_thumb, int sampleBase) {
		if (attach_request_list.containsKey(token))
			return;
		
		ImageRequest ir = new ImageRequest(type, token, is_thumb, sampleBase);
		
        synchronized(image_requests){
        	attach_request_list.put(AttachImageManager.buildAttachImageKey(token, is_thumb), "");
        	image_requests.push(ir);
        	image_requests.notify();
        }
	}
	
	/**
	 * Enqueue a program icon request.
	 * @param type
	 * @param title
	 * @param icon_url
	 * @param sampleBase
	 */
	private static void queueProgramIconRequest (ImageType type, String title, String icon_url, int sampleBase) {
		String key = ProgramIconManager.buildProgramIconKey(title, sampleBase);
		if (pgicon_request_list.containsKey(key))
			return;

		ImageRequest ir = new ImageRequest(type, title, icon_url, sampleBase);
		
        synchronized(image_requests){
        	pgicon_request_list.put(key, "");
        	image_requests.push(ir);
        	image_requests.notify();
        }
	}
	
	/**
	 * Enqueue a channel icon request.
	 * @param type
	 * @param chcode
	 * @param icon_url
	 * @param sampleBase
	 */
	private static void queueChannelIconRequest (ImageType type, String chcode, int sampleBase) {
		if (chicon_request_list.containsKey(chcode))
			return;

		ImageRequest ir = new ImageRequest(type, chcode, null, sampleBase);
		
        synchronized(image_requests){
        	chicon_request_list.put(chcode, "");
        	image_requests.push(ir);
        	image_requests.notify();
        }
	}
	
	/**
	 * Handle an avatar request.
	 * @param ir
	 */
	private static void handleAvatarRequest (ImageRequest ir) {
		String username = ir.getKey();
		Bitmap bmp = AvatarManager.getAvatarBitmap(username);
    	avatar_request_list.remove(username);
    	fireRetrieveAvatarEvent(username, bmp);
	}
	
	/**
	 * Handle a badge request.
	 * @param ir
	 */
	private static void handleBadgeRequest (ImageRequest ir) {
		String badge_id = ir.getKey();
		boolean scaled = (Boolean) ir.getTag();
		Bitmap bmp = scaled ? BadgeImageManager.getScaledBadgeBitmap(badge_id) 
							: BadgeImageManager.getBadgeBitmap(badge_id);
		badge_request_list.remove(badge_id);
		fireRetrieveBadgeEvent(badge_id, scaled, bmp);
	}
	
	/**
	 * Handle an attach request.
	 * @param ir
	 */
	private static void handleAttachRequest (ImageRequest ir) {
		String token = ir.getKey();
		boolean is_thumb = (Boolean) ir.getTag();
		Bitmap bmp = AttachImageManager.getAttachBitmap(token, is_thumb);
		attach_request_list.remove(AttachImageManager.buildAttachImageKey(token, is_thumb));
		fireRetrieveAttachEvent(token, is_thumb, bmp);
	}
	
	/**
	 * Handle a program icon request.
	 * @param ir
	 */
	private static void handleProgramIconRequest (ImageRequest ir) {
		String title = ir.getKey();
		String icon_url = (String) ir.getTag();
		int sampleBase = ir.getSampleBase();
		Bitmap bmp = ProgramIconManager.getProgramIconBitmap(title, icon_url, sampleBase);
		String key = ProgramIconManager.buildProgramIconKey(title, sampleBase);
		pgicon_request_list.remove(key);
		fireRetrieveProgramIconEvent(title, sampleBase, bmp);
	}
	/**
	 * Handle a channel icon request.
	 * @param ir
	 */
	private static void handleChannelIconRequest (ImageRequest ir) {
		String chcode = ir.getKey();
		Bitmap bmp = ChannelIconManager.getChannelIconBitmap(chcode);
		chicon_request_list.remove(chcode);
		fireRetrieveChannelIconEvent(chcode, bmp);
	}
	
	/**
	 * ImageRequest Class.
	 * @author Kencool
	 *
	 */
	static class ImageRequest {
		
		private ImageType type;
		private String key;
		private Object tag;
		private int sample_base = SampleBase.IGNORE_SAMPLED;
		
		public ImageRequest (ImageType type, String key, Object tag, int sample_base) {
			this.type = type;
			this.key = key;
			this.tag = tag;
			this.sample_base = sample_base;
		}
		public ImageType getImageType () {
			return type;
		}
		public String getKey () {
			return key;
		}
		public Object getTag () {
			return tag;
		}
		public int getSampleBase () {
			return sample_base;
		}
	}
	
	/**
	 * ImageLoader Class.
	 * @author Kencool
	 *
	 */
	static class ImageLoader extends Thread {
		public void run() {
            try {
                while(true)
                {	
                    if(image_requests.size() == 0) {
                        synchronized(image_requests) {
                        	image_requests.wait();
                        }
                    }
                    
                    if(image_requests.size() > 0) {
                    	ImageRequest ir;
                        synchronized(image_requests){
                        	ir = image_requests.pop();
                        }
                        
                        if (ir.getImageType() == ImageType.avatar)
                        	handleAvatarRequest(ir);
                        else if (ir.getImageType() == ImageType.badge)
                        	handleBadgeRequest(ir);
                        else if (ir.getImageType() == ImageType.attach)
                        	handleAttachRequest(ir);
                        else if (ir.getImageType() == ImageType.pgicon)
                        	handleProgramIconRequest(ir);
                        else if (ir.getImageType() == ImageType.chicon)
                        	handleChannelIconRequest(ir);
                    }
                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
	}
	
	public static class SampleBase {
		public static final int IGNORE_SAMPLED		= -1;
		public static final int UNSAMPLED 			= 0;
		public static final int RIGOROUS_SAMPLED 	= 200;
		public static final int HIGH_SAMPLED 		= 400;
		public static final int MEDIUM_SAMPLED 		= 600;
		public static final int LOW_SAMPLED 		= 800;
		public static final int ROUGH_SAMPLED 		= 1200;
		public static final int LIGHT_SAMPLED 		= 1600;
	}
}
