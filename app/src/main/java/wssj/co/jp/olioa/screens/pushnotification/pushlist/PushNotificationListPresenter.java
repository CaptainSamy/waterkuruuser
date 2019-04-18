package wssj.co.jp.olioa.screens.pushnotification.pushlist;

import android.text.TextUtils;

import java.util.List;

import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationModel;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationResponse;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;
import wssj.co.jp.olioa.utils.Logger;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationListPresenter extends FragmentPresenter<IPushNotificationListView> {

    private static final String TAG = "PushNotificationListPresenter";

    protected PushNotificationListPresenter(IPushNotificationListView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new PushNotificationModel(view.getViewContext()));
    }

    public void getListPushNotification(int page) {
        getView().showProgress();
        getModel(PushNotificationModel.class).getListPushNotification(page, new APICallback<PushNotificationResponse>() {

            @Override
            public void onSuccess(PushNotificationResponse response) {
                getView().hideProgress();
                getView().showListPushNotification(response);
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                getView().showToast(errorMessage);
            }
        });
    }
}
