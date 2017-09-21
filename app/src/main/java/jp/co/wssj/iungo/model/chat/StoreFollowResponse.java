package jp.co.wssj.iungo.model.chat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 12/9/2017.
 */

public class StoreFollowResponse extends ResponseData<StoreFollowResponse.StoreChatData> {

    public class StoreChatData implements GsonSerializable {

        @SerializedName("stores_follow")
        private List<StoreFollow> listStoreFollow;

        public class StoreFollow implements GsonSerializable {

            @SerializedName("id")
            private int id;

            @SerializedName("name")
            private String storeName;

            @SerializedName("logo")
            private String imageStore;

            @SerializedName("service_company_id")
            private String serviceCompanyId;

            public int getId() {
                return id;
            }

            public String getStoreName() {
                return storeName;
            }

            public String getImageStore() {
                return imageStore;
            }

            public String getServiceCompanyId() {
                return serviceCompanyId;
            }
        }

        public List<StoreFollow> getListStoreFollow() {
            return listStoreFollow;
        }
    }

}
