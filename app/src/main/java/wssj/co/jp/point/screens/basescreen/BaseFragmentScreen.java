package wssj.co.jp.point.screens.basescreen;

/**
 * Created by Nguyen Huu Ta on 26/6/2017.
 */

public class BaseFragmentScreen extends wssj.co.jp.point.screens.base.BaseFragment<IBaseView, BasePresenterScreen> implements IBaseView {
    private static String TAG = "ContactUsFragment";

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
        return 0;
    }

    @Override
    protected BasePresenterScreen onCreatePresenter(IBaseView view) {
        return new BasePresenterScreen(view);
    }

    @Override
    protected IBaseView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        return "";
    }
}
