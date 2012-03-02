package com.wildmind.fanwave.activity;

import com.urbanairship.AirshipConfigOptions;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;
import com.wildmind.fanwave.c2dm.UrbanAirshipReceiver;

import android.app.Application;

public class FanwaveApplication extends Application {
	
	@Override
    public void onCreate() {
        super.onCreate();

        AirshipConfigOptions options = AirshipConfigOptions.loadDefaultOptions(this);
        UAirship.takeOff(this, options);
        PushManager.shared().setIntentReceiver(UrbanAirshipReceiver.class);
        
        // if apid is null, enable push to generate apid, otherwise, we shouldn't enable push without
        // judging user's sign state
        if (PushManager.shared().getPreferences().getPushId() == null)
        	PushManager.enablePush();
    }
}