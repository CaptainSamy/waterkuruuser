package wssj.co.jp.point.model.firebase;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import wssj.co.jp.point.model.GsonSerializable;

/**
 * Created by tuanle on 6/1/17.
 */

public class NotificationMessage implements GsonSerializable, Serializable {

    @SerializedName("id")
    private int mPushId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("content")
    private String mMessage;

    @SerializedName("send_time")
    private long mPushTime;

    private boolean mIsSound;

    /*
    *  1 - read
    *  0 - not read
    * */
    @SerializedName("status_read")
    private int mStatusRead;

    @SerializedName("type")
    private int mAction;

    public NotificationMessage(int pushId, String title, String message) {
        this.mTitle = title;
        this.mMessage = message;
        mPushId = pushId;
        mIsSound = true;
        mPushTime = System.currentTimeMillis();
    }

    public long getPushTime() {
        return mPushTime;
    }

    public int getPushId() {
        return mPushId;
    }

    public String getMessage() {
        return mMessage;
    }

    public boolean isSound() {
        return mIsSound;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getStatusRead() {
        return mStatusRead;
    }

    public int getAction() {
        return mAction;
    }
}
