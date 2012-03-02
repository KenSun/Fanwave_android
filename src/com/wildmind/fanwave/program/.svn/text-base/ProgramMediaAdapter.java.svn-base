package com.wildmind.fanwave.program;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wildmind.fanwave.media.ImageManager.SampleBase;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.media.UrlImageManager;
import com.wildmind.fanwave.activity.R;

public class ProgramMediaAdapter extends BaseAdapter {
	
	private Context context;
	private Gallery gallery;
	
	private ArrayList<String> image_urls = new ArrayList<String>();
	private ArrayList<TVProgramApp> program_apps = null;
	private ArrayList<TVProgramImage> program_images = null;
	private ArrayList<TVProgramVideo> program_videos = null;
	private int apps_position 	= -1;
	private int images_position = -1;
	private int videos_position = -1;
	
	private ImageLoader image_loader = new ImageLoader();
	
	/**
	 * Constructor
	 * @param gallery
	 * @param apps
	 * @param images
	 * @param videos
	 * @param context
	 */
	public ProgramMediaAdapter (Gallery gallery, ArrayList<TVProgramApp> apps, 
			ArrayList<TVProgramImage> images, ArrayList<TVProgramVideo> videos, Context context) {
		
		this.gallery = gallery;
		this.program_apps = apps;
		this.program_images = images;
		this.program_videos = videos;
		this.context = context;
		
		// add image urls into image_urls
		//
		if (program_apps != null) {
			for (TVProgramApp app:program_apps) 
				image_urls.add(app.getImageLink());
		}
		if (program_images != null) {
			for (TVProgramImage image:program_images) 
				image_urls.add(image.getThumbnailUrl());
		}
		if (program_videos != null) {
			for (TVProgramVideo video:program_videos) 
				image_urls.add(video.getThumbnailUrl());
		}
		
		// get section positions
		apps_position = getAppPosition();
		images_position = getImagePosition();
		videos_position = getVideoPosition();
		
		// start image loader thread
		if (image_urls.size() > 0)
			image_loader.start();
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		context = null;
		gallery = null;
		image_loader = null;
		
		image_urls = null;
		program_apps = null;
		program_images = null;
		program_videos = null;
	}
	
	@Override
	public int getCount() {
		return (int)(Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2)% image_urls.size();
	}

