package jp.co.wssj.iungo.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nguyen Huu Ta on 16/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Iungo.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DatabaseContract.PushNotification.TABLE_NAME + " ("
                + DatabaseContract.PushNotification.COLUMN_PUSH_ID + " INTEGER PRIMARY KEY, "
                + DatabaseContract.PushNotification.COLUMN_PUSH_TIME + " INTEGER, "
                + DatabaseContract.PushNotification.COLUMN_TITLE_PUSH + " TEXT, "
                + DatabaseContract.PushNotification.COLUMN_CONTENT_PUSH + " TEXT, "
                + DatabaseContract.PushNotification.COLUMN_ACTION_PUSH + " VARCHAR, "
                + DatabaseContract.PushNotification.COLUMN_IMAGE_STORE + " VARCHAR, "
                + DatabaseContract.PushNotification.COLUMN_LIKE + " INTEGER, "
                + DatabaseContract.PushNotification.COLUMN_STATUS_READ + " INTEGER) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
