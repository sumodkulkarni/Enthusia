package org.enthusia.app.parse.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import org.enthusia.app.model.PushMessage;
import org.enthusia.app.parse.model.Message;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Sumod on 21-Nov-15.
 */
public class NotificationDBManager extends SQLiteOpenHelper {

    public static final int VERSION = 1;
    public static final String NAME = "push_notification";
    public static final String TABLE_NAME = "Notifications";
    public static final String KEY_ID = "id";
    public static final String KEY_READ = "read";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String COLUMNS = KEY_ID + "," + KEY_TITLE + "," + KEY_MESSAGE + "," + KEY_READ + "," + KEY_TIMESTAMP;
    private static final String QUERY_STRING = "=?";
    private static final String TAG = "NotificationDBManager";

    private Context context;

    /**
     * TABLE FORMAT
     * ID  TITLE   MESSAGE READ TIMESTAMP
     * INT VARCHAR VARCHAR INT  VARCHAR
     */

    public NotificationDBManager(Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_TITLE + " VARCHAR(255), "
                + KEY_MESSAGE + " VARCHAR(255), "
                + KEY_READ + " INTEGER, "
                + KEY_TIMESTAMP + " VARCHAR(255) "
                + ")";
        db.execSQL(CREATE_NOTIFICATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);

        onCreate(db);
    }

    /** CRUD Operations
     */

    public void addMessage(Message message){
        try{
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, message.getTitle());
            values.put(KEY_MESSAGE, message.getMessage());
            values.put(KEY_READ, message.isRead() ? 1 : 0);
            values.put(KEY_TIMESTAMP, message.getTimestamp());
            db.insert(TABLE_NAME, null, values);
            db.close();
        }catch(SQLException e){
            Log.e(TAG, e.getMessage());
        }
    }

    public Message getMessage(int _id){
        Message message = new Message();
        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT " + COLUMNS + " WHERE " + KEY_ID + "=?", new String[]{String.valueOf(_id)});
            if (cursor == null)
                return null;

            cursor.moveToFirst();
            message.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            message.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            message.setMessage(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
            message.setIsRead(cursor.getInt(cursor.getColumnIndex(KEY_READ)));
            message.setTimestamp(cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP)));

        }catch (SQLException e){
            Log.e(TAG, e.getMessage());
        }
        return message;
    }

    public ArrayList<Message> getAllMessages(){
        ArrayList<Message> messages = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMNS}, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor == null)
            return null;
        int i = 0;
        do{
            messages.add(i, new Message(cursor.getInt(cursor.getColumnIndex(KEY_ID)), cursor.getString(cursor.getColumnIndex(KEY_TITLE)),cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)),
                    cursor.getInt(cursor.getColumnIndex(KEY_READ)), cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP))));
            i++;
        }while (cursor.moveToNext());

        cursor.close();
        db.close();
        return messages;
    }

    public void deleteMessage(int _id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, KEY_ID + QUERY_STRING, new String[]{String.valueOf(_id)});
        db.close();
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMNS}, null, null, null, null, null);
        cursor.moveToFirst();

        for (int i=0; i<cursor.getCount(); i++){
            db.delete(TABLE_NAME, KEY_ID + QUERY_STRING, new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndex(KEY_ID)))});
            cursor.moveToNext();
        }
    }

    public int getUnreadCount() {
        try {
            Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_READ + " = 0", null);
            return cursor.getCount();
        } catch (SQLiteException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
