package wssj.co.jp.obis.screens.about;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.screens.IMainView;
import wssj.co.jp.obis.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class AboutFragment extends BaseFragment<IAboutView, AboutPresenter> implements IAboutView {

    private static String TAG = "AboutFragment";

    private TextView mVersion, txtTest;

    public static AboutFragment newInstance(Bundle args) {
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
    public int getNavigationMenuId() {
        return R.id.menu_version;
    }

    @Override
    protected void initViews(View rootView) {
        mVersion = (TextView) rootView.findViewById(R.id.version);
        txtTest = (TextView) rootView.findViewById(R.id.txtScreenTest);
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            txtTest.setText(bundle.getString("name"));
        }
    }
}
