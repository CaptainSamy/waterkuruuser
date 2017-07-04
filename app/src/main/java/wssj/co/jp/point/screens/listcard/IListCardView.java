package wssj.co.jp.point.screens.listcard;

import java.util.List;

import wssj.co.jp.point.model.stamp.ListCardResponse;
import wssj.co.jp.point.screens.base.IFragmentView;

/**
 * Created by HieuPT on 6/1/2017.
 */

interface IListCardView extends IFragmentView {

    void showListUnusedCard(List<ListCardResponse.ListCardData.CardData> cardDataList);

    void hideListUnusedCard();

    void showListCanUseCard(List<ListCardResponse.ListCardData.CardData> cardDataList);

    void hideListCanUseCard();

    void showListUsedCard(List<ListCardResponse.ListCardData.CardData> cardDataList, int page, int totalPage);

    void hideListUsedCard();

    void displayEditServiceMemoScreen(int serviceId);
}
