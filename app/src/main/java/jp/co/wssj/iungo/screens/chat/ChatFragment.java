package jp.co.wssj.iungo.screens.chat;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.chat.ChatResponse;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.chat.adapter.ChatAdapter;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class ChatFragment extends BaseFragment<IChatView, ChatPresenter> implements IChatView {

    private static String TAG = "ChatFragment";

    private ListView mListViewChat;

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
    public int getNavigationBottomId() {
        return R.id.navigation_another;
    }

    @Override
    public int getMenuBottomID() {
        return MENU_MY_REQUEST;
    }

    @Override
    protected void initViews(View rootView) {
        mListViewChat = (ListView) rootView.findViewById(R.id.lvChat);
    }

    @Override
    protected void initAction() {
        mListViewChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivityCallback().displayScreen(IMainView.FRAGMENT_CHAT_DETAIL, true, true);
            }
        });
    }

    @Override
    protected void initData() {
        List<ChatResponse> listChat = new ArrayList<>();
        ChatResponse response = new ChatResponse("Du lịch", System.currentTimeMillis());
        listChat.add(response);
        response = new ChatResponse("Thời trang", System.currentTimeMillis());
        listChat.add(response);
        response = new ChatResponse("Công nghệ", System.currentTimeMillis());
        listChat.add(response);
        response = new ChatResponse("Sức khỏe", System.currentTimeMillis());
        listChat.add(response);
        ChatAdapter adapter = new ChatAdapter(getActivityContext(), listChat);
        mListViewChat.setAdapter(adapter);
    }
}
