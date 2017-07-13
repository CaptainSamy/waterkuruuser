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

        @SerializedName("memo_config")
        private List<UserMemoConfig> mListMemoConfig;

        @SerializedName("memo_value")
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

                /*
                * Edit Text
                * */
                @SerializedName("length")
                @Expose
                private int length;

                @SerializedName("placeholder")
                @Expose
                private String placeholder;

                /*
                *
                * CheckBox
                * */
                @SerializedName("combo_list")
                @Expose
                private List<String> listComboBox;

                /*
                * Image
                * */
                @SerializedName("number_of_image")
                private int numberImages;

                /*
                * Level
                * */
                @SerializedName("level")
                private int level;

                public int getLength() {
                    return length;
                }

                public String getPlaceholder() {
                    return placeholder;
                }

                public List<String> getListComboBox() {
                    return listComboBox;
                }

                public int getNumberImages() {
                    return numberImages;
                }

                public int getLevel() {
                    return level;
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

            public void setValue(Value value) {
                this.value = value;
            }

            public class Value {

                /*
                * Edit Text
                * */
                @SerializedName("value")
                @Expose
                private String value;

                /*
                *Switch
                * */
                @SerializedName("status")
                @Expose
                private boolean status;

                /*
                * Combo box
                * */
                @SerializedName("selected_position")
                private int selectedItem;

                /*
                * Images
                * */
                @SerializedName("images")
                List<Image> listImage;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public boolean getStatus() {
                    return status;
                }

                public void setStatus(boolean status) {
                    this.status = status;
                }

                public int getSelectedItem() {
                    return selectedItem;
                }

                public void setSelectedItem(int selectedItem) {
                    this.selectedItem = selectedItem;
                }

                public List<Image> getListImage() {
                    return listImage;
                }

                public void setListImage(List<Image> listImage) {
                    this.listImage = listImage;
                }

                public class Image {

                    @SerializedName("id")
                    private String imageId;

                    @SerializedName("value")
                    private String urlImage;

                    public String getImageId() {
                        return imageId;
                    }

                    public String getUrlImage() {
                        return urlImage;
                    }

                    public void setUrlImage(String urlImage) {
                        this.urlImage = urlImage;
                    }
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
