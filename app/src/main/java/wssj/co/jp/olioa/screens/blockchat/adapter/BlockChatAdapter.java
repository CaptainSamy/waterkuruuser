package wssj.co.jp.olioa.screens.blockchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.baseapi.IOlioaCallback;
import wssj.co.jp.olioa.model.entities.BlockChatStore;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Utils;

public class BlockChatAdapter extends BaseAdapter {
    private List<BlockChatStore> mListStore;
    private IOlioaCallback<BlockChatStore> callback;
    private LayoutInflater inflater;
    private Context mContext;
    public BlockChatAdapter(Context context, List<BlockChatStore> mListStore, IOlioaCallback<BlockChatStore> callback) {
        if (mListStore == null) {
            mListStore = new ArrayList<>();
        }
        this.mContext = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListStore = mListStore;
        this.callback = callback;
    }

    public void updateList(List<BlockChatStore> list) {
        if (list == null) return;
        mListStore.clear();
        mListStore.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mListStore.size();
    }

    @Override
    public BlockChatStore getItem(int position) {
        return mListStore.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_block_chat, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bindData(getItem(position));
        return convertView;
    }

    class ViewHolder {
        private CircleImageView imageStore;
        private TextView mStoreName;
        private Switch mSwitchView;

        ViewHolder(View view) {
            imageStore = view.findViewById(R.id.logoStore);
            mStoreName = view.findViewById(R.id.storeName);
            mSwitchView = view.findViewById(R.id.switchView);
        }

        void bindData(final BlockChatStore store) {
            Utils.fillImage(mContext,store.getLogoStore(),imageStore,R.drawable.icon_user);
            mStoreName.setText(store.getStoreName());
            mSwitchView.setChecked(store.getStatus() == 1);
            mSwitchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int status = isChecked ? 1 : 2;
                    store.setStatus(status);
                    callback.onAction(Constants.SUCCESSFULLY, store);
                }
            });

        }
    }
}
