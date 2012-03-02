package com.wildmind.fanwave.activity;


import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.tutroial.TutorialManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AccountSettingsTutorialActivity extends BaseActivity implements View.OnClickListener{
	private ImageView back_imagebutton;
	private Button initial_tutorial_button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.account_settings_tutorial_activity);
		initData();
		initUI();
		EventListenerHandle();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		back_imagebutton = null;
		initial_tutorial_button = null;
	}

	private void initData(){	
		 
	}
	
	private void initUI(){
		//Home Button
		back_imagebutton 	= (ImageView)findViewById(R.id.back_imagebutton);
		initial_tutorial_button 		= (Button)findViewById(R.id.initial_tutorial_button);
	}
	
	private void EventListenerHandle(){
		back_imagebutton.setOnClickListener(this);
		initial_tutorial_button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.back_imagebutton:
				AccountSettingsTutorialActivity.this.finish();
				break;
			case R.id.initial_tutorial_button:
				ResetTutorial();
				break;
		}
	}
	
	private void ResetTutorial(){
		 for(String className :TutorialManager.TutorialList)
		 	getSharedPreferences("Tutorial", 0).edit().putBoolean(className, false).commit();
		 	
	  	Toast.makeText(this, ApplicationManager.getAppContext().getResources().getString(R.string.main_page_account_settings_tutorial_resetok), Toast.LENGTH_SHORT).show();
	}
}
