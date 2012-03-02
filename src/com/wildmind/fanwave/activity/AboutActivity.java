package com.wildmind.fanwave.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends BaseActivity  implements View.OnClickListener{
	private ImageView back_imagebutton;
	private LinearLayout contact_us_linearlayout;
	private TextView version_textview;
	private String versionName, sdk, model;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);
		initData();
		initUI();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		back_imagebutton = null;
		
		contact_us_linearlayout = null;
		version_textview = null;
		versionName = null;
		sdk = null;
		model = null;
	}

	
	private void initData () {
		try {
			versionName =  this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     sdk = Build.VERSION.RELEASE;
	     model =Build.MODEL;
	}
	
	private void initUI () {
		back_imagebutton = (ImageView)findViewById(R.id.back_imagebutton);
		back_imagebutton.setOnClickListener(this);
		version_textview = (TextView) findViewById(R.id.version_textview);
		version_textview.setText("Version "+versionName);
		contact_us_linearlayout = (LinearLayout) findViewById(R.id.contact_us_linearlayout);
		contact_us_linearlayout.setOnClickListener(getContactbtnOnClick());

	}
	private OnClickListener getContactbtnOnClick (){
		return new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent mEmailIntent = new Intent(android.content.Intent.ACTION_SEND);  
		        
				mEmailIntent.setType("plain/text");
		        
		        String[] email={"support@fanwave.tv"};
		        
		        String title =  "Fanwave "
							      +"\t"
							      +getText(R.string.about_email_message_1)
							      +"Android v"
							      +versionName;
		        
		        String message = getText(R.string.about_email_message_2)
		        				  + model 
		        				  +"\t"
		        				  +"os:"
		        				  + sdk 
		        				  +getText(R.string.about_email_message_3) 
		        				  +model+getText(R.string.about_email_message_4);
		        				  
			    mEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, 	email);
			    mEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
		        mEmailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 	message);

		        AboutActivity.this.startActivity(Intent.createChooser(mEmailIntent, getResources().getString(R.string.about_email_message_Attention)));
			}
		};
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub		
		switch(v.getId()){
			case R.id.back_imagebutton:
				AboutActivity.this.finish();
				break;
		}
	}
	
	

	
}
