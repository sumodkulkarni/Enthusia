package org.enthusia.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by rahuliyer on 1/6/14.
 * Automatic Highlight for Button when Clicked, Pressed, Enabled
 * No need for custom drawables
 */

public class HighlightButton extends Button {

    public HighlightButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setCustomFont(context);
    }

    public HighlightButton(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        setCustomFont(context);
    }

    public void setCustomFont (Context context) {
        setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Regular.ttf"));
    }

    @Override
    @SuppressWarnings("deprecated")
    public void setBackgroundDrawable(Drawable d) {
        // Replace the original background drawable (e.g. image) with a LayerDrawable that
        // contains the original drawable.
        SAutoBgButtonBackgroundDrawable layer = new SAutoBgButtonBackgroundDrawable(d);
        super.setBackgroundDrawable(layer);
    }

    @Override
    @SuppressLint("NewApi")
    public void setBackground(Drawable d) {
        SAutoBgButtonBackgroundDrawable layer = new SAutoBgButtonBackgroundDrawable(d);
        super.setBackground(layer);
    }

    /**
     * The stateful LayerDrawable used by this button.
     */
    protected class SAutoBgButtonBackgroundDrawable extends LayerDrawable {

        // The color filter to apply when the button is pressed
        protected ColorFilter _pressedFilter = new LightingColorFilter(Color.LTGRAY, 1);
        // Alpha value when the button is disabled
        protected int _disabledAlpha = 100;
        // Alpha value when the button is enabled
        protected int _fullAlpha = 255;

        public SAutoBgButtonBackgroundDrawable(Drawable d) {
            super(new Drawable[] { d });
        }

        @Override
        protected boolean onStateChange(int[] states) {
            boolean enabled = false;
            boolean pressed = false;

            for (int state : states) {
                if (state == android.R.attr.state_enabled)
                    enabled = true;
                else if (state == android.R.attr.state_pressed)
                    pressed = true;
            }

            mutate();
            if (enabled && pressed) {
                setColorFilter(_pressedFilter);
            } else if (!enabled) {
                setColorFilter(null);
                setAlpha(_disabledAlpha);
            } else {
                setColorFilter(null);
                setAlpha(_fullAlpha);
            }

            invalidateSelf();

            return super.onStateChange(states);
        }

        @Override
        public boolean isStateful() {
            return true;
        }
    }

}
