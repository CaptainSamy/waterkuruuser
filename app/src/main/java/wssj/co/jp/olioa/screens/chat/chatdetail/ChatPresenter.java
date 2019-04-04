package wssj.co.jp.olioa.screens.chat.chatdetail;

import java.util.List;

import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.model.chat.ChatMessage;
import wssj.co.jp.olioa.model.chat.ChatModel;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class ChatPresenter extends FragmentPresenter<IChatView> {

    protected ChatPresenter(IChatView view) {
        super(view);
        registerModel(new ChatModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }

    public SharedPreferencesModel getShareModel() {
        return getModel(SharedPreferencesModel.class);
    }

    void getHistoryChat(int storeId, long lastChatId, boolean... isLoading) {
        if (isLoading.length == 0) {
            getView().showProgress();
        }
        getModel(ChatModel.class).getHistoryChat(storeId, lastChatId, new APICallback<List<ChatMessage>>() {

            @Override
            public void onSuccess(List<ChatMessage> chatMessageResponses) {
                getView().hideProgress();
                getView().onGetHistoryChatSuccess(chatMessageResponses);
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                getView().showDialog(errorMessage);
            }
        });
    }

    void sendChat(int storeId, String content) {
        getModel(ChatModel.class).sendChat(storeId, content, new APICallback<ChatMessage>() {

            @Override
            public void onSuccess(ChatMessage chatMessage) {
                getView().onSendChatSuccess(chatMessage);
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().onSendChatFailure(errorMessage);
            }
        });
    }

    void saveLastTimeReadChat(long storeId) {
        getShareModel().putLastTimeReadChat(storeId, System.currentTimeMillis());
    }
}
