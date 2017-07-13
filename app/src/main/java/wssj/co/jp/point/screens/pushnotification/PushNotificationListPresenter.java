package wssj.co.jp.point.screens.pushnotification;

import java.util.List;

import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.model.firebase.NotificationMessage;
import wssj.co.jp.point.model.preference.SharedPreferencesModel;
import wssj.co.jp.point.model.pushnotification.PushNotificationModel;
import wssj.co.jp.point.screens.base.FragmentPresenter;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationListPresenter extends FragmentPresenter<IPushNotificationListView> {

    protected PushNotificationListPresenter(IPushNotificationListView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new PushNotificationModel(view.getViewContext()));
    }

    public void getListPushNotification(int page, int limit) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(PushNotificationModel.class).getListPushNotification(token, page, limit, new PushNotificationModel.IGetListPushNotificationCallback() {

            @Override
            public void onGetListPushNotificationSuccess(List<NotificationMessage> list, int page, int totalPage) {
                getView().showListPushNotification(list, page, totalPage);
            }

            @Override
            public void onGetListPushNotificationFailure(ErrorMessage errorMessage) {
                getView().displayErrorMessage(errorMessage);
            }
        });
    }

}
