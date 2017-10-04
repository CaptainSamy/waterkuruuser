package jp.co.wssj.iungo.screens.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.IActivityCallback;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.utils.Utils;

/**
 * Created by HieuPT on 3/21/2017.
 */

public abstract class BaseFragment<V extends IFragmentView, P extends FragmentPresenter<V>>
        extends Fragment implements IFragmentView {

    private static final String TAG = "BaseFragment";

    private IActivityCallback mActivityCallback;

    private P mPresenter;

    private Activity mActivity;

    private ProgressDialog mProgressDialog;

    private TextView mTextNoItem;

    @Override
    public Context getViewContext() {
        return mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        Logger.i(TAG, "#onAttach: " + getInternalLogTag());
        super.onAttach(activity);
        if (activity instanceof IActivityCallback) {
            mActivity = activity;
            mActivityCallback = (IActivityCallback) activity;
            mPresenter = onCreatePresenter(onCreateView());
            mPresenter.onFragmentAttach();
        } else {
            throw new RuntimeException("Activity must implement IFragmentCallback");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.i(TAG, "#onCreate: " + getInternalLogTag());
        super.onCreate(savedInstanceState);
        mPresenter.onFragmentCreate();
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.i(TAG, "#onCreateView: " + getInternalLogTag());
        mPresenter.onFragmentCreateView();
        mActivityCallback.onFragmentResumed(this);
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setCancelable(false);
        View rootView = null;
        int resId = getResourceLayout();
        if (resId > 0) {
            rootView = inflater.inflate(resId, container, false);
            initViews(rootView);
            initAction();
            mTextNoItem = (TextView) rootView.findViewById(R.id.textNoItem);
        }
        initData();
        if (rootView != null && getActivity() != null) {
            Utils.setupUI(rootView, getActivity());
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Logger.i(TAG, "#onViewCreated: " + getInternalLogTag());
        super.onViewCreated(view, savedInstanceState);
        mPresenter.onFragmentViewCreated();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Logger.i(TAG, "#onActivityCreated: " + getInternalLogTag());
        super.onActivityCreated(savedInstanceState);
        mPresenter.onActivityCreated();
    }

    @Override
    public void onStart() {
        Logger.i(TAG, "#onStart: " + getInternalLogTag());
        super.onStart();
        mPresenter.onFragmentStart();
    }

    @Override
    public void onResume() {
        Logger.i(TAG, "#onResume: " + getInternalLogTag());
        super.onResume();
        mPresenter.onFragmentResume();
    }

    @Override
    public void onPause() {
        Logger.i(TAG, "#onPause: " + getInternalLogTag());
        super.onPause();
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (inputManager != null && getActivity().getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
        mPresenter.onFragmentPause();
    }

    @Override
    public void onStop() {
        Logger.i(TAG, "#onStop: " + getInternalLogTag());
        super.onStop();
        mPresenter.onFragmentStop();
    }

    @Override
    public void onDestroyView() {
        Logger.i(TAG, "#onDestroyView: " + getInternalLogTag());
        super.onDestroyView();
        mPresenter.onFragmentDestroyView();
    }

    @Override
    public void onDestroy() {
        Logger.i(TAG, "#onDestroy: " + getInternalLogTag());
        super.onDestroy();
        mPresenter.onFragmentDestroy();
    }

    @Override
    public void onDetach() {
        Logger.i(TAG, "#onDetach: " + getInternalLogTag());
        super.onDetach();
        mPresenter.onFragmentDetach();
    }

    @Override
    public final void backToPreviousScreen() {
        Logger.d(TAG, "#backToPreviousScreen");
        mActivity.onBackPressed();
    }

    @Override
    public final void showProgress() {
        if (mProgressDialog != null && mPresenter.isViewAttached()) {
            mProgressDialog.show();
        }
    }

    @Override
    public final void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && mPresenter.isViewAttached()) {
            mProgressDialog.dismiss();
        }
    }

    public void showToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void showTextNoItem(String text, View view) {
        mTextNoItem.setVisibility(View.VISIBLE);
        view.setVisibility(View.INVISIBLE);
        mTextNoItem.setText(text);
    }

    public void hideTextNoItem(boolean isShow, View view) {
        mTextNoItem.setVisibility(isShow ? View.VISIBLE : View.GONE);
        view.setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }

    public boolean isEnableBottomNavigationMenu() {
        return true;
    }

    public String getAppBarTitle() {
        return Constants.EMPTY_STRING;
    }

    public boolean isDisplayActionBar() {
        return true;
    }

    public int getActionBarColor() {
        return ContextCompat.getColor(getActivityContext(), R.color.colorBackground_Actionbar);
    }

    public boolean isDisplayNavigationButton() {
        return true;
    }

    public boolean isDisplayIconNotification() {
        return true;
    }

    public boolean isDisplayExtraNavigationButton() {
        return true;
    }

    public Runnable onNavigationClickListener() {
        return null;
    }

    public int getNavigationBottomId() {
        return 0;
    }

    public boolean isEnableDrawableLayout() {
        return true;
    }

    protected Context getActivityContext() {
        return mActivity;
    }

    protected boolean isActivityFinishing() {
        return mActivity.isFinishing();
    }

    protected IActivityCallback getActivityCallback() {
        return mActivityCallback;
    }

    protected P getPresenter() {
        return mPresenter;
    }

    protected void initViews(View rootView) {
    }

    protected void initAction() {
    }

    protected void initData() {
    }

    private String getInternalLogTag() {
        String tag = getLogTag();
        return tag != null ? tag : getClass().getSimpleName();
    }

    protected abstract String getLogTag();

    public abstract int getFragmentId();

    protected abstract int getResourceLayout();

    protected abstract P onCreatePresenter(V view);

    protected abstract V onCreateView();

    private class ProgressDialog extends Dialog {

        private ProgressDialog(Context context) {
            super(context);
            setCancelable(false);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_progress);
            Window window = getWindow();
            if (window != null) {
                getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            }
        }
    }

    public int getMenuBottomID() {
        return 0;
    }

    public int getNavigationMenuID() {
        return 0;
    }

    public static final int MENU_HOME = 1;

    public static final int MENU_MY_STAMP = 2;

    public static final int MENU_TIME_LINE = 4;

    public static final int MENU_STORE_FOLLOW = 3;

}
