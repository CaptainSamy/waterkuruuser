package wssj.co.jp.point.screens.listservicecompany;

import java.util.List;

import wssj.co.jp.point.model.stamp.ListCompanyResponse;
import wssj.co.jp.point.screens.base.IFragmentView;

/**
 * Created by HieuPT on 17/5/2017.
 */

interface IListServiceCompanyView extends IFragmentView {

    void displayListCardScreen(int serviceCompanyId, int serviceId, String serviceName, String cardName);

    void showListCompany(List<ListCompanyResponse.ListCompanyData.CompanyData> cardList);

    void showNoCompany();
}
