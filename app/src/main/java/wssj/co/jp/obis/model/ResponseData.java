package wssj.co.jp.obis.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HieuPT on 5/17/2017.
 */

public class ResponseData<T extends GsonSerializable> implements GsonSerializable {

    @SerializedName("success")
    private boolean mIsSuccess;

    @SerializedName("message")
    private String mMessage;

    @SerializedName("data")
    private T mData;

    @SerializedName("result")
    private String result;

    public boolean isSuccess() {
        return mIsSuccess || result.equalsIgnoreCase("ok");
    }

    public String getMessage() {
        return mMessage;
    }

    public T getData() {
        return mData;
    }
}
