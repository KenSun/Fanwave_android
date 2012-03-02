package com.wildmind.fanwave.c2dm;

import com.urbanairship.push.PushManager;
import com.wildmind.fanwave.notification.FWNotificationManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class C2DMReceiver extends BroadcastReceiver {

	public static final String BROADCAST_C2DM_REGISTRATION = "com.google.android.c2dm.intent.REGISTRATION";
	public static final String BROADCAST_C2DM_RECEIVE = "com.google.android.c2dm.intent.RECEIVE";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (action.equals(BROADCAST_C2DM_REGISTRATION)) {
			
			if (intent.getStringExtra("error") != null) {
				// Registration failed, should try again later.
				C2DMManager.handleRegistrationError(intent.getStringExtra("error"));
			} else if (intent.getStringExtra("unregistered") != null) {
				// unregistration done, new messages from the authorized sender will be rejected
			} else if (intent.getStringExtra("registration_id") != null) {
				// Process registration with application server.  This should be done in a separate thread.
				C2DMManager.processRegistration(intent.getStringExtra("registration_id"));
			}
		} else if (action.equals(BROADCAST_C2DM_RECEIVE)) {
			
			String id = intent.getStringExtra("id");
			String content = intent.getStringExtra(PushManager.EXTRA_ALERT);
			FWNotificationManager.receiveNotification(id, content);
		}
	}
}
