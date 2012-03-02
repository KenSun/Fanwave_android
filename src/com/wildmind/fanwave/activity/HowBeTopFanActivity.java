package com.wildmind.fanwave.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class HowBeTopFanActivity extends BaseActivity  implements View.OnClickListener{
	private ImageView back_imagebutton;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.howtotopfan_activity);
		initData();
		initUI();
	}
	private void initData () {
		
	}
	
	private void initUI () {
		back_imagebutton = (ImageView)findViewById(R.id.back_imagebutton);
		back_imagebutton.setOnClickListener(this);
		

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub		
		switch(v.getId()){
			case R.id.back_imagebutton:
				HowBeTopFanActivity.this.finish();
				break;
		}
	}
	
	

	protected void onPause() {
		super.onPause();
		
	}

	
	
	
}
