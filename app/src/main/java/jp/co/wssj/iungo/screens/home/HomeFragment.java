package jp.co.wssj.iungo.screens.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 20/6/2017.
 */

public class HomeFragment extends BaseFragment<IHomeView, HomePresenter> implements IHomeView {

    private static final String TAG = "HomeFragment";

    private static final int REQUEST_CODE = 100;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_HOME;
    }

    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_home;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }

    @Override
    public boolean isDisplayActionBar() {
        return false;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_home;
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
    protected void initViews(View rootView) {
        super.initViews(rootView);
        getPresenter().onHomeNavigationButtonClicked();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initAction() {
        super.initAction();
    }

    @Override
    public void requestCameraPermission() {
        requestPermissions(new String[]{
                Manifest.permission.CAMERA
        }, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isSuccess = permissions.length > 0 && grantResults != null && grantResults.length > 0;
        if (requestCode == REQUEST_CODE && permissions != null && isSuccess) {
            if (Manifest.permission.CAMERA.equals(permissions[0]) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                switchScreen(IMainView.FRAGMENT_SCANNER, true, false, null);
            } else {
                Toast.makeText(getActivityContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void switchScreen(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle) {
        getActivityCallback().displayScreen(screenId, hasAnimation, addToBackStack, bundle);
    }

    @Override
    public int getMenuBottomID() {
        return MENU_HOME;
    }
}
