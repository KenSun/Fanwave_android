package com.wildmind.fanwave.facebook;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.animation.AlphaSetter;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.friend.FriendManager;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.media.UrlImageManager;
import com.wildmind.fanwave.media.ImageManager.SampleBase;
import com.wildmind.fanwave.profile.FacebookProfile;
import com.wildmind.fanwave.user.TVUser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FBFriendsAdapter extends BaseAdapter {

	/**
	 * Row view type count.
	 * 
	 * Bar row		: 0
	 * Button row	: 1
	 * Complex row	: 2	row with action layout on the right side
	 * Simple row	: 3 row with simple button on the right side
	 */
	public static final int ROW_VIEW_TYPE_COUNT = 4;
	public static final int ROW_TYPE_BAR		= 0;
	public static final int ROW_TYPE_BUTTON		= 1;
	public static final int ROW_TYPE_COMPLEX 	= 2;
	public static final int ROW_TYPE_SIMPLE 	= 3;
	
	private ListView friend_listview;
	
	private Context context;
	private LayoutInflater inflater;
	
	private ArrayList<TVUser> friends = new ArrayList<TVUser>();
	private ArrayList<TVUser> requests = new ArrayList<TVUser>();
	private ArrayList<TVUser> invitings = new ArrayList<TVUser>();
	private ArrayList<TVUser> strangers = new ArrayList<TVUser>();
	private ArrayList<FBUser> non_apps = new ArrayList<FBUser>();
	private FriendImageListener image_listener = new FriendImageListener();
	private ConcurrentHashMap<String, String> avatar_requests = new ConcurrentHashMap<String, String>();
	
	private ArrayList<String> image_urls = new ArrayList<String>();
	private ImageLoader image_loader = new ImageLoader();
	
	private boolean all_inviting = false;
	
	/**
	 * Constructor
	 * @param listview
	 * @param appUsers
	 * @param nonAppUsers
	 * @param context
	 */
	public FBFriendsAdapter (ListView listview, ArrayList<TVUser> appUsers, ArrayList<FBUser> nonAppUsers,
			Context context) {
		this.friend_listview = listview;
		this.non_apps = nonAppUsers;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		
		filterUsers(appUsers);
		ImageManager.addImageListener(image_listener);
		
		// add image urls into image_urls
		//
		if (non_apps != null) {
			for (FBUser user:non_apps)
				image_urls.add(user.getPicSquare());
		}
		
		// start image loader thread
		if (image_urls.size() > 0)
			image_loader.start();
	}
	
	/**
	 * Filter out friends, request users, inviting users and strangers.
	 * @param users
	 */
	public void filterUsers (ArrayList<TVUser> users) {
		friends.clear();
		requests.clear();
		invitings.clear();
		strangers.clear();
		
		if (users == null)
			return;
		
		for (TVUser user:users) {
			if (FriendManager.isFriend(user.getUsername()))
				friends.add(user);
			else if (FriendManager.isRequest(user.getUsername()))
				requests.add(user);
			else if (FriendManager.isInviting(user.getUsername()))
				invitings.add(user);
			else 
				strangers.add(user);
		}
	} 
	
	/**
	 * Clear all resources.
	 */
	public void clear () {
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		friend_listview = null;
		context = null;
		inflater = null;
		
		friends = null;
		requests = null;
		invitings = null;
		strangers = null;
		
		avatar_requests.clear();
		avatar_requests = null;
		
		image_urls = null;
		image_loader = null;
	}

	@Override
	public int getCount() {
		int count = 0;
		int appCount = friends.size() + requests.size() + invitings.size() + strangers.size();
		count += appCount > 0 ? appCount + 1 : 0;
		count += non_apps != null && non_apps.size() > 0 ? non_apps.size() + 2 : 0;
		
		return count;
	}

	@Override
	public Object getItem(int position) {
		Object obj = null;
		if (getItemViewType(position) != ROW_TYPE_BAR &&
			getItemViewType(position) != ROW_TYPE_BUTTON) {
			obj = getFriendItem(position);
			if (obj == null)
				obj = getRequestItem(position);
			if (obj == null)
				obj = getInvitingItem(position);
			if (obj == null)
				obj = getStrangerItem(position);
			if (obj == null)
				obj = getNonAppItem(position);
		}
		
		return obj;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount () {
		return ROW_VIEW_TYPE_COUNT;
	}
	
	@Override
	public int getItemViewType (int position) {
		if (position == getInAppBarPosition() || position == getNonAppBarPosition())
			return ROW_TYPE_BAR;
		else if (position == getNonAppInvitePosition())
			return ROW_TYPE_BUTTON;
		else if (getStrangerItem(position) != null)
			return ROW_TYPE_COMPLEX;
		else
			return ROW_TYPE_SIMPLE;
	}
	
	@Override
	public boolean isEnabled (int position) {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Object obj = getItem(position);
		
		switch (getItemViewType(position)) {
		case ROW_TYPE_BAR:
			return getBarView(position, convertView);
		case ROW_TYPE_BUTTON:
			return getInviteButtonView(convertView);
		case ROW_TYPE_COMPLEX:
			return getComplexView((TVUser) obj, convertView, position);
		case ROW_TYPE_SIMPLE:
			if (TVUser.class.isInstance(obj))
				return getSimpleView((TVUser) obj, convertView, position);
			else
				return getSimpleView((FBUser) obj, convertView, position);
		default:
			return null;
		}
	}
	
	/**
	 * Get in app friends bar position.
	 * @return
	 */
	public int getInAppBarPosition () {
		return  friends.size() + requests.size() + invitings.size() + strangers.size() > 0 ? 0 : -1;
	}
	
	/**
	 * Get non app friends bar position.
	 * @return
	 */
	public int getNonAppBarPosition () {
		if (non_apps == null || non_apps.size() == 0)
			return -1;
		
		int appCount = friends.size() + requests.size() + invitings.size() + strangers.size();
		return appCount > 0 ? appCount + 1 : 0;
	}
	
	/**
	 * Get non app invite all button position.
	 * @return
	 */
	public int getNonAppInvitePosition () {
		if (non_apps == null || non_apps.size() == 0)
			return -1;
		
		return getNonAppBarPosition() + 1;
	}
	
	/**
	 * Get in app friend position.
	 * @return
	 */
	public int getFriendPosition () {
		return friends.size() > 0 ? 1 : -1;
	}
	
	/**
	 * Get in app request position.
	 * @return
	 */
	public int getRequestPosition () {
		if (requests.size() == 0)
			return -1;
		
		return friends.size() + 1;
	}
	
	/**
	 * Get in app inviting position.
	 * @return
	 */
	public int getInvitingPosition () {
		if (invitings.size() == 0)
			return -1;
		
		return friends.size() + requests.size() + 1;
	}
	
	/**
	 * Get in app stranger position.
	 * @return
	 */
	public int getStrangerPosition () {
		if (strangers.size() == 0)
			return -1;
		
		return friends.size() + requests.size() + invitings.size() + 1;
	}
	
	/**
	 * Get non app friend position.
	 * @return
	 */
	public int getNonAppUserPosition () {
		if (non_apps == null || non_apps.size() == 0)
			return -1;
		
		return getNonAppBarPosition() + 2;
	}
	
	/**
	 * Get friend item for position.
	 * If no friend items or position is not in friend section, return null
	 * @param position
	 * @return TVUser
	 */
	public TVUser getFriendItem (int position) {
		if (friends.size() == 0)
			return null;
		
		return position <= friends.size() ? friends.get(position - 1) : null;
	}
	
	/**
	 * Get request item for position.
	 * If no request items or position is not in request section, return null
	 * @param position
	 * @return Object or null
	 */
	public TVUser getRequestItem (int position) {
		if (requests.size() == 0)
			return null;
		
		int itemPosition = position - getRequestPosition();
		return 0 <= itemPosition && itemPosition < requests.size() ?requests.get(itemPosition) : null;
	}
	
	/**
	 * Get inviting item for position.
	 * If no inviting items or position is not in inviting section, return null
	 * @param position
	 * @return Object or null
	 */
	public TVUser getInvitingItem (int position) {
		if (invitings.size() == 0)
			return null;
		
		int itemPosition = position - getInvitingPosition();
		return 0 <= itemPosition && itemPosition < invitings.size() ? invitings.get(itemPosition) : null;
	}
	
	/**
	 * Get stranger item for position.
	 * If no stranger items or position is not in stranger section, return null
	 * @param position
	 * @return Object or null
	 */
	public TVUser getStrangerItem (int position) {
		if (strangers.size() == 0)
			return null;
		
		int itemPosition = position - getStrangerPosition();
		return 0 <= itemPosition && itemPosition < strangers.size() ? strangers.get(itemPosition) : null;
	}
	/**
	 * Get non app item for position.
	 * If no non app items or position is not in non app section, return null
	 * @param position
	 * @return Object or null
	 */
	public FBUser getNonAppItem (int position) {
		if (non_apps == null || non_apps.size() == 0)
			return null;
		
		int itemPosition = position - getNonAppUserPosition();
		return 0 <= itemPosition && itemPosition < non_apps.size() ? non_apps.get(itemPosition) : null;
	}
	
	/**
	 * Get bar row view.
	 * @param position
	 * @param view
	 * @param parent
	 * @return
	 */
	private View getBarView (int position, View view) {
		if (view == null) {
			view = new TextView(context);
			((TextView) view).setTextColor(Color.WHITE);
			((TextView) view).setTextSize(14);
			((TextView) view).setTypeface(null,Typeface.BOLD);
			((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
			((TextView) view).setPadding(16, 0, 0, 0);
			view.setBackgroundResource(R.drawable.bar_gray);
		}
		
		if (position == getInAppBarPosition()) 
			((TextView) view).setText(R.string.fb_friend_in_app_title);
		else if (position == getNonAppBarPosition())
			((TextView) view).setText(R.string.fb_friend_non_app_title);
		
		return view;
	}
	
	/**
	 * Get invite all row view.
	 * @param view
	 * @return
	 */
	private View getInviteButtonView (View view) {
		if (view == null) {
			view = inflater.inflate(R.layout.inviteall_facebook_friend_button, null);
			view.setOnClickListener(getInviteAllClickedListener());
		}
		
		view.setEnabled(!all_inviting);
		view.clearAnimation();
		if (all_inviting)
			view.startAnimation(new AlphaSetter(0.5f));
		
		return view;
	}
	
	/**
	 * Get complex view.
	 * @param user
	 * @param view
	 * @param position
	 * @return
	 */
	private View getComplexView (TVUser user, View view, int position) {
		ComplexViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.personal_friend_row_complex, null);
			viewHolder = new ComplexViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.divider = (ImageView) view.findViewById(R.id.divider_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			viewHolder.mood = (TextView) view.findViewById(R.id.mood_textview);
			viewHolder.action_layout = (LinearLayout) view.findViewById(R.id.action_layout);
			
			// no mood for current version
			viewHolder.mood.setVisibility(View.GONE);
						
			view.setTag(viewHolder);
 		} else
			viewHolder = (ComplexViewHolder) view.getTag();
		
		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		loadAvatar(viewHolder.avatar, user.getUsername());
		
		// Nickname
		viewHolder.nickname.setText(user.getNickname());
		
		// Mood
		
		// Action
		viewHolder.action_layout.setOnClickListener(getActionLayoutClickedListener(user));
		
		// Divider
		boolean invisible = position == getNonAppBarPosition() - 1;
		viewHolder.divider.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
				
		return view;
	}
	
	/**
	 * Get simple view for Fanwave user.
	 * @param user
	 * @param view
	 * @param position
	 * @return
	 */
	private View getSimpleView (TVUser user, View view, int position) {
		SimpleViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.personal_friend_row_simple, null);
			viewHolder = new SimpleViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.divider = (ImageView) view.findViewById(R.id.divider_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			viewHolder.mood = (TextView) view.findViewById(R.id.mood_textview);
			viewHolder.action_button = (Button) view.findViewById(R.id.action_button);
			
			// no mood for current version
			viewHolder.mood.setVisibility(View.GONE);
						
			view.setTag(viewHolder);
 		} else
			viewHolder = (SimpleViewHolder) view.getTag();
		
		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		loadAvatar(viewHolder.avatar, user.getUsername());
		
		// Nickname
		viewHolder.nickname.setText(user.getNickname());
		
		// Mood
		
		// Action
		viewHolder.action_button.clearAnimation();
		
		if (FriendManager.isFriend(user.getUsername())) {
			viewHolder.action_button.setVisibility(View.INVISIBLE);
			viewHolder.action_button.setEnabled(false);
			
		} else if (FriendManager.isRequest(user.getUsername())) {
			viewHolder.action_button.setVisibility(View.VISIBLE);
			viewHolder.action_button.setEnabled(true);
			viewHolder.action_button.setText(R.string.personal_action_accept);
			viewHolder.action_button.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			viewHolder.action_button.setTextColor(context.getResources().getColor(R.drawable.color_orange_white_selector));
		
		} else if (FriendManager.isInviting(user.getUsername())) {
			viewHolder.action_button.setVisibility(View.VISIBLE);
			viewHolder.action_button.startAnimation(new AlphaSetter(0.5f));
			viewHolder.action_button.setEnabled(false);
			viewHolder.action_button.setText(R.string.personal_action_inviting);
			viewHolder.action_button.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
			viewHolder.action_button.setTextColor(context.getResources().getColor(R.drawable.color_light_brown_white_selector));
		}
		if (viewHolder.action_button.isEnabled())
			viewHolder.action_button.setOnClickListener(getActionButtonClickedListener(user));
		
		// Divider
		boolean invisible = position == getNonAppBarPosition() - 1;
		viewHolder.divider.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
				
		return view;
	}
	
	/**
	 * Get simple view for Facebook
	 * @param user
	 * @param view
	 * @param position
	 * @return
	 */
	private View getSimpleView (FBUser user, View view, int position) {
		SimpleViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.personal_friend_row_simple, null);
			viewHolder = new SimpleViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.divider = (ImageView) view.findViewById(R.id.divider_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			viewHolder.mood = (TextView) view.findViewById(R.id.mood_textview);
			viewHolder.action_button = (Button) view.findViewById(R.id.action_button);
			
			// no mood for current version
			viewHolder.mood.setVisibility(View.GONE);
						
			view.setTag(viewHolder);
 		} else
			viewHolder = (SimpleViewHolder) view.getTag();
		
		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		loadFBAvatar(viewHolder.avatar, user.getPicSquare());
		
		// Nickname
		viewHolder.nickname.setText(user.getName());
		
		// Mood
		
		// Action
		viewHolder.action_button.setVisibility(View.VISIBLE);
		viewHolder.action_button.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		viewHolder.action_button.setTextColor(context.getResources().getColor(R.drawable.color_light_brown_white_selector));
		viewHolder.action_button.clearAnimation();
		
		FacebookProfile fp = new FacebookProfile(ApplicationManager.getAppContext());
		if (fp.isFriendInvited(user.getUid())) {
			viewHolder.action_button.setText(R.string.personal_action_inviting);
			viewHolder.action_button.startAnimation(new AlphaSetter(0.5f));
			viewHolder.action_button.setEnabled(false);
		} else {
			viewHolder.action_button.setText(R.string.fb_friend_invite);
			viewHolder.action_button.setEnabled(true);
			viewHolder.action_button.setOnClickListener(getInviteNonAppClickedListener(user));
		}
		
		// Divider
		viewHolder.divider.setVisibility(View.VISIBLE);
				
		return view;
	}
	
	/**
	 * Callback for action layout clicked.
	 * @param user
	 * @return
	 */
	private View.OnClickListener getActionLayoutClickedListener (final TVUser user) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (FriendManager.inviteFriend(user)) {
					invitings.add(user);
					strangers.remove(user);
					notifyDataSetChanged();
				}
			}
		};
	}
	
	/**
	 * Callback for action button clicked.
	 * @param user
	 * @return
	 */
	private View.OnClickListener getActionButtonClickedListener (final TVUser user) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (FriendManager.isRequest(user.getUsername())) {
					if (FriendManager.acceptFriend(user)) {
						friends.add(user);
						requests.remove(user);
						notifyDataSetChanged();
					}
				}
			}
		};
	}
	
	/**
	 * Callback for invite all clicked.
	 * @return
	 */
	private View.OnClickListener getInviteAllClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				// button
				DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						all_inviting = true;
						v.setEnabled(false);
						v.clearAnimation();
						v.startAnimation(new AlphaSetter(0.5f));
						
						new Thread (new Runnable () {
							public void run () {
								ArrayList<FBUser> users = non_apps;
								for (FBUser user:users)
									FacebookManager.inviteFriend(user);
								all_inviting = false;
										
								if (friend_listview != null) {
									friend_listview.post( new Runnable () {
										public void run () {
											notifyDataSetChanged();
										}
									});
								}
							}
						}).start();
					}
				};
				
				// alert dialog
				new AlertDialog.Builder(context)
							   .setTitle(R.string.app_message)
							   .setMessage(context.getResources().getString(R.string.fb_friend_invite_all))
							   .setPositiveButton(R.string.action_confirm, positive)
							   .setNegativeButton(R.string.action_cancel, null)
							   .show();
			}
		};
	}
	
	/**
	 * Callback for invite non app friend clicked.
	 * @param user
	 * @return
	 */
	private View.OnClickListener getInviteNonAppClickedListener (final FBUser user) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread (new Runnable () {
					public void run () {
						FacebookManager.inviteFriend(user);
								
						if (friend_listview != null) {
							friend_listview.post( new Runnable () {
								public void run () {
									notifyDataSetChanged();
								}
							});
						}
					}
				}).start();
			}
		};
	}
	
	/**
	 * Refresh data.
	 * @param users
	 */
	public void refreshData (ArrayList<TVUser> appUsers, ArrayList<FBUser> nonAppUsers) {
		this.non_apps = nonAppUsers;
		filterUsers(appUsers);
		notifyDataSetChanged();
		
		if (!image_loader.isAlive()) {
			if (non_apps != null) {
				for (FBUser user:non_apps)
					image_urls.add(user.getPicSquare());
			}
			
			// start image loader thread
			if (image_urls.size() > 0)
				image_loader.start();
		}
	}
	
	/**
	 * Common view holder.
	 * @author Kencool
	 *
	 */
	private class ViewHolder {
		ImageView 	avatar;
		ImageView	divider;
		TextView 	nickname;
		TextView 	mood;
	}
	
	/**
	 * Complex type row view holder.
	 * @author Kencool
	 *
	 */
	private class ComplexViewHolder extends ViewHolder {
		LinearLayout action_layout;
	}
	
	/**
	 * Simple type row view holder.
	 * @author Kencool
	 *
	 */
	private class SimpleViewHolder extends ViewHolder {
		Button action_button; 
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
	 * Load Facebook use avatar.
	 * @param iv
	 * @param url
	 */
	private void loadFBAvatar (ImageView iv, String url) {
		if (url == null || url.length() == 0)
			return;
		
		if (UrlImageManager.isUrlImageExistInStorage(url, SampleBase.HIGH_SAMPLED)) 
			UrlImageManager.drawUrlImage(iv, url, SampleBase.RIGOROUS_SAMPLED);
	}
	
	/**
	 * Listener for listening image loading requests.
	 * @author Kencool
	 *
	 */
	private class FriendImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(final String username, final Bitmap bmp) {
			if (avatar_requests == null || !avatar_requests.containsKey(username) || bmp == null)
				return;
			
			if (friend_listview != null) {
				friend_listview.post( new Runnable () {
					public void run () {
						if (friend_listview == null)
							return;
						
						// go through current visible rows and check if refresh is need
						//
						for (int i = friend_listview.getFirstVisiblePosition(); 
								 i <= friend_listview.getLastVisiblePosition();
								 i++) {
							Object obj = getItem(i);
							if (obj == null || !TVUser.class.isInstance(obj))
								continue;
							
							TVUser user = (TVUser) obj;
							if (username.equals(user.getUsername())) {
								int childPosition = i - friend_listview.getFirstVisiblePosition();
								ViewHolder viewHolder = 
										(ViewHolder) friend_listview.getChildAt(childPosition).getTag();
								viewHolder.avatar.setImageBitmap(bmp);
							}
						}
						
						avatar_requests.remove(username);
					}
				});
			}
		}

		@Override
		public void retrieveBadge(final String badge_id, boolean scaled, final Bitmap bmp) {
			
		}

		@Override
		public void retrieveAttach(final String token, final boolean is_thumb, final Bitmap bmp) {
			
		}

		@Override
		public void retrieveProgramIcon(String title, int sampleBase, Bitmap bmp) {

		}

		@Override
		public void retrieveChannelIcon(String chcode, Bitmap bmp) {

		}
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
		if (UrlImageManager.isUrlImageExistInStorage(imageUrl, SampleBase.RIGOROUS_SAMPLED)) 
			return;
		
		// download image
		//
		final Bitmap bmp = UrlImageManager.downloadUrlImage(imageUrl, SampleBase.RIGOROUS_SAMPLED);
		if (bmp == null)
			return;
		
		if (friend_listview != null) {
			friend_listview.post( new Runnable () {
				public void run () {
					if (friend_listview == null)
						return;
					
					int firstPosition = friend_listview.getFirstVisiblePosition();
					int lastPosition = friend_listview.getLastVisiblePosition();
					
					for (int i = firstPosition; i <= lastPosition; i++) {
						Object obj = getItem(i);
						if (obj == null || !FBUser.class.isInstance(obj))
							continue;
					
						FBUser user = (FBUser) obj;
						if (imageUrl.equals(user.getPicSquare())) {
							int childPosition = i - friend_listview.getFirstVisiblePosition();
							View view = friend_listview.getChildAt(childPosition);
							ViewHolder viewHolder = (ViewHolder) view.getTag();
							viewHolder.avatar.setImageBitmap(bmp);
						}
					}
				}
			});
		}
	}
}
