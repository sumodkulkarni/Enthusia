package com.vjti.fests.technovanza;

import android.app.Activity;
import android.os.Bundle;

import com.vjti.fests.ActivitySplitAnimationUtil;
import com.vjti.fests.R;

public class TechnovanzaStartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technovanza_start);
        setTitle(getString(R.string.technovanza_fest_name));
        ActivitySplitAnimationUtil.prepareAnimation(this);
        ActivitySplitAnimationUtil.animate(this, 1000);
    }

    @Override
    protected void onStop() {
        ActivitySplitAnimationUtil.cancel();
        super.onStop();
    }
}
