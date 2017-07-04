package wssj.co.jp.point.model.checkin;

import com.google.gson.annotations.SerializedName;

import wssj.co.jp.point.model.GsonSerializable;
import wssj.co.jp.point.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 18/5/2017.
 */

public class CheckInStatusResponse extends ResponseData<CheckInStatusResponse.CheckInStatusData> {

    public class CheckInStatusData implements GsonSerializable {

        @SerializedName("status_check_in")
        private String mStatus;

        @SerializedName("number_of_waiting_people")
        private int mNumberPeople;

        @SerializedName("expire_time")
        private long mTimeWaiting;

        @SerializedName("numerical_session")
        private int mNumberSession;

        @SerializedName("store_name")
        private String mStoreName;

        @SerializedName("session_id")
        private int mSessionId;

        @SerializedName("service_company_id")
        private int mServiceCompanyId;

        @SerializedName("service_id")
        private int mServiceId;

        public String getStatus() {
            return mStatus;
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

        public String getStoreName() {
            return mStoreName;
        }

        public int getSessionId() {
            return mSessionId;
        }

        public int getServiceCompanyId() {
            return mServiceCompanyId;
        }

        public int getServiceId() {
            return mServiceId;
        }
    }
}
