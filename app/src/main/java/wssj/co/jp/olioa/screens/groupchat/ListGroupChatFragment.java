package wssj.co.jp.olioa.screens.groupchat;

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
import wssj.co.jp.olioa.screens.groupchat.adapter.ListGroupChatAdapter;
import wssj.co.jp.olioa.screens.groupchat.groupchatdetail.GroupChatDetailFragment;

/**
 * Created by Nguyen Huu Ta on 12/6/2017.
 */

public class ListGroupChatFragment extends BaseFragment<IGroupChatView, ListGroupChatPresenter> implements IGroupChatView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ListGroupChatFragment";

    public static final String ARG_STORE_INFO = "storeInfo";

    private SwipeRefreshLayout mRefreshLayout;

    private ListView mListStoreCheckedIn;

    private TextView mEmptyView;

    private ListGroupChatAdapter mAdapter;

    public static ListGroupChatFragment newInstance(Bundle args) {
        ListGroupChatFragment fragment = new ListGroupChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_group_chat);
    }

    @Override
    public boolean isDisplayBackButton() {
        return false;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_GROUP_CHAT;
    }

    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_group_chat;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_list_store_checked_in;
    }

    @Override
    protected ListGroupChatPresenter onCreatePresenter(IGroupChatView view) {
        return new ListGroupChatPresenter(view);
    }

    @Override
    protected IGroupChatView onCreateView() {
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
                GroupChat groupInfo = (GroupChat) parent.getItemAtPosition(position);
                getPresenter().saveLastTimeReadChat(groupInfo.getId());
                mAdapter.notifyDataSetChanged();
                Bundle bundle = new Bundle();
                bundle.putParcelable(GroupChatDetailFragment.ARG_GROUP, groupInfo);
                getActivityCallback().displayScreen(IMainView.FRAGMENT_GROUP_CHAT_DETAIL, true, true, bundle);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter = new ListGroupChatAdapter(getActivityContext(), new ArrayList<GroupChat>());
        mListStoreCheckedIn.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().getGroupChat(mAdapter.getCount() == 0);
    }

    @Override
    public void onGetListGroupSuccess(List<GroupChat> listGroup) {
        mAdapter.setListStore(listGroup);
        mEmptyView.setVisibility(mAdapter.getCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(false);
        getPresenter().getGroupChat(true);
    }

    @Override
    protected void onRefreshWhenBackFragment() {
        super.onRefreshWhenBackFragment();
        getPresenter().getGroupChat(false);
    }
}
