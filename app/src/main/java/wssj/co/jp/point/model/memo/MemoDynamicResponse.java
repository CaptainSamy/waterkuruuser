package wssj.co.jp.point.model.memo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import wssj.co.jp.point.model.GsonSerializable;
import wssj.co.jp.point.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 10/7/2017.
 */

public class MemoDynamicResponse extends ResponseData<MemoDynamicResponse.ServiceListData> {

    public class ServiceListData implements GsonSerializable {

        @SerializedName("service_list")
        private List<ServiceList> mListService;

        public List<ServiceList> getListService() {
            return mListService;
        }

        public class ServiceList {

            @SerializedName("service_id")
            @Expose
            private int serviceId;

            @SerializedName("service_name")
            @Expose
            private String serviceName;

            @SerializedName("service_memo_config")
            @Expose
            private List<ServiceMemoConfig> serviceMemoConfig = null;

            public class ServiceMemoConfig {

                @SerializedName("id")
                @Expose
                private String mId;

                @SerializedName("type")
                @Expose
                private String mType;

                @SerializedName("name")
                @Expose
                private String mName;

                @SerializedName("config")
                @Expose
                private Config mConfig;

                public String getType() {
                    return mType;
                }

                public String getId() {
                    return mId;
                }

                public String getName() {
                    return mName;
                }

                public Config getConfig() {
                    return mConfig;
                }

                public class Config {

                    @SerializedName("multiline")
                    @Expose
                    private int mMultiline;

                    @SerializedName("max_lengt")
                    @Expose
                    private int mMaxLenght;

                    @SerializedName("numb_image")
                    @Expose
                    private int mNumbImage;

                    @SerializedName("data")
                    @Expose
                    private List<String> mListOption = null;

                    public int getMultiline() {
                        return mMultiline;
                    }

                    public int getMaxLengt() {
                        return mMaxLenght;
                    }

                    public int getNumbImage() {
                        return mNumbImage;
                    }

                    public List<String> getData() {
                        return mListOption;
                    }


                }
            }

            public int getServiceId() {
                return serviceId;
            }

            public String getServiceName() {
                return serviceName;
            }

            public List<ServiceMemoConfig> getServiceMemoConfig() {
                return serviceMemoConfig;
            }
        }
    }
}
