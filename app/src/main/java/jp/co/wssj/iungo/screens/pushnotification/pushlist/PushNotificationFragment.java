package jp.co.wssj.iungo.screens.pushnotification.pushlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationFragment extends BaseFragment<IPushNotificationListView, PushNotificationListPresenter>
        implements IPushNotificationListView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "PushNotificationFragment";

    private SwipeRefreshLayout mRefreshLayout;

    private PushNotificationAdapter mAdapter;

    private ListView mListView;

    private List<NotificationMessage> mListNotification;

    private SearchView mInputSearch;

    private TextView mTextSearch;

    private RelativeLayout mLayoutSearch;

    private boolean isExpandedSearchView;

    private long mPushId;

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
    public boolean isGlobal() {
        return false;
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
        mInputSearch = (SearchView) rootView.findViewById(R.id.inputSearch);
        mInputSearch.setMaxWidth(Integer.MAX_VALUE);
        mTextSearch = (TextView) rootView.findViewById(R.id.tvSearch);
        mLayoutSearch = (RelativeLayout) rootView.findViewById(R.id.layoutSearch);
        mInputSearch.clearFocus();
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        int type = bundle.getInt(PushNotificationPageAdapter.ARG_TYPE_PUSH);
        mRefreshLayout.setEnabled(false);
        switch (type) {
            case PushNotificationPageAdapter.TYPE_ALL_PUSH:
                if (mListNotification == null) {
                    mListNotification = new ArrayList<>();
                    mAdapter = new PushNotificationAdapter(getActivityContext(), mListNotification);
                    mListNotification.addAll(mDatabase.getListPush(type, 0));
                    if (mListNotification.size() != 0) {
                        mPushId = mListNotification.get(0).getPushId();
                    }
                    mRefreshLayout.setRefreshing(true);
                    getPresenter().getListPushNotification(mPushId);
                }
                break;
            case PushNotificationPageAdapter.TYPE_LIKED_PUSH:
            case PushNotificationPageAdapter.TYPE_QUESTION_NAIRE_PUSH:
                if (mListNotification == null) {
                    mListNotification = new ArrayList<>();
                    mAdapter = new PushNotificationAdapter(getActivityContext(), mListNotification);
                } else {
                    mListNotification.clear();
                }
                mListNotification.addAll(mDatabase.getListPush(type, 0));
                mAdapter.setIsAllowOnLoadMore(false);
                break;
        }
        if (!mRefreshLayout.isRefreshing()) {
            if (mListNotification.size() == 0) {
                showTextNoItem(true, getString(R.string.text_no_item_push_all));
            } else {
                showTextNoItem(true, null);
            }
        }
        mAdapter.setListPushTemp(mListNotification);
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
        mLayoutSearch.setOnClickListener(new View.OnClickListener() {

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
        getPresenter().getListPushNotification(mPushId);
    }

    public void hideSwipeRefreshLayout() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showListPushNotification(List<NotificationMessage> list, final int page, final int totalPage) {
        mInputSearch.setEnabled(true);
        hideSwipeRefreshLayout();
        if (list != null && list.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            showTextNoItem(false, null);
            Bundle bundle = getArguments();
            int type = bundle.getInt(PushNotificationPageAdapter.ARG_TYPE_PUSH);
            if (type == PushNotificationPageAdapter.TYPE_ALL_PUSH) {
                mListNotification.addAll(list);
            } else if (type == PushNotificationPageAdapter.TYPE_QUESTION_NAIRE_PUSH) {
                for (NotificationMessage item : list) {
                    if (Constants.PushNotification.TYPE_QUESTION_NAIRE.equals(item.getAction())) {
                        mListNotification.add(item);
                    }
                }
            }
            mAdapter.setListPushTemp(mListNotification);
            mAdapter.notifyDataSetChanged();
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
            if (mListNotification != null && mListNotification.size() == 0) {
                showTextNoItem(true, getString(R.string.text_no_item_push_all));
            }
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
