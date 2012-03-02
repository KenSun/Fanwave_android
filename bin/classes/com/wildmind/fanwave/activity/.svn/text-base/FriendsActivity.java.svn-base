package com.wildmind.fanwave.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.wildmind.fanwave.animation.FWOptionAnimation;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.facebook.FBPendingCommand;
import com.wildmind.fanwave.facebook.FacebookManager;
import com.wildmind.fanwave.friend.FriendListAdapter;
import com.wildmind.fanwave.friend.FriendListAdapter.PlatformRequestListener;
import com.wildmind.fanwave.friend.FriendManager;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.xmpp.FWXMPPManager;
import com.wildmind.fanwave.activity.R;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsActivity extends BaseActivity implements OnItemClickListener, PlatformRequestListener {

	private final int STATUS_SEGMENT 	= 0;
	private final int REQUEST_SEGMENT 	= 1;
	private final int FIND_SEGMENT 		= 2;
	
	private final int LOGIN_FACEBOOK_REQUEST = 11;
	
	private FrameLayout 		option_layout;
	private TextView 			status_textview, request_textview, find_textview, descr_textview;
	private LinearLayout 		loading_indicator;
	private ListView 			friend_listview;
	private FriendListAdapter 	friend_list_adapter;
		
	private int selected_segment = STATUS_SEGMENT;
	
	/**
	 * UI thread handler.
	 */
	private Handler handler = new Handler();
	
	/**
	 * Update friends runnable.
	 */
	private Runnable update_list = new Runnable () {
		public void run () {
			if ((selected_segment == STATUS_SEGMENT && FriendManager.isFriendListUpdated())) {
				refreshFriends();
				FriendManager.resetFriendUpdated();
			} 
			if (selected_segment == REQUEST_SEGMENT && FriendManager.isRequestListUpdated()) {
				refreshFriends();
				FriendManager.resetRequestUpdated();
			}
			
			handler.postDelayed(update_list, 3000);
		}
	};
	
	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if activity is destroyed due to abnormal situation
        if (isDestroyed())
			return;
        
        setContentView(R.layout.friends_activity);
        
     	initData();
        initUI();
        initSelection();
    }
	
	protected void onStart() {
		super.onStart();
		
		startFriendsMonitor();
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onStop() {
		super.onStop();
		
		stopFriendsMonitor();
	}

	protected void onDestroy() {
		super.onDestroy();
		
		handler.removeCallbacks(update_list);
		handler = null;
		update_list = null;
		
		if (friend_list_adapter != null)
			friend_list_adapter.clear();
		friend_list_adapter = null;
		
		friend_listview = null;
		option_layout = null;
		status_textview = null;
		request_textview = null;
		find_textview = null;
		loading_indicator = null;
        descr_textview = null;
	}
	
	private void initData () {
		
	}
	
	private void initUI () {
		// Back Button
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// Option Layout
		option_layout = (FrameLayout) findViewById(R.id.option_layout);
		
		// Status Segment Control
		status_textview = (TextView) findViewById(R.id.friend_status_textview);
		status_textview.setOnClickListener(getStatusSegmentClickedListener());
		
		// Request Segment Control
		request_textview = (TextView) findViewById(R.id.friend_request_textview);
		request_textview.setOnClickListener(getRequestSegmentClickedListener());
		
		// Find Segment Control
		find_textview = (TextView) findViewById(R.id.friend_find_textview);
		find_textview.setOnClickListener(getFindSegmentClickedListener());
		
		// Friend List View
		friend_listview = (ListView) findViewById(R.id.friend_list_listview);
		friend_listview.setDivider(null);
		friend_listview.setDividerHeight(0);
		friend_listview.setOnItemClickListener(this);
		friend_list_adapter = new FriendListAdapter(friend_listview, null, null, this);
		friend_list_adapter.setPlatformRequestListener(this);
		friend_listview.setAdapter(friend_list_adapter);
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);

        // Description Text View
        descr_textview = (TextView) findViewById(R.id.descr_textview);
	}
	
	/**
	 * Initialize selection.
	 */
	private void initSelection () {
		if (FriendManager.getRequestList().size() > 0) {
			selected_segment = REQUEST_SEGMENT;
			moveOption(REQUEST_SEGMENT, R.drawable.friends_activity_request_icon);
		}
	}
	
	/**
	 * Start monitoring friends update.
	 */
	private void startFriendsMonitor () {
		refreshFriends();
		
		handler.removeCallbacks(update_list);
    	handler.postDelayed(update_list, 3000);
	}
	
	/**
	 * Stop monitoring friends update.
	 */
	private void stopFriendsMonitor () {
		handler.removeCallbacks(update_list);
	}
	
	/**
	 * Refresh friends.
	 */
	private void refreshFriends () {
		// show loading if not connected XMPP yet
		if (!FWXMPPManager.isConnected()) {
			showFriendLoading();
			return;
		}
		
		switch (selected_segment) {
		case STATUS_SEGMENT:
			showStatusFriend();
			break;
		case REQUEST_SEGMENT:
			showRequestFriend();
			break;
		case FIND_SEGMENT:
			showFindFriend();
			break;
		default:
			break;
		}
	}


    /**
     * Show Description.
     */
    private void showDescription (String desc) {
        friend_listview.setVisibility(View.GONE);
        loading_indicator.setVisibility(View.GONE);
        descr_textview.setVisibility(View.VISIBLE);
        
        descr_textview.setText(desc);
    }
	/**
	 * Show friend list view.
	 * @return
	 */
	private void showFriendList () {
		friend_listview.setVisibility(View.VISIBLE);
		loading_indicator.setVisibility(View.GONE);
        descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show friend list loading view.
	 * @return
	 */
	private void showFriendLoading () {
		friend_listview.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.VISIBLE);
        descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show status friend.
	 */
	private void showStatusFriend () {
		
		// get online list
		ArrayList<TVUser> onlines = new ArrayList<TVUser>();
		Collection<TVUser> users = (Collection<TVUser>) FriendManager.getOnlineFriendList().values();
		for (TVUser user:users)
			onlines.add(user);
		Collections.sort(onlines);
			
		// get offline list
		ArrayList<TVUser> offlines = new ArrayList<TVUser>();
		users = (Collection<TVUser>) FriendManager.getOfflineFriendList().values();
		for (TVUser user:users)
			offlines.add(user);
		Collections.sort(offlines);		
		
		// show friends or message if no friends
		if (onlines.size() + offlines.size() == 0)
			showDescription(getString(R.string.friend_no_friends));
		else{
			showFriendList();
			friend_list_adapter.refreshStatusData(onlines, offlines);
		}
	}
	
	/**
	 * Show request friend.
	 */
	private void showRequestFriend () {
		// get request list
		ArrayList<TVUser> requests = new ArrayList<TVUser>();
		Collection<TVUser> users = (Collection<TVUser>) FriendManager.getRequestList().values();
		for (TVUser user:users)
			requests.add(user);
		Collections.sort(requests);

		// show requests or message if no requests
        if (requests.size() == 0)
            showDescription(getString(R.string.friend_no_request));
        else{
            showFriendList();
            friend_list_adapter.refreshRequestData(requests);
        }
	}
	
	/**
	 * Show find friend.
	 */
	private void showFindFriend () {
		showFriendList();
		friend_list_adapter.refreshplatformsData(ApplicationManager.getInterRosterPlatforms());
	}
	
	private void moveOption (int toOption, final int resource) {
		FWOptionAnimation anim = new FWOptionAnimation(3, selected_segment, toOption);
		final ImageView iv = (ImageView) option_layout.findViewById(R.id.option_imageview);
		iv.setImageDrawable(null);
		iv.postDelayed(new Runnable () {
			public void run () {
				iv.setImageBitmap(ImageManager.getBitmapFromResources(resource));
			}
		}, anim.getInterval() * 150);
		option_layout.startAnimation(anim);
	}
	
	/**
	 * Callback for status segment control.
	 * @return
	 */
	private View.OnClickListener getStatusSegmentClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// animating option bar
				moveOption(STATUS_SEGMENT, R.drawable.friends_activity_status_icon);
				
				// refresh friend
				selected_segment = STATUS_SEGMENT;
				refreshFriends();
			}
		};
	}
	
	/**
	 * Callback for request segment control.
	 * @return
	 */
	private View.OnClickListener getRequestSegmentClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// animating option bar
				moveOption(REQUEST_SEGMENT, R.drawable.friends_activity_request_icon);
				
				// refresh friend
				selected_segment = REQUEST_SEGMENT;
				refreshFriends();
			}
		};
	}
	
	/**
	 * Callback for find segment control.
	 * @return
	 */
	private View.OnClickListener getFindSegmentClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// animating option bar
				moveOption(FIND_SEGMENT, R.drawable.friends_activity_find_icon);
				
				// refresh friend
				selected_segment = FIND_SEGMENT;
				refreshFriends();
			}
		};
	}
	
	/**
	 * Get status user of friend list adapter position.
	 * @param position
	 * @return TVUser
	 */
	private TVUser getStatusUser (int position) {
		ArrayList<TVUser> onlines = friend_list_adapter.getOnlines();
		ArrayList<TVUser> offlines = friend_list_adapter.getOfflines();
		
		if (friend_list_adapter == null || onlines == null || offlines == null ||
			friend_list_adapter.getStatusRowType(position) != FriendListAdapter.STATUS_ROW_TYPE_USER )
			return null;
		
		return position < friend_list_adapter.getStatusOfflinePosition()
			   ? onlines.get(position - 1)
			   : offlines.get(position - ((onlines.size() > 0 ? onlines.size() : 1) + 2));
	}
	
	/**
	 * Get request user of friend list adapter position.
	 * @param position
	 * @return
	 */
	private TVUser getRequestUser (int position) {
		if (friend_list_adapter == null ||
			friend_list_adapter.getRequests() == null)
			return null;
		
		return friend_list_adapter.getRequests().get(position);
	}

	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		TVUser user = null;
		switch (selected_segment) {
		
		case STATUS_SEGMENT:
			user = getStatusUser(position);
			break;
		case REQUEST_SEGMENT:
			user = getRequestUser(position);
			break;	
		case FIND_SEGMENT:
			break;	
		default:
			break;
		}
		
		if (user != null) {
			Intent i = new Intent(FriendsActivity.this, PersonalActivity.class);
			i.putExtra("username", user.getUsername());
			i.putExtra("nickname", user.getNickname());
			i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.friends_icon));
			startActivity(i);
		}
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == LOGIN_FACEBOOK_REQUEST)
			FacebookManager.getFacebook().authorizeCallback(requestCode, resultCode, data);
	}

	@Override
	public void onFacebookActiveRequest(FBPendingCommand command) {
		FacebookDialogListener listener = new FacebookDialogListener(command);
		if (FacebookManager.login(this, LOGIN_FACEBOOK_REQUEST, listener))
			listener.onComplete(null);
	}
	
	/**
	 * Listener for listening Facebook dialog.
	 * @author Kencool
	 *
	 */
	private class FacebookDialogListener implements DialogListener {
		
		private FBPendingCommand command = null;
		
		public FacebookDialogListener (FBPendingCommand command) {
			this.command = command;
		}
		
		@Override
		public void onCancel() {
		}
		@Override
		public void onComplete(Bundle bundle) {
			FacebookManager.loadFacebookService();
			FacebookManager.setLinkProfile();
			
			if (command != null)
				command.execute();
		}
		@Override
		public void onError(DialogError error) {
			new AlertDialog.Builder(FriendsActivity.this)
						   .setTitle("Facebook Error")
						   .setMessage(error.getMessage())
						   .setNeutralButton(R.string.action_confirm, null)
						   .show();
		}
		@Override
		public void onFacebookError(FacebookError facebookError) {
		}
	}
}
