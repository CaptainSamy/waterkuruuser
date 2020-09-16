package wssj.co.jp.obis.screens.groupchat.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.chat.ChatMessage;
import wssj.co.jp.obis.model.entities.Actions;
import wssj.co.jp.obis.model.entities.Area;
import wssj.co.jp.obis.model.entities.ContentChatImage;
import wssj.co.jp.obis.model.entities.ContentImageMap;
import wssj.co.jp.obis.model.entities.GroupChatMessage;
import wssj.co.jp.obis.screens.MainActivity;
import wssj.co.jp.obis.utils.DateConvert;
import wssj.co.jp.obis.utils.Utils;
import wssj.co.jp.obis.screens.ImageDetailActivity;

public class GroupChatAdapter extends ArrayAdapter<GroupChatMessage> {

    private static final String TAG = "GroupChatAdapter";

    private static final int TYPE_USER = 0;

    private static final int TYPE_GROUP = 1;

    private static final int TYPE_GROUP_IMAGE = 2;

    private static final int TYPE_GROUP_IMAGE_MAP = 3;

    private static final int TYPE_COUNT = 4;

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
                    case ChatMessage.TYPE_IMAGE_MAP:
                        return TYPE_GROUP_IMAGE_MAP;
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
            case TYPE_GROUP_IMAGE_MAP:
                layoutResource = R.layout.item_chat_group_image_map;
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

        private ImageView mImageMap;

        private ConstraintLayout layoutBtn;

        private View btn1, btn2, btn3, btn4, btn5, btn6;

        private List<View> lstBtn = new ArrayList<>();



