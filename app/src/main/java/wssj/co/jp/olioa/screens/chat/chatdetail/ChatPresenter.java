package wssj.co.jp.olioa.screens.chat.chatdetail;

import java.util.List;

import wssj.co.jp.olioa.model.chat.ChatModel;
import wssj.co.jp.olioa.model.chat.HistoryChatResponse;
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

    public void getHistoryChat(int storeId, int lastChatId) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(ChatModel.class).getHistoryChat(token, storeId,lastChatId, new ChatModel.OnGetHistoryChatCallback() {

            @Override
            public void onGetHistoryChatSuccess(List<HistoryChatResponse.HistoryChatData.ChatData> history) {
                getView().onGetHistoryChatSuccess(history);
            }

            @Override
            public void onGetHistoryChatFailure(String message) {
                getView().onGetHistoryChatFailure(message);
            }
        });
    }

    public void sendChat(int storeId, String content) {
        String token = getModel(SharedPreferencesModel.class).getToken();
        getModel(ChatModel.class).sendChat(token, storeId, content, new ChatModel.OnSendChatCallback() {

            @Override
            public void onSendChatSuccess() {
                getView().onSendChatSuccess();
            }

            @Override
            public void onSendChatFailure(String message) {
                getView().onSendChatFailure(message);
            }
        });
    }
}
