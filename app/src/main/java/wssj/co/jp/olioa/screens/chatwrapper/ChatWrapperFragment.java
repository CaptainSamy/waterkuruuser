package wssj.co.jp.olioa.screens.chatwrapper;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.chat.StoreFollowResponse;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.chat.StoreFollowFragment;
import wssj.co.jp.olioa.utils.Logger;
import wssj.co.jp.olioa.screens.chatrealtime.chatdeatail.ChatRealTimeFragment;
import wssj.co.jp.olioa.screens.switcher.SwitcherFragment;

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
    public boolean isDisplayBackButton() {
        return false;
    }

    @Override
    public boolean onBackPressed() {
        getActivityCallback().finishActivity();
        return true;
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
//        bundle.putInt(ChatFragment.KEY_STORE_ID, storeFollow.getId());
//        bundle.putString(ChatFragment.KEY_STORE_NAME, storeFollow.getStoreName());
//        bundle.putString(ChatFragment.KEY_IMAGE_STORE, storeFollow.getImageStore());
//        displayFragment(ChatFragment.newInstance(bundle));
        bundle.putInt(ChatRealTimeFragment.KEY_STORE_ID, storeFollow.getId());
        bundle.putString(ChatRealTimeFragment.KEY_STORE_NAME, storeFollow.getStoreName());
        bundle.putString(ChatRealTimeFragment.KEY_IMAGE_STORE, storeFollow.getImageStore());
        displayFragment(ChatRealTimeFragment.newInstance(bundle));
    }

    @Override
    public void displayStoreFollowScreen(List<StoreFollowResponse.StoreChatData.StoreFollow> storeFollowList) {
        Logger.d("thangChat", storeFollowList.size() + "");
//        for (int i=0;i<storeFollowList.size();i++){
//            Logger.d("thangChat", storeFollowList.get(i).getId()+ "");
//        }
        Bundle bundle = new Bundle(createBaseBundle());
        if (storeFollowList != null) {
            bundle.putParcelableArrayList(StoreFollowFragment.KEY_STORE_FOLLOW_LIST, new ArrayList<>(storeFollowList));
        }
        displayFragment(StoreFollowFragment.newInstance(bundle));
    }
}
