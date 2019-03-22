package wssj.co.jp.olioa.model.auth;

import com.google.gson.annotations.SerializedName;

import wssj.co.jp.olioa.model.GsonSerializable;

/**
 * Created by HieuPT on 17/5/2017.
 */

public class RegisterData implements GsonSerializable {

    @SerializedName("username")
    private String mUserName;

    @SerializedName("email")
    private String mEmail;

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

    public String getUserName() {
        return mUserName;
    }

    public String getEmail() {
        return mEmail;
    }
}
