package jp.co.wssj.iungo.screens.pushnotificationforstore;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.pushnotification.ListPushForServiceCompanyResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.listcard.ListCardFragment;
import jp.co.wssj.iungo.screens.pushnotificationforstore.detail.PushNotificationDetailServiceCompanyFragment;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationForServiceCompanyFragment extends BaseFragment<IPushNotificationForServiceCompany, PushNotificationForServiceCompanyPresenter> implements IPushNotificationForServiceCompany, SwipeRefreshLayout.OnRefreshListener {

    private String TAG = "PushNotificationForServiceCompanyFragment";

    private SwipeRefreshLayout mRefreshLayout;

    private PushNotificationForServiceCompanyAdapter mAdapter;

    private ListView mListView;

    private List<ListPushForServiceCompanyResponse.ListPushForServiceCompany.PushNotification> mListNotification;

    private int mServiceCompanyId;

    public static PushNotificationForServiceCompanyFragment newInstance(Bundle args) {
        PushNotificationForServiceCompanyFragment fragment = new PushNotificationForServiceCompanyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_NOTIFICATION_FOR_SERVICE_COMPANY;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_push_notification_list;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_push_notification_list);
    }

    @Override
    public boolean isDisplayIconNotification() {
        return false;
    }

    @Override
    protected PushNotificationForServiceCompanyPresenter onCreatePresenter(IPushNotificationForServiceCompany view) {
        return new PushNotificationForServiceCompanyPresenter(view);
    }

    @Override
    protected IPushNotificationForServiceCompany onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        mListView = (ListView) rootView.findViewById(R.id.list_push_notification);
    }

    @Override
    protected void initData() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            mServiceCompanyId = bundle.getInt(ListCardFragment.KEY_SERVICE_COMPANY_ID);
            mListNotification = new ArrayList<>();
            mAdapter = new PushNotificationForServiceCompanyAdapter(getActivityContext(), R.layout.item_push_notification, mListNotification);
            mListView.setAdapter(mAdapter);
            mRefreshLayout.setRefreshing(true);
            getPresenter().getListPushNotification(mServiceCompanyId, Constants.INIT_PAGE, Constants.LIMIT);
        }

    }

    @Override
    protected void initAction() {
        mRefreshLayout.setOnRefreshListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListPushForServiceCompanyResponse.ListPushForServiceCompany.PushNotification message = (ListPushForServiceCompanyResponse.ListPushForServiceCompany.PushNotification) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(PushNotificationDetailServiceCompanyFragment.NOTIFICATION_ARG, message);
                getActivityCallback().displayScreen(IMainView.FRAGMENT_NOTIFICATION_DETAIL_FOR_SERVICE_COMPANY, true, true, bundle);
            }
        });
    }

    @Override
    public void onRefresh() {
        getPresenter().getListPushNotification(mServiceCompanyId, Constants.INIT_PAGE, Constants.LIMIT);
    }

    public void hideSwipeRefreshLayout() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showListPushNotification(List<ListPushForServiceCompanyResponse.ListPushForServiceCompany.PushNotification> list, final int page, final int totalPage) {
        hideSwipeRefreshLayout();
        if (list != null) {
            if (page == Constants.INIT_PAGE) {
                mListNotification.clear();
            }
            mListNotification.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.setListenerEndOfListView(new PushNotificationForServiceCompanyAdapter.IEndOfListView() {

            @Override
            public void onEndOfListView() {
                if (page < totalPage) {
                    mRefreshLayout.setRefreshing(true);
                    getPresenter().getListPushNotification(mServiceCompanyId, page + 1, Constants.LIMIT);
                }
            }
        });


    }

    @Override
    public void displayErrorMessage(ErrorMessage errorMessage) {
        hideSwipeRefreshLayout();
        if (errorMessage != null && !TextUtils.isEmpty(errorMessage.getMessage()))
            Toast.makeText(getActivityContext(), errorMessage.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
