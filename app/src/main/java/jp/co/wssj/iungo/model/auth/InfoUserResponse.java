package jp.co.wssj.iungo.model.auth;

import com.google.gson.annotations.SerializedName;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

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

        private String mNewAvatar;

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

        public String getNewAvatar() {
            return mNewAvatar;
        }

        public void setNewAvatar(String mNewAvatar) {
            this.mNewAvatar = mNewAvatar;
        }
    }

}
