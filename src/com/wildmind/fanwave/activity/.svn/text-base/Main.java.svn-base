package com.wildmind.fanwave.activity;

import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.network.NetworkManager;
import com.wildmind.fanwave.util.StringGenerator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Main extends BaseActivity {

	/**
	 * Activity Life Cycle Methods
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
	}

	protected void onStart() {
		super.onStart();

		// shut down if service is close
		if (Double.valueOf(StringGenerator.getCurrentUTCTimeString()) >= Double.valueOf("20111231160000")) {
			DialogInterface.OnClickListener close = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			};
			
			new AlertDialog.Builder(this)
						   .setTitle(R.string.app_name)
						   .setMessage(R.string.app_shut_down)
						   .setNeutralButton(R.string.action_confirm, close)
						   .show();
			return;
		}
		
		// if internet available, starts up Fanwave, else close application
		if (NetworkManager.isInternetAvailable(this))
			startApp();
		else {
			new AlertDialog.Builder(this)
						   .setTitle(R.string.app_name)
						   .setMessage(R.string.app_close_no_internet)
						   .show();
			closeApp();
		}
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
	}

	private void startApp() {
		ApplicationManager.startApplication();
		
		final Intent i = new Intent(Main.this, LaunchingActivity.class);
		final Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.removeCallbacks(this);

				startActivity(i);
			}
		}, 1000);
	}

	private void closeApp() {
		final Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				mHandler.removeCallbacks(this);

				finish();
			}
		}, 3000);
	}
}
