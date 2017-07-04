package wssj.co.jp.point.screens.listservicecompany;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.stamp.ListCompanyResponse;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;
import wssj.co.jp.point.screens.listcard.ListCardFragment;
import wssj.co.jp.point.screens.note.UserMemoFragment;
import wssj.co.jp.point.screens.listservicecompany.adapter.ServicesCompanyAdapter;
import wssj.co.jp.point.utils.Logger;

/**
 * Created by HieuPT on 17/5/2017.
 */

public class ListServiceCompanyFragment extends BaseFragment<IListServiceCompanyView, ListServiceCompanyPresenter>
        implements IListServiceCompanyView, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private static final String TAG = "ListServiceCompanyFragment";

    private SwipeRefreshLayout mRefreshLayout;

    private ListView mCardListView;

    private ServicesCompanyAdapter mAdapter;

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_STAMP;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }

    @Override
    public boolean isDisplayNavigationButton() {
        return false;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_stamp);
    }

    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_stamp;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.stamp_fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected ListServiceCompanyPresenter onCreatePresenter(IListServiceCompanyView view) {
        return new ListServiceCompanyPresenter(view);
    }

    @Override
    protected void initViews(View rootView) {
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        mCardListView = (ListView) rootView.findViewById(R.id.card_list_view);
    }

    @Override
    protected void initAction() {
        mRefreshLayout.setOnRefreshListener(this);
        mCardListView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        getPresenter().getCompanyList();
    }

    @Override
    protected IListServiceCompanyView onCreateView() {
        return this;
    }

    @Override
    public void displayListCardScreen(int serviceCompanyId, int serviceId, String serviceName, String cardName) {
        Bundle bundle = new Bundle();
        bundle.putInt(ListCardFragment.KEY_SERVICE_COMPANY_ID, serviceCompanyId);
        bundle.putInt(ListCardFragment.KEY_SERVICE_ID, serviceId);
        bundle.putString(ListCardFragment.KEY_CARD_NAME, cardName);
        bundle.putString(UserMemoFragment.KEY_SERVICE_NAME, serviceName);
        getActivityCallback().displayScreen(IMainView.FRAGMENT_LIST_CARD, true, true, bundle);
    }

    @Override
    public void showListCompany(List<ListCompanyResponse.ListCompanyData.CompanyData> cardList) {
        hideSwipeRefreshLayout();
        if (mAdapter == null) {
            mAdapter = new ServicesCompanyAdapter(getActivityContext(), cardList);
            mCardListView.setAdapter(mAdapter);
        } else {
            if (mCardListView.getAdapter() == null) {
                mCardListView.setAdapter(mAdapter);
            }
            mAdapter.refreshData(cardList);
        }
    }

    @Override
    public void showNoCompany() {
        hideSwipeRefreshLayout();
    }

    public void hideSwipeRefreshLayout() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        getPresenter().getCompanyList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Logger.i(TAG, "#onItemClick");
        getPresenter().onItemClicked((ListCompanyResponse.ListCompanyData.CompanyData) parent.getItemAtPosition(position));
    }

    @Override
    public int getMenuBottomID() {
        return MENU_MY_STAMP;
    }
}
