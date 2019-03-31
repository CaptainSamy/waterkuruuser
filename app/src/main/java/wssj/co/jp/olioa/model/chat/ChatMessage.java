package wssj.co.jp.olioa.model.chat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DaiKySy on 3/28/2019.
 */
public class ChatMessage {

    @SerializedName("id")
    private long id;

    @SerializedName("from_id")
    private int fromId;

    @SerializedName("to_id")
    private int toId;

    @SerializedName("content")
    private String content;

    @SerializedName("created")
    private long created;

    @SerializedName("isUser")
    private boolean isUser;

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }
}
