package wssj.co.jp.obis.screens.listcard;

import java.util.List;

import wssj.co.jp.obis.model.ErrorMessage;
import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.model.stamp.ListCardResponse;
import wssj.co.jp.obis.model.stamp.StampModel;
import wssj.co.jp.obis.screens.base.FragmentPresenter;
import wssj.co.jp.obis.utils.Constants;
import wssj.co.jp.obis.utils.Utils;

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
            public void onSuccess(List<ListCardResponse.ListCardData.CardData> cards, int page, int totalPage, int numberStampInCard, String explain) {
                List<ListCardResponse.ListCardData.CardData> unusedList = Utils.getListCardByType(cards, Constants.CardTypes.CARD_TYPE_UNUSED);
                if (!unusedList.isEmpty()) {
                    getView().showListUnusedCard(unusedList,explain);
                } else {
                    getView().hideListUnusedCard(numberStampInCard,explain);
                }
                List<ListCardResponse.ListCardData.CardData> canUseList = Utils.getListCardByType(cards, Constants.CardTypes.CARD_TYPE_CAN_USE);

                if (!canUseList.isEmpty()) {
                    getView().showListCanUseCard(canUseList);
                } else {
                    getView().hideListCanUseCard();
                }
                List<ListCardResponse.ListCardData.CardData> usedList = Utils.getListCardByType(cards, Constants.CardTypes.CARD_TYPE_USED);
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
