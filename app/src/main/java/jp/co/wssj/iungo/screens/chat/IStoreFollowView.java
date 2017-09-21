package jp.co.wssj.iungo.screens.chat;

import java.util.List;

import jp.co.wssj.iungo.model.chat.StoreFollowResponse;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public interface IStoreFollowView extends IFragmentView {

    void onGetListStoreFollowSuccess(List<StoreFollowResponse.StoreChatData.StoreFollow> storeFollows);

    void onGetListStoreFollowFailure(String message);
}
