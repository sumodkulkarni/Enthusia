package org.enthusia.app.model;

import java.io.Serializable;

public class PushMessage implements Serializable, Comparable<PushMessage> {

    private String message;
    private boolean read;

    protected PushMessage() {}

    public PushMessage(String message) {
        this (message, false);
    }

    public PushMessage(String message, boolean read) {
        this.message = message;
        this.read = read;
    }

    public void setMessage(String message) { this.message = message; }
    public void setRead(boolean read) { this.read = read; }

    public String getMessage() { return this.message; }
    public boolean isRead() { return this.read; }


    @Override
    public int compareTo(PushMessage other) {
        return 0;
    }
}
