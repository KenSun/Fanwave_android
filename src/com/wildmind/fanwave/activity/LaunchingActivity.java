package com.wildmind.fanwave.activity;

import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.account.AccountManager.FacebookResult;
import com.wildmind.fanwave.account.AccountManager.SignResult;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.reminder.ReminderManager;
import com.wildmind.fanwave.vendor.VendorManager;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.c2dm.C2DMManager;
import com.wildmind.fanwave.facebook.FacebookManager;
import com.wildmind.fanwave.follow.FollowManager;
import com.wildmind.fanwave.friend.FriendManager;
import com.wildmind.fanwave.guide.ChannelManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Intent input data "launching_state" : int
 * @author Kencool
 *
 */

public class LaunchingActivity extends BaseActivity {

	private final int LOGIN_ACTIVITY_REQUEST 	= 0;
	private final int VENDOR_ACTIVITY_REQUEST 	= 1;
	private final int LOGIN_FACEBOOK_REQUEST 	= 11;
	private final int FACEBOOK_EMAIL_REQUEST	= 12;
	
	/**
	 * Launching handler message constants.
	 */
	public static final int LAUNCHING_INIT_DONE		= 0;
	public static final int LAUNCHING_NETWORK_DONE 	= 1;
	public static final int LAUNCHING_DATABASE_DONE = 2;
	public static final int LAUNCHING_FACEBOOK_DONE	= 3;
	public static final int LAUNCHING_LOGIN_DONE 	= 4;
	public static final int LAUNCHING_REMINDER_DONE	= 5;
	public static final int LAUNCHING_EPG_DONE 		= 6;
	
	private TextView loading_percent_textview;
	private TextView loading_state_textview;
	private ImageView footprint_20;
	private ImageView footprint_40;
	private ImageView footprint_60;
	private ImageView footprint_80;
	private ImageView footprint_100;
	
	private LaunchingHandler launching_handler = new LaunchingHandler();
	private int launching_state = -1;
	
	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if activity is destroyed due to abnormal situation
        if (isDestroyed())
			return;
        
        setContentView(R.layout.launching_activity);
        
        initData();
        initUI();
        launchService();
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
		
		launching_handler = null;
		
		loading_percent_textview = null;
		loading_state_textview = null;
		
