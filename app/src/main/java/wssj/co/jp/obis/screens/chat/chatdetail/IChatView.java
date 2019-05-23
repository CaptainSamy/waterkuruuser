package wssj.co.jp.obis.screens.chat.chatdetail;

import java.util.List;

import wssj.co.jp.obis.model.chat.ChatMessage;
import wssj.co.jp.obis.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public interface IChatView extends IFragmentView {

    void onGetHistoryChatSuccess(List<ChatMessage> history);

    void onSendChatSuccess(ChatMessage chatMessage);

    void onSendChatFailure(String message);
}
