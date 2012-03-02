package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.friend.FriendManager;
import com.wildmind.fanwave.hot.HotManager;
import com.wildmind.fanwave.hot.RecommendGalleryAdapter;
import com.wildmind.fanwave.hot.TVHot;
import com.wildmind.fanwave.media.AvatarManager;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.program.TVProgram;
import com.wildmind.fanwave.vendor.VendorManager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainPageActivity extends BaseActivity {
	
	private ImageView 	waves_imageview, guides_imageview, hot_imageview, splash_imageview, 
						friends_imageview, profile_imageview;
	private ImageView 	shaking_imageview = null;
	private TextView	friend_badge_textview, nickname_textview;
	private Gallery		media_gallery;
	
	private Intent 		function_intent = null;
	private Animation 	shake_animation = null;
	private RecommendGalleryAdapter	recommend_adapter = null;
	private ArrayList<TVHot> recommendation = new ArrayList<TVHot>();
	
	private MainPageReceiver main_page_receiver = new MainPageReceiver();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.main_page_activity);
		
		if (!AccountManager.isLogin())
			finish();
		
		initData();
		initUI();
		refreshMedia();
		
		// show pending intent if exist
		if (ApplicationManager.getPendingIntent() != null) {
			startActivity(ApplicationManager.getPendingIntent());
			ApplicationManager.setPendingIntent(null);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();

	}
	
	protected void onStop() {
		super.onStop();
	}

	protected void onDestroy() {
		super.onDestroy();
		
		/** unregisterReceiver some time was have error : 
		 *  I was search the network , got :
		 *  
		 *     If the receiver was already unregistered (probably in the code that you didn't include in this post) 
		 *  or was not registered, then call to unregisterReceiver throws IllegalArgumentException. In your case you 
		 *  need to just put special try/catch for this exception and ignore it (assuming you can't or don't want to 
		 *  control number of times you call unregisterReceiver on the same recevier).
		 *  
		 *   So i use the "try". 
		 *                            Eli
		 *     
		*/
		try {
			if (ApplicationManager.getAppContext() != null)
				ApplicationManager.getAppContext().unregisterReceiver(main_page_receiver);
		}
		catch(IllegalArgumentException e) {}
		
		main_page_receiver = null;
		
		waves_imageview = null;
		guides_imageview = null;
		hot_imageview = null;
		splash_imageview = null;
		friends_imageview = null;
		profile_imageview = null;
		shaking_imageview = null;
		friend_badge_textview = null;
		nickname_textview = null;
		media_gallery = null;
		
		shake_animation = null;
		function_intent = null;
		recommendation = null;
		
		if(recommend_adapter != null)
			recommend_adapter.clear();
		recommend_adapter = null;
	}
	
	private void initData () {
		// Shake Animation
		shake_animation = AnimationUtils.loadAnimation(this, R.anim.shake_animation);
		shake_animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				if(function_intent != null){
					startActivity(function_intent);
				}
			}
		});
		
		// init receiver
		initReceiver();
	}
	
	/**
	 * Init receiver.
	 */
	private void initReceiver () {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AvatarManager.BROADCAST_REFRESH_AVATAR);
		intentFilter.addAction(AccountManager.BROADCAST_USER_CHANGE_NAME);
		intentFilter.addAction(FriendManager.BROADCAST_REQUEST_CHANGED);
		ApplicationManager.getAppContext().registerReceiver(main_page_receiver, intentFilter);
	}
	
	private void initUI () {
		// Settings Button
		Button settings = (Button) findViewById(R.id.account_button);
		settings.setOnClickListener(getSettingsButtonClickedListener());
		
		// Favorite Button
		Button favoriteBtn = (Button) findViewById(R.id.favorite_button);
		favoriteBtn.setOnClickListener(getFavoriteButtonClickedListener());
		
		// Media Gallery
		media_gallery = (Gallery) findViewById(R.id.media_gallery);
		recommend_adapter = new RecommendGalleryAdapter(media_gallery, null, this);
		media_gallery.setAdapter(recommend_adapter);
		media_gallery.setOnItemClickListener(getMediaClickedListener());
		
		// Function
		initFunctionUI();
		
		// Notification
		RelativeLayout notification = (RelativeLayout) findViewById(R.id.notification_layout);
		notification.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainPageActivity.this, NotificationActivity.class);
				startActivity(i);
			}
		});
	}
	
	private void initFunctionUI () {
		// Function Layout
		View.OnTouchListener listener = getFunctionTouchedListener();
		LinearLayout waves_layout = (LinearLayout)findViewById(R.id.waves_function_layout);
		waves_layout.setOnTouchListener(listener);
		
		LinearLayout guides_layout = (LinearLayout)findViewById(R.id.guides_function_layout);
		guides_layout.setOnTouchListener(listener);
		
		LinearLayout hot_layout = (LinearLayout)findViewById(R.id.hot_function_layout);
		hot_layout.setOnTouchListener(listener);
		
		LinearLayout splash_layout = (LinearLayout)findViewById(R.id.splash_function_layout);
		splash_layout.setOnTouchListener(listener);
		
		LinearLayout friends_layout = (LinearLayout)findViewById(R.id.friends_function_layout);
		friends_layout.setOnTouchListener(listener);
		
		LinearLayout profile_layout = (LinearLayout)findViewById(R.id.profile_function_layout);
		profile_layout.setOnTouchListener(listener);
		
		// Function Image View
		waves_imageview = (ImageView)findViewById(R.id.waves_function_imageview);
		guides_imageview = (ImageView)findViewById(R.id.guides_function_imageview);
		hot_imageview = (ImageView)findViewById(R.id.hot_function_imageview);
		splash_imageview = (ImageView)findViewById(R.id.splash_function_imageview);
		friends_imageview = (ImageView)findViewById(R.id.friends_function_imageview);
		profile_imageview = (ImageView)findViewById(R.id.profile_function_imageview);
		loadAvatar();
		
		// Function Text view
		friend_badge_textview = (TextView) findViewById(R.id.friends_request_textview);
		refreshFriendBadge();
		
		nickname_textview = (TextView) findViewById(R.id.profile_function_textview);
		nickname_textview.setText(AccountManager.getCurrentUser().getNickname());
	}
	
	/**
	 * Show account dialog.
	 */
	private void showAccountDialog () {
		// item adapter
		final CharSequence [] items = { getString(R.string.main_page_account_help),
										getString(R.string.main_page_account_settings),
										getString(R.string.main_page_account_about),
										getString(R.string.main_page_account_logout)};
		final LayoutInflater inflater = LayoutInflater.from(this);
		ListAdapter adapter = new BaseAdapter() {
			public long getItemId(int position) {
				return position;
			}
			public Object getItem(int position) {
				return items[position];
			}
			public int getCount() {
				return items.length;
			}
			public View getView(int position, View view, ViewGroup parent) {
				view = inflater.inflate(R.layout.account_dialog_view, null);
				TextView item = (TextView) view.findViewById(R.id.account_dialog_textview);
				LinearLayout help = (LinearLayout) view.findViewById(R.id.help_layout);
				TextView help_item = (TextView) view.findViewById(R.id.account_dialog_help_center_textview);
				
				if (position == 0) {
					item.setVisibility(View.INVISIBLE);
					help.setVisibility(View.VISIBLE);
					help_item.setText(items[position]);
				} else {
					item.setVisibility(View.VISIBLE);
					help.setVisibility(View.INVISIBLE);
					item.setText(items[position]);
				}
				
				return view;
			}
		};
		
		// item listener
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i = null;
				switch(which){
				case 0: 
					TVProgram program = new TVProgram();
					program.setTitle("FanwaveªA°È¥x");
					program.setPgid("46616e77617665e69c8de58b99e58fb0");
					program.setCountry(VendorManager.getCountry());
					i = new Intent(MainPageActivity.this, ProgramActivity.class);
					i.putExtra("program", program);
					i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.home_icon));
					break;
				case 1:
					i = new Intent(MainPageActivity.this, AccountSettingsActivity.class);
                    break;
				case 2:	
					i = new Intent(MainPageActivity.this, AboutActivity.class);
					break;
				case 3:
					processLogout();
					break;
				}
				if (i != null)
					startActivity(i);
			}
		};
		
		// alert dialog
		new AlertDialog.Builder(MainPageActivity.this)
					   .setTitle(R.string.main_page_account_title)
					   .setPositiveButton(R.string.action_cancel, null)
					   .setAdapter(adapter, listener)
					   .show();
	}
	
	/**
	 * Process logout.
	 */
	private void processLogout () {
		// button
		DialogInterface.OnClickListener positive = new  DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				AccountManager.logout();
				Intent i = new Intent(MainPageActivity.this, LoginActivity.class);
				i.putExtra("from_launching", false);
				finish();
				startActivity(i);
			}
		}; 
		
		// alert dialog
		new AlertDialog.Builder(this)
		   			   .setTitle(R.string.action_logout)
		   			   .setMessage(R.string.main_page_account_logout_message)
		   			   .setPositiveButton(R.string.action_confirm, positive)
		   			   .setNegativeButton(R.string.action_cancel, null)
		   			   .show();
	}
	
	/**
	 * Refresh friend badge text view.
	 */
	private void refreshFriendBadge () {
		int count = FriendManager.getRequestList().size();
		friend_badge_textview.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
		friend_badge_textview.setText(String.valueOf(count));
	}
	
	/**
	 * Refresh media.
	 */
	private void refreshMedia () {
		recommendation.clear();
		getMedia();
	}
	
	/**
	 * Show media.
	 */
	private void showMedia () {
		recommend_adapter.refreshData(recommendation);
		if(recommendation.size() > 1)
			media_gallery.setSelection(1);
	}	
	
	/**
	 * Get media data.
	 */
	private void getMedia () {
		new Thread (new Runnable () {
			public void run () {
				// get recommendation
				final ArrayList<TVHot> recommend = new ArrayList<TVHot>();
				ArrayList<TVHot> hotRecommendation = HotManager.getRecommendation();
				for (TVHot hot:hotRecommendation) {
					if (hot.getIconUrl().length() > 0)
						recommend.add(hot);
				}
				
				if (!isDestroyed()) {
					media_gallery.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							recommendation = recommend;
							showMedia();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Load avatar. Here we don't use image listener since avatar is only loaded once. Just try redraw 
	 * after certain interval if failed.
	 */
	private void loadAvatar () {
		if (isDestroyed())
			return;
		
		if (!ImageManager.drawAvatarImage(profile_imageview, AccountManager.getCurrentUser().getUsername()))
			profile_imageview.postDelayed(new Runnable () {
				public void run () {
					loadAvatar();
				}
			}, 1000);
	}
	
	/**
	 * Open program activity with TVProgram tp.
	 * @param tp
	 */
	public void openProgramActivity (TVProgram tp) {
		if (tp.getTitle().length() == 0)
			return;
		
		Intent i = new Intent(MainPageActivity.this, ProgramActivity.class);
		i.putExtra("program", tp);
		i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.home_icon));
		startActivity(i);
	}
	
	/**
	 * Callback for account button clicked.
	 * @return
	 */
	private View.OnClickListener getSettingsButtonClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showAccountDialog();
			}
		};
	}
	
	/**
	 * Callback for favorite button clicked.
	 * @return
	 */
	private View.OnClickListener getFavoriteButtonClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainPageActivity.this, ProgramSearchActivity.class);
				startActivity(i);
			}
		};
	}
	
	/**
	 * Callback for function touched.
	 * @return
	 */
	private View.OnTouchListener getFunctionTouchedListener () {
		return new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				Intent i = null;
				ImageView iv = null;
				
				// create function intent
				switch (v.getId()) {
				case R.id.waves_function_layout:
					i = new Intent(MainPageActivity.this, WavesActivity.class);
					iv = waves_imageview;
					break;
				case R.id.guides_function_layout:
					iv = guides_imageview;
					i = new Intent(MainPageActivity.this, GuideActivity.class);
					break;
				case R.id.hot_function_layout:
					iv = hot_imageview;
					i = new Intent(MainPageActivity.this, HotActivity.class);
					break;
				case R.id.splash_function_layout:
					i = new Intent(MainPageActivity.this, SplashActivity.class);
					iv = splash_imageview;
					break;
				case R.id.friends_function_layout:
					i = new Intent(MainPageActivity.this, FriendsActivity.class);
					iv = friends_imageview;
					break;
				case R.id.profile_function_layout:
					i = new Intent(MainPageActivity.this, PersonalActivity.class);
					i.putExtra("username", AccountManager.getCurrentUser().getUsername());
					i.putExtra("nickname", AccountManager.getCurrentUser().getNickname());
					i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.home_icon));
					iv = profile_imageview;
					break;
				}
				
				// shake function image view
				if (iv != null && event.getAction() == MotionEvent.ACTION_DOWN) {
					if(shake_animation.hasStarted() && shaking_imageview != null){
						shaking_imageview.clearAnimation();
						shaking_imageview = null;
						function_intent = null;
					}
					function_intent = i;
					iv.startAnimation(shake_animation);
					shaking_imageview = iv;
				}
				
				return true;
			}
		};
	}
	
	/**
	 * Callback for media item clicked.
	 * @return
	 */
	private OnItemClickListener getMediaClickedListener () {
		return new OnItemClickListener () {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				openProgramActivity((TVProgram) recommend_adapter.getItem(position));
			}
		};
	}
	
	/**
	  * onKeyDown Touch Event Method
	  * @param keyCode
	  * @param event
	  * @return boolean
	  */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			showAccountDialog();
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_BACK) 
			ApplicationManager.stopApplication(); 

		return super.onKeyDown(keyCode, event); 
	}
	
	private class MainPageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			
			if (action.equals(AvatarManager.BROADCAST_REFRESH_AVATAR)) {
				loadAvatar();
			} else if (action.equals(AccountManager.BROADCAST_USER_CHANGE_NAME)) {
				nickname_textview.setText(AccountManager.getCurrentUser().getNickname());
			} else if (action.equals(FriendManager.BROADCAST_REQUEST_CHANGED)) {
				refreshFriendBadge();
			}
		}
		
	}
}
