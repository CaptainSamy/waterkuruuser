package jp.co.wssj.iungo.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.screens.pushnotification.pushpagecontainer.PushNotificationPageAdapter;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by Nguyen Huu Ta on 16/10/2017.
 */

public class DBManager {

    private static final String TAG = "DBManager";

    private DatabaseHelper mDatabaseHelper;

    private static DBManager mInstance;

    private SQLiteDatabase mDatabaseRead;

    private SQLiteDatabase mDatabaseWrite;

    public void init(Context context) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new DatabaseHelper(context);
            mDatabaseRead = mDatabaseHelper.getReadableDatabase();
            mDatabaseWrite = mDatabaseHelper.getWritableDatabase();
        }
    }

    public synchronized static DBManager getInstance() {
        if (mInstance == null) {
            mInstance = new DBManager();
        }
        return mInstance;
    }

    /*
    *
    * Add Push Notification
    *
    * */

    public void insertPushNotification(List<NotificationMessage> listPush) {
        Logger.d("bello", "start insert");
        if (listPush != null && listPush.size() > 0) {
            for (NotificationMessage notificationMessage : listPush) {
                if (!isExitsPush(notificationMessage.getPushId())) {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.PushNotification.COLUMN_PUSH_ID, notificationMessage.getPushId());
                    values.put(DatabaseContract.PushNotification.COLUMN_PUSH_TIME, notificationMessage.getPushTime());
                    values.put(DatabaseContract.PushNotification.COLUMN_TITLE_PUSH, notificationMessage.getTitle());
                    values.put(DatabaseContract.PushNotification.COLUMN_CONTENT_PUSH, notificationMessage.getMessage());
                    values.put(DatabaseContract.PushNotification.COLUMN_ACTION_PUSH, notificationMessage.getAction());
                    values.put(DatabaseContract.PushNotification.COLUMN_IMAGE_STORE, notificationMessage.getLogo());
                    values.put(DatabaseContract.PushNotification.COLUMN_LIKE, notificationMessage.isLike());
                    values.put(DatabaseContract.PushNotification.COLUMN_STORE_ANNOUNCE, notificationMessage.getStoreAnnounce());
                    values.put(DatabaseContract.PushNotification.COLUMN_STATUS_READ, notificationMessage.getStatusRead());
                    mDatabaseWrite.insert(DatabaseContract.PushNotification.TABLE_NAME, null, values);
                } else {
                    Logger.d("bello", "exist");
                }
            }
        }
        Logger.d("bello", "end insert");
    }

    public void insertPushNotification(NotificationMessage notificationMessage) {
        Logger.d("bello", "start insert");
        if (!isExitsPush(notificationMessage.getPushId())) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.PushNotification.COLUMN_PUSH_ID, notificationMessage.getPushId());
            values.put(DatabaseContract.PushNotification.COLUMN_PUSH_TIME, notificationMessage.getPushTime());
            values.put(DatabaseContract.PushNotification.COLUMN_TITLE_PUSH, notificationMessage.getTitle());
            values.put(DatabaseContract.PushNotification.COLUMN_CONTENT_PUSH, notificationMessage.getMessage());
            values.put(DatabaseContract.PushNotification.COLUMN_ACTION_PUSH, notificationMessage.getAction());
            values.put(DatabaseContract.PushNotification.COLUMN_IMAGE_STORE, notificationMessage.getLogo());
            values.put(DatabaseContract.PushNotification.COLUMN_LIKE, notificationMessage.isLike());
            values.put(DatabaseContract.PushNotification.COLUMN_STORE_ANNOUNCE, notificationMessage.getStoreAnnounce());
            values.put(DatabaseContract.PushNotification.COLUMN_STATUS_READ, notificationMessage.getStatusRead());
            mDatabaseWrite.insert(DatabaseContract.PushNotification.TABLE_NAME, null, values);
        } else {
            String whereClause = DatabaseContract.PushNotification.COLUMN_PUSH_ID + " =? ";
            String[] whereArgs = new String[]{String.valueOf(notificationMessage.getPushId())};
            mDatabaseWrite.delete(DatabaseContract.PushNotification.TABLE_NAME, whereClause, whereArgs);
        }
    }

    public void insertPushStoreAnnounce(List<NotificationMessage> listPush, int serviceCompanyId) {
        if (listPush != null && listPush.size() > 0) {
            for (NotificationMessage notificationMessage : listPush) {
                if (!isExitsPush(notificationMessage.getPushId())) {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.PushNotification.COLUMN_PUSH_ID, notificationMessage.getPushId());
                    values.put(DatabaseContract.PushNotification.COLUMN_PUSH_TIME, notificationMessage.getPushTime());
                    values.put(DatabaseContract.PushNotification.COLUMN_TITLE_PUSH, notificationMessage.getTitle());
                    values.put(DatabaseContract.PushNotification.COLUMN_CONTENT_PUSH, notificationMessage.getMessage());
                    values.put(DatabaseContract.PushNotification.COLUMN_ACTION_PUSH, notificationMessage.getAction());
                    values.put(DatabaseContract.PushNotification.COLUMN_IMAGE_STORE, notificationMessage.getLogo());
                    values.put(DatabaseContract.PushNotification.COLUMN_LIKE, notificationMessage.isLike());
                    values.put(DatabaseContract.PushNotification.COLUMN_STORE_ANNOUNCE, serviceCompanyId);
                    values.put(DatabaseContract.PushNotification.COLUMN_STATUS_READ, notificationMessage.getStatusRead());
                    mDatabaseWrite.insert(DatabaseContract.PushNotification.TABLE_NAME, null, values);
                } else {
                    updateStoreAnnounce(notificationMessage.getPushId());
                }
            }
        }
    }

    public void updateStoreAnnounce(long pushId) {
        String selection = DatabaseContract.PushNotification.COLUMN_PUSH_ID + " = ? ";
        String selectionArgs[] = new String[]{String.valueOf(pushId)};
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.PushNotification.COLUMN_STORE_ANNOUNCE, 1);
        mDatabaseRead.update(DatabaseContract.PushNotification.TABLE_NAME, contentValues, selection, selectionArgs);
    }

    public List<NotificationMessage> searchListPush(String keySearch) {
        List<NotificationMessage> listPush = new ArrayList<>();
        if (TextUtils.isEmpty(keySearch)) {
            String columnSelection[] = new String[]{DatabaseContract.PushNotification.COLUMN_TITLE_PUSH, DatabaseContract.PushNotification.COLUMN_CONTENT_PUSH};
            String selection = DatabaseContract.PushNotification.COLUMN_TITLE_PUSH + " = ? AND " + DatabaseContract.PushNotification.COLUMN_CONTENT_PUSH + " = ? ";
            String selectionArgs[] = new String[]{String.valueOf(keySearch), String.valueOf(keySearch)};

            Cursor cursorSearchPush = mDatabaseRead.query(DatabaseContract.PushNotification.TABLE_NAME, columnSelection, selection, selectionArgs, null, null, null);
            while (cursorSearchPush.moveToFirst()) {
                NotificationMessage notificationMessage = new NotificationMessage();
                notificationMessage.setPushId(cursorSearchPush.getInt(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_PUSH_ID)));
                notificationMessage.setPushTime(cursorSearchPush.getLong(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_PUSH_TIME)));
                notificationMessage.setTitle(cursorSearchPush.getString(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_TITLE_PUSH)));
                notificationMessage.setMessage(cursorSearchPush.getString(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_CONTENT_PUSH)));
                notificationMessage.setAction(cursorSearchPush.getString(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_ACTION_PUSH)));
                notificationMessage.setLogo(cursorSearchPush.getString(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_IMAGE_STORE)));
                notificationMessage.setIsLike(cursorSearchPush.getInt(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_LIKE)));
                notificationMessage.setStoreAnnounce(cursorSearchPush.getInt(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_STORE_ANNOUNCE)));
                notificationMessage.setmStatusRead(cursorSearchPush.getInt(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_STATUS_READ)));
                listPush.add(notificationMessage);
            }
            cursorSearchPush.close();
        }
        return listPush;
    }

    public List<NotificationMessage> getListPush(int type, int serviceCompanyId) {
        List<NotificationMessage> listPush = new ArrayList<>();
        String sqlGetListPush;
        if (type == Constants.TypePush.TYPE_LIKED_PUSH) {
            sqlGetListPush = "SELECT * FROM " + DatabaseContract.PushNotification.TABLE_NAME + " WHERE " + DatabaseContract.PushNotification.COLUMN_LIKE + " = 1";
        } else if (type == Constants.TypePush.TYPE_QUESTION_NAIRE_PUSH) {
            sqlGetListPush = "SELECT * FROM " + DatabaseContract.PushNotification.TABLE_NAME + " WHERE " + DatabaseContract.PushNotification.COLUMN_ACTION_PUSH + " = '" + Constants.PushNotification.TYPE_QUESTION_NAIRE + "'";
        } else if (type == Constants.TypePush.TYPE_PUSH_ANNOUNCE) {
            sqlGetListPush = "SELECT * FROM " + DatabaseContract.PushNotification.TABLE_NAME + " WHERE " + DatabaseContract.PushNotification.COLUMN_STORE_ANNOUNCE + " = " + serviceCompanyId;
        } else {
            sqlGetListPush = "SELECT * FROM " + DatabaseContract.PushNotification.TABLE_NAME + " ORDER BY " + DatabaseContract.PushNotification.COLUMN_PUSH_ID + " DESC";
        }
        Cursor cursorSearchPush = mDatabaseRead.rawQuery(sqlGetListPush, null);
        while (cursorSearchPush.moveToNext()) {
            NotificationMessage notificationMessage = new NotificationMessage();
            notificationMessage.setPushId(cursorSearchPush.getInt(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_PUSH_ID)));
            notificationMessage.setPushTime(cursorSearchPush.getLong(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_PUSH_TIME)));
            notificationMessage.setTitle(cursorSearchPush.getString(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_TITLE_PUSH)));
            notificationMessage.setMessage(cursorSearchPush.getString(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_CONTENT_PUSH)));
            notificationMessage.setAction(cursorSearchPush.getString(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_ACTION_PUSH)));
            notificationMessage.setLogo(cursorSearchPush.getString(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_IMAGE_STORE)));
            notificationMessage.setIsLike(cursorSearchPush.getInt(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_LIKE)));
            notificationMessage.setStoreAnnounce(cursorSearchPush.getInt(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_STORE_ANNOUNCE)));
            notificationMessage.setmStatusRead(cursorSearchPush.getInt(cursorSearchPush.getColumnIndex(DatabaseContract.PushNotification.COLUMN_STATUS_READ)));
            listPush.add(notificationMessage);
        }
        cursorSearchPush.close();
        Collections.reverse(listPush);
        return listPush;
    }

    public boolean isExitsPush(long pushId) {
        String columnSelection[] = new String[]{DatabaseContract.PushNotification.COLUMN_PUSH_ID};
        String selection = DatabaseContract.PushNotification.COLUMN_PUSH_ID + " = ?";
        String selectionArgs[] = new String[]{String.valueOf(pushId)};

        Cursor cursorPush = mDatabaseRead.query(DatabaseContract.PushNotification.TABLE_NAME, columnSelection, selection, selectionArgs, null, null, null);
        boolean isExits = cursorPush.moveToNext();
        cursorPush.close();
        return isExits;
    }

    public void likePush(long pushId, int statusLike) {
        String selection = DatabaseContract.PushNotification.COLUMN_PUSH_ID + " = ? ";
        String selectionArgs[] = new String[]{String.valueOf(pushId)};
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.PushNotification.COLUMN_LIKE, statusLike);
        mDatabaseRead.update(DatabaseContract.PushNotification.TABLE_NAME, contentValues, selection, selectionArgs);
    }

    public void clearDatabase() {
        mDatabaseRead.execSQL("DELETE FROM " + DatabaseContract.PushNotification.TABLE_NAME);
    }
}
