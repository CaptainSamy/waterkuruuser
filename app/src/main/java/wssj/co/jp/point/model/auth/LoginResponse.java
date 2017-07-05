package wssj.co.jp.point.model.auth;

import com.google.gson.annotations.SerializedName;

import wssj.co.jp.point.model.GsonSerializable;
import wssj.co.jp.point.model.ResponseData;

/**
 * Created by HieuPT on 5/17/2017.
 */

public class LoginResponse extends ResponseData<LoginResponse.LoginData> {

    public class LoginData implements GsonSerializable {

        @SerializedName("username")
        private String mUserName;

        @SerializedName("email")
        private String mEmail;

        @SerializedName("token")
        private String mToken;

        @SerializedName("expire_date")
        private long mExpireDate;

        @SerializedName("reset_password_required")
        private boolean mIsRequireReset;

        public String getUserName() {
            return mUserName;
        }

        public String getEmail() {
            return mEmail;
        }

        public String getToken() {
            return mToken;
        }

        public long getExpireDate() {
            return mExpireDate;
        }

        public boolean isRequireResetPassword() {
            return mIsRequireReset;
        }
    }
}
