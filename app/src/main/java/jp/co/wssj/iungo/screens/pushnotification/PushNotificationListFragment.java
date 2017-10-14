package jp.co.wssj.iungo.screens.pushnotification;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.pushnotification.detail.PushNotificationDetailFragment;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationListFragment extends BaseFragment<IPushNotificationListView, PushNotificationListPresenter> implements IPushNotificationListView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "PushNotificationListFragment";

    private SwipeRefreshLayout mRefreshLayout;

    private TextView mTextNoItem;

    private PushNotificationAdapter mAdapter;

    private ListView mListView;

    private List<NotificationMessage> mListNotification;

    private SearchView mInputSearch;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_PUSH_NOTIFICATION_LIST;
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
    public int getNavigationMenuId() {
        return R.id.menu_push_notification;
    }

    @Override
    public boolean isDisplayIconNotification() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected PushNotificationListPresenter onCreatePresenter(IPushNotificationListView view) {
        return new PushNotificationListPresenter(view);
    }

    @Override
    protected IPushNotificationListView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        mListView = (ListView) rootView.findViewById(R.id.list_push_notification);
        mTextNoItem = (TextView) rootView.findViewById(R.id.textNoItem);
        mInputSearch = (SearchView) rootView.findViewById(R.id.inputSearch);
        mInputSearch.setMaxWidth(Integer.MAX_VALUE);

    }

    @Override
    protected void initData() {
        mInputSearch.setEnabled(false);
        if (mListNotification == null) {
            mListNotification = new ArrayList<>();
            mAdapter = new PushNotificationAdapter(getActivityContext(), mListNotification);
            mRefreshLayout.setRefreshing(true);
            getPresenter().getListPushNotification(Constants.INIT_PAGE, Constants.LIMIT);
        }

        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initAction() {
        mRefreshLayout.setOnRefreshListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotificationMessage message = (NotificationMessage) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(PushNotificationDetailFragment.NOTIFICATION_ARG, message);
                getActivityCallback().displayScreen(IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL, true, true, bundle);
            }
        });
        mInputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Logger.d(TAG, "onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Logger.d(TAG, "onQueryTextChange");
                mAdapter.setIsAllowOnLoadMore(false);
                if (mAdapter != null) {
                    mAdapter.filter(newText);
                }
                return false;
            }

        });
        mInputSearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Logger.d(TAG, "onFocusChange " + hasFocus);
                if (hasFocus) {
                    if (mAdapter != null) {
                        mAdapter.setIsAllowOnLoadMore(false);
                    }
                } else {
                    if (mAdapter != null) {
                        mAdapter.setIsAllowOnLoadMore(true);
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getPresenter().getListPushNotification(Constants.INIT_PAGE, Constants.LIMIT);
    }

    public void hideSwipeRefreshLayout() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    private int mPage, mTotalPage;

    @Override
    public void showListPushNotification(List<NotificationMessage> list, final int page, final int totalPage) {
        mInputSearch.setEnabled(true);
        hideSwipeRefreshLayout();
        if (list != null && list.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            mTextNoItem.setVisibility(View.GONE);
            mPage = page;
            mTotalPage = totalPage;
            if (list != null) {
                if (page == Constants.INIT_PAGE) {
                    mListNotification.clear();
                }
                mListNotification.addAll(list);
                mAdapter.setListPushTemp(mListNotification);
                mAdapter.notifyDataSetChanged();
            }
            mAdapter.setListenerEndOfListView(new PushNotificationAdapter.IEndOfListView() {

                @Override
                public void onEndOfListView() {
                    if (page < totalPage) {
                        mRefreshLayout.setRefreshing(true);
                        getPresenter().getListPushNotification(page + 1, Constants.LIMIT);
                    }
                }
            });
            if (list.size() > 0) {
                List<Long> listPushId = new ArrayList<>();
                for (NotificationMessage notificationMessage : list) {
                    if (notificationMessage.getStatusRead() != Constants.STATUS_VIEW && notificationMessage.getStatusRead() != Constants.STATUS_READ) {
                        listPushId.add(notificationMessage.getPushId());
                    }
                }
                if (listPushId != null && listPushId.size() > 0) {
                    getPresenter().setListPushUnRead(listPushId, Constants.STATUS_VIEW);
                }
            }
        } else {
            mListView.setVisibility(View.GONE);
            mTextNoItem.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void displayErrorMessage(ErrorMessage errorMessage) {
        mInputSearch.setEnabled(true);
        hideSwipeRefreshLayout();
        if (errorMessage != null && !TextUtils.isEmpty(errorMessage.getMessage()))
            Toast.makeText(getActivityContext(), errorMessage.getMessage(), Toast.LENGTH_SHORT).show();
    }


}
