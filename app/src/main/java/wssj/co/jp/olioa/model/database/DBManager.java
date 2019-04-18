package wssj.co.jp.olioa.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wssj.co.jp.olioa.model.chat.ChatMessage;
import wssj.co.jp.olioa.model.entities.StoreInfo;
import wssj.co.jp.olioa.model.firebase.NotificationMessage;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Logger;
import wssj.co.jp.olioa.utils.Utils;

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
     *
     * Store Chat
     *
     * */


    public void insertStore(List<StoreInfo> listStore) {
        if (Utils.isNotEmpty(listStore)) {
            for (StoreInfo storeInfo : listStore) {
                if (storeInfo != null && !checkExitsStoreId(storeInfo.getId())) {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.StoreColumn.COLUMN_STORE_ID, storeInfo.getId());
                    values.put(DatabaseContract.StoreColumn.STORE_NAME, storeInfo.getName());
                    values.put(DatabaseContract.StoreColumn.COLUMN_LAST_MESSAGE, storeInfo.getLastMessage());
                    values.put(DatabaseContract.StoreColumn.COLUMN_LAST_TIME_MESSAGE, storeInfo.getLastTimeMessage());
                    values.put(DatabaseContract.StoreColumn.COLUMN_LOGO_STORE, storeInfo.getLogo());
                    mDatabaseWrite.insert(DatabaseContract.StoreColumn.TABLE_NAME, null, values);
                }
            }
        }
    }

    private boolean checkExitsStoreId(long storeId) {
        String columnSelection[] = new String[]{DatabaseContract.StoreColumn.COLUMN_STORE_ID};
        String selection = DatabaseContract.ChatColumns.COLUMN_STORE_ID + " = ?";
        String selectionArgs[] = new String[]{String.valueOf(storeId)};

        Cursor cursorPush = mDatabaseRead.query(DatabaseContract.StoreColumn.TABLE_NAME, columnSelection, selection, selectionArgs, null, null, null);
        boolean isExits = cursorPush.moveToNext();
        cursorPush.close();
        return isExits;
    }

    public List<StoreInfo> getListStore() {
        String sql = "SELECT * FROM " + DatabaseContract.StoreColumn.TABLE_NAME;
        Cursor cursorChats = mDatabaseRead.rawQuery(sql, null);
        List<StoreInfo> storeInfos = new ArrayList<>();
        while (cursorChats.moveToNext()) {
            StoreInfo storeInfo = new StoreInfo();
            storeInfo.setId(cursorChats.getInt(cursorChats.getColumnIndex(DatabaseContract.StoreColumn.COLUMN_STORE_ID)));
            storeInfo.setName(cursorChats.getString(cursorChats.getColumnIndex(DatabaseContract.StoreColumn.STORE_NAME)));
            storeInfo.setLastMessage(cursorChats.getString(cursorChats.getColumnIndex(DatabaseContract.StoreColumn.COLUMN_LAST_MESSAGE)));
            storeInfo.setLastTimeMessage(cursorChats.getString(cursorChats.getColumnIndex(DatabaseContract.StoreColumn.COLUMN_LAST_TIME_MESSAGE)));
            storeInfo.setLogo(cursorChats.getString(cursorChats.getColumnIndex(DatabaseContract.StoreColumn.COLUMN_LOGO_STORE)));
            storeInfos.add(storeInfo);
        }
        cursorChats.close();
        return storeInfos;
    }



    /*
     *
     * Chat
     *
     * */

    public void insertListChat(List<ChatMessage> listChat) {
        if (Utils.isNotEmpty(listChat)) {
            for (ChatMessage chatMessage : listChat) {
                if (chatMessage != null && !checkExitsChatId(chatMessage.getId())) {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseContract.ChatColumns.COLUMN_CHAT_ID, chatMessage.getId());
                    long storeId = chatMessage.isUser() ? chatMessage.getToId() : chatMessage.getFromId();
                    values.put(DatabaseContract.ChatColumns.COLUMN_STORE_ID, storeId);
                    values.put(DatabaseContract.ChatColumns.COLUMN_CONTENT, chatMessage.getContent());
                    values.put(DatabaseContract.ChatColumns.COLUMN_IMAGES, Constants.EMPTY_STRING);
                    values.put(DatabaseContract.ChatColumns.COLUMN_VIDEOS, Constants.EMPTY_STRING);
                    values.put(DatabaseContract.ChatColumns.COLUMN_IS_USER, chatMessage.isUser() ? 1 : 0);
                    values.put(DatabaseContract.ChatColumns.COLUMN_CREATED, chatMessage.getCreated());
                    mDatabaseWrite.insert(DatabaseContract.ChatColumns.TABLE_NAME, null, values);
                }
            }
        }
    }

    private boolean checkExitsChatId(long chatId) {
        String columnSelection[] = new String[]{DatabaseContract.ChatColumns.COLUMN_CHAT_ID};
        String selection = DatabaseContract.ChatColumns.COLUMN_CHAT_ID + " = ?";
        String selectionArgs[] = new String[]{String.valueOf(chatId)};

        Cursor cursorPush = mDatabaseRead.query(DatabaseContract.ChatColumns.TABLE_NAME, columnSelection, selection, selectionArgs, null, null, null);
        boolean isExits = cursorPush.moveToNext();
        cursorPush.close();
        return isExits;
    }

    public List<ChatMessage> getListChatByLastChatId(long storeId, long lastChatId) {
        String selection = DatabaseContract.ChatColumns.COLUMN_STORE_ID + " = ? AND " + DatabaseContract.ChatColumns.COLUMN_CHAT_ID + " < ?";
        String selectionArgs[] = new String[]{String.valueOf(storeId), String.valueOf(lastChatId)};
        String orderBy = DatabaseContract.ChatColumns.COLUMN_CHAT_ID + " DESC";
        Cursor cursorChats = mDatabaseRead.query(DatabaseContract.ChatColumns.TABLE_NAME, null, selection, selectionArgs, null, null, orderBy, String.valueOf(Constants.MAX_ITEM_PAGE_CHAT));
        List<ChatMessage> chatMessages = new ArrayList<>();
        while (cursorChats.moveToNext()) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setId(cursorChats.getLong(cursorChats.getColumnIndex(DatabaseContract.ChatColumns.COLUMN_CHAT_ID)));
            chatMessage.setContent(cursorChats.getString(cursorChats.getColumnIndex(DatabaseContract.ChatColumns.COLUMN_CONTENT)));
            int isUser = cursorChats.getInt(cursorChats.getColumnIndex(DatabaseContract.ChatColumns.COLUMN_IS_USER));
            chatMessage.setUser(isUser == 1);
            chatMessage.setCreated(cursorChats.getLong(cursorChats.getColumnIndex(DatabaseContract.ChatColumns.COLUMN_CREATED)));
            chatMessages.add(0, chatMessage);
        }
        cursorChats.close();

        return chatMessages;
    }

    public long getChatId(long storeId, boolean isNewest) {
        String columnSelection[] = new String[]{DatabaseContract.ChatColumns.COLUMN_CHAT_ID};
        String selection = DatabaseContract.ChatColumns.COLUMN_STORE_ID + " = ?";
        String selectionArgs[] = new String[]{String.valueOf(storeId)};
        String sort = isNewest ? " DESC" : " ASC";
        String orderBy = DatabaseContract.ChatColumns.COLUMN_CHAT_ID + sort;
        Cursor cursorChats = mDatabaseRead.query(DatabaseContract.ChatColumns.TABLE_NAME, columnSelection, selection, selectionArgs, null, null, orderBy, "1");
        long chatId = 0;
        if (cursorChats.moveToFirst()) {
            chatId = cursorChats.getLong(cursorChats.getColumnIndex(DatabaseContract.ChatColumns.COLUMN_CHAT_ID));
            cursorChats.close();
        }
        return chatId;
    }

    public void deleteChatId(long lastChatId) {
        String whereClause = DatabaseContract.ChatColumns.COLUMN_CHAT_ID + " > ? ";
        String[] whereArgs = new String[]{String.valueOf(lastChatId)};
        mDatabaseWrite.delete(DatabaseContract.ChatColumns.TABLE_NAME, whereClause, whereArgs);
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
        mDatabaseRead.execSQL("DELETE FROM " + DatabaseContract.ChatColumns.TABLE_NAME);
    }
}
