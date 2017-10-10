package jp.co.wssj.iungo.screens.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.base.IWrapperFragment;
import jp.co.wssj.iungo.screens.base.IWrapperFragmentController;
import jp.co.wssj.iungo.screens.base.PagedFragment;
import jp.co.wssj.iungo.screens.checkin.ManageStampFragment;
import jp.co.wssj.iungo.screens.scanner.ScannerFragment;
import jp.co.wssj.iungo.screens.waitstoreconfirm.WaitStoreConfirmFragment;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by Nguyen Huu Ta on 20/6/2017.
 */

public class HomeFragment extends PagedFragment<IHomeView, HomePresenter>
        implements IHomeView, PagedFragment.IOnPageSelectChangeListener, IWrapperFragment {

    private static final String TAG = "HomeFragment";

    private PagedFragment mCurrentFragment;

    private FragmentManager mFragmentManager;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_home;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected boolean isRetainState() {
        return true;
    }

    @Override
    public String getPageTitle(Context context) {
        if (mCurrentFragment != null) {
            return mCurrentFragment.getPageTitle(context);
        }
        return super.getPageTitle(context);
    }

    @Override
    public void refresh() {
        if (mCurrentFragment != null) {
            mCurrentFragment.refresh();
        }
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
    public void onAttachFragment(Fragment childFragment) {
        Logger.d(getLogTag(), "#onAttachFragment");
        if (childFragment instanceof PagedFragment) {
            ((PagedFragment) childFragment).setPagerFragmentCallback(getPagerFragmentCallback());
        }
        if (childFragment instanceof IWrapperFragmentController) {
            ((IWrapperFragmentController) childFragment).setWrapperFragment(this);
        }
    }

    @Override
    protected void initViews(View rootView) {
        mFragmentManager = getChildFragmentManager();
        getPresenter().showFragment();
    }

    @Override
    protected void initAction() {
        addOnPageSelectChangeListener(this);
    }

    private void replaceFragment(PagedFragment fragment) {
        if (fragment != null && mCurrentFragment != fragment) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.home_fragment_container, fragment)
                    .commitAllowingStateLoss();
            mCurrentFragment = fragment;
            getActivityCallback().setAppBarTitle(fragment.getPageTitle(getActivityContext()));
        }
    }

    @Override
    public void onPageSelected() {
        if (mCurrentFragment != null) {
            mCurrentFragment.notifyPageSelected();
        }
    }

    @Override
    public void onPageUnselected() {
        if (mCurrentFragment != null) {
            mCurrentFragment.notifyPageUnselected();
        }
    }

    @Override
    public void displayStampManagerScreen(Bundle bundle) {
        ManageStampFragment fragment = ManageStampFragment.newInstance(bundle);
        replaceFragment(fragment);
    }

    @Override
    public void displayScanQRCodeScreen(Bundle bundle) {
        ScannerFragment fragment = new ScannerFragment();
        replaceFragment(fragment);
    }

    @Override
    public void displayWaitStoreConfirmScreen(Bundle bundle) {
        WaitStoreConfirmFragment fragment = WaitStoreConfirmFragment.newInstance(bundle);
        replaceFragment(fragment);
    }

    @Override
    public void displayScreen(PagedFragment fragment) {
        replaceFragment(fragment);
    }
}
