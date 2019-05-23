package wssj.co.jp.obis.model.pushnotification;

import android.content.Context;

import wssj.co.jp.obis.model.BaseModel;
import wssj.co.jp.obis.model.baseapi.APICallback;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationModel extends BaseModel {


    public PushNotificationModel(Context context) {
        super(context);
    }

    public void getListPushNotification(int page, APICallback<PushNotificationResponse> callback) {
        getApi().getPush(page).getAsyncResponse(callback);
    }


}
