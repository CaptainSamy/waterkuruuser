package wssj.co.jp.olioa.screens.groupchat.groupchatdetail;

import java.util.List;

import wssj.co.jp.olioa.model.chat.ChatMessage;
import wssj.co.jp.olioa.model.entities.GroupChatMessage;
import wssj.co.jp.olioa.screens.base.IFragmentView;
import wssj.co.jp.olioa.screens.base.IView;

public interface IGroupChatDetailView extends IFragmentView {

    void onGetHistoryChatSuccess(List<GroupChatMessage> messageList);

    void onSendChatSuccess(GroupChatMessage chatMessage);

    void onSendChatFailure(String message);

}
