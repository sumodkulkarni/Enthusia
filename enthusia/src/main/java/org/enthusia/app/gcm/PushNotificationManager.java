package org.enthusia.app.gcm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import org.enthusia.app.model.PushMessage;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by rahuliyer on 22/07/14.
 * Database Manager for the push notifications recieved
 */

@SuppressWarnings("unused")
public class PushNotificationManager extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String NAME = "push_notification";
    public static final String TABLE_NAME = "messages";
    public static final String COL_ID = "id";
    public static final String COL_READ = "read";
    public static final String COL_MESSAGE = "message";
    public static final String[] COLS = {COL_ID, COL_MESSAGE, COL_READ};
    private static final String QUERY_STRING = "=?";

    /**
     * TABLE FORMAT
     * ID     MESSAGE READ
     * STRING STRING INTEGER
     */

    public PushNotificationManager(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " TEXT PRIMARY KEY,"
                    + COL_MESSAGE + " TEXT,"
                    + COL_READ + " INTEGER" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Database Functions
     */

    public void addMessage(PushMessage message) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, message.getId());
        values.put(COL_MESSAGE, message.getMessage());
        values.put(COL_READ, message.isRead() ? 1 : 0);
        this.getWritableDatabase().insert(TABLE_NAME, null, values);
    }

    public PushMessage getMessage(String id) {
        Cursor cursor = this.getWritableDatabase().query(TABLE_NAME, COLS, COL_ID + QUERY_STRING, null, null, null, null);

        if (cursor != null) {
            return new PushMessage(cursor.getString(0), cursor.getString(1), cursor.getInt(2) == 1);
        }
        return null;
    }

    public int getCount() {
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.close();

        return cursor.getCount();
    }

    public ArrayList<PushMessage> getAllMessages() {
        ArrayList<PushMessage> messageList = null;
        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor != null) {
            messageList = new ArrayList<PushMessage>();
            if (cursor.moveToFirst()) {
                do {
                    messageList.add(new PushMessage(cursor.getString(0), cursor.getString(1), cursor.getInt(2) == 1));
                } while (cursor.moveToNext());
            }

        }
        if (messageList != null && messageList.size() > 0)
            Collections.reverse(messageList);
        return messageList;
    }

    public void updateContact(PushMessage message) {
        ContentValues values = new ContentValues();
        values.put(COL_ID, message.getId());
        values.put(COL_MESSAGE, message.getMessage());
        values.put(COL_READ, message.isRead() ? 1 : 0);

        this.getWritableDatabase().update(TABLE_NAME, values, COL_ID + QUERY_STRING, new String[] { message.getId() });
    }

    public void deleteMessage(PushMessage message) {
        this.getWritableDatabase().delete(TABLE_NAME, COL_ID + QUERY_STRING, new String[] { message.getId() });
    }

    public void deleteAll() {
        try {
            this.getWritableDatabase().rawQuery("DELETE FROM " + TABLE_NAME, null);
        } catch (SQLiteException ignore) {}
    }
}
