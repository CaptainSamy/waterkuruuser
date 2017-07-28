package jp.co.wssj.iungo.model.volleyrequest;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import jp.co.wssj.iungo.model.volleylistener.ReAuthenticationErrorListener;
import jp.co.wssj.iungo.utils.Constants;

public class GsonRequest<T> extends Request<T> {

    public static final String TOKEN_EXPIRED = "セクションが切りました。再ログインしてください";

    private final Gson mGson = new Gson();

    private final Class<T> mClazz;

    private final Map<String, String> mHeaders;

    private final Response.Listener<T> mListener;

    private ReAuthenticationErrorListener mErrorListener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url     URL of the request to make
     * @param clazz   Relevant class object, for Gson's reflection
     * @param headers Map of request mHeaders
     */
    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, url, clazz, headers, DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, listener, errorListener);
    }

    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, int timeOutMs,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mClazz = clazz;
        mHeaders = headers;
        mListener = listener;
        mErrorListener = new ReAuthenticationErrorListener(this, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(timeOutMs, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        Map<String, String> params = getParams();
        if (params != null && params.size() > 0) {
            return encodeParameters(params, getParamsEncoding());
        }
        return null;
    }

    private byte[] encodeParameters(Map<String, String> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            Map.Entry<String, String> entry;
            while (iterator.hasNext()) {
                entry = iterator.next();
                encodedParams.append(URLEncoder.encode(entry.getKey(), paramsEncoding));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), paramsEncoding));
                if (iterator.hasNext()) {
                    encodedParams.append('&');
                }
            }
            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, uee);
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers, Constants.UTF8_ENCODING));
            return Response.success(
                    mGson.fromJson(json, mClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
