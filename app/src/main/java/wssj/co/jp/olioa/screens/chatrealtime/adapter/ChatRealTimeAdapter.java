package wssj.co.jp.olioa.screens.chatrealtime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.firebaseChat.StoreFirebase;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by thang on 1/30/2018.
 */

public class ChatRealTimeAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<StoreFirebase> arrayStoreFirebase;

    public ChatRealTimeAdapter(Context context, int layout, List<StoreFirebase> storeFirebaseList) {
        myContext = context;
        myLayout = layout;
        arrayStoreFirebase = storeFirebaseList;
    }


    @Override
    public int getCount() {
        return arrayStoreFirebase.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(myLayout, null);
        CircleImageView ivStore = (CircleImageView) convertView.findViewById(R.id.ivStore);
        TextView tvStoreName = (TextView) convertView.findViewById(R.id.tvStoreName);
        TextView tvLastMessage = (TextView) convertView.findViewById(R.id.tvLastMessage);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);

        Picasso.with(myContext).load(arrayStoreFirebase.get(position).getLogo()).into(ivStore);
        tvStoreName.setText(arrayStoreFirebase.get(position).getName());
        tvLastMessage.setText(arrayStoreFirebase.get(position).getLast_message());
        //tvTime.setText(arrayStoreFirebase.get(position).getLast_update()+"");

        tvTime.setText(Utils.formatDate(arrayStoreFirebase.get(position).getLast_update(), "MM/dd"));

        return convertView;
    }
}
