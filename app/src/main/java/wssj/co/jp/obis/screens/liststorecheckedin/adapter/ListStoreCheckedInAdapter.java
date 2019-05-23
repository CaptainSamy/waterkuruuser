package wssj.co.jp.obis.screens.liststorecheckedin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.entities.StoreInfo;
import wssj.co.jp.obis.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 13/6/2017.
 */

public class ListStoreCheckedInAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater mLayoutInflate;

    private List<StoreInfo> mListStore;

    public ListStoreCheckedInAdapter(Context context, List<StoreInfo> listStoreList) {
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
            convertView = mLayoutInflate.inflate(R.layout.item_list_store_checked_in, parent, false);
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

        private ImageView mLogoStore;

        public StoreCheckedHolder(View view) {
            mStoreName = view.findViewById(R.id.storeName);
            mLogoStore = view.findViewById(R.id.logoStore);
        }

        public void bindData(StoreInfo store) {
            if (store != null) {
                mStoreName.setText(store.getName());
                Utils.fillImage(mContext, store.getLogo(), mLogoStore, R.drawable.logo_app);
            }
        }
    }
}
