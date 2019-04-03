package wssj.co.jp.olioa.screens.chat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.chat.ChatMessage;
import wssj.co.jp.olioa.utils.DateConvert;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 12/9/2017.
 */

public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    private static final int TYPE_USER = 0;

    private static final int TYPE_STORE = 1;

    private static final int TYPE_COUNT = TYPE_STORE + 1;

    private LayoutInflater mInflate;

    private String mUrlImageStore;

    private IClickImageStore clickImageStore;

    public ChatAdapter(@NonNull Context context, @NonNull List<ChatMessage> objects) {
        super(context, 0, objects);
        mInflate = LayoutInflater.from(context);
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chat = getItem(position);
        if (chat != null) {
            return chat.isUser() ? TYPE_USER : TYPE_STORE;
        }
        return TYPE_USER;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMessage chat = getItem(position);
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
        holderUser.bind(chat, position);
        return convertView;
    }

    public class ChatDetailHolderUser {

        private TextView mDate;

        private TextView mTime;

        private TextView mContent;

        private CircleImageView mImageStore;

        private LinearLayout mLayoutDate;

        public ChatDetailHolderUser(View view) {
            mDate = (TextView) view.findViewById(R.id.tvDate);
            mContent = (TextView) view.findViewById(R.id.tvContent);
            mTime = (TextView) view.findViewById(R.id.tvTime);
            mLayoutDate = (LinearLayout) view.findViewById(R.id.layoutDate);
            mImageStore = (CircleImageView) view.findViewById(R.id.imageStore);
        }

        public void bind(final ChatMessage chat, int position) {
            switch (getItemViewType(position)) {
                case TYPE_STORE:
                    Utils.fillImage(getContext(), mUrlImageStore, mImageStore, R.drawable.icon_user);
                    break;
            }
//            if (mImageStore != null && clickImageStore != null) {
//                mImageStore.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        clickImageStore.onClick(chat.getToId());
//                    }
//                });
//            }
            if (TextUtils.isEmpty(chat.getDate())) {
                mLayoutDate.setVisibility(View.GONE);
            } else {
                mLayoutDate.setVisibility(View.VISIBLE);
                mDate.setText(chat.getDate());
            }
            mContent.setText(StringEscapeUtils.unescapeJava(chat.getContent()));
            String time = DateConvert.formatToString(DateConvert.TIME_FORMAT, chat.getCreated());
            mTime.setText(time);
        }
    }

    public void setImageStore(String imageStore) {
        this.mUrlImageStore = imageStore;
    }

    public interface IClickImageStore {

        void onClick(int managerId);
    }

    public void setOnClickImageStore(IClickImageStore clickImageStore) {
        this.clickImageStore = clickImageStore;
    }

}
