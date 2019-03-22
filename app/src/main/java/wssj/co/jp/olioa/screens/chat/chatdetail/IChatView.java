package wssj.co.jp.olioa.screens.chat.chatdetail;

import java.util.List;

import wssj.co.jp.olioa.model.chat.HistoryChatResponse;
import wssj.co.jp.olioa.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public interface IChatView extends IFragmentView {

    void onGetHistoryChatSuccess(List<HistoryChatResponse.HistoryChatData.ChatData> history);

    void onGetHistoryChatFailure(String message);

    void onSendChatSuccess();

    void onSendChatFailure(String message);
}
