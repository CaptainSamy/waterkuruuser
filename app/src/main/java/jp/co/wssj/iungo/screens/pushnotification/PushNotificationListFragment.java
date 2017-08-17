package jp.co.wssj.iungo.screens.pushnotification;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
    public int getNavigationMenuID() {
        return R.id.menu_push_notification;
    }

    @Override
    public boolean isDisplayIconNotification() {
        return false;
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
    }

    @Override
    protected void initData() {

        if (mListNotification == null) {
            mListNotification = new ArrayList<>();
            mAdapter = new PushNotificationAdapter(getActivityContext(), R.layout.item_push_notification, mListNotification);
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


            if (list != null && list.size() > 0) {
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
        hideSwipeRefreshLayout();
        if (errorMessage != null && !TextUtils.isEmpty(errorMessage.getMessage()))
            Toast.makeText(getActivityContext(), errorMessage.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
