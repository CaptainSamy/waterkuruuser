package jp.co.wssj.iungo.screens.coupone;

import android.view.View;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;
import jp.co.wssj.iungo.screens.calender.CalenderPresenter;
import jp.co.wssj.iungo.screens.calender.ICalenderView;

/**
 * Created by thang on 12/29/2017.
 */

public class CouponeFragment extends BaseFragment<ICouponeView,CouponePresenter>implements ICouponeView{

    private static String TAG="CouponeFragment";

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_COUPONE;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_coupone;
    }

    @Override
    protected CouponePresenter onCreatePresenter(ICouponeView view) {
        return new CouponePresenter(view);
    }

    @Override
    protected ICouponeView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initAction() {
        super.initAction();
    }
}
