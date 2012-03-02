package com.wildmind.fanwave.hot;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager.SampleBase;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.media.ProgramIconManager;
import com.wildmind.fanwave.activity.R;

public class RecommendGalleryAdapter extends BaseAdapter {
	
	private Context context;
	private Gallery gallery;
	
	private ArrayList<TVHot> recommendation = null;
	private RecommendImageListener image_listener = new RecommendImageListener();
	private ConcurrentHashMap<String, String> pgicon_requests = new ConcurrentHashMap<String, String>();
	
	/**
	 * Constructor
	 * @param gallery
	 * @param recommendation
	 * @param context
	 */
	public RecommendGalleryAdapter (Gallery gallery, ArrayList<TVHot> recommendation, Context context) {
		this.gallery = gallery;
		this.recommendation = recommendation;
		this.context = context;
		
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		context = null;
		gallery = null;
		recommendation = null;

	}
	
	@Override
	public int getCount() {		
		return recommendation != null ? recommendation.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return recommendation != null ? recommendation.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		TVHot hot = recommendation.get(position);
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
			
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		// Image
		Bitmap bmp = getProgramIcon(hot.getTitle(), hot.getIconUrl());
		if (bmp != null) {
			adjustView(view, bmp);
			viewHolder.image.setImageBitmap(bmp);
		}
		
		return view;
	}
	
	private void adjustView (View view, Bitmap bmp) {
		int length = 150;
		int imageWidth = bmp.getWidth();
		int imageHeight = bmp.getHeight();
		
		if (imageWidth >= imageHeight) {
			// scale image width to length, scale height without changing aspect ratio
			float ratio = (float) length / imageWidth;
			imageWidth = length;
			imageHeight = (int) (ratio > 0 ? imageHeight * ratio : imageHeight);
		} else {
			// scale image height to length, scale width without changing aspect ratio
			float ratio = (float) length / imageHeight;
			imageHeight = length;
			imageWidth = (int) (ratio > 0 ? imageWidth * ratio : imageWidth);
		}
		
		view.setLayoutParams(new Gallery.LayoutParams(imageWidth + 40, imageHeight));
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		
		// Image
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(imageWidth, imageHeight);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		viewHolder.image.setLayoutParams(params);
	}
	
	/**
	 * Refres data.
	 * @param recommendation
	 */
	public void refreshData (ArrayList<TVHot> recommendation) {
		this.recommendation = recommendation;
		notifyDataSetChanged();
	}
	
	/**
	 * ViewHolder class
	 * @author Kencool
	 *
	 */
	private class ViewHolder {
		ImageView image;
	}
	
	/**
	 * Image Process Methods
	 */
	
	/**
	 * Get program icon bitmap. If icon not exist, return loading icon and queue icon request.
	 * @param title
	 * @param icon_url
	 * @return bitmap
	 */
	private Bitmap getProgramIcon (String title, String icon_url) {
		if (icon_url == null || icon_url.length() == 0)
			return null;
		
		if (!ProgramIconManager.isProgramIconExistInStorage(title, SampleBase.RIGOROUS_SAMPLED)) {
			ImageManager.drawProgramIcon(null, title, icon_url, SampleBase.RIGOROUS_SAMPLED);
			pgicon_requests.put(ProgramIconManager.buildProgramIconKey(title, SampleBase.RIGOROUS_SAMPLED), "");
			return BitmapFactory.decodeResource(context.getResources(), R.drawable.program_media_image_loading);
			
		} else 
			return ProgramIconManager.getProgramIconFromStorage(title, SampleBase.RIGOROUS_SAMPLED);
	}
	
	/**
	 * Listener for listening image requests.
	 * @author Kencool
	 *
	 */
	private class RecommendImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(String username, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void retrieveBadge(String badge_id, boolean scaled, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void retrieveAttach(String token, boolean is_thumb, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void retrieveProgramIcon(final String title, final int sampleBase, final Bitmap bmp) {
			final String key = ProgramIconManager.buildProgramIconKey(title, sampleBase);
			if (pgicon_requests == null || !pgicon_requests.containsKey(key) || bmp == null)
				return;
			
			if (gallery != null) {
				gallery.post (new Runnable () {
					public void run () {
						if (gallery == null)
							return;
						
						// go through current visible rows and check if refresh is need
						//
						for (int i = gallery.getFirstVisiblePosition(); 
							 i <= gallery.getLastVisiblePosition(); 
							 i++) {
							if (i == recommendation.size())
								break;
						
							if (title.equals(recommendation.get(i).getTitle())) {
								int childPosition = i - gallery.getFirstVisiblePosition();
								View view = gallery.getChildAt(childPosition);
								adjustView(view, bmp);
								ViewHolder viewHolder = (ViewHolder) view.getTag();
								viewHolder.image.setImageBitmap(bmp);
							}
								notifyDataSetChanged();
						}
						
						pgicon_requests.remove(key);
					}
				});
			}
		}

		@Override
		public void retrieveChannelIcon(String chcode, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
