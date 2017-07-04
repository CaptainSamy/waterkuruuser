package wssj.co.jp.point.screens.checkin;

import java.util.List;

import wssj.co.jp.point.model.stamp.ListCardResponse;
import wssj.co.jp.point.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 23/5/2017.
 */

interface IManageStampView extends IFragmentView {

    void showListUnusedCard(List<ListCardResponse.ListCardData.CardData> cardDataList);

    void showListUnusedCardEmpty();

    void showListCanUseCard(List<ListCardResponse.ListCardData.CardData> cardDataList,int page,int totalPage);

    void hideListCanUseCard();

    void displayEditServiceMemoScreen(int serviceId);

    void displayMyStampScreen();
}
