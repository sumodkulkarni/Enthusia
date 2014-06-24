package org.enthusia.app.model;

import android.text.SpannableString;
import android.text.Spanned;

import java.io.Serializable;

public class PushMessage implements Serializable, Comparable<PushMessage> {

    private SpannableString message;
    private boolean read;

    protected PushMessage() {}

    public PushMessage(String message) {
        this (message, false);
    }

    public PushMessage(Spanned message) {
        this(message.toString(), false);
    }

    public PushMessage(SpannableString message) {
        this (message.toString(), false);
    }

    public PushMessage(SpannableString message, boolean read) {
        this (message.toString(), read);
    }

    public PushMessage(String message, boolean read) {
        this.message = new SpannableString(message);
        this.read = read;
    }

    public void setMessage(String message) { this.message = new SpannableString(message); }
    public void setRead(boolean read) { this.read = read; }

    public SpannableString getMessage() { return this.message; }
    public boolean isRead() { return this.read; }


    @Override
    public int compareTo(PushMessage other) {
        return 0;
    }
}
