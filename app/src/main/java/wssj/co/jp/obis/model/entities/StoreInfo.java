package wssj.co.jp.obis.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DaiKySy on 3/25/19.
 */

public class StoreInfo implements Parcelable {

    private int id;

    private String name;

    private String logo;

    private String checkInCode;

    private String lastMessage;

    private String lastTimeMessage;

    public StoreInfo() {

    }

    protected StoreInfo(Parcel in) {
        id = in.readInt();
        name = in.readString();
        logo = in.readString();
        checkInCode = in.readString();
        lastMessage = in.readString();
        lastTimeMessage = in.readString();
    }

    public static final Creator<StoreInfo> CREATOR = new Creator<StoreInfo>() {

        @Override
        public StoreInfo createFromParcel(Parcel in) {
            return new StoreInfo(in);
        }

        @Override
        public StoreInfo[] newArray(int size) {
            return new StoreInfo[size];
        }
    };

    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCheckInCode() {
        return checkInCode;
    }

    public void setCheckInCode(String checkInCode) {
        this.checkInCode = checkInCode;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getLastTimeMessage() {
        return lastTimeMessage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setLastTimeMessage(String lastTimeMessage) {
        this.lastTimeMessage = lastTimeMessage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(logo);
        dest.writeString(checkInCode);
        dest.writeString(lastMessage);
        dest.writeString(lastTimeMessage);
    }
}
