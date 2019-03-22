package wssj.co.jp.olioa.screens.pushnotification.detail;

import android.text.TextUtils;

import java.util.List;

import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.model.pushnotification.ContentPushResponse;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationModel;
import wssj.co.jp.olioa.model.pushnotification.QuestionNaireResponse;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;
import wssj.co.jp.olioa.utils.Logger;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationDetailPresenter extends FragmentPresenter<IPushNotificationDetailView> {

    private static final String TAG = "PushNotificationDetailServiceCompanyPresenter";

    protected PushNotificationDetailPresenter(IPushNotificationDetailView view) {
        super(view);
        registerModel(new PushNotificationModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    public void setListPushUnRead(List<Long> pushId, int type) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getModel(PushNotificationModel.class).setListPushUnRead(token, pushId, type, new PushNotificationModel.ISetListPushNotificationCallback() {

                @Override
                public void onSetListPushNotificationSuccess() {
                    Logger.d(TAG, "onSetListPushNotificationSuccess");
                    getView().onUpdateStatusPushSuccess();
                }

                @Override
                public void onSetListPushNotificationFailure() {
                    Logger.d(TAG, "onSetListPushNotificationFailure");
                }
            });
        }
    }

    public void updateActionPush(long pushId) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getModel(PushNotificationModel.class).updateActionPush(token, pushId, new PushNotificationModel.IUpdateActionPushCallback() {

                @Override
                public void onUpdateActionPushSuccess() {

                }

                @Override
                public void onUpdateActionPushFailure() {

                }
            });
        }
    }

    public void getContentPush(long pushId) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getView().showProgress();
        getModel(PushNotificationModel.class).getContentPush(token, pushId, new PushNotificationModel.IGetContentPushCallback() {

            @Override
            public void onGetContentPushSuccess(ContentPushResponse.ContentPushData contentPushResponse) {
                getView().hideProgress();
                getView().onGetContentPushSuccess(contentPushResponse);
            }

            @Override
            public void onGetContentPushFailure(String message) {
                getView().hideProgress();
                getView().onGetContentPushFailure(message);
            }
        });
    }

    public void getQuestionNaire(long pushId) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getView().showProgress();
        getModel(PushNotificationModel.class).getQuestionNaire(token, pushId, new PushNotificationModel.IGetQuestionNaireCallback() {

            @Override
            public void onGetQuestionNaireSuccess(QuestionNaireResponse response) {
                getView().hideProgress();
                getView().onGetQuestionNaireSuccess(response);
            }

            @Override
            public void onGetQuestionNaireFailure(String message) {
                getView().hideProgress();
                getView().onGetQuestionNaireFailure();
            }
        });
    }
}
