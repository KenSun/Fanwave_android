package com.wildmind.fanwave.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wildmind.fanwave.animation.DivergenceAnimation;
import com.wildmind.fanwave.badge.TVBadge;
import com.wildmind.fanwave.media.ImageManager;
import com.wildmind.fanwave.activity.R;

/**
 * Intent input data "program_title" : String
 * 					 "badge"		 : TVBadge
 * 					 "wearable"		 : boolean
 * 					 "auto_close"	 : boolean
 * @author Kencool
 *
 */

public class BadgePlayerActivity extends BaseActivity {
	
	public RelativeLayout	activity_layout;
	public ImageView		ray_imageview;
	public ImageView		badge_imageview;
	public TextView			badge_title_textview;
	public TextView			description_textview;
	
	private String	program_title = null;
	private TVBadge badge = null;
	//private boolean wearable = false;
	private boolean	auto_close = false;
	
	/**
	 * Activity Life Cycle Methods
	 * @author Kencool
	 * 
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// check if activity is destroyed due to abnormal situation
		if (isDestroyed())
			return;
		
		setContentView(R.layout.badge_player_activity);
		
		initData();
		initUI();
	}

	protected void onStart() {
		super.onStart();
		
		if (auto_close)
			autoClose();
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

		if (ray_imageview != null)
			stopRayRotating();
		ray_imageview = null;
		
		activity_layout = null;
		badge_imageview = null;
		badge_title_textview = null;
		description_textview = null;
		
		program_title = null;
		badge = null;
	}
	
	private void initData () {
		Intent i = getIntent();
		program_title = i.getStringExtra("program_title");
		badge = (TVBadge) i.getParcelableExtra("badge");
		//wearable = i.getBooleanExtra("wearable", false);
		auto_close = i.getBooleanExtra("auto_close", false);
	}
	
	private void initUI () {
		// Ray Image
		ray_imageview = (ImageView) findViewById(R.id.ray_imageview);
		startRayRotating();
		
		// Badge Image
		badge_imageview = (ImageView) findViewById(R.id.badge_imageview);
		ImageManager.drawBadgeImage(badge_imageview, badge.getId(), false);
		
		// Badge Title
		badge_title_textview = (TextView) findViewById(R.id.badge_title_textview);
		badge_title_textview.setText(badge.getTitle());
		
		// Description
		description_textview = (TextView) findViewById(R.id.description_textview);
		description_textview.setText(badge.getDescription());
		
		
		// Program Title
		TextView title = (TextView) findViewById(R.id.program_title_textview);
		if (program_title.length() > 0)
			title.setText("in " + program_title);
		else
			title.setVisibility(View.GONE);
		
		// Activity Layout
		activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
		activity_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		activity_layout.startAnimation(new DivergenceAnimation(0.1f, 1, false));
	}

	public void startRayRotating () {
		ray_imageview.clearAnimation();
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.rotate_infinite);
		ray_imageview.startAnimation(anim);
	}
	
	public void stopRayRotating () {
		ray_imageview.clearAnimation();
	}
	
	private void autoClose () {
		ray_imageview.postDelayed( new Runnable () {
			public void run () {
				if (!isDestroyed())
					close();
			}
		}, 5000);
	}
	
	private void close () {
		Animation fade_out = AnimationUtils.loadAnimation(BadgePlayerActivity.this, android.R.anim.fade_out);
		fade_out.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				finish();
			}
			@Override
			public void onAnimationRepeat(Animation animation) {

			}
			@Override
			public void onAnimationStart(Animation animation) {

			}
		});
		activity_layout.startAnimation(fade_out);
	}
	
	
}
