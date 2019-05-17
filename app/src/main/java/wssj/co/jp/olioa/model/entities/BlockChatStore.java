package wssj.co.jp.olioa.model.entities;

import com.google.gson.annotations.SerializedName;

public class BlockChatStore {

    @SerializedName("storeId")
    private int storeId;
    @SerializedName("name")
    private String storeName;
    @SerializedName("logo")
    private String logoStore;
    /*
     * 1 : checkin
     * 2 : checkout
     */
    @SerializedName("status")
    private int status;


    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLogoStore() {
        return logoStore;
    }

    public void setLogoStore(String logoStore) {
        this.logoStore = logoStore;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        if (status == 2) {
            return "本当にブロックしますか？";
        }
        return "";
    }
}
