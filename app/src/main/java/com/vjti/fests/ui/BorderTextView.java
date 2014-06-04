package com.vjti.fests.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class BorderTextView extends TextView {
	
	 private static final float DEFAULT_SPEED = 15.0f;
	 private float speed = DEFAULT_SPEED;
	 private boolean continuousScrolling = false;
	
	public BorderTextView(Context context) {
		super(context);
	}
	
	public BorderTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BorderTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void draw(Canvas canvas) {
		for (int i=0; i < 5; i++)
			super.draw(canvas);
	}
}
