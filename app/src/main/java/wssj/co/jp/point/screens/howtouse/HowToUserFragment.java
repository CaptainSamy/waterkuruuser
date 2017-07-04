package wssj.co.jp.point.screens.howtouse;

import wssj.co.jp.point.R;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class HowToUserFragment extends BaseFragment<IHowToUseView, HowToUsePresenter> implements IHowToUseView {

    private static String TAG = "HowToUserFragment";

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_HOW_TO_USE;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_how_to_use;
    }

    @Override
    protected HowToUsePresenter onCreatePresenter(IHowToUseView view) {
        return new HowToUsePresenter(view);
    }

    @Override
    protected IHowToUseView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_how_to_use);
    }

    @Override
    public int getNavigationMenuID() {
        return R.id.menu_how_to_use;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }
}
