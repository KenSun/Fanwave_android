package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.badge.BadgeManager;
import com.wildmind.fanwave.badge.BadgeTypeAdapter;
import com.wildmind.fanwave.badge.TVBadge;
import com.wildmind.fanwave.media.AvatarManager;
import com.wildmind.fanwave.media.BadgeImageManager;
import com.wildmind.fanwave.activity.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Intent input data "username" : String
 * 					 "type"		: String
 * @author Kencool
 *
 */

public class BadgeTypeActivity extends BaseActivity implements OnItemClickListener {

	private ListView 		badge_listview;
	private LinearLayout 	loading_indicator;
	
	private String username = "", badge_type = "";
	private ArrayList<TVBadge> badge_list = null;
	private BadgeTypeAdapter type_adapter;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if activity is destroyed due to abnormal situation
        if (isDestroyed())
			return;
        
        setContentView(R.layout.badge_type_activity);
        
     	initData();
        initUI();
        refreshBadges();
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
		
		if (type_adapter != null)
			type_adapter.clear();
		type_adapter = null;
		
		badge_list = null;
		
		badge_listview = null;
		loading_indicator = null;
	}
	
	private void initData () {
		Intent i = getIntent();
		username = i.getStringExtra("username");
		badge_type = i.getStringExtra("type");
		
		badge_list = new ArrayList<TVBadge>();
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
		AvatarManager.drawAvatar(back_button, username, true);
		
		// Badge Type List View
		badge_listview = (ListView) findViewById(R.id.type_list_listview);
		badge_listview.setDivider(null);
		badge_listview.setDividerHeight(0);
		badge_listview.setOnItemClickListener(this);
		type_adapter = new BadgeTypeAdapter(badge_listview, null, 
						AccountManager.getCurrentUser().getUsername().equals(username), this);
		badge_listview.setAdapter(type_adapter);
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
	}
	
	/**
	 * Reload badges.
	 */
	private void refreshBadges () {
		showBadgeLoading();
		badge_list.clear();
		getTypeBadges();
	}
	
	/**
	 * Show badge list view.
	 */
	private void showBadgeList () {
		badge_listview.setVisibility(View.VISIBLE);
		loading_indicator.setVisibility(View.GONE);
	}
	
	/**
	 * Show badge loading view.
	 */
	private void showBadgeLoading () {
		badge_listview.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Show type badges.
	 */
	private void showTypeBadges () {
		showBadgeList();
		type_adapter.refershData(badge_list);
	}
	
	/**
	 * Get badges.
	 */
	private void getTypeBadges () {
		new Thread ( new Runnable () {
			public void run () {
				ArrayList<TVBadge> array = BadgeManager.getTypeDetailBadges(badge_type, username);
				
				if (!isDestroyed()) {
					if (!AccountManager.getCurrentUser().getUsername().equals(username)) {
						for (TVBadge badge:array) {
							if (badge.getProgress() == 1.0)
								badge_list.add(badge);
						}
					} else
						badge_list = array;
					
					badge_listview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							showTypeBadges();
						}
					});
				}
			}
		}).start();
	}
	
	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		final TVBadge badge = (TVBadge) type_adapter.getItem(position);
		final boolean wearable = username.equals(AccountManager.getCurrentUser().getUsername()) &&
								 !badge.getId().equals(AccountManager.getCurrentUser().getBadgeId());
		
		if (BadgeImageManager.isBadgeImageExistInStorage(badge.getId())) {
			if (badge != null)
				showBadgePlayer(badge, null, wearable, false);
		} else {
			final ProgressDialog pd = ProgressDialog.show(BadgeTypeActivity.this, "", 
					ApplicationManager.getAppContext().getResources().getString(R.string.action_loading));
			
			new Thread ( new Runnable () {
				public void run () {
					// prepare badge image
					BadgeImageManager.downloadBadgeImage(badge.getId());
					
					if (!isDestroyed()) {
						badge_listview.post( new Runnable () {
							public void run () {
								if (isDestroyed())
									return;
								
								pd.dismiss();
								showBadgePlayer(badge, null, wearable, false);
							}
						});
					}	
				}
			}).start();
		}
	}
}
