package org.enthusia.app.parse.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;

import org.enthusia.app.enthusia.EnthusiaStartActivity;
import org.enthusia.app.parse.helper.NotificationDBManager;
import org.enthusia.app.parse.helper.NotificationUtils;
import org.enthusia.app.parse.model.Message;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumod on 20-Nov-15.
 */
public class CustomPushReceiver extends ParsePushBroadcastReceiver {

    private final String TAG = CustomPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;

    private Intent parseIntent;

    public CustomPushReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);

        if (intent == null)
            return;

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.e(TAG, "Push received: " + json);

            parseIntent = intent;

            parsePushJson(context, json);

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        Toast.makeText(context, "Push Dismissed!", Toast.LENGTH_LONG).show();
        super.onPushDismiss(context, intent);
        Toast.makeText(context, "Push Dismissed!", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
        ParseAnalytics.trackAppOpened(intent);

    }

    /**
     * Parses the push notification json
     *
     * @param context
     * @param json
     */
    private void parsePushJson(Context context, JSONObject json) {
        try {
            boolean isBackground = json.getBoolean("is_background");
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");

            NotificationDBManager db = new NotificationDBManager(context);

            Message new_message = new Message();
            new_message.setTitle(title); new_message.setMessage(message); new_message.setIsRead(false);
            db.addMessage(new_message);

            if (!isBackground) {
                Intent resultIntent = new Intent(context, EnthusiaStartActivity.class);
                showNotificationMessage(context, title, message, resultIntent);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }


    /**
     * Shows the notification message in the notification bar
     * If the app is in background, launches the app
     *
     * @param context
     * @param title
     * @param message
     * @param intent
     */
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {

        notificationUtils = new NotificationUtils(context);

        intent.putExtras(parseIntent.getExtras());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        notificationUtils.showNotificationMessage(title, message, intent);
    }

    private void addNotificationToDB(Message message){

    }
}
