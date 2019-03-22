package wssj.co.jp.olioa.screens.pushnotification.detail;

import wssj.co.jp.olioa.model.pushnotification.ContentPushResponse;
import wssj.co.jp.olioa.model.pushnotification.QuestionNaireResponse;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by tuanle on 6/7/17.
 */

public interface IPushNotificationDetailView extends IFragmentView {

    void onGetContentPushSuccess(ContentPushResponse.ContentPushData contentPushResponse);

    void onGetContentPushFailure(String message);

    void onUpdateStatusPushSuccess();

    void onGetQuestionNaireSuccess(QuestionNaireResponse response);

    void onGetQuestionNaireFailure();
}
