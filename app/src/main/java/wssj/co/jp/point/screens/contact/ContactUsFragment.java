package wssj.co.jp.point.screens.contact;

import wssj.co.jp.point.R;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class ContactUsFragment extends BaseFragment<IContactUsView, ContactUsPresenter> implements IContactUsView {

    private static String TAG = "ContactUsFragment";

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_CONTACT_US;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_contact_us;
    }

    @Override
    protected ContactUsPresenter onCreatePresenter(IContactUsView view) {
        return new ContactUsPresenter(view);
    }

    @Override
    protected IContactUsView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_contact);
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return super.isDisplayBottomNavigationMenu();
    }

    @Override
    public int getNavigationMenuID() {
        return R.id.menu_contact_us;
    }
}
