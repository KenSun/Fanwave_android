package com.wildmind.fanwave.activity;


import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.account.AccountManager.SignResult;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.activity.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends BaseActivity {
	
	private EditText email_edittext, password_edittext, confimation_edittext, name_edittext;
	private ProgressDialog progressDialog = null;
	private boolean is_logging = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.sign_up_activity);
		initUI();		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
				
		super.onStop();
	}
	
	
	private void initUI(){
		// back button
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		email_edittext = (EditText)findViewById(R.id.email_edittext);

		password_edittext = (EditText)findViewById(R.id.password_edittext);
		password_edittext.setOnFocusChangeListener(getFocusChangeListener ());

		confimation_edittext = (EditText)findViewById(R.id.confimation_edittext);
		confimation_edittext.setOnFocusChangeListener(getFocusChangeListener ());
		
		name_edittext = (EditText)findViewById(R.id.name_edittext);
		name_edittext.setOnFocusChangeListener(getFocusChangeListener ());
		name_edittext.addTextChangedListener(mTextWatcher);
		// sign up button
		Button sign_up_button = (Button)findViewById(R.id.sign_up_button);
		sign_up_button.setOnClickListener(getSighUpClickedListener());
	}

	private OnFocusChangeListener getFocusChangeListener (){
		return new OnFocusChangeListener (){

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				switch(v.getId()){

					case R.id.password_edittext:
						showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_password_hint));
						break;
						
					case R.id.confimation_edittext:
						showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_confirmation_hint));
						break;
						
					case R.id.name_edittext:
						if(!password_edittext.getText().toString().equals(confimation_edittext.getText().toString()))
							showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_err_password));
						
						showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_nickname_hint));
						break;
				}
			}

		};
	}
	
	private void showToast(String msr){
  		Toast.makeText(SignUpActivity.this, msr, Toast.LENGTH_SHORT).show();
	}
	private void SignUp(final String email,final  String password, final String nickname){
	 	progressDialog = new ProgressDialog(SignUpActivity.this);
		progressDialog.setMessage(getResources().getString(R.string.action_loading));
		progressDialog.show();
		new Thread ( new Runnable () {
			public void run () {
				final SignResult result = AccountManager.signUpFanwave(email, password, nickname);
				SignUpActivity.this.runOnUiThread( new Runnable () {
					public void run () {
						if(progressDialog!=null&&progressDialog.isShowing())
							progressDialog.dismiss();
						
						if(result == SignResult.success){
				      		showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_successfully));
				      		handleLoginProcess ();
						}else if(result == SignResult.incorrect){
				      		showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_err));
						}else {
				      		showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_failed));
						}
					}
				});
			}
		}).start();
	}
	
	private OnClickListener getSighUpClickedListener (){
		return new OnClickListener (){
			@Override
			public void onClick(View v) {
				
				 if(email_edittext.getText().toString().length() == 0){
		      		showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_err_email));
				}else if(password_edittext.getText().toString().length() == 0){
					showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_err_password_null));
				}else if(password_edittext.getText().toString().length() < 4){
		      		showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_err_password_length));
				}else if(!password_edittext.getText().toString().equals(confimation_edittext.getText().toString())){
					showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_err_password));
				}else if(name_edittext.getText().toString().length() == 0){
		      		showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_err_nickname_null));
				}else if(length(name_edittext.getText().toString()) < 4){
		      		showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_err_nickname));
				}else{
					SignUp(email_edittext.getText().toString(), password_edittext.getText().toString(), name_edittext.getText().toString());
				}
			}
		};
	}
    
	/**
	 * Handle login process.
	 */
	private void handleLoginProcess () {
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
		final ProgressDialog login_pd = ProgressDialog.show(SignUpActivity.this, "", 
										getResources().getString(R.string.login_submitting));
		new Thread( new Runnable () {
			public void run () {
				is_logging = true;
				SignResult result = AccountManager.loginFanwave(email_edittext.getText().toString(), 
															password_edittext.getText().toString());
				if (result == SignResult.success)
					result = AccountManager.loginXMPP() ? result : SignResult.failure;
				
				final SignResult _result = result;
				SignUpActivity.this.runOnUiThread (new Runnable () {
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
							
							new AlertDialog.Builder(SignUpActivity.this)
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
	
	public static double length(String value) {
		double valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
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
	
	
	TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart ;
        private int editEnd ;
        @Override
        public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                int arg3) {
            temp = s;
        }
       
        @Override
        public void onTextChanged(CharSequence s, int arg1, int arg2,
                int arg3) {
           
        }
       
        @Override
        public void afterTextChanged(Editable s) {
            editStart = name_edittext.getSelectionStart();
            editEnd = name_edittext.getSelectionEnd();
            if (length(temp.toString()) > 20) {
                s.delete(editStart-1, editEnd);
                int tempSelection = editStart;
                name_edittext.setText(s);
                name_edittext.setSelection(tempSelection);
            }
        }
    };
		
}
