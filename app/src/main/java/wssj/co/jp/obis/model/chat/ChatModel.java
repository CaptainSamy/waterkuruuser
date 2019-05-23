package wssj.co.jp.obis.model.chat;

import android.content.Context;

import java.util.List;

import wssj.co.jp.obis.model.BaseModel;
import wssj.co.jp.obis.model.baseapi.APICallback;
import wssj.co.jp.obis.model.entities.BlockChatStore;
import wssj.co.jp.obis.model.entities.GroupChatMessage;

/**
 * Created by Nguyen Huu Ta on 21/9/2017.
 */

public class ChatModel extends BaseModel {

    public ChatModel(Context context) {
        super(context);
    }


    public void getHistoryChat(int storeId, long lastChatId, APICallback<List<ChatMessage>> callback) {
        getApi().getHistoryChat(storeId, lastChatId).getAsyncResponse(callback);
    }

    public void sendChat(int storeId, String content, APICallback<ChatMessage> callback) {
        MessageBody messageBody = new MessageBody(storeId, content);
        getApi().sendChat(messageBody).getAsyncResponse(callback);
    }


    public void getHistoryGroupChat(int groupId, long lastChatId, APICallback<List<GroupChatMessage>> callback) {
        getApi().getHistoryGroupChat(groupId, lastChatId).getAsyncResponse(callback);
    }


    public void sendGroupChat(int storeId, String content, APICallback<ChatMessage> callback) {
        MessageBody messageBody = new MessageBody(storeId, content);
        getApi().sendGroupChat(messageBody).getAsyncResponse(callback);
    }

    public void getListStoreBlock(APICallback<List<BlockChatStore>> callback){
        getApi().getBlockChat().getAsyncResponse(callback);
    }

    public void blockStore(BlockChatStore param,APICallback callback){
        getApi().saveListBlock(param).getAsyncResponse(callback);
    }

}
