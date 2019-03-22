package wssj.co.jp.olioa.screens.chatwrapper;

import java.util.List;

import wssj.co.jp.olioa.model.chat.StoreFollowResponse;
import wssj.co.jp.olioa.screens.switcher.ISwitcherView;

public interface IChatWrapperView extends ISwitcherView {

    void displayChatScreen(StoreFollowResponse.StoreChatData.StoreFollow storeFollow);

    void displayStoreFollowScreen(List<StoreFollowResponse.StoreChatData.StoreFollow> storeFollowList);
}
