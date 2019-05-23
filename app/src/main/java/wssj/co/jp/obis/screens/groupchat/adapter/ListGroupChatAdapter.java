package wssj.co.jp.obis.screens.groupchat.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.List;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.entities.GroupChat;
import wssj.co.jp.obis.model.preference.SharedPreferencesModel;
import wssj.co.jp.obis.utils.Constants;
import wssj.co.jp.obis.utils.DateConvert;
import wssj.co.jp.obis.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 13/6/2017.
 */

public class ListGroupChatAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater mLayoutInflate;

    private List<GroupChat> mListStore;

    private SharedPreferencesModel sharedPreferencesModel;

    public ListGroupChatAdapter(Context context, List<GroupChat> listStoreList) {
        mContext = context;
        mListStore = listStoreList;
        sharedPreferencesModel = new SharedPreferencesModel(context);

    }

    public void setListStore(List<GroupChat> mListStore) {
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
    public GroupChat getItem(int position) {
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

        private View newMessage;

        private TextView mLastMessage;

        private TextView mLastTimeMessage;

        private ImageView mLogoStore;

        StoreCheckedHolder(View view) {
            mStoreName = view.findViewById(R.id.storeName);
            newMessage = view.findViewById(R.id.newMessage);
            mLastMessage = view.findViewById(R.id.lastMessage);
            mLastTimeMessage = view.findViewById(R.id.lastTimeMessage);
            mLogoStore = view.findViewById(R.id.logoStore);
        }

        void bindData(GroupChat groupChat) {
            if (groupChat != null) {
                mStoreName.setText(groupChat.getName());
                if (!TextUtils.isEmpty(groupChat.getLastMessage())) {
                    mLastMessage.setText(StringEscapeUtils.unescapeJava(groupChat.getLastMessage()));
                } else {
                    mLastMessage.setText(Constants.EMPTY_STRING);
                    newMessage.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(groupChat.getLastTimeMessage())) {
                    long time = DateConvert.convertDateToDate(groupChat.getLastTimeMessage(), DateConvert.DATE_NANO);
                    long lastTimeRead = sharedPreferencesModel.getLastTimeReadChatGroup(groupChat.getId());
                    if (lastTimeRead > 0 && lastTimeRead < time) {
                        mLastMessage.setTypeface(null, Typeface.BOLD);
                        newMessage.setVisibility(View.VISIBLE);
                    } else {
                        mLastMessage.setTypeface(null, Typeface.NORMAL);
                        newMessage.setVisibility(View.GONE);
                    }
                    String lastTime = DateConvert.convertDateToDate(groupChat.getLastTimeMessage(), DateConvert.DATE_NANO, DateConvert.DATE_FULL_FORMAT);
                    mLastTimeMessage.setText(lastTime);
                } else {
                    mLastTimeMessage.setText(Constants.EMPTY_STRING);
                    newMessage.setVisibility(View.GONE);
                }
                Utils.fillImage(mContext, groupChat.getAvatar(), mLogoStore, R.drawable.logo_app);
            }
        }
    }
}
