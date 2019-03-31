package wssj.co.jp.olioa.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import wssj.co.jp.olioa.utils.Logger;

/**
 * Created by Nguyen Huu Ta on 16/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Olioa.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTablePush(db);
        createTableChat(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logger.d(TAG, "#onUpgrade");
    }

    private void createTablePush(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DatabaseContract.PushNotification.TABLE_NAME + " ("
                + DatabaseContract.PushNotification.COLUMN_PUSH_ID + " INTEGER PRIMARY KEY, "
                + DatabaseContract.PushNotification.COLUMN_PUSH_TIME + " INTEGER, "
                + DatabaseContract.PushNotification.COLUMN_TITLE_PUSH + " TEXT, "
                + DatabaseContract.PushNotification.COLUMN_CONTENT_PUSH + " TEXT, "
                + DatabaseContract.PushNotification.COLUMN_ACTION_PUSH + " VARCHAR, "
                + DatabaseContract.PushNotification.COLUMN_IMAGE_STORE + " VARCHAR, "
                + DatabaseContract.PushNotification.COLUMN_LIKE + " INTEGER, "
                + DatabaseContract.PushNotification.COLUMN_STORE_ANNOUNCE + " INTEGER, "
                + DatabaseContract.PushNotification.COLUMN_STATUS_READ + " INTEGER) ");
    }

    private void createTableChat(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DatabaseContract.ChatColumns.TABLE_NAME + " ("
                + DatabaseContract.ChatColumns.COLUMN_CHAT_ID + " INTEGER PRIMARY KEY, "
                + DatabaseContract.ChatColumns.COLUMN_STORE_ID + " INTEGER, "
                + DatabaseContract.ChatColumns.COLUMN_CONTENT + " TEXT, "
                + DatabaseContract.ChatColumns.COLUMN_IMAGES + " TEXT, "
                + DatabaseContract.ChatColumns.COLUMN_VIDEOS + " TEXT, "
                + DatabaseContract.ChatColumns.COLUMN_IS_USER + " INTEGER, "
                + DatabaseContract.ChatColumns.COLUMN_CREATED + " INTEGER) ");
    }

}
