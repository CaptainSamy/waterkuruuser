package jp.co.wssj.iungo.model.chat;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 12/9/2017.
 */

public class ChatResponse {

    private String mImage;

    private String mStoreName;

    private long mTime;

    List<Chat> mListChat;

    public ChatResponse(String storeName, long time) {
        mStoreName = storeName;
        mTime = time;
        mImage = Constants.EMPTY_STRING;
        mListChat = new ArrayList<>();
        Chat chat = new Chat("Cắt tóc", "How are you? How are you? How are you? How are you? How are you? How are you? How are you?", false, System.currentTimeMillis() - 1000);
        mListChat.add(chat);
        chat = new Chat("Cắt tóc", "I fine!", true, System.currentTimeMillis() - 60000);
        mListChat.add(chat);
        chat = new Chat("Cắt tóc", "And you?", true, System.currentTimeMillis() - 120000);
        mListChat.add(chat);
        chat = new Chat("Cắt tóc", "Me too", false, System.currentTimeMillis() - 180000);
        mListChat.add(chat);
        chat = new Chat("Cắt tóc", "What are you doing?", true, System.currentTimeMillis() - 240000);
        mListChat.add(chat);
        chat = new Chat("Cắt tóc", "I'm playing game Age of Empty", false, System.currentTimeMillis() - 300000);
        mListChat.add(chat);
    }

    public static class Chat {

        public Chat(String mStoreName, String mContent, boolean isFromUser, long time) {
            this.mStoreName = mStoreName;
            this.mContent = mContent;
            this.isFromUser = isFromUser;
            this.time = time;
        }

        private String mStoreName;

        private String mContent;

        private boolean isFromUser;

        private long time;

        public String getStoreName() {
            return mStoreName;
        }

        public String getContent() {
            return mContent;
        }

        public boolean isFromUser() {
            return isFromUser;
        }

        public long getTime() {
            return time;
        }
    }

    public List<Chat> getListChat() {
        return mListChat;
    }

    public String getStoreName() {
        return mStoreName;
    }

    public long getTime() {
        return mTime;
    }
}
