package wssj.co.jp.olioa.model.chat;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.BaseModel;
import wssj.co.jp.olioa.model.ErrorResponse;
import wssj.co.jp.olioa.model.ResponseData;
import wssj.co.jp.olioa.utils.Utils;
import wssj.co.jp.olioa.utils.VolleySequence;

/**
 * Created by Nguyen Huu Ta on 21/9/2017.
 */

public class ChatModel extends BaseModel {

    public ChatModel(Context context) {
        super(context);
    }

    public interface OnGetListStoreFollowCallback {

        void onGetListStoreFollowSuccess(List<StoreFollowResponse.StoreChatData.StoreFollow> storeFollows);

        void onGetListStoreFollowFailure(String message);
    }

    public interface OnGetHistoryChatCallback {

        void onGetHistoryChatSuccess(List<HistoryChatResponse.HistoryChatData.ChatData> historys);

        void onGetHistoryChatFailure(String message);
    }

    public interface OnSendChatCallback {

        void onSendChatSuccess();

        void onSendChatFailure(String message);
    }

    public void getListStoreFollow(String token, final OnGetListStoreFollowCallback callback) {
        final Request request = APICreater.getListStoreFollow(token, new Response.Listener<StoreFollowResponse>() {

            @Override
            public void onResponse(StoreFollowResponse response) {
                if (response.isSuccess() && response.getData() != null) {
                    callback.onGetListStoreFollowSuccess(response.getData().getListStoreFollow());
                } else {
                    callback.onGetListStoreFollowFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onGetListStoreFollowFailure(errorResponse.getMessage());
                } else {
                    callback.onGetListStoreFollowFailure(getStringResource(R.string.network_error));
                }
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }

    public void getHistoryChat(String token, int storeId, int lastChatId,final OnGetHistoryChatCallback callback) {
        final Request request = APICreater.getHistoryChat(token, storeId,lastChatId, new Response.Listener<HistoryChatResponse>() {

            @Override
            public void onResponse(HistoryChatResponse response) {
                if (response.isSuccess() && response.getData() != null) {
                    callback.onGetHistoryChatSuccess(response.getData().getListChat());
                } else {
                    callback.onGetHistoryChatFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onGetHistoryChatFailure(errorResponse.getMessage());
                } else {
                    callback.onGetHistoryChatFailure(getStringResource(R.string.network_error));
                }
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }

    public void sendChat(String token, int storeId, String content, final OnSendChatCallback callback) {
        final Request request = APICreater.sendChat(token, storeId, content, new Response.Listener<ResponseData>() {

            @Override
            public void onResponse(ResponseData response) {
                if (response.isSuccess()) {
                    callback.onSendChatSuccess();
                } else {
                    callback.onSendChatFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onSendChatFailure(errorResponse.getMessage());
                } else {
                    callback.onSendChatFailure(getStringResource(R.string.network_error));
                }
            }
        });
        VolleySequence.getInstance().addRequest(request);
    }
}
