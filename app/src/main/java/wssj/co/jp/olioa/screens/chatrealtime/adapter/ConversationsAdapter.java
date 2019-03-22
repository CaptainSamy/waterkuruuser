package wssj.co.jp.olioa.screens.chatrealtime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.firebaseChat.MessagesFirebase;

/**
 * Created by thang on 1/31/2018.
 */

public class ConversationsAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<MessagesFirebase> arrayMessagesFirebase;
    public ConversationsAdapter(Context context,int layout,List<MessagesFirebase> messagesFirebaseList){
        myContext=context;
        myLayout=layout;
        arrayMessagesFirebase=messagesFirebaseList;
    }
    @Override
    public int getCount() {
        if (arrayMessagesFirebase==null){
            return 0;
        }
        return arrayMessagesFirebase.size();
    }

    @Override
    public Object getItem(int position) {
        if (position<arrayMessagesFirebase.size()){
            return arrayMessagesFirebase.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(myLayout,null);
        LinearLayout layoutDate=(LinearLayout)convertView.findViewById(R.id.layoutDate);

        MessagesFirebase messagesFirebase= (MessagesFirebase) getItem(position);
        TextView  tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        if (messagesFirebase!=null){
            if (position==0){
                tvDate.setText(messagesFirebase.getCreated());
                tvDate.setVisibility(View.VISIBLE);
            }else {
                if (messagesFirebase.getCreated().equals(arrayMessagesFirebase.get(position-1).getCreated())){
                    layoutDate.setVisibility(View.GONE);
                    tvDate.setVisibility(View.GONE);

                }else {
                    tvDate.setText(messagesFirebase.getCreated());
                    tvDate.setVisibility(View.VISIBLE);
                }
            }
            TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            tvTime.setText(messagesFirebase.getTime());
            TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            tvContent.setText(messagesFirebase.getContent());
        }
//        TextView  tvDate = (TextView) convertView.findViewById(R.id.tvDate);
//        tvDate.setText(arrayMessagesFirebase.get(position).getCreated());
//        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
//        tvTime.setText(arrayMessagesFirebase.get(position).getTime());
//        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
//        tvContent.setText(arrayMessagesFirebase.get(position).getContent());
        return convertView;
    }
}
