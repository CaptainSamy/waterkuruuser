package jp.co.wssj.iungo.screens.chat.chatdetail;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconEditText;
import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.chat.HistoryChatResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.chat.adapter.ChatAdapter;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class ChatFragment extends BaseFragment<IChatView, ChatPresenter> implements IChatView, SwipeRefreshLayout.OnRefreshListener {

    private static String TAG = "ChatFragment";

    public static final String KEY_STORE_ID = "store_id";

    private SwipeRefreshLayout mRefreshListChat;

    private ListView mListViewChat;

    private EmojiconEditText mInputChat;

    private ImageView mButtonSend;

    private ProgressBar mProgressSendChat;

    private List<HistoryChatResponse.HistoryChatData.ChatData> mListChat;

    private ChatAdapter mAdapter;

    private int mStoreId;

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
        return getString(R.string.title_screen_chat);
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return false;
    }

    @Override
    protected void initViews(View rootView) {
        mRefreshListChat = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshHistoryChat);
        mListViewChat = (ListView) rootView.findViewById(R.id.lvStoreFollow);
        mInputChat = (EmojiconEditText) rootView.findViewById(R.id.etChat);
        mButtonSend = (ImageView) rootView.findViewById(R.id.tvSendChat);
        mProgressSendChat = (ProgressBar) rootView.findViewById(R.id.progressSendChat);
    }

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
                if (s.length() == 0) {
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
                String content = mInputChat.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    mProgressSendChat.setVisibility(View.VISIBLE);
                    mButtonSend.setVisibility(View.GONE);
                    getPresenter().sendChat(mStoreId, StringEscapeUtils.escapeJava(content));
                }
                Utils.hideSoftKeyboard(getActivity());
            }
        });

        mRefreshListChat.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        getPresenter().getHistoryChat(mStoreId);
    }

    @Override
    protected void initData() {
        mListChat = new ArrayList<>();
        mAdapter = new ChatAdapter(getActivityContext(), mListChat);
        mListViewChat.setAdapter(mAdapter);
        if (getArguments() != null) {
            mStoreId = getArguments().getInt(KEY_STORE_ID);
            mRefreshListChat.setRefreshing(true);
            getPresenter().getHistoryChat(mStoreId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandle.postDelayed(mRunAble, Constants.TIME_DELAY_GET_LIST_CHAT);
    }

    private Handler mHandle = new Handler();

    private Runnable mRunAble = new Runnable() {

        @Override
        public void run() {
            mHandle.postDelayed(mRunAble, Constants.TIME_DELAY_GET_LIST_CHAT);
            getPresenter().getHistoryChat(mStoreId);
        }
    };

    @Override
    public void onGetHistoryChatSuccess(List<HistoryChatResponse.HistoryChatData.ChatData> history) {
        mRefreshListChat.setRefreshing(false);
        if (mListChat != null) {
            mListChat.clear();
        }
        mListChat.addAll(history);
        Collections.reverse(mListChat);
        mAdapter.notifyDataSetChanged();
        scrollEndOfListView();
    }

    @Override
    public void onGetHistoryChatFailure(String message) {
        mRefreshListChat.setRefreshing(false);
        showToast(message);
    }

    @Override
    public void onSendChatSuccess() {
        mInputChat.setText(Constants.EMPTY_STRING);
        mProgressSendChat.setVisibility(View.GONE);
        mButtonSend.setVisibility(View.VISIBLE);
        onRefresh();
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
