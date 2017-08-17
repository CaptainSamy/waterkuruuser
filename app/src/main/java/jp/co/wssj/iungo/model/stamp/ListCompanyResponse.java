package jp.co.wssj.iungo.model.stamp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

/**
 * Created by HieuPT on 5/18/2017.
 */

public class ListCompanyResponse extends ResponseData<ListCompanyResponse.ListCompanyData> {

    public class ListCompanyData implements GsonSerializable {

        @SerializedName("company")
        private List<CompanyData> mCompanies;

        public List<CompanyData> getListCompany() {
            return mCompanies;
        }

        public class CompanyData implements GsonSerializable {

            @SerializedName("service_id")
            private int mServiceId;

            @SerializedName("service_name")
            private String mServiceName;

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
            private int mCardNumber;

            @SerializedName("card_type")
            private int mCardType;

            @SerializedName("unread_push")
            private int mUnreadPush;

            public int getServiceId() {
                return mServiceId;
            }

            public String getServiceName() {
                return mServiceName;
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

            public int getCardNumber() {
                return mCardNumber;
            }

            /*
*card_type:
* 1 - stamp
* 2 - point
* 3 - None
* */
            public int getCardType() {
                return mCardType;
            }

            public int getUnreadPush() {
                return mUnreadPush;
            }
        }
    }
}
