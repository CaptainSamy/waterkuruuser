package wssj.co.jp.olioa.screens.pushnotification.pushquestionnaire;

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

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.screens.pushnotification.detail.PushNotificationDetailFragment;
import wssj.co.jp.olioa.model.firebase.NotificationMessage;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.pushnotification.adapter.PushNotificationAdapter;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Logger;

/**
 * Created by Nguyen Huu Ta on 26/10/2017.
 */

public class PushTypeQuestionNaireFragment extends BaseFragment<IPushTypeQuestionNaireView, PushTypeQuestionNairePresenter>
        implements IPushTypeQuestionNaireView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "PushNotificationFragment";

    private SwipeRefreshLayout mRefreshLayout;

    private PushNotificationAdapter mAdapter;

    private ListView mListView;

    private List<NotificationMessage> mListNotification;

    private SearchView mInputSearch;

    private TextView mTextSearch;

    private RelativeLayout mLayoutSearch;

    private boolean isExpandedSearchView;

    private long mPushUserIdLastPage;

    private boolean mIsSearch;

    private boolean mIsPullDownRequest;

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
    protected PushTypeQuestionNairePresenter onCreatePresenter(IPushTypeQuestionNaireView view) {
        return new PushTypeQuestionNairePresenter(view);
    }

    @Override
    protected boolean isRetainState() {
        return false;
    }

    @Override
    protected IPushTypeQuestionNaireView onCreateView() {
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
        mInputSearch.setMaxWidth(Integer.MAX_VALUE);
        mInputSearch.clearFocus();
    }

    @Override
    protected void initData() {
        if (mListNotification == null) {
            mListNotification = new ArrayList<>();
            //mAdapter = new PushNotificationAdapter(getActivityContext(), mListNotification);
            mRefreshLayout.setRefreshing(true);
            getPresenter().getListPushQuestionNaire(mPushUserIdLastPage, 0, Constants.EMPTY_STRING);
        } else {
            if (mListNotification.size() == 0) {
                showTextNoItem(true, getString(R.string.text_no_item_push_all));
            }
        }
        mAdapter.setListenerEndOfListView(new PushNotificationAdapter.IEndOfListView() {

            @Override
            public void onEndOfListView() {
                mPushUserIdLastPage = mListNotification.get(mListNotification.size() - 1).getUserPushId();
                mRefreshLayout.setRefreshing(true);
                getPresenter().getListPushQuestionNaire(mPushUserIdLastPage, 0, Constants.EMPTY_STRING);
            }
        });
        //mAdapter.setListPushTemp(mListNotification);
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
                mIsSearch = true;
                Logger.d(TAG, "onQueryTextSubmit");
                mRefreshLayout.setRefreshing(true);
                getPresenter().getListPushQuestionNaire(0, 1, query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Logger.d(TAG, "onQueryTextChange");
                if (TextUtils.isEmpty(newText)) {
                    mAdapter.filter(newText);
                }
                statusSearchView(true);
                return false;
            }
        });
        mInputSearch.setOnCloseListener(new SearchView.OnCloseListener() {

            @Override
            public boolean onClose() {
                Logger.d(TAG, "onClose");
                mTextSearch.setVisibility(View.VISIBLE);
                isExpandedSearchView = false;
                mIsSearch = false;
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
        mIsPullDownRequest = true;
        getPresenter().getListPushQuestionNaire(0, 0, Constants.EMPTY_STRING);
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
            mInputSearch.setEnabled(true);
            if (list != null && list.size() > 0) {
                mListView.setVisibility(View.VISIBLE);
                showTextNoItem(false, null);
                if (mIsPullDownRequest) {
                    getItemNew(list);
                } else {
                    mListNotification.addAll(list);
                }
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

                //mAdapter.setListPushTemp(mListNotification);
                mAdapter.notifyDataSetChanged();
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
        mInputSearch.setEnabled(true);
        hideSwipeRefreshLayout();

        if (mListNotification != null && mListNotification.size() == 0) {
            showTextNoItem(true, getString(R.string.text_no_item_push_all));
        }
        if (errorMessage != null && !TextUtils.isEmpty(errorMessage.getMessage()))
            Toast.makeText(getActivityContext(), errorMessage.getMessage(), Toast.LENGTH_SHORT).show();
    }


}
