package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.friend.FriendManager;
import com.wildmind.fanwave.notification.FWNotificationManager;
import com.wildmind.fanwave.notification.NotificationListener;
import com.wildmind.fanwave.splash.Splash;
import com.wildmind.fanwave.splash.SplashManager;
import com.wildmind.fanwave.splash.SplashPrivateAdapter;
import com.wildmind.fanwave.splash.SplashPrivateAdapter.SplashPrivateListener;
import com.wildmind.fanwave.user.TVUser;
import com.wildmind.fanwave.user.TVUserPrivacy;
import com.wildmind.fanwave.user.UserManager;
import com.wildmind.fanwave.widget.SoftKeyboardFrameLayout;
import com.wildmind.fanwave.widget.SoftKeyboardFrameLayout.SoftKeyboardListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Intent input data "username" 	: String
 * 					 "nickname" 	: String
 * 					 "back_image" 	: Bitmap
 * 					 "focus_typing"	: boolean
 * @author Kencool
 *
 */

public class SplashPrivateActivity extends BaseActivity implements SoftKeyboardListener, 
		SplashPrivateListener, NotificationListener {

	private final int SPLASH_TEXT_MAX_BYTES = 100;
	
	private SoftKeyboardFrameLayout keyboard_listener_layout;
	private ListView 		splash_listview;
	private LinearLayout 	poster_layout, loading_indicator;
	private FrameLayout		poster_entry_layout;
	private RelativeLayout  notification_layout;
	private EditText		splash_edittext;
	private Button			splash_button;
	private TextView		notification_textview;
	
	private String username = null;
	private String nickname = null;
	private String history_id = null;
	private ArrayList<Splash> history = null;
	private SplashPrivateAdapter splash_adapter = null;
	
	private boolean focus_typing = false;
	private boolean keyboard_auto_show = false;
	private boolean more_history = false;
	private boolean more_loading = false;
	private boolean keyboard_showing = false;
	private int history_position = 0;
	
	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if activity is destroyed due to abnormal situation
        if (isDestroyed())
			return;
        
        setContentView(R.layout.splash_private_activity);
        
        initData();
        initUI();
        prepareSplash();
    }
	
	protected void onStart() {
		super.onStart();
		
		FWNotificationManager.setListener(this);
		if (history_id != null)
			FWNotificationManager.addUnnotifiedId(FWNotificationManager.UNNOTIFIED_CATEGORY_SPLASH, 
					  							  history_id, true);
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onStop() {
		super.onStop();
		
		FWNotificationManager.setListener(null);
		if (history_id != null)
			FWNotificationManager.removeUnnotifiedId(FWNotificationManager.UNNOTIFIED_CATEGORY_SPLASH, 
													 history_id);
	}

	protected void onDestroy() {
		super.onDestroy();
		
		if (keyboard_listener_layout != null)
			keyboard_listener_layout.clear();
		keyboard_listener_layout = null;
		
		if (splash_adapter != null)
			splash_adapter.clear();
		splash_adapter = null;
		
		splash_listview = null;
		poster_layout = null;
		loading_indicator = null;
		poster_entry_layout = null;
		notification_layout = null;
		splash_edittext = null;
		splash_button = null;
		notification_textview = null;
	}
	
	private void initData () {
		Intent i = getIntent();
		username = i.getStringExtra("username");
		nickname = i.getStringExtra("nickname");
		focus_typing = i.getBooleanExtra("focus_typing", false);
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
		
		// Back Image
		Bitmap bmp = getIntent().getParcelableExtra("back_image");
		if (bmp != null)
			back_button.setImageBitmap(bmp);
		
		// Title Text View
		TextView title = (TextView) findViewById(R.id.title_textview);
		title.setText(nickname);
		
		// Splash List View
		splash_listview = (ListView) findViewById(R.id.splash_list_listview);
		splash_listview.setDivider(null);
		splash_listview.setDividerHeight(0);
		splash_adapter = new SplashPrivateAdapter(splash_listview, null, username, 
								AccountManager.getCurrentUser().getUsername(), this);
		splash_adapter.setListener(this);
		splash_listview.setAdapter(splash_adapter);
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
		
		// Splash Poster Layout
		poster_layout = (LinearLayout) findViewById(R.id.message_splash_poster);
		poster_layout.setVisibility(View.GONE);
		splash_edittext = (EditText) poster_layout.findViewById(R.id.post_edittext);
		splash_edittext.addTextChangedListener(new SplashTextWatcher());
		splash_button = (Button) poster_layout.findViewById(R.id.post_button);
		splash_button.setOnClickListener(getSplashClickedListener());
		
		// Poster Entry Layout
		poster_entry_layout = (FrameLayout) findViewById(R.id.poster_entry_layout);
		
		// Notification Layout
		notification_layout = (RelativeLayout) findViewById(R.id.message_notification);
		notification_textview = (TextView) notification_layout.findViewById(R.id.notification_textview);
		
		// Keyboard Listener Layout
		keyboard_listener_layout = (SoftKeyboardFrameLayout) findViewById(R.id.keyboard_listener_layout);
		keyboard_listener_layout.setSoftKeyboardListener(this);
	}

	@Override
	public void onSoftKeyboardShown(boolean isShowing) {
		if (isShowing && !keyboard_showing) {
			// keyboard is going to show up
			keyboard_showing = true;
			onKeyboardShown();
			
		} else if (isShowing && keyboard_showing) {
			// keyboard is already shown
			
		} else if (!isShowing && keyboard_showing) {
			// keyboard is going to hide
			keyboard_showing = false;
			onKeyboardHidden();
			
		} else if (!isShowing && keyboard_showing) {
			// keyboard is already hidden
		}
	}
	
	private void onKeyboardShown () {
		poster_entry_layout.setVisibility(View.GONE);
		poster_layout.setVisibility(View.VISIBLE);
		splash_edittext.requestFocus();
		if (splash_listview != null && history != null) {
			splash_listview.post( new Runnable () {
				public void run () {
					if (splash_listview == null || history == null)
						return;
					
					splash_listview.setSelection(history.size() - 1);
				}
			});
		}
	}
	
	private void onKeyboardHidden () {
		poster_entry_layout.setVisibility(View.VISIBLE);
		poster_layout.setVisibility(View.GONE);
		splash_edittext.clearFocus();
	}
	
	/**
	 * Prepare splash functions.
	 */
	private void prepareSplash () {
		checkPrivacy();
		getHistoryId();
		refreshHistory();
	}
	
	/**
	 * Refresh splash history.
	 */
	private void refreshHistory () {
		showHistoryLoading();
		history = null;
		more_history = false;
		getHistory();
	}
	
	private void showHistoryLoading () {
		splash_listview.setVisibility(View.INVISIBLE);
		loading_indicator.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Show history.
	 * @param bottom
	 */
	private void showHistory (boolean bottom) {
		splash_listview.setVisibility(View.VISIBLE);
		loading_indicator.setVisibility(View.INVISIBLE);
		
		splash_adapter.refreshData(history, more_history);
		splash_listview.setSelection(bottom ? history.size() - 1 : history_position);
	}
	
	/**
	 * Check user's splash privacy.
	 */
	private void checkPrivacy () {
		notification_textview.setText(R.string.splash_check_privacy);
		new Thread (new Runnable () {
			public void run () {
				final TVUser user = UserManager.getUserByUsername(username);
				final boolean splashable = user.getPrivacy().getSplash().equals(TVUserPrivacy.PRIAVCY_ALL) ||
							 (user.getPrivacy().getSplash().equals(TVUserPrivacy.PRIVACY_FRIEND) && 
							  FriendManager.isFriend(username));
				
				if (!isDestroyed()) {
					poster_entry_layout.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							if (splashable) {
								notification_layout.setVisibility(View.GONE);
								if (focus_typing) {
									if (history != null) {
										InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
										imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
									} else
										// pending show keyboard to get history task
										keyboard_auto_show = true;
								}
							} else {
								if (user.getPrivacy().getSplash().equals(TVUserPrivacy.PRIVACY_FRIEND))
									notification_textview.setText(R.string.splash_privacy_friend);
								else
									notification_textview.setText(R.string.splash_privacy_none);
							}
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get history notification id.
	 */
	private void getHistoryId () {
		new Thread (new Runnable () {
			public void run () {
				history_id = SplashManager.getHistoryNotificationId(username);
				FWNotificationManager.addUnnotifiedId(FWNotificationManager.UNNOTIFIED_CATEGORY_SPLASH, 
						  							  history_id, true);
			}
		}).start();
	}
	
	/**
	 * Get history.
	 */
	private void getHistory () {
		new Thread ( new Runnable () {
			public void run () {
				final SplashManager sm = new SplashManager();
				final ArrayList<Splash> splashes = sm.getHistoryWithUser(null, username);
				
				if (!isDestroyed()) {
					splash_listview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							history = splashes;
							more_history = sm.isMoreSplash();
							showHistory(true);
							
							// show keyboard if need
							if (keyboard_auto_show) {
								keyboard_auto_show = false;
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,0);
							}
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Get more history.
	 */
	private void getMoreHistory () {
		if (more_loading)
			return;
		more_loading = true;
		
		new Thread ( new Runnable () {
			public void run () {
				final SplashManager sm = new SplashManager();
				final ArrayList<Splash> splashes = sm.getHistoryWithUser(
													history.get(0).getId(), username);
				
				if (!isDestroyed()) {
					splash_listview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							if (!more_loading)
								return;
							
							history_position = splashes.size();
							history.addAll(0, splashes);
							more_history = sm.isMoreSplash();
							showHistory(false);
							
							more_loading = false;
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Send splash.
	 * @param message
	 */
	private void sendSplash (final String message) {
		// show loading progress dialog
		final ProgressDialog pd = ProgressDialog.show(this, "", getString(R.string.action_sending));
		new Thread (new Runnable() {
			public void run () {
				final boolean success = SplashManager.splashUser(username, message);

				if (!isDestroyed()) {
					splash_listview.post( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							pd.dismiss();
							if (success) {
								getHistory();
								splash_edittext.setText("");
							} else
								Toast.makeText(getApplicationContext(), R.string.splash_user_failed, Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Callback for splash clicked.
	 * @return
	 */
	private View.OnClickListener getSplashClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = splash_edittext.getText().toString();
				if (content.length() > 0)
					sendSplash(content);
				else
					Toast.makeText(getApplicationContext(), R.string.message_poster_no_content, Toast.LENGTH_SHORT).show();
			}
		};
	}

	@Override
	public void onMoreRequest() {
		getMoreHistory();
	}

	@Override
	public void onReceiveNotification(String id, String content) {
		if (history_id != null && id.equals(history_id))
			getHistory();
	}
	
	/**
	 * Splash Text Watcher class
	 * @author Kencool
	 *
	 */
	private class SplashTextWatcher implements TextWatcher {
		private String temp_text;
		
		@Override
		public void afterTextChanged(Editable s) {
			String content = splash_edittext.getText().toString();
			int bytes = content.getBytes().length;
			if (bytes > SPLASH_TEXT_MAX_BYTES) {
				splash_edittext.setText(temp_text);
				splash_edittext.setSelection(temp_text.length());
			} else
				temp_text = content;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			temp_text = splash_edittext.getText().toString();
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
		}
	}
}
