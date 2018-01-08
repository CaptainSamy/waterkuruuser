package jp.co.wssj.iungo.screens.pushnotification.pushlike;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.database.DBManager;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.pushnotification.adapter.PushNotificationAdapter;
import jp.co.wssj.iungo.screens.pushnotification.detail.PushNotificationDetailFragment;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by Nguyen Huu Ta on 26/10/2017.
 */

public class PushLikeFragment extends BaseFragment<IPushLikeView, PushLikePresenter>
        implements IPushLikeView {

    private static final String TAG = "PushLikeFragment";

    private SwipeRefreshLayout mResfeshLayout;

    private PushNotificationAdapter mAdapter;

    private ListView mListView;

    private List<NotificationMessage> mListNotification;

    private SearchView mInputSearch;

    private TextView mTextSearch;

    private RelativeLayout mLayoutSearch;

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
    protected PushLikePresenter onCreatePresenter(IPushLikeView view) {
        return new PushLikePresenter(view);
    }

    @Override
    protected boolean isRetainState() {
        return false;
    }

    @Override
    protected IPushLikeView onCreateView() {
        return this;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }

    @Override
    protected void initViews(View rootView) {
        mResfeshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        mResfeshLayout.setEnabled(false);
        mListView = (ListView) rootView.findViewById(R.id.list_push_notification);
        mInputSearch = (SearchView) rootView.findViewById(R.id.inputSearch);
        mInputSearch.setMaxWidth(Integer.MAX_VALUE);
        mTextSearch = (TextView) rootView.findViewById(R.id.tvSearch);
        mLayoutSearch = (RelativeLayout) rootView.findViewById(R.id.layoutSearch);
        mInputSearch.clearFocus();
    }

    @Override
    protected void initData() {
        if (mListNotification == null) {
            mListNotification = new ArrayList<>();
            mAdapter = new PushNotificationAdapter(getActivityContext(), mListNotification);
        } else {
            mListNotification.clear();
        }
        mListNotification.addAll(mDatabase.getListPush(Constants.TypePush.TYPE_LIKED_PUSH, 0));
        mAdapter.setIsAllowOnLoadMore(false);
        mAdapter.setListPushTemp(mListNotification);
        mListView.setAdapter(mAdapter);
        if (mListNotification.size() == 0) {
            showTextNoItem(true, getString(R.string.text_no_item_push_all));
        } else {
            showTextNoItem(false, null);
        }
    }

    @Override
    protected void initAction() {
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
                Logger.d(TAG, "onClose");
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
    }

    private void statusSearchView(boolean hasFocus) {
        mTextSearch.setVisibility(isExpandedSearchView ? View.GONE : View.VISIBLE);
        if (mAdapter != null) {
            mAdapter.setIsAllowOnLoadMore(!hasFocus);
        }
    }

}
