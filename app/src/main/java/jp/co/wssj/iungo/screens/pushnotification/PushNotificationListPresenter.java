package jp.co.wssj.iungo.screens.pushnotification;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.pushnotification.PushNotificationModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

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
