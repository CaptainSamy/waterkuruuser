package wssj.co.jp.olioa.model.auth;

import com.google.gson.annotations.SerializedName;

import wssj.co.jp.olioa.model.GsonSerializable;
import wssj.co.jp.olioa.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 17/10/2017.
 */

public class InfoUserResponse extends ResponseData<InfoUserResponse.InfoUser> {

    public static class InfoUser implements GsonSerializable {

        @SerializedName("age_avg")
        private int mAvg;

        @SerializedName("email")
        private String mEmail;

        @SerializedName("img_url")
        private String mAvatar;

        @SerializedName("name")
        private String mName;

        @SerializedName("sex")
        private int mSex;

        private boolean mChangePassword;

        private String mCurrentPassword;

        private String mNewPassword;

        private String mConfirmPassword;

        public int getAvg() {
            return mAvg;
        }

        public String getEmail() {
            return mEmail;
        }

        public String getAvatar() {
            return mAvatar;
        }

        public String getName() {
            return mName;
        }

        public int getSex() {
            return mSex;
        }

        public void setAvg(int mAvg) {
            this.mAvg = mAvg;
        }

        public void setEmail(String mEmail) {
            this.mEmail = mEmail;
        }

        public void setAvatar(String mAvatar) {
            this.mAvatar = mAvatar;
        }

        public void setName(String mName) {
            this.mName = mName;
        }

        public void setSex(int mSex) {
            this.mSex = mSex;
        }

        public void setCurrentPassword(String currentPassword) {
            this.mCurrentPassword = currentPassword;
        }

        public String getCurrentPassword() {
            return mCurrentPassword;
        }

        public String getNewPassword() {
            return mNewPassword;
        }

        public void setNewPassword(String newPassword) {
            this.mNewPassword = newPassword;
        }

        public String getConfirmPassword() {
            return mConfirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.mConfirmPassword = confirmPassword;
        }

        public boolean isChangePassword() {
            return mChangePassword;
        }

        public void setChangePassword(boolean changePassword) {
            this.mChangePassword = changePassword;
        }
    }

}
