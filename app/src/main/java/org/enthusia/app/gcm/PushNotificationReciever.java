package org.enthusia.app.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import org.enthusia.app.Utils;

public class PushNotificationReciever extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.log("Boradcast Received");
        ComponentName name = new ComponentName(context.getPackageName(), GCMIntentService.class.getName());
        startWakefulService(context, intent.setComponent(name));
        setResultCode(Activity.RESULT_OK);
    }
}
