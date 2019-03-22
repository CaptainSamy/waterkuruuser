package wssj.co.jp.olioa.model.pushnotification;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import wssj.co.jp.olioa.model.entities.PushNotification;

/**
 * Created by tuanle on 6/1/17.
 */

public class PushNotificationResponse {

    @SerializedName("maxPage")
    private int mTotalPage;

    @SerializedName("listPush")
    private List<PushNotification> mList;

    public int getTotalPage() {
        return mTotalPage;
    }

    public List<PushNotification> getListPushNotification() {
        return mList;
    }
}
