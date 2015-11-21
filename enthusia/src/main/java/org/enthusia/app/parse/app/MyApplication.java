package org.enthusia.app.parse.app;

import android.app.Application;

import org.enthusia.app.parse.helper.ParseUtils;

/**
 * Created by Sumod on 20-Nov-15.
 */
public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // register with parse
        ParseUtils.registerParse(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}
