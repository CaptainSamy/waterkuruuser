package jp.co.wssj.iungo.model.chat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import jp.co.wssj.iungo.model.GsonSerializable;
import jp.co.wssj.iungo.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 21/9/2017.
 */

public class HistoryChatResponse extends ResponseData<HistoryChatResponse.HistoryChatData> {

    public class HistoryChatData implements GsonSerializable {

        @SerializedName("list_message")
        private List<ChatData> listChat;

        public class ChatData implements GsonSerializable {

            @SerializedName("content")
            private String content;

            @SerializedName("created")
            private long timeCreate;

            @SerializedName("is_user_send")
            private int isUser;

            @SerializedName("management_user_name")
            private String managerName;

            @SerializedName("user_name")
            private String userName;

            public String getContent() {
                return content;
            }

            public long getTimeCreate() {
                return timeCreate;
            }

            public boolean isUser() {
                return isUser == 0;
            }

            public String getManagerName() {
                return managerName;
            }

            public String getUserName() {
                return userName;
            }
        }

        public List<ChatData> getListChat() {
            return listChat;
        }
    }
}
