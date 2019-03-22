package wssj.co.jp.olioa.model.stamp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import wssj.co.jp.olioa.model.GsonSerializable;
import wssj.co.jp.olioa.model.ResponseData;

/**
 * Created by HieuPT on 5/18/2017.
 */

public class ListCompanyResponse extends ResponseData<ListCompanyResponse.ListCompanyData> {

    public static class ListCompanyData implements GsonSerializable {

        @SerializedName("company")
        private List<CompanyData> mCompanies;

        public List<CompanyData> getListCompany() {
            return mCompanies;
        }

        public static class CompanyData implements GsonSerializable, Parcelable {

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

            protected CompanyData(Parcel in) {
                mServiceId = in.readInt();
                mServiceName = in.readString();
                mCompanyId = in.readInt();
                mServiceCompanyId = in.readInt();
                mName = in.readString();
                mLogo = in.readString();
                mCardName = in.readString();
                mCardNumber = in.readInt();
                mCardType = in.readInt();
                mUnreadPush = in.readInt();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(mServiceId);
                dest.writeString(mServiceName);
                dest.writeInt(mCompanyId);
                dest.writeInt(mServiceCompanyId);
                dest.writeString(mName);
                dest.writeString(mLogo);
                dest.writeString(mCardName);
                dest.writeInt(mCardNumber);
                dest.writeInt(mCardType);
                dest.writeInt(mUnreadPush);
            }

            public static final Parcelable.Creator<CompanyData> CREATOR = new Parcelable.Creator<CompanyData>() {

                @Override
                public CompanyData createFromParcel(Parcel in) {
                    return new CompanyData(in);
                }

                @Override
                public CompanyData[] newArray(int size) {
                    return new CompanyData[size];
                }
            };
        }
    }
}
