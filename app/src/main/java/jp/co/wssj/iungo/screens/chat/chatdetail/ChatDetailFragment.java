package jp.co.wssj.iungo.screens.chat.chatdetail;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconEditText;
import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.chat.ChatResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.chat.adapter.ChatDetailAdapter;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class ChatDetailFragment extends BaseFragment<IChatDetailView, ChatDetailPresenter> implements IChatDetailView {

    private static String TAG = "ChatDetailFragment";

    private ListView mListViewChat;

    private EmojiconEditText mInputChat;

    private TextView mButtonSend;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_CHAT_DETAIL;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_chat_detail;
    }

    @Override
    protected ChatDetailPresenter onCreatePresenter(IChatDetailView view) {
        return new ChatDetailPresenter(view);
    }

    @Override
    protected IChatDetailView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_chat_detail);
    }

    @Override
    protected void initViews(View rootView) {
        mListViewChat = (ListView) rootView.findViewById(R.id.lvChat);
        mInputChat = (EmojiconEditText) rootView.findViewById(R.id.etChat);
        mButtonSend = (TextView) rootView.findViewById(R.id.tvSend);
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
                    mButtonSend.setBackground(getResources().getDrawable(R.drawable.icon_send));
                } else {
                    mButtonSend.setEnabled(true);
                    mButtonSend.setBackground(getResources().getDrawable(R.drawable.icon_send_enable));
                }
            }
        });
        mButtonSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ChatResponse.Chat chat = new ChatResponse.Chat(Constants.EMPTY_STRING, mInputChat.getText().toString(), true, System.currentTimeMillis());
                mListChat.add(chat);
                mAdapter.notifyDataSetChanged();
                mInputChat.setText(Constants.EMPTY_STRING);
                Utils.hideSoftKeyboard(getActivity());
                scrollEndOfListView();
            }
        });
    }

    private void scrollEndOfListView() {
        mListViewChat.post(new Runnable() {

            @Override
            public void run() {
                mListViewChat.setSelection(mAdapter.getCount() - 1);
            }
        });
    }

    private List<ChatResponse.Chat> mListChat;

    private ChatDetailAdapter mAdapter;

    @Override
    protected void initData() {
        ChatResponse response = new ChatResponse("Du lịch", System.currentTimeMillis());
        mListChat = response.getListChat();
        mAdapter = new ChatDetailAdapter(getActivityContext(), mListChat);
        mListViewChat.setAdapter(mAdapter);
    }
}
