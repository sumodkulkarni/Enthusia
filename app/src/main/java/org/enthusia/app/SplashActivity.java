package org.enthusia.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import org.enthusia.app.enthusia.EnthusiaStartActivity;
import org.enthusia.app.ui.ActivitySplitAnimationUtil;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getActionBar().hide();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivitySplitAnimationUtil.startActivity(SplashActivity.this, new Intent(SplashActivity.this, EnthusiaStartActivity.class));
                finish();
            }
        }, 11 * 1000);
    }
}
