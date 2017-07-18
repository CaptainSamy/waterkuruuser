package jp.co.wssj.iungo.model.firebase;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import jp.co.wssj.iungo.model.GsonSerializable;

/**
 * Created by tuanle on 6/1/17.
 */

public class NotificationMessage implements GsonSerializable, Serializable {

    @SerializedName("id")
    private long mPushId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("content")
    private String mMessage;

    @SerializedName("send_time")
    private long mPushTime;

    @SerializedName("logo")
    private String mLogo;

    private boolean mIsSound;

    /*
    *  1 - read
    *  0 - not read
    * */
    @SerializedName("status_read")
    private int mStatusRead;

    /*
    * 1 : TYPE_NOTIFICATION
    * 2 : TYPE_REMIND
    * 3 : TYPE_REQUEST_REVIEW
    * */
    @SerializedName("type")
    private String mAction;

    @SerializedName("stamp_id")
    private int mStampId;

    public NotificationMessage(long pushId, String title, String message, String action, int stampId) {
        this.mTitle = title;
        this.mMessage = message;
        mAction = action;
        mStampId = stampId;
        mPushId = pushId;
        mIsSound = true;
        mPushTime = System.currentTimeMillis();
    }

    public long getPushTime() {
        return mPushTime;
    }

    public long getPushId() {
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

    public String getAction() {
        return mAction;
    }

    public int getStampId() {
        return mStampId;
    }

    public String getLogo() {
        return mLogo;
    }
}
