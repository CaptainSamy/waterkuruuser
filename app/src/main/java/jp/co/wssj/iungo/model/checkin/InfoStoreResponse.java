package jp.co.wssj.iungo.model.checkin;

import com.google.gson.annotations.SerializedName;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 22/5/2017.
 */

public class InfoStoreResponse extends ResponseData<InfoStoreResponse.InfoStoreData> {

    public static class InfoStoreData implements GsonSerializable {

        @SerializedName("store_id")
        private int mStoreId;

        @SerializedName("store_name")
        private String mSoreName;

        @SerializedName("address")
        private String mAddress;

        @SerializedName("phone")
        private String mPhone;

        @SerializedName("information")
        private String mInformation;

        @SerializedName("logo")
        private String mLogoCompany;

        public int getStoreId() {
            return mStoreId;
        }

        public String getStoreName() {
            return mSoreName;
        }

        public String getAddress() {
            return mAddress;
        }

        public String getPhone() {
            return mPhone;
        }

        public String getInformation() {
            return mInformation;
        }

        public String getLogoCompany() {
            return mLogoCompany;
        }
    }
}
