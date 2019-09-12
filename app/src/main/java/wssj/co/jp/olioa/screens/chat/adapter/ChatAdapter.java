package wssj.co.jp.olioa.screens.chat.adapter;

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
import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.chat.ChatMessage;
import wssj.co.jp.olioa.model.entities.ContentChatImage;
import wssj.co.jp.olioa.screens.ImageDetailActivity;
import wssj.co.jp.olioa.screens.MainActivity;
import wssj.co.jp.olioa.utils.DateConvert;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 12/9/2017.
 */

public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    private static final int TYPE_USER = 0;

    private static final int TYPE_STORE = 1;

    private static final int TYPE_STORE_IMAGE = 2;

    private static final int TYPE_COUNT = 3;

    private LayoutInflater mInflate;

    private String mUrlImageStore;

    private int mWidth;

    private IClickImageStore clickImageStore;

    private MainActivity mainActivity;

    public ChatAdapter(@NonNull MainActivity context, @NonNull List<ChatMessage> objects) {
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
        ChatMessage chat = getItem(position);
        if (chat != null) {
            if (chat.isUser()) {
                return TYPE_USER;
            } else {
                if (chat.getContentType() == null) {
                    return TYPE_STORE;
                }
                switch (chat.getContentType()) {
                    case ChatMessage.TYPE_IMAGE:
                        return TYPE_STORE_IMAGE;
                    default:
                        return TYPE_STORE;
                }
            }
        }
        return TYPE_USER;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMessage chat = getItem(position);
        if (chat == null) {
            return null;
        }
        ChatDetailHolderUser holderUser;
        int layoutResource;
        switch (getItemViewType(position)) {
            case TYPE_USER:
                layoutResource = R.layout.item_chat_user;
                break;
            case TYPE_STORE:
                layoutResource = R.layout.item_chat_store;
                break;
            case TYPE_STORE_IMAGE:
                layoutResource = R.layout.item_chat_image_store;
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
        holderUser.bind(chat, position);
        return convertView;
    }

    public class ChatDetailHolderUser {

        private TextView mDate;

        private TextView mTime;

        private TextView mContent;

        private ImageView mContentImage;

        private CircleImageView mImageStore;

        private LinearLayout mLayoutDate;

        public ChatDetailHolderUser(View view) {
            mDate = (TextView) view.findViewById(R.id.tvDate);
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

        public void bind(final ChatMessage chat, int position) {
            switch (getItemViewType(position)) {
                case TYPE_STORE:
                case TYPE_STORE_IMAGE:
                    Utils.fillImage(getContext(), mUrlImageStore, mImageStore, R.drawable.icon_user);
                    break;
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

    public void setImageStore(String imageStore) {
        this.mUrlImageStore = imageStore;
    }

    public interface IClickImageStore {

        void onClick(int managerId);
    }

    public void setOnClickImageStore(IClickImageStore clickImageStore) {
        this.clickImageStore = clickImageStore;
    }

}
