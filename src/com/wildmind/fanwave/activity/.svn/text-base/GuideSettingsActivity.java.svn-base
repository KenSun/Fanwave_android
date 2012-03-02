package com.wildmind.fanwave.activity;

import com.wildmind.fanwave.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class GuideSettingsActivity extends BaseActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.guide_settings_activity);
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
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(getclicklistener ());
		
		RelativeLayout my_provider_relativelayout = (RelativeLayout)findViewById(R.id.my_provider_relativelayout);
		my_provider_relativelayout.setOnClickListener(getclicklistener ());

		RelativeLayout channel_reorder_relativelayout = (RelativeLayout)findViewById(R.id.channel_reorder_relativelayout);
		channel_reorder_relativelayout.setOnClickListener(getclicklistener ());

		RelativeLayout channel_hidder_relativelayout = (RelativeLayout)findViewById(R.id.channel_hidder_relativelayout);
		channel_hidder_relativelayout.setOnClickListener(getclicklistener ());
	}
	
	private OnClickListener getclicklistener (){
		return new OnClickListener (){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mIntent = null;
				switch(v.getId()){
					case R.id.back_imagebutton:
						GuideSettingsActivity.this.finish();
						break;
					case R.id.my_provider_relativelayout:
						mIntent = new Intent(GuideSettingsActivity.this, GuideSettingsProviderActivity.class);
						break;
					case R.id.channel_reorder_relativelayout:
						mIntent = new Intent(GuideSettingsActivity.this, GuideSettingsReorderActivity.class);
						break;
					case R.id.channel_hidder_relativelayout:
						mIntent = new Intent(GuideSettingsActivity.this, GuideSettingsHidderActivity.class);
						break;
				}
	
				 if(mIntent!=null) 
					 startActivity(mIntent);
			}
		};
	}
	
}
