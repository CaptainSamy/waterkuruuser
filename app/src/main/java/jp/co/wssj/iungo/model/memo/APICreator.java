package jp.co.wssj.iungo.model.memo;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.wssj.iungo.model.ResponseData;
import jp.co.wssj.iungo.model.volleylistener.ResponseListener;
import jp.co.wssj.iungo.model.volleyrequest.GsonJsonRequest;
import jp.co.wssj.iungo.model.volleyrequest.GsonRequest;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

final class APICreator {

    private static final String TAG = "Note.APICreator";

    private static final String GET_LIST_SERVICES_URL = Constants.BASE_URL + "/api/client/users/get-service-list";

    private static final String GET_USER_MEMO_URL = Constants.BASE_URL + "/api/client/users/get-memo-user";

    private static final String UPDATE_USER_MEMO_URL = Constants.BASE_URL + "/api/client/users/set-memo-user";

    static GsonRequest<MemoDynamicResponse> getUserMemoConfigRequest(String token, final int servicesId, final Response.Listener<MemoDynamicResponse> responseListener, final Response.ErrorListener errorListener) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Accept", "application/json");
        ResponseListener<MemoDynamicResponse> listener = new ResponseListener<>(TAG, "#getUserMemoConfigRequest", responseListener, errorListener);
        return new GsonJsonRequest<MemoDynamicResponse>(Request.Method.POST, GET_USER_MEMO_URL, MemoDynamicResponse.class, header, listener, listener) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("service_id", String.valueOf(servicesId));
                return map;
            }
        };
    }

    static GsonRequest<ListServiceResponse> getListService(String token, final Response.Listener<ListServiceResponse> listener, final Response.ErrorListener errorListener) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Accept", "application/json");
        return new GsonRequest<>(
                Request.Method.GET, GET_LIST_SERVICES_URL,
                ListServiceResponse.class,
                header,
                new Response.Listener<ListServiceResponse>() {

                    @Override
                    public void onResponse(ListServiceResponse response) {
                        Logger.i(TAG, "#getListService => onResponse");
                        listener.onResponse(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.i(TAG, "#getListService => onErrorResponse");
                errorListener.onErrorResponse(error);
            }
        });
    }

    static GsonRequest<ResponseData> updateUserMemo(String token, final int serviceId, final List<MemoDynamicResponse.UserMemoData.UserMemoValue> memoValues, final Response.Listener<ResponseData> responseListener, final Response.ErrorListener errorListener) {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", token);
        header.put("Accept", "application/json");
        ResponseListener<ResponseData> listener = new ResponseListener<>(TAG, "#updateUserMemo", responseListener, errorListener);
        return new GsonJsonRequest<ResponseData>(
                Request.Method.POST,
                UPDATE_USER_MEMO_URL,
                ResponseData.class,
                header,
                listener,
                listener) {

            @Override
            protected Map<String, Object> getBodyParams() {
                Map<String, Object> map = new HashMap<>();
                map.put("service_id", String.valueOf(serviceId));
                JSONArray array = new JSONArray();
                for (MemoDynamicResponse.UserMemoData.UserMemoValue item : memoValues) {
                    Map<String, Object> mapItem = new HashMap<>();
                    mapItem.put("id", item.getId());
                    mapItem.put("type", item.getType());
                    MemoDynamicResponse.UserMemoData.UserMemoValue.Value itemValue = item.getValue();
                    Map<String, Object> mapValue = new HashMap<>();
                    switch (item.getType()) {
                        case Constants.MemoConfig.TYPE_LONG_TEXT:
                        case Constants.MemoConfig.TYPE_LEVEL:
                        case Constants.MemoConfig.TYPE_SHORT_TEXT:
                            mapValue.put("value", itemValue.getValue());
                            break;
                        case Constants.MemoConfig.TYPE_COMBO_BOX:
                            mapValue.put("selected_position", itemValue.getSelectedItem());
                            break;
                        case Constants.MemoConfig.TYPE_SWITCH:
                            mapValue.put("status", itemValue.getStatus());
                            break;
                        case Constants.MemoConfig.TYPE_IMAGE:
                            JSONArray arrImg = new JSONArray();
                            for (MemoDynamicResponse.UserMemoData.UserMemoValue.Value.Image itemImage : itemValue.getListImage()) {
                                Map<String, Object> mapItemImage = new HashMap<>();
                                mapItemImage.put("id", itemImage.getImageId());
                                mapItemImage.put("value", itemImage.getUrlImage());
                                arrImg.put(new JSONObject(mapItemImage));
                            }
                            mapValue.put("images", arrImg);

                    }
                    mapItem.put("value", new JSONObject(mapValue));
                    array.put(new JSONObject(mapItem));
                }
                map.put("user_memo_value", array);
                return map;
            }
        };
    }

}
