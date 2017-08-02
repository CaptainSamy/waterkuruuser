package jp.co.wssj.iungo.screens.pushnotificationforstore;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.pushnotification.ListPushForServiceCompanyResponse;
import jp.co.wssj.iungo.model.pushnotification.PushNotificationModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationForServiceCompanyPresenter extends FragmentPresenter<IPushNotificationForServiceCompany> {

    protected PushNotificationForServiceCompanyPresenter(IPushNotificationForServiceCompany view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new PushNotificationModel(view.getViewContext()));
    }

    public void getListPushNotification(int serviceCompanyId, int page, int limit) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(PushNotificationModel.class).getListPushNotificationForServiceCompany(token, serviceCompanyId, page, limit, new PushNotificationModel.IGetListPushForServiceCompanyCallback() {

            @Override
            public void onGetListPushNotificationSuccess(List<ListPushForServiceCompanyResponse.ListPushForServiceCompany.PushNotification> list, int page, int totalPage) {
                getView().showListPushNotification(list, page, totalPage);
            }

            @Override
            public void onGetListPushNotificationFailure(ErrorMessage errorMessage) {
                getView().displayErrorMessage(errorMessage);
            }
        });
    }

}
