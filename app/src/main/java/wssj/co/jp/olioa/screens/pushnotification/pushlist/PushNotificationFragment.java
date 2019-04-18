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
import wssj.co.jp.olioa.screens.pushnotification.detail.PushNotificationDetailFragment;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.widget.ILoadMoreListener;
import wssj.co.jp.olioa.widget.LoadMoreListView;

/**
 * Created by tuanle on 6/7/17.
 */

public class PushNotificationFragment extends BaseFragment<IPushNotificationListView, PushNotificationListPresenter>
        implements IPushNotificationListView, SwipeRefreshLayout.OnRefreshListener, ILoadMoreListener {

    private static final String TAG = "PushNotificationFragment";

    private SwipeRefreshLayout mRefreshLayout;

    private PushNotificationAdapter mAdapter;

    private LoadMoreListView mListView;

    private TextView mTextNoItem;

    private List<PushNotification> mListNotification;


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
    public boolean isDisplayBackButton() {
        return false;
    }


    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_push;
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
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mListView = rootView.findViewById(R.id.list_push_notification);
        mTextNoItem = rootView.findViewById(R.id.textNoItem);
    }

    @Override
    protected void initData() {
        mListNotification = new ArrayList<>();
        mAdapter = new PushNotificationAdapter(getActivityContext(), mListNotification);
        getPresenter().getListPushNotification(0);
        mAdapter.setListPushTemp(mListNotification);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initAction() {
        mRefreshLayout.setOnRefreshListener(this);
        mListView.setOnLoadMoreListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PushNotification message = (PushNotification) parent.getAdapter().getItem(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(PushNotificationDetailFragment.NOTIFICATION_ARG, message);
                getActivityCallback().displayScreen(IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL, true, true, bundle);
            }
        });
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        mListView.reload();
        getPresenter().getListPushNotification(0);
    }

    @Override
    public void showListPushNotification(PushNotificationResponse response) {
        if (response != null) {
            if (mListView.getCurrentPage() == 0) {
                mListNotification.clear();
            }
            List<PushNotification> list = response.getListPushNotification();
            if (list != null && list.size() > 0) {
                if (list.size() < Constants.MAX_ITEM_PAGE) {
                    mListView.stopLoadMore();
                } else {
                    mListView.notifyLoadComplete();
                }
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
        getPresenter().getListPushNotification(page);
    }
}
