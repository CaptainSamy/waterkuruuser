package wssj.co.jp.olioa.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class GroupChat implements Parcelable {
    public static String TAG = "GroupChat";
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("lastMessage")
    private String lastMessage;
    @SerializedName("lastTimeMessage")
    private String lastTimeMessage;
    @SerializedName("lastMessageFromId")
    private String lastMessageFromId;
    @SerializedName("storeId")
    private String storeId;
    @SerializedName("type")
    private String type;

    protected GroupChat(Parcel in) {
        id = in.readInt();
        name = in.readString();
        avatar = in.readString();
        lastMessage = in.readString();
        lastTimeMessage = in.readString();
        lastMessageFromId = in.readString();
        storeId = in.readString();
        type = in.readString();
    }

    public static final Creator<GroupChat> CREATOR = new Creator<GroupChat>() {
        @Override
        public GroupChat createFromParcel(Parcel in) {
            return new GroupChat(in);
        }

        @Override
        public GroupChat[] newArray(int size) {
            return new GroupChat[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastTimeMessage() {
        return lastTimeMessage;
    }

    public void setLastTimeMessage(String lastTimeMessage) {
        this.lastTimeMessage = lastTimeMessage;
    }

    public String getLastMessageFromId() {
        return lastMessageFromId;
    }

    public void setLastMessageFromId(String lastMessageFromId) {
        this.lastMessageFromId = lastMessageFromId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(avatar);
        dest.writeString(lastMessage);
        dest.writeString(lastTimeMessage);
        dest.writeString(lastMessageFromId);
        dest.writeString(storeId);
        dest.writeString(type);
    }
}
