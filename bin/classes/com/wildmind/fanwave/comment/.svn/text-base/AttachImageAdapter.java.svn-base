package com.wildmind.fanwave.comment;

import java.util.ArrayList;

import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.media.AttachImageManager;
import com.wildmind.fanwave.media.ImageManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AttachImageAdapter extends BaseAdapter {

	private Context context;
	private Gallery gallery;
	
	private ArrayList<String> image_tokens = new ArrayList<String>();
	private ArrayList<Attachment> images = null;
	
	private ImageLoader image_loader = new ImageLoader();
	
	/**
	 * Constructor
	 * @param galler
	 * @param images
	 * @param context
	 */
	public AttachImageAdapter (Gallery gallery, ArrayList<Attachment> images, Context context) {
		this.gallery = gallery;
		this.images = images;
		this.context = context;
		
		if (images != null) {
			for (Attachment image:images)
				image_tokens.add(image.getToken());
		}
		
		if (image_tokens.size() > 0)
			image_loader.start();
	}
	
	/**
	 * Clear all resoureces.
	 */
	public void clear () {
		context = null;
		gallery = null;
		image_tokens = null;
		images = null;
		image_loader = null;
	}

	@Override
	public int getCount() {
		return images != null ? images.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return images.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Attachment image = (Attachment) getItem(position);
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
			
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		// Image
		Bitmap bmp = AttachImageManager.getAttachImageFromStorage(image.getToken(), true);
		adjustView(view, bmp);
		if (bmp != null)
			viewHolder.image.setImageBitmap(bmp);
		else
			viewHolder.image.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.program_media_image_loading));
		
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
	 * ViewHolder class
	 * @author Kencool
	 *
	 */
	private class ViewHolder {
		ImageView image;
		ImageView mask;
	}
	
	/**
	 * ImageLoader class
	 * @author Kencool
	 *
	 */
	private class ImageLoader extends Thread {
		public void run () {
			for (String token:image_tokens)
				processImage(token);
		}
	}
	
	/**
	 * Process image.
	 * @param token
	 */
	private void processImage (final String token) {
		
		// image already exists, ignore it
		//
		if (AttachImageManager.isAttachImageExistInStorage(token, true))
			return;
		
		// download image
		//
		final Bitmap bmp = AttachImageManager.downloadAttachImage(token, true);
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
						if (i == image_tokens.size())
							break;
						
						if (token.equals(image_tokens.get(i))) {
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
