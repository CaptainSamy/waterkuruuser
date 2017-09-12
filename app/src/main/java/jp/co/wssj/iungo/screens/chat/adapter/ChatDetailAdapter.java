package jp.co.wssj.iungo.screens.chat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconTextView;
import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.chat.ChatResponse;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 12/9/2017.
 */

public class ChatDetailAdapter extends ArrayAdapter<ChatResponse.Chat> {

    private static final int TYPE_USER = 0;

    private static final int TYPE_STORE = 1;

    private static final int TYPE_COUNT = TYPE_STORE + 1;

    private LayoutInflater mInflate;

    public ChatDetailAdapter(@NonNull Context context, @NonNull List<ChatResponse.Chat> objects) {
        super(context, 0, objects);
        mInflate = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        ChatResponse.Chat chat = getItem(position);
        if (chat != null) {
            return chat.isFromUser() ? TYPE_USER : TYPE_STORE;
        }
        return TYPE_USER;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatResponse.Chat chat = getItem(position);
        ChatDetailHolderUser holderUser;
        int layoutResource;
        switch (getItemViewType(position)) {
            case TYPE_USER:
                layoutResource = R.layout.item_chat_user;
                break;
            case TYPE_STORE:
                layoutResource = R.layout.item_chat_store;
                break;
            default:
                layoutResource = R.layout.item_chat_user;
                break;
        }
        if (convertView == null) {
            convertView = mInflate.inflate(layoutResource, parent, false);
            holderUser = new ChatDetailHolderUser(convertView);
            convertView.setTag(holderUser);
        } else {
            holderUser = (ChatDetailHolderUser) convertView.getTag();
        }
        holderUser.bind(chat);
        return convertView;
    }

    public class ChatDetailHolderUser {

        private TextView mTime;

        private EmojiconTextView mContent;

        public ChatDetailHolderUser(View view) {
            mContent = (EmojiconTextView) view.findViewById(R.id.tvContent);
            mTime = (TextView) view.findViewById(R.id.tvTime);
        }

        public void bind(ChatResponse.Chat chat) {
            mContent.setText(chat.getContent());
            mTime.setText(Utils.formatDate(chat.getTime(), "HH:mm"));
        }
    }
}
