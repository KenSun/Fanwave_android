package com.wildmind.fanwave.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.wildmind.fanwave.app.ApplicationManager;
import com.wildmind.fanwave.notification.FWNotificationManager;
import com.wildmind.fanwave.notification.NotificationAdapter;
import com.wildmind.fanwave.notification.NotificationListener;
import com.wildmind.fanwave.notification.SystemNotification;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationActivity extends BaseActivity implements NotificationListener, 
			OnItemClickListener {

	/** ----- Ranni Modify-----  */
	private final int TYPE_NOTIFICATION_FRIEND_REQ=1;
	private final int TYPE_NOTIFICATION_FRIEND_ACEPT=2;
	private final int TYPE_NOTIFICATION_SPLASH_TOPFAN=3;
	private final int TYPE_NOTIFICATION_SPLASH=4;
	private final int TYPE_NOTIFICATION_LIKE=5;
	private ArrayList<Map<String, String>> fakeGeneralNotifi;
	/** ----- Ranni Modify-----  */
	
	private LinearLayout	activity_layout, loading_indicator;
	private ListView		notif_listview;
    private TextView        descr_textview;
	
	private NotificationAdapter notif_adapter = null;
	
	private boolean notification_coming = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		if (isDestroyed())
			return;
		
		// we shouldn't create a notification activity while an old notification activity
		// already shows up
		if (ApplicationManager.getLatestActivityName().equals(getClass().getName())) {
			finish();
			return;
		}
		
		setContentView(R.layout.notification_activity);
		
		
        /** ----- Ranni Modify-----  */
		initFakeData();
        /** ----- Ranni Modify-----  */
		
		initUI();
		refreshNotification();
		
		// present activity in
		show();
	}
	
	protected void onStart() {
		super.onStart();
		
		// block notify system while user focuses on notification list
		//FWNotificationManager.blockNotify();
		
		// listen for new notification coming
		//FWNotificationManager.setListener(this);
		
		// refresh notifications
		refreshNotification();
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onStop() {
		super.onStop();
		
		// unblock notify
		//FWNotificationManager.unblockNotify();
	}
	
	protected void onDestroy() {
		super.onDestroy();
		
		activity_layout = null;
		loading_indicator = null;
		notif_listview = null;
		
		if (notif_adapter != null)
			notif_adapter.clear();
		notif_adapter = null;
        
		descr_textview = null;
	}
	
    /** ----- Ranni Modify-----  */
	private void initFakeData(){
		ArrayList<Map<String, String>> fakeGeneralNotifiTag = new ArrayList<Map<String, String>>();
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", ""+TYPE_NOTIFICATION_FRIEND_REQ);
		map.put("username","Dada");
		fakeGeneralNotifiTag.add(map);
		
		map = new HashMap<String, String>();
		map.put("type", ""+TYPE_NOTIFICATION_FRIEND_ACEPT);
		map.put("username","Didadi");
		fakeGeneralNotifiTag.add(map);
		
		map = new HashMap<String, String>();
		map.put("type", ""+TYPE_NOTIFICATION_SPLASH_TOPFAN);
		map.put("program","我可能不會愛你");
		map.put("username", "小默");
		map.put("content", "快一起來看節目，上傳雪肌精照片抽隨身碟！");
		fakeGeneralNotifiTag.add(map);
		
		map = new HashMap<String, String>();
		map.put("type", ""+TYPE_NOTIFICATION_LIKE);
		map.put("program","我可能不會愛你");
		map.put("username", "Dada");
		map.put("usercount", "10");
		map.put("content", "我覺得大仁哥一直都不把心中的我覺得大仁哥一直都不把心中的");
		fakeGeneralNotifiTag.add(map);
		
		map = new HashMap<String, String>();
		map.put("type", ""+TYPE_NOTIFICATION_SPLASH);
		map.put("username", "abcdefghijklmn金莎巧克力abcdefghijklmn");
		map.put("content", "我覺得大仁哥一直都不把心中的我覺得大仁哥一直都不把心中的");
		fakeGeneralNotifiTag.add(map);
		
		initGeneralsNotifi(fakeGeneralNotifiTag);
	}
	
	private String getUsernameHTMLFont(String username){
		return "<font color=\""+getResources().getColor(R.color.light_blue)+"\">"+username+"</font>";
	}
	
	private String getProgramHTMLFont(String program){
		return "<font color=\""+getResources().getColor(R.color.orange)+"\">"+program+"</font>";
	}
	
	private void initGeneralsNotifi(ArrayList<Map<String, String>> fakeGeneralNotifiTag){
		fakeGeneralNotifi = new ArrayList<Map<String, String>>();
		
		for(int i=0;i<fakeGeneralNotifiTag.size();i++){
			Map<String, String> mapItem = fakeGeneralNotifiTag.get(i);
			Map<String, String> mapResult = new HashMap<String, String>();
			
			switch(Integer.parseInt(mapItem.get("type"))){
				case TYPE_NOTIFICATION_FRIEND_REQ:
					mapResult.put("content", 
							getUsernameHTMLFont(mapItem.get("username"))
							+getString(R.string.notification_friend_request_constant)
							);
					break;
				case TYPE_NOTIFICATION_FRIEND_ACEPT:
					mapResult.put("content", 
							getUsernameHTMLFont(mapItem.get("username"))
							+getString(R.string.notification_friend_accept_constant)
							);
					break;
				case TYPE_NOTIFICATION_LIKE:
					String users = getUsernameHTMLFont(mapItem.get("username"))
									+((Integer.parseInt(mapItem.get("usercount"))>0)?
											getString(R.string.notification_like_and)+getUsernameHTMLFont(getString(R.string.notification_like_other)+mapItem.get("usercount")+getString(R.string.notification_like_people))
											:
											"");
					mapResult.put("content", 
							users
							+getString(R.string.notification_like_constant)
							+getProgramHTMLFont(mapItem.get("program"))
							+getString(R.string.notification_like_comment)
							+mapItem.get("content"));
					break;
				case TYPE_NOTIFICATION_SPLASH_TOPFAN:
					mapResult.put("content", 
							getProgramHTMLFont(mapItem.get("program"))
							+getString(R.string.topfan_be_on_top_topfan)
							+getUsernameHTMLFont(mapItem.get("username"))
							+getString(R.string.notification_splash_topfan_constant)
							+mapItem.get("content"));
					break;
				case TYPE_NOTIFICATION_SPLASH:
					mapResult.put("content", 
							getUsernameHTMLFont(mapItem.get("username"))
							+getString(R.string.notification_splash_constant)
							+mapItem.get("content"));
					break;
			}
			fakeGeneralNotifi.add(mapResult);
		}
	}
    /** ----- Ranni Modify -----  */
                
	
	private void initUI () {
		initTitleBarUI();
		initNotificationUI();
	}
	
	private void initTitleBarUI () {
		// Activity Layout
		activity_layout = (LinearLayout) findViewById(R.id.activity_layout);
				
		// Title Layout
		RelativeLayout title_layout = (RelativeLayout) findViewById(R.id.notification_layout);
		title_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	
	private void initNotificationUI () {
		// Notification List View
		notif_listview = (ListView) findViewById(R.id.notification_list_listview);
		notif_listview.setDivider(null);
		notif_listview.setDividerHeight(0);
		notif_listview.setOnItemClickListener(this);
		notif_adapter = new NotificationAdapter(notif_listview, null, this);
		notif_listview.setAdapter(notif_adapter);
		
		
		// Loading Indicator
		loading_indicator = (LinearLayout) findViewById(R.id.loading_indicator);
        
        // Description Text View
		descr_textview = (TextView) findViewById(R.id.descr_textview);
	}
	
	/**
	 * Show activity.
	 */
	private void show () {
		activity_layout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.notification_up));
	}
	
	/**
	 * Dismiss activity.
	 */
	private void dismiss () {
		Animation anim = AnimationUtils.loadAnimation(NotificationActivity.this, R.anim.notification_down);
		activity_layout.startAnimation(anim);
		activity_layout.postDelayed(new Runnable () {
			public void run () {
				if (!isDestroyed())
					finish();
			}
		}, anim.getDuration());
	}
	
	/**
	 * Refresh notification.
	 */
	private void refreshNotification () {
		showLoading();
        getNotification();
	}
	
	/**
	 * Show loading view.
	 */
	private void showLoading () {
		loading_indicator.setVisibility(View.VISIBLE);
		notif_listview.setVisibility(View.INVISIBLE);
        descr_textview.setVisibility(View.GONE);
	}
	
	/**
	 * Show notification list.
	 */
	private void showNotification (ArrayList<SystemNotification> systems) {
		loading_indicator.setVisibility(View.INVISIBLE);
		notif_listview.setVisibility(systems.size()==0?View.GONE:View.VISIBLE);
        descr_textview.setVisibility(systems.size()==0?View.VISIBLE:View.GONE);
		
        /** ----- Ranni Modify-----  */
		notif_adapter.refreshData(systems,fakeGeneralNotifi);
        /** ----- Ranni Modify-----  */
	}
                
    /**
    * Show Description.
    */
    private void showDescription () {
    	notif_listview.setVisibility(View.GONE);
        loading_indicator.setVisibility(View.GONE);
        descr_textview.setVisibility(View.VISIBLE);
    }
	
	/**
	 * get notification list.
	 */
	private void getNotification () {
		// since we're going to retrieve latest notification history, we could say there's no newer
		// notifications coming
		notification_coming = false;
		
		new Thread (new Runnable () {
			public void run () {
				// get notifications
				final ArrayList<SystemNotification> systems = FWNotificationManager.getSystemNotification();
				
				if (!isDestroyed()) {
					activity_layout.post(new Runnable () {
						public void run () {
							if (isDestroyed())
								return;
							
							// show notifications
							showNotification(systems);
							
							// check if new notification is waiting for retrieved
							if (notification_coming)
								getNotification();
						}
					});
				}
			}
		}).start();
	}
	
	/**
	 * Notification Listener Methods
	 * @param notification
	 */
	
	@Override
	public void onReceiveNotification(String id, String content) {
		notification_coming = true;
	}
	
	/**
	  * onKeyDown Touch Event Method
	  * @param keyCode
	  * @param event
	  * @return boolean
	  */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) { 
			dismiss();
			return true; 
		}
		return super.onKeyDown(keyCode, event); 
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		SystemNotification system = (SystemNotification) notif_adapter.getItem(position);
		
		if (system.getType().equals(SystemNotification.TYPE_LINK)) {
			Intent i = new Intent(this, WebViewActivity.class);
			i.putExtra("url", system.getLink());
			startActivity(i);
			
		} else if (system.getType().equals(SystemNotification.TYPE_PROGRAM)) {
			Intent i = new Intent(this, ProgramActivity.class);
			i.putExtra("program", system.getProgram());
			i.putExtra("back_image", BitmapFactory.decodeResource(getResources(), R.drawable.notification_icon));
			startActivity(i);
		}
	}
}
