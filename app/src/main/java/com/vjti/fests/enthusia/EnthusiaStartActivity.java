package com.vjti.fests.enthusia;

import android.app.Activity;
import android.os.Bundle;

import com.vjti.fests.ActivitySplitAnimationUtil;
import com.vjti.fests.R;

public class EnthusiaStartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enthusia_start);
        setTitle(getString(R.string.enthusia_fest_name));
        ActivitySplitAnimationUtil.prepareAnimation(this);
        ActivitySplitAnimationUtil.animate(this, 1000);
    }

    @Override
    protected void onStop() {
        ActivitySplitAnimationUtil.cancel();
        super.onStop();
    }
}
