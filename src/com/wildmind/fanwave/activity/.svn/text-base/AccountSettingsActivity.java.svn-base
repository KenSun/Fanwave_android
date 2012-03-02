package com.wildmind.fanwave.activity;

import com.wildmind.fanwave.activity.R;
import com.wildmind.fanwave.facebook.FacebookManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AccountSettingsActivity extends BaseActivity implements View.OnClickListener{
	private ImageView back_imagebutton;
	private ImageView icon_password_divider;
	private RelativeLayout username_relativelayout,password_relativelayout,sound_relativelayout,privacy_relativelayout,socialnetwork_relativelayout,tutorial_relativelayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
				
		setContentView(R.layout.account_settings_activity);
				
		initUI();
		EventListenerHandle();
		
		
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		back_imagebutton = null;
		
		username_relativelayout = null;
		password_relativelayout = null;
		sound_relativelayout = null;
		privacy_relativelayout = null;
		socialnetwork_relativelayout = null;
		tutorial_relativelayout = null;
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
		//Home Button
		back_imagebutton = (ImageView)findViewById(R.id.back_imagebutton);
		icon_password_divider = (ImageView)findViewById(R.id.icon_password_divider);
		username_relativelayout = (RelativeLayout)findViewById(R.id.icon_username_relativelayout);
		password_relativelayout = (RelativeLayout)findViewById(R.id.icon_password_relativelayout);
		if(FacebookManager.isFacebookAccount()){
			password_relativelayout.setVisibility(View.GONE);
			icon_password_divider.setVisibility(View.GONE);
		}
		sound_relativelayout = (RelativeLayout)findViewById(R.id.icon_sound_relativelayout);
		privacy_relativelayout = (RelativeLayout)findViewById(R.id.icon_privacy_relativelayout);
		socialnetwork_relativelayout = (RelativeLayout)findViewById(R.id.icon_socialnetwork_relativelayout);
		tutorial_relativelayout = (RelativeLayout)findViewById(R.id.icon_tutorial_relativelayout);
	}
	
	private void EventListenerHandle(){
		back_imagebutton.setOnClickListener(this);
		
		username_relativelayout.setOnClickListener(this);
		password_relativelayout.setOnClickListener(this);
		sound_relativelayout.setOnClickListener(this);
		privacy_relativelayout.setOnClickListener(this);
		socialnetwork_relativelayout.setOnClickListener(this);
		tutorial_relativelayout.setOnClickListener(this);
	}
	
	
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent mIntent=null;
		
		switch(v.getId()){
			case R.id.back_imagebutton:
				AccountSettingsActivity.this.finish();
				break;
				
			case R.id.icon_username_relativelayout:
				mIntent = new Intent(AccountSettingsActivity.this, AccountSettingsUsernameActivity.class);
				break;
			case R.id.icon_password_relativelayout:
				mIntent = new Intent(AccountSettingsActivity.this, AccountSettingsPasswordActivity.class);
				break;
			case R.id.icon_sound_relativelayout:
				mIntent = new Intent(AccountSettingsActivity.this, AccountSettingsSoundActivity.class);
				break;
			case R.id.icon_privacy_relativelayout:
				mIntent = new Intent(AccountSettingsActivity.this, AccountSettingsPrivacyActivity.class);
				break;
			case R.id.icon_socialnetwork_relativelayout:
				mIntent = new Intent(AccountSettingsActivity.this, AccountSettingsSocialNetworkActivity.class);
				break;
			case R.id.icon_tutorial_relativelayout:
				mIntent = new Intent(AccountSettingsActivity.this, AccountSettingsTutorialActivity.class);
				break;
			
		}
		
		if(mIntent!=null) startActivity(mIntent);
	}
	
	
}
