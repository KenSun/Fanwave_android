package com.wildmind.fanwave.activity;

import java.util.ArrayList;
import java.util.Collections;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.animation.AlphaSetter;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.badge.BadgeListAdapter;
import com.wildmind.fanwave.badge.BadgeManager;
import com.wildmind.fanwave.badge.TVBadge;
import com.wildmind.fanwave.feed.Feed;
import com.wildmind.fanwave.feed.FeedListAdapter;
import com.wildmind.fanwave.feed.FeedManager;
import com.wildmind.fanwave.follow.FollowListAdapter;
import com.wildmind.fanwave.follow.FollowManager;
import com.wildmind.fanwave.follow.TVFollow;
import com.wildmind.fanwave.friend.FriendManager;
import com.wildmind.fanwave.friend.PersonalFriendAdapter;
import com.wildmind.fanwave.media.AvatarManager;
import com.wildmind.fanwave.media.BadgeImageManager;
import com.wildmind.fanwave.media.ImageListener;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.reminder.ReminderListAdapter;
import com.wildmind.fanwave.reminder.ReminderManager;
import com.wildmind.fanwave.reminder.TVReminder;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.user.TVUserPrivacy;
import com.wildmind.fanwave.user.UserManager;
import com.wildmind.fanwave.util.StringGenerator;
import com.wildmind.fanwave.activity.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * Intent input data "username" 	: String
 * 					 "nickname" 	: String
 * 					 "back_image" 	: Bitmap
 * @author Kencool
 *
 */

public class PersonalActivity extends BaseActivity implements OnItemClickListener {

	/**
	 * Tab bar identifies.
	 */
	private final int WAVE_TAB_ID 		= R.id.wave_tab_button;
	private final int BADGE_TAB_ID 		= R.id.badge_tab_button;
	private final int FRIEND_TAB_ID 	= R.id.friend_tab_button;
	private final int FAVORITE_TAB_ID 	= R.id.favorite_tab_button;
	private final int REMINDER_TAB_ID 	= R.id.reminder_tab_button;
	
	private ImageView 		avatar_imageview, badge_imageview;
	private LinearLayout 	friend_layout, splash_layout;
	private TextView 		mood_textview;
	private ImageButton 	wave_imagebutton, badge_imagebutton, friend_imagebutton, 
							favorite_imagebutton, reminder_imagebutton;
	private ListView 		personal_listview;
	private LinearLayout 	loading_indicator;
	private TextView		descr_textview;
	
	private TVUser 	user = null;
	private String 	username = "", nickname = "";
	private String	temp_mood = "";
	private int 	selected_tab = WAVE_TAB_ID;
	private boolean is_self = false;
	private boolean avatar_loaded = false, badge_loaded = false;
	
	/**
	 * List adapters.
	 */
	private FeedListAdapter 		feed_adapter;
	private PersonalFriendAdapter 	friend_adapter;
	private BadgeListAdapter		badge_adapter;
	private FollowListAdapter		favorite_adapter;
	private ReminderListAdapter		reminder_adapter;
	
	/**
	 * List data.
	 */
	private ArrayList<Feed>			feed_list = null;
	private ArrayList<TVUser>		friend_list = null;
	private BadgeManager			badge_manager = null;
	
	private ArrayList<TVFollow> 	favorite_list = null;
	private ArrayList<TVFollow> 	common_favorite_list = null;
	private ArrayList<TVFollow> 	uncommon_favorite_list = null;
	
	private ArrayList<TVReminder> 	reminder_list = null;
	private ArrayList<TVReminder> 	common_reminder_list = null;
	private ArrayList<TVReminder> 	uncommon_reminder_list = null;
	
	/**
	 * List info.
	 */
	private int feed_position = 0;
	private int badge_position = 0;
	private int friend_position = 0;
	private int favorite_position = 0;
	private int reminder_position = 0;
	private boolean feed_load = false, more_feed = false, feed_loading_more = false;
	private boolean badge_load = false;
	private boolean friend_load = false;
	private boolean favorite_load = false;
	private boolean reminder_load = false;
	
	private PersonalImageListener image_listener = new PersonalImageListener();
	private AvatarReceiver avatar_receiver = new AvatarReceiver();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if activity is destroyed due to abnormal situation
        if (isDestroyed())
			return;
        
        setContentView(R.layout.personal_activity);
        
     	initData();
        initUI();
        selectTabItem(selected_tab);
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
		
		if (ApplicationManager.getAppContext() != null)
			ApplicationManager.getAppContext().unregisterReceiver(avatar_receiver);
		avatar_receiver = null;
		
		if (feed_adapter != null)
			feed_adapter.clear();
		feed_adapter = null;
		if (friend_adapter != null)
			friend_adapter.clear();
		friend_adapter = null;
		if (badge_adapter != null)
			badge_adapter.clear();
		badge_adapter = null;
		if (favorite_adapter != null)
			favorite_adapter.clear();
		favorite_adapter = null;
		if (reminder_adapter != null)
			reminder_adapter.clear();
		reminder_adapter = null;
		
