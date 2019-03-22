package wssj.co.jp.olioa.screens.chat;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.chat.StoreFollowResponse;
import wssj.co.jp.olioa.screens.chat.adapter.StoreFollowAdapter;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.chatrealtime.chatdeatail.ChatRealTimeFragment;
import wssj.co.jp.olioa.utils.Logger;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class StoreFollowFragment extends BaseFragment<IStoreFollowView, StoreFollowPresenter>
        implements IStoreFollowView, SwipeRefreshLayout.OnRefreshListener {

    public static final String KEY_STORE_FOLLOW_LIST = "KEY_STORE_FOLLOW_LIST";

    private static final String TAG = "StoreFollowFragment";

    private ListView mListViewStoreFollow;

    private SwipeRefreshLayout mRefreshStoreFollow;

    private List<StoreFollowResponse.StoreChatData.StoreFollow> mStoreFollows;

    private StoreFollowAdapter mAdapter;

    public static StoreFollowFragment newInstance(Bundle bundle) {
        StoreFollowFragment fragment = new StoreFollowFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_store_follow;
    }

    @Override
    protected boolean isRetainState() {
        return true;
    }

    @Override
    protected StoreFollowPresenter onCreatePresenter(IStoreFollowView view) {
        return new StoreFollowPresenter(view);
    }

    @Override
    protected IStoreFollowView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_store_follow);
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_STORE_FOLLOW;
    }

    @Override
    protected void initViews(View rootView) {
        mListViewStoreFollow = (ListView) rootView.findViewById(R.id.lvStoreFollow);
        mRefreshStoreFollow = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshStoreFollow);
    }

    @Override
    protected void initAction() {
        mListViewStoreFollow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                StoreFollowResponse.StoreChatData.StoreFollow store = (StoreFollowResponse.StoreChatData.StoreFollow) parent.getItemAtPosition(position);
//                bundle.putInt(ChatFragment.KEY_STORE_ID, store.getId());
//                bundle.putString(ChatFragment.KEY_STORE_NAME, store.getStoreName());
//                bundle.putString(ChatFragment.KEY_IMAGE_STORE, store.getImageStore());
//                getActivityCallback().displayScreen(IMainView.FRAGMENT_CHAT, true, true, bundle);
                Logger.d("keystore1",store.getId()+"");
                bundle.putInt(ChatRealTimeFragment.KEY_STORE_ID, store.getId());
                bundle.putString(ChatRealTimeFragment.KEY_STORE_NAME, store.getStoreName());
                bundle.putString(ChatRealTimeFragment.KEY_IMAGE_STORE, store.getImageStore());
                getActivityCallback().displayScreen(IMainView.FRAGMENT_CHAT_REALTIME, true, true, bundle);

            }
        });

        mRefreshStoreFollow.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mStoreFollows = bundle.getParcelableArrayList(KEY_STORE_FOLLOW_LIST);
        }
        if (mStoreFollows == null) {
            mStoreFollows = new ArrayList<>();
            mAdapter = new StoreFollowAdapter(getActivityContext(), mStoreFollows);
            setRefreshing(true);
            getPresenter().getListStoreFollow();
        } else if (mStoreFollows.isEmpty()) {
            showTextNoItem(true, getString(R.string.no_conversation));
        } else {
            mAdapter = new StoreFollowAdapter(getActivityContext(), mStoreFollows);
        }
        mListViewStoreFollow.setAdapter(mAdapter);
    }
    @Override
    public void onGetListStoreFollowSuccess(List<StoreFollowResponse.StoreChatData.StoreFollow> storeFollows) {
        setRefreshing(false);
        if (storeFollows != null && storeFollows.size() > 0) {
            if (mStoreFollows != null) {
                mStoreFollows.clear();
                mStoreFollows.addAll(storeFollows);
            }
            mAdapter.notifyDataSetChanged();
            showTextNoItem(false, null);
        } else {
            showTextNoItem(true, getString(R.string.no_conversation));
        }
    }

    @Override
    public void onGetListStoreFollowFailure(String message) {
        showToast(message);
        setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getPresenter().getListStoreFollow();
    }

    public void setRefreshing(boolean isRefresh) {
        mRefreshStoreFollow.setRefreshing(isRefresh);
    }
}
