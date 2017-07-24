package jp.co.wssj.iungo.screens.listcard;

import java.util.List;

import jp.co.wssj.iungo.model.stamp.ListCardResponse;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by HieuPT on 6/1/2017.
 */

interface IListCardView extends IFragmentView {

    void showListUnusedCard(List<ListCardResponse.ListCardData.CardData> cardDataList);

    void hideListUnusedCard(int numberStampInCard);

    void showListCanUseCard(List<ListCardResponse.ListCardData.CardData> cardDataList);

    void hideListCanUseCard();

    void showListUsedCard(List<ListCardResponse.ListCardData.CardData> cardDataList, int page, int totalPage);

    void hideListUsedCard();

    void displayEditServiceMemoScreen(int serviceId);
}
