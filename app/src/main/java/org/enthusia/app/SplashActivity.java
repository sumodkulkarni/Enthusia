package org.enthusia.app;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.enthusia.app.enthusia.EnthusiaStartActivity;
import org.enthusia.app.ui.ActivitySplitAnimationUtil;

public class SplashActivity extends Activity {

    private Handler animationHandler;
    private Runnable newActivity;
    private SplashAnimation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getActionBar().hide();
        } catch (NullPointerException ex) {}
        setContentView(R.layout.activity_splash);
        ((ShimmerTextView) findViewById(R.id.splash_enthusia_tagline)).setText(Html.fromHtml(getString(R.string.enthusia_tagline)));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        if (animationHandler == null) {
            animationHandler = new Handler();
            animation = new SplashAnimation();
            animationHandler.postDelayed(animation, 500);
        }
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        cancelAnim();
        super.onDestroy();
    }

    private void cancelAnim() {
        if (animationHandler != null && animation != null) {
            animationHandler.removeCallbacks(animation);
        }

        if (animationHandler != null && newActivity != null) {
            animationHandler.removeCallbacks(newActivity);
        }

        animation = null;
        animationHandler = null;
        newActivity = null;
    }

    private class SplashAnimation implements Runnable {

        @Override
        public void run() {
            AnimatorSet set = new AnimatorSet();
            set.addListener(getListener());
            set.play(title()).before(tagline());
            set.start();
        }

        private Animator title() {
            AnimatorSet set = new AnimatorSet();
            set.play(getAnimation(R.id.splash_enthusia_title, View.SCALE_X.getName(), new BounceInterpolator(), new float[] { 3.0f, 1.0f }, 1250, 0, 0))
               .with(getAnimation(R.id.splash_enthusia_title, View.SCALE_Y.getName(), new BounceInterpolator(), new float[] { 3.0f, 1.0f }, 1250, 0, 0))
               .with(getAnimation(R.id.splash_enthusia_title, View.ALPHA.getName(), null, new float[] { 0.0f, 1.0f }, 1000, 0, 0))
               .before(getAnimation(R.id.splash_enthusia_title, View.TRANSLATION_Y.getName(), new AccelerateDecelerateInterpolator(), new float[] { -findViewById(R.id.splash_enthusia_title).getHeight() - 100.0f }, 1500, 250, 0));
            return set;
        }

        private Animator tagline() {
            AnimatorSet set = new AnimatorSet();
            set.play(getAnimation(R.id.splash_enthusia_tagline, View.ROTATION_X.getName(), new AccelerateDecelerateInterpolator(), new float[] { 0.0f, 360.0f }, 1000, 500, 2))
               .with(getAnimation(R.id.splash_enthusia_tagline, View.ALPHA.getName(), null, new float[] { 0.0f, 1.0f }, 500, 0, 0));
            return set;
        }

        private Animator getAnimation (int id, String property, TimeInterpolator interpolator, float[] values, long duration, long delay, int repeatCount) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(findViewById(id), property, values);
            if (interpolator != null)
                animator.setInterpolator(interpolator);
            animator.setDuration(duration);
            animator.setStartDelay(delay);
            animator.setRepeatCount(repeatCount);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            return animator;
        }

        private Animator.AnimatorListener getListener() {
            return new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    Shimmer shimmer = new Shimmer();
                    shimmer.setRepeatCount(2);
                    shimmer.setDuration(1250);
                    shimmer.start((ShimmerTextView) findViewById(R.id.splash_enthusia_tagline));
                    newActivity = new Runnable() {
                        @Override
                        public void run() {
                            ActivitySplitAnimationUtil.startActivity(SplashActivity.this, new Intent(SplashActivity.this, EnthusiaStartActivity.class));
                            finish();
                        }
                    };
                    animationHandler.postDelayed(newActivity, 2700);
                }

                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}

            };
        }
    }

}
