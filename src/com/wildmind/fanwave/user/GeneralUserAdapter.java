package com.wildmind.fanwave.user;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.activity.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GeneralUserAdapter extends BaseAdapter {

	private ListView user_listview;
	private LayoutInflater inflater;
	
	private ArrayList<TVUser> users = null;
	private UserImageListener image_listener = new UserImageListener();
	private ConcurrentHashMap<String, String> avatar_requests = new ConcurrentHashMap<String, String>();
	
	/**
	 * Constructor
	 * @param listview
	 * @param users
	 * @param context
	 */
	public GeneralUserAdapter (ListView listview, ArrayList<TVUser> users, Context context) {
		this.user_listview = listview;
		this.users = users;
		this.inflater = LayoutInflater.from(context);
		
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		user_listview = null;
		inflater = null;
		users = null;
		
		avatar_requests.clear();
		avatar_requests = null;
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
			view = inflater.inflate(R.layout.general_user_row, null);
			viewHolder = new ViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();
		
		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		loadAvatar(viewHolder.avatar, user.getUsername());
		
		// Nickname
		viewHolder.nickname.setText(user.getNickname());
		
		return view;
	}
	
	private class ViewHolder {
		ImageView 	avatar;
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
	private class UserImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(final String username, final Bitmap bmp) {
			if (avatar_requests == null || !avatar_requests.containsKey(username) || bmp == null)
				return;
			
			if (user_listview != null) {
				user_listview.post( new Runnable () {
					public void run () {
						if (user_listview == null)
							return;
						
						// go through current visible rows and check if refresh is need
						//
						for (int i = user_listview.getFirstVisiblePosition(); 
							i <= user_listview.getLastVisiblePosition();
							i++) {
							// over user list position, may be other purpose row
							if (i == users.size())
								break;
									
							if (username.equals(users.get(i).getUsername())) {
								int childPosition = i - user_listview.getFirstVisiblePosition();
								ViewHolder viewHolder = 
										(ViewHolder) user_listview.getChildAt(childPosition).getTag();
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
