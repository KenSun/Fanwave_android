package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import com.wildmind.fanwave.facebook.FBFriendsAdapter;
import com.wildmind.fanwave.facebook.FBUser;
import com.wildmind.fanwave.facebook.FacebookManager;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.user.UserManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsFacebookActivity extends BaseActivity {
	
	private ListView		friend_listview;
	private LinearLayout	loading_indicator;
	private TextView		descr_textview;
	
	private FBFriendsAdapter friend_adapter = null;
	
	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if activity is destroyed due to abnormal situation
        if (isDestroyed())
			return;
        
        setContentView(R.layout.friends_facebook_activity);
        
        initUI();
        refreshFriends();
    }
	
	protected void onStart() {
		super.onStart();
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onStop() {
		super.onStop();
	}

	protected void onDestroy() {
		super.onDestroy();
		
		friend_listview = null;
		loading_indicator = null;
		descr_textview = null;
		
		if (friend_adapter != null)
			friend_adapter.clear();
		friend_adapter = null;
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
		
		// Friend List Adapter
		friend_listview = (ListView) findViewById(R.id.friend_list_listview);
		friend_listview.setDivider(null);
		friend_listview.setDividerHeight(0);
		friend_adapter = new FBFriendsAdapter(friend_listview, null, null, this);
		friend_listview.setAdapter(friend_adapter);
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
		
		// Description Text View
		descr_textview = (TextView) findViewById(R.id.descr_textview);
	}
	
	/**
	 * Refresh friends.
	 */
	private void refreshFriends () {
		showLoading();
		getFriends();
	}
	
	/**
	 * Show friend list.
	 */
	private void showFriendList () {
		friend_listview.setVisibility(View.VISIBLE);
		loading_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show loading view.
	 */
	private void showLoading () {
		friend_listview.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.VISIBLE);
		descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show Description.
	 */
	private void showDescription () {
		friend_listview.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Show friends.
	 * @param app
	 * @param nonApp
	 */
	private void showFriends (ArrayList<TVUser> app, ArrayList<FBUser> nonApp) {
		if (app.size() + nonApp.size() == 0)
			showDescription();
		else {
			showFriendList();
			friend_adapter.refreshData(app, nonApp);
		}
	}
	
	/**
	 * Get friends.
	 */
	private void getFriends () {
		new Thread (new Runnable () {
			public void run () {
				ArrayList<FBUser> app = FacebookManager.getAppFriends();
				final ArrayList<TVUser> appUsers = UserManager.getUsersByFacebookIds(app);
				final ArrayList<FBUser> nonApp = FacebookManager.getNonAppFriends();
				
				if (!isDestroyed()) {
					friend_listview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							showFriends(appUsers, nonApp);
						}
					});
				}
			}
		}).start();
	}
}
