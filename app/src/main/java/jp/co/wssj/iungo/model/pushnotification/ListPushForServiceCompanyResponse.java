package jp.co.wssj.iungo.model.pushnotification;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 2/8/2017.
 */

public class ListPushForServiceCompanyResponse extends ResponseData<ListPushForServiceCompanyResponse.ListPushForServiceCompany> {

    public class ListPushForServiceCompany implements GsonSerializable {

        @SerializedName("limit")
        private int mLimit;

        @SerializedName("number_of_notifications_in_page")
        private int mCountInPage;

        @SerializedName("page")
        private int mPage;

        @SerializedName("total_number_of_notifications")
        private int mTotalItem;

        @SerializedName("total_page")
        private int mTotalPage;

        @SerializedName("push_notification")
        private List<PushNotification> mList;

        public int getLimit() {
            return mLimit;
        }

        public int getCountInPage() {
            return mCountInPage;
        }

        public int getPage() {
            return mPage;
        }

        public int getTotalItem() {
            return mTotalItem;
        }

        public int getTotalPage() {
            return mTotalPage;
        }

        public List<PushNotification> getListPushNotification() {
            return mList;
        }

        public class PushNotification implements Serializable {

            @SerializedName("id")
            private int mId;

            @SerializedName("store_id")
            private int mStoreId;

            @SerializedName("management_user_id")
            private int mManagerUserId;

            @SerializedName("title")
            private String mTitle;

            @SerializedName("sort_description")
            private String mShortDesc;

            @SerializedName("content")
            private String mBody;

            @SerializedName("sound")
            private String mSound;

            @SerializedName("badge")
            private String mBadge;

            @SerializedName("send_time")
            private long mSendTime;

            @SerializedName("status")
            private int mStatus;

            @SerializedName("logo")
            private String mLogo;

            @SerializedName("stores")
            private List<String> mListStores;

            public String getShortDesc() {
                return mShortDesc;
            }

            public void setShortDesc(String shortDesc) {
                mShortDesc = shortDesc;
            }

            public String getTitle() {
                return mTitle;
            }

            public void setTitle(String mTitle) {
                this.mTitle = mTitle;
            }

            public String getBody() {
                return mBody;
            }

            public void setBody(String mBody) {
                this.mBody = mBody;
            }

            public long getSendTime() {
                return mSendTime;
            }

            public void setSendTime(long mSendTime) {
                this.mSendTime = mSendTime;
            }

            public int getStatus() {
                return mStatus;
            }

            public void setStatus(int mStatus) {
                this.mStatus = mStatus;
            }

            public List<String> getListStores() {
                return mListStores;
            }

            public String getLogo() {
                return mLogo;
            }
        }
    }

}
