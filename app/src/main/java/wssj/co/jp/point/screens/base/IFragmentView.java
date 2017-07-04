package wssj.co.jp.point.screens.base;

/**
 * Created by HieuPT on 4/3/2017.
 */

public interface IFragmentView extends IView {

    void showProgress();

    void hideProgress();

    void backToPreviousScreen();
}
