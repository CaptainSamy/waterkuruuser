package jp.co.wssj.iungo.model.memo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 9/6/2017.
 */

public class ListServiceResponse extends ResponseData<ListServiceResponse.ServiceData> {

    public class ServiceData implements GsonSerializable {

        @SerializedName("service")
        private List<Service> mListService;

        public List<Service> getListService() {
            return mListService;
        }
    }

    public class Service implements GsonSerializable {

        @SerializedName("id")
        private int mId;

        @SerializedName("name")
        private String mServiceName;

        public int getId() {
            return mId;
        }

        public String getServiceName() {
            return mServiceName;
        }
    }
}