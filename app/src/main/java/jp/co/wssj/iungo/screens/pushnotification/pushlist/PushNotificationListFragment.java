package jp.co.wssj.iungo.screens.pushnotification.pushlist;

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
import jp.co.wssj.iungo.model.database.DBManager;
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

    private TextView mTextSearch;

    private boolean isExpandedSearchView;

    DBManager mDatabase = DBManager.getInstance();

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected PushNotificationListPresenter onCreatePresenter(IPushNotificationListView view) {
        return new PushNotificationListPresenter(view);
    }

    @Override
    protected boolean isRetainState() {
        return false;
    }

    @Override
    protected IPushNotificationListView onCreateView() {
        return this;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }

    @Override
    protected void initViews(View rootView) {
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        mListView = (ListView) rootView.findViewById(R.id.list_push_notification);
        mTextNoItem = (TextView) rootView.findViewById(R.id.textNoItem);
        mInputSearch = (SearchView) rootView.findViewById(R.id.inputSearch);
        mInputSearch.setMaxWidth(Integer.MAX_VALUE);
        mTextSearch = (TextView) rootView.findViewById(R.id.tvSearch);
        mInputSearch.clearFocus();
        mListNotification = new ArrayList<>();
        mAdapter = new PushNotificationAdapter(getActivityContext(), mListNotification);

    }

    @Override
    protected void initData() {
        mInputSearch.setEnabled(false);
        mRefreshLayout.setEnabled(false);
        mListNotification.addAll(mDatabase.getListPush());
        if (mListNotification.size() == 0) {
            mRefreshLayout.setRefreshing(true);
            getPresenter().getListPushNotification(Constants.INIT_PAGE, Constants.LIMIT);
        } else {
            int page = mListNotification.size() / Constants.LIMIT;
            mPage = page == 0 ? 1 : page;
            mAdapter.setListPushTemp(mListNotification);
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
        mTextSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mInputSearch.setIconified(false);
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
                Logger.d(TAG, "setOnCloseListener");
                mTextSearch.setVisibility(View.VISIBLE);
                isExpandedSearchView = false;
                return false;
            }
        });
        mInputSearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Logger.d(TAG, "onFocusChange " + hasFocus);
                if (hasFocus) {
                    isExpandedSearchView = hasFocus;
                }
                statusSearchView(hasFocus);
            }
        });


        mAdapter.setListenerEndOfListView(new PushNotificationAdapter.IEndOfListView() {

            @Override
            public void onEndOfListView() {

                Logger.d(TAG, "page : " + mPage + " totalPage " + mTotalPage);
                if (mTotalPage != 0) {
                    if (mPage < mTotalPage) {
                        mRefreshLayout.setRefreshing(true);
                        getPresenter().getListPushNotification(mPage + 1, Constants.LIMIT);
                    }
                } else {
                    getPresenter().getListPushNotification(mPage + 1, Constants.LIMIT);
                }
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
//        getPresenter().getListPushNotification(Constants.INIT_PAGE, Constants.LIMIT);
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
        mPage = page;
        mTotalPage = totalPage;
        if (list != null && list.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            mTextNoItem.setVisibility(View.GONE);
//            if (page == Constants.INIT_PAGE) {
//                mListNotification.clear();
//            }

            mListNotification.addAll(list);
            mAdapter.setListPushTemp(mListNotification);
            mAdapter.notifyDataSetChanged();
            mDatabase.insertPushNotification(list);


            if (list.size() > 0) {
                List<Long> listPushId = new ArrayList<>();
                for (NotificationMessage notificationMessage : list) {
                    if (notificationMessage.getStatusRead() != Constants.STATUS_VIEW && notificationMessage.getStatusRead() != Constants.STATUS_READ) {
                        listPushId.add(notificationMessage.getPushId());
                    }
                }
                if (listPushId != null && listPushId.size() > 0) {
                    getPresenter().setListPushUnRead(listPushId, Constants.STATUS_VIEW);
                    //TODO update status read in database
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
