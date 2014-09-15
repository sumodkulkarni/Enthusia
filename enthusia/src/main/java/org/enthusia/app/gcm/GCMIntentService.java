package org.enthusia.app.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.enthusia.app.R;
import org.enthusia.app.Utils;
import org.enthusia.app.enthusia.EnthusiaStartActivity;
import org.enthusia.app.enthusia.fragments.dialog.EnthusiaPointsTableDialog;
import org.enthusia.app.model.PushMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GCMIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 47;

    public GCMIntentService() {
        super("GcmIntentService");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {
            if (messageType.equals(GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE)) {
                Utils.log(extras.toString());
                if (extras.getString("price").startsWith("POINTSTABLE,")) {
                    updatePointsTable(extras.getString("price").split(","));
                } else if (extras.getString("price").equals("successful"));
                else {
                    new PushNotificationManager(this).addMessage(new PushMessage(new SimpleDateFormat("ddMMyyyy hhmmss").format(new Date()),extras.getString("price"), false));
                    int count = 0;
                    for (PushMessage message : new PushNotificationManager(this).getAllMessages())
                        if (!message.isRead())
                            count++;
                    sendNotification("You have " + count + " unread messages!", false);

                }
            }

        }
        PushNotificationReciever.completeWakefulIntent(intent);
    }

    private void updatePointsTable(String[] points) {
        String[] depts = getResources().getStringArray(R.array.enthusia_departments);
        for (int i=1; i < points.length; i++) {
            getSharedPreferences(EnthusiaPointsTableDialog.PREF_POINT_TABLE, Context.MODE_PRIVATE).edit().putInt(EnthusiaPointsTableDialog.PREF_POINTS + depts[i-1].toLowerCase(), Integer.parseInt(points[i])).commit();
        }
        sendNotification(getString(R.string.points_table_updated), true);
    }

    private void sendNotification(String message, boolean points) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, new Intent(this, EnthusiaStartActivity.class).putExtra("points", points), PendingIntent.FLAG_ONE_SHOT);

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
