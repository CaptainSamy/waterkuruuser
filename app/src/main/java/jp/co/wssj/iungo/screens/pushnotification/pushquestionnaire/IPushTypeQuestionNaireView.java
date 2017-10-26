package jp.co.wssj.iungo.screens.pushnotification.pushquestionnaire;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 26/10/2017.
 */

public interface IPushTypeQuestionNaireView extends IFragmentView {

    void showListPushNotification(List<NotificationMessage> list, int page, int totalPage);

    void displayErrorMessage(ErrorMessage errorMessage);
}
