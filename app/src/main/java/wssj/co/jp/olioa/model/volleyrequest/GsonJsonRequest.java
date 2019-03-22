package wssj.co.jp.olioa.model.volleyrequest;

import com.android.volley.Response;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Logger;

/**
 * Created by HieuPT on 4/26/2017.
 */

public class GsonJsonRequest<T> extends GsonRequest<T> {

    private static final String TAG = "GsonJsonRequest";

    public GsonJsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, clazz, headers, listener, errorListener);
    }

    public GsonJsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, int timeOutMs, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, clazz, headers, timeOutMs, listener, errorListener);
    }

    protected Map<String, Object> getBodyParams() {
        return null;
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    public byte[] getBody() {
        try {
            Map<String, Object> params = getBodyParams();
            JSONObject jsonObject = params != null ? new JSONObject(params) : null;
            return jsonObject == null ? null : jsonObject.toString().getBytes(Constants.UTF8_ENCODING);
        } catch (UnsupportedEncodingException e) {
            Logger.e(TAG, "UnsupportedEncodingException: " + e.getMessage());
            return null;
        }
    }
}
