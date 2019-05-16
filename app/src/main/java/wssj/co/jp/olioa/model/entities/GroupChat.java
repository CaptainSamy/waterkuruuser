package wssj.co.jp.olioa.model.entities;

import com.google.gson.annotations.SerializedName;

public class GroupChat {
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
}
