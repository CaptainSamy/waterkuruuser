package wssj.co.jp.obis.screens.groupchat.groupchatdetail;

import java.util.List;

import wssj.co.jp.obis.model.entities.GroupChatMessage;
import wssj.co.jp.obis.screens.base.IFragmentView;

public interface IGroupChatDetailView extends IFragmentView {

    void onGetHistoryChatSuccess(List<GroupChatMessage> messageList);

    void onSendChatSuccess(GroupChatMessage chatMessage);

    void onSendChatFailure(String message);

}
