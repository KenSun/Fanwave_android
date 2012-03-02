package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.badge.BadgeManager;
import com.wildmind.fanwave.badge.BadgeProgramAdapter;
import com.wildmind.fanwave.badge.TVBadge;
import com.wildmind.fanwave.media.AvatarManager;
import com.wildmind.fanwave.media.BadgeImageManager;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.activity.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Intent input data "username" : String
 * 					 "badge"	: TVBadge
 * @author Kencool
 *
 */

public class BadgeProgramActivity extends BaseActivity {

	private ImageView 		badge_imageview;
	private Button			wear_button;
	private ListView		title_listview;
	private LinearLayout	loading_indicator;
	private TextView		descr_textview;
	
	private String username = "";
	private TVBadge badge = null;; 
	private ArrayList<String> title_list = null;
	private BadgeProgramAdapter program_adapter;
	
	private BadgeProgramImageListener image_listener = new BadgeProgramImageListener();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if activity is destroyed due to abnormal situation
        if (isDestroyed())
			return;
        
        setContentView(R.layout.badge_program_activity);
        
     	initData();
        initUI();
        refreshTitles();
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
		
		ImageManager.removeImageListener(image_listener);
		image_listener = null;
		
		if (program_adapter != null)
			program_adapter.clear();
		program_adapter = null;
		
		title_list = null;
		badge = null;
		
		badge_imageview = null;
		wear_button = null;
		title_listview = null;
		loading_indicator = null;
		descr_textview = null;
	}
	
	private void initData () {
		Intent i = getIntent();
		username = i.getStringExtra("username");
		badge = (TVBadge) i.getParcelableExtra("badge");
		
		ImageManager.addImageListener(image_listener);
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
		
		// Badge Image View
		badge_imageview = (ImageView) findViewById(R.id.badge_imageview);
		badge_imageview.setOnClickListener(getBadgeClickedListener());
		loadBadge();
		
		// Wear Button
		wear_button = (Button) findViewById(R.id.wear_button);
		wear_button.setVisibility(View.GONE);
		wear_button.setOnClickListener(getWearButtonClickedListener());
		
		// Title
		TextView title = (TextView) findViewById(R.id.title_textview);
		title.setText(badge.getTitle());
		
		// Description
		TextView description = (TextView) findViewById(R.id.description_textview);
		description.setText(badge.getDescription());
		
		// Title List View
		title_listview = (ListView) findViewById(R.id.program_list_listview);
		ImageView imv = new ImageView(this.getBaseContext());
		imv.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.list_divider));
		title_listview.addFooterView(imv);
		program_adapter = new BadgeProgramAdapter(null);
		title_listview.setAdapter(program_adapter);
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
		
		// Description Text View
		descr_textview = (TextView) findViewById(R.id.descr_textview);
	}
	
	/**
	 * Reload titles.
	 */
	private void refreshTitles () {
		showTitleLoading();
		title_list = null;
		getProgramTitles();
	}
	
	/**
	 * Show title list view.
	 */
	private void showTitleList () {
		title_listview.setVisibility(View.VISIBLE);
		loading_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show title loading view.
	 */
	private void showTitleLoading () {
		title_listview.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.VISIBLE);
		descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show Description.
	 */
	private void showDescription () {
		title_listview.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Show program title list.
	 */
	private void showProgramTitles () {
		if (title_list == null || title_list.size() == 0)
			showDescription();
		else {
			showTitleList();
			program_adapter.refreshData(title_list);
		}
	}
	
	/**
	 * Get program titles.
	 */
	private void getProgramTitles () {
		new Thread (new Runnable () {
			public void run () {
				ArrayList<String> array = BadgeManager.getProgramLevelTitles(badge.getLevel(), username);
				
				if (!isDestroyed()) {
					title_list = array;
					
					title_listview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							if (title_list.size() > 0 && 
								username.equals(AccountManager.getCurrentUser().getUsername()) && 
								!badge.getId().equals(AccountManager.getCurrentUser().getBadgeId()))
								wear_button.setVisibility(View.VISIBLE);
							
							showProgramTitles();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Set wearing badge.
	 */
	private void setWearingBadge () {
		new Thread ( new Runnable () {
			public void run () {
				final boolean success = BadgeManager.setSelectedBadge(username, badge.getId());
				
				if (wear_button != null && !success) {
					BadgeProgramActivity.this.runOnUiThread(new Runnable () {
						public void run () {
							wear_button.setVisibility(View.INVISIBLE);
							Toast.makeText( BadgeProgramActivity.this, R.string.badge_wear_failed, 
											Toast.LENGTH_SHORT ).show();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Callback for badge clicked.
	 * @return
	 */
	private View.OnClickListener getBadgeClickedListener () {
		return new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				if (title_list == null)
					return;
				
				final boolean wearable = title_list.size() > 0 && 
										 username.equals(AccountManager.getCurrentUser().getUsername()) && 
										 !badge.getId().equals(AccountManager.getCurrentUser().getBadgeId());
				
				if (BadgeImageManager.isBadgeImageExistInStorage(badge.getId())) {
					if (badge != null)
						showBadgePlayer(badge, null, wearable, false);
				} else {
					final ProgressDialog pd = ProgressDialog.show(BadgeProgramActivity.this, "",
							getResources().getString(R.string.action_loading));
					
					new Thread ( new Runnable () {
						public void run () {
							// prepare badge image
							BadgeImageManager.downloadBadgeImage(badge.getId());
							
							if (!isDestroyed()) {
								badge_imageview.post( new Runnable () {
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
		};
	}
	
	/**
	 * Callback for wear button clicked.
	 * @return
	 */
	private View.OnClickListener getWearButtonClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);
				setWearingBadge();
			}
		};
	}
	
	/**
	 * Image Process Methods
	 */
	
	/**
	 * Load badge.
	 */
	private void loadBadge () {
		if (badge == null || badge.getId().length() == 0)
			return;
		
		if (ImageManager.drawBadgeImage(badge_imageview, badge.getId(), true)) {
			ImageManager.removeImageListener(image_listener);
		}
	}
	
	/**
	 * Listener for listening image loading requests.
	 * @author Kencool
	 *
	 */
	private class BadgeProgramImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(String username, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void retrieveBadge(final String badge_id, final boolean scaled, final Bitmap bmp) {
			if (badge == null || !badge.getId().equals(badge_id) || !scaled)
				return;
			
			if (badge_imageview != null) {
				badge_imageview.post( new Runnable () {
					public void run () {
						if (badge_imageview == null)
							return;
						badge_imageview.setImageBitmap(bmp);
						ImageManager.removeImageListener(image_listener);
					}
				});
			}
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
		public void retrieveChannelIcon(String title, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
