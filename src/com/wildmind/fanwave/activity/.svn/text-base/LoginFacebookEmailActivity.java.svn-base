package com.wildmind.fanwave.activity;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.account.AccountManager.FacebookResult;
import com.wildmind.fanwave.facebook.FacebookManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFacebookEmailActivity extends BaseActivity {

	private EditText email_edittext;
	private Button submit_button;

	private ProgressDialog loading_indicator;

	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;

		setContentView(R.layout.login_facebook_email_activity);

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

		email_edittext = null;
		submit_button = null;

		if (loading_indicator != null)
			loading_indicator.dismiss();
		loading_indicator = null;
	}

	private void initUI() {
		// Email Edit Text
		email_edittext = (EditText) findViewById(R.id.email_edittext);

		// Submit
		submit_button = (Button) findViewById(R.id.submit_button);
		submit_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processEmailValidation();
			}
		});
	}

	/**
	 * Process email validation.
	 * 
	 * @param email
	 */
	private void processEmailValidation() {
		// show loading progress dialog
		loading_indicator = ProgressDialog.show(this, "", getString(R.string.action_setting));

		new Thread(new Runnable() {
			public void run() {
				final String email = email_edittext.getText().toString();
				final FacebookResult result = AccountManager.checkFacebookEmailValid(email);

				if (result == FacebookResult.success) {
					// email is valid, sign up email and login
					FacebookManager.setEmail(email);
					processAccountAuthentication(email);
				} else {
					if (!isDestroyed()) {
						submit_button.post(new Runnable() {
							public void run() {
								if (isDestroyed())
									return;

								loading_indicator.dismiss();
								
								int textId = result == FacebookResult.emailInvalid 
											? R.string.fb_sign_email_invalid 
											: R.string.fb_sign_email_fail; 
								Toast.makeText( LoginFacebookEmailActivity.this, textId, Toast.LENGTH_SHORT).show();
							}
						});
					}
				}
			}
		}).start();
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
			// login
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
			} else
				success = false;
		}
		
		final boolean facebookSuccess = success;
		if (!isDestroyed()) {
			LoginFacebookEmailActivity.this.runOnUiThread(new Runnable() {
				public void run () {
					if (isDestroyed())
						return;
					
					loading_indicator.dismiss();
					
					if (facebookSuccess) {
						setResult(RESULT_OK);
					} else {
						// login or sign up failed, show error message here
						Toast.makeText(LoginFacebookEmailActivity.this, R.string.app_server_error, 
										Toast.LENGTH_SHORT).show();
						setResult(RESULT_CANCELED);
					}
					finish();
				}
			});

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
			setResult(RESULT_CANCELED);
			finish();
			
			return true;
		}
		return super.onKeyDown(keyCode, event); 
	}
}
