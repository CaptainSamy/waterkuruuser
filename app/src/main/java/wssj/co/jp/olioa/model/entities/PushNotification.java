package wssj.co.jp.olioa.model.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tuanle on 6/1/17.
 */

public class PushNotification implements Serializable {

    @SerializedName("id")
    private int mId;

    @SerializedName("storeId")
    private int mManagerUserId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("sortDescription")
    private String mShortDesc;

    @SerializedName("content")
    private String mBody;

    @SerializedName("sound")
    private String mSound;

    @SerializedName("badge")
    private String mBadge;

    @SerializedName("sendTime")
    private long mSendTime;

    @SerializedName("status")
    private int mStatus;

    @SerializedName("number_user_get_push")

    private String logo;

    public PushNotification(String title, String shortDesc, String body, long sendTime, int status) {
        mTitle = title;
        mBody = body;
        mSendTime = sendTime;
        mStatus = status;
        mShortDesc = shortDesc;
    }

    public void setManagerUserId(int mManagerUserId) {
        this.mManagerUserId = mManagerUserId;
    }

    public String getShortDesc() {
        return mShortDesc;
    }

    public void setShortDesc(String shortDesc) {
        mShortDesc = shortDesc;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public long getSendTime() {
        return mSendTime;
    }

    public void setSendTime(long sendTime) {
        mSendTime = sendTime;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public String getLogo() {
        return logo;
    }
}
