package wssj.co.jp.olioa.screens.chatwrapper;

import java.util.List;

import wssj.co.jp.olioa.model.chat.ChatModel;
import wssj.co.jp.olioa.model.chat.StoreFollowResponse;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.screens.switcher.SwitcherPresenter;

public class ChatWrapperPresenter extends SwitcherPresenter<IChatWrapperView> {

    protected ChatWrapperPresenter(IChatWrapperView view) {
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
                if (storeFollows != null) {
                    if (storeFollows.size() > 1) {
                        getView().displayStoreFollowScreen(storeFollows);
                        return;
                    } else if (storeFollows.size() == 1) {
                        getView().displayChatScreen(storeFollows.get(0));
                        return;
                    }
                }
                getView().displayStoreFollowScreen(null);
            }

            @Override
            public void onGetListStoreFollowFailure(String message) {
                getView().hideProgress();
                getView().displayStoreFollowScreen(null);
            }
        });
    }
}