        ChatDetailHolderUser(View view) {
            mDate = (TextView) view.findViewById(R.id.tvDate);
            mUserName = view.findViewById(R.id.userName);
            mContent = (TextView) view.findViewById(R.id.tvContent);
            mContentImage = view.findViewById(R.id.imageContent);
            mTime = (TextView) view.findViewById(R.id.tvTime);
            mLayoutDate = (LinearLayout) view.findViewById(R.id.layoutDate);
            mImageStore = (CircleImageView) view.findViewById(R.id.imageStore);

            mImageMap = (ImageView) view.findViewById(R.id.image_map);
            layoutBtn = (ConstraintLayout) view.findViewById(R.id.layout_btn);
            btn1 = (View)view.findViewById(R.id.btn_1);
            btn2 = (View)view.findViewById(R.id.btn_2);
            btn3 = (View)view.findViewById(R.id.btn_3);
            btn4 = (View)view.findViewById(R.id.btn_4);
            btn5 = (View)view.findViewById(R.id.btn_5);
            btn6 = (View)view.findViewById(R.id.btn_6);

            lstBtn.add(btn1);
            lstBtn.add(btn2);
            lstBtn.add(btn3);
            lstBtn.add(btn4);
            lstBtn.add(btn5);
            lstBtn.add(btn6);

            if (mContentImage != null) {
                mContentImage.getLayoutParams().height = mWidth;
                mContentImage.getLayoutParams().width = mWidth;
            }

            if (mImageMap != null) {
                mImageMap.getLayoutParams().height = mWidth;
                mImageMap.getLayoutParams().width = mWidth;
            }
            if (layoutBtn != null){
                layoutBtn.getLayoutParams().height = mWidth;
                layoutBtn.getLayoutParams().width = mWidth;
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

            if (mImageMap != null) {
                final ContentImageMap contentImageMap = chat.getContentImageMap();
                Log.d(TAG, "chat content: " + chat.getContent() );
                final String urlImage ;
                if (contentImageMap != null) {
                    urlImage = contentImageMap.getBaseUrl() + "/" + "1040";
                    Log.d(TAG, "url image map: " + urlImage);
                    Utils.fillImage(getContext(), urlImage, mImageMap, R.drawable.image_choose, 100);
                    final List<Actions> actions = contentImageMap.getActions();

                    ConstraintSet constraintSetClear = new ConstraintSet();
                    constraintSetClear.clone(layoutBtn);
                    constraintSetClear.clear(btn1.getId(), ConstraintSet.START);
                    constraintSetClear.clear(btn1.getId(), ConstraintSet.TOP);
                    constraintSetClear.clear(btn1.getId(), ConstraintSet.END);
                    constraintSetClear.clear(btn1.getId(), ConstraintSet.BOTTOM);

                    constraintSetClear.clear(btn2.getId(), ConstraintSet.START);
                    constraintSetClear.clear(btn2.getId(), ConstraintSet.TOP);
                    constraintSetClear.clear(btn2.getId(), ConstraintSet.END);
                    constraintSetClear.clear(btn2.getId(), ConstraintSet.BOTTOM);

                    constraintSetClear.clear(btn2.getId(), ConstraintSet.START);
                    constraintSetClear.clear(btn2.getId(), ConstraintSet.TOP);
                    constraintSetClear.clear(btn2.getId(), ConstraintSet.END);
                    constraintSetClear.clear(btn2.getId(), ConstraintSet.BOTTOM);

                    constraintSetClear.clear(btn3.getId(), ConstraintSet.START);
                    constraintSetClear.clear(btn3.getId(), ConstraintSet.TOP);
                    constraintSetClear.clear(btn3.getId(), ConstraintSet.END);
                    constraintSetClear.clear(btn3.getId(), ConstraintSet.BOTTOM);

                    constraintSetClear.clear(btn4.getId(), ConstraintSet.START);
                    constraintSetClear.clear(btn4.getId(), ConstraintSet.TOP);
                    constraintSetClear.clear(btn4.getId(), ConstraintSet.END);
                    constraintSetClear.clear(btn4.getId(), ConstraintSet.BOTTOM);

                    constraintSetClear.clear(btn5.getId(), ConstraintSet.START);
                    constraintSetClear.clear(btn5.getId(), ConstraintSet.TOP);
                    constraintSetClear.clear(btn5.getId(), ConstraintSet.END);
                    constraintSetClear.clear(btn5.getId(), ConstraintSet.BOTTOM);

                    constraintSetClear.clear(btn6.getId(), ConstraintSet.START);
                    constraintSetClear.clear(btn6.getId(), ConstraintSet.TOP);
                    constraintSetClear.clear(btn6.getId(), ConstraintSet.END);
                    constraintSetClear.clear(btn6.getId(), ConstraintSet.BOTTOM);

                    constraintSetClear.applyTo(layoutBtn);

                    hideBtn();

                    for (int i = 0; i < actions.size(); i++) {
                        Area area = actions.get(i).getArea();
                        final String url = actions.get(i).getLinkUri();
                        if (area.getX() == 0 && area.getY() == 0) {
                            View btn = lstBtn.get(i);
//                            btn.setBackgroundColor(Color.BLUE);
                            btn.setVisibility(View.VISIBLE);

                            btn.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                            btn.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    mainActivity.startActivity(intent);
                                }
                            });

                        }
                        if (area.getX() != 0 && area.getY() == 0) {
                            View btn = lstBtn.get(i);
//                            if (i % 2 == 0) {
//                                btn.setBackgroundColor(Color.RED);
//                            } else {
//                                btn.setBackgroundColor(Color.GREEN);
//                            }

                            btn.setVisibility(View.VISIBLE);
                            int withBtn = mWidth - Math.round((float) (mWidth * area.getX()) / area.getWidth());

                            btn.getLayoutParams().width = withBtn;
                            btn.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

                            ConstraintSet constraintSet = new ConstraintSet();
                            constraintSet.clone(layoutBtn);


                            constraintSet.connect(btn.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
                            constraintSet.connect(btn.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
                            constraintSet.connect(btn.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

                            constraintSet.applyTo(layoutBtn);

                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    mainActivity.startActivity(intent);
                                }
                            });
                        }

                        if (area.getX() == 0 && area.getY() != 0) {
                            View btn = lstBtn.get(i);
//                            if (i % 2 == 0) {
//                                btn.setBackgroundColor(Color.BLACK);
//                            } else {
//                                btn.setBackgroundColor(Color.YELLOW);
//                            }
                            btn.setVisibility(View.VISIBLE);
                            int heightBtn = mWidth - Math.round((float) (mWidth * area.getY()) / area.getHeight());

                            btn.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                            btn.getLayoutParams().height = heightBtn;

                            ConstraintSet constraintSet = new ConstraintSet();
                            constraintSet.clone(layoutBtn);

                            constraintSet.connect(btn.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
                            constraintSet.connect(btn.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

                            constraintSet.applyTo(layoutBtn);

                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    mainActivity.startActivity(intent);
                                }
                            });

                        }

                        if (area.getX() != 0 && area.getY() != 0) {
                            View btn = lstBtn.get(i);
//                            if (i % 2 == 0) {
//                                btn.setBackgroundColor(Color.BLACK);
//                            } else {
//                                btn.setBackgroundColor(Color.WHITE);
//                            }

                            btn.setVisibility(View.VISIBLE);

                            int heightBtn = mWidth - Math.round((float) (mWidth * area.getY()) / area.getHeight());
                            int withBtn = mWidth - Math.round((float) (mWidth * area.getX()) / area.getWidth());

                            btn.getLayoutParams().height = heightBtn;
                            btn.getLayoutParams().width = withBtn;

                            ConstraintSet constraintSet = new ConstraintSet();
                            constraintSet.clone(layoutBtn);

                            constraintSet.connect(btn.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
                            constraintSet.connect(btn.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

                            constraintSet.applyTo(layoutBtn);

                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    mainActivity.startActivity(intent);
                                }
                            });
                        }
                    }

                }
            }

            if (mContent != null) {
                mContent.setText((chat.getContent()));
            }
            String time = DateConvert.formatToString(DateConvert.TIME_FORMAT, chat.getCreated());
            mTime.setText(time);
        }

        private void hideBtn(){
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            btn4.setVisibility(View.GONE);
            btn5.setVisibility(View.GONE);
            btn6.setVisibility(View.GONE);
        }
    }


}
