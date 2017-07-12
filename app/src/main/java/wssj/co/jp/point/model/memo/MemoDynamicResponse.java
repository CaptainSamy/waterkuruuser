package wssj.co.jp.point.model.memo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import wssj.co.jp.point.model.GsonSerializable;
import wssj.co.jp.point.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 10/7/2017.
 */

public class MemoDynamicResponse extends ResponseData<MemoDynamicResponse.UserMemoData> {

    public class UserMemoData implements GsonSerializable {

        @SerializedName("user_memo_config")
        private List<UserMemoConfig> mListMemoConfig;

        @SerializedName("user_memo_value")
        private List<UserMemoValue> mListMemoValue;

        public class UserMemoConfig {

            @SerializedName("config")
            @Expose
            private Config config;

            @SerializedName("id")
            @Expose
            private String id;

            @SerializedName("title")
            @Expose
            private String title;

            @SerializedName("type")
            @Expose
            private int type;

            public Config getConfig() {
                return config;
            }

            public String getId() {
                return id;
            }

            public String getTitle() {
                return title;
            }

            public int getType() {
                return type;
            }

            public class Config {

                @SerializedName("length")
                @Expose
                private int length;

                @SerializedName("placeholder")
                @Expose
                private String placeholder;

                public int getLength() {
                    return length;
                }

                public String getPlaceholder() {
                    return placeholder;
                }
            }

        }

        public class UserMemoValue {

            @SerializedName("id")
            @Expose
            private String id;

            @SerializedName("type")
            @Expose
            private int type;

            @SerializedName("value")
            @Expose
            private Value value;

            public String getId() {
                return id;
            }

            public int getType() {
                return type;
            }

            public Value getValue() {
                return value;
            }

            public class Value {

                @SerializedName("value")
                @Expose
                private String value;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

            }
        }

        public List<UserMemoConfig> getListMemoConfig() {
            return mListMemoConfig;
        }

        public List<UserMemoValue> getListMemoValue() {
            return mListMemoValue;
        }
    }
}
