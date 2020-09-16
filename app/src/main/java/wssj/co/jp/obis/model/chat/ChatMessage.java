package wssj.co.jp.obis.model.chat;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import wssj.co.jp.obis.model.entities.ContentChatImage;
import wssj.co.jp.obis.model.entities.ContentImageMap;

/**
 * Created by DaiKySy on 3/28/2019.
 */
public class ChatMessage {

    //    public static final String TYPE_TEXT = "text";
    public static final String TYPE_IMAGE_MAP = "imagemap";
    public static final String TYPE_IMAGE = "image";


    @SerializedName("id")
    private long id;

    @SerializedName("from_id")
    private int fromId;

    @SerializedName("to_id")
    private int toId;

    @SerializedName("content")
    private String content;

    @SerializedName("content_type")
    private String contentType;

    @SerializedName("created")
    private long created;

    @SerializedName("isUser")
    private boolean isUser;


    private String date;

    private ContentChatImage contentChatImage;

    private ContentImageMap contentImageMap;

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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

    public ContentChatImage getContentChatImage() {
        if (contentChatImage == null) {
            Gson gson = new Gson();
            contentChatImage = gson.fromJson(content, ContentChatImage.class);
        }
        return contentChatImage;
    }

    public ContentImageMap getContentImageMap() {
        if (contentImageMap == null) {
            Gson gson = new Gson();
            contentImageMap = gson.fromJson(content, ContentImageMap.class);
        }
        return contentImageMap;
    }



}
