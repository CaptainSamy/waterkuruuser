package wssj.co.jp.point.screens.qa;

import android.view.View;

import wssj.co.jp.point.R;
import wssj.co.jp.point.screens.base.BaseFragment;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class QAFragment extends BaseFragment<IQAView, QAPresenter> implements IQAView, View.OnClickListener {

    private static String TAG = "QAFragment";

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return 0;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_qa;
    }

    @Override
    protected QAPresenter onCreatePresenter(IQAView view) {
        return new QAPresenter(view);
    }

    @Override
    protected IQAView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_qa);
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }

    @Override
    public int getNavigationMenuID() {
        return R.id.menu_question_answer;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);

    }

    @Override
    public void onClick(final View view) {

    }


}
