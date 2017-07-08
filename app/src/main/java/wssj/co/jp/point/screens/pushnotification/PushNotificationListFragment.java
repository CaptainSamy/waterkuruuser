package wssj.co.jp.point.screens.pushnotification;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.model.firebase.NotificationMessage;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;
import wssj.co.jp.point.screens.pushnotification.detail.PushNotificationDetailFragment;
import wssj.co.jp.point.utils.Constants;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationListFragment extends BaseFragment<IPushNotificationListView, PushNotificationListPresenter> implements IPushNotificationListView, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mRefreshLayout;

    private PushNotificationAdapter mAdapter;

    private ListView mListView;

    private List<NotificationMessage> mListNotification;

    @Override
    protected String getLogTag() {
        return "PushNotificationListFragment";
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
    }

    @Override
    protected void initData() {
        mListNotification = new ArrayList<>();
        mAdapter = new PushNotificationAdapter(getContext(), R.layout.item_push_notification, mListNotification);
        mListView.setAdapter(mAdapter);
        mRefreshLayout.setRefreshing(true);
        getPresenter().getListPushNotification(Constants.INIT_PAGE, Constants.LIMIT);

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

    @Override
    public void showListPushNotification(List<NotificationMessage> list, final int page, final int totalPage) {
        hideSwipeRefreshLayout();
        if (list != null) {
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


    }

    @Override
    public void displayErrorMessage(ErrorMessage errorMessage) {
        hideSwipeRefreshLayout();
        if (errorMessage != null && !TextUtils.isEmpty(errorMessage.getMessage()))
            Toast.makeText(getActivityContext(), errorMessage.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
