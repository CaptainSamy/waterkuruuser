package jp.co.wssj.iungo.screens.pushnotificationforstore;

import android.os.Bundle;
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
import jp.co.wssj.iungo.screens.pushnotification.pushlist.PushNotificationAdapter;
import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationForStoreAnnounce extends BaseFragment<IPushNotificationForStoreAnnounce, PushNotificationForStoreAnnouncePresenter>
        implements IPushNotificationForStoreAnnounce, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "PushNotificationForStoreAnnounce";

    private SwipeRefreshLayout mRefreshLayout;

    private PushNotificationAdapter mAdapter;

    private ListView mListView;

    private List<NotificationMessage> mListNotification;

    private int mServiceCompanyId;

    private SearchView mInputSearch;

    private TextView mTextSearch;

    private RelativeLayout mLayoutSearch;

    private boolean isExpandedSearchView;

    DBManager mDatabase = DBManager.getInstance();

    public static PushNotificationForStoreAnnounce newInstance(Bundle args) {
        PushNotificationForStoreAnnounce fragment = new PushNotificationForStoreAnnounce();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public boolean isGlobal() {
        return false;
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
    protected PushNotificationForStoreAnnouncePresenter onCreatePresenter(IPushNotificationForStoreAnnounce view) {
        return new PushNotificationForStoreAnnouncePresenter(view);
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }

    @Override
    protected IPushNotificationForStoreAnnounce onCreateView() {
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
        mLayoutSearch = (RelativeLayout) rootView.findViewById(R.id.layoutSearch);
        mInputSearch.clearFocus();

    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mServiceCompanyId = bundle.getInt(Constants.KEY_SERVICE_COMPANY_ID);
            int type = bundle.getInt(PushNotificationPageAdapter.ARG_TYPE_PUSH, 0);
            if (mListNotification == null) {
                mListNotification = new ArrayList<>();
                mListNotification.addAll(mDatabase.getListPush(type, mServiceCompanyId));
                mAdapter = new PushNotificationAdapter(getActivityContext(), mListNotification);
                long lastPushId = 0;
                mRefreshLayout.setRefreshing(true);
                if (mListNotification.size() != 0) {
                    lastPushId = mListNotification.get(0).getPushId();
                }
                getPresenter().getListPushNotification(mServiceCompanyId, lastPushId);
            } else {
                if (mListNotification.size() == 0) {
                    showTextNoItem(true, getString(R.string.text_no_item_push_all));
                } else {
                    showTextNoItem(false, null);
                }
            }
            mAdapter.setListPushTemp(mListNotification);
            mListView.setAdapter(mAdapter);
        }
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
        if (list != null && list.size() > 0) {
            mListNotification.addAll(list);
            mAdapter.setListPushTemp(mListNotification);
            mAdapter.notifyDataSetChanged();
            mDatabase.insertPushStoreAnnounce(list, mServiceCompanyId);
        }
        if (mListNotification.size() == 0) {
            showTextNoItem(true, getString(R.string.text_no_item_push_all));
        } else {
            showTextNoItem(false, null);
        }


    }

    @Override
    public void displayErrorMessage(ErrorMessage errorMessage) {
        hideSwipeRefreshLayout();
        if (errorMessage != null && !TextUtils.isEmpty(errorMessage.getMessage()))
            Toast.makeText(getActivityContext(), errorMessage.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
