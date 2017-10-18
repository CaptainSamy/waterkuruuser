package jp.co.wssj.iungo.screens.home;

import android.os.Bundle;

import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.checkin.ManageStampFragment;
import jp.co.wssj.iungo.screens.scanner.ScannerFragment;
import jp.co.wssj.iungo.screens.switcher.SwitcherFragment;
import jp.co.wssj.iungo.screens.waitstoreconfirm.WaitStoreConfirmFragment;

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

//    @Override
//    public int getNavigationBottomId() {
//        return R.id.navigation_home;
//    }

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
}
