package wssj.co.jp.olioa.screens.home;

import android.os.Bundle;
import android.view.View;

import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.checkin.ManageStampFragment;
import wssj.co.jp.olioa.screens.scanner.ScannerFragment;
import wssj.co.jp.olioa.screens.waitstoreconfirm.WaitStoreConfirmFragment;
import wssj.co.jp.olioa.screens.switcher.SwitcherFragment;

/**
 * Created by Nguyen Huu Ta on 20/6/2017.
 */

public class HomeFragment extends SwitcherFragment<IHomeView, HomePresenter>
        implements IHomeView {

    private static final String TAG = "HomeFragment";

    private ScannerFragment mScannerFragment;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getNavigationBottomId() {
        return 0;// R.id.navigation_home;
    }

    @Override
    protected boolean isRetainState() {
        return true;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_HOME;
    }

    @Override
    protected HomePresenter onCreatePresenter(IHomeView view) {
        return new HomePresenter(view);
    }

    @Override
    protected IHomeView onCreateView() {
        return this;
    }

    @Override
    public void displayStampManagerScreen(Bundle bundle) {
        displayFragment(ManageStampFragment.newInstance(bundle));
    }

    @Override
    public void displayScanQRCodeScreen() {
        if (mScannerFragment == null) {
            mScannerFragment = new ScannerFragment();
        }
        displayFragment(mScannerFragment);
    }

    @Override
    public void displayWaitStoreConfirmScreen(Bundle bundle) {
        displayFragment(WaitStoreConfirmFragment.newInstance(bundle));
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        displayScanQRCodeScreen();
    }
}
