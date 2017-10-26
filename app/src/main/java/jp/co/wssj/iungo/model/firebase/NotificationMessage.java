package jp.co.wssj.iungo.model.firebase;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import jp.co.wssj.iungo.model.GsonSerializable;

/**
 * Created by tuanle on 6/1/17.
 */

public class NotificationMessage implements GsonSerializable, Serializable {

    @SerializedName("push_id")
    private long mPustQuestionNaire;

    @SerializedName("id")
    private long mPushId;

    @SerializedName("user_push_id")
    private long mUserPushId;

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

    @SerializedName("like")
    private int mIsLike;

    private int mStoreAnnounce;

    public NotificationMessage() {

    }

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

    public void setPushId(long mPushId) {
        this.mPushId = mPushId;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public void setPushTime(long mPushTime) {
        this.mPushTime = mPushTime;
    }

    public void setLogo(String mLogo) {
        this.mLogo = mLogo;
    }

    public void setIsSound(boolean mIsSound) {
        this.mIsSound = mIsSound;
    }

    public void setmStatusRead(int mStatusRead) {
        this.mStatusRead = mStatusRead;
    }

    public void setAction(String mAction) {
        this.mAction = mAction;
    }

    public void setStampId(int mStampId) {
        this.mStampId = mStampId;
    }

    public void setIsLike(int mIsLike) {
        this.mIsLike = mIsLike;
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

    public int isLike() {
        return mIsLike;
    }

    public int getStoreAnnounce() {
        return mStoreAnnounce;
    }

    public void setStoreAnnounce(int storeAnnounce) {
        this.mStoreAnnounce = storeAnnounce;
    }

    public long getUserPushId() {
        return mUserPushId;
    }

    public void setUserPushId(long userPushId) {
        this.mUserPushId = userPushId;
    }

    public long getPustQuestionNaire() {
        return mPustQuestionNaire;
    }
}
