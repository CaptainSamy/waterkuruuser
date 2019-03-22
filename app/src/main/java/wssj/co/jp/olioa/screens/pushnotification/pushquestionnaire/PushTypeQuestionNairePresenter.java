package wssj.co.jp.olioa.screens.pushnotification.pushquestionnaire;

import android.text.TextUtils;

import java.util.List;

import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.firebase.NotificationMessage;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;
import wssj.co.jp.olioa.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 26/10/2017.
 */

public class PushTypeQuestionNairePresenter extends FragmentPresenter<IPushTypeQuestionNaireView> {

    protected PushTypeQuestionNairePresenter(IPushTypeQuestionNaireView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new PushNotificationModel(view.getViewContext()));
    }

    public void getListPushQuestionNaire(long userPushId, int isSearch, String keySearch) {
        String token = getModel(SharedPreferencesModel.class).getToken();

        getModel(PushNotificationModel.class).getListPushNotification(token, userPushId, isSearch, keySearch, 0, Constants.TypePush.TYPE_QUESTION_NAIRE_PUSH,Constants.EMPTY_STRING,Constants.EMPTY_STRING, new PushNotificationModel.IGetListPushNotificationCallback() {

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

    public void setListPushUnRead(List<Long> pushId, int type) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getModel(PushNotificationModel.class).setListPushUnRead(token, pushId, type, new PushNotificationModel.ISetListPushNotificationCallback() {

                @Override
                public void onSetListPushNotificationSuccess() {
                }

                @Override
                public void onSetListPushNotificationFailure() {
                }
            });
        }
    }
}
