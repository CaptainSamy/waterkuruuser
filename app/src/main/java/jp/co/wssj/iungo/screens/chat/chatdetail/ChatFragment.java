package jp.co.wssj.iungo.screens.chat.chatdetail;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import org.apache.commons.lang.StringEscapeUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.chat.HistoryChatResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.chat.adapter.ChatAdapter;
import jp.co.wssj.iungo.screens.chat.dialog.DialogProfile;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.utils.Utils;
import jp.co.wssj.iungo.widget.swipyrefreshlayout.SwipyRefreshLayout;
import jp.co.wssj.iungo.widget.swipyrefreshlayout.SwipyRefreshLayoutDirection;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class ChatFragment extends BaseFragment<IChatView, ChatPresenter> implements IChatView, SwipyRefreshLayout.OnRefreshListener {

    private static String TAG = "ChatFragment";

    public static final String KEY_STORE_ID = "store_id";

    public static final String KEY_STORE_NAME = "store_name";

    public static final String KEY_IMAGE_STORE = "image_store";

    private SwipyRefreshLayout mRefreshListChat;

    private ListView mListViewChat;

    private EditText mInputChat;

    private ImageView mButtonSend;

    private ProgressBar mProgressSendChat;

    private List<HistoryChatResponse.HistoryChatData.ChatData> mListChat;

    private ChatAdapter mAdapter;

    private int mStoreId;

    private DialogProfile mDialogProfile;

    public static ChatFragment newInstance(Bundle b) {
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_CHAT;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_chat;
    }

    @Override
    protected ChatPresenter onCreatePresenter(IChatView view) {
        return new ChatPresenter(view);
    }

    @Override
    protected IChatView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        String storeName = getString(R.string.title_screen_chat);
        if (getArguments() != null) {
            storeName = getArguments().getString(KEY_STORE_NAME);

        }
        return storeName;
    }

    @Override
    protected void initViews(View rootView) {
        mRefreshListChat = (SwipyRefreshLayout) rootView.findViewById(R.id.refreshHistoryChat);
        mRefreshListChat.setDirection(SwipyRefreshLayoutDirection.BOTH);
        mListViewChat = (ListView) rootView.findViewById(R.id.lvStoreFollow);
        mInputChat = (EditText) rootView.findViewById(R.id.etChat);
        mButtonSend = (ImageView) rootView.findViewById(R.id.tvSendChat);
        mProgressSendChat = (ProgressBar) rootView.findViewById(R.id.progressSendChat);
    }

    private String dateOld = Constants.EMPTY_STRING;

    private String mContent = Constants.EMPTY_STRING;

    @Override
    protected void initAction() {
        mInputChat.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    mButtonSend.setEnabled(false);
                    mButtonSend.setImageDrawable(getResources().getDrawable(R.drawable.icon_send));
                } else {
                    mButtonSend.setEnabled(true);
                    mButtonSend.setImageDrawable(getResources().getDrawable(R.drawable.icon_send_enable));
                }
            }
        });
        mButtonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mContent = mInputChat.getText().toString().trim();
                if (!TextUtils.isEmpty(mContent)) {
                    mProgressSendChat.setVisibility(View.VISIBLE);
                    mButtonSend.setVisibility(View.GONE);
                    getPresenter().sendChat(mStoreId, StringEscapeUtils.escapeJava(mContent));
                }
                Utils.hideSoftKeyboard(getActivity());
            }
        });

        mRefreshListChat.setOnRefreshListener(this);
    }

    private boolean isOnRefresh;

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction != SwipyRefreshLayoutDirection.TOP) {
            isOnRefresh = true;
            getPresenter().getHistoryChat(mStoreId, 0);
        } else {
            if (mListChat != null && mListChat.size() > 0) {
                int lastId = mListChat.get(0).getId();
                getPresenter().getHistoryChat(mStoreId, lastId);
            }
        }
    }

    @Override
    protected void initData() {
        mListChat = new ArrayList<>();
        mAdapter = new ChatAdapter(getActivityContext(), mListChat);
        mAdapter.setOnClickImageStore(new ChatAdapter.IClickImageStore() {
            @Override
            public void onClick(int managerId) {
                if (mDialogProfile == null) {
                    mDialogProfile = new DialogProfile(getActivityContext(), managerId, getActivityCallback());
                }
                mDialogProfile.show();
            }
        });
        mListViewChat.setAdapter(mAdapter);
        if (getArguments() != null) {
            mStoreId = getArguments().getInt(KEY_STORE_ID);
            String imageStore = getArguments().getString(KEY_IMAGE_STORE);
            mAdapter.setImageStore(imageStore);
            mRefreshListChat.setRefreshing(true);
            getPresenter().getHistoryChat(mStoreId, 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private Handler mHandle = new Handler();

    private Runnable mRunAble = new Runnable() {

        @Override
        public void run() {
            mHandle.postDelayed(mRunAble, Constants.TIME_DELAY_GET_LIST_CHAT);
            getPresenter().getHistoryChat(mStoreId, 0);
        }
    };

    @Override
    public void onGetHistoryChatSuccess(List<HistoryChatResponse.HistoryChatData.ChatData> history) {
        mRefreshListChat.setRefreshing(false);
        if (history != null && history.size() > 0) {
            if (isOnRefresh) {
                isOnRefresh = false;
                mListChat.clear();
            }
            Collections.reverse(history);
            sortListChat(history);
            mListChat.addAll(0, history);
            mAdapter.notifyDataSetChanged();
        }
        if (mAdapter.getCount() == 0) {
            showTextNoItem(true, getString(R.string.no_conversation));
        }else {
            showTextNoItem(false, null);
        }
    }

    private void sortListChat(List<HistoryChatResponse.HistoryChatData.ChatData> history) {
        String date = Constants.EMPTY_STRING;
        for (HistoryChatResponse.HistoryChatData.ChatData chatData : history) {
            if (TextUtils.isEmpty(date)) {
                date = Utils.formatDate(chatData.getTimeCreate(), "MM/dd/yyyy");
                chatData.setDate(date);
            } else {
                String dateTemp = Utils.formatDate(chatData.getTimeCreate(), "MM/dd/yyyy");
                if (!TextUtils.equals(date, dateTemp)) {
                    date = dateTemp;
                    chatData.setDate(dateTemp);
                }
            }
        }

        if (mListChat != null) {
            for (HistoryChatResponse.HistoryChatData.ChatData chat : mListChat) {
                if (!TextUtils.isEmpty(chat.getDate())) {
                    dateOld = chat.getDate();
                    if (TextUtils.equals(date, dateOld)) {
                        chat.setDate(null);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onGetHistoryChatFailure(String message) {
        isOnRefresh = false;
        mRefreshListChat.setRefreshing(false);
        if (mListChat.size() == 0) {
            showToast(message);
        }
    }

    @Override
    public void onSendChatSuccess() {
        mInputChat.setText(Constants.EMPTY_STRING);
        mProgressSendChat.setVisibility(View.GONE);
        mButtonSend.setVisibility(View.VISIBLE);
        onRefresh(SwipyRefreshLayoutDirection.BOTTOM);
    }

    @Override
    public void onSendChatFailure(String message) {
        mProgressSendChat.setVisibility(View.GONE);
        mButtonSend.setVisibility(View.VISIBLE);
    }

    private void scrollEndOfListView() {
        mListViewChat.post(new Runnable() {

            @Override
            public void run() {
                mListViewChat.setSelection(mAdapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.d(TAG, "removeCallbacks ");
        mHandle.removeCallbacks(mRunAble);
    }
}
