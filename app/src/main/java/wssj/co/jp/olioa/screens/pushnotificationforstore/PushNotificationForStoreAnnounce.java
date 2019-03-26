package wssj.co.jp.olioa.screens.pushnotificationforstore;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.firebase.NotificationMessage;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.pushnotification.adapter.PushNotificationAdapter;
import wssj.co.jp.olioa.screens.pushnotification.detail.PushNotificationDetailFragment;
import wssj.co.jp.olioa.utils.Constants;

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

    private boolean isExpandedSearchView;

    private boolean mIsSearch;

    private boolean mIsPullDownRequest;

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
        mListView = (ListView) rootView.findViewById(R.id.list_push_notification);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mServiceCompanyId = bundle.getInt(Constants.KEY_SERVICE_COMPANY_ID);
            if (mListNotification == null) {
                mListNotification = new ArrayList<>();
                //mAdapter = new PushNotificationAdapter(getActivityContext(), mListNotification);
                mRefreshLayout.setRefreshing(true);
                getPresenter().getListPushNotificationForStoreAnnounce(mServiceCompanyId, 0, 0, Constants.EMPTY_STRING);
            } else {
                if (mListNotification.size() == 0) {
                    showTextNoItem(true, getString(R.string.text_no_item_push_all));
                } else {
                    showTextNoItem(false, null);
                }
            }
//            mAdapter.setListenerEndOfListView(new PushNotificationAdapter.IEndOfListView() {
//
//                @Override
//                public void onEndOfListView() {
//                    mRefreshLayout.setRefreshing(true);
//                    long pushUserIdLastPage = mListNotification.get(mAdapter.getCount() - 1).getUserPushId();
//                    getPresenter().getListPushNotificationForStoreAnnounce(mServiceCompanyId, pushUserIdLastPage, 0, Constants.EMPTY_STRING);
//                }
//            });
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
    }

    @Override
    public void onRefresh() {
        mIsPullDownRequest = true;
        getPresenter().getListPushNotificationForStoreAnnounce(mServiceCompanyId, 0, 0, Constants.EMPTY_STRING);
    }

    public void hideSwipeRefreshLayout() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showListPushNotification(List<NotificationMessage> list, final int page, final int totalPage) {
        hideSwipeRefreshLayout();
        if (mIsSearch) {
            mListNotification.clear();
            if (list != null && list.size() > 0) {
                mListNotification.addAll(list);
            }
            mAdapter.notifyDataSetChanged();
        } else {
            if (list != null && list.size() > 0) {
                mListView.setVisibility(View.VISIBLE);
                showTextNoItem(false, null);
                if (mIsPullDownRequest) {
                    getItemNew(list);
                } else {
                    mListNotification.addAll(list);
                }
                // mAdapter.setListPushTemp(mListNotification);
                mAdapter.notifyDataSetChanged();
                if (list.size() > 0) {
                    List<Long> listPushId = new ArrayList<>();
                    for (NotificationMessage notificationMessage : list) {
                        if (notificationMessage.getStatusRead() == 0) {
                            listPushId.add(notificationMessage.getPushId());
                        }
                    }
                    if (listPushId.size() > 0) {
                        getPresenter().setListPushUnRead(listPushId, Constants.STATUS_VIEW);
                    }
                }
            } else {
                if (mListNotification != null && mListNotification.size() == 0) {
                    showTextNoItem(true, getString(R.string.text_no_item_push_all));
                }
                if (list != null && list.size() == 0) {
                    mAdapter.setIsEndOfPage(true);
                }
            }
        }

    }

    private void getItemNew(List<NotificationMessage> listNew) {
        List<NotificationMessage> listResult = new ArrayList<>();
        for (NotificationMessage newNotification : listNew) {
            boolean isExits = false;
            for (NotificationMessage notificationMessage : mListNotification) {
                if (notificationMessage.getPushId() == newNotification.getPushId()) {
                    isExits = true;
                }
            }
            if (!isExits) {
                listResult.add(newNotification);
            }
        }
        if (listResult.size() > 0) {
            mListNotification.addAll(0, listResult);
        }

        mIsPullDownRequest = false;
    }

    @Override
    public void displayErrorMessage(ErrorMessage errorMessage) {
        hideSwipeRefreshLayout();
        if (errorMessage != null && !TextUtils.isEmpty(errorMessage.getMessage()))
            Toast.makeText(getActivityContext(), errorMessage.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
