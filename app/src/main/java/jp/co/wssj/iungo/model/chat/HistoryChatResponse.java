package jp.co.wssj.iungo.model.chat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 21/9/2017.
 */

public class HistoryChatResponse extends ResponseData<HistoryChatResponse.HistoryChatData> {

    public static class HistoryChatData implements GsonSerializable {

        @SerializedName("list_message")
        private List<ChatData> listChat;

        public static class ChatData implements GsonSerializable {

            @SerializedName("content")
            private String content;

            @SerializedName("created")
            private long timeCreate;

            @SerializedName("is_user_send")
            private int isUser;

            @SerializedName("management_user_name")
            private String managerName;

            @SerializedName("management_user_id")
            private int managerId;

            @SerializedName("user_name")
            private String userName;

            @SerializedName("img_store")
            private String mImageStore;

            @SerializedName("id")
            private int id;

            private String date;

            public int getId() {
                return id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public long getTimeCreate() {
                return timeCreate;
            }

            public void setTimeCreate(long timeCreate) {
                this.timeCreate = timeCreate;
            }

            public boolean isUser() {
                return isUser == 0;
            }

            public void setIsUser(int isUser) {
                this.isUser = isUser;
            }

            public String getManagerName() {
                return managerName;
            }

            public String getUserName() {
                return userName;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getImageStore() {
                return mImageStore;
            }

            public int getManagerId() {
                return managerId;
            }
        }

        public List<ChatData> getListChat() {
            return listChat;
        }
    }
}
