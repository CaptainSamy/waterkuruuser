package jp.co.wssj.iungo.model.timeline;

import com.google.gson.annotations.SerializedName;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 6/11/2017.
 */

public class ProfileResponse extends ResponseData<ProfileResponse.Profile> {

    public class Profile implements GsonSerializable {

        @SerializedName("information")
        private String information;

        @SerializedName("name")
        private String storeName;

        @SerializedName("logo")
        private String logo;

        @SerializedName("logo_main")
        private String logoMain;

        @SerializedName("sort_description")
        private String description;

        public String getInformation() {
            return information;
        }

        public String getStoreName() {
            return storeName;
        }

        public String getLogo() {
            return logo;
        }

        public String getLogoMain() {
            return logoMain;
        }

        public String getDescription() {
            return description;
        }
    }

}