		feed_list = null;
		friend_list = null;
		badge_manager = null;
		favorite_list = null;
		common_favorite_list = null;
		uncommon_favorite_list = null;
		user = null;
		
		avatar_imageview = null;
		badge_imageview = null;
		friend_layout = null;
		splash_layout = null;
		mood_textview = null;
		wave_imagebutton = null;
		badge_imagebutton = null;
		friend_imagebutton = null;
		favorite_imagebutton = null;
		reminder_imagebutton = null;
		personal_listview = null;
		loading_indicator = null;
		descr_textview = null;
	}
	
	/**
	 * Init data.
	 */
	private void initData () {
		Intent i = getIntent();
		username = i.getStringExtra("username");
		nickname = i.getStringExtra("nickname");
		
		is_self = AccountManager.getCurrentUser().getUsername().equals(username);
		if (is_self)
			user = AccountManager.getCurrentUser();

		getUserProfile();
		ImageManager.addImageListener(image_listener);
		
		// init receiver
		initReceiver();
	}
	
	/**
	 * Init receiver.
	 */
	private void initReceiver () {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AvatarManager.BROADCAST_REFRESH_AVATAR);
		
		ApplicationManager.getAppContext().registerReceiver(avatar_receiver, intentFilter);
	}
	
	/**
	 * Init UI.
	 */
	private void initUI () {
		initTitleBarUI();
		initImageUI();
		
		// Social Layout
		if (is_self) 
			initSelfSocialUI();
		else
			initOtherSocialUI();

		initTabbarUI();
		initPersonalListUI();
	}
	
	private void initTitleBarUI () {
		// Back Button
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// Back Image
		Bitmap bmp = getIntent().getParcelableExtra("back_image");
		if (bmp != null)
			back_button.setImageBitmap(bmp);
		
		
		// Title
		TextView title = (TextView) findViewById(R.id.title_textview);
		title.setText(nickname);
		
		// Next Button
		ImageButton next_button = (ImageButton) findViewById(R.id.next_imagebutton);
		next_button.setOnClickListener(getExtraInfoClickedListener());
	}
	
	private void initImageUI () {
		avatar_imageview = (ImageView) findViewById(R.id.avatar_imageview);
		badge_imageview = (ImageView) findViewById(R.id.badge_imageview);
		badge_imageview.setOnClickListener(getBadgeClickedListener());
		
		// if is self, username and selected badge id is already prepared
		if (is_self) {
			loadAvatar();
			loadBadge();
		}
	}
	
	private void initSelfSocialUI () {
		// hide other social layout
		findViewById(R.id.other_social_layout).setVisibility(View.GONE);
		
		// Social Text View
		mood_textview = (TextView) findViewById(R.id.self_social_textview);
		mood_textview.setText(user.getExtraInfo().getMood());
		mood_textview.setOnClickListener(getSocialTextViewClickedListener());
	}
	
	private void initOtherSocialUI () {
		// hide self social text view
		findViewById(R.id.self_social_textview).setVisibility(View.GONE);
		
		// Social Action Layout
		AlphaSetter alpha = new AlphaSetter(0.5f);	
		View.OnClickListener action_listener = getSocialActionClickedListener();
		splash_layout = (LinearLayout) findViewById(R.id.splash_social_layout);
		splash_layout.setOnClickListener(action_listener);
		splash_layout.startAnimation(alpha);
		splash_layout.setEnabled(false);
		friend_layout = (LinearLayout) findViewById(R.id.friend_action_layout);
		friend_layout.setOnClickListener(action_listener);
		friend_layout.startAnimation(alpha);
		friend_layout.setEnabled(false);
		
		// Social Text View
		mood_textview = (TextView) findViewById(R.id.other_social_textview);
		mood_textview.setText("");
	}
	
	private void initTabbarUI () {
		View.OnClickListener tab_listener = getTabClickedListener();
		
		wave_imagebutton = (ImageButton) findViewById(R.id.wave_tab_button);
		wave_imagebutton.setOnClickListener(tab_listener);
		
		badge_imagebutton = (ImageButton) findViewById(R.id.badge_tab_button);
		badge_imagebutton.setOnClickListener(tab_listener);
		
		friend_imagebutton = (ImageButton) findViewById(R.id.friend_tab_button);
		friend_imagebutton.setOnClickListener(tab_listener);
		
		favorite_imagebutton = (ImageButton) findViewById(R.id.favorite_tab_button);
		favorite_imagebutton.setOnClickListener(tab_listener);
		
		reminder_imagebutton = (ImageButton) findViewById(R.id.reminder_tab_button);
		reminder_imagebutton.setOnClickListener(tab_listener);	
	}
	
	private void initPersonalListUI () {
		// Personal List View
		personal_listview = (ListView) findViewById(R.id.personal_list_listview);
		personal_listview.setDivider(null);
		personal_listview.setDividerHeight(0);
		personal_listview.setOnItemClickListener(this);
		personal_listview.setOnScrollListener(getScrollListener());
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
		
		// Description Text View
		descr_textview = (TextView) findViewById(R.id.descr_textview);
	}
	
	/**
	 * Refresh profile with user.
	 */
	private void refreshProfile () {
		// Avatar, Badge
		loadAvatar();
		loadBadge();
		
		// Social Action Layout
		refreshSplashActionUI();
		refreshFriendActionUI();
		
		// Social Text View
		mood_textview.setText(user.getExtraInfo().getMood());
	}
	
	/**
	 * Refresh splash action layout.
	 */
	private void refreshSplashActionUI () {
		if (user == null)
			return;
		
		splash_layout.clearAnimation();
		if (is_self)
			splash_layout.setVisibility(View.INVISIBLE);
		else {
			splash_layout.startAnimation(new AlphaSetter(1));
			splash_layout.setEnabled(true);
		}
	}
	
	/**
	 * Refresh friend action layout.
	 */
	private void refreshFriendActionUI () {
		if (user == null)
			return;
		
		friend_layout.clearAnimation();
		TextView action = (TextView) friend_layout.findViewById(R.id.friend_social_action_textview);
		
		if ( is_self || FriendManager.isFriend(username) ) {
			friend_layout.setVisibility(View.GONE);
		}
		else {
			friend_layout.startAnimation(new AlphaSetter(1));
			friend_layout.setEnabled(true);
			friend_layout.clearAnimation();
			
			if (FriendManager.isRequest(username)) {
				friend_layout.findViewById(R.id.friend_social_add_imageview).setVisibility(View.GONE);
				action.setText(R.string.personal_action_accept);
				action.setTextColor(getResources().getColor(R.drawable.color_orange_white_selector));
			} else {
				action.setText(R.string.personal_action_invite);
			}
		}
	}
	
	/**
	 * Reload waves.
	 */
	private void refreshWaves () {
		showPersonalLoading();
		
		feed_load = false;
		more_feed = false;
		feed_loading_more = false;
		feed_list = null;
		feed_position = 0;
		
		getWaves();
	}
	
	/**
	 * Reload badges.
	 */
	private void refreshBadges () {
		showPersonalLoading();
		
		badge_load = false;
		badge_manager = null;
		badge_position = 0;
		
		getBadges();
	}
	
	/**
	 * Reload friends.
	 */
	private void refreshFriends () {
		showPersonalLoading();
		
		friend_load = false;
		friend_list = null;
		friend_position = 0;
		
		getFriends();
	}
	
	/**
	 * Reload favorites.
	 */
	private void refreshFavorites () {
		showPersonalLoading();
		
		favorite_load = false;
		favorite_list = null;
		common_favorite_list = null;
		uncommon_favorite_list = null;
		favorite_position = 0;
		
		if(is_self)
			getSelfFavorites();
		else 
			getOtherFavorites();
	}
	
	/**
	 * Reload reminders.
	 */
	private void refreshReminders () {
		showPersonalLoading();
		
		reminder_load = false;
		reminder_list = null;
		common_reminder_list = null;
		uncommon_reminder_list = null;
		reminder_position = 0;
		
		if (is_self)
			getSelfReminders();
		else
			getOtherReminders();
	}
	
	private void showPersonalList () {
		personal_listview.setVisibility(View.VISIBLE);
		loading_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.GONE);
	}
	
	private void showPersonalLoading () {
		personal_listview.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.VISIBLE);
		descr_textview.setVisibility(View.GONE);
	}
	
	private void showDescription (String descr) {
		personal_listview.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.VISIBLE);
		
		descr_textview.setText(descr);
	}
	
	/**
	 * Show wave list.
	 * @param reposition whether reposition to last position
	 */
	private void showWaveList (boolean reposition) {
		if (feed_list == null || feed_list.size() == 0) {
			if(is_self) showDescription(getString(R.string.personal_no_result_self_waves));
			else showDescription(getString(R.string.personal_no_result_waves));
		} else {
			showPersonalList();

			feed_adapter = new FeedListAdapter(personal_listview, feed_list, this);
			feed_adapter.setAvatarClickable(false);
			feed_adapter.setMore(more_feed);
			personal_listview.setAdapter(feed_adapter);
			
			if (reposition)
				personal_listview.setSelection(feed_position);
		}
	}
	
	/**
	 * Show badge list.
	 * @param reposition whether reposition to last position
	 */
	private void showBadgeList (boolean reposition) {
		int count = badge_manager.getEventBadges().size() + badge_manager.getSystemBadges().size() +
					badge_manager.getProgramBadges().size();
		if (badge_manager == null || count == 0) {
			showDescription(getString(R.string.personal_no_result_badges));
		} else {
			showPersonalList();

			badge_adapter = new BadgeListAdapter(personal_listview, badge_manager, this);
			personal_listview.setAdapter(badge_adapter);
			
			if (reposition)
				personal_listview.setSelection(badge_position);
		}
	}
	
	/**
	 * Show friend list.
	 * @param reposition whether reposition to last position
	 */
	private void showFriendList (boolean reposition) {
		if (friend_list == null || friend_list.size() == 0) {
			if(is_self) showDescription(getString(R.string.personal_no_result_self_friends));
			else showDescription(getString(R.string.personal_no_result_friends));
		} else {
			showPersonalList();
			
			if (is_self)
				friend_adapter = new PersonalFriendAdapter(personal_listview, friend_list, this);
			else {
				ArrayList<TVUser> commons = new ArrayList<TVUser>();
				ArrayList<TVUser> requests = new ArrayList<TVUser>();
				ArrayList<TVUser> invitings = new ArrayList<TVUser>();
				ArrayList<TVUser> uncommons = new ArrayList<TVUser>();
				for (TVUser user:friend_list) {
					if (FriendManager.isFriend(user.getUsername()))
						commons.add(user);
					else if (FriendManager.isRequest(user.getUsername()))
						requests.add(user);
					else if (FriendManager.isInviting(user.getUsername()))
						invitings.add(user);
					else 
						uncommons.add(user);
				}
				friend_adapter = new PersonalFriendAdapter( personal_listview, commons, requests, 
															invitings, uncommons, this);
			}
			personal_listview.setAdapter(friend_adapter);
			
			if (reposition)
				personal_listview.setSelection(friend_position);
		}
	}
	
	/**
	 * Shwo favorite list.
	 * @param reposition whether reposition to last position
	 */
	private void showFavoriteList (boolean reposition) {
		if ((is_self && (favorite_list == null || favorite_list.size() == 0)) || 
			(!is_self && (common_favorite_list == null || common_favorite_list.size() == 0) && 
						 (uncommon_favorite_list == null || uncommon_favorite_list.size() == 0)))
				showDescription(getString(R.string.personal_no_result_follows));
		else {
			showPersonalList();
			
			if (is_self)
				favorite_adapter = new FollowListAdapter(personal_listview, favorite_list, this);
			else
				favorite_adapter = new FollowListAdapter(personal_listview, common_favorite_list,
										uncommon_favorite_list, this);
			personal_listview.setAdapter(favorite_adapter);
			
			if (reposition)
				personal_listview.setSelection(favorite_position);
		}
	}
	
	/**
	 * Show reminder list.
	 * @param reposition whether reposition to last position
	 */
	private void showReminderList (boolean reposition) {
		if ((is_self && reminder_list.size() == 0) || 
			(!is_self && common_reminder_list.size() == 0 && uncommon_reminder_list.size() == 0))
			showDescription(PersonalActivity.this.getString(R.string.personal_no_result_reminders));
		else {
			showPersonalList();
			
			if (is_self)
				reminder_adapter = new ReminderListAdapter(personal_listview, reminder_list, this);
			else
				reminder_adapter = new ReminderListAdapter(personal_listview, common_reminder_list,
										uncommon_reminder_list, this);
			personal_listview.setAdapter(reminder_adapter);
			
			if (reposition)
				personal_listview.setSelection(reminder_position);
		}
	}
	
	/**
	 * Get user profile data and refresh.
	 */
	private void getUserProfile () {
		new Thread ( new Runnable () {
			public void run () {
				TVUser tu = UserManager.getUserByUsername(username);
				
				if (!isDestroyed()) {
					user = tu;
					if (is_self)
						AccountManager.getCurrentUser().setScores(user.getScores());
					else {
						// refresh profile
						PersonalActivity.this.runOnUiThread(new Runnable () {
							public void run () {
								refreshProfile();
							}
						});
					}
				}
			}
		}).start();
	}
	
	/**
	 * Update mood.
	 * @param mood
	 */
	private void updateMood (final String mood) {
		// show loading progress dialog
		final ProgressDialog pd = ProgressDialog.show(this, "", getString(R.string.action_saving));
		new Thread (new Runnable () {
			public void run () {
				// update mood
				final boolean success = AccountManager.updateMood(mood);
				
				if (!isDestroyed()) {
					mood_textview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							// dismiss loading progress dialog
							pd.dismiss();
							
							if (success) {
								user.getExtraInfo().setMood(mood);
								mood_textview.setText(mood);
							} else
								Toast.makeText(PersonalActivity.this, 
									R.string.personal_mood_update_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Callback for extra info clicked.
	 * @return
	 */
	private View.OnClickListener getExtraInfoClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (user == null)
					return;
				Intent i = new Intent(PersonalActivity.this, PersonalExtraInfoActivity.class);
				i.putExtra("user", user);
				startActivity(i);
			}
		};
	}
	
	/**
	 * Callback for social text view clicked.
	 * @return
	 */
	private View.OnClickListener getSocialTextViewClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!is_self)
					return;
				
				temp_mood = mood_textview.getText().toString();
				
				// Mood Edit Text
				final EditText moodEditText = new EditText(PersonalActivity.this);
				moodEditText.setTextSize(14);
				moodEditText.setText(temp_mood);
				moodEditText.addTextChangedListener(new TextWatcher () {
					@Override
					public void afterTextChanged(Editable s) {
						String mood = moodEditText.getText().toString();
						int visualLength = 0;
						for (int i = 0; i < mood.length(); i++)
							visualLength += StringGenerator.isChinese(mood.substring(i, i+1)) ? 2 : 1;
						if (visualLength >= 50) {
							moodEditText.setText(temp_mood);
							moodEditText.setSelection(temp_mood.length());
						} else
							temp_mood = mood;
					}
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						
					}
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						
					}			
				});
				moodEditText.setOnEditorActionListener(getMoodEditorActionListener());
				
				// Button
				DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener () {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						updateMood(moodEditText.getText().toString());
					}
				};
				
				// Dialog
				AlertDialog dialog = new AlertDialog.Builder(PersonalActivity.this)
							   .setTitle(R.string.personal_mood_title)
							   .setView(moodEditText)
							   .setPositiveButton(R.string.action_confirm, positive)
							   .setNegativeButton(R.string.action_cancel, null)
							   .create();
				dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				dialog.show();
			}
		};
	}
	
	/**
	 * Callback for mood editor.
	 * @return
	 */
	private OnEditorActionListener getMoodEditorActionListener () {
		return new OnEditorActionListener () {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
					updateMood(v.getText().toString());
					return true;
				}
				return false;
			}
		};
	}
	
	/**
	 * Callback for social action layout clicked.
	 * @return
	 */
	private View.OnClickListener getSocialActionClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.splash_social_layout) {
					Intent i = new Intent(PersonalActivity.this, SplashPrivateActivity.class);
					i.putExtra("username", user.getUsername());
					i.putExtra("nickname", user.getNickname());
					i.putExtra("focus_typing", true);
					i.putExtra("back_image", AvatarManager.getAvatarBitmap(user.getUsername(), true));
					startActivity(i);
					
				} else if (v.getId() == R.id.friend_action_layout) {
					friend_layout.setVisibility(View.INVISIBLE);
					if (FriendManager.isFriend(username))
						return;
					else if (FriendManager.isRequest(username))
						FriendManager.acceptFriend(user);
					else
						FriendManager.inviteFriend(user);
				}
			}
		};
	}
	
	/**
	 * Callback for badge clicked.
	 * @return
	 */
	private View.OnClickListener getBadgeClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (user == null)
					return;
				
				final ProgressDialog pd = ProgressDialog.show(PersonalActivity.this, "", 
						ApplicationManager.getAppContext().getResources().getString(R.string.action_loading));
				
				new Thread ( new Runnable () {
					public void run () {
						final TVBadge badge = BadgeManager.getBadge(user.getBadgeId());
						
						// prepare badge image
						if (!BadgeImageManager.isBadgeImageExistInStorage(user.getBadgeId()))
							BadgeImageManager.downloadBadgeImage(user.getBadgeId());
						
						if (!isDestroyed()) {
							badge_imageview.post( new Runnable () {
								public void run () {
									if (isDestroyed())
										return;
									
									pd.dismiss();
									if (badge != null)
										showBadgePlayer(badge, null, false, false);
									else
										Toast.makeText(PersonalActivity.this, R.string.personal_view_badge_failed, Toast.LENGTH_SHORT).show();
								}
							});
						}
							
					}
				}).start();
			}
		};
	}
	
	/**
	 * Callback for tab button clicked.
	 * @return
	 */
	private View.OnClickListener getTabClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (selected_tab == v.getId())
					return;
				
				int lastPosition = personal_listview.getFirstVisiblePosition();
				
				// interrupt current adapter scrolling
				personal_listview.setSelection(lastPosition);
				
				// clear old list data and record old position
				switch (selected_tab) {
				case WAVE_TAB_ID:
					feed_position = lastPosition;
					if (feed_adapter != null)
						feed_adapter.clear();
					feed_adapter = null;
					break;
				case BADGE_TAB_ID:
					badge_position = lastPosition;
					if (badge_adapter != null)
						badge_adapter.clear();
					badge_adapter = null;
					break;
				case FRIEND_TAB_ID:
					friend_position = lastPosition;
					if (friend_adapter != null)
						friend_adapter.clear();
					friend_adapter = null;
					break;
				case FAVORITE_TAB_ID:
					favorite_position = lastPosition;
					if (favorite_adapter != null)
						favorite_adapter.clear();
					favorite_adapter = null;
					break;
				case REMINDER_TAB_ID:
					reminder_position = lastPosition;
					if (reminder_adapter != null)
						reminder_adapter.clear();
					reminder_adapter = null;
					break;
				default:
					break;
				}
				
				selectTabItem(selected_tab = v.getId());
			}
		};
	}
	
	/**
	 * Callback for personal list view scrolled.
	 * @return
	 */
	private AbsListView.OnScrollListener getScrollListener () {
		return new AbsListView.OnScrollListener () {
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, 
					int totalItemCount) {
				if (selected_tab == WAVE_TAB_ID) {
					if (firstVisibleItem + visibleItemCount > totalItemCount - 2) {
						if (more_feed)
							getMoreWaves(feed_list.get(feed_list.size() - 1).getId());
					}
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
		};
	}
	
	/**
	 * Select tab item.
	 * @param id
	 */
	private void selectTabItem (int id) {
		// refresh tab bar
		wave_imagebutton.setSelected(id == WAVE_TAB_ID);
		badge_imagebutton.setSelected(id == BADGE_TAB_ID);
		friend_imagebutton.setSelected(id == FRIEND_TAB_ID);
		favorite_imagebutton.setSelected(id == FAVORITE_TAB_ID);
		reminder_imagebutton.setSelected(id == REMINDER_TAB_ID);
		
		// refresh list
		switch (id) {
		case WAVE_TAB_ID:
			if (feed_list != null)
				showWaveList(true);
			else {
				showPersonalLoading();
				getWaves();
			}
			break;
			
		case BADGE_TAB_ID:
			if (badge_manager != null)
				showBadgeList(true);
			else {
				showPersonalLoading();
				getBadges();
			}
			break;
			
		case FRIEND_TAB_ID:
			if (friend_list != null)
				showFriendList(true);
			else {
				showPersonalLoading();
				if (user != null)
					getFriends();
			}
			break;
			
		case FAVORITE_TAB_ID:
			if ((is_self && favorite_list != null) ||
				(!is_self && common_favorite_list != null && uncommon_favorite_list != null))
				showFavoriteList(true);
			else {
				showPersonalLoading();
				if (is_self)
					getSelfFavorites();
				else
					getOtherFavorites();
			}
			break;
			
		case REMINDER_TAB_ID:
			if ((is_self && reminder_list != null) ||
				(!is_self && common_reminder_list != null && uncommon_reminder_list != null))
					showReminderList(true);
				else {
					showPersonalLoading();
					if (is_self)
						getSelfReminders();
					else {
						if (user != null)
							getOtherReminders();
					}
				}
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * Get waves.
	 */
	private void getWaves () {
		if (feed_load)
			return;
		feed_load = true;
		
		new Thread ( new Runnable () {
			public void run () {
				FeedManager fm = new FeedManager();
				ArrayList<Feed> array = fm.getUserFeed(null, username);
				
				if (!isDestroyed()) {
					// check if feed_load becomes false while downloading feeds, which means user has
					// request feeds again, then we don't need to refresh for this old request
					if (!feed_load)
						return;
					feed_list = array;
					more_feed = fm.isMoreUser();
					
					PersonalActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							if (selected_tab == WAVE_TAB_ID)
								showWaveList(true);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get more waves after feed_id.
	 * @param feed_id
	 */
	private void getMoreWaves (final String feed_id) {
		if (feed_loading_more)
			return;
		feed_loading_more = true;
		
		new Thread ( new Runnable () {
			public void run () {
				final FeedManager fm = new FeedManager();
				final ArrayList<Feed> array = fm.getUserFeed(feed_id, username);
				
				if (!isDestroyed()) {
					PersonalActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							if (!feed_loading_more)
								return;
							
							more_feed = fm.isMoreUser();
							feed_list.addAll(array);
							
							if (selected_tab == WAVE_TAB_ID)
								feed_adapter.refreshData(feed_list, more_feed);
							
							feed_loading_more = false;
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get badges.
	 */
	private void getBadges () {
		if (badge_load)
			return;
		badge_load = true;
		
		new Thread ( new Runnable () {
			public void run () {
				BadgeManager bm = new BadgeManager(username);
				bm.getBadgeSummary();
				
				if (!isDestroyed()) {
					if (!badge_load)
						return;
					badge_manager = bm;
					
					PersonalActivity.this.runOnUiThread (new Runnable () {
						public void run () {
							if (selected_tab == BADGE_TAB_ID)
								showBadgeList(true);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get friends.
	 */
	private void getFriends () {
		if (friend_load)
			return;
		friend_load = true;
		
		new Thread ( new Runnable () {
			public void run () {
				ArrayList<TVUser> array = UserManager.getFriendsByJid(user.getJid());
				
				if (!isDestroyed()) {
					if (!friend_load)
						return;
					friend_list = array;
					Collections.sort(friend_list);
					
					PersonalActivity.this.runOnUiThread (new Runnable () {
						public void run () {
							if (selected_tab == FRIEND_TAB_ID)
								showFriendList(true);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get favorites for self's profile.
	 */
	private void getSelfFavorites () {
		if (favorite_load)
			return;
		favorite_load = true;
		
		new Thread ( new Runnable () {
			public void run () {
				final ArrayList<TVFollow> array = FollowManager.getFollowList(username);
				
				if (!isDestroyed()) {
					if (!favorite_load)
						return;
					
					PersonalActivity.this.runOnUiThread(new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							favorite_list = array;
							if (selected_tab == FAVORITE_TAB_ID)
								showFavoriteList(true);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get favorites for other's profile.
	 */
	private void getOtherFavorites () {
		if (favorite_load)
			return;
		favorite_load = true;
		
		new Thread ( new Runnable () {
			public void run () {
				final ArrayList<TVFollow> self_follows = FollowManager.getFollowList(AccountManager.getCurrentUser().getUsername());
				final ArrayList<TVFollow> other_follows = FollowManager.getFollowList(username);
				
				if (!isDestroyed()) {
					if (!favorite_load)
						return;
					
					PersonalActivity.this.runOnUiThread(new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							common_favorite_list = new ArrayList<TVFollow>();
							uncommon_favorite_list = new ArrayList<TVFollow>();
							
							for (TVFollow other:other_follows) {
								boolean common = false;
								for (TVFollow self:self_follows) {
									if (other.getPgid().equals(self.getPgid())) {
										common_favorite_list.add(other);
										common = true;
										break;
									}
								}
								if (!common)
									uncommon_favorite_list.add(other);
							}
							if (selected_tab == FAVORITE_TAB_ID)
								showFavoriteList(true);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get reminders for self's profile.
	 */
	private void getSelfReminders () {
		if (reminder_load)
			return;
		reminder_load = true;
		
		new Thread ( new Runnable () {
			public void run () {
				final ArrayList<TVReminder> array = ReminderManager.getReminderList(username);
				
				if (!isDestroyed()) {
					if (!reminder_load)
						return;
					
					PersonalActivity.this.runOnUiThread(new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							reminder_list = array;
							if (selected_tab == REMINDER_TAB_ID)
								showReminderList(true);
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get reminders for other's profile.
	 */
	private void getOtherReminders () {
		// if privacy is forbidden, just make zero item list
		//
		String privacy = user.getPrivacy().getReminder();
		if (privacy.equals(TVUserPrivacy.PRIVACY_NONE) ||
			privacy.equals(TVUserPrivacy.PRIVACY_FRIEND) && !FriendManager.isFriend(user.getUsername())) {
			common_reminder_list = new ArrayList<TVReminder>();
			uncommon_reminder_list = new ArrayList<TVReminder>();
			showReminderList(true);
			
			Toast.makeText(PersonalActivity.this, "Privacy forbidden", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// private if not forbidden, get reminder list
		//
		if (reminder_load)
			return;
		reminder_load = true;
		
		new Thread ( new Runnable () {
			public void run () {
				final ArrayList<TVReminder> self_reminders = ReminderManager.getReminderList(AccountManager.getCurrentUser().getUsername());
				final ArrayList<TVReminder> other_reminders = ReminderManager.getReminderList(username);
				
				if (!isDestroyed()) {
					if (!reminder_load)
						return;
					
					PersonalActivity.this.runOnUiThread(new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							common_reminder_list = new ArrayList<TVReminder>();
							uncommon_reminder_list = new ArrayList<TVReminder>();
							
							// filter common and uncommon
							for (TVReminder other:other_reminders) {
								boolean common = false;
								for (TVReminder self:self_reminders) {
									if (other.getPgid().equals(self.getPgid())) {
										common_reminder_list.add(other);
										common = true;
										break;
									}
								}
								if (!common)
									uncommon_reminder_list.add(other);
							}
							// show reminder list
							if (selected_tab == REMINDER_TAB_ID)
								showReminderList(true);
						}
					});
				}
			}
		}).start();
	}
	
	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		switch (selected_tab) {
		case WAVE_TAB_ID:
			onWaveItemClick(position);
			break;
		case BADGE_TAB_ID:
			onBadgeItemClick(position);
			break;
		case FRIEND_TAB_ID:
			onFriendItemClick(position);
			break;
		case FAVORITE_TAB_ID:
			onFavoriteItemClick(position);
			break;
		case REMINDER_TAB_ID:
			onReminderItemClick(position);
			break;
		}
	}
	
	/**
	 * Callback for wave list item clicked.
	 * @param position
	 */
	private void onWaveItemClick (int position) {
		if (feed_adapter == null)
			return;
		Feed feed = (Feed) feed_adapter.getItem(position);
		if (feed != null)
			openProgramActivity(feed.getProgram());
	}
	
	/**
	 * Callback for badge list item clicked.
	 * @param position
	 */
	private void onBadgeItemClick (int position) {
		if (badge_adapter == null)
			return;
		Object obj = badge_adapter.getProgramItem(position);
		if (obj != null) {
			openBadgeProgramActivity((TVBadge) obj);
			return;
		}
		obj = badge_adapter.getItem(position);
		openBadgeTypeActivity((TVBadge) obj);
	}
	
	/**
	 * Callback for friend item clicked.
	 * @param position
	 */
	private void onFriendItemClick (int position) {
		if (friend_adapter == null)
			return;
		TVUser user = (TVUser) friend_adapter.getItem(position);
		if (user != null)
			openPersonalActivity(user);
	}
	
	/**
	 * Callback for favorite item clicked.
	 * @param position
	 */
	private void onFavoriteItemClick (int position) {
		if (favorite_adapter == null)
			return;
		TVFollow follow = (TVFollow) favorite_adapter.getItem(position);
		if (follow != null)
			openProgramActivity(follow);
	}
	
	/**
	 * Callback for reminder item clicked.
	 * @param position
	 */
	private void onReminderItemClick (int position) {
		if (reminder_adapter == null)
			return;
		TVReminder reminder = (TVReminder) reminder_adapter.getItem(position);
		if (reminder != null)
			openProgramActivity(reminder);
	}
	
	/**
	 * Open program activity.
	 * @param tp
	 */
	public void openProgramActivity (TVProgram tp) {
		if (tp.getTitle().length() == 0)
			return;
		
		Intent i = new Intent(PersonalActivity.this, ProgramActivity.class);
		i.putExtra("program", tp);
		i.putExtra("back_image", AvatarManager.getAvatarBitmap(username, true));
		startActivity(i);
	}
	
	/**
	 * Open personal activity.
	 * @param user
	 */
	public void openPersonalActivity (TVUser user) {
		if (user.getUsername().length() == 0)
			return;
		
		Intent i = new Intent(PersonalActivity.this, PersonalActivity.class);
		i.putExtra("username", user.getUsername());
		i.putExtra("nickname", user.getNickname());
		i.putExtra("back_image", AvatarManager.getAvatarBitmap(username, true));
		startActivity(i);
	}
	
	/**
	 * Open badge type activity.
	 * @param badge
	 */
	public void openBadgeTypeActivity (TVBadge badge) {
		if (badge.getType().length() == 0)
			return;
		
		Intent i = new Intent(PersonalActivity.this, BadgeTypeActivity.class);
		i.putExtra("username", user.getUsername());
		i.putExtra("type", badge.getType());
		startActivity(i);
	}
	
	public void openBadgeProgramActivity (TVBadge badge) {
		if (badge.getType().length() == 0)
			return;
		
		Intent i = new Intent(PersonalActivity.this, BadgeProgramActivity.class);
		i.putExtra("username", user.getUsername());
		i.putExtra("badge", badge);
		startActivity(i);
	}
	
	/**
	 * Menu Control Methods
	 */
	
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.personal_menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(selected_tab) {
		
		case WAVE_TAB_ID:
			refreshWaves();
			break;
			
		case BADGE_TAB_ID:
			refreshBadges();
			break;
			
		case FRIEND_TAB_ID:
			refreshFriends();
			break;
			
		case FAVORITE_TAB_ID:
			refreshFavorites();
			break;
			
		case REMINDER_TAB_ID:
			refreshReminders();
			break;
			
		default:
			break;
		}
		return true;
	}
	
	/**
	 * Image Process Methods
	 */
	
	/**
	 * Load avatar.
	 */
	private void loadAvatar () {
		if (user == null || user.getUsername() == null || user.getUsername().length() == 0)
			return;
		
		if (ImageManager.drawAvatarImage(avatar_imageview, username)) {
			avatar_loaded = true;
			if (badge_loaded) 
				ImageManager.removeImageListener(image_listener);
		}
	}
	
	/**
	 * Load badge.
	 */
	private void loadBadge () {
		if (user == null || user.getBadgeId() == null || user.getBadgeId().length() == 0)
			return;
		
		if (ImageManager.drawBadgeImage(badge_imageview, user.getBadgeId(), true)) {
			badge_loaded = true;
			if (avatar_loaded) 
				ImageManager.removeImageListener(image_listener);
		}
	}
	
	/**
	 * Listener for listening image loading requests.
	 * @author Kencool
	 *
	 */
	private class PersonalImageListener implements ImageListener {

		@Override
		public void retrieveAvatar(final String username, final Bitmap bmp) {
			if (avatar_loaded || user == null || !user.getUsername().equals(username))
				return;
			
			PersonalActivity.this.runOnUiThread(new Runnable () {
				public void run () {
					if (isDestroyed())
						return;
					avatar_imageview.setImageBitmap(bmp);
					avatar_loaded = true;
					if (badge_loaded) 
						ImageManager.removeImageListener(image_listener);
				}
			});
		}

		@Override
		public void retrieveBadge(final String badge_id, final boolean scaled, final Bitmap bmp) {
			if (badge_loaded || user == null || !user.getBadgeId().equals(badge_id) || !scaled)
				return;
			
			if (!isDestroyed()) {
				badge_imageview.post( new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						badge_imageview.setImageBitmap(bmp);
						badge_loaded = true;
						if (avatar_loaded) 
							ImageManager.removeImageListener(image_listener);
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
		public void retrieveChannelIcon(String title, Bitmap bmp) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * AvatarReceiver class
	 * @author Kencool
	 *
	 */
	private class AvatarReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (action.equals(AvatarManager.BROADCAST_REFRESH_AVATAR)) {
				avatar_loaded = false;
				ImageManager.addImageListener(image_listener);
				loadAvatar();
				if (feed_adapter != null)
					feed_adapter.notifyDataSetChanged();
			}
		}
		
	}
}
