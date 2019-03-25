package wssj.co.jp.olioa.screens.base;

import android.os.Bundle;

/**
 * Created by HieuPT on 4/3/2017.
 */

public interface IFragmentView extends IView {

    void showDialog(String message);

    void showProgress();

    void hideProgress();

    void showToast(String message);

    void backToPreviousScreen();

    void backToPreviousScreen(Bundle bundle);
}
