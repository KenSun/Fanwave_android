package com.wildmind.fanwave.activity;

import java.util.concurrent.CopyOnWriteArrayList;

import com.wildmind.fanwave.account.AccountManager;
import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.badge.BadgeManager;
import com.wildmind.fanwave.badge.TVBadge;
import com.wildmind.fanwave.media.BadgeImageManager;
import com.wildmind.fanwave.tutroial.TutorialManager;
import com.wildmind.fanwave.xmpp.FWXMPPManager;
import com.wildmind.fanwave.xmpp.badge.FWXMPPBadgeManager.FWBadgeListener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

public class BaseActivity extends Activity {
	
	/**
	 * Current BaseActivity instance.
	 */
	private static BaseActivity _instance = null;
	
	/**
	 * BaseActivity receiver
	 */
	private static BaseReceiver base_receiver = null;
	
	/**
	 * Badge Player 
	 */
	private static Intent badge_player_intent = null;
	private static CopyOnWriteArrayList<BadgeRequest> badge_requests = new CopyOnWriteArrayList<BadgeRequest>();
	private static BadgeListener badge_listener = new BadgeListener();
	private static boolean badge_playing = false;
	
	/**
	 * Tutorial
	 */
	private static Intent tutorial_intent = null;
	
	private boolean ac_destroyed = false;
	
	/**
	 * Get current activity instance.
	 * @return
	 */
	public static BaseActivity getCurrentActivity () {
		return _instance;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
     	
		// if app works abnormally, just restart app from launching activity
		if (shouldRestartApp()) {
			Intent i = new Intent(this, LaunchingActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			finish();
			startActivity(i);
			ac_destroyed = true;
			return;
		}
		
		// set application context
     	ApplicationManager.setAppContext(getApplicationContext());
     	
     	ac_destroyed = false;
	}
	
	protected void onStart() {
		super.onStart();
		if(!TutorialManager.everShowTutorial(getClass().getName())){
     		if(TutorialManager.needToShowTutorial(getClass().getName()))
 				showTutorial(getClass().getName());
 		}
	}

	protected void onResume() {
		super.onResume();
		
		_instance = this;
		ApplicationManager.setLatestActivityName(this);
	}

	protected void onPause() {
		super.onPause();
		
		// if badge player is finished, process next if any
		if (BadgePlayerActivity.class.isInstance(_instance)) {
			badge_playing = false;
			processBadge();
		}
		
		_instance = null;
	}

	protected void onStop() {
		super.onStop();
	}
	
	protected void onDestroy() {
		super.onDestroy();
		
		ac_destroyed = true;
	}
	
	private boolean shouldRestartApp () {
		return !Main.class.isInstance(this) && 
			   !LaunchingActivity.class.isInstance(this) &&
			   ApplicationManager.getAppContext() == null;
	}
	
	/**
	 * Callback for title bar.
	 * @param v
	 */
	public void onTitleBarClick (View v) {
		if (_instance == null)
			return;
		
		v.setSelected(true);
		Intent i = new Intent(_instance, MainPageActivity.class);
		startActivity(i);
	}
	
	/**
	 * Check if activity is destroyed.
	 * @return
	 */
	public boolean isDestroyed() {
		return ac_destroyed;
	}
	
	/**
	 * Initialize receiver
	 */
	public static void initBaseReceiver () {
		base_receiver = new BaseReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(FWXMPPManager.BROADCAST_USER_WILL_CONNECT_XMPP);
		ApplicationManager.getAppContext().registerReceiver(base_receiver, intentFilter);
	}
	
	
	/**
	 * Badge Player Methods
	 */
	
	/**
	 * Start listening badge event.
	 */
	private static void listenBadge () {
		if (FWXMPPManager.getBadgeManager() != null)
			FWXMPPManager.getBadgeManager().addBadgeListener(badge_listener);
	}
	
	/**
	 * Show badge player.
	 * @param badge
	 * @param program_title	could be null if is not a program badge.
	 * @param auto_close
	 */
	protected void showBadgePlayer (TVBadge badge, String program_title, boolean wearable, boolean auto_close) {
		if (badge_player_intent == null) {
			badge_player_intent = new Intent(this, BadgePlayerActivity.class);
		}
		badge_player_intent.putExtra("badge", badge);
		badge_player_intent.putExtra("program_title", program_title != null ? program_title : "");
		badge_player_intent.putExtra("wearable", wearable);
		badge_player_intent.putExtra("auto_close", auto_close);
		
		startActivity(badge_player_intent);
	}
	
	/**
	 * Process badge request.
	 */
	private static void processBadge () {
		if (badge_playing || badge_requests.size() == 0)
			return;
		
		badge_playing = true;
		
		new Thread (new Runnable () {
			public void run () {
				final BadgeRequest br = badge_requests.remove(0);
				final TVBadge badge = BadgeManager.getBadge(br.badge_id);
				
				// prepare badge image
				if (!BadgeImageManager.isBadgeImageExistInStorage(br.badge_id))
					BadgeImageManager.downloadBadgeImage(br.badge_id);
				
				if (_instance != null) {
					_instance.runOnUiThread( new Runnable () {
						public void run () {
							// if badge is null, give up this badge playing and process next
							if (badge == null) {
								badge_playing = false;
								processBadge();
							} else {
								if (_instance != null) {
									boolean wearable = !br.badge_id.equals(AccountManager.getCurrentUser().getBadgeId()) &&
											   !br.badge_id.equals("topFan_1");
									_instance.showBadgePlayer(badge, br.program_title, wearable, true);
								}
							}
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Listener for listening badge event.
	 * @author Kencool
	 *
	 */
	private static class BadgeListener implements FWBadgeListener {

		@Override
		public void receiveBadge(String badge_id) {
			badge_requests.add(new BadgeRequest(badge_id, null));
			processBadge();
		}

		@Override
		public void receiveBadgeFromProgram(String badge_id, String programTitle) {
			badge_requests.add(new BadgeRequest(badge_id, programTitle));
			processBadge();
		}
	}
	
	/**
	 * BadgeRequest class
	 * @author Kencool
	 *
	 */
	private static class BadgeRequest {
		String badge_id = null;
		String program_title = null;
		
		public BadgeRequest (String badge_id, String program_title) {
			this.badge_id = badge_id;
			this.program_title = program_title;
		}
	}
	
	/**
	 * BaseReceiver class
	 * @author Kencool
	 *
	 */
	private static class BaseReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// get action
			String action = intent.getAction();
			
			if (action.equals(FWXMPPManager.BROADCAST_USER_WILL_CONNECT_XMPP)) {
				// listen badge message
				listenBadge();
			}
		}
	}
	
	
	/**
	 * Show tutorial.
	 * @param key.
	 */
	protected void showTutorial (String key) {
		if (tutorial_intent == null) {
			tutorial_intent = new Intent(this, TutorialActivity.class);
		}
		tutorial_intent.putExtra("class_name", key);
		startActivity(tutorial_intent);
	}
	
	/**
	 * Show tutorial.
	 * @param key
	 * @param acResult
	 */
	protected void showTutorial (String key, int acResult) {
		if (tutorial_intent == null) {
			tutorial_intent = new Intent(this, TutorialActivity.class);
		}
		tutorial_intent.putExtra("class_name", key);
		startActivityForResult(tutorial_intent, acResult);
	}
}
