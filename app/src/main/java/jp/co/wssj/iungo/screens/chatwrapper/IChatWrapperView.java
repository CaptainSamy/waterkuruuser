package jp.co.wssj.iungo.screens.chatwrapper;

import java.util.List;

import jp.co.wssj.iungo.model.chat.StoreFollowResponse;
import jp.co.wssj.iungo.screens.switcher.ISwitcherView;

/**
 * Created by HieuPT on 10/17/2017.
 */

public interface IChatWrapperView extends ISwitcherView {

    void displayChatScreen(StoreFollowResponse.StoreChatData.StoreFollow storeFollow);

    void displayStoreFollowScreen(List<StoreFollowResponse.StoreChatData.StoreFollow> storeFollowList);
}