	@Override
	public Object getItem(int position) {
		return image_urls.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		position = decodeOriginPosition(position);
		ViewHolder viewHolder;
		if (view == null) {
			viewHolder = new ViewHolder();
			
			// View
			view = new RelativeLayout(context);
			
			// Image
			viewHolder.image = new ImageView(context);
			viewHolder.image.setBackgroundResource(R.drawable.media_mask_shadow);
			viewHolder.image.setScaleType(ImageView.ScaleType.FIT_XY);
			((RelativeLayout) view).addView(viewHolder.image);
			
			// Image Mask
			viewHolder.mask = new ImageView(context);
			viewHolder.mask.setBackgroundResource(R.drawable.media_mask_selector);
			viewHolder.mask.setDuplicateParentStateEnabled(true);
			((RelativeLayout) view).addView(viewHolder.mask);
			
			// Film Play
			viewHolder.play = new ImageView(context);
			viewHolder.play.setAdjustViewBounds(true);
			viewHolder.play.setMaxHeight(30);
			viewHolder.play.setMaxWidth(30);
			RelativeLayout.LayoutParams params 
								= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 
													 			  ViewGroup.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			viewHolder.play.setLayoutParams(params);
			viewHolder.play.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.media_filmplay));
			viewHolder.play.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			((RelativeLayout) view).addView(viewHolder.play);
			
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		// Image
		Bitmap bmp = UrlImageManager.getUrlImageFromStorage((String) getItem(position), SampleBase.HIGH_SAMPLED);
		adjustView(view, bmp);
		if (bmp != null)
			viewHolder.image.setImageBitmap(bmp);
		else {
			if (isAppItem(position))
				viewHolder.image.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_media_app_loading));
			else if (isImageItem(position))
				viewHolder.image.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_media_image_loading));
			else if (isVideoItem(position))
				viewHolder.image.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_media_video_loading));
			else
				viewHolder.image.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_loading));
		}
			
		// Play Icon
		if (bmp != null)
			viewHolder.play.setVisibility(isVideoItem(position) ? View.VISIBLE : View.INVISIBLE); 
		else
			viewHolder.play.setVisibility(View.INVISIBLE);
		
		return view;
	}
	
	private void adjustView (View view, Bitmap bmp) {
		int imageWidth = bmp != null ? bmp.getWidth() : 100;
		int imageHeight = bmp != null ? bmp.getHeight() : 100;
		
		float ratio = (float) 100 / imageHeight;
		imageHeight = 100;
		imageWidth = (int) (ratio > 0 ? imageWidth * ratio : imageWidth);
		
		view.setLayoutParams(new Gallery.LayoutParams(imageWidth + 40, imageHeight));
		ViewHolder viewHolder = (ViewHolder) view.getTag();

		// Image Mask
		RelativeLayout.LayoutParams aparams = new RelativeLayout.LayoutParams(imageWidth, imageHeight);
		aparams.addRule(RelativeLayout.CENTER_IN_PARENT);
		viewHolder.mask.setLayoutParams(aparams);
		
		// Image
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imageWidth, imageHeight);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		viewHolder.image.setLayoutParams(params);
	}
	
	/**
	 * Get first app item position.
	 * @return int	first position, -1 if no app item.
	 */
	public int getAppPosition () {
		return program_apps != null ? 0 : -1;
	}
	
	/**
	 * Get first image item position.
	 * @return int	first position, -1 if no image item.
	 */
	public int getImagePosition () {
		if (program_images == null)
			return -1;
		
		return program_apps != null ? program_apps.size() : 0;
	}
	
	/**
	 * Get first video item position.
	 * @return int 	first position, -1 if no video item.
	 */
	public int getVideoPosition () {
		if (program_videos == null)
			return -1;
		
		int position = 0;
		position += program_apps != null ? program_apps.size() : 0;
		position += program_images != null ?program_images.size() : 0;
		
		return position;
	}
	
	/**
	 * Get program app item for position.
	 * If no program app items or position is not in app section, return null
	 * @param position
	 * @return Object or null
	 */
	public TVProgramApp getAppItem (int position) {
		if (program_apps == null)
			return null;
		
		return position < program_apps.size() ? program_apps.get(position) : null;
	}
	
	/**
	 * Get program image item for position.
	 * If no program image items or position is not in image section, return null
	 * @param position
	 * @return Object or null
	 */
	public TVProgramImage getImageItem (int position) {
		if (program_images == null)
			return null;
		
		int itemPosition = position - (program_apps != null ? program_apps.size() : 0);
		if (itemPosition < 0)
			return null;
		
		return itemPosition < program_images.size() ?  program_images.get(itemPosition) : null;
	}
	
	/**
	 * Get program video item for position.
	 * If no program video items or position is not in video section, return null
	 * @param position
	 * @return Object or null
	 */
	public TVProgramVideo getVideoItem (int position) {
		if (program_videos == null)
			return null;
		
		int itemPosition = position - (program_apps != null ? program_apps.size() : 0);
		itemPosition -= program_images != null ? program_images.size() : 0;
		if (itemPosition < 0)
			return null;
		
		return itemPosition < program_videos.size() ? program_videos.get(itemPosition) : null;
	}
	
	/**
	 * Check if is program app item for position.
	 * @param position
	 * @return boolean
	 */
	public boolean isAppItem (int position) {
		if (apps_position == -1)
			return false;
		
		return apps_position <= position && position < program_apps.size();
	}
	
	/**
	 * Check if is program video item for position.
	 * @param position
	 * @return boolean
	 */
	public boolean isImageItem (int position) {
		if (images_position == -1)
			return false;
		
		if (apps_position == -1)
			return images_position <= position && position < program_images.size();
		else
			return images_position <= position && position < program_apps.size() + program_images.size();
	}
	
	/**
	 * Check if is program video item for position.
	 * @param position
	 * @return boolean
	 */
	public boolean isVideoItem (int position) {
		if (videos_position == -1)
			return false;
		
		return videos_position <= position;
	}
	
	public int decodeOriginPosition (int position) {
		return position % image_urls.size();
	}
	
	/**
	 * ViewHolder class
	 * @author Kencool
	 *
	 */
	private class ViewHolder {
		ImageView image;
		ImageView mask;
		ImageView play;
	}
	
	/**
	 * ImageLoader class
	 * @author Kencool
	 *
	 */
	private class ImageLoader extends Thread {
		public void run () {
			for (String url:image_urls)
				processImage(url);
		}
	}
	
	/**
	 * Process image url.
	 * @param imageUrl
	 */
	private void processImage (final String imageUrl) {
		
		// image already exists, ignore it
		//
		if (UrlImageManager.isUrlImageExistInStorage(imageUrl, SampleBase.HIGH_SAMPLED)) 
			return;
		
		// download image
		//
		final Bitmap bmp = UrlImageManager.downloadUrlImage(imageUrl, SampleBase.HIGH_SAMPLED);
		if (bmp == null)
			return;
		
		if (gallery != null) {
			gallery.post( new Runnable () {
				public void run () {
					if (gallery == null)
						return;
					
					int firstPosition = gallery.getFirstVisiblePosition();
					int lastPosition = gallery.getLastVisiblePosition();
					
					for (int i = firstPosition; i <= lastPosition; i++) {
						if (i == image_urls.size())
							break;
					
						if (imageUrl.equals(image_urls.get(decodeOriginPosition(i)))) {
							int childPosition = i - gallery.getFirstVisiblePosition();
							View view = gallery.getChildAt(childPosition);
							adjustView(view, bmp);
							ViewHolder viewHolder = (ViewHolder) view.getTag();
							viewHolder.image.setImageBitmap(bmp);
						}
					}
				}
			});
		}
	}
}
