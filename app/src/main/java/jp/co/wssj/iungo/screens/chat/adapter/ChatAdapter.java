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

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.chat.ChatResponse;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 12/9/2017.
 */

public class ChatAdapter extends ArrayAdapter<ChatResponse> {

    LayoutInflater mInflate;

    public ChatAdapter(@NonNull Context context, @NonNull List<ChatResponse> objects) {
        super(context, 0, objects);
        mInflate = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatHolder chatHolder;
        if (convertView == null) {
            convertView = mInflate.inflate(R.layout.item_chat, parent, false);
            chatHolder = new ChatHolder(convertView);
            convertView.setTag(chatHolder);
        } else {
            chatHolder = (ChatHolder) convertView.getTag();
        }
        chatHolder.bindData(getItem(position));
        return convertView;
    }

    public class ChatHolder {

        private TextView mStoreName, mContent, mTime;

        public ChatHolder(View view) {
            mStoreName = (TextView) view.findViewById(R.id.tvStoreName);
            mContent = (TextView) view.findViewById(R.id.tvContent);
            mTime = (TextView) view.findViewById(R.id.tvTime);
        }

        public void bindData(ChatResponse response) {
            mStoreName.setText(response.getStoreName());
            mContent.setText(response.getListChat().get(0).getContent());
            mTime.setText(Utils.formatDate(response.getTime(),"MM/dd"));
        }
    }

}
