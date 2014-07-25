package org.enthusia.app.model;

import android.os.Parcel;
import android.text.Spanned;
import android.os.Parcelable;

import java.io.Serializable;

@SuppressWarnings("unused")
public class PushMessage implements Serializable, Comparable<PushMessage>, Parcelable {

    private String message, id;
    private Spanned spannableString;
    private boolean read;

    protected PushMessage() {}

    public PushMessage(String message) {
        this (message, false);
    }

    public PushMessage(Spanned spannableString, boolean read) {
        this.read = read;
        this.spannableString = spannableString;
    }

    public PushMessage(String message, boolean read) {
        this.message = message;
        this.read = read;
    }

    public PushMessage(String id, String message, boolean read) {
        this.message = message;
        this.id = id;
        this.read = read;
    }

    public void setMessage(String message) { this.message = message; }
    public void setId(String id) { this.id = id; }
    public void setSpannableString(Spanned string) { this.spannableString = string; }
    public void setRead(boolean read) { this.read = read; }

    public String getMessage() { return this.message; }
    public String getId() { return this.id; }
    public Spanned getSpannableString() { return this.spannableString; }
    public boolean isRead() { return this.read; }


    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(PushMessage other) {
        return 0;
    }

    protected PushMessage(Parcel in) {
        message = in.readString();
        spannableString = (Spanned) in.readValue(Spanned.class.getClassLoader());
        read = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeValue(spannableString);
        dest.writeByte((byte) (read ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PushMessage> CREATOR = new Parcelable.Creator<PushMessage>() {
        @Override
        public PushMessage createFromParcel(Parcel in) {
            return new PushMessage(in);
        }

        @Override
        public PushMessage[] newArray(int size) {
            return new PushMessage[size];
        }
    };
}