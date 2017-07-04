package wssj.co.jp.point.screens.listservicecompany;

import java.util.List;

import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.model.preference.SharedPreferencesModel;
import wssj.co.jp.point.model.stamp.ListCompanyResponse;
import wssj.co.jp.point.model.stamp.StampModel;
import wssj.co.jp.point.screens.base.FragmentPresenter;

/**
 * Created by HieuPT on 17/5/2017.
 */

class ListServiceCompanyPresenter extends FragmentPresenter<IListServiceCompanyView> {

    ListServiceCompanyPresenter(IListServiceCompanyView view) {
        super(view);
        registerModel(new StampModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    void getCompanyList() {
        getView().showProgress();
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(StampModel.class).getListCompany(token, new StampModel.IGetListCompanyResponse() {

            @Override
            public void onSuccess(List<ListCompanyResponse.ListCompanyData.CompanyData> companyDataList) {
                getView().hideProgress();
                if (companyDataList != null) {
                    getView().showListCompany(companyDataList);
                } else {
                    getView().showNoCompany();
                }
            }

            @Override
            public void onFailure(ErrorMessage errorMessage) {
                getView().hideProgress();
                getView().showNoCompany();
            }
        });
    }

    void onItemClicked(ListCompanyResponse.ListCompanyData.CompanyData companyData) {
        if (companyData != null) {
            getView().displayListCardScreen(companyData.getServiceCompanyId(), companyData.getServiceId(), companyData.getServiceName(), companyData.getCardName());
        }
    }
}
