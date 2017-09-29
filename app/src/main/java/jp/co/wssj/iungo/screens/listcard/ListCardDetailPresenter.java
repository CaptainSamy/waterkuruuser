package jp.co.wssj.iungo.screens.listcard;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.stamp.ListCardResponse.ListCardData.CardData;
import jp.co.wssj.iungo.model.stamp.StampModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by HieuPT on 6/1/2017.
 */

class ListCardDetailPresenter extends FragmentPresenter<IListCardDetailView> {

    ListCardDetailPresenter(IListCardDetailView view) {
        super(view);
        registerModel(new StampModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    void getListCardByServiceCompany(int serviceCompanyId, int page, int limit) {
        getView().showProgress();
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(StampModel.class).getListCardByServiceCompany(token, serviceCompanyId, page, limit, new StampModel.IGetListCardResponse() {

            @Override
            public void onSuccess(List<CardData> cards, int page, int totalPage, int numberStampInCard,String explain) {
                List<CardData> unusedList = Utils.getListCardByType(cards, Constants.CardTypes.CARD_TYPE_UNUSED);
                if (!unusedList.isEmpty()) {
                    getView().showListUnusedCard(unusedList,explain);
                } else {
                    getView().hideListUnusedCard(numberStampInCard,explain);
                }
                List<CardData> canUseList = Utils.getListCardByType(cards, Constants.CardTypes.CARD_TYPE_CAN_USE);

                if (!canUseList.isEmpty()) {
                    getView().showListCanUseCard(canUseList);
                } else {
                    getView().hideListCanUseCard();
                }
                List<CardData> usedList = Utils.getListCardByType(cards, Constants.CardTypes.CARD_TYPE_USED);
                if (!usedList.isEmpty()) {
                    getView().showListUsedCard(usedList, page, totalPage);
                } else {
                    getView().hideListUsedCard();
                }
                getView().hideProgress();
            }

            @Override
            public void onFailure(ErrorMessage errorMessage) {
                getView().hideProgress();
                getView().hideListUnusedCard(0,Constants.EMPTY_STRING);
                getView().hideListCanUseCard();
                getView().hideListUsedCard();
            }
        });
    }

    void onEditServiceMemoClicked(int serviceId) {
        getView().displayEditServiceMemoScreen(serviceId);
    }
}
