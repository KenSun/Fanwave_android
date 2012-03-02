package com.wildmind.fanwave.splash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.user.TVUser;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashGroupAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private GridView users_gridview;
	
	private ArrayList<TVUser> users = null;
	private HashMap<String, TVUser> selected_users = new HashMap<String, TVUser>();
	private SplashGroupListener listener = null;
	private SplashImageListener image_listener = new SplashImageListener();
	private ConcurrentHashMap<String, String> avatar_requests = new ConcurrentHashMap<String, String>();
	
	/**
	 * Constructor
	 * @param gridview
	 * @param users
	 * @param context
	 */
	public SplashGroupAdapter (GridView gridview, ArrayList<TVUser> users, Context context) {
		this.users_gridview = gridview;
		this.users = users;
		this.inflater = LayoutInflater.from(context);
		
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Set splash group listener.
	 * @param listener
	 */
	public void setSplashGroupListener (SplashGroupListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		listener = null;
		inflater = null;
		users_gridview = null;
		users = null;
		selected_users = null;
	}
	
	public HashMap<String, TVUser> getSelectedUsers () {
		return selected_users;
	}
	
	@Override
	public int getCount() {
		return users != null ? users.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return users != null ? users.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		TVUser user = users.get(position);
		ViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.splash_group_item, null);
			viewHolder = new ViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.select = (ImageView) view.findViewById(R.id.selected_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		viewHolder.avatar.setSelected(selected_users.containsKey(user.getUsername()) ? true : false);
		loadAvatar(viewHolder.avatar, user.getUsername());
		
		// Select
		viewHolder.select.setSelected(selected_users.containsKey(user.getUsername()) ? true : false);
		
		// Nickname
		viewHolder.nickname.setText(user.getNickname());
		
		return view;
	}
	
	/**
	 * Refresh data.
	 * @param users
	 */
	public void refreshData (ArrayList<TVUser> users) {
		this.users = users;
		notifyDataSetChanged();
	}
	
	/**
	 * Click item at position.
	 * @param position
	 * @param view
	 */
	public void clickItem (int position, View view) {
		TVUser user = users.get(position);
		if (selected_users.containsKey(user.getUsername()))
			unselectUser(user, view);
		else
			selectUser(user, view);
	}
	
	/**
	 * Select all.
	 */
	public void selectAll () {
		for (TVUser user:users)
			selected_users.put(user.getUsername(), user);	
		notifyDataSetChanged();
				
		if (listener != null) {
			listener.onSelectedCountChanged(selected_users.size());
			listener.onAllSelected();
		}
	}
	
	/**
	 * Unselect all.
	 */
	public void unselectAll () {
		selected_users.clear();
		notifyDataSetChanged();
		
		if (listener != null) {
			listener.onSelectedCountChanged(selected_users.size());
			listener.onAllSelectedCancel();
		}
		
	}
	
	/**
	 * Select user.
	 * @param position
	 * @param view
	 */
	public void selectUser (TVUser user, View view) {
		selected_users.put(user.getUsername(), user);
		
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.avatar.setSelected(true);
		viewHolder.select.setSelected(true);
		
		if (listener != null) {
			listener.onSelectedCountChanged(selected_users.size());
			
			if (selected_users.size() == users.size())
				listener.onAllSelected();
		}
	}
	
	/**
	 * Unselect user.
	 * @param position
	 * @param view
	 */
	public void unselectUser (TVUser user, View view) {
		selected_users.remove(user.getUsername());
		
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		viewHolder.avatar.setSelected(false);
		viewHolder.select.setSelected(false);
		
		if (listener != null) {
			listener.onSelectedCountChanged(selected_users.size());
			
			if (selected_users.size() < users.size())
				listener.onAllSelectedCancel();
		}
	}
	
	private class ViewHolder {
		ImageView	avatar;
		ImageView	select;
		TextView	nickname;
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
			
			if (users_gridview != null) {
				users_gridview.post( new Runnable () {
					public void run () {
						if (users_gridview == null)
							return;
						
						// go through current visible rows and check if refresh is need
						//
						for (int i = users_gridview.getFirstVisiblePosition(); 
							 i <= users_gridview.getLastVisiblePosition(); 
							 i++) {
							if (users.get(i).getUsername().equals(username)) {
								int childPosition = i - users_gridview.getFirstVisiblePosition();
								ViewHolder viewHolder = 
										(ViewHolder) users_gridview.getChildAt(childPosition).getTag();
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
	
	/**
	 * Splash Group Listener interface
	 * @author Kencool
	 *
	 */
	public interface SplashGroupListener {
		
		public void onSelectedCountChanged(int selectedCount);
		
		public void onAllSelected();
		
		public void onAllSelectedCancel();
	}
}
