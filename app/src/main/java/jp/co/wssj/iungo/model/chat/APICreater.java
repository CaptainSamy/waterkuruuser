package jp.co.wssj.iungo.model.chat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import jp.co.wssj.iungo.model.ResponseData;
import jp.co.wssj.iungo.model.volleyrequest.GsonJsonRequest;
import jp.co.wssj.iungo.model.volleyrequest.GsonRequest;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by Nguyen Huu Ta on 21/9/2017.
 */

public class APICreater {

    private static final String TAG = "Chat.APICreator";

    private static final String API_GET_LIST_STORE_FOLLOW = Constants.BASE_URL + "/api/client/users/get-list-store-following";

    private static final String API_GET_HISTORY_CHAT = Constants.BASE_URL + "/api/client/users/get-list-chat-history-for-user";

    private static final String API_SEND_CHAT = Constants.BASE_URL + "/api/client/users/user-send-chat";

    static GsonRequest<StoreFollowResponse> getListStoreFollow(final String token, final Response.Listener<StoreFollowResponse> listener, final Response.ErrorListener errorListener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<>(Request.Method.GET,
                API_GET_LIST_STORE_FOLLOW,
                StoreFollowResponse.class,
                headers,
                new Response.Listener<StoreFollowResponse>() {

                    @Override
                    public void onResponse(StoreFollowResponse response) {
                        Logger.i(TAG, "#getListStoreFollow => onResponse");
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "#getListStoreFollow => onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                });
    }

    static GsonRequest<HistoryChatResponse> getHistoryChat(final String token, final int storeId, final Response.Listener<HistoryChatResponse> listener, final Response.ErrorListener errorListener) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<HistoryChatResponse>(Request.Method.POST,
                API_GET_HISTORY_CHAT,
                HistoryChatResponse.class,
                headers,
                new Response.Listener<HistoryChatResponse>() {

                    @Override
                    public void onResponse(HistoryChatResponse response) {
                        Logger.i(TAG, "#getListStoreFollow => onResponse");
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "#getListStoreFollow => onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("store_id", storeId);
                return map;
            }
        };
    }

    static GsonRequest<ResponseData> sendChat(final String token, final int storeId, final String content, final Response.Listener<ResponseData> listener, final Response.ErrorListener errorListener) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", token);
        return new GsonJsonRequest<ResponseData>(Request.Method.POST,
                API_SEND_CHAT,
                ResponseData.class,
                headers,
                new Response.Listener<ResponseData>() {

                    @Override
                    public void onResponse(ResponseData response) {
                        Logger.i(TAG, "#sendChat => onResponse");
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.i(TAG, "#sendChat => onErrorResponse");
                        errorListener.onErrorResponse(error);
                    }
                }) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("store_id", storeId);
                map.put("chat_info", content);
                return map;
            }
        };
    }

}
