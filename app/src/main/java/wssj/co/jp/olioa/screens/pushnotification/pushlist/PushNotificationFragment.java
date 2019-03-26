package wssj.co.jp.olioa.screens.pushnotification.pushlist;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.entities.PushNotification;
import wssj.co.jp.olioa.model.pushnotification.PushNotificationResponse;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.pushnotification.adapter.PushNotificationAdapter;
import wssj.co.jp.olioa.widget.ILoadMoreListView;
import wssj.co.jp.olioa.widget.LoadMoreListView;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationFragment extends BaseFragment<IPushNotificationListView, PushNotificationListPresenter>
        implements IPushNotificationListView, SwipeRefreshLayout.OnRefreshListener, ILoadMoreListView {

    private static final String TAG = "PushNotificationFragment";

    public static final String ARG_STORE_ID = "storeID";

    private SwipeRefreshLayout mRefreshLayout;

    private PushNotificationAdapter mAdapter;

    private LoadMoreListView mListView;

    private TextView mTextNoItem;

    private List<PushNotification> mListNotification;

    private int storeId;

    public static PushNotificationFragment newInstance(Bundle args) {

        PushNotificationFragment fragment = new PushNotificationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_PUSH_NOTIFICATION;
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
    protected PushNotificationListPresenter onCreatePresenter(IPushNotificationListView view) {
        return new PushNotificationListPresenter(view);
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return false;
    }

    @Override
    protected IPushNotificationListView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mListView = rootView.findViewById(R.id.list_push_notification);
        mTextNoItem = rootView.findViewById(R.id.textNoItem);
    }

    @Override
    protected void initData() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            storeId = bundle.getInt(ARG_STORE_ID);
            mListNotification = new ArrayList<>();
            mAdapter = new PushNotificationAdapter(getActivityContext(), mListNotification);
            getPresenter().getListPushNotification(storeId, 0);
            mAdapter.setListPushTemp(mListNotification);
            mListView.setAdapter(mAdapter);
        } else {
            backToPreviousScreen();
        }
    }

    @Override
    protected void initAction() {
        mRefreshLayout.setOnRefreshListener(this);
        mListView.setOnLoadMoreListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                NotificationMessage message = (NotificationMessage) parent.getAdapter().getItem(position);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(PushNotificationDetailFragment.NOTIFICATION_ARG, message);
//                getActivityCallback().displayScreen(IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL, true, true, bundle);
            }
        });
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        getPresenter().getListPushNotification(storeId, 0);
    }

    @Override
    public void showListPushNotification(PushNotificationResponse response) {
        if (response != null) {
            if (response.getTotalPage() != 0) {
                mListNotification.clear();
                mListView.setCurrentPage(0);
                mListView.setTotalPage(response.getTotalPage());
            }
            List<PushNotification> list = response.getListPushNotification();
            if (list != null && list.size() > 0) {
                mListNotification.addAll(list);
                mAdapter.notifyDataSetChanged();
                if (mAdapter.getCount() == 0) {
                    showTextNoItem(true, getString(R.string.no_timeline));
                } else {
                    showTextNoItem(false, null);
                }
            }
        }
        mTextNoItem.setVisibility(mListNotification.size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override

    public void displayErrorMessage(ErrorMessage errorMessage) {
        if (errorMessage != null && !TextUtils.isEmpty(errorMessage.getMessage()))
            Toast.makeText(getActivityContext(), errorMessage.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMore(int page) {
        getPresenter().getListPushNotification(storeId, page);
    }
}
