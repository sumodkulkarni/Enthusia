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

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Notifications.db";
    public static final String TABLE_NAME = "Notifications";
    public static final String KEY_ID = "id";
    public static final String KEY_READ = "read";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String COLUMNS = KEY_ID + "," + KEY_TITLE + "," + KEY_MESSAGE + "," + KEY_READ + "," + KEY_TIMESTAMP;
    private static final String QUERY_STRING = "=?";

    private static final String TABLE_POINTS = "POINTS_TABLE";
    private static final String KEY_DEPARTMENT = "DEPARTMENT";
    private static final String KEY_POINTS = "POINTS";
    private static final String TAG = "NotificationDBManager";
    private static final String COLUMNS_POINTS = KEY_DEPARTMENT + "," + KEY_POINTS;

    public NotificationDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    /**
     * TABLE FORMAT
     * ID  TITLE   MESSAGE READ TIMESTAMP
     * INT VARCHAR VARCHAR INT  VARCHAR
     */
/*
    public NotificationDBManager(Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
    }
*/



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

        String CREATE_POINTS_TABLE = "CREATE TABLE " + TABLE_POINTS + "("
                + KEY_DEPARTMENT + " VARCHAR(255) PRIMARY KEY, "
                + KEY_POINTS + " INTEGER "
                + ")";
        db.execSQL(CREATE_POINTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);

        onCreate(db);
    }

    public void resetDatabase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);

        onCreate(db);
    }


    /** CRUD Operations
     */

    public long addMessage(Message message){
        long returnValue = -2;
        try{
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, message.getTitle());
            values.put(KEY_MESSAGE, message.getMessage());
            values.put(KEY_READ, message.isRead() ? 1 : 0);
            values.put(KEY_TIMESTAMP, message.getTimestamp());
            returnValue = db.insert(TABLE_NAME, null, values);
            db.close();
        }catch(SQLException e){
            Log.e(TAG, e.getMessage());
        }
        return returnValue;
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

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        Log.i(TAG, String.valueOf(cursor.getCount()));

        for(int i = 0; i<cursor.getCount(); i++){
            Message message = new Message();
            message.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            message.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            message.setMessage(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
            message.setIsRead(cursor.getInt(cursor.getColumnIndex(KEY_READ)));
            message.setTimestamp(cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP)));

            messages.add(message);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return messages;
    }

    public int updateMessage(Message message) {
        int returnValue;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, message.getId());
        values.put(KEY_TITLE, message.getTitle());
        values.put(KEY_MESSAGE, message.getMessage());
        values.put(KEY_READ, message.isRead() ? 1 : 0);
        values.put(KEY_TIMESTAMP, message.getTimestamp());

        returnValue = db.update(TABLE_NAME, values, KEY_ID + QUERY_STRING, new String[]{String.valueOf(message.getId())});
        db.close();
        return returnValue;
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

    public ArrayList<Message> getUnreadMessages(){
        ArrayList<Message> unreadMessages = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT ID, TITLE, MESSAGE, READ, TIMESTAMP FROM NOTIFICATIONS WHERE READ =?", new String[]{"0"});
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                Message message = new Message();
                message.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                message.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                message.setMessage(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
                message.setIsRead(cursor.getInt(cursor.getColumnIndex(KEY_READ)));
                message.setTimestamp(cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP)));
                unreadMessages.add(message);
                cursor.moveToNext();
            }
            cursor.close();
        }catch (SQLException e){
            Log.e(TAG, e.getMessage());
        }

        return unreadMessages;
    }

    /**
     * CRUD OPERATIONS FOR POINTS TABLE
     */

    public void updateDepartmentPoints(String department, int points){
        try {


            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_DEPARTMENT, department);
            values.put(KEY_POINTS, points);
            db.insertWithOnConflict(TABLE_POINTS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.close();
        }catch (SQLException e){
            Log.e(TAG, e.getMessage());
        }
    }

    public int getDepartmentPoints(String department){
        int returnValue = 0;
        try{
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT " + COLUMNS_POINTS + " FROM " + TABLE_POINTS + " WHERE " + KEY_DEPARTMENT + "=?", new String[]{department});
            cursor.moveToFirst();
            if (cursor.getCount() == 0) {
                return 0;
            }
            returnValue =  cursor.getInt(cursor.getColumnIndex(KEY_POINTS));
            cursor.close();
        }catch (SQLException e){
            Log.e(TAG, e.getMessage());
        }
        return returnValue;
    }
}
