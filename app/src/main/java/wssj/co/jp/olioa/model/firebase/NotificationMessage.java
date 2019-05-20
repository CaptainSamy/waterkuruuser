package wssj.co.jp.olioa.model.firebase;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

import wssj.co.jp.olioa.model.GsonSerializable;

/**
 * Created by tuanle on 6/1/17.
 */

public class NotificationMessage implements GsonSerializable, Parcelable {

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

    private int storeId;

    private int groupId;

    private int day;

    protected NotificationMessage(Parcel in) {
        mPushId = in.readLong();
        mUserPushId = in.readLong();
        mTitle = in.readString();
        mMessage = in.readString();
        mPushTime = in.readLong();
        mLogo = in.readString();
        day = in.readInt();
        mIsSound = in.readByte() != 0;
        mStatusRead = in.readInt();
        mAction = in.readString();
        mStampId = in.readInt();
        mIsLike = in.readInt();
        mStoreAnnounce = in.readInt();
        storeId = in.readInt();
        groupId = in.readInt();
    }

    public static final Creator<NotificationMessage> CREATOR = new Creator<NotificationMessage>() {

        @Override
        public NotificationMessage createFromParcel(Parcel in) {
            return new NotificationMessage(in);
        }

        @Override
        public NotificationMessage[] newArray(int size) {
            return new NotificationMessage[size];
        }
    };

    public int getDay() {
        if (mPushTime != 0) {
            String dateFormat = "dd/MM/yy";
            String dateConvert = new SimpleDateFormat(dateFormat).format(new Date(mPushTime));
            if (!TextUtils.isEmpty(dateConvert)) {
                String[] date = dateConvert.split("/");
                if (date != null && date.length > 0) {
                    day = Integer.parseInt(date[0]);
                }

            }
            return day;
        }

        return 0;
    }

    public void setDay(int dateContert) {
        this.day = dateContert;
    }

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

    public NotificationMessage(long pushId, String title, String message, String action, int storeId, int groupId) {
        this.mTitle = title;
        this.mMessage = message;
        mAction = action;
        this.storeId = storeId;
        this.groupId = groupId;
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


    public int getGroupId() {
        return groupId;
    }

    public int getStoreId() {
        return storeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mPushId);
        dest.writeLong(mUserPushId);
        dest.writeString(mTitle);
        dest.writeString(mMessage);
        dest.writeLong(mPushTime);
        dest.writeString(mLogo);
        dest.writeInt(day);
        dest.writeByte((byte) (mIsSound ? 1 : 0));
        dest.writeInt(mStatusRead);
        dest.writeString(mAction);
        dest.writeInt(mStampId);
        dest.writeInt(mIsLike);
        dest.writeInt(mStoreAnnounce);
        dest.writeInt(storeId);
        dest.writeInt(groupId);
    }
}
