package com.wildmind.fanwave.friend;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wildmind.fanwave.activity.FriendsFacebookActivity;
import com.wildmind.fanwave.activity.FriendsSearchActivity;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.facebook.FBPendingCommand;
import com.wildmind.fanwave.facebook.FacebookManager;

import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.user.TVUser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendListAdapter extends BaseAdapter {
	
	/**
	 * List type.
	 */
	public static final int TYPE_LIST_STATUS 	= 0;
	public static final int TYPE_LIST_REQUEST 	= 1;
	public static final int TYPE_LIST_FIND 		= 2;
	
	/**
	 * Row view type count.
	 * 
	 * Status 	bar  		: 0	
	 * 			text 		: 1	
	 * 			user 		: 2
	 * Request	user 		: 3
	 * Find		platform 	: 4
	 */
	public static final int ROW_VIEW_TYPE_COUNT 	= 5;
	public static final int STATUS_ROW_TYPE_BAR  	= 0;
	public static final int STATUS_ROW_TYPE_TEXT 	= 1;
	public static final int STATUS_ROW_TYPE_USER 	= 2;
	public static final int REQUEST_ROW_TYPE_USER 	= 3;
	public static final int FIND_ROW_TYPE_PLATFORM 	= 4;
	
	private ListView friend_listview;
	private Context context;
	private LayoutInflater inflater;
	private int list_type = -1;
	
	private PlatformRequestListener platform_listener = null;
	private FriendListImageListener image_listener = new FriendListImageListener();
	private ConcurrentHashMap<String, String> avatar_requests = new ConcurrentHashMap<String, String>();
	private ConcurrentHashMap<String, String> badge_requests = new ConcurrentHashMap<String, String>();
	
	/**
	 * Status array list.
	 */
	private ArrayList<TVUser> onlines = null;
	private ArrayList<TVUser> offlines = null;
	
	/**
	 * Request array list.
	 */
	private ArrayList<TVUser> requests = null;
	
	/**
	 * Find platform array list.
	 */
	private ArrayList<String> platforms = null;
	
	
	/**
	 * Constructor
	 * @param listview
	 * @param type
	 * @param context
	 */
	public FriendListAdapter (ListView listview, int type, Context context) {
		this.friend_listview = listview;
		this.list_type = type;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		
		ImageManager.addImageListener(image_listener);
	}
	
	/**
	 * Constructor for request type.
	 * @param listview
	 * @param users
	 * @param context
	 */
	public FriendListAdapter (ListView listview, ArrayList<TVUser> users, Context context) {
		this(listview, TYPE_LIST_REQUEST, context);
		this.requests = users;
	}
	
	/**
	 * Constructor for status type.
	 * @param listview
	 * @param onlineList
	 * @param offlineList
	 * @param context
	 */
	public FriendListAdapter (ListView listview, ArrayList<TVUser> onlineList, 
			ArrayList<TVUser> offlineList, Context context) {
		this(listview, TYPE_LIST_STATUS, context);
		this.onlines = onlineList;
		this.offlines = offlineList;
	}
	
	// getters
	//
	public ArrayList<TVUser> getOnlines () {
		return onlines;
	}
	
	public ArrayList<TVUser> getOfflines () {
		return offlines;
	}
	
	public ArrayList<TVUser> getRequests () {
		return requests;
	}
	
	// setters
	//
	public void setPlatformRequestListener (PlatformRequestListener listener) {
		this.platform_listener = listener;
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
		onlines = null;
		offlines = null;
		requests = null;
		platforms = null;
		
		avatar_requests.clear();
		avatar_requests = null;
		badge_requests.clear();
		badge_requests = null;
	}
	
	@Override
	public int getCount() {
		
		switch (list_type) {
		
		case TYPE_LIST_STATUS:
			return getStatusCount();
			
		case TYPE_LIST_REQUEST:
			return requests != null ? requests.size() : 0;
			
		case TYPE_LIST_FIND:
			return platforms != null ? platforms.size() : 0;
			
		default:
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		
		switch (list_type) {
		
		case TYPE_LIST_STATUS:
			int userPosition = 0;
			TVUser user = null;
			boolean online = position < getStatusOfflinePosition();
			if (online) {
				// user position in onlines
				userPosition = position - 1;
				user = onlines.get(userPosition);
			} else {
				// user position in offlines
				userPosition = position - ((onlines.size() > 0 ? onlines.size() : 1) + 2);
				user = offlines.get(userPosition);
			}
			return user;
			
		case TYPE_LIST_REQUEST:
			return requests != null ? requests.get(position) : null;
			
		case TYPE_LIST_FIND:
			return platforms != null ? platforms.get(position) : null;
			
		default:
			return null;
		}
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
		
		case TYPE_LIST_STATUS:
			return getStatusRowType(position);
			
		case TYPE_LIST_REQUEST:
			return REQUEST_ROW_TYPE_USER;
			
		case TYPE_LIST_FIND:
			return FIND_ROW_TYPE_PLATFORM;
			
		default:
			return IGNORE_ITEM_VIEW_TYPE;
		}
	}
	
	@Override
	public boolean isEnabled (int position) {
		
		switch (list_type) {
		
		case TYPE_LIST_STATUS:
			return isStatusRowEnabled(position);
			
		case TYPE_LIST_REQUEST:
			return true;
			
		case TYPE_LIST_FIND:
			return false;
				
		default:
			return true;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		switch(list_type) {
		
		case TYPE_LIST_STATUS:
			return getStatusView(convertView, position);
			
		case TYPE_LIST_REQUEST:
			return getRequestView(requests.get(position), convertView, position);
			
		case TYPE_LIST_FIND:
			return getFindView(platforms.get(position), convertView, position);
			
		default:
			return null;
		}
	}
	
	public int getStatusOfflinePosition () {
		int count = 0;
		
		// online bar
		count++;
		
		// text or users
		count += onlines.size() > 0 ? onlines.size() : 1;
		
		return count;
	}
	
	private int getStatusCount () {
		// no friend, show nothing
		if (onlines == null || offlines == null || onlines.size() + offlines.size() == 0)
			return 0;
		
		// sum rows count for friends
		int count = 0;
		// online bar
		count++;
		// if no online, insert a row for text
		count += onlines.size() > 0 ? onlines.size() : 1;
		// offline bar
		count++;
		// if no offline, insert a row for text
		count += offlines.size() > 0 ? offlines.size() : 1;
		
		return count;
	}
	
	/**
	 * Get status row type.
	 * @param position row position
	 * @return int type
	 */
	public int getStatusRowType (int position) {
		
		// status bar
		if (position == 0 || position == getStatusOfflinePosition())
			return STATUS_ROW_TYPE_BAR;
		
		// no online or offline text
		if ((position == 1 && onlines.size() == 0) ||
			(position == getStatusOfflinePosition() + 1 && offlines.size() == 0))
			return STATUS_ROW_TYPE_TEXT;
		
		// user
		return STATUS_ROW_TYPE_USER;
	}
	
	/**
	 * Whether status row is enabled.
	 * @param position
	 * @return
	 */
	private boolean isStatusRowEnabled (int position) {
		switch (getStatusRowType(position)) {
		case STATUS_ROW_TYPE_BAR:
			return false;
		case STATUS_ROW_TYPE_TEXT:
			return false;
		case STATUS_ROW_TYPE_USER:
			return true;
		default:
			return true;
		}
	}

	private View getStatusView (View view, int position) {
		
		switch (getStatusRowType(position)) {
		
		case STATUS_ROW_TYPE_BAR:
			return getStatusBarView(view, position == 0);
			
		case STATUS_ROW_TYPE_TEXT:
			return getStatusTextView(view, position == 1);
			
		case STATUS_ROW_TYPE_USER:
			int userPosition = 0;
			TVUser user = null;
			boolean online = position < getStatusOfflinePosition();
			if (online) {
				// user position in onlines
				userPosition = position - 1;
				user = onlines.get(userPosition);
			} else {
				// user position in offlines
				userPosition = position - ((onlines.size() > 0 ? onlines.size() : 1) + 2);
				user = offlines.get(userPosition);
			}
			
			return getStatusUserView(user, view, online, userPosition);
		
		default:
			return null;
		}
	}
	
	private View getStatusBarView (View view, boolean online) {
		if (view == null) {
			view = new TextView(context);
			((TextView) view).setTextColor(Color.WHITE);
			((TextView) view).setTextSize(14);
			((TextView) view).setTypeface(null,Typeface.BOLD);
			((TextView) view).setGravity(Gravity.CENTER_VERTICAL);
			((TextView) view).setPadding(16, 0, 0, 0);
		}
		view.setBackgroundResource(online ? R.drawable.bar_green : R.drawable.bar_gray);
		((TextView) view).setText(online ? R.string.friend_online : R.string.friend_offline);
		
		return view;
	}
	
	private View getStatusTextView (View view, boolean online) {
		if (view == null) {
			view = new TextView(context);
			view.setPadding(28,28,28,28);
			((TextView)view).setGravity(Gravity.CENTER);
			((TextView)view).setTextSize(14);
			((TextView)view).setTextColor(context.getResources().getColor(R.color.mood_color));
		}
		((TextView) view).setText(online ? context.getResources().getString(R.string.friend_no_online).toString() : context.getResources().getString(R.string.friend_no_offline).toString());
		
		return view;
	}
	
	private View getStatusUserView (TVUser user, View view, boolean online, int position) {
		StatusUserViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.friend_row_status, null);
			viewHolder = new StatusUserViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			viewHolder.mood = (TextView) view.findViewById(R.id.mood_textview);
			viewHolder.badge = (ImageView) view.findViewById(R.id.badge_imageview);
			viewHolder.divider = (ImageView) view.findViewById(R.id.divider_imageview);
			view.setTag(viewHolder);
		} else 
			viewHolder = (StatusUserViewHolder) view.getTag();
		
		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		loadAvatar(viewHolder.avatar, user.getUsername());
		
		// Nickname
		viewHolder.nickname.setText(user.getNickname());
		
		// Mood
		viewHolder.mood.setText(user.getExtraInfo().getMood());
		viewHolder.mood.setVisibility((online)?View.VISIBLE:View.GONE);
		
		// Badge
		viewHolder.badge.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.badge_loading));
		loadBadge(viewHolder.badge, user.getBadgeId());
		
		// Divider
		if (online && position == onlines.size() - 1)
			viewHolder.divider.setVisibility(View.INVISIBLE);
		else
			viewHolder.divider.setVisibility(View.VISIBLE);
		
		return view;
	}
	
	private View getRequestView (TVUser user, View view, int position) {
		RequestViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.friend_row_request, null);
			viewHolder = new RequestViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.nickname = (TextView) view.findViewById(R.id.nickname_textview);
			viewHolder.mood = (TextView) view.findViewById(R.id.mood_textview);
			viewHolder.confirm = (Button) view.findViewById(R.id.confirm_button);
			viewHolder.ignore = (Button) view.findViewById(R.id.ignore_button);
			
			// no mood for current version
			viewHolder.mood.setVisibility(View.GONE);
			
			view.setTag(viewHolder);
		} else 
			viewHolder = (RequestViewHolder) view.getTag();
		
		// Avatar
		viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.avatar_loading));
		loadAvatar(viewHolder.avatar, user.getUsername());
		
		// Nickname
		viewHolder.nickname.setText(user.getNickname());
		
		// Mood
		
		// Confirm, Ingore
		View.OnClickListener listener = getRequestButtonClickedListener(user);
		viewHolder.confirm.setOnClickListener(listener);
		viewHolder.ignore.setOnClickListener(listener);
		
		return view;
	}
	
	private View getFindView (String platform, View view, int position) {
		FindViewHolder viewHolder;
		if (view == null) {
			view = inflater.inflate(R.layout.friend_row_find, null);
			viewHolder = new FindViewHolder();
			viewHolder.avatar = (ImageView) view.findViewById(R.id.avatar_imageview);
			viewHolder.platform = (TextView) view.findViewById(R.id.platform_textview);
			viewHolder.find = (Button) view.findViewById(R.id.find_button);
			view.setTag(viewHolder);
		} else
			viewHolder = (FindViewHolder) view.getTag();
		
		// Avatar
		if (platform.equals(ApplicationManager.PLATFORM_FANWAVE))
			viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.fanwave_avatar));
		else if (platform.equals(ApplicationManager.PLATFORM_FACEBOOK))
			viewHolder.avatar.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.facebook_avatar));
		
		// Platform
		viewHolder.platform.setText(platform);

		// Find
		if (platform.equals(ApplicationManager.PLATFORM_FANWAVE))
			viewHolder.find.setOnClickListener(getFanwaveSearchClickedListener());
		else if (platform.equals(ApplicationManager.PLATFORM_FACEBOOK))
			viewHolder.find.setOnClickListener(getFacebookSearchClickedListener());
		
		return view;
	}
	
	/**
	 * Callback for request button clicked.
	 * @param user
	 * @return
	 */
	private View.OnClickListener getRequestButtonClickedListener (final TVUser user) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.confirm_button)
					FriendManager.acceptFriend(user);
				else if (v.getId() == R.id.ignore_button)
					FriendManager.rejectFriend(user);
				
				// remove user from request and refresh list
				requests.remove(user);
				notifyDataSetChanged();
			}
		};
	}
	
	/**
	 * Callback for Fanwave search clicked.
	 * @return
	 */
	private View.OnClickListener getFanwaveSearchClickedListener () {
		return new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				// Search Edit Text
				final EditText searchEditText = new EditText(context);
				searchEditText.setTextSize(14);
				searchEditText.setHint(R.string.friend_search_nickname_hint);
				
				// Button
				DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener () {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String searchTerm = searchEditText.getText().toString();
						if (searchTerm.getBytes().length < 4) 
							Toast.makeText(context, R.string.sign_up_err_nickname, Toast.LENGTH_SHORT).show();
						else {
							Intent i = new Intent(context, FriendsSearchActivity.class);
							i.putExtra("search_term", searchTerm);
							context.startActivity(i);
						}
					}
				};
				
				// Dialog
				AlertDialog dialog = new AlertDialog.Builder(context)
				   .setTitle(R.string.friend_search_nickname_title)
				   .setView(searchEditText)
				   .setPositiveButton(R.string.action_confirm, positive)
				   .setNegativeButton(R.string.action_cancel, null)
				   .create();
				dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				dialog.show();
			}
		};
	}
	
	/**
	 * Callback for Facebook clicked.
	 * @return
	 */
	private View.OnClickListener getFacebookSearchClickedListener () {
		return new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (!FacebookManager.isFacebookActive()) {
					// Button
					DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener () {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (platform_listener != null)
								platform_listener.onFacebookActiveRequest(new FriendCommand());
						}
					};
					// Dialog
					new AlertDialog.Builder(context)
								   .setTitle(R.string.app_message)
								   .setMessage(R.string.fb_link_request)
								   .setPositiveButton(R.string.action_confirm, positive)
								   .setNegativeButton(R.string.action_cancel, null)
								   .show();
				} else {
					Intent i = new Intent(context, FriendsFacebookActivity.class);
					context.startActivity(i);
				}
			}
		};
	}
	
	/**
	 * Refresh status list data.
	 * @param onlineList
	 * @param offlineList
	 */
	public void refreshStatusData (ArrayList<TVUser> onlineList, ArrayList<TVUser> offlineList) {
		this.requests = null;
		this.platforms = null;
		this.onlines = onlineList;
		this.offlines = offlineList;
		this.list_type = TYPE_LIST_STATUS;
		this.notifyDataSetChanged();
	}
	
	/**
	 * Refresh requests data.
	 * @param list
	 * @param type
	 */
	public void refreshRequestData (ArrayList<TVUser> requestList) {
		this.onlines = null;
		this.offlines = null;
		this.platforms = null;
		this.requests = requestList;
		this.list_type = TYPE_LIST_REQUEST;
		this.notifyDataSetChanged();
	}
	
	/**
	 * Refresh platforms data.
	 * @param platforms
	 */
	public void refreshplatformsData (ArrayList<String> platforms) {
		this.onlines = null;
		this.offlines = null;
		this.requests = null;
		this.platforms = platforms;
		this.list_type = TYPE_LIST_FIND;
		this.notifyDataSetChanged();
	}

	/**
	 * Status user row view holder.
	 * @author Kencool
	 *
	 */
	private class StatusUserViewHolder {
		ImageView 	avatar;
		ImageView	badge;
		ImageView	divider;
		TextView	nickname;
		TextView	mood;
	}
	/**
	 * Request user row view holder.
	 * @author Kencool
	 *
	 */
	private class RequestViewHolder {
		ImageView 	avatar;
		TextView	nickname;
		TextView	mood;
		Button		confirm;
		Button		ignore;
	}
	/**
	 * Find row view holder.
	 * @author Kencool
	 *
	 */
	private class FindViewHolder {
		ImageView	avatar;
		TextView	platform;
		Button		find;
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
	 * Load badge.
	 * @param iv
	 * @param badge_id
	 */
	private void loadBadge (ImageView iv, String badge_id) {
		if (badge_id == null || badge_id.length() == 0)
			return;
		
		if (!ImageManager.drawBadgeImage(iv, badge_id, true))
			badge_requests.put(badge_id, "");
	}
	
	/**
	 * Draw status user avatar.
	 * @param username
	 * @param bmp
	 */
	private void drawStatusAvatar (String username, Bitmap bmp) {
		int firstPosition = friend_listview.getFirstVisiblePosition();
		int lastPosition = friend_listview.getLastVisiblePosition();
		
		for (int i = firstPosition; i <= lastPosition; i++) {
			// not an user row
			if ( i == 0 || i == getStatusOfflinePosition() ||
				(i == 1 && onlines.size() == 0) || 
				(i == getStatusOfflinePosition() + 1 && offlines.size() == 0) )
				continue;
			
			TVUser user = null;
			user = i < getStatusOfflinePosition() 
				 ? onlines.get(i-1) 
				 : offlines.get(i - ((onlines.size() > 0 ? onlines.size() : 1) + 2));
					 
			if (username.equals(user.getUsername())) {
				int childPosition = i - firstPosition;
				StatusUserViewHolder viewHolder = 
						(StatusUserViewHolder) friend_listview.getChildAt(childPosition).getTag();
				viewHolder.avatar.setImageBitmap(bmp);
			}
		}
	}
	
	/**
	 * Draw request user avatar.
	 * @param username
	 * @param bmp
	 */
	private void drawRequestAvatar (String username, Bitmap bmp) {
		for (int i = friend_listview.getFirstVisiblePosition(); 
				 i <= friend_listview.getLastVisiblePosition();
				 i++) {
				// over user list position, may be other purpose row
				if (i >= requests.size())
					break;
				
				if (username.equals(requests.get(i).getUsername())) {
					int childPosition = i - friend_listview.getFirstVisiblePosition();
					RequestViewHolder viewHolder = 
							(RequestViewHolder) friend_listview.getChildAt(childPosition).getTag();
					viewHolder.avatar.setImageBitmap(bmp);
				}
		}
	}
	
	/**
	 * Draw status user badge.
	 * @param badge_id
	 * @param bmp
	 */
	private void drawStatusBadge (String badge_id, Bitmap bmp) {
		int firstPosition = friend_listview.getFirstVisiblePosition();
		int lastPosition = friend_listview.getLastVisiblePosition();
		
		for (int i = firstPosition; i <= lastPosition; i++) {
			// not an user row
			if ( i == 0 || i == getStatusOfflinePosition() ||
				(i == 1 && onlines.size() == 0) || 
				(i == getStatusOfflinePosition() + 1 && offlines.size() == 0) )
				continue;
			
			TVUser user = i < getStatusOfflinePosition() 
						? onlines.get(i-1) 
						: offlines.get(i - ((onlines.size() > 0 ? onlines.size() : 1) + 2));
					 
			if (badge_id.equals(user.getBadgeId())) {
				int childPosition = i - firstPosition;
				StatusUserViewHolder viewHolder = 
						(StatusUserViewHolder) friend_listview.getChildAt(childPosition).getTag();
				viewHolder.badge.setImageBitmap(bmp);
			}
		}
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
						switch (list_type) {
						
						case TYPE_LIST_STATUS:
							drawStatusAvatar(username, bmp);
							break;
							
						case TYPE_LIST_REQUEST:
							drawRequestAvatar(username, bmp);
							break;
							
						case TYPE_LIST_FIND:
							break;
							
						default:
							break;
						}
						
						avatar_requests.remove(username);
					}
				});
			}
		}

		@Override
		public void retrieveBadge(final String badge_id, final boolean scaled, final Bitmap bmp) {
			if (badge_requests == null || !badge_requests.containsKey(badge_id) || !scaled || bmp == null)
				return;
			
			if (friend_listview != null) {
				friend_listview.post( new Runnable () {
					public void run () {
						if (friend_listview == null)
							return;
						
						// go through current visible rows and check if refresh is need
						switch (list_type) {
						
						case TYPE_LIST_STATUS:
							drawStatusBadge(badge_id, bmp);
							break;
							
						case TYPE_LIST_REQUEST:
							break;
							
						case TYPE_LIST_FIND:
							break;
							
						default:
							break;
						}
						
						badge_requests.remove(badge_id);
					}
				});
			}
		}

		@Override
		public void retrieveAttach(final String token, final boolean is_thumb, final Bitmap bmp) {
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
	
	private class FriendCommand implements FBPendingCommand {
		@Override
		public void execute() {
			if (friend_listview != null) {
				friend_listview.post( new Runnable () {
					public void run () {
						Intent i = new Intent(context, FriendsFacebookActivity.class);
						context.startActivity(i);
					}
				});
			}
		}
	}
	
	public interface PlatformRequestListener {
		
		public void onFacebookActiveRequest(FBPendingCommand command);
	}
}
