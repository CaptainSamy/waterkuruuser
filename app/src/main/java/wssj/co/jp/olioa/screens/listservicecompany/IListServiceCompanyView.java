package wssj.co.jp.olioa.screens.listservicecompany;

import java.util.List;

import wssj.co.jp.olioa.model.stamp.ListCompanyResponse;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by HieuPT on 17/5/2017.
 */

interface IListServiceCompanyView extends IFragmentView {

    void displayListCardScreen(int serviceCompanyId, int serviceId, String serviceName, String cardName, int cardType);

    void showListCompany(List<ListCompanyResponse.ListCompanyData.CompanyData> cardList);

    void showNoCompany();
}
