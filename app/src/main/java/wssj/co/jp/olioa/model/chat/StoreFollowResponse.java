package wssj.co.jp.olioa.model.chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import wssj.co.jp.olioa.model.GsonSerializable;
import wssj.co.jp.olioa.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 12/9/2017.
 */

public class StoreFollowResponse extends ResponseData<StoreFollowResponse.StoreChatData> {

    public static class StoreChatData implements GsonSerializable {

        @SerializedName("stores_follow")
        private List<StoreFollow> listStoreFollow;

        public static class StoreFollow implements GsonSerializable, Parcelable {

            @SerializedName("id")
            private int id;

            @SerializedName("img_store")
            private String imageStore;

            @SerializedName("last_message")
            private String lastMessage;

            @SerializedName("last_message_time")
            private long lastTimeMessage;

            @SerializedName("name")
            private String storeName;

            @SerializedName("service_company_id")
            private String serviceCompanyId;

            public int getId() {
                return id;
            }

            public String getLastMessage() {
                return lastMessage;
            }

            public long getLastTimeMessage() {
                return lastTimeMessage;
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

            protected StoreFollow(Parcel in) {
                id = in.readInt();
                imageStore = in.readString();
                lastMessage = in.readString();
                lastTimeMessage = in.readLong();
                storeName = in.readString();
                serviceCompanyId = in.readString();
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(id);
                dest.writeString(imageStore);
                dest.writeString(lastMessage);
                dest.writeLong(lastTimeMessage);
                dest.writeString(storeName);
                dest.writeString(serviceCompanyId);
            }

            public static final Parcelable.Creator<StoreFollow> CREATOR = new Parcelable.Creator<StoreFollow>() {

                @Override
                public StoreFollow createFromParcel(Parcel in) {
                    return new StoreFollow(in);
                }

                @Override
                public StoreFollow[] newArray(int size) {
                    return new StoreFollow[size];
                }
            };
        }

        public List<StoreFollow> getListStoreFollow() {
            return listStoreFollow;
        }
    }

}
