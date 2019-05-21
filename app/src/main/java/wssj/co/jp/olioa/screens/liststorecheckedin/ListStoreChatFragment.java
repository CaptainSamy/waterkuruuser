package wssj.co.jp.olioa.screens.liststorecheckedin;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.entities.GroupChat;
import wssj.co.jp.olioa.model.entities.StoreInfo;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.groupchat.groupchatdetail.GroupChatDetailFragment;
import wssj.co.jp.olioa.screens.liststorecheckedin.adapter.ListStoreChatAdapter;
import wssj.co.jp.olioa.screens.splash.SplashFragment;

/**
 * Created by Nguyen Huu Ta on 12/6/2017.
 */

public class ListStoreChatFragment extends BaseFragment<IListStoreCheckedInView, ListStoreCheckedInPresenter> implements IListStoreCheckedInView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ListStorePushFragment";

    private static final int TYPE = 2;

    public static final String ARG_STORE_INFO = "storeInfo";

    private SwipeRefreshLayout mRefreshLayout;

    private ListView mListStoreCheckedIn;

    private TextView mEmptyView;

    private ListStoreChatAdapter mAdapter;

    private int mStoreId;

    public static ListStoreChatFragment newInstance(Bundle args) {
        ListStoreChatFragment fragment = new ListStoreChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_chat);
    }

    @Override
    public boolean isDisplayBackButton() {
        return false;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_LIST_STORE_CHAT;
    }

    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_chat;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_list_store_checked_in;
    }

    @Override
    protected ListStoreCheckedInPresenter onCreatePresenter(IListStoreCheckedInView view) {
        return new ListStoreCheckedInPresenter(view);
    }

    @Override
    protected IListStoreCheckedInView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        mListStoreCheckedIn = (ListView) view.findViewById(R.id.lvListStoreCheckedIn);
        mEmptyView = view.findViewById(R.id.emptyView);
    }

    @Override
    protected void initAction() {
        mRefreshLayout.setOnRefreshListener(this);
        mListStoreCheckedIn.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoreInfo storeInfo = (StoreInfo) parent.getItemAtPosition(position);
                getPresenter().saveLastTimeReadChat(storeInfo.getId());
                mAdapter.notifyDataSetChanged();
                Bundle bundle = new Bundle();
                bundle.putParcelable(ARG_STORE_INFO, storeInfo);
                getActivityCallback().displayScreen(IMainView.FRAGMENT_CHAT, true, true, bundle);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        if (getArguments() != null) {
            mStoreId = getArguments().getInt(SplashFragment.ARG_ID, -1);
        }
        mAdapter = new ListStoreChatAdapter(getActivityContext(), new ArrayList<StoreInfo>());
        mListStoreCheckedIn.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter.getCount() == 0) {
            getPresenter().getListStoreCheckedIn(TYPE);
        } else {
            getPresenter().getListStoreCheckedIn(TYPE, false);
        }
    }

    @Override
    public void onGetListStoreCheckedInSuccess(List<StoreInfo> listStores) {
        mAdapter.setListStore(listStores);
        mEmptyView.setVisibility(mAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);

        if (mStoreId != -1) {
            StoreInfo item = null;
            for (StoreInfo storeInfo : listStores) {
                if (storeInfo.getId() == mStoreId) {
                    item = storeInfo;
                    break;
                }
            }
            mStoreId = -1;
            if (item == null) return;
            getPresenter().saveLastTimeReadChat(item.getId());
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARG_STORE_INFO, item);
            getActivityCallback().displayScreen(IMainView.FRAGMENT_CHAT, true, true, bundle);
        }
    }

    @Override
    public void onGetListStoreCheckedInFailure(String message) {
        showToast(message);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        getPresenter().getListStoreCheckedIn(TYPE, false);
    }

    @Override
    protected void onRefreshWhenBackFragment() {
        super.onRefreshWhenBackFragment();
        getPresenter().getListStoreCheckedIn(TYPE, false);
    }
}
