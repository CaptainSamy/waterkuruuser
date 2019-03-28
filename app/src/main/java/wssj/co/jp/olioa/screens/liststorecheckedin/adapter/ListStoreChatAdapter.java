package wssj.co.jp.olioa.screens.liststorecheckedin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.entities.StoreInfo;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.DateConvert;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 13/6/2017.
 */

public class ListStoreChatAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater mLayoutInflate;

    private List<StoreInfo> mListStore;

    public ListStoreChatAdapter(Context context, List<StoreInfo> listStoreList) {
        mContext = context;
        mListStore = listStoreList;

    }

    public void setListStore(List<StoreInfo> mListStore) {
        this.mListStore.clear();
        this.mListStore.addAll(mListStore);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mListStore != null && mListStore.size() > 0) {
            return mListStore.size();
        }
        return 0;
    }

    @Override
    public StoreInfo getItem(int position) {
        if (mListStore != null && mListStore.size() > 0) {
            return mListStore.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StoreCheckedHolder holder;
        if (convertView == null) {
            mLayoutInflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mLayoutInflate.inflate(R.layout.item_list_store_chat, parent, false);
            holder = new StoreCheckedHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (StoreCheckedHolder) convertView.getTag();
        }
        holder.bindData(getItem(position));
        return convertView;
    }

    public class StoreCheckedHolder {

        private TextView mStoreName;

        private TextView mLastMessage;

        private TextView mLastTimeMessage;

        private ImageView mLogoStore;

        public StoreCheckedHolder(View view) {
            mStoreName = view.findViewById(R.id.storeName);
            mLastMessage = view.findViewById(R.id.lastMessage);
            mLastTimeMessage = view.findViewById(R.id.lastTimeMessage);
            mLogoStore = view.findViewById(R.id.logoStore);
        }

        public void bindData(StoreInfo store) {
            if (store != null) {
                mStoreName.setText(store.getName());
                if (!TextUtils.isEmpty(store.getLastMessage())) {
                    mLastMessage.setText(store.getLastMessage());
                } else {
                    mLastMessage.setText(Constants.EMPTY_STRING);
                }
                if (!TextUtils.isEmpty(store.getLastTimeMessage())) {
                    String lastTime = DateConvert.convertDateToDate(store.getLastTimeMessage(), DateConvert.DATE_NANO, DateConvert.DATE_FULL_FORMAT);
                    mLastTimeMessage.setText(lastTime);
                } else {
                    mLastTimeMessage.setText(Constants.EMPTY_STRING);
                }
                Utils.fillImage(mContext, store.getLogo(), mLogoStore, R.drawable.logo_app);
            }
        }
    }
}
