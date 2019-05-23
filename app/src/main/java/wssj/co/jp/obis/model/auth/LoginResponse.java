package wssj.co.jp.obis.model.auth;

import com.google.gson.annotations.SerializedName;

import wssj.co.jp.obis.model.GsonSerializable;
import wssj.co.jp.obis.model.ResponseData;

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

        @SerializedName("password")
        private String mPassword;

        @SerializedName("name")
        private String mName;

        @SerializedName("reset_password_required")
        private boolean mIsRequireReset;

        @SerializedName("img_user")
        private String mImageUser;

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

        public String getImageUser() {
            return mImageUser;
        }

        public String getPassword() {
            return mPassword;
        }

        public String getName() {
            return mName;
        }
    }
}
