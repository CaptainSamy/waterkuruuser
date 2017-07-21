package jp.co.wssj.iungo.screens.liststorecheckedin.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.stamp.ListStoreCheckedResponse;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 13/6/2017.
 */

public class ListStoreCheckedInAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater mLayoutInflate;

    private List<ListStoreCheckedResponse.StoreCheckedIn> mListStore;

    public ListStoreCheckedInAdapter(Context context, List<ListStoreCheckedResponse.StoreCheckedIn> listStoreList) {
        mContext = context;
        mListStore = listStoreList;

    }

    @Override
    public int getCount() {
        if (mListStore != null && mListStore.size() > 0) {
            return mListStore.size();
        }
        return 0;
    }

    @Override
    public ListStoreCheckedResponse.StoreCheckedIn getItem(int position) {
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

        private TextView mAddressStore;

        private TextView mTimeWaiting, mNumberPeopleWaiting, mNumberUsedStore, mTimeLastUsedStore;

        private TextView mButtonMap;

        public StoreCheckedHolder(View view) {
            mStoreName = (TextView) view.findViewById(R.id.tvCardName);
            mAddressStore = (TextView) view.findViewById(R.id.tvAddress);
            mTimeWaiting = (TextView) view.findViewById(R.id.tvTimeWaiting);
            mNumberPeopleWaiting = (TextView) view.findViewById(R.id.tvPeopleWaiting);
            mNumberUsedStore = (TextView) view.findViewById(R.id.tvNumberUsedService);
            mTimeLastUsedStore = (TextView) view.findViewById(R.id.tvTimeLastUsedService);
            mButtonMap = (TextView) view.findViewById(R.id.tvMap);
        }

        public void bindData(final ListStoreCheckedResponse.StoreCheckedIn store) {
            if (store != null) {
                mStoreName.setText(store.getStoreName());
                mAddressStore.setText(store.getStoreAddress());
                long timeWaiting = System.currentTimeMillis() - store.getExpireTime();
                timeWaiting = timeWaiting < 0 ? 0 : timeWaiting;

                String hour = String.valueOf(TimeUnit.MILLISECONDS.toHours(timeWaiting));
                String minute = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(timeWaiting) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeWaiting)));
                mTimeWaiting.setText(mContext.getString(R.string.content_time_waiting, hour, minute));

                mNumberPeopleWaiting.setText(mContext.getString(R.string.content_people_waiting, String.valueOf(store.getPeopleWaiting())));
                mNumberUsedStore.setText(mContext.getString(R.string.content_number_user_store, String.valueOf(store.getNumberCheckedIn())));

                long timeLastUser = store.getTimeLastCheckedIn();
                timeLastUser = timeLastUser < 0 ? 0 : timeLastUser;
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GTM"));
                calendar.setTimeInMillis(timeLastUser);
                String year = String.valueOf(calendar.get(Calendar.YEAR));
                String month = Utils.get2NumbericString(calendar.get(Calendar.MONTH) + 1);
                String day = Utils.get2NumbericString(calendar.get(Calendar.DAY_OF_MONTH));
                mTimeLastUsedStore.setText(mContext.getString(R.string.content_time_last_used, year, month, day));
            }
            mButtonMap.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String targetPackage = mContext.getString(R.string.app_google);
                    if (isPackageExisted(targetPackage)) {
//                        String position = mContext.getString(R.string.position_map, String.valueOf(store.getLatitude()), String.valueOf(store.getLongitude()));
                        String position = mContext.getString(R.string.position_map_address, String.valueOf(store.getLatitude()), String.valueOf(store.getLongitude()), store.getStoreAddress());//Lê Đức Thọ, Mỹ Đình 2, Mai Dịch, Từ Liêm, Hà Nội, Việt Nam
                        Uri googleMap = Uri.parse(position);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, googleMap);
                        mapIntent.setPackage(targetPackage);
                        mContext.startActivity(mapIntent);
                    }
                }
            });
        }

        public boolean isPackageExisted(String targetPackage) {
            PackageManager pm = mContext.getPackageManager();
            try {
                PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                return false;
            }
            return true;
        }
    }
}
