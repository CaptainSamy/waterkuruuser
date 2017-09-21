package jp.co.wssj.iungo.screens.chat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.chat.StoreFollowResponse;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Utils;

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

        private ImageView mImageStore;

        private TextView mStoreName, mContent, mTime;

        public ChatHolder(View view) {
            mImageStore = (ImageView) view.findViewById(R.id.ivStore);
            mStoreName = (TextView) view.findViewById(R.id.tvStoreName);
            mContent = (TextView) view.findViewById(R.id.tvContent);
            mTime = (TextView) view.findViewById(R.id.tvTime);
        }

        public void bindData(Context context, StoreFollowResponse.StoreChatData.StoreFollow response) {
            Utils.fillImageRound(context, response.getImageStore(), mImageStore);
            mStoreName.setText(response.getStoreName());
            mContent.setText(Constants.EMPTY_STRING);
            mTime.setText(Utils.formatDate(System.currentTimeMillis(), "MM/dd"));

        }
    }

}
