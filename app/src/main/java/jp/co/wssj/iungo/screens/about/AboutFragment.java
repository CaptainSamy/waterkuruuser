package jp.co.wssj.iungo.screens.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class AboutFragment extends BaseFragment<IAboutView, AboutPresenter> implements IAboutView {

    private static String TAG = "AboutFragment";

    private TextView mVersion;

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_ABOUT;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_version;
    }

    @Override
    protected AboutPresenter onCreatePresenter(IAboutView view) {
        return new AboutPresenter(view);
    }

    @Override
    protected IAboutView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.menu_version);
    }

    @Override
    public int getNavigationMenuID() {
        return R.id.menu_version;
    }

    @Override
    protected void initViews(View rootView) {
        mVersion = (TextView) rootView.findViewById(R.id.version);
    }

    @Override
    protected void initData() {

        PackageInfo pInfo;
        try {
            pInfo = getActivityContext().getPackageManager().getPackageInfo(getActivityContext().getPackageName(), 0);
            String version = pInfo.versionName;
            mVersion.setText(getString(R.string.content_version, version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}