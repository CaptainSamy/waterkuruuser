package wssj.co.jp.obis.screens.listcard;

import java.util.List;

import wssj.co.jp.obis.model.stamp.ListCardResponse;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by HieuPT on 6/1/2017.
 */

interface IListCardDetailView extends IFragmentView {

    void showListUnusedCard(List<ListCardResponse.ListCardData.CardData> cardDataList, String explain);

    void hideListUnusedCard(int numberStampInCard, String explain);

    void showListCanUseCard(List<ListCardResponse.ListCardData.CardData> cardDataList);

    void hideListCanUseCard();

    void showListUsedCard(List<ListCardResponse.ListCardData.CardData> cardDataList, int page, int totalPage);

    void hideListUsedCard();

    void displayEditServiceMemoScreen(int serviceId);
}
