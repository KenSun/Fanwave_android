package com.wildmind.fanwave.activity;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.account.AccountManager.SignResult;
import com.wildmind.fanwave.profile.AccountProfile;
import com.wildmind.fanwave.activity.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginFanwaveActivity extends BaseActivity	{

	// UI variables
	//
	private EditText email_edittext;
	private EditText password_edittext;
	
	private boolean is_logging = false;
	
	/**
	 *  Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.login_fanwave_activity);
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
	
	// Initialization Methods
	//
	private void initUI () {
		// back image button
		ImageButton back = (ImageButton) findViewById(R.id.back_imagebutton);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// email edit text
		email_edittext = (EditText) findViewById(R.id.email_edittext);
		AccountProfile ap = new AccountProfile(this);
		if (ap.getUsername().length() > 0)
			email_edittext.setText(ap.getUsername());
		
		// password edit text
		password_edittext = (EditText) findViewById(R.id.password_edittext);
		
		// sign in button
		Button sign_in_button = (Button) findViewById(R.id.sign_in_button);
		sign_in_button.setOnClickListener(getSignInButtonClickedListener());
		
		// forgot password
		ImageView forgot_imageview = (ImageView) findViewById(R.id.forgot_password_imageview);
		forgot_imageview.setOnClickListener(getForgotPasswordClickedListener());
	}
	
	/**
	 * Handle login process.
	 */
	private void processLogin () {
		String email = email_edittext.getText().toString();
		String password = password_edittext.getText().toString();
		
		// check if email or password missed
		//
		if (email.length() == 0) {
			Toast.makeText(this, R.string.login_email_required, Toast.LENGTH_SHORT).show();
			return;
		}
		if (password.length() == 0) {
			Toast.makeText(this, R.string.login_password_required, Toast.LENGTH_SHORT).show();
			return;
		}
		
		// show progress dialog and login
		//
		final ProgressDialog login_pd = ProgressDialog.show(LoginFanwaveActivity.this, "", 
										getResources().getString(R.string.login_submitting));
		new Thread( new Runnable () {
			public void run () {
				is_logging = true;
				SignResult result = AccountManager.loginFanwave(email_edittext.getText().toString(), 
															password_edittext.getText().toString());
				if (result == SignResult.success)
					result = AccountManager.loginXMPP() ? result : SignResult.failure;
				
				final SignResult _result = result;
				LoginFanwaveActivity.this.runOnUiThread (new Runnable () {
					public void run () {
						is_logging = false;
						
						login_pd.dismiss();
						if (_result == SignResult.success) {
							setResult(RESULT_OK);
							finish();
						} else {
							int msg_id = -1;
							if (_result == SignResult.incorrect)
								msg_id = R.string.login_wrong_data;
							else if (_result == SignResult.failure)
								msg_id = R.string.app_server_error;
							
							new AlertDialog.Builder(LoginFanwaveActivity.this)
											.setTitle(R.string.app_message)
											.setMessage(msg_id)
											.setNegativeButton(R.string.action_confirm, null)
											.show();
						}
					}
				});
			}
		}).start();
	}
	
	/**
	 * Request password.
	 * @param email
	 */
	private void requestPassword (final String email) {
		// show loading progress dialog
		final ProgressDialog pd = ProgressDialog.show(LoginFanwaveActivity.this, "", 
				  									  getResources().getString(R.string.action_sending));
		new Thread (new Runnable () {
			public void run () {
				final SignResult result = AccountManager.requestPassword(email);
				
				if (!isDestroyed()) {
					LoginFanwaveActivity.this.runOnUiThread( new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							pd.dismiss();
							
							int textId;
							if (result == SignResult.success)
								textId = R.string.login_send_password_success;
							else if (result == SignResult.incorrect)
								textId = R.string.login_send_password_invalid;
							else 
								textId = R.string.login_send_password_failure;
							
							new AlertDialog.Builder(LoginFanwaveActivity.this)
										   .setTitle(R.string.app_message)
										   .setMessage(textId)
										   .setNeutralButton(R.string.action_confirm, null)
										   .show();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Callback for sign in button clicked.
	 * @return
	 */
	private View.OnClickListener getSignInButtonClickedListener () {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processLogin();
			}
		};
	}
	
	/**
	 * Callback for forgot password button clicked.
	 * @return
	 */
	private View.OnClickListener getForgotPasswordClickedListener() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Email Edit Text
				final EditText emailEditText = new EditText(LoginFanwaveActivity.this);
				emailEditText.setTextSize(14);
				emailEditText.setText(AccountManager.getCurrentUser().getEmail());
				emailEditText.setInputType( InputType.TYPE_CLASS_TEXT | 
											InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );
				
				// Button
				DialogInterface.OnClickListener positive = new DialogInterface.OnClickListener () {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						requestPassword(emailEditText.getText().toString());
					}
				};
				
				// Dialog
				AlertDialog dialog = new AlertDialog.Builder(LoginFanwaveActivity.this)
				   .setTitle(R.string.login_email)
				   .setView(emailEditText)
				   .setPositiveButton(R.string.action_confirm, positive)
				   .setNegativeButton(R.string.action_cancel, null)
				   .create();
				dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				dialog.show();
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
		if (keyCode == KeyEvent.KEYCODE_BACK) { 
			if (is_logging)
				AccountManager.logout(); 
		}
		return super.onKeyDown(keyCode, event); 
	}
}
