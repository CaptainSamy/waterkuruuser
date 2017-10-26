package jp.co.wssj.iungo.screens.pushnotification.pushquestionnaire;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.pushnotification.PushNotificationModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 26/10/2017.
 */

public class PushTypeQuestionNairePresenter extends FragmentPresenter<IPushTypeQuestionNaireView> {

    protected PushTypeQuestionNairePresenter(IPushTypeQuestionNaireView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new PushNotificationModel(view.getViewContext()));
    }

    public void getListPushNotification(long userPushId, int isSearch, String keySearch) {
        String token = "6899c690-0607-4a1f-9c2e-283c1e747cec";// getModel(SharedPreferencesModel.class).getToken();
        getModel(PushNotificationModel.class).getListPushQuestionNaire(token, userPushId, isSearch, keySearch, new PushNotificationModel.IGetListPushNotificationCallback() {

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
