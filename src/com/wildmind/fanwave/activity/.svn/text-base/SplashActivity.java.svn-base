package com.wildmind.fanwave.activity;

import java.util.ArrayList;

import com.wildmind.fanwave.splash.Splash;
import com.wildmind.fanwave.splash.SplashListAdapter;
import com.wildmind.fanwave.splash.SplashManager;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SplashActivity extends BaseActivity implements OnItemClickListener {

	private ListView		splash_listview;
	private LinearLayout	loading_indicator;
	private TextView		descr_textview;
	
	private SplashListAdapter splash_adapter = null;
	
	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if activity is destroyed due to abnormal situation
        if (isDestroyed())
			return;
        
        setContentView(R.layout.splash_activity);
        
        initData();
        initUI();
    }
	
	protected void onStart() {
		super.onStart();
		
		refreshSplashes();
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
		
		if (splash_adapter != null)
			splash_adapter.clear();
		splash_adapter = null;
		
		splash_listview = null;
		loading_indicator = null;
		descr_textview  = null;
	}
	
	private void initData () {
		
	}
	
	private void initUI () {
		// Back Button
		ImageButton back_button = (ImageButton) findViewById(R.id.back_imagebutton);
		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		// Next Button
		ImageButton next_button = (ImageButton) findViewById(R.id.next_imagebutton);
		next_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SplashActivity.this, SplashSearchActivity.class);
				startActivity(i);
			}
		});
		
		// Splash List View
		splash_listview = (ListView) findViewById(R.id.splash_list_listview);
		splash_listview.setDivider(null);
		splash_listview.setDividerHeight(0);
		splash_listview.setOnItemClickListener(this);
		splash_adapter = new SplashListAdapter(splash_listview, null, this);
		splash_listview.setAdapter(splash_adapter);
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
		
		// Description Text View
		descr_textview = (TextView) findViewById(R.id.descr_textview);
	}
	
	/**
	 * Refresh splashes.
	 */
	private void refreshSplashes () {
		showSplashLoading();
		getSplashes();
	}
	
	/**
	 * Show splash list view.
	 */
	private void showSplashList () {
		splash_listview.setVisibility(View.VISIBLE);
		loading_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show splash loading.
	 */
	private void showSplashLoading () {
		splash_listview.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.VISIBLE);
		descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show Description.
	 */
	private void showDescription () {
		splash_listview.setVisibility(View.GONE);
		loading_indicator.setVisibility(View.GONE);
		descr_textview.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Show splashes.
	 * @param splashes
	 */
	private void showSplashes (ArrayList<Splash> splashes) {
		if (splashes == null || splashes.size() == 0)
			showDescription();
		else {
			showSplashList();
			splash_adapter.refreshData(splashes);
		}
	}
	
	/**
	 * Get splashes.
	 */
	private void getSplashes () {
		new Thread (new Runnable () {
			public void run () {
				final ArrayList<Splash> splashes = SplashManager.getSplashList();
				
				if (isDestroyed())
					return;
				
				splash_listview.post( new Runnable () {
					public void run () {
						if (isDestroyed())
							return;
						
						showSplashes(splashes);
					}
				});
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> listview, View view, int position, long id) {
		Splash splash = (Splash) splash_adapter.getItem(position);
		Intent i = new Intent(SplashActivity.this, SplashPrivateActivity.class);
		i.putExtra("username", splash.getSender());
		i.putExtra("nickname", splash.getNickname());
		i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.splash_icon));
		startActivity(i);
	}
}
