package com.wildmind.fanwave.activity;

import com.wildmind.fanwave.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

/**
 * Intent input data "url" : String
 * @author Kencool
 *
 */

public class WebViewActivity extends BaseActivity {

	private WebView 		webview;
	private LinearLayout 	loading_indicator;

	private String url = "";

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
		
		setContentView(R.layout.webview_activity);
		
		initData();
		initUI();
	}

	protected void onStart() {
		super.onStart();
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
	
		webview = null;
		loading_indicator = null;
		
		url = null;
	}
	
	private void initData() {
		Intent i = getIntent();
		this.url = (String) i.getStringExtra("url");
	}

	private void initUI() {
		// Web View
		webview = (WebView) findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setVerticalScrollbarOverlay(true);
		webview.loadUrl(this.url);
		
		webview.setWebViewClient(new WebViewClient () {
			@Override
			public void onPageFinished (WebView web, String url) {
				if (!isDestroyed()) {
					if (loading_indicator.getVisibility() == View.VISIBLE)
						loading_indicator.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
	}

	/**
	 * onKeyDown Touch Event Methods
	 * @author Kencool
	 * 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
