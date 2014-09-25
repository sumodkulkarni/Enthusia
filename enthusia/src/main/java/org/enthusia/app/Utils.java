package org.enthusia.app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by rahuliyer on 3/6/14.
 * Use this class to access methods to make Toast and Shared Prefs
 */

public class Utils {

    public static String SHARED_PREFS = "org.enthusia.app";

    /**
     * Preferences
     * Add your preferences here
     */

    public static String PREF_USER_NAME = "pref_username";
    public static String PREF_EMAIL = "pref_email";
    public static String PREF_REGISTRATION_DONE = "pref_registration_done";
    public static String PREF_FIRST_RUN = "pref_first_run";
    public static String PREF_REGISTRATION_ID = "pref_reg_id";
    public static String PREF_APP_VERSION = "pref_app_version";

    /**
     * This function returns the Shared_Prefs for particular key
     * @param context Context of the android application running (getApplicationContext()) will help
     * @param key The key for the desired preference
     * @param object The class of the desired preference value (e.g: String.class)
     * @return Object of the type of preference
     */

    public static Object getPrefs(Context context, String key, Class object) {
        if (object.equals(String.class)) {
            return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getString(key, null);
        } else if (object.equals(Integer.class)) {
            return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getInt(key, 0);
        } else if (object.equals(Boolean.class)) {
            return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getBoolean(key, false);
        } else if (object.equals(Float.class)) {
            return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).getFloat(key, 0.0f);
        }
        return null;
    }

    /**
     * Use this function to store the preference's value
     * @param context Context of the android application running (getApplicationContext()) will help
     * @param key The key for the desired preference
     * @param value The value of the desired preference
     */

    public static void putPrefs(Context context, String key, Object value) {
        if (value.getClass().equals(String.class)) {
            context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putString(key, (String) value).commit();
        } else if (value.getClass().equals(Boolean.class)) {
            context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putBoolean(key, ((Boolean) value).booleanValue()).commit();
        } else if (value.getClass().equals(Integer.class)) {
            context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putInt(key, (Integer) value).commit();
        } else if (value.getClass().equals(Float.class)) {
            context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().putFloat(key, (Float) value).commit();
        }

    }

    /**
     * Make Toast Message in BLUE color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param id the id of the string resource to be displayed
     */

    public static void showInfo (Activity activity, int id) {
        showInfo(activity, activity.getString(id));
    }

    /**
     * Make Toast Message in BLUE color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param message the string message to be displayed
     */

    public static void showInfo (Activity activity, String message) {
        crouton(activity, message, Style.INFO);
    }

    /**
     * Make Toast Message in GREEN color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param id the the id of the string resource to be displayed
     */

    public static void showConfirm (Activity activity, int id) {
        showConfirm(activity, activity.getString(id));
    }

    /**
     * Make Toast Message in GREEN color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param message the string message to be displayed
     */

    public static void showConfirm (Activity activity, String message) {
        crouton(activity, message, Style.CONFIRM);
    }

    /**
     * Make Toast Message in RED color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param id the id of the string resource to be displayed
     */

    public static void showAlert (Activity activity, int id) {
        showAlert(activity, activity.getString(id));
    }

    /**
     * Make Toast Message in RED color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param message the string message to be displayed
     */

    public static void showAlert (Activity activity, String message) {
        crouton(activity, message, Style.ALERT);
    }

    private static void crouton (Activity activity, String message, Style style) {
        Crouton.makeText(activity, message, style).show();
    }

    public static void makeToast (Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void makeToast (Context context, int message) {
        makeToast(context, context.getString(message));
    }

    public static void log (Object log) {
        Log.d("org.enthusia.app.debug", log.toString());
    }

}
