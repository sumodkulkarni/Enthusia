package com.vjti.fests;

import android.app.Activity;
import android.content.Context;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by rahuliyer on 3/6/14.
 * Use this class to access methods to make Toast and Shared Prefs
 */

public class Utils {

    public static final String SHARED_PREFS = "com.vjti.fests";

    /**
     * Preferences
     * Add your preferences here
     */

    public static final String PREF_USER_NAME = "pref_username";
    public static final String PREF_EMAIL = "pref_email";
    public static final String PREF_REGISTRATION_DONE = "pref_registration_done";


    /**
     * Make Toast Message in BLUE color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param id the id of the string resource to be displayed
     */

    public static final void showInfo (Activity activity, int id) {
        showInfo(activity, activity.getString(id));
    }

    /**
     * Make Toast Message in BLUE color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param message the string message to be displayed
     */

    public static final void showInfo (Activity activity, String message) {
        crouton(activity, message, Style.INFO);
    }

    /**
     * Make Toast Message in GREEN color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param id the the id of the string resource to be displayed
     */

    public static final void showConfirm (Activity activity, int id) {
        showConfirm(activity, activity.getString(id));
    }

    /**
     * Make Toast Message in GREEN color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param message the string message to be displayed
     */

    public static final void showConfirm (Activity activity, String message) {
        crouton(activity, message, Style.CONFIRM);
    }

    /**
     * Make Toast Message in RED color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param id the id of the string resource to be displayed
     */

    public static final void showAlert (Activity activity, int id) {
        showAlert(activity, activity.getString(id));
    }

    /**
     * Make Toast Message in RED color shown at top of screen
     * @param activity the activity in which message has to be displayed
     * @param message the string message to be displayed
     */

    public static final void showAlert (Activity activity, String message) {
        crouton(activity, message, Style.ALERT);
    }

    private static final void crouton (Activity activity, String message, Style style) {
        Crouton.makeText(activity, message, style).show();
    }

}
