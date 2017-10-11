package jp.co.wssj.iungo.screens.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.chat.StoreFollowResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.PagedFragment;
import jp.co.wssj.iungo.screens.chat.adapter.StoreFollowAdapter;
import jp.co.wssj.iungo.screens.chat.chatdetail.ChatFragment;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class StoreFollowFragment extends PagedFragment<IStoreFollowView, StoreFollowPresenter> implements IStoreFollowView {

    private static final String TAG = "StoreFollowFragment";

    private ListView mListViewStoreFollow;

    private List<StoreFollowResponse.StoreChatData.StoreFollow> mStoreFollows;

    private StoreFollowAdapter mAdapter;

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
    public String getPageTitle(Context context) {
        return getString(context, R.string.title_screen_store_follow);
    }

    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_another;
    }

    @Override
    protected void initViews(View rootView) {
        mListViewStoreFollow = (ListView) rootView.findViewById(R.id.lvStoreFollow);
    }

    @Override
    protected void initAction() {
        mListViewStoreFollow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                StoreFollowResponse.StoreChatData.StoreFollow store = (StoreFollowResponse.StoreChatData.StoreFollow) parent.getItemAtPosition(position);
                bundle.putInt(ChatFragment.KEY_STORE_ID, store.getId());
                getActivityCallback().displayScreen(IMainView.FRAGMENT_CHAT, true, true, bundle);
            }
        });
    }

    @Override
    protected void initData() {
        mStoreFollows = new ArrayList<>();
        mAdapter = new StoreFollowAdapter(getActivityContext(), mStoreFollows);
        mListViewStoreFollow.setAdapter(mAdapter);
        getPresenter().getListStoreFollow();
    }

    @Override
    public void onGetListStoreFollowSuccess(List<StoreFollowResponse.StoreChatData.StoreFollow> storeFollows) {
        mStoreFollows.addAll(storeFollows);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetListStoreFollowFailure(String message) {
        showToast(message);
    }
}
