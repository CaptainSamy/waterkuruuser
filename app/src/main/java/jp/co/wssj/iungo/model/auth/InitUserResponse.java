package jp.co.wssj.iungo.model.auth;

import com.google.gson.annotations.SerializedName;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

/**
 * Created by i.am.Trung on 11/21/17.
 */

public class InitUserResponse extends ResponseData<InitUserResponse.InitUserData> {
    public static class InitUserData implements GsonSerializable {

        @SerializedName("status")
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
