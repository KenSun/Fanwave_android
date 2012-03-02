package com.wildmind.fanwave.friend;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.animation.AlphaSetter;
import com.wildmind.fanwave.app.ApplicationManager;

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

public class PersonalFriendAdapter extends BaseAdapter {

	/**
	 * List type.
	 */
	public static final int TYPE_LIST_SELF 	= 0;
	public static final int TYPE_LIST_OTHER = 1;
	
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
	
	private ListView friend_listview;
	
	private Context context;
	private LayoutInflater inflater;
	private int list_type = -1;
	
	private ArrayList<TVUser> users = null;
	private ArrayList<TVUser> common_friends = null;
	private ArrayList<TVUser> request_friends = null;
	private ArrayList<TVUser> inviting_friends = null;
	private ArrayList<TVUser> uncommon_friends = null;
	
	private FriendListImageListener image_listener = new FriendListImageListener();
	private ConcurrentHashMap<String, String> avatar_requests = new ConcurrentHashMap<String, String>();
	
	/**
	 * Constructor
	 * @param listview
	 * @param users
	 * @param type
	 * @param context
	 */
	public PersonalFriendAdapter (ListView listview, int type, Context context) {
		this.friend_listview = listview;
		this.list_type = type;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Constructor for TYPE_LIST_SELF
	 * @param listview
	 * @param users
	 * @param context
	 */
	public PersonalFriendAdapter (ListView listview, ArrayList<TVUser> users, Context context) {
		this(listview, TYPE_LIST_SELF, context);
		this.users = users;
	}
	
	/**
	 * Constructor for TYPE_LIST_OTHER
	 * @param listview
	 * @param commons
	 * @param requests
	 * @param invitings
	 * @param uncommons
	 * @param context
	 */
	public PersonalFriendAdapter (ListView listview, ArrayList<TVUser> commons, ArrayList<TVUser> requests,
			ArrayList<TVUser> invitings, ArrayList<TVUser> uncommons, Context context) {
		this(listview, TYPE_LIST_OTHER, context);
		this.common_friends = commons;
		this.request_friends = requests;
		this.inviting_friends = invitings;
		this.uncommon_friends = uncommons;
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
		users = null;
		common_friends = null;
		request_friends = null;
		inviting_friends = null;
		uncommon_friends = null;		
		avatar_requests.clear();
		avatar_requests = null;
	}
	
	@Override
	public int getCount() {
		int count = 0;
		switch (list_type) {
		case TYPE_LIST_SELF:
			count = users != null ? users.size() : 0;
			break;
		case TYPE_LIST_OTHER:
			if (common_friends != null)
				count += common_friends.size() > 0 ? common_friends.size() + 1 : 0;
			int uncommonCount = 0;
			uncommonCount += request_friends != null ? request_friends.size() : 0;
			uncommonCount += inviting_friends != null ? inviting_friends.size() : 0;
			uncommonCount += uncommon_friends != null ? uncommon_friends.size() : 0;
			count += uncommonCount > 0 ? uncommonCount + 1 : 0;
			break;
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		Object obj = null;
		switch (list_type) {
		case TYPE_LIST_SELF:
			if (position < users.size())
				obj = users != null ? users.get(position) : null;
			break;
		case TYPE_LIST_OTHER:
			if (getItemViewType(position) != ROW_TYPE_BAR) {
				obj = getCommonItem(position);
				if (obj == null)
					obj = getRequestItem(position);
				if (obj == null)
					obj = getInvitingItem(position);
				if (obj == null)
					obj = getUncommonItem(position);
			}
			break;
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
		
		switch (list_type) {
		
		case TYPE_LIST_SELF:
			return ROW_TYPE_SIMPLE;
			
		case TYPE_LIST_OTHER:
			return getOtherRowType (position);
			
		default:
			return IGNORE_ITEM_VIEW_TYPE;
		}
	}
	
	@Override
	public boolean isEnabled (int position) {
		switch (list_type) {
		
		case TYPE_LIST_SELF:
			return true;
			
		case TYPE_LIST_OTHER:
			return getItemViewType(position) != ROW_TYPE_BAR;
			
		default:
			return false;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Object obj = getItem(position);
		TVUser user = obj != null ? (TVUser) obj : null;
		switch (list_type) {
		case TYPE_LIST_SELF:
			return getSimpleView(user, convertView, position);
			
		case TYPE_LIST_OTHER:
			switch (getOtherRowType(position)) {
			case ROW_TYPE_BAR:
				return getBarView(position, convertView);
			case ROW_TYPE_SIMPLE:
				return getSimpleView(user, convertView, position);
			case ROW_TYPE_COMPLEX:
				return getComplexView(user, convertView, position);
			}
			
		default:
			return null;
		}
	}
	
	/**
	 * Get common bar position.
	 * @return int
	 */
	public int getCommonBarPosition () {
		return common_friends != null && common_friends.size() > 0 ? 0 : -1;
	}
	
	/**
	 * Get uncommon bar position.
	 * @return int
	 */
	public int getUncommonBarPosition () {
		int uncommonCount = 0;
		uncommonCount += request_friends != null ? request_friends.size() : 0;
		uncommonCount += inviting_friends != null ? inviting_friends.size() : 0;
		uncommonCount += uncommon_friends != null ? uncommon_friends.size() : 0;
		if (uncommonCount == 0)
			return -1;
		else {
			return getCommonBarPosition() != -1 ? common_friends.size() + 1 : 0;
		}
	}
	
	/**
	 * Get first common item position.
	 * @return
	 */
	public int getCommonPosition () {
		return common_friends != null && common_friends.size() > 0 ? 1 : -1;
	}
	
	/**
	 * Get first request item position.
	 * @return int	first position, -1 if no request item.
	 */
	public int getRequestPosition () {
		if (request_friends == null || request_friends.size() == 0)
			return -1;
		
		// add bar
		int position = 1;
		// add common friend positions
		position += common_friends != null && common_friends.size() > 0 ? common_friends.size() + 1 : 0;
		
		return position;
	}
	
	/**
	 * Get first inviting item position.
	 * @return int	first position, -1 if no inviting item.
	 */
	public int getInvitingPosition () {
		if (inviting_friends == null || inviting_friends.size() == 0)
			return -1;
		
		// add bar
		int position = 1;
		// add common friend positions
		position += common_friends != null && common_friends.size() > 0 ? common_friends.size() + 1 : 0;
		// add request friend positions
		position += request_friends != null && request_friends.size() > 0 ? request_friends.size() : 0;
		
		return position;
	}
	
	/**
	 * Get first uncommon item position.
	 * @return int 	first position, -1 if no uncommon item.
	 */
	public int getUncommonPosition () {
		if (uncommon_friends == null || uncommon_friends.size() == 0)
			return -1;
		
		// add bar
		int position = 1;
		// add common friend positions
		position += common_friends != null && common_friends.size() > 0 ? common_friends.size() + 1 : 0;
		// add request friend positions
		position += request_friends != null && request_friends.size() > 0 ? request_friends.size() : 0;
		// add inviting friend positions
		position += inviting_friends != null && inviting_friends.size() > 0 ? inviting_friends.size() : 0;
		
		return position;
	}
	
	/**
	 * Get common friend item for position.
	 * If no common friend items or position is not in common friend section, return null
	 * @param position
	 * @return Object or null
	 */
	public TVUser getCommonItem (int position) {
		if (common_friends == null || common_friends.size() == 0)
			return null;
		
		return position <= common_friends.size() ? common_friends.get(position - 1) : null;
	}
	
	/**
	 * Get request friend item for position.
	 * If no request friend items or position is not in request friend section, return null
	 * @param position
	 * @return Object or null
	 */
	public TVUser getRequestItem (int position) {
		if (request_friends == null || request_friends.size() == 0)
			return null;
		
		int itemPosition = position - getRequestPosition();
		return 0 <= itemPosition && itemPosition < request_friends.size() 
					? request_friends.get(itemPosition) : null;
	}
	
	/**
	 * Get inviting friend item for position.
	 * If no inviting friend items or position is not in inviting friend section, return null
	 * @param position
	 * @return Object or null
	 */
	public TVUser getInvitingItem (int position) {
		if (inviting_friends == null || inviting_friends.size() == 0)
			return null;
		
		int itemPosition = position - getInvitingPosition();
		return 0 <= itemPosition && itemPosition < inviting_friends.size() 
					? inviting_friends.get(itemPosition) : null;
	}
	
	/**
	 * Get uncommon friend item for position.
	 * If no uncommon friend items or position is not in uncommon friend section, return null
	 * @param position
	 * @return Object or null
	 */
	public TVUser getUncommonItem (int position) {
		if (uncommon_friends == null || uncommon_friends.size() == 0)
			return null;
		
		int itemPosition = position - getUncommonPosition();
		return 0 <= itemPosition && itemPosition < uncommon_friends.size() 
					? uncommon_friends.get(itemPosition) : null;
	}
	
	/**
	 * Get other list row type.
	 * @param position
	 * @return
	 */
	private int getOtherRowType (int position) {
		if (position == getCommonBarPosition() || position == getUncommonBarPosition())
			return ROW_TYPE_BAR;
		else if (getUncommonItem(position) != null)
			return ROW_TYPE_COMPLEX;
		else
			return ROW_TYPE_SIMPLE;
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
		
		if (position == getCommonBarPosition()) 
			((TextView) view).setText(ApplicationManager.getAppContext().getString(R.string.personal_friend_common));
		else if (position == getUncommonBarPosition())
			((TextView) view).setText(ApplicationManager.getAppContext().getString(R.string.personal_friend_uncommon));
		
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
		if (list_type == TYPE_LIST_SELF) {
			viewHolder.action_button.setVisibility(View.VISIBLE);
			viewHolder.action_button.setEnabled(true);
			viewHolder.action_button.setText(R.string.action_remove);
			viewHolder.action_button.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));	
			viewHolder.action_button.setTextColor(context.getResources().getColor(R.drawable.color_light_brown_white_selector));
		} else {
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
		}
		if (viewHolder.action_button.isEnabled())
			viewHolder.action_button.setOnClickListener(getActionButtonClickedListener(user));
		
		// Divider
		boolean invisible = position == getUncommonBarPosition() - 1;
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
					inviting_friends.add(user);
					uncommon_friends.remove(user);
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
				if (list_type == TYPE_LIST_SELF) {
					// button
					DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							FriendManager.removeFriend(user);
							users.remove(user);
							notifyDataSetChanged();
						}
					};
					
					// alert dialog
					new AlertDialog.Builder(context)
								   .setTitle(user.getNickname())
								   .setMessage(context.getResources().getString(R.string.personal_remove_friend_description))
								   .setPositiveButton(R.string.action_confirm, positive)
								   .setNegativeButton(R.string.action_cancel, null)
								   .show();
				} else {
					if (FriendManager.isRequest(user.getUsername())) {
						if (FriendManager.acceptFriend(user)) {
							common_friends.add(user);
							request_friends.remove(user);
							notifyDataSetChanged();
						}
					}
				}
			}
		};
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
	private class FriendListImageListener implements ImageListener {

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
							TVUser user = (TVUser) getItem(i);
							if (user != null && username.equals(user.getUsername())) {
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
			// TODO Auto-generated method stub
			
		}

		@Override
		public void retrieveChannelIcon(String chcode, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
