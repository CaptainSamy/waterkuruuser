package jp.co.wssj.iungo.screens.chatwrapper;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.chat.StoreFollowResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.chat.StoreFollowFragment;
import jp.co.wssj.iungo.screens.chat.chatdetail.ChatFragment;
import jp.co.wssj.iungo.screens.switcher.SwitcherFragment;

/**
 * Created by HieuPT on 10/17/2017.
 */

public class ChatWrapperFragment extends SwitcherFragment<IChatWrapperView, ChatWrapperPresenter> implements IChatWrapperView {

    private static final String TAG = "ChatWrapperFragment";

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected ChatWrapperPresenter onCreatePresenter(IChatWrapperView view) {
        return new ChatWrapperPresenter(view);
    }

    @Override
    protected IChatWrapperView onCreateView() {
        return this;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_CHAT_WRAPPER;
    }

    @Override
    public boolean isDisplayNavigationButton() {
        return false;
    }

    @Override
    public boolean onBackPressed() {
        getActivityCallback().finishActivity();
        return true;
    }

    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_another;
    }

    @Override
    protected boolean isRetainState() {
        return true;
    }

    @Override
    protected void initData() {
        getPresenter().getListStoreFollow();
    }

    @Override
    public void displayChatScreen(StoreFollowResponse.StoreChatData.StoreFollow storeFollow) {
        Bundle bundle = new Bundle(createBaseBundle());
        bundle.putInt(ChatFragment.KEY_STORE_ID, storeFollow.getId());
        bundle.putString(ChatFragment.KEY_STORE_NAME, storeFollow.getStoreName());
        bundle.putString(ChatFragment.KEY_IMAGE_STORE, storeFollow.getImageStore());
        displayFragment(ChatFragment.newInstance(bundle));
    }

    @Override
    public void displayStoreFollowScreen(List<StoreFollowResponse.StoreChatData.StoreFollow> storeFollowList) {
        Bundle bundle = new Bundle(createBaseBundle());
        if (storeFollowList != null) {
            bundle.putParcelableArrayList(StoreFollowFragment.KEY_STORE_FOLLOW_LIST, new ArrayList<>(storeFollowList));
        }
        displayFragment(StoreFollowFragment.newInstance(bundle));
    }
}
