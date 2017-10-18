package jp.co.wssj.iungo.screens.listservicecompanywrapper;

import java.util.List;

import jp.co.wssj.iungo.model.stamp.ListCompanyResponse;
import jp.co.wssj.iungo.screens.switcher.ISwitcherView;

/**
 * Created by HieuPT on 10/17/2017.
 */

public interface IListServiceCompanyWrapperView extends ISwitcherView {

    void displayServiceCompanyScreen(List<ListCompanyResponse.ListCompanyData.CompanyData> companyDataList);

    void displayListCardScreen(ListCompanyResponse.ListCompanyData.CompanyData companyData);

    void displayNotificationForServiceScreen(ListCompanyResponse.ListCompanyData.CompanyData companyData);
}
