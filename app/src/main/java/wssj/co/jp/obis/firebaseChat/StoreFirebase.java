package wssj.co.jp.obis.firebaseChat;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by thang on 1/30/2018.
 */
//@IgnoreExtraProperties
public class StoreFirebase {
    private String id_store;
//    @SerializedName("is_read")
    private boolean is_read;
//    @SerializedName("last_message")
    private String last_message;
//    @SerializedName("last_update")
    private long last_update;
//    @SerializedName("logo")
    private String logo;
//    @SerializedName("name")
    private String name;



    public StoreFirebase() {
    }

    public StoreFirebase(DataSnapshot data) {
        this.id_store=data.getKey();
        this.is_read = (boolean)data.child("is_read").getValue();
        this.last_message = data.child("last_message").getValue().toString();
        this.last_update = (long) data.child("last_update").getValue();
        this.logo = data.child("logo").getValue().toString();
        this.name = data.child("name").getValue().toString();
    }

    public StoreFirebase(boolean is_read, String last_message, long last_update, String logo, String name) {
        this.is_read = is_read;
        this.last_message = last_message;
        this.last_update = last_update;
        this.logo = logo;
        this.name = name;
    }

    public boolean is_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public long getLast_update() {
        return last_update;
    }

    public void setLast_update(long last_update) {
        this.last_update = last_update;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_store() {
        return id_store;
    }

    public void setId_store(String id_store) {
        this.id_store = id_store;
    }
}
