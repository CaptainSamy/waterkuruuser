package wssj.co.jp.olioa.screens.groupchat.groupchatdetail;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.entities.GroupChat;
import wssj.co.jp.olioa.model.entities.GroupChatMessage;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.groupchat.adapter.GroupChatAdapter;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.DateConvert;
import wssj.co.jp.olioa.utils.Logger;
import wssj.co.jp.olioa.utils.Utils;
import wssj.co.jp.olioa.widget.ILoadMoreTopListener;
import wssj.co.jp.olioa.widget.LoadMoreListView;

public class GroupChatDetailFragment extends BaseFragment<IGroupChatDetailView, GroupChatDetailPresenter> implements IGroupChatDetailView, ILoadMoreTopListener {
    public static final String TAG = "GroupChatDetailFragment";
    public static final String ARG_GROUP = "arg_group";
    private LoadMoreListView mListViewChat;

    private EditText mInputChat;

    private ImageView mButtonSend;

    private ProgressBar mProgressSendChat;

    private List<GroupChatMessage> mListChat;

    private GroupChatAdapter mAdapter;

    private GroupChat groupInfo;

    private String lastTime = Constants.EMPTY_STRING;

    public static GroupChatDetailFragment newInstance(Bundle b) {
        GroupChatDetailFragment fragment = new GroupChatDetailFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_GROUP_CHAT_DETAIL;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_group_detail_chat;
    }

    @Override
    protected GroupChatDetailPresenter onCreatePresenter(IGroupChatDetailView view) {
        return new GroupChatDetailPresenter(view);
    }

    @Override
    protected IGroupChatDetailView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        String storeName = getString(R.string.title_screen_chat);
        if (getArguments() != null) {
            groupInfo = getArguments().getParcelable(ARG_GROUP);
            if (groupInfo != null && !TextUtils.isEmpty(groupInfo.getName()))
                storeName = groupInfo.getName();
        }
        return storeName;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return false;
    }

    @Override
    protected void initViews(View rootView) {
        mListViewChat = rootView.findViewById(R.id.lvStoreFollow);
        mInputChat = rootView.findViewById(R.id.etChat);
        mButtonSend = rootView.findViewById(R.id.tvSendChat);
        mProgressSendChat = rootView.findViewById(R.id.progressSendChat);
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
                    getPresenter().sendChat(groupInfo.getId(), mContent);// StringEscapeUtils.escapeJava
                }
                Utils.hideSoftKeyboard(getActivity());
            }
        });

    }

    @Override
    protected void initData() {

        if (groupInfo == null) {
            showDialog("GroupInfo null");
            return;
        }

        mListChat = new ArrayList<>();
        mAdapter = new GroupChatAdapter(getMainActivity(), mListChat);
        mListViewChat.setAdapter(mAdapter);
        getPresenter().getHistoryChat(groupInfo.getId(), 0);
    }

    @Override
    public void onGetHistoryChatSuccess(final List<GroupChatMessage> history) {
        if (!Utils.isEmpty(history)) {
            sortListChat(history);
            int size = mListChat.size();
            mListChat.addAll(0, history);

            final int newSize = mListChat.size() - size;
            mListViewChat.post(new Runnable() {

                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                    mListViewChat.setSelection(newSize - 3);
                    if (history.size() < Constants.MAX_ITEM_PAGE_CHAT) {
                        mListViewChat.stopLoadMore();
                    } else {
                        mListViewChat.notifyLoadComplete();
                    }
                }
            });

        }
        if (mAdapter.getCount() == 0) {
            showTextNoItem(true, getString(R.string.no_conversation));
        } else {
            showTextNoItem(false, null);
        }
    }

    private void sortListChat(List<GroupChatMessage> history) {
        for (GroupChatMessage chatData : history) {
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
            for (GroupChatMessage chat : mListChat) {
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
    public void onSendChatSuccess(GroupChatMessage chatMessage) {
        mInputChat.setText(Constants.EMPTY_STRING);
        mProgressSendChat.setVisibility(View.GONE);
        mButtonSend.setVisibility(View.VISIBLE);
        if (chatMessage != null) {
            String time = DateConvert.formatToString(DateConvert.DATE_FORMAT, chatMessage.getCreated());
            if (!lastTime.equals(time)) {
                lastTime = time;
                chatMessage.setDate(time);
            }
            mListChat.add(chatMessage);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
            showLog("onLoadMoreTop with request");
            long lastId = mListChat.get(0).getId();
            getPresenter().getHistoryChat(groupInfo.getId(), lastId);
        }
    }

    public void onRefresh() {
        mListChat.clear();
        getPresenter().getHistoryChat(groupInfo.getId(), 0);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (groupInfo != null) {
            if (getMainActivity() != null) {
                getMainActivity().onReloadFragment(IMainView.FRAGMENT_LIST_STORE_CHAT);
            }
            getPresenter().saveLastTimeReadChat(groupInfo.getId());
        }
    }
}
