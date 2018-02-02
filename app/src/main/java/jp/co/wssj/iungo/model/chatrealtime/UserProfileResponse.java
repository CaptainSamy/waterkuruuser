package jp.co.wssj.iungo.model.chatrealtime;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;
import jp.co.wssj.iungo.model.coupone.Coupone;

/**
 * Created by thang on 1/26/2018.
 */

public class UserProfileResponse extends ResponseData<UserProfileResponse.UserProfileData> {
    public class UserProfileData implements GsonSerializable {
        @SerializedName("age_avg")
        private int ageAvg;

        @SerializedName("app_id")
        private int appId;

        @SerializedName("email")
        private String email;

        @SerializedName("id")
        private long id;

        @SerializedName("img_url")
        private String imgUrl;

        @SerializedName("name")
        private String name;

        public UserProfileData() {
        }

        public UserProfileData(int ageAvg, int appId, String email, long id, String imgUrl, String name) {
            this.ageAvg = ageAvg;
            this.appId = appId;
            this.email = email;
            this.id = id;
            this.imgUrl = imgUrl;
            this.name = name;
        }

        public int getAgeAvg() {
            return ageAvg;
        }

        public void setAgeAvg(int ageAvg) {
            this.ageAvg = ageAvg;
        }

        public int getAppId() {
            return appId;
        }

        public void setAppId(int appId) {
            this.appId = appId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

