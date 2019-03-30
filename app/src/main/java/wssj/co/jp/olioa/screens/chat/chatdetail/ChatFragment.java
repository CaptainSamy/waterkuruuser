package wssj.co.jp.olioa.screens.chat.chatdetail;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.chat.ChatMessage;
import wssj.co.jp.olioa.model.entities.StoreInfo;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.chat.adapter.ChatAdapter;
import wssj.co.jp.olioa.screens.chat.dialog.DialogProfile;
import wssj.co.jp.olioa.screens.liststorecheckedin.ListStoreChatFragment;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.DateConvert;
import wssj.co.jp.olioa.utils.Logger;
import wssj.co.jp.olioa.utils.Utils;
import wssj.co.jp.olioa.widget.ILoadMoreTopListener;
import wssj.co.jp.olioa.widget.LoadMoreListView;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class ChatFragment extends BaseFragment<IChatView, ChatPresenter> implements IChatView, ILoadMoreTopListener {

    private static String TAG = "ChatFragment";

    private LoadMoreListView mListViewChat;

    private EditText mInputChat;

    private ImageView mButtonSend;

    private ProgressBar mProgressSendChat;

    private List<ChatMessage> mListChat;

    private ChatAdapter mAdapter;

    private DialogProfile mDialogProfile;

    private StoreInfo storeInfo;

    private String lastTime = Constants.EMPTY_STRING;

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
    public boolean isDisplayBottomNavigationMenu() {
        return false;
    }

    @Override
    protected IChatView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        String storeName = getString(R.string.title_screen_chat);
        if (getArguments() != null) {
            storeInfo = getArguments().getParcelable(ListStoreChatFragment.ARG_STORE_INFO);
            if (storeInfo != null && !TextUtils.isEmpty(storeInfo.getName()))
                storeName = storeInfo.getName();
        }
        return storeName;
    }

    @Override
    protected void initViews(View rootView) {
        mListViewChat = rootView.findViewById(R.id.lvStoreFollow);
        mInputChat = (EditText) rootView.findViewById(R.id.etChat);
        mButtonSend = (ImageView) rootView.findViewById(R.id.tvSendChat);
        mProgressSendChat = (ProgressBar) rootView.findViewById(R.id.progressSendChat);
    }

    private String mContent = Constants.EMPTY_STRING;

    @Override
    protected void initAction() {
        mListViewChat.setOnLoadListenerTop(this);
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
                    getPresenter().sendChat(storeInfo.getId(), StringEscapeUtils.escapeJava(mContent.toString()));
                }
                Utils.hideSoftKeyboard(getActivity());
            }
        });

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
        mAdapter.setImageStore(storeInfo.getLogo());
        getPresenter().getHistoryChat(storeInfo.getId(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onGetHistoryChatSuccess(List<ChatMessage> history) {
        if (!Utils.isEmpty(history)) {
            sortListChat(history);
            int size = mListChat.size();
            mListChat.addAll(0, history);
            int newSize = mListChat.size() - size;
            mAdapter.notifyDataSetChanged();
            mListViewChat.setSelection(newSize);
            if (history.size() == Constants.MAX_ITEM_PAGE_CHAT) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mListViewChat.notifyLoadComplete();
                    }
                }, 300);
            } else {
                mListViewChat.stopLoadMore();
            }
        }

        if (mAdapter.getCount() == 0) {
            showTextNoItem(true, getString(R.string.no_conversation));
        } else {
            showTextNoItem(false, null);
        }
    }

    private void sortListChat(List<ChatMessage> history) {
        //lastTime = Constants.EMPTY_STRING;
        for (ChatMessage chatData : history) {
            if (TextUtils.isEmpty(lastTime)) {
                lastTime = DateConvert.formatToString(DateConvert.DATE_FORMAT, chatData.getCreated());
                chatData.setDate(lastTime);
            } else {
                String dateTemp = DateConvert.formatToString(DateConvert.DATE_FORMAT, chatData.getCreated());
                if (!TextUtils.equals(lastTime, dateTemp)) {
                    lastTime = dateTemp;
                    chatData.setDate(dateTemp);
                }
            }
        }

        if (mListChat != null) {
            for (ChatMessage chat : mListChat) {
                if (!TextUtils.isEmpty(chat.getDate())) {
                    String dateOld = chat.getDate();
                    if (TextUtils.equals(lastTime, dateOld)) {
                        chat.setDate(null);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onSendChatSuccess(ChatMessage chatMessage) {
        mInputChat.setText(Constants.EMPTY_STRING);
        mProgressSendChat.setVisibility(View.GONE);
        mButtonSend.setVisibility(View.VISIBLE);
        String time = DateConvert.formatToString(DateConvert.DATE_FORMAT, chatMessage.getCreated());
        if (!lastTime.equals(time)) {
            lastTime = time;
            chatMessage.setDate(time);
        }
        mListChat.add(chatMessage);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSendChatFailure(String message) {
        mProgressSendChat.setVisibility(View.GONE);
        mButtonSend.setVisibility(View.VISIBLE);
        showToast(message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.d(TAG, "removeCallbacks ");
    }

    @Override
    public void onLoadMoreTop(int page) {
        if (mListChat.size() > 0) {
            long lastId = mListChat.get(0).getId();
            getPresenter().getHistoryChat(storeInfo.getId(), lastId);
        }
    }
}
