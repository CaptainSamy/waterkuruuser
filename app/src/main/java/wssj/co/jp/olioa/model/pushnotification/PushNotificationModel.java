package wssj.co.jp.olioa.model.pushnotification;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.BaseModel;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.ErrorResponse;
import wssj.co.jp.olioa.model.ResponseData;
import wssj.co.jp.olioa.model.firebase.NotificationMessage;
import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Utils;
import wssj.co.jp.olioa.utils.VolleySequence;

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
