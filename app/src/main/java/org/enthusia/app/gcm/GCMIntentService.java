package org.enthusia.app.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.enthusia.app.R;
import org.enthusia.app.Utils;
import org.enthusia.app.enthusia.EnthusiaStartActivity;
import org.enthusia.app.model.PushMessage;

import java.io.IOException;
import java.util.ArrayList;

public class GCMIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 47;

    public GCMIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        ArrayList<PushMessage> messages = null;
        try {
            messages = Utils.getPushMessages(getApplicationContext(), Utils.ENTHUSIA);
        } catch (IOException ioe) {
            messages = null;
        }

        if (messages == null)
            messages = new ArrayList<PushMessage>();

        messages.add(0, new PushMessage(Html.fromHtml(extras.getString("price")).toString()));

        try {
            Utils.editPushMessages(this, Utils.ENTHUSIA, messages);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (!extras.isEmpty()) {
            if (messageType.equals(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE)) {
                Utils.log(extras.toString());
                sendNotification(extras.getString("price"));
            }
        }



        PushNotificationReciever.completeWakefulIntent(intent);
    }

    private void sendNotification(String message) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, new Intent(this, EnthusiaStartActivity.class), PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getResources().getString(R.string.enthusia_fest_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL);

        builder.setContentIntent(pendingIntent);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
