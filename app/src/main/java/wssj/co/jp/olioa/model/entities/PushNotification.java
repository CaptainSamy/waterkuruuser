package wssj.co.jp.olioa.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tuanle on 6/1/17.
 */

public class PushNotification implements Serializable, Parcelable {

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

    protected PushNotification(Parcel in) {
        mId = in.readInt();
        mManagerUserId = in.readInt();
        mTitle = in.readString();
        mShortDesc = in.readString();
        mBody = in.readString();
        mSound = in.readString();
        mBadge = in.readString();
        mSendTime = in.readLong();
        mStatus = in.readInt();
        logo = in.readString();
    }

    public static final Creator<PushNotification> CREATOR = new Creator<PushNotification>() {

        @Override
        public PushNotification createFromParcel(Parcel in) {
            return new PushNotification(in);
        }

        @Override
        public PushNotification[] newArray(int size) {
            return new PushNotification[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeInt(mManagerUserId);
        dest.writeString(mTitle);
        dest.writeString(mShortDesc);
        dest.writeString(mBody);
        dest.writeString(mSound);
        dest.writeString(mBadge);
        dest.writeLong(mSendTime);
        dest.writeInt(mStatus);
        dest.writeString(logo);
    }
}
