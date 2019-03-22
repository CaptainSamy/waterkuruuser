package wssj.co.jp.olioa.screens.listservicecompanywrapper;

import java.util.List;

import wssj.co.jp.olioa.model.stamp.ListCompanyResponse;
import wssj.co.jp.olioa.screens.switcher.ISwitcherView;

/**
 * Created by HieuPT on 10/17/2017.
 */

public interface IListServiceCompanyWrapperView extends ISwitcherView {

    void displayServiceCompanyScreen(List<ListCompanyResponse.ListCompanyData.CompanyData> companyDataList);

    void displayListCardScreen(ListCompanyResponse.ListCompanyData.CompanyData companyData);

    void displayNotificationForServiceScreen(ListCompanyResponse.ListCompanyData.CompanyData companyData);
}
