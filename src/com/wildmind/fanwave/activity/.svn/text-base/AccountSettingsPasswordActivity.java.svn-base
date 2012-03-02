package com.wildmind.fanwave.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.activity.R;

public class AccountSettingsPasswordActivity extends BaseActivity implements View.OnClickListener{
	private ImageView back_imagebutton;
	private Button pwd_update_button;
	private EditText  password_edittext, new_password_edittext, confimation_edittext;
	private ProgressDialog progressDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
				
		setContentView(R.layout.account_settings_password_activity);
		initUI();
		EventListenerHandle();
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		back_imagebutton = null;
		pwd_update_button = null;
		password_edittext = null;
		new_password_edittext = null;
		confimation_edittext = null;
		progressDialog = null;
	}
	
	
	private void initUI(){
		//Home Button
		back_imagebutton = (ImageView)findViewById(R.id.back_imagebutton);
		pwd_update_button = (Button)findViewById(R.id.pwd_update_button);
		password_edittext = (EditText)findViewById(R.id.password_edittext);
		password_edittext.setOnFocusChangeListener(getFocusChangeListener ());
		new_password_edittext = (EditText)findViewById(R.id.new_password_edittext);
		new_password_edittext.setOnFocusChangeListener(getFocusChangeListener ());
		confimation_edittext = (EditText)findViewById(R.id.confimation_edittext);
		confimation_edittext.setOnFocusChangeListener(getFocusChangeListener ());
		
		
	}
	
	private void EventListenerHandle(){
		back_imagebutton.setOnClickListener(this);
		pwd_update_button.setOnClickListener(this);
		
	}
	
	private OnFocusChangeListener getFocusChangeListener (){
		return new OnFocusChangeListener (){

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus)
				switch(v.getId()){
					
					case R.id.new_password_edittext:
						showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_password_hint));
						break;
						
					case R.id.confimation_edittext:
						showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_confirmation_hint));
						break;
						
				}
			}

		};
	}
	
	private void showToast(String msr){
  		Toast.makeText(AccountSettingsPasswordActivity.this, msr, Toast.LENGTH_SHORT).show();
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
			case R.id.back_imagebutton:
				AccountSettingsPasswordActivity.this.finish();
				break;
			case R.id.pwd_update_button:
				if(password_edittext.getText().toString().length() < 4 ||new_password_edittext.getText().toString().length() < 4 ||new_password_edittext.getText().toString().length() < 4)
					showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_err_password_length));
				else if(!password_edittext.getText().toString().equals(AccountManager.getCurrentUser().getPassword()))
					showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_err_password_match));
				else if(new_password_edittext.getText().toString().equals("")||confimation_edittext.getText().toString().equals(""))
					showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_confirmation_hint));
				else if(!new_password_edittext.getText().toString().equals(confimation_edittext.getText().toString()))
					showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_err_new_password_match));
				else
					updatePassword(new_password_edittext.getText().toString());

				break;

		}
	}
	private void updatePassword(final  String password){
	 	progressDialog = new ProgressDialog(AccountSettingsPasswordActivity.this);
		progressDialog.setMessage(getResources().getString(R.string.action_loading));
		progressDialog.show();
		new Thread ( new Runnable () {
			public void run () {
				final boolean recode = AccountManager.updatePassword(password);
				AccountSettingsPasswordActivity.this.runOnUiThread( new Runnable () {
					public void run () {
						if(progressDialog!=null&&progressDialog.isShowing())
							progressDialog.dismiss();
						
						if(recode){
				      		showToast(ApplicationManager.getAppContext().getResources().getString(R.string.action_update_successfully));
				      		finish();
						}else {
				      		showToast(ApplicationManager.getAppContext().getResources().getString(R.string.sign_up_failed));
						}
					}
				});
			}
		}).start();
	}
	
	
}
