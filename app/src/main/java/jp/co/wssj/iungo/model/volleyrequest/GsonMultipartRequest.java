package jp.co.wssj.iungo.model.volleyrequest;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by HieuPT on 4/20/2017.
 */

public class GsonMultipartRequest<T> extends MultipartRequest {

    private static final String TAG = "GsonMultipartRequest";

    public GsonMultipartRequest(String url, Map<String, String> headers, final Class<T> clazz, final Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(url, headers, new Response.Listener<NetworkResponse>() {

            @Override
            public void onResponse(NetworkResponse response) {
                T res = null;
                try {
                    Gson gson = new Gson();
                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers, Constants.UTF8_ENCODING));
                    res = gson.fromJson(json, clazz);
                } catch (UnsupportedEncodingException e) {
                    Logger.e(TAG, "UnsupportedEncodingException: " + e.getMessage());
                } catch (JsonSyntaxException e) {
                    Logger.e(TAG, "JsonSyntaxException: " + e.getMessage());
                }
                listener.onResponse(res);
            }
        }, errorListener);
    }
}
