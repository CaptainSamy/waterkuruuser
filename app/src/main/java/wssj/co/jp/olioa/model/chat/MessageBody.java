package wssj.co.jp.olioa.model.chat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DaiKySy on 3/28/2019.
 */
public class MessageBody {

    @SerializedName("to_id")
    private int toId;

    @SerializedName("content")
    private String content;

    public MessageBody(int storeId, String content) {
        this.toId = storeId;
        this.content = content;
    }

    public int getToId() {
        return toId;
    }

    public String getContent() {
        return content;
    }
}
