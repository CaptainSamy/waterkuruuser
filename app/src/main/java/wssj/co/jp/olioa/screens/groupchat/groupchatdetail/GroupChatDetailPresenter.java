package wssj.co.jp.olioa.screens.groupchat.groupchatdetail;

import java.util.Collections;
import java.util.List;

import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.model.chat.ChatMessage;
import wssj.co.jp.olioa.model.chat.ChatModel;
import wssj.co.jp.olioa.model.entities.GroupChat;
import wssj.co.jp.olioa.model.entities.GroupChatMessage;
import wssj.co.jp.olioa.model.preference.SharedPreferencesModel;
import wssj.co.jp.olioa.screens.base.FragmentPresenter;

public class GroupChatDetailPresenter extends FragmentPresenter<IGroupChatDetailView> {
    protected GroupChatDetailPresenter(IGroupChatDetailView view) {
        super(view);
        registerModel(new ChatModel(view.getViewContext()));
        registerModel(new SharedPreferencesModel(view.getViewContext()));
    }


    public SharedPreferencesModel getShareModel() {
        return getModel(SharedPreferencesModel.class);
    }

    void getHistoryChat(int groupId, long lastChatId) {
        getModel(ChatModel.class).getHistoryGroupChat(groupId, lastChatId, new APICallback<List<GroupChatMessage>>() {

            @Override
            public void onSuccess(List<GroupChatMessage> chatMessageResponses) {
                getView().hideProgress();
                if (chatMessageResponses != null) {
                    Collections.reverse(chatMessageResponses);
                    getView().onGetHistoryChatSuccess(chatMessageResponses);
                }

            }

            @Override
            public void onFailure(String errorMessage) {
                getView().hideProgress();
                getView().showDialog(errorMessage);
            }
        });
    }

    void sendChat(int groupId, String content) {
        getModel(ChatModel.class).sendGroupChat(groupId, content, new APICallback<ChatMessage>() {

            @Override
            public void onSuccess(ChatMessage chatMessage) {
                GroupChatMessage groupChatMessage = new GroupChatMessage();
                groupChatMessage.mapping(chatMessage);
                getView().onSendChatSuccess(groupChatMessage);
            }

            @Override
            public void onFailure(String errorMessage) {
                getView().onSendChatFailure(errorMessage);
            }
        });
    }

    void saveLastTimeReadChat(int groupId) {
        getShareModel().putLastTimeReadChatGroup(groupId, System.currentTimeMillis());
    }


}
