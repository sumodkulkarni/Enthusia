package org.enthusia.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class ExpandableTextView extends TextView {

    public ExpandableTextView(Context context) { super(context); }

    public ExpandableTextView(Context context, AttributeSet set) { super(context, set); }

    public void setMaxLines(float maxLines) {
        super.setMaxLines((int) maxLines);
    }
}
