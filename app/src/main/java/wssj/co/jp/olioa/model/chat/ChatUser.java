package wssj.co.jp.olioa.model.chat;

/**
 * Created by DaiKySy on 3/28/2019.
 */
public class ChatUser {

    public ChatUser(int userID, int storeID, int type) {
        this.userID = userID;
        this.storeID = storeID;
        this.type = 1;
    }

    private int userID;

    private int storeID;

    private int type;

    public int getUserID() {
        return userID;
    }

    public int getStoreID() {
        return storeID;
    }

    public int getType() {
        return type;
    }
}
