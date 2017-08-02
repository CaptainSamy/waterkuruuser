package jp.co.wssj.iungo.screens.pushnotificationforstore;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.pushnotification.ListPushForServiceCompanyResponse;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by tuanle on 6/7/17.
 */

public interface IPushNotificationForServiceCompany extends IFragmentView {

    void showListPushNotification(List<ListPushForServiceCompanyResponse.ListPushForServiceCompany.PushNotification> list, int page, int totalPage);

    void displayErrorMessage(ErrorMessage errorMessage);
}
