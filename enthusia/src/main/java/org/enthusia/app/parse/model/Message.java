package org.enthusia.app.parse.model;

import android.widget.Switch;

/**
 * Created by Sumod on 20-Nov-15.
 */
public class Message {
    private int id;
    private String title;
    private String message;
    private long timestamp;
    private boolean isRead;

    public Message() {
    }

    public Message(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public Message(int id, String title, String message, boolean isRead, long timestamp) {
        this.id = id;
        this.title = title;
        this.isRead = isRead;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Message(int id, String title,String message, int isRead, String timestamp) {
        this.id = id;
        this.title = title;
        this.message = message;

        switch (isRead){
            case 0:
                this.isRead = false; break;
            case 1:
                this.isRead = true; break;
        }

        this.timestamp = Long.parseLong(timestamp);
    }

    public Message(String title,String message, int isRead, String timestamp){
        this.title = title;
        this.message = message;

        switch (isRead){
            case 0:
                this.isRead = false; break;
            case 1:
                this.isRead = true; break;
        }

        this.timestamp = Long.parseLong(timestamp);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = Long.parseLong(timestamp);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {

        return id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public void setIsRead(int isRead) {
        switch (isRead){
            case 0:
                this.isRead = false; break;
            case 1:
                this.isRead = true; break;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

