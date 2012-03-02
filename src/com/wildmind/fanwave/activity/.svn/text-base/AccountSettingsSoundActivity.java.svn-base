package com.wildmind.fanwave.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.profile.SoundProfile;
import com.wildmind.fanwave.activity.R;

public class AccountSettingsSoundActivity extends BaseActivity implements View.OnClickListener{
	private ImageView back_imagebutton;
	private ToggleButton notification_toggle_button, splash_toggle_button, gaincredits_toggle_button;
	private boolean notification, splash, gaincredits;
	private SoundProfile sp = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.account_settings_sound_activity);
		initData();
		initUI();
		EventListenerHandle();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		back_imagebutton = null;
		notification_toggle_button = null; 
		splash_toggle_button = null;
		gaincredits_toggle_button = null;
		sp = null;
	}
	
	private void initData(){	
		 sp = new SoundProfile(ApplicationManager.getAppContext());
		 notification = sp.isNotificationOn();
		 gaincredits  = sp.isCreditOn();
	}
	
	
	private void initUI(){
		//Home Button
		back_imagebutton 	= (ImageView)findViewById(R.id.back_imagebutton);
		notification_toggle_button  = (ToggleButton)findViewById(R.id.notification_toggle_button);
		splash_toggle_button 		= (ToggleButton)findViewById(R.id.splash_toggle_button);
		gaincredits_toggle_button 	= (ToggleButton)findViewById(R.id.gaincredits_toggle_button);
		notification_toggle_button	.setChecked(notification);
		splash_toggle_button		.setChecked(splash);
		gaincredits_toggle_button	.setChecked(gaincredits);
		
	}
	
	private void EventListenerHandle(){
		back_imagebutton			.setOnClickListener(this);
		notification_toggle_button	.setOnCheckedChangeListener(getCheckedChange());
		splash_toggle_button		.setOnCheckedChangeListener(getCheckedChange());
		gaincredits_toggle_button	.setOnCheckedChangeListener(getCheckedChange());

		
	}
	
	public OnCheckedChangeListener getCheckedChange(){
		return new OnCheckedChangeListener() { 
	
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				switch(buttonView.getId()){
					case R.id.notification_toggle_button:
						notification = isChecked;
						sp.setNotification(notification);
						break;							
					case R.id.gaincredits_toggle_button:
						gaincredits = isChecked;
						sp.setGainCredit(gaincredits);
						break;
				}
			}
	    };
	}
	
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId()){
			case R.id.back_imagebutton:
				AccountSettingsSoundActivity.this.finish();
				break;
				
			
			
		}
	}

}
