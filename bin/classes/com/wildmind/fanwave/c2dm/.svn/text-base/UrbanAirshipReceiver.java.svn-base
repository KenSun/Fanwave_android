package com.wildmind.fanwave.c2dm;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;
import com.wildmind.fanwave.activity.BaseActivity;
import com.wildmind.fanwave.activity.Main;
import com.wildmind.fanwave.activity.NotificationActivity;

public class UrbanAirshipReceiver extends BroadcastReceiver {

	private static final String logTag = "UrbanAirShipReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (action.equals(PushManager.ACTION_PUSH_RECEIVED)) {
		    int id = intent.getIntExtra(PushManager.EXTRA_NOTIFICATION_ID, 0);
		    logPushExtras(intent);

		} else if (action.equals(PushManager.ACTION_NOTIFICATION_OPENED)) {
			logPushExtras(intent);

            if (BaseActivity.getCurrentActivity() != null) {
            	// application is forground, show notification list
            	Intent i = new Intent(context, NotificationActivity.class);
            	BaseActivity.getCurrentActivity().startActivity(i);
            } else {
            	// application is background, start up application
            	Intent launch = new Intent(Intent.ACTION_MAIN);
    			launch.setClass(UAirship.shared().getApplicationContext(), Main.class);
    			launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			
    			// set notification list as pending intent
    			//ApplicationManager.setPendingIntent(new Intent(context, NotificationActivity.class));
                UAirship.shared().getApplicationContext().startActivity(launch);
            }

		} else if (action.equals(PushManager.ACTION_REGISTRATION_FINISHED)) {
            
		}

	}

	/**
	 * Log the values sent in the payload's "extra" dictionary.
	 * 
	 * @param intent A PushManager.ACTION_NOTIFICATION_OPENED or ACTION_PUSH_RECEIVED intent.
	 */
	private void logPushExtras(Intent intent) {
        Set<String> keys = intent.getExtras().keySet();
        for (String key : keys) {

            //ignore standard C2DM extra keys
            List<String> ignoredKeys = (List<String>)Arrays.asList(
                    "collapse_key",//c2dm collapse key
                    "from",//c2dm sender
                    PushManager.EXTRA_NOTIFICATION_ID,//int id of generated notification (ACTION_PUSH_RECEIVED only)
                    PushManager.EXTRA_PUSH_ID,//internal UA push id
                    PushManager.EXTRA_ALERT);//ignore alert
            if (ignoredKeys.contains(key)) {
                continue;
            }
        }
	}
}