		footprint_20 = null;
		footprint_40 = null;
		footprint_60 = null;
		footprint_80 = null;
		footprint_100 = null;
	}
	
	private void initData () {
		Intent i = getIntent();
		launching_state = i.getIntExtra("launching_state", -1);
	}
	
	private void initUI () {
		footprint_20 = (ImageView) findViewById(R.id.footprint_20);
		footprint_40 = (ImageView) findViewById(R.id.footprint_40);
		footprint_60 = (ImageView) findViewById(R.id.footprint_60);
		footprint_80 = (ImageView) findViewById(R.id.footprint_80);
		footprint_100 = (ImageView) findViewById(R.id.footprint_100);
		
		loading_percent_textview = (TextView) findViewById(R.id.loading_anim_activity_percent);
		loading_state_textview = (TextView) findViewById(R.id.loading_anim_activity_state);
	}
	
	/**
	 * Forward progress with one step and refresh UI.
	 */
	private void refreshProgress () {
		// refresh footprints.
		refreshFootprint();
		
		// refresh percentage text.
		switch (launching_state) {
		case LAUNCHING_INIT_DONE:
			loading_percent_textview.setText("20%");
			break;
		case LAUNCHING_NETWORK_DONE:
			loading_percent_textview.setText("40%");
			break;
		case LAUNCHING_DATABASE_DONE:
			loading_percent_textview.setText("50%");
			break;
		case LAUNCHING_FACEBOOK_DONE:
			loading_percent_textview.setText("60%");
			break;
		case LAUNCHING_LOGIN_DONE:
			loading_percent_textview.setText("70%");
			break;
		case LAUNCHING_REMINDER_DONE:
			loading_percent_textview.setText("80%");
			break;
		case LAUNCHING_EPG_DONE:
			loading_percent_textview.setText("100%");
			break;
		default:
			break;
		}
	}
	
	/**
	 * Refresh footprints.
	 */
	private void refreshFootprint () {
		footprint_20.setVisibility(launching_state >= LAUNCHING_INIT_DONE ? View.VISIBLE : View.INVISIBLE);
		footprint_40.setVisibility(launching_state >= LAUNCHING_NETWORK_DONE ? View.VISIBLE : View.INVISIBLE);
		footprint_60.setVisibility(launching_state >= LAUNCHING_DATABASE_DONE ? View.VISIBLE : View.INVISIBLE);
		footprint_80.setVisibility(launching_state >= LAUNCHING_REMINDER_DONE ? View.VISIBLE : View.INVISIBLE);
		footprint_100.setVisibility(launching_state >= LAUNCHING_EPG_DONE ? View.VISIBLE : View.INVISIBLE);
	}
	
	/**
	 * Launch service. Initialize application working prerequisites.
	 */
	private void launchService () {
		if (launching_state != -1)
			refreshProgress();
		
		new Thread ( new Runnable () {
			public void run () {
				if (!isDestroyed()) {
					if (launching_state != -1)
						launching_handler.sendEmptyMessage(launching_state);
					else {
						// setup prerequisites
						initReceivers();
						
						if (!isDestroyed())
							launching_handler.sendEmptyMessage(LAUNCHING_INIT_DONE);
					}
				}
			}
		}).start();
	}
	
	/**
	 * Setup network service.
	 */
	private void loadNetworkService () {
		new Thread ( new Runnable () {
			public void run () {
				// download base url
				NetworkManager.monitorNetworkState();
				NetworkManager.downloadBaseUrls();
				
				if (!isDestroyed())
					launching_handler.sendEmptyMessage(LAUNCHING_NETWORK_DONE);
			}
		}).start();
	}
	
	/**
	 * Setup database service.
	 */
	private void loadDatabaseService () {
		new Thread ( new Runnable () {
			public void run () {
				if (!isDestroyed())
					launching_handler.sendEmptyMessage(LAUNCHING_DATABASE_DONE);
			}
		}).start();
	}
	
	/**
	 * Login Fanwave service.
	 */
	private void loadFanwaveService () {
		// if user is logout, just launch login activity
		if (!AccountManager.isLogin()) {
			openLoginActivity();
			return;
		}

		// auto login for user
		if (FacebookManager.isFacebookAccount()) 
			loginWithFacebookAccount();
		else {
			loginWithFanwaveAccount();
			
			// if link is invalid, unlink Facebook
			if (FacebookManager.isFacebookLink())
				FacebookManager.checkFacebookLink();
		}
	}
	
	/**
	 * Load reminder service.
	 */
	private void loadReminderService () {
		if (ReminderManager.areRemindersSync())
			launching_handler.sendEmptyMessage(LAUNCHING_REMINDER_DONE);
		else {
			DialogInterface.OnClickListener sync = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ReminderManager.syncReminders();
					if (!isDestroyed())
						launching_handler.sendEmptyMessage(LAUNCHING_REMINDER_DONE);
				}
			};
			DialogInterface.OnClickListener unsync = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (!isDestroyed())
						launching_handler.sendEmptyMessage(LAUNCHING_REMINDER_DONE);
				}
			};
			new AlertDialog.Builder(this)
						   .setTitle(R.string.reminder_title)
						   .setMessage(R.string.reminder_sync_description)
						   .setPositiveButton(R.string.action_confirm, sync)
						   .setNegativeButton(R.string.action_cancel, unsync)
						   .show();
		}
	}
	
	/**
	 * Load EPG service.
	 */
	private void loadEPGService () {
				
		new Thread (new Runnable () {
			public void run () {
				// load vendor settings
				ChannelManager.getUserMso(true);
				if (!VendorManager.isVendorSelected()) {
					LaunchingActivity.this.runOnUiThread ( new Runnable () {
						public void run () {
							openVendorActivity();
						}
					});
				}else {
                    if (!isDestroyed())
                        launching_handler.sendEmptyMessage(LAUNCHING_EPG_DONE);
					ChannelManager.getChannelList();
				}	
			}
		}).start();
	}
	
	/**
	 * Enter service.
	 */
	private void enterService () {
		new Thread ( new Runnable () {
			public void run () {
				
				LaunchingActivity.this.runOnUiThread ( new Runnable () {
					public void run () {
						openMainPageActivity();
					}
				});
			}
		}).start();
	}
	
	/**
	 * Initialize application receivers.
	 */
	private void initReceivers () {
		BaseActivity.initBaseReceiver();
		C2DMManager.initReceiver();
		FriendManager.initReceiver();
		FollowManager.initReceiver();
		ChannelManager.initReceiver();
		VendorManager.initReceiver();
	}
	
	/**
	 * Login with Fanwave account.
	 */
	private void loginWithFanwaveAccount () {
		new Thread (new Runnable () {
			public void run () {
				final SignResult result = AccountManager.login();
				
				if (!isDestroyed()) {
					if (result == SignResult.success) 
						launching_handler.sendEmptyMessage(LAUNCHING_LOGIN_DONE);		
					else {
						LaunchingActivity.this.runOnUiThread (new Runnable () {
							public void run () {
								if (isDestroyed())
									return;
								
								// open login activity
								openLoginActivity();
							}
						});
					}
				}
			}
		}).start();
	}
	
	/**
	 * Login with Facebook account.
	 */
	private void loginWithFacebookAccount () {
		FacebookDialogListener listener = new FacebookDialogListener();
		if (FacebookManager.login(this, LOGIN_FACEBOOK_REQUEST, listener))
			listener.onComplete(null);
	}
	
	/**
	 * Load Facebook service.
	 */
	private void loadFacebookService () {
		new Thread (new Runnable () {
			public void run () {
				// login Fanwave
				final FacebookResult fbResult = AccountManager.loginFanwaveWithFacebook (
																FacebookManager.getEmail(), 
																FacebookManager.getName(),
																FacebookManager.getUid(),
																FacebookManager.getAccessToken());
				if (fbResult == FacebookResult.success) {
					// login success
					AccountManager.loginXMPP(); 
					if (!isDestroyed())
						launching_handler.sendEmptyMessage(LAUNCHING_LOGIN_DONE);
					
				} else if (fbResult == FacebookResult.expire) {
					// logout Facebook and login again to retrieve new access token
					FacebookManager.logout();
					loginWithFacebookAccount();
					
				} else if (fbResult == FacebookResult.firstLogin) {
					processFacebookFirstLogin();
					
				} else {
					LaunchingActivity.this.runOnUiThread (new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							// login failed, show error message here
							Toast.makeText( LaunchingActivity.this, R.string.app_server_error, 
											Toast.LENGTH_SHORT).show();
							// open login activity
							openLoginActivity();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Process Facebook account first login.
	 */
	private void processFacebookFirstLogin () {
		String email = FacebookManager.getEmail();
		final FacebookResult result = AccountManager.checkFacebookEmailValid(email);
		
		if (result == FacebookResult.success) {
			// email is valid, sign up email and login
			processAccountAuthentication(email);
		} else {
			if (!isDestroyed()) {
				LaunchingActivity.this.runOnUiThread (new Runnable() {
					public void run() {
						if (isDestroyed())
							return;
						// failed to validate email, open email validation activity
						Intent i = new Intent(LaunchingActivity.this, LoginFacebookEmailActivity.class);
						startActivityForResult(i, FACEBOOK_EMAIL_REQUEST);
					}
				});
			}
		}
	}
	
	/**
	 * Process Facebook account sign up and login.
	 * @param email
	 */
	private void processAccountAuthentication(final String email) {
		// sign up
		boolean success = AccountManager.signUpForFacebookAccount(email, 
																  FacebookManager.getName(), 
																  FacebookManager.getUid(),
																  FacebookManager.getAccessToken());
		if (success) {
			// login Fanwave
			final FacebookResult fbResult = AccountManager.loginFanwaveWithFacebook(email, 
																					FacebookManager.getName(),
																					FacebookManager.getUid(),
																					FacebookManager.getAccessToken());
			if (isDestroyed())
				return;

			if (fbResult == FacebookResult.success) {
				// login success
				if (AccountManager.isFirstLogin()) {
					// sign up successfully, upload avatar
					new Thread(new Runnable() {
						public void run() {
							FacebookManager.getUserPicture();
						}
					}).start();
				}
				// login XMPP
				AccountManager.loginXMPP();
				if (!isDestroyed())
					launching_handler.sendEmptyMessage(LAUNCHING_LOGIN_DONE);
			} else
				success = false;
		}
		
		if (!success) {
			LaunchingActivity.this.runOnUiThread (new Runnable() {
				public void run() {
					if (isDestroyed())
						return;
					
					// login or sign up failed, show error message here
					Toast.makeText(LaunchingActivity.this, R.string.app_server_error, 
							Toast.LENGTH_SHORT).show();
					// open login activity
					openLoginActivity();
				}
			});
		}
	}
	
	/**
	 * Open login activity.
	 */
	private void openLoginActivity () {
		Intent i = new Intent(LaunchingActivity.this, LoginActivity.class);
		i.putExtra("from_launching", true);
		startActivityForResult(i, LOGIN_ACTIVITY_REQUEST);
	}
	
	/**
	 * Open vendor activity.
	 */
	private void openVendorActivity () {
		Intent i = new Intent(LaunchingActivity.this, WizardActivity.class);
		startActivityForResult(i, VENDOR_ACTIVITY_REQUEST);
	}
	
	/**
	 * Open main page activity.
	 */
	private void openMainPageActivity () {
		Intent i = new Intent(LaunchingActivity.this, MainPageActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(i);
		finish();
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case LOGIN_ACTIVITY_REQUEST:
				launching_handler.sendEmptyMessage(LAUNCHING_LOGIN_DONE);
				break;
			case VENDOR_ACTIVITY_REQUEST:
				launching_handler.sendEmptyMessage(LAUNCHING_EPG_DONE);
				break;
			case LOGIN_FACEBOOK_REQUEST:
				FacebookManager.getFacebook().authorizeCallback(requestCode, resultCode, data);
				break;
			case FACEBOOK_EMAIL_REQUEST:
				launching_handler.sendEmptyMessage(LAUNCHING_LOGIN_DONE);
				break;
			default:
				break;
			}
		} else if (resultCode == RESULT_FIRST_USER) {
			switch (requestCode) {
			case LOGIN_ACTIVITY_REQUEST:
				launching_handler.sendEmptyMessage(LAUNCHING_FACEBOOK_DONE);
				break;
			default:
				break;
			}
		} else if (resultCode == RESULT_CANCELED) {
			switch (requestCode) {
			case FACEBOOK_EMAIL_REQUEST:
				openLoginActivity();
				break;
			}
		}
	}
	
	/**
	 * Launching tasks handler.
	 * @author Kencool
	 *
	 */
	private class LaunchingHandler extends Handler {
		@Override
		public void handleMessage (Message message) {
			super.handleMessage(message);
			
			switch (message.what) {
			case LAUNCHING_INIT_DONE:
				launching_state = LAUNCHING_INIT_DONE;
				refreshProgress();
				loadNetworkService();
				break;
			case LAUNCHING_NETWORK_DONE:
				launching_state = LAUNCHING_NETWORK_DONE;
				refreshProgress();
				loadDatabaseService();
				break;
			case LAUNCHING_DATABASE_DONE:
				launching_state = LAUNCHING_DATABASE_DONE;
				refreshProgress();
				loadFanwaveService();
				break;
			case LAUNCHING_FACEBOOK_DONE:
				launching_state = LAUNCHING_FACEBOOK_DONE;
				refreshProgress();
				loadFacebookService();
				break;
			case LAUNCHING_LOGIN_DONE:
				launching_state = LAUNCHING_LOGIN_DONE;
				refreshProgress();
				loadReminderService();
				break;
			case LAUNCHING_REMINDER_DONE:
				launching_state = LAUNCHING_REMINDER_DONE;
				refreshProgress();
				loadEPGService();
				break;
			case LAUNCHING_EPG_DONE:
				launching_state = LAUNCHING_EPG_DONE;
				refreshProgress();
				enterService();
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Listener for listening Facebook dialog.
	 * @author Kencool
	 *
	 */
	private class FacebookDialogListener implements DialogListener {
		@Override
		public void onCancel() {
		}
		@Override
		public void onComplete(Bundle bundle) {
			FacebookManager.loadFacebookService();
			launching_handler.sendEmptyMessage(LAUNCHING_FACEBOOK_DONE);
		}
		@Override
		public void onError(DialogError error) {
			new AlertDialog.Builder(LaunchingActivity.this)
						   .setTitle("Facebook Error")
						   .setMessage(error.getMessage())
						   .setNeutralButton(R.string.action_confirm, null)
						   .show();
		}
		@Override
		public void onFacebookError(FacebookError facebookError) {
		}
	}
	
	/**
	  * onKeyDown Touch Event Method
	  * @param keyCode
	  * @param event
	  * @return boolean
	  */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ApplicationManager.stopApplication();
			
			return true;
		}
		return super.onKeyDown(keyCode, event); 
	}
}
