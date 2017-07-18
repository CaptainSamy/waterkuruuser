package jp.co.wssj.iungo.model.pushnotification;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;

/**
 * Created by tuanle on 6/7/17.
 */

public class ListNotificationResponse extends ResponseData<ListNotificationResponse.PushNotificationListData> {

    public class PushNotificationListData implements GsonSerializable {

        @SerializedName("page")
        private int mPage;

        @SerializedName("total_page")
        private int mTotalPage;

        @SerializedName("limit")
        private int mLimit;

        @SerializedName("total_number_of_notifications")
        private int mTotalNumberOfNotification;

        @SerializedName("unread_push")
        private int mUnreadPush;

        @SerializedName("number_of_notifications_in_page")
        private int mNumberOfNotificationInPage;

        @SerializedName("push_notification")
        private List<NotificationMessage> mList;

        public List<NotificationMessage> getListPushNotification() {
            return mList;
        }

        public int getTotalPage() {
            return mTotalPage;
        }

        public int getPage() {
            return mPage;
        }

        public int getLimit() {
            return mLimit;
        }

        public int getNumberOfNotificationInPage() {
            return mNumberOfNotificationInPage;
        }

        public int getTotalNumberOfNotification() {
            return mTotalNumberOfNotification;
        }

        public int getUnreadPush() {
            return mUnreadPush;
        }

    }

}
