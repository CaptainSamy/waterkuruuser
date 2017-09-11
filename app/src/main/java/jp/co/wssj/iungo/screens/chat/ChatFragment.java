package jp.co.wssj.iungo.screens.chat;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class ChatFragment extends BaseFragment<IChatView, ChatPresenter> implements IChatView {

    private static String TAG = "ChatFragment";

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
    public int getMenuBottomID() {
        return MENU_MY_REQUEST;
    }
}
