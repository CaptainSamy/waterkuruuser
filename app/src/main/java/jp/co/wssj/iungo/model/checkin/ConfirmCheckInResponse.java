package jp.co.wssj.iungo.model.checkin;

import com.google.gson.annotations.SerializedName;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 17/5/2017.
 */

public class ConfirmCheckInResponse extends ResponseData<ConfirmCheckInResponse.SessionData> {

    public class SessionData implements GsonSerializable {

        @SerializedName("session_id")
        private int mSessionId;

        @SerializedName("status_check_in")
        private String mStatus;

        @SerializedName("store_id")
        private int mStoreId;

        @SerializedName("service_company_id")
        private int mServiceCompanyId;

        @SerializedName("service_id")
        private int mServiceId;

        @SerializedName("service_name")
        private String mServiceName;

        @SerializedName("company_id")
        private int mCompanyId;

        @SerializedName("number_of_waiting_people")
        private int mNumberPeople;

        @SerializedName("waiting_time")
        private long mTimeWaiting;

        @SerializedName("numerical_session")
        private int mNumberSession;

        @SerializedName("card_type")
        private int mCardType;

        public int getSessionId() {
            return mSessionId;
        }

        public String getStatus() {
            return mStatus;
        }

        public int getStoreId() {
            return mStoreId;
        }

        public int getServiceCompanyId() {
            return mServiceCompanyId;
        }

        public int getServiceId() {
            return mServiceId;
        }

        public int getCompanyId() {
            return mCompanyId;
        }

        public int getNumberPeople() {
            return mNumberPeople;
        }

        public long getTimeWaiting() {
            return mTimeWaiting;
        }

        public int getNumberSession() {
            return mNumberSession;
        }

        public String getServiceName() {
            return mServiceName;
        }

        public int getCardType() {
            return mCardType;
        }
    }
}
