package com.wildmind.fanwave.splash;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.util.StringGenerator;

public class SplashListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ListView splash_listview;
	
	private ArrayList<Splash> splashes = null;
	private SplashImageListener image_listener = new SplashImageListener();
	private ConcurrentHashMap<String, String> avatar_requests = new ConcurrentHashMap<String, String>();
	
	/**
	 * Constructor 
	 * @param listview
	 * @param context
	 */
	public SplashListAdapter(ListView listview, ArrayList<Splash> splashes, Context context) {
		this.splash_listview = listview;
		this.splashes = splashes;
		this.inflater = LayoutInflater.from(context);
		
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		inflater = null;
		splash_listview = null;
		splashes = null;
	}
	
	@Override
	public int getCount() {
		return splashes != null ? splashes.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return splashes != null ? splashes.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Splash splash = splashes.get(position);
		ViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.splash_list_row, null);
			viewHolder = new ViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			viewHolder.content = (TextView) view.findViewById(R.id.content_textview);
			viewHolder.created_time = (TextView) view.findViewById(R.id.created_time_textview);
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		loadAvatar(viewHolder.avatar, splash.getSender());
		
		// Nickname
		viewHolder.nickname.setText(splash.getNickname());
		
		// Content
		viewHolder.content.setText(splash.getMessage());
		
		// Created Time
		viewHolder.created_time.setText(StringGenerator.distinctTimeStringFromTimeString(splash.getCreatedTime()));
		
		return view;
	}
	
	/**
	 * Refresh data.
	 * @param splashes
	 */
	public void refreshData (ArrayList<Splash> splashes) {
		this.splashes = splashes;
		notifyDataSetChanged();
	}
	
	/**
	 * ViewHolder class
	 * @author Kencool
	 *
	 */
	private class ViewHolder {
		ImageView	avatar;
		TextView	nickname;
		TextView	content;
		TextView	created_time;
	}
	
	/**
	 * Image Process Methods
	 */
	
	/**
	 * Load avatar.
	 * @param iv
	 * @param username
	 */
	private void loadAvatar (ImageView iv, String username) {
		if (username == null || username.length() == 0)
			return;
		
		if (!ImageManager.drawAvatarImage(iv, username))
			avatar_requests.put(username, "");
	}

	/**
	 * Listener for listening image requests.
	 * @author Kencool
	 *
	 */
	private class SplashImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(final String username, final Bitmap bmp) {
			if (avatar_requests == null || !avatar_requests.containsKey(username) || bmp == null)
				return;
			
			if (splash_listview != null) {
				splash_listview.post( new Runnable () {
					public void run () {
						if (splash_listview == null)
							return;
						
						// go through current visible rows and check if refresh is need
						//
						for (int i = splash_listview.getFirstVisiblePosition(); 
							 i <= splash_listview.getLastVisiblePosition(); 
							 i++) {
							// over feed list position, may be other purpose row
							if (i == splashes.size())
								break;
							
							if (splashes.get(i).getSender().equals(username)) {
								int childPosition = i - splash_listview.getFirstVisiblePosition();
								ViewHolder viewHolder = 
										(ViewHolder) splash_listview.getChildAt(childPosition).getTag();
								viewHolder.avatar.setImageBitmap(bmp);
							}
						}
						
						avatar_requests.remove(username);
					}
				});
			}
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
		public void retrieveProgramIcon(String title, int sampleBase, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void retrieveChannelIcon(String chcode, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
