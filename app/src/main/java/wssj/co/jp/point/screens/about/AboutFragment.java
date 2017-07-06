package wssj.co.jp.point.screens.about;

import android.view.View;
import android.widget.TextView;

import wssj.co.jp.point.R;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;

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
        mVersion.setText(getString(R.string.content_version, "1.0.0"));
    }
}
