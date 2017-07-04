package wssj.co.jp.point.model.auth;

import com.google.gson.annotations.SerializedName;

import wssj.co.jp.point.model.GsonSerializable;

/**
 * Created by HieuPT on 17/5/2017.
 */

public class RegisterData implements GsonSerializable {

    @SerializedName("token")
    private String mToken;

    @SerializedName("expire_date")
    private long mExpireDate;

    public String getToken() {
        return mToken;
    }

    public long getExpireDate() {
        return mExpireDate;
    }
}
