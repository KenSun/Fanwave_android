package com.wildmind.fanwave.user;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.animation.AlphaSetter;
import com.wildmind.fanwave.friend.FriendManager;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;

import android.content.Context;
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

public class FriendedUserAdapter extends BaseAdapter {

	/**
	 * Row view type count.
	 * 
	 * Bar row		: 0
	 * Complex row	: 1	row with action layout on the right side
	 * Simple row	: 2 row with simple button on the right side
	 */
	public static final int ROW_VIEW_TYPE_COUNT = 3;
	public static final int ROW_TYPE_BAR		= 0;
	public static final int ROW_TYPE_COMPLEX 	= 1;
	public static final int ROW_TYPE_SIMPLE 	= 2;
	
	private ListView user_listview;
	
	private Context context;
	private LayoutInflater inflater;
	
	private String friend_title, nonfriend_title;
	private ArrayList<TVUser> friends 	= new ArrayList<TVUser>();
	private ArrayList<TVUser> requests 	= new ArrayList<TVUser>();
	private ArrayList<TVUser> invitings = new ArrayList<TVUser>();
	private ArrayList<TVUser> strangers = new ArrayList<TVUser>();
	private UserImageListener image_listener = new UserImageListener();
	private ConcurrentHashMap<String, String> avatar_requests = new ConcurrentHashMap<String, String>();
	
	private boolean filter_out_self = false;
	
	public FriendedUserAdapter (ListView listview, ArrayList<TVUser> users, String friendTitle, 
			String nonfriendTitle, Context context) {
		this.user_listview = listview;
		this.friend_title = friendTitle;
		this.nonfriend_title = nonfriendTitle;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		
		filterUsers(users);
		ImageManager.addImageListener(image_listener);
	}
	
	public void filterOutSelf (boolean filterOut) {
		this.filter_out_self = filterOut;
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
			if (filter_out_self && user.getUsername().equals(AccountManager.getCurrentUser().getUsername()))
				continue;
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
		
		user_listview = null;
		context = null;
		inflater = null;
		
		friends = null;
		requests = null;
		invitings = null;
		strangers = null;
		
		avatar_requests.clear();
		avatar_requests = null;
	}
	
	@Override
	public int getCount() {
		int count = friends.size() > 0 ? friends.size() + 1 : 0;
		int nonfriendCount = requests.size() + invitings.size() + strangers.size();
		count += nonfriendCount > 0 ? nonfriendCount + 1 : 0;
		
		return count;
	}
	
	@Override
	public Object getItem(int position) {
		Object obj = null;
		if (getItemViewType(position) != ROW_TYPE_BAR) {
			obj = getFriendItem(position);
			if (obj == null)
				obj = getRequestItem(position);
			if (obj == null)
				obj = getInvitingItem(position);
			if (obj == null)
				obj = getStrangerItem(position);
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
		if (position == getFriendBarPosition() || position == getNonfriendBarPosition())
			return ROW_TYPE_BAR;
		else if (getStrangerItem(position) != null)
			return ROW_TYPE_COMPLEX;
		else
			return ROW_TYPE_SIMPLE;
	}
	
	@Override
	public boolean isEnabled (int position) {
		return getItemViewType(position) != ROW_TYPE_BAR;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Object obj = getItem(position);
		TVUser user = obj != null ? (TVUser) obj : null;
		switch (getItemViewType(position)) {
		case ROW_TYPE_BAR:
			return getBarView(position, convertView);
		case ROW_TYPE_SIMPLE:
			return getSimpleView(user, convertView, position);
		case ROW_TYPE_COMPLEX:
			return getComplexView(user, convertView, position);
		default:
			return null;
		}
	}
	
	/**
	 * Get friend bar position.
	 * @return int
	 */
	public int getFriendBarPosition () {
		return friends.size() > 0 ? 0 : -1;
	}
	
	/**
	 * Get nonfriend bar position.
	 * @return int
	 */
	public int getNonfriendBarPosition () {
		int nonfriendCount = requests.size() + invitings.size() + strangers.size();
		if (nonfriendCount == 0)
			return -1;
		else
			return friends.size() > 0 ? friends.size() + 1 : 0;
	}
	
	/**
	 * Get first request item position.
	 * @return int	first position, -1 if no request item.
	 */
	public int getRequestPosition () {
		if (requests.size() == 0)
			return -1;
		
		// add bar
		int position = 1;
		// add friends positions
		position += friends.size() > 0 ?friends.size() + 1 : 0;
		
		return position;
	}
	
	/**
	 * Get first inviting item position.
	 * @return int	first position, -1 if no inviting item.
	 */
	public int getInvitingPosition () {
		if (invitings.size() == 0)
			return -1;
		
		// add bar
		int position = 1;
		// add friends positions
		position += friends.size() > 0 ? friends.size() + 1 : 0;
		// add requests positions
		position += requests.size() > 0 ? requests.size() : 0;
		
		return position;
	}
	
	/**
	 * Get first stranger item position.
	 * @return int 	first position, -1 if no stranger item.
	 */
	public int getStrangerPosition () {
		if (strangers.size() == 0)
			return -1;
		
		// add bar
		int position = 1;
		// add friends positions
		position += friends.size() > 0 ? friends.size() + 1 : 0;
		// add requests positions
		position += requests.size() > 0 ? requests.size() : 0;
		// add inviting friend positions
		position += invitings.size() > 0 ? invitings.size() : 0;
		
		return position;
	}
	
	/**
	 * Get friend item for position.
	 * If no friend items or position is not in friend section, return null.
	 * @param position
	 * @return Object or null
	 */
	public TVUser getFriendItem (int position) {
		if (friends.size() == 0)
			return null;
		
		return position <= friends.size() ? friends.get(position - 1) : null;
	}
	
	/**
	 * Get request item for position.
	 * If no request items or position is not in request section, return null.
	 * @param position
	 * @return Object or null
	 */
	public TVUser getRequestItem (int position) {
		if (requests.size() == 0)
			return null;
		
		int itemPosition = position - getRequestPosition();
		return 0 <= itemPosition && itemPosition < requests.size() ? requests.get(itemPosition) : null;
	}
	
	/**
	 * Get inviting item for position.
	 * If no inviting items or position is not in inviting section, return null.
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
	 * If no stranger items or position is not in stranger section, return null.
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
		
		if (position == getFriendBarPosition()) 
			((TextView) view).setText(friend_title);
		else if (position == getNonfriendBarPosition())
			((TextView) view).setText(nonfriend_title);
		
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
		if (user.getUsername().equals(AccountManager.getCurrentUser().getUsername()))
			viewHolder.action_layout.setVisibility(View.INVISIBLE);
		else {
			viewHolder.action_layout.setVisibility(View.VISIBLE);
			viewHolder.action_layout.setOnClickListener(getActionLayoutClickedListener(user));
		}
		
		return view;
	}
	
	/**
	 * Get simple view.
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
		boolean invisible = position == getNonfriendBarPosition() - 1;
		viewHolder.divider.setVisibility(invisible ? View.INVISIBLE : View.VISIBLE);
				
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
	 * Refresh data.
	 * @param users
	 */
	public void refreshData (ArrayList<TVUser> users) {
		filterUsers(users);
		notifyDataSetChanged();
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
	 * Listener for listening image loading requests.
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
							TVUser user = (TVUser) getItem(i);
							if (user != null && username.equals(user.getUsername())) {
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
}
