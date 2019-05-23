package wssj.co.jp.obis.firebaseChat;

/**
 * Created by thang on 1/31/2018.
 */

public class MessagesFirebase {
    private String content;
    private String created;
    private boolean deleted;
    private int from;
    private int type;
    private boolean is_store;
    private String time;


    public MessagesFirebase() {
    }

    public MessagesFirebase(String content) {
        this.content = content;
    }

    public MessagesFirebase(String content, String created) {
        this.content = content;
        this.created = created;
    }

    public MessagesFirebase(String content, String created, String time) {
        this.content = content;
        this.created = created;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean is_store() {
        return is_store;
    }

    public void setIs_store(boolean is_store) {
        this.is_store = is_store;
    }
}
