package wssj.co.jp.olioa.screens.chat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.chat.StoreFollowResponse;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 12/9/2017.
 */

public class StoreFollowAdapter extends ArrayAdapter<StoreFollowResponse.StoreChatData.StoreFollow> {

    LayoutInflater mInflate;

    public StoreFollowAdapter(@NonNull Context context, @NonNull List<StoreFollowResponse.StoreChatData.StoreFollow> objects) {
        super(context, 0, objects);
        mInflate = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatHolder chatHolder;
        if (convertView == null) {
            convertView = mInflate.inflate(R.layout.item_store_follow, parent, false);
            chatHolder = new ChatHolder(convertView);
            convertView.setTag(chatHolder);
        } else {
            chatHolder = (ChatHolder) convertView.getTag();
        }
        chatHolder.bindData(getContext(), getItem(position));
        return convertView;
    }

    public class ChatHolder {

        private CircleImageView mImageStore;

        private TextView mStoreName, mTime;

        private TextView mLastMessage;

        public ChatHolder(View view) {
            mImageStore = (CircleImageView) view.findViewById(R.id.ivStore);
            mStoreName = (TextView) view.findViewById(R.id.tvStoreName);
            mLastMessage = (TextView) view.findViewById(R.id.tvLastMessage);
            mTime = (TextView) view.findViewById(R.id.tvTime);
        }

        public void bindData(Context context, StoreFollowResponse.StoreChatData.StoreFollow storeFollow) {
            Utils.fillImage(context, storeFollow.getImageStore(), mImageStore,R.drawable.icon_user);
            mStoreName.setText(storeFollow.getStoreName());
            mLastMessage.setText(StringEscapeUtils.unescapeJava(storeFollow.getLastMessage()));
            if (storeFollow.getLastTimeMessage() > 0) {
                mTime.setText(Utils.formatDate(storeFollow.getLastTimeMessage(), "MM/dd"));
                mTime.setVisibility(View.VISIBLE);
            } else {
                mTime.setVisibility(View.GONE);
            }
        }
    }

}
