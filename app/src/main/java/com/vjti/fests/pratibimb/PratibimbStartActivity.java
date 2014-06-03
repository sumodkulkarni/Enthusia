package com.vjti.fests.pratibimb;

import android.app.Activity;
import android.os.Bundle;

import com.vjti.fests.ui.ActivitySplitAnimationUtil;
import com.vjti.fests.R;

public class PratibimbStartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pratibimb_start);
        ActivitySplitAnimationUtil.prepareAnimation(this);
        ActivitySplitAnimationUtil.animate(this, 1000);
    }

    @Override
    protected void onStop() {
        ActivitySplitAnimationUtil.cancel();
        super.onStop();
    }
}
