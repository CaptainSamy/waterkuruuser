package jp.co.wssj.iungo.screens.listservicecompany;

import java.util.List;

import jp.co.wssj.iungo.model.stamp.ListCompanyResponse;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by HieuPT on 17/5/2017.
 */

interface IListServiceCompanyView extends IFragmentView {

    void displayListCardScreen(int serviceCompanyId, int serviceId, String serviceName, String cardName);

    void showListCompany(List<ListCompanyResponse.ListCompanyData.CompanyData> cardList);

    void showNoCompany();
}
