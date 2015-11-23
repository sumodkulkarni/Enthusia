package org.enthusia.app.parse.receiver;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.parse.ParseAnalytics;
import com.parse.ParsePushBroadcastReceiver;


import org.enthusia.app.enthusia.EnthusiaStartActivity;

import org.enthusia.app.parse.helper.NotificationDBManager;
import org.enthusia.app.parse.helper.NotificationUtils;
import org.enthusia.app.parse.model.Message;
import org.json.JSONException;
import org.json.JSONObject;;

/**
 * Created by Sumod on 20-Nov-15.
 */
public class CustomPushReceiver extends ParsePushBroadcastReceiver {

    private final String TAG = CustomPushReceiver.class.getSimpleName();

    public static final int NOTIFICATION_ID = 47;

    private NotificationUtils notificationUtils;
    private Intent parseIntent;
    private Message new_message;
    private String[] departments_array = {"CHEMSA","CIVIL","COMPUTERS","ELECTRICAL","ELECTRONICS","EXTC","I.T","MASTERS","MECHANICAL","PRODUCTION","TEXTILE"};

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
        super.onPushDismiss(context, intent);
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
            boolean receiving_points = json.getBoolean("sending_points");
            JSONObject points_matrix = json.getJSONObject("points_matrix");
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");


            NotificationDBManager db = new NotificationDBManager(context, null, null, 1);
            new_message = new Message();
            new_message.setTitle(title); new_message.setMessage(message); new_message.setIsRead(false);
            long value = db.addMessage(new_message);
            Log.i(TAG, "db.addMessage = " + value);

            updatePointsTable(db, points_matrix);

            if (!isBackground) {
                Intent resultIntent = new Intent(context, EnthusiaStartActivity.class);
                resultIntent.putExtra("receiving_points", receiving_points);
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

    public void updatePointsTable(NotificationDBManager db, JSONObject points_matrix) {
        try {
            for (int i = 0; i < departments_array.length; i++) {
                if (points_matrix.has(departments_array[i])) {
                    db.updateDepartmentPoints(departments_array[i], points_matrix.getInt(departments_array[i]));
                }

            }
        }catch (JSONException e){
            Log.e(TAG, e.getMessage());
        }

    }
}
