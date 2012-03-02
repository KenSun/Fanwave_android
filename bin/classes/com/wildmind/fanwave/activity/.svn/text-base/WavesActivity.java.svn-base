package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.animation.FWOptionAnimation;
import com.wildmind.fanwave.feed.Feed;
import com.wildmind.fanwave.feed.FeedListAdapter;
import com.wildmind.fanwave.feed.FeedManager;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.activity.R;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class WavesActivity extends BaseActivity implements OnItemClickListener {

	private final int PUBLIC_SEGMENT = 0;
	private final int FRIEND_SEGMENT = 1;
	private final int FOLLOW_SEGMENT = 2;
	
	// UI variables
	//
	private FrameLayout 	option_layout;
	private TextView 		public_wave_textview, friend_wave_textview, follow_wave_textview ,descr_textview;
	private LinearLayout 	loading_indicator;
	private ListView 		wave_listview;
	private FeedListAdapter wave_list_adapter;
	
	// structure variables
	//
	private ArrayList<Feed> public_feed_list = null;
	private ArrayList<Feed> friend_feed_list = null;
	private ArrayList<Feed> follow_feed_list = null;
	private boolean more_public_feed = false;
	private boolean more_friend_feed = false;
	private boolean more_follow_feed = false;
	private boolean public_feed_loading_more = false;
	private boolean friend_feed_loading_more = false;
	private boolean follow_feed_loading_more = false;
	private int public_feed_position = 0;
	private int friend_feed_position = 0;
	private int follow_feed_position = 0;
	private int selected_feed = PUBLIC_SEGMENT;
	
	/**
	 *  Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.waves_activity);
		
		initData();
		initUI();
		refreshFeeds();
	}

	protected void onStart() {
		super.onStart();
		// The activity is about to become visible.
	}

	protected void onResume() {
		super.onResume();
		// The activity has become visible (it is now "resumed").
	}

	protected void onPause() {
		super.onPause();
		// Another activity is taking focus (this activity is about to be "paused").
	}

	protected void onStop() {
		super.onStop();
		// The activity is no longer visible (it is now "stopped")
	}

	protected void onDestroy() {
		super.onDestroy();
		// The activity is about to be destroyed.
		
		public_feed_list = null;
		friend_feed_list = null;
		follow_feed_list = null;
		
		wave_listview = null;
		option_layout = null;
		public_wave_textview = null;
		friend_wave_textview = null;
		follow_wave_textview = null;
		loading_indicator = null;
		descr_textview = null;
		
		if (wave_list_adapter != null)
			wave_list_adapter.clear();
		wave_list_adapter = null;
	}

	// Initialization Methods
	//
	private void initData() {
		
	}

	private void initUI() {
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
		
		// Public Wave Segment Control
		public_wave_textview = (TextView) findViewById(R.id.wave_all_textview);
		public_wave_textview.setOnClickListener(getPublicSegmentClickedListener());
		
		// Friend Wave Segment Control
		friend_wave_textview = (TextView) findViewById(R.id.wave_friends_textview);
		friend_wave_textview.setOnClickListener(getFriendSegmentClickedListener());
		
		// Follow Wave Segment Control
		follow_wave_textview = (TextView) findViewById(R.id.wave_follows_textview);
		follow_wave_textview.setOnClickListener(getFollowSegmentClickedListener());
				
		// Wave List View
		wave_listview = (ListView) findViewById(R.id.wave_list_listview);
		wave_listview.setDivider(null);
		wave_listview.setDividerHeight(0);
		wave_listview.setOnItemClickListener(this);
		wave_listview.setOnScrollListener(getFeedListScrollListener());
		wave_list_adapter = new FeedListAdapter(wave_listview, null, this);
		wave_listview.setAdapter(wave_list_adapter);
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
        
		// Description Text View
		descr_textview = (TextView) findViewById(R.id.descr_textview);
	}
	
	/**
	 * Reload feeds.
	 */
	public void refreshFeeds () {
		showWaveLoading();
		
		more_public_feed = false;
		more_friend_feed = false;
		more_follow_feed = false;
		public_feed_loading_more = false;
		friend_feed_loading_more = false;
		follow_feed_loading_more = false;
		public_feed_list = null;
		friend_feed_list = null;
		follow_feed_list = null;
		public_feed_position = 0;
		friend_feed_position = 0;
		follow_feed_position = 0;
		FeedManager.clear();
		
		getPublicFeed();
		getFriendFeed();
		getFollowFeed();
	}
	
	/**
	 * Show wave list view.
	 */
	private void showWaveList () {
		wave_listview.setVisibility(View.VISIBLE);
		loading_indicator.setVisibility(View.GONE);
        descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show wave loading view.
	 */
	private void showWaveLoading () {
		wave_listview.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.VISIBLE);
        descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show Description.
	 */
	private void showDescription (String descr) {
		wave_listview.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.VISIBLE);
		
		descr_textview.setText(descr);
	}
	
	/**
	 * Show public feed.
	 * @param reposition whether reposition to last position
	 */
	private void showPublicFeed (boolean reposition) {
        if (public_feed_list == null || public_feed_list.size() == 0)
            showDescription(getString(R.string.waves_no_result_all));
        else{
            showWaveList();
            wave_list_adapter.refreshData(public_feed_list, more_public_feed);
            if (reposition)
                wave_listview.setSelection(public_feed_position);   
        }
	}
	
	/**
	 * Show friend feed.
	 * @param reposition whether reposition to last position
	 */
	private void showFriendFeed (boolean reposition) {
        if(friend_feed_list==null || friend_feed_list.size()==0){
            showDescription(getString(R.string.waves_no_result_friends));
        }else{
            showWaveList();
            wave_list_adapter.refreshData(friend_feed_list, more_friend_feed);
            if (reposition)
                wave_listview.setSelection(friend_feed_position); 
        }
	}
	
	/**
	 * Show follow feed.
	 * @param reposition whether reposition to last position
	 */
	private void showFollowFeed (boolean reposition) {
        if(follow_feed_list==null || follow_feed_list.size()==0){
            showDescription(getString(R.string.waves_no_result_follow));
        }else{
            showWaveList();
            wave_list_adapter.refreshData(follow_feed_list, more_follow_feed);
            if (reposition)
                wave_listview.setSelection(follow_feed_position);
        }
	}
	
	/**
	 * Get latest public feeds in background and refresh wave list view on UI thread.
	 */
	private void getPublicFeed () {
		new Thread ( new Runnable () {
			public void run () {
				final ArrayList<Feed> array = FeedManager.getPublicFeed(null);
				
				if (!isDestroyed()) {
					WavesActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							public_feed_list = array;
							more_public_feed = FeedManager.isMorePublic();
							if (selected_feed == PUBLIC_SEGMENT) 
								showPublicFeed(true);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get previous public feed with feed_id in background and refresh wave list view on UI thread.
	 * @param feed_id
	 */
	private void getMorePublicFeed (final String feed_id) {
		// can't asking for more feeds while already being
		if (public_feed_loading_more)
			return;
		public_feed_loading_more = true;
		
		new Thread ( new Runnable () {
			public void run () {
				final ArrayList<Feed> array = FeedManager.getPublicFeed(feed_id);
				
				if (!isDestroyed()) {
					WavesActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							// check if public_feed_loading becomes false while downloading feeds, which means 
							// the request for feeds is no more required now, then we don't need to refresh feed 
							// list with these un-demand data
							if (!public_feed_loading_more)
								return;
							
							more_public_feed = FeedManager.isMorePublic();
							public_feed_list.addAll(array);
							
							if (selected_feed == PUBLIC_SEGMENT) 
								showPublicFeed(false);
							
							public_feed_loading_more = false;
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get latest friend feeds in background and refresh wave list view on UI thread.
	 */
	private void getFriendFeed () {		
		new Thread ( new Runnable () {
			public void run () {
				final ArrayList<Feed> array = FeedManager.getFriendFeed(null);
				
				if (!isDestroyed()) {
					WavesActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							friend_feed_list = array;
							more_friend_feed = FeedManager.isMoreFriend();
							if (selected_feed == FRIEND_SEGMENT) 
								showFriendFeed(true);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get more friend feeds with feed_id in background and refresh wave list view on UI thread.
	 * @param feed_id
	 */
	private void getMoreFriendFeed (final String feed_id) {
		// can't asking for feeds while already being
		if (friend_feed_loading_more)
			return;
		friend_feed_loading_more = true;
		
		new Thread ( new Runnable () {
			public void run () {
				final ArrayList<Feed> array = FeedManager.getFriendFeed(feed_id);
				
				if (!isDestroyed()) {
					WavesActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							// check if public_feed_loading becomes false while downloading feeds, which means the
							// request for feeds is no more required now, then we don't need to refresh feed list
							// with these un-demand data
							if (!friend_feed_loading_more)
								return;
							friend_feed_loading_more = false;
							
							more_friend_feed = FeedManager.isMoreFriend();
							friend_feed_list.addAll(array);
							if (selected_feed == FRIEND_SEGMENT) 
								showFriendFeed(false);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get latest follow feeds in background and refresh wave list view on UI thread.
	 */
	private void getFollowFeed () {
		follow_feed_loading_more = false;
		
		new Thread ( new Runnable () {
			public void run () {
				final ArrayList<Feed> array = FeedManager.getFollowFeed(null, AccountManager.getCurrentUser().getUsername());
				
				if (!isDestroyed()) {
					WavesActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							follow_feed_list = array;
							more_follow_feed = FeedManager.isMoreFollow();
							if (selected_feed == FOLLOW_SEGMENT)
								showFollowFeed(true);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get follow feed in background and refresh wave list view on UI thread. If feed_id is null, get latest feeds.
	 * Else, get previous feeds from feed_id.
	 * @param feed_id
	 */
	private void getMoreFollowFeed (final String feed_id) {
		// can't asking for feeds while already being
		if (follow_feed_loading_more)
			return;
		follow_feed_loading_more = true;
		
		new Thread ( new Runnable () {
			public void run () {
				final ArrayList<Feed> array = FeedManager.getFollowFeed(feed_id, AccountManager.getCurrentUser().getUsername());
				
				if (!isDestroyed()) {		
					WavesActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							// check if public_feed_loading becomes false while downloading feeds, which means the
							// request for feeds is no more required now, then we don't need to refresh feed list 
							// with these un-demand data
							if (!follow_feed_loading_more)
								return;
							follow_feed_loading_more = false;
							
							more_follow_feed = FeedManager.isMoreFollow();
							follow_feed_list.addAll(array);
							if (selected_feed == FOLLOW_SEGMENT)
								showFollowFeed(false);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get callback for public segment control.
	 * @return
	 */
	private View.OnClickListener getPublicSegmentClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// record feed position
				if (selected_feed == PUBLIC_SEGMENT)
					return;
				else if (selected_feed == FRIEND_SEGMENT)
					friend_feed_position = wave_listview.getFirstVisiblePosition();
				else if (selected_feed == FOLLOW_SEGMENT)
					follow_feed_position = wave_listview.getFirstVisiblePosition();
				
				// animating option bar
				FWOptionAnimation anim = new FWOptionAnimation(3, selected_feed, PUBLIC_SEGMENT);
				final ImageView iv = (ImageView) option_layout.findViewById(R.id.option_imageview);
				iv.setImageDrawable(null);
				iv.postDelayed(new Runnable () {
					public void run () {
						iv.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.waves_activity_all_icon));
					}
				}, anim.getInterval() * 150);
				option_layout.startAnimation(anim);
				
				// refresh feed 
				selected_feed = PUBLIC_SEGMENT;
				if (public_feed_list != null)
					showPublicFeed(true);
				else
					showWaveLoading();
			}
		};
	}

	/**
	 * Get callback for friend segment control.
	 * @return
	 */
	private View.OnClickListener getFriendSegmentClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// record feed position
				if (selected_feed == FRIEND_SEGMENT)
					return;
				else if (selected_feed == PUBLIC_SEGMENT)
					public_feed_position = wave_listview.getFirstVisiblePosition();
				else if (selected_feed == FOLLOW_SEGMENT)
					follow_feed_position = wave_listview.getFirstVisiblePosition();
				
				// animating option bar
				FWOptionAnimation anim = new FWOptionAnimation(3, selected_feed, FRIEND_SEGMENT);
				final ImageView iv = (ImageView) option_layout.findViewById(R.id.option_imageview);
				iv.setImageDrawable(null);
				iv.postDelayed(new Runnable () {
					public void run () {
						iv.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.waves_activity_friend_icon));
					}
				}, anim.getInterval() * 150);
				option_layout.startAnimation(anim);
				
				// refresh feed 
				selected_feed = FRIEND_SEGMENT;
				if (friend_feed_list != null)
					showFriendFeed(true);
				else
					showWaveLoading();
			}
		};
	}
	
	/**
	 * Get callback for follow segment control.
	 * @return
	 */
	private View.OnClickListener getFollowSegmentClickedListener () {
		return new View.OnClickListener() {
			//@Override
			public void onClick(View v) {
				// record feed position
				if (selected_feed == FOLLOW_SEGMENT)
					return;
				else if (selected_feed == PUBLIC_SEGMENT)
					public_feed_position = wave_listview.getFirstVisiblePosition();
				else if (selected_feed == FRIEND_SEGMENT)
					friend_feed_position = wave_listview.getFirstVisiblePosition();
				
				// animating option bar
				FWOptionAnimation anim = new FWOptionAnimation(3, selected_feed, FOLLOW_SEGMENT);
				final ImageView iv = (ImageView) option_layout.findViewById(R.id.option_imageview);
				iv.setImageDrawable(null);
				iv.postDelayed(new Runnable () {
					public void run () {
						iv.setImageBitmap(ImageManager.getBitmapFromResources(R.drawable.waves_activity_follow_icon));
					}
				}, anim.getInterval() * 150);
				option_layout.startAnimation(anim);
				
				// refresh feed 
				selected_feed = FOLLOW_SEGMENT;
				if (follow_feed_list != null)
					showFollowFeed(true);
				else
					showWaveLoading();
			}
		};
	}
	
	/**
	 * Callback for feed list scrolled.
	 * @return
	 */
	private AbsListView.OnScrollListener getFeedListScrollListener () {
		return new AbsListView.OnScrollListener() {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount > totalItemCount - 3) {
					if (selected_feed == PUBLIC_SEGMENT && more_public_feed && !public_feed_loading_more)
						getMorePublicFeed(public_feed_list.get(public_feed_list.size()-1).getId());
					else if (selected_feed == FRIEND_SEGMENT && more_friend_feed && !friend_feed_loading_more)
						getMoreFriendFeed(friend_feed_list.get(friend_feed_list.size()-1).getId());
					else if (selected_feed == FOLLOW_SEGMENT && more_follow_feed && !follow_feed_loading_more)
						getMoreFollowFeed(follow_feed_list.get(follow_feed_list.size()-1).getId());
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
		};
	}
	
	/**
	 *  Callback for wave list view clicked.
	 */
	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		
		switch (selected_feed) {
		
		case PUBLIC_SEGMENT:
			if (position < public_feed_list.size())
				openProgramActivity(public_feed_list.get(position).getProgram());
			break;
		case FRIEND_SEGMENT:
			if (position < friend_feed_list.size())
				openProgramActivity(friend_feed_list.get(position).getProgram());
			break;
		case FOLLOW_SEGMENT:
			if (position < follow_feed_list.size())
				openProgramActivity(follow_feed_list.get(position).getProgram());
			break;
		}
	}
	
	/**
	 * Open program activity with TVProgram tp.
	 * @param tp
	 */
	public void openProgramActivity (TVProgram tp) {
		if (tp.getTitle().length() == 0)
			return;
		
		Intent i = new Intent(WavesActivity.this, ProgramActivity.class);
		i.putExtra("program", tp);
		i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.waves_icon));
		startActivity(i);
	}
	
	/**
	 * Menu Control Methods
	 */
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.wave_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		refreshFeeds();
		return true;
	}
}
