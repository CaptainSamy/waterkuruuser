package wssj.co.jp.obis.screens.chatwrapper;

import java.util.List;

import wssj.co.jp.obis.model.chat.StoreFollowResponse;
import wssj.co.jp.obis.screens.switcher.ISwitcherView;

public interface IChatWrapperView extends ISwitcherView {

    void displayChatScreen(StoreFollowResponse.StoreChatData.StoreFollow storeFollow);

    void displayStoreFollowScreen(List<StoreFollowResponse.StoreChatData.StoreFollow> storeFollowList);
}
