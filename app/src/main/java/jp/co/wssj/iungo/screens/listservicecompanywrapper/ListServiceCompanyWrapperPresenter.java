package jp.co.wssj.iungo.screens.listservicecompanywrapper;

import java.util.List;

import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.stamp.ListCompanyResponse;
import jp.co.wssj.iungo.model.stamp.StampModel;
import jp.co.wssj.iungo.screens.switcher.SwitcherPresenter;

/**
 * Created by HieuPT on 10/17/2017.
 */

public class ListServiceCompanyWrapperPresenter extends SwitcherPresenter<IListServiceCompanyWrapperView> {

    protected ListServiceCompanyWrapperPresenter(IListServiceCompanyWrapperView view) {
        super(view);
        registerModel(new StampModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    public void getCompanyList() {
        getView().showProgress();
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(StampModel.class).getListCompany(token, new StampModel.IGetListCompanyResponse() {

            @Override
            public void onSuccess(List<ListCompanyResponse.ListCompanyData.CompanyData> companyDataList) {

                if (companyDataList != null) {
                    if (companyDataList.size() > 1) {
                        getView().displayServiceCompanyScreen(companyDataList);
                        getView().hideProgress();
                        return;
                    } else if (companyDataList.size() == 1) {
                        ListCompanyResponse.ListCompanyData.CompanyData companyData = companyDataList.get(0);
                        if (companyData.getCardType() == 1) {
                            getView().displayListCardScreen(companyData);
                            getView().hideProgress();
                            return;
                        } else {
                            getView().displayNotificationForServiceScreen(companyData);
                            getView().hideProgress();
                            return;
                        }
                    }
                }
                getView().displayServiceCompanyScreen(companyDataList);
                getView().hideProgress();
            }

            @Override
            public void onFailure(ErrorMessage errorMessage) {
                getView().displayServiceCompanyScreen(null);
                getView().hideProgress();
            }
        });
    }
}
