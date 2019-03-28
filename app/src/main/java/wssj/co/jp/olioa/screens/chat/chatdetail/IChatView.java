package wssj.co.jp.olioa.screens.chat.chatdetail;

import java.util.List;

import wssj.co.jp.olioa.model.chat.ChatMessage;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public interface IChatView extends IFragmentView {

    void onGetHistoryChatSuccess(List<ChatMessage> history);

    void onSendChatSuccess(ChatMessage chatMessage);

    void onSendChatFailure(String message);
}
