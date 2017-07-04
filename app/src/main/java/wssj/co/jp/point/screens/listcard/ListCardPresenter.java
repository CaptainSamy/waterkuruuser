package wssj.co.jp.point.screens.listcard;

import java.util.List;

import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.model.preference.SharedPreferencesModel;
import wssj.co.jp.point.model.stamp.ListCardResponse.ListCardData.CardData;
import wssj.co.jp.point.model.stamp.StampModel;
import wssj.co.jp.point.screens.base.FragmentPresenter;
import wssj.co.jp.point.utils.Constants;
import wssj.co.jp.point.utils.Utils;

/**
 * Created by HieuPT on 6/1/2017.
 */

class ListCardPresenter extends FragmentPresenter<IListCardView> {

    ListCardPresenter(IListCardView view) {
        super(view);
        registerModel(new StampModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    void getListCardByServiceCompany(int serviceCompanyId, int page, int limit) {
        getView().showProgress();
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(StampModel.class).getListCardByServiceCompany(token, serviceCompanyId, page, limit, new StampModel.IGetListCardResponse() {

            @Override
            public void onSuccess(List<CardData> cards, int page, int totalPage) {
                List<CardData> unusedList = Utils.getListCardByType(cards, Constants.CardTypes.CARD_TYPE_UNUSED);
                if (!unusedList.isEmpty()) {
                    getView().showListUnusedCard(unusedList);
                } else {
                    getView().hideListUnusedCard();
                }
                List<CardData> canUseList = Utils.getListCardByType(cards, Constants.CardTypes.CARD_TYPE_CAN_USE);
                if (!canUseList.isEmpty()) {
                    getView().showListCanUseCard(canUseList);
                } else {
                    getView().hideListCanUseCard();
                }
                List<CardData> usedList = Utils.getListCardByType(cards, Constants.CardTypes.CARD_TYPE_USED);
                if (!usedList.isEmpty()) {
                    getView().showListUsedCard(usedList,page,totalPage);
                } else {
                    getView().hideListUsedCard();
                }
                getView().hideProgress();
            }

            @Override
            public void onFailure(ErrorMessage errorMessage) {
                getView().hideProgress();
                getView().hideListUnusedCard();
                getView().hideListCanUseCard();
                getView().hideListUsedCard();
            }
        });
    }

    void onEditServiceMemoClicked(int serviceId) {
        getView().displayEditServiceMemoScreen(serviceId);
    }
}
