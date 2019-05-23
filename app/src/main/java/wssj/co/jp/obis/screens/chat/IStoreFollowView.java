package wssj.co.jp.obis.screens.chat;

import java.util.List;

import wssj.co.jp.obis.model.chat.StoreFollowResponse;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public interface IStoreFollowView extends IFragmentView {

    void onGetListStoreFollowSuccess(List<StoreFollowResponse.StoreChatData.StoreFollow> storeFollows);

    void onGetListStoreFollowFailure(String message);
}
