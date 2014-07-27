package org.enthusia.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.alexvasilkov.foldablelayout.UnfoldableView;

@SuppressWarnings("unused")
public class UnfoldableNoSwipe extends UnfoldableView {


    public UnfoldableNoSwipe(Context context) {
        super(context);
    }

    public UnfoldableNoSwipe(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnfoldableNoSwipe(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
}
