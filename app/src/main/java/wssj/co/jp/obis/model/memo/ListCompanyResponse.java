package wssj.co.jp.obis.model.memo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import wssj.co.jp.obis.model.GsonSerializable;
import wssj.co.jp.obis.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 2/6/2017.
 */

public class ListCompanyResponse extends ResponseData<ListCompanyResponse.ServicesData> {

    public class ServicesData implements GsonSerializable {

        @SerializedName("company")
        private List<Company> mListServices;

        public List<Company> getListServices() {
            return mListServices;
        }
    }

    public class Company implements GsonSerializable {

        @SerializedName("service_id")
        private int mServiceId;

        @SerializedName("company_id")
        private int mCompanyId;

        @SerializedName("service_company_id")
        private int mServiceCompanyId;

        @SerializedName("name")
        private String mName;

        @SerializedName("logo")
        private String mLogo;

        @SerializedName("card_name")
        private String mCardName;

        @SerializedName("available_card")
        private int mAvailableCard;

        public int getServiceId() {
            return mServiceId;
        }

        public int getCompanyId() {
            return mCompanyId;
        }

        public int getServiceCompanyId() {
            return mServiceCompanyId;
        }

        public String getName() {
            return mName;
        }

        public String getLogo() {
            return mLogo;
        }

        public String getCardName() {
            return mCardName;
        }

        public int getAvailableCard() {
            return mAvailableCard;
        }
    }

}
