package wssj.co.jp.obis.screens.chat.adapter;

import android.content.Context;
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
import wssj.co.jp.obis.screens.ImageDetailActivity;
import wssj.co.jp.obis.screens.MainActivity;
import wssj.co.jp.obis.utils.DateConvert;
import wssj.co.jp.obis.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 12/9/2017.
 */

public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    private static final String TAG = "ChatAdapter";

    private static final int TYPE_USER = 0;

    private static final int TYPE_STORE = 1;

    private static final int TYPE_STORE_IMAGE = 2;

    private static final int TYPE_COUNT = 4;

    private static final int TYPE_IMAGE_MAP = 3;

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
        Log.d(TAG, "Constructor Adapter");
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
                    case ChatMessage.TYPE_IMAGE: {
                        return TYPE_STORE_IMAGE;
                    }
                    case ChatMessage.TYPE_IMAGE_MAP:
                        Log.d(TAG, "is type imagemap");
                        return TYPE_IMAGE_MAP;

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
            case TYPE_IMAGE_MAP:
                layoutResource = R.layout.item_chat_image_map;
                break;
            default:
                layoutResource = R.layout.item_chat_user;
                break;
        }
        if (convertView == null) {
            Log.d(TAG, "convert view null");
            convertView = mInflate.inflate(layoutResource, parent, false);
            holderUser = new ChatDetailHolderUser(convertView, mainActivity);
            convertView.setTag(holderUser);
        } else {
            Log.d(TAG, "convert view  not null");
            holderUser = (ChatDetailHolderUser) convertView.getTag();
        }
        holderUser.bind(chat, position);
        return convertView;
    }

    public class ChatDetailHolderUser {


        private Context mContext;

        private TextView mDate;

        private TextView mTime;

        private TextView mContent;

        private ImageView mContentImage;

        private CircleImageView mImageStore;

        private LinearLayout mLayoutDate;

        private ImageView mImageMap;

        private ConstraintLayout layoutBtn;

        private View btn1, btn2, btn3, btn4, btn5, btn6;

        private List<View> lstBtn = new ArrayList<>();

        public ChatDetailHolderUser(View view, Context context) {

            mContext = context;

            mDate = (TextView) view.findViewById(R.id.tvDate);
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

        public void bind(final ChatMessage chat, int position) {
            switch (getItemViewType(position)) {
                case TYPE_STORE:
                case TYPE_STORE_IMAGE:
                case TYPE_IMAGE_MAP:
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
            if (mImageMap != null) {
                final ContentImageMap contentImageMap = chat.getContentImageMap();
                Log.d(TAG, "chat content: " + chat.getContent() + "position: " + position);
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

                    if (actions.size() == 4){
                        Log.d(TAG, "chat content 4 action: " + chat.getContent());
                    }

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
//            if (mImageMap != null) {
//                final ContentImageMap contentImageMap = chat.getContentImageMap();
//                Log.d(TAG, "chat content: " + chat.getContent() + "position: " + position);
//                final String urlImage ;
//                if (contentImageMap != null) {
////                    String nameImg = contentImageMap.getBaseUrl().split("/"split)[contentImageMap.getBaseUrl().split("/").length - 1];
//                    urlImage = contentImageMap.getBaseUrl() + "/" + "1040";
//                    Log.d(TAG, "url image map: " + urlImage);
//                    Utils.fillImage(getContext(), urlImage, mImageMap, R.drawable.image_choose, 100);
//
//
//                    //todo handle action
//
//                    final int sizeAction = contentImageMap.getActions().size();
//
////                    showBtn();
//                    if (sizeAction == 0) {
//                        showBtn();
//                        hide5Btn();
//                        Log.d(TAG, "size action 0" + urlImage + " position: " + position);
//                        btn1.getLayoutParams().height = mWidth;
//                        btn1.getLayoutParams().width = mWidth;
////                        btn1.setBackgroundColor(Color.RED);
//                        btn1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(mainActivity, ImageDetailActivity.class);
//                                intent.putExtra(ImageDetailActivity.ARG_IMAGE, urlImage);
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                                            makeSceneTransitionAnimation(mainActivity, view, mainActivity.getString(R.string.image_transaction_name));
//                                    mainActivity.startActivity(intent, options.toBundle());
//                                } else {
//                                    mainActivity.startActivity(intent);
//                                }
//                            }
//                        });
//                    }
//                    if (sizeAction == 1){
//                        showBtn();
//                        hide5Btn();
//                        Log.d(TAG, "size action 1" + urlImage + " position: " + position + ", chat content: " + chat.getContent());
//                        btn1.getLayoutParams().height = mWidth;
//                        btn1.getLayoutParams().width = mWidth;
//
//                        btn1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 0)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//                    }
//                    // 2 action
//                    if (sizeAction == 2){
//                        showBtn();
//                        Log.d(TAG, "size action 2" + urlImage + " position: " + position + ", chat content: " + chat.getContent());
//                        hide4Btn();
////                        btn1.setBackgroundColor(Color.BLACK);
////                        btn2.setBackgroundColor(Color.BLUE);
//
//                        Boolean isVerticalBtn = true;
//                        for (int i = 0; i < sizeAction; i ++){
//                            Actions action = contentImageMap.getActions().get(i);
//                            if (action.getArea().getX() != 0){
//                                isVerticalBtn = false;
//                                Log.d(TAG, "btn horizontal");
//                            }
//                            if (action.getArea().getY() != 0){
//                                isVerticalBtn = true;
//                                Log.d(TAG, "btn vertical");
//                            }
//                        }
//
//                        if (isVerticalBtn){
//                            btn1.getLayoutParams().height = mWidth/2;
//                            btn1.getLayoutParams().width = mWidth;
//                            btn1.requestLayout();
//
//                            btn2.getLayoutParams().height = mWidth/2;
//                            btn2.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
////                            btn1.requestLayout();
//                        } else {
//                            btn1.getLayoutParams().height = mWidth;
//                            btn1.getLayoutParams().width = mWidth/2;
//
//                            btn2.getLayoutParams().height = mWidth;
//                            btn2.getLayoutParams().width = mWidth/2;
//                        }
//
//                        ConstraintSet constraintSet = new ConstraintSet();
//                        constraintSet.clone(layoutBtn);
//
//                        constraintSet.clear(btn1.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn1.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn1.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn1.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.clear(btn2.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.BOTTOM);
//
//                        if (isVerticalBtn){
//
//                            constraintSet.connect(btn1.getId(),ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,0);
//                            constraintSet.connect(btn2.getId(),ConstraintSet.TOP, btn1.getId(), ConstraintSet.BOTTOM,0);
//                            constraintSet.connect(btn2.getId(),ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,0);
//                        } else {
//
//                            constraintSet.connect(btn1.getId(),ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
//                            constraintSet.connect(btn2.getId(),ConstraintSet.START, btn1.getId(), ConstraintSet.END,0);
//                        }
//
//                        constraintSet.applyTo(layoutBtn);
//
//                        btn1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 0)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//                        btn2.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 1)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//
//                    }
//                    // 3 action
//                    if (sizeAction == 3){
//                        showBtn();
//                        Log.d(TAG, "size action 3" + urlImage + " position: " + position  + "chatcontent: " + chat.getContent());
//                        hide3Btn();
//
////                        btn1.setBackgroundColor(Color.BLACK);
////
////                        btn2.setBackgroundColor(Color.BLUE);
////
////                        btn3.setBackgroundColor(Color.RED);
//
//                        Boolean isX = false;
//                        boolean isRegular = false;
//
//                        for (int i = 0; i< sizeAction; i++){
//                            Actions action = contentImageMap.getActions().get(i);
//                            if (action.getArea().getX() != 0){
//                                isX = true;
//                            }
//                        }
//
//                        if (isX){
//                            btn1.getLayoutParams().height = mWidth/2;
//                            btn1.getLayoutParams().width = mWidth;
//
//                            btn2.getLayoutParams().height = mWidth / 2;
//                            btn2.getLayoutParams().width = mWidth / 2;
//
//                            btn3.getLayoutParams().height = mWidth / 2;
//                            btn3.getLayoutParams().width = mWidth / 2;
//                        }
//                        else {
//                            for (int i = 0; i< sizeAction; i++){
//                                Actions action = contentImageMap.getActions().get(i);
//                                if (action.getArea().getY() != 0){
//                                    int check = Math.round((float)action.getArea().getHeight()/action.getArea().getY());
//                                    Log.d(TAG, "number check: "+ check);
//                                    if (check == 3){
//                                        isRegular = true;
//                                    }
//                                }
//                            }
//                            if (isRegular){
//                                btn1.getLayoutParams().height = mWidth/3;
//                                btn1.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
//
//                                btn2.getLayoutParams().height = mWidth/3;
//                                btn2.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
//
//                                btn3.getLayoutParams().height = mWidth/3;
//                            } else {
//                                btn1.getLayoutParams().height = mWidth/2;
//                                btn1.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
//
//                                btn2.getLayoutParams().height = mWidth/4;
//                                btn2.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
//
//                                btn3.getLayoutParams().height = mWidth/4;
//                            }
//                            btn3.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
//                        }
//
//                        ConstraintSet constraintSet = new ConstraintSet();
//                        constraintSet.clone(layoutBtn);
//
//                        constraintSet.clear(btn1.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn1.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn1.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn1.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.clear(btn2.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.clear(btn2.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.clear(btn3.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn3.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn3.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn3.getId(), ConstraintSet.BOTTOM);
//
//                        if (isX){
//                            constraintSet.connect(btn1.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
//                            constraintSet.connect(btn1.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
//                            constraintSet.connect(btn1.getId(), ConstraintSet.BOTTOM, btn2.getId(), ConstraintSet.TOP, 0);
//
//                            constraintSet.connect(btn2.getId(), ConstraintSet.TOP, btn1.getId(), ConstraintSet.BOTTOM, 0);
//                            constraintSet.connect(btn2.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
//                            constraintSet.connect(btn2.getId(), ConstraintSet.END, btn3.getId(), ConstraintSet.START, 0);
//
//                            constraintSet.connect(btn3.getId(), ConstraintSet.START, btn2.getId(), ConstraintSet.END, 0);
//                            constraintSet.connect(btn3.getId(), ConstraintSet.TOP, btn1.getId(), ConstraintSet.BOTTOM, 0);
//                            constraintSet.connect(btn3.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
//                        }else {
//                            constraintSet.connect(btn1.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
//                            constraintSet.connect(btn1.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
//                            constraintSet.connect(btn1.getId(), ConstraintSet.BOTTOM, btn2.getId(), ConstraintSet.TOP, 0);
//
//                            constraintSet.connect(btn2.getId(), ConstraintSet.TOP, btn1.getId(), ConstraintSet.BOTTOM, 0);
//                            constraintSet.connect(btn2.getId(), ConstraintSet.BOTTOM, btn3.getId(), ConstraintSet.TOP, 0);
//
//                            constraintSet.connect(btn3.getId(), ConstraintSet.TOP, btn2.getId(), ConstraintSet.BOTTOM, 0);
//                            constraintSet.connect(btn3.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
//                        }
//
//                        constraintSet.applyTo(layoutBtn);
//
//                        btn1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 0)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//                        btn2.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 1)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//                        btn3.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 2)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//
//                    }
//                    // 4 action
//                    if (sizeAction == 4){
//                        showBtn();
//                        Log.d(TAG, "size action 4" + urlImage + " position: " + position);
//                        hide2Btn();
//                        btn1.getLayoutParams().height = mWidth/2;
//                        btn1.getLayoutParams().width = mWidth/2;
////                        btn1.setBackgroundColor(Color.BLACK);
//
//                        btn2.getLayoutParams().height = mWidth/2;
//                        btn2.getLayoutParams().width = mWidth/2;
////                        btn2.setBackgroundColor(Color.BLUE);
//
//                        btn3.getLayoutParams().height = mWidth/2;
//                        btn3.getLayoutParams().width = mWidth/2;
////                        btn3.setBackgroundColor(Color.BLUE);
//
//
//                        btn4.getLayoutParams().height = mWidth/2;
//                        btn4.getLayoutParams().width = mWidth/2;
////                        btn4.setBackgroundColor(Color.BLACK);
//
//
//                        ConstraintSet constraintSet = new ConstraintSet();
//                        constraintSet.clone(layoutBtn);
//
//                        constraintSet.clear(btn1.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn1.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn1.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn1.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.clear(btn2.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.clear(btn2.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.clear(btn3.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn3.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn3.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn3.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.clear(btn4.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn4.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn4.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn4.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.connect(btn1.getId(),ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
//                        constraintSet.connect(btn2.getId(),ConstraintSet.LEFT, btn1.getId(), ConstraintSet.RIGHT,0);
//                        constraintSet.connect(btn3.getId(),ConstraintSet.TOP, btn1.getId(), ConstraintSet.BOTTOM, 0);
//                        constraintSet.connect(btn3.getId(),ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
//                        constraintSet.connect(btn4.getId(), ConstraintSet.TOP, btn2.getId(), ConstraintSet.BOTTOM, 0);
//                        constraintSet.connect(btn4.getId(),ConstraintSet.LEFT, btn3.getId(), ConstraintSet.RIGHT,0);
//                        constraintSet.applyTo(layoutBtn);
//
//                        btn1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 0)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//                        btn2.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 1)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//                        btn3.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 2)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//                        btn4.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 3)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//
//                    }
//                    // 6 action click
//                    if (sizeAction == 6){
//                        showBtn();
//                        Log.d(TAG, "size action 6" + urlImage + " position: " + position+ "chat content: "+ chat.getContent());
//
//                        btn1.getLayoutParams().height = mWidth/2;
//                        btn1.getLayoutParams().width = mWidth/3;
////                        btn1.setBackgroundColor(Color.BLACK);
//
//                        btn2.getLayoutParams().height = mWidth/2;
//                        btn2.getLayoutParams().width = mWidth/3;
////                        btn2.setBackgroundColor(Color.BLUE);
//
//                        btn3.getLayoutParams().height = mWidth/2;
//                        btn3.getLayoutParams().width = mWidth/3;
////                        btn3.setBackgroundColor(Color.BLACK);
//
//
//                        btn4.getLayoutParams().height = mWidth/2;
//                        btn4.getLayoutParams().width = mWidth/3;
////                        btn4.setBackgroundColor(Color.BLUE);
//
//                        btn5.getLayoutParams().height = mWidth/2;
//                        btn5.getLayoutParams().width = mWidth/3;
////                        btn5.setBackgroundColor(Color.BLACK);
//
//                        btn6.getLayoutParams().height = mWidth/2;
//                        btn6.getLayoutParams().width = mWidth/3;
////                        btn6.setBackgroundColor(Color.BLUE);
//
//                        ConstraintSet constraintSet = new ConstraintSet();
//                        constraintSet.clone(layoutBtn);
//                        constraintSet.clear(btn1.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn1.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn1.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn1.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.clear(btn2.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.clear(btn2.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn2.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.clear(btn3.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn3.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn3.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn3.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.clear(btn4.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn4.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn4.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn4.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.clear(btn5.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn5.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn5.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn5.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.clear(btn6.getId(), ConstraintSet.START);
//                        constraintSet.clear(btn6.getId(), ConstraintSet.TOP);
//                        constraintSet.clear(btn6.getId(), ConstraintSet.END);
//                        constraintSet.clear(btn6.getId(), ConstraintSet.BOTTOM);
//
//                        constraintSet.connect(btn1.getId(),ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
//                        constraintSet.connect(btn2.getId(),ConstraintSet.LEFT, btn1.getId(), ConstraintSet.RIGHT,0);
//                        constraintSet.connect(btn3.getId(),ConstraintSet.LEFT, btn2.getId(), ConstraintSet.RIGHT,0);
//                        constraintSet.connect(btn4.getId(),ConstraintSet.TOP, btn1.getId(), ConstraintSet.BOTTOM, 0);
//                        constraintSet.connect(btn4.getId(),ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START,0);
//                        constraintSet.connect(btn5.getId(), ConstraintSet.TOP, btn2.getId(), ConstraintSet.BOTTOM, 0);
//                        constraintSet.connect(btn5.getId(),ConstraintSet.LEFT, btn4.getId(), ConstraintSet.RIGHT,0);
//                        constraintSet.connect(btn6.getId(), ConstraintSet.TOP, btn3.getId(), ConstraintSet.BOTTOM, 0);
//                        constraintSet.connect(btn6.getId(),ConstraintSet.LEFT, btn5.getId(), ConstraintSet.RIGHT,0);
//                        constraintSet.applyTo(layoutBtn);
//
//                        btn1.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 0)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//                        btn2.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 1)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//                        btn3.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 2)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//                        btn4.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 3)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//                        btn5.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 4)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//                        btn6.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getUrl(contentImageMap, 5)));
//                                mainActivity.startActivity(intent);
//                            }
//                        });
//                    }
//                }
//            }
            if (mContent != null) {
                mContent.setText((chat.getContent()));
            }
            String time = DateConvert.formatToString(DateConvert.TIME_FORMAT, chat.getCreated());
            mTime.setText(time);
        }

        private String getUrl(ContentImageMap contentImageMap, int index){
            return contentImageMap.getActions().get(index).getLinkUri();
        }

//        private void showBtn(){
//            btn1.setVisibility(View.VISIBLE);
//            btn2.setVisibility(View.VISIBLE);
//            btn3.setVisibility(View.VISIBLE);
//            btn4.setVisibility(View.VISIBLE);
//            btn5.setVisibility(View.VISIBLE);
//            btn6.setVisibility(View.VISIBLE);
//        }
//
        private void hideBtn(){
            btn1.setVisibility(View.GONE);
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.GONE);
            btn4.setVisibility(View.GONE);
            btn5.setVisibility(View.GONE);
            btn6.setVisibility(View.GONE);
        }
//
//        private void hide4Btn(){
//            btn3.setVisibility(View.GONE);
//            btn4.setVisibility(View.GONE);
//            btn5.setVisibility(View.GONE);
//            btn6.setVisibility(View.GONE);
//        }
//
//        private void hide3Btn(){
//            btn4.setVisibility(View.GONE);
//            btn5.setVisibility(View.GONE);
//            btn6.setVisibility(View.GONE);
//        }
//
//        private void hide2Btn(){
//            btn5.setVisibility(View.GONE);
//            btn6.setVisibility(View.GONE);
//        }
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
