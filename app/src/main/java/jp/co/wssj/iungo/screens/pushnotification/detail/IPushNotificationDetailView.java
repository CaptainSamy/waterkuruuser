package jp.co.wssj.iungo.screens.pushnotification.detail;

import jp.co.wssj.iungo.model.pushnotification.ContentPushResponse;
import jp.co.wssj.iungo.model.pushnotification.QuestionNaireResponse;
import jp.co.wssj.iungo.screens.base.IFragmentView;

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
