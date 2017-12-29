package jp.co.wssj.iungo.screens.listservicecompanywrapper;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.stamp.ListCompanyResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.listcard.ListCardFragmentDetail;
import jp.co.wssj.iungo.screens.listservicecompany.ListServiceCompanyFragment;
import jp.co.wssj.iungo.screens.note.UserMemoFragment;
import jp.co.wssj.iungo.screens.pushnotification.pushpagecontainer.PushNotificationPageFragment;
import jp.co.wssj.iungo.screens.switcher.SwitcherFragment;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by HieuPT on 10/17/2017.
 */

public class ListServiceCompanyWrapperFragment extends SwitcherFragment<IListServiceCompanyWrapperView, ListServiceCompanyWrapperPresenter>
        implements IListServiceCompanyWrapperView {

    private static final String TAG = "ListServiceCompanyWrapperFragment";

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected ListServiceCompanyWrapperPresenter onCreatePresenter(IListServiceCompanyWrapperView view) {
        return new ListServiceCompanyWrapperPresenter(view);
    }

    @Override
    public boolean isDisplayNavigationButton() {
        return false;
    }

    @Override
    public boolean onBackPressed() {
        getActivityCallback().finishActivity();
        return true;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_LIST_SERVICE_COMPANY_WRAPPER;
    }

    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_stamp;
    }

    @Override
    protected IListServiceCompanyWrapperView onCreateView() {
        return this;
    }

    @Override
    protected boolean isRetainState() {
        return true;
    }

    @Override
    protected void initData() {
        getPresenter().getCompanyList();
    }

    @Override
    public void displayServiceCompanyScreen(List<ListCompanyResponse.ListCompanyData.CompanyData> companyDataList) {
        Bundle bundle = new Bundle(createBaseBundle());
        if (companyDataList != null) {
            ArrayList<ListCompanyResponse.ListCompanyData.CompanyData> dataList = new ArrayList<>(companyDataList);
            bundle.putParcelableArrayList(ListServiceCompanyFragment.KEY_COMPANY_LIST, dataList);
        }
        displayFragment(ListServiceCompanyFragment.newInstance(bundle));
    }

    @Override
    public void displayListCardScreen(ListCompanyResponse.ListCompanyData.CompanyData companyData) {
        Bundle bundle = new Bundle(createBaseBundle());
        bundle.putInt(Constants.KEY_SERVICE_COMPANY_ID, companyData.getServiceCompanyId());
        bundle.putInt(ListCardFragmentDetail.KEY_SERVICE_ID, companyData.getServiceId());
        bundle.putString(ListCardFragmentDetail.KEY_CARD_NAME, companyData.getCardName());
        bundle.putString(UserMemoFragment.KEY_SERVICE_NAME, companyData.getServiceName());
        displayFragment(ListCardFragmentDetail.newInstance(bundle));
    }

    @Override
    public void displayNotificationForServiceScreen(ListCompanyResponse.ListCompanyData.CompanyData companyData) {
        Bundle bundle = new Bundle(createBaseBundle());
        bundle.putInt(Constants.KEY_SERVICE_COMPANY_ID, companyData.getServiceCompanyId());
        displayFragment(PushNotificationPageFragment.newInstance(bundle));
    }
}
