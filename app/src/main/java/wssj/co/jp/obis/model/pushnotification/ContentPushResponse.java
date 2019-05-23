package wssj.co.jp.obis.model.pushnotification;

import com.google.gson.annotations.SerializedName;

import wssj.co.jp.obis.model.GsonSerializable;
import wssj.co.jp.obis.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 28/7/2017.
 */

public class ContentPushResponse extends ResponseData<ContentPushResponse.ContentPushData> {

    public class ContentPushData implements GsonSerializable {

        @SerializedName("badge")
        private String mBadge;

        @SerializedName("content")
        private String mContent;

        @SerializedName("created")
        private long mCreated;

        @SerializedName("id")
        private int mId;

        @SerializedName("key_gen")
        private String mKeyGen;

        @SerializedName("management_user_id")
        private int mUserManagerId;

        @SerializedName("modified")
        private long mModified;

        @SerializedName("send_time")
        private long mSendTime;

        @SerializedName("sort_description")
        private String mShortDescription;

        @SerializedName("sound")
        private String mSound;

        @SerializedName("status")
        private int mStatus;

        @SerializedName("title")
        private String mTitle;

        @SerializedName("type")
        private int mType;

        public String getBadge() {
            return mBadge;
        }

        public String getContent() {
            return mContent;
        }

        public long getCreated() {
            return mCreated;
        }

        public int getId() {
            return mId;
        }

        public String getkeyGen() {
            return mKeyGen;
        }

        public int getUserManagerId() {
            return mUserManagerId;
        }

        public long getModified() {
            return mModified;
        }

        public long getSendTime() {
            return mSendTime;
        }

        public String getShortDescription() {
            return mShortDescription;
        }

        public String getSound() {
            return mSound;
        }

        public int getmStatus() {
            return mStatus;
        }

        public String getTitle() {
            return mTitle;
        }

        public int getType() {
            return mType;
        }
    }

}
