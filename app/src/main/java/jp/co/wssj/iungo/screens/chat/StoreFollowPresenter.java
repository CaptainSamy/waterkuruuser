package jp.co.wssj.iungo.screens.chat;

import java.util.List;

import jp.co.wssj.iungo.model.chat.ChatModel;
import jp.co.wssj.iungo.model.chat.StoreFollowResponse;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class StoreFollowPresenter extends FragmentPresenter<IStoreFollowView> {

    protected StoreFollowPresenter(IStoreFollowView view) {
        super(view);
        registerModel(new ChatModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    public void getListStoreFollow() {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getView().showProgress();
        getModel(ChatModel.class).getListStoreFollow(token, new ChatModel.OnGetListStoreFollowCallback() {

            @Override
            public void onGetListStoreFollowSuccess(List<StoreFollowResponse.StoreChatData.StoreFollow> storeFollows) {
                getView().hideProgress();
                getView().onGetListStoreFollowSuccess(storeFollows);
            }

            @Override
            public void onGetListStoreFollowFailure(String message) {
                getView().hideProgress();
                getView().onGetListStoreFollowFailure(message);
            }
        });

    }
}
