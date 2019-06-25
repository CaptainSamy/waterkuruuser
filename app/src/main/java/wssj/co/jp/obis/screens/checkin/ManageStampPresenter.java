package wssj.co.jp.obis.screens.checkin;

import android.text.TextUtils;

import java.util.List;

import wssj.co.jp.obis.model.ErrorMessage;
import wssj.co.jp.obis.model.checkin.CheckInModel;
import wssj.co.jp.obis.model.checkin.CheckInStatusResponse;
import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.model.stamp.ListCardResponse;
import wssj.co.jp.obis.model.stamp.StampModel;
import wssj.co.jp.obis.screens.base.FragmentPresenter;
import wssj.co.jp.obis.utils.Constants;
import wssj.co.jp.obis.utils.Logger;
import wssj.co.jp.obis.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 23/5/2017.
 */

class ManageStampPresenter extends FragmentPresenter<IManageStampView> {

    ManageStampPresenter(IManageStampView view) {
        super(view);
        registerModel(new CheckInModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new StampModel(view.getViewContext()));
    }

    void getCheckInStatus() {
        String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getModel(CheckInModel.class).getCheckInStatus(token, new CheckInModel.IGetCheckInStatusCallback() {

                @Override
                public void onCheckInStatusSuccess(CheckInStatusResponse.CheckInStatusData data) {
                    Logger.d("ManageStampPresenter", "#onCheckInStatusSuccess");
                }

                @Override
                public void onCheckInStatusFailure(ErrorMessage errorMessage) {
                    Logger.d("ManageStampPresenter", "#onCheckInStatusSuccess");
                    if (isViewAttached() && errorMessage != null && errorMessage.getMessage().equals(Constants.CheckInStatus.STATUS_CHECKED_OUT)) {
                        getModel(SharedPreferencesModel.class).clearSession();
                        getView().displayMyStampScreen();
                    }
                }
            });
        }
    }

    void onEditServiceMemoClicked() {
        int serviceId = getModel(SharedPreferencesModel.class).getServiceId();
        getView().displayEditServiceMemoScreen(serviceId);
    }

    void getListCardByServiceCompany(int page, int limit) {
        getView().showProgress();
        final String token = getModel(SharedPreferencesModel.class).getToken();
        int serviceCompanyId = getModel(SharedPreferencesModel.class).getServiceCompanyId();
        getModel(StampModel.class).getListCardByServiceCompany(token, serviceCompanyId, page, limit, new StampModel.IGetListCardResponse() {

            @Override
            public void onSuccess(List<ListCardResponse.ListCardData.CardData> cards, int page, int totalPage, int numberStampInCard, String explain) {
                getView().hideProgress();
                List<ListCardResponse.ListCardData.CardData> unusedList = Utils.getListCardByType(cards, Constants.CardTypes.CARD_TYPE_UNUSED);
                if (!unusedList.isEmpty()) {
                    getView().showListUnusedCard(unusedList, explain);
                } else {
                    getView().showListUnusedCardEmpty(numberStampInCard, explain);
                }
                List<ListCardResponse.ListCardData.CardData> canUseList = Utils.getListCardByType(cards, Constants.CardTypes.CARD_TYPE_CAN_USE);
                if (!canUseList.isEmpty()) {
                    getView().showListCanUseCard(canUseList, page, totalPage);
                } else {
                    getView().hideListCanUseCard();
                }
            }

            @Override
            public void onFailure(ErrorMessage errorMessage) {
                getView().hideProgress();
                getView().showListUnusedCardEmpty(0, Constants.EMPTY_STRING);
                getView().hideListCanUseCard();
            }
        });
    }
}