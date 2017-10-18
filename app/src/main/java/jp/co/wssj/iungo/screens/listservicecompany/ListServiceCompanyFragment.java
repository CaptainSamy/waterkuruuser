package jp.co.wssj.iungo.screens.listservicecompany;

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
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.listcard.ListCardFragmentDetail;
import jp.co.wssj.iungo.screens.listservicecompany.adapter.ServicesCompanyAdapter;
import jp.co.wssj.iungo.screens.note.UserMemoFragment;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by HieuPT on 17/5/2017.
 */

public class ListServiceCompanyFragment extends BaseFragment<IListServiceCompanyView, ListServiceCompanyPresenter>
        implements IListServiceCompanyView, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    public static final String KEY_COMPANY_LIST = "KEY_COMPANY_LIST";

    private static final String TAG = "ListServiceCompanyFragment";

    private SwipeRefreshLayout mRefreshLayout;

    private ListView mCardListView;

    private ServicesCompanyAdapter mAdapter;

    private List<ListCompanyResponse.ListCompanyData.CompanyData> mCardList;

    public static ListServiceCompanyFragment newInstance(Bundle bundle) {
        ListServiceCompanyFragment fragment = new ListServiceCompanyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_bottom_stamp);
    }

    @Override
    protected boolean isRetainState() {
        return true;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_LIST_SERVICE_COMPANY;
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCardList = bundle.getParcelableArrayList(KEY_COMPANY_LIST);
        }
        if (mCardList == null) {
            mCardList = new ArrayList<>();
            mAdapter = new ServicesCompanyAdapter(getActivityContext(), mCardList);
            setRefreshing(true);
            getPresenter().getCompanyList();
        } else if (mCardList.isEmpty()) {
            showTextNoItem(true, getString(R.string.no_item_service));
        } else {
            mAdapter = new ServicesCompanyAdapter(getActivityContext(), mCardList);
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
            fragmentId = IMainView.FRAGMENT_PUSH_NOTIFICATION_PAGER;
        }
        getActivityCallback().displayScreen(fragmentId, true, true, bundle);
    }

    @Override
    public void showListCompany(List<ListCompanyResponse.ListCompanyData.CompanyData> cardList) {
        Logger.d(TAG, "showListCompany");
        if (cardList != null && cardList.size() > 0) {
            showTextNoItem(false, null);
            setRefreshing(false);
            if (mCardList != null) {
                mCardList.clear();
            }
            mCardList.addAll(cardList);
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
