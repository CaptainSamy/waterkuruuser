package jp.co.wssj.iungo.screens.chat.chatdetail;

import java.util.List;

import jp.co.wssj.iungo.model.chat.HistoryChatResponse;
import jp.co.wssj.iungo.screens.base.IFragmentView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public interface IChatView extends IFragmentView {

    void onGetHistoryChatSuccess(List<HistoryChatResponse.HistoryChatData.ChatData> history);

    void onGetHistoryChatFailure(String message);

    void onSendChatSuccess();

    void onSendChatFailure(String message);
}
