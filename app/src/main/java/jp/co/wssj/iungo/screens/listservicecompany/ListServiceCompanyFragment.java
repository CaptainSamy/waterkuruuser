package jp.co.wssj.iungo.screens.listservicecompany;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.stamp.ListCompanyResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.PagedFragment;
import jp.co.wssj.iungo.screens.listcard.ListCardFragmentDetail;
import jp.co.wssj.iungo.screens.listservicecompany.adapter.ServicesCompanyAdapter;
import jp.co.wssj.iungo.screens.note.UserMemoFragment;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by HieuPT on 17/5/2017.
 */

public class ListServiceCompanyFragment extends PagedFragment<IListServiceCompanyView, ListServiceCompanyPresenter>
        implements IListServiceCompanyView, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private static final String TAG = "ListServiceCompanyFragment";

    private SwipeRefreshLayout mRefreshLayout;

    private ListView mCardListView;

    private ServicesCompanyAdapter mAdapter;

    private List<ListCompanyResponse.ListCompanyData.CompanyData> mCardList;

    @Override
    public String getPageTitle(Context context) {
        return getString(context, R.string.title_screen_stamp);
    }

    @Override
    protected boolean isRetainState() {
        return true;
    }

    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_stamp;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_list_service;
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
        if (mCardList == null) {
            mCardList = new ArrayList<>();
            mAdapter = new ServicesCompanyAdapter(getActivityContext(), mCardList);
            setRefreshing(true);
            getPresenter().getCompanyList();
        } else if (mCardList.isEmpty()) {
            showTextNoItem(true, getString(R.string.no_item_service));
        }
        mCardListView.setAdapter(mAdapter);
    }

    @Override
    protected IListServiceCompanyView onCreateView() {
        return this;
    }

    /*
    *card_type:
    * 1 - stamp
    * 2 - point
    * 3 - None
    * */
    @Override
    public void displayListCardScreen(int serviceCompanyId, int serviceId, String serviceName, String cardName, int cardType) {
        int fragmentId;
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_SERVICE_COMPANY_ID, serviceCompanyId);
        if (cardType == 1) {
            bundle.putInt(ListCardFragmentDetail.KEY_SERVICE_ID, serviceId);
            bundle.putString(ListCardFragmentDetail.KEY_CARD_NAME, cardName);
            bundle.putString(UserMemoFragment.KEY_SERVICE_NAME, serviceName);
            fragmentId = IMainView.FRAGMENT_LIST_CARD;
        } else {
            fragmentId = IMainView.FRAGMENT_NOTIFICATION_FOR_SERVICE_COMPANY;
        }
        getActivityCallback().displayScreen(fragmentId, true, true, bundle);
    }

    @Override
    public void showListCompany(List<ListCompanyResponse.ListCompanyData.CompanyData> cardList) {
        if (cardList != null && cardList.size() > 0) {
            showTextNoItem(false, null);
            setRefreshing(false);
            if (mCardList != null && mCardList.size() > 0) {
                mCardList.clear();
            }
            mCardList.addAll(cardList);
            mAdapter.notifyDataSetChanged();
        } else {
            showTextNoItem(true, getString(R.string.no_item_service));
        }
    }

    @Override
    public void showNoCompany() {
        setRefreshing(false);
    }

    public void setRefreshing(boolean refreshing) {

        mRefreshLayout.setRefreshing(refreshing);
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
}
