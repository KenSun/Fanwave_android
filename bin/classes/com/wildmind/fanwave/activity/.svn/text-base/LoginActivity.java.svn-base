package com.wildmind.fanwave.activity;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.facebook.FacebookManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 * Intent input data "from_launching" : boolean
 * @author Kencool
 *
 */

public class LoginActivity extends BaseActivity {

	private final int LOGIN_FANWAVE_REQUEST  	= 0;
	private final int LOGIN_FACEBOOK_REQUEST 	= 1;
	private final int LOGIN_REGISTER_REQUEST 	= 2;
	
	private boolean from_launching = false;
	
	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if activity is destroyed due to abnormal situation
        if (isDestroyed())
			return;
        
        setContentView(R.layout.login_activity);
        
        initData();
        initUI();
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
	}
	
	private void initData () {
		Intent i = getIntent();
		from_launching = i.getBooleanExtra("from_launching", false);
	}
	
	private void initUI() {
		View.OnClickListener action_listener = getActionClickedListener();
		
		// Sign In Button
		Button signin = (Button) findViewById(R.id.login_fanwave_button);
		signin.setOnClickListener(action_listener);
		
		// Facebook Button
		Button facebook = (Button) findViewById(R.id.login_facebook_button);
		facebook.setOnClickListener(action_listener);
		
		// Sign Up Button
		Button signup = (Button) findViewById(R.id.login_register_button);
		signup.setOnClickListener(action_listener);
	}
	
	private View.OnClickListener getActionClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = null;
				switch (v.getId()) {
				case R.id.login_fanwave_button:
					i = new Intent(LoginActivity.this, LoginFanwaveActivity.class);
					startActivityForResult(i, LOGIN_FANWAVE_REQUEST);
					break;
					
				case R.id.login_facebook_button:
					FacebookDialogListener listener = new FacebookDialogListener();
					if(FacebookManager.login(LoginActivity.this, LOGIN_FACEBOOK_REQUEST, listener))
						listener.onComplete(null);
					break;
					
				case R.id.login_register_button:
					i = new Intent(LoginActivity.this, SignUpActivity.class);
					startActivityForResult(i, LOGIN_REGISTER_REQUEST);
					break;
				}
			}
		};
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case LOGIN_FANWAVE_REQUEST:
			if (resultCode == RESULT_OK && AccountManager.isLogin()) {
				if (from_launching) {
					setResult(RESULT_OK);
					finish();
				} else {
					Intent i = new Intent(LoginActivity.this, LaunchingActivity.class);
					i.putExtra("launching_state", LaunchingActivity.LAUNCHING_LOGIN_DONE);
					startActivity(i);
					finish();
				}
			}
			break;
			
		case LOGIN_FACEBOOK_REQUEST:
			FacebookManager.getFacebook().authorizeCallback(requestCode, resultCode, data);
			break;
			
		case LOGIN_REGISTER_REQUEST:
			if (resultCode == RESULT_OK && AccountManager.isLogin()) {
				if (from_launching) {
					setResult(RESULT_OK);
					finish();
				} else {
					Intent i = new Intent(LoginActivity.this, LaunchingActivity.class);
					i.putExtra("launching_state", LaunchingActivity.LAUNCHING_LOGIN_DONE);
					startActivity(i);
					finish();
				}
			}
			break;
		default:
			break;
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
			
			// Facebook dialog complete, leave Facebook login to LaunchingActivity
			if (from_launching) {
				setResult(RESULT_FIRST_USER);
				finish();
			} else {
				Intent i = new Intent(LoginActivity.this, LaunchingActivity.class);
				i.putExtra("launching_state", LaunchingActivity.LAUNCHING_FACEBOOK_DONE);
				startActivity(i);
				finish();
			}
		}
		@Override
		public void onError(DialogError error) {
			new AlertDialog.Builder(LoginActivity.this)
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
