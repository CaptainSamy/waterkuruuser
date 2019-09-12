package wssj.co.jp.obis.screens.groupchat.adapter;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.chat.ChatMessage;
import wssj.co.jp.obis.model.entities.ContentChatImage;
import wssj.co.jp.obis.model.entities.GroupChatMessage;
import wssj.co.jp.obis.screens.MainActivity;
import wssj.co.jp.obis.utils.DateConvert;
import wssj.co.jp.obis.utils.Utils;
import wssj.co.jp.obis.screens.ImageDetailActivity;

public class GroupChatAdapter extends ArrayAdapter<GroupChatMessage> {

    private static final int TYPE_USER = 0;

    private static final int TYPE_GROUP = 1;

    private static final int TYPE_GROUP_IMAGE = 2;

    private static final int TYPE_COUNT = 3;

    private LayoutInflater mInflate;

    private int mWidth;

    private MainActivity mainActivity;

    public GroupChatAdapter(@NonNull MainActivity context, @NonNull List<GroupChatMessage> objects) {
        super(context, 0, objects);
        this.mainActivity = context;
        mInflate = LayoutInflater.from(context);
        mWidth = Utils.getWidthDevice(context) / 2;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        GroupChatMessage chat = getItem(position);
        if (chat != null) {
            if (chat.isFromMe()) {
                return TYPE_USER;
            } else {
                if (chat.getContentType() == null) {
                    return TYPE_GROUP;
                }
                switch (chat.getContentType()) {
                    case ChatMessage.TYPE_IMAGE:
                        return TYPE_GROUP_IMAGE;
                    default:
                        return TYPE_GROUP;
                }
            }
        }
        return TYPE_USER;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        GroupChatMessage chat = getItem(position);
        ChatDetailHolderUser holderUser;
        int layoutResource;
        switch (getItemViewType(position)) {
            case TYPE_USER:
                layoutResource = R.layout.item_chat_user;
                break;
            case TYPE_GROUP:
                layoutResource = R.layout.item_chat_group;
                break;
            case TYPE_GROUP_IMAGE:
                layoutResource = R.layout.item_chat_image_group;
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
        holderUser.bind(chat);
        return convertView;
    }

    public class ChatDetailHolderUser {

        private TextView mDate;

        private TextView mTime;

        private TextView mUserName, mContent;

        private ImageView mContentImage;

        private CircleImageView mImageStore;

        private LinearLayout mLayoutDate;

        ChatDetailHolderUser(View view) {
            mDate = (TextView) view.findViewById(R.id.tvDate);
            mUserName = view.findViewById(R.id.userName);
            mContent = (TextView) view.findViewById(R.id.tvContent);
            mContentImage = view.findViewById(R.id.imageContent);
            mTime = (TextView) view.findViewById(R.id.tvTime);
            mLayoutDate = (LinearLayout) view.findViewById(R.id.layoutDate);
            mImageStore = (CircleImageView) view.findViewById(R.id.imageStore);
            if (mContentImage != null) {
                mContentImage.getLayoutParams().height = mWidth;
                mContentImage.getLayoutParams().width = mWidth;
            }

        }

        void bind(final GroupChatMessage chat) {
            if (mImageStore != null) {
                Utils.fillImage(getContext(), chat.getAvatar(), mImageStore, R.drawable.icon_user);
            }
            if (mUserName != null) {
                mUserName.setText(chat.getName());
            }
            if (TextUtils.isEmpty(chat.getDate())) {
                mLayoutDate.setVisibility(View.GONE);
            } else {
                mLayoutDate.setVisibility(View.VISIBLE);
                mDate.setText(chat.getDate());
            }
            if (mContentImage != null) {
                final ContentChatImage contentChatImage = chat.getContentChatImage();
                if (contentChatImage != null) {
                    Utils.fillImage(getContext(), contentChatImage.getPreviewImageUrl(), mContentImage, R.drawable.image_choose, 100);
                }
                mContentImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mainActivity, ImageDetailActivity.class);
                        intent.putExtra(ImageDetailActivity.ARG_IMAGE, contentChatImage.getPreviewImageUrl());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(mainActivity, v, mainActivity.getString(R.string.image_transaction_name));
                            mainActivity.startActivity(intent, options.toBundle());
                        } else {
                            mainActivity.startActivity(intent);
                        }
                    }
                });
            }
            if (mContent != null) {
                mContent.setText((chat.getContent()));
            }
            String time = DateConvert.formatToString(DateConvert.TIME_FORMAT, chat.getCreated());
            mTime.setText(time);
        }
    }


}
