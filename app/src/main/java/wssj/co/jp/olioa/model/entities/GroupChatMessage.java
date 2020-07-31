package wssj.co.jp.olioa.model.entities;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import wssj.co.jp.olioa.model.chat.ChatMessage;

public class GroupChatMessage {

    //    public static final String TYPE_TEXT = "text";
    public static final String TYPE_IMAGE = "image";


    public void mapping(ChatMessage chatMessage) {
        id = chatMessage.getId();
        content = chatMessage.getContent();
        contentType = chatMessage.getContentType();
        isFromMe = true;
        created = chatMessage.getCreated();
    }

    @SerializedName("id")
    private long id;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("name")
    private String name;

    @SerializedName("content")
    private String content;

    @SerializedName("contentType")
    private String contentType;

    @SerializedName("created")
    private long created;

    @SerializedName("isFromMe")
    private boolean isFromMe;


    private String date;

    private ContentChatImage contentChatImage;

    private ContentImageMap contentImageMap;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isFromMe() {
        return isFromMe;
    }

    public void setFromMe(boolean fromMe) {
        isFromMe = fromMe;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ContentChatImage getContentChatImage() {
        if (contentChatImage == null) {
            Gson gson = new Gson();
            contentChatImage = gson.fromJson(content, ContentChatImage.class);
        }
        return contentChatImage;
    }

    public ContentImageMap getContentImageMap(){
        if (contentImageMap == null) {
            Gson gson = new Gson();
            contentImageMap = gson.fromJson(content, ContentImageMap.class);
        }
        return contentImageMap;
    }

}
