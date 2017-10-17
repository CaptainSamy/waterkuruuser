package jp.co.wssj.iungo.screens.pushnotificationforstore;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.database.DBManager;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.pushnotification.PushNotificationPageAdapter;
import jp.co.wssj.iungo.screens.pushnotification.detail.PushNotificationDetailFragment;
import jp.co.wssj.iungo.screens.pushnotification.pushlist.PushNotificationAdapter;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationForServiceCompanyFragment extends BaseFragment<IPushNotificationForServiceCompany, PushNotificationForServiceCompanyPresenter> implements IPushNotificationForServiceCompany, SwipeRefreshLayout.OnRefreshListener {

    private String TAG = "PushNotificationForServiceCompanyFragment";

    private SwipeRefreshLayout mRefreshLayout;

    private PushNotificationAdapter mAdapter;

    private ListView mListView;

    private List<NotificationMessage> mListNotification;

    private int mServiceCompanyId;

    private SearchView mInputSearch;

    private TextView mTextSearch;

    private boolean isExpandedSearchView;

    DBManager mDatabase = DBManager.getInstance();

    private int mPage, mTotalPage;

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
    protected PushNotificationForServiceCompanyPresenter onCreatePresenter(IPushNotificationForServiceCompany view) {
        return new PushNotificationForServiceCompanyPresenter(view);
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }

    @Override
    protected IPushNotificationForServiceCompany onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        mRefreshLayout.setEnabled(false);
        mListView = (ListView) rootView.findViewById(R.id.list_push_notification);
        mInputSearch = (SearchView) rootView.findViewById(R.id.inputSearch);
        mInputSearch.setMaxWidth(Integer.MAX_VALUE);
        mTextSearch = (TextView) rootView.findViewById(R.id.tvSearch);
        mInputSearch.clearFocus();
        mListNotification = new ArrayList<>();
        mAdapter = new PushNotificationAdapter(getActivityContext(), mListNotification);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mServiceCompanyId = bundle.getInt(Constants.KEY_SERVICE_COMPANY_ID);
            int type = bundle.getInt(PushNotificationPageAdapter.ARG_TYPE_PUSH, 0);
            mListNotification.addAll(mDatabase.getListPush(type, mServiceCompanyId));
            if (mListNotification.size() == 0) {
                mRefreshLayout.setRefreshing(true);
                getPresenter().getListPushNotification(mServiceCompanyId, Constants.INIT_PAGE, Constants.LIMIT);
            } else {
                int page = mListNotification.size() / Constants.LIMIT;
                mPage = page == 0 ? 1 : page;
                mAdapter.setListPushTemp(mListNotification);
            }
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    protected void initAction() {
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter.setListenerEndOfListView(new PushNotificationAdapter.IEndOfListView() {

            @Override
            public void onEndOfListView() {
                if (mTotalPage != 0) {
                    if (mPage < mTotalPage) {
                        mRefreshLayout.setRefreshing(true);
                        getPresenter().getListPushNotification(mServiceCompanyId, mPage + 1, Constants.LIMIT);
                    }
                } else {
                    mRefreshLayout.setRefreshing(true);
                    getPresenter().getListPushNotification(mServiceCompanyId, mPage + 1, Constants.LIMIT);
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotificationMessage message = (NotificationMessage) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(PushNotificationDetailFragment.NOTIFICATION_ARG, message);
                getActivityCallback().displayScreen(IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL, true, true, bundle);
            }
        });

        mTextSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mInputSearch.setIconified(false);
            }
        });
        mInputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                statusSearchView(true);
                if (mAdapter != null) {
                    mAdapter.filter(newText);
                }
                return false;
            }

        });
        mInputSearch.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                mTextSearch.setVisibility(View.VISIBLE);
                isExpandedSearchView = false;
                return false;
            }
        });
        mInputSearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    isExpandedSearchView = hasFocus;
                }
                statusSearchView(hasFocus);
            }
        });
    }

    private void statusSearchView(boolean hasFocus) {
        mTextSearch.setVisibility(isExpandedSearchView ? View.GONE : View.VISIBLE);
        mRefreshLayout.setEnabled(!hasFocus);
        if (mAdapter != null) {
            mAdapter.setIsAllowOnLoadMore(!hasFocus);
        }
    }

    @Override
    public void onRefresh() {
//        getPresenter().getListPushNotification(mServiceCompanyId, Constants.INIT_PAGE, Constants.LIMIT);
    }

    public void hideSwipeRefreshLayout() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showListPushNotification(List<NotificationMessage> list, final int page, final int totalPage) {
        hideSwipeRefreshLayout();
        mPage = page;
        mTotalPage = totalPage;
        if (list != null) {
            mListNotification.addAll(list);
            mAdapter.setListPushTemp(mListNotification);
            mAdapter.notifyDataSetChanged();
            mDatabase.insertPushStoreAnnounce(list, mServiceCompanyId);
        }


    }

    @Override
    public void displayErrorMessage(ErrorMessage errorMessage) {
        hideSwipeRefreshLayout();
        if (errorMessage != null && !TextUtils.isEmpty(errorMessage.getMessage()))
            Toast.makeText(getActivityContext(), errorMessage.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
