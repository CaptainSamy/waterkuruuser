package wssj.co.jp.obis.screens.pushnotification.pushlist;

import wssj.co.jp.obis.model.baseapi.APICallback;
import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.model.pushnotification.PushNotificationModel;
import wssj.co.jp.obis.model.pushnotification.PushNotificationResponse;
import wssj.co.jp.obis.screens.base.FragmentPresenter;

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

    public void getListPushNotification(int page, boolean isShowProgress) {
        if (isShowProgress) {
            getView().showProgress();
        }
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
