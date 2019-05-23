package wssj.co.jp.obis.model.stamp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import wssj.co.jp.obis.model.GsonSerializable;
import wssj.co.jp.obis.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 13/6/2017.
 */

public class ListStoreCheckedResponse extends ResponseData<ListStoreCheckedResponse.ListStoreData> {

    public class ListStoreData implements GsonSerializable {

        @SerializedName("store_list")
        private List<StoreCheckedIn> mListStore;

        public List<StoreCheckedIn> getListStore() {
            return mListStore;
        }
    }

    public static class StoreCheckedIn {

        @SerializedName("name")
        private String mStoreName;

        @SerializedName("address")
        private String mStoreAddress;

        @SerializedName("waiting_time")
        private long mWaitingTime;

        @SerializedName("number_of_waiting_people")
        private int mPeopleWaiting;

        @SerializedName("check_in_amount")
        private int mNumberCheckedIn;

        @SerializedName("last_check_in_time")
        private long mTimeLastCheckedIn;

        @SerializedName("latitude")
        private double mLatitude;

        @SerializedName("longitude")
        private double mLongitude;

        @SerializedName("distance")
        private String mDistance;

        public String getStoreName() {
            return mStoreName;
        }

        public String getStoreAddress() {
            return mStoreAddress;
        }

        public long getWaitingTime() {
            return mWaitingTime;
        }

        public int getPeopleWaiting() {
            return mPeopleWaiting;
        }

        public int getNumberCheckedIn() {
            return mNumberCheckedIn;
        }

        public long getTimeLastCheckedIn() {
            return mTimeLastCheckedIn;
        }

        public double getLatitude() {
            return mLatitude;
        }

        public double getLongitude() {
            return mLongitude;
        }

        public String getDistance() {
            return mDistance;
        }
    }

}
