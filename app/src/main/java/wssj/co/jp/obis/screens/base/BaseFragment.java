package wssj.co.jp.obis.screens.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
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

import wssj.co.jp.obis.R;
import wssj.co.jp.obis.screens.IActivityCallback;
import wssj.co.jp.obis.screens.MainActivity;
import wssj.co.jp.obis.screens.dialogerror.DialogMessage;
import wssj.co.jp.obis.utils.Constants;
import wssj.co.jp.obis.utils.FragmentBackStackManager;
import wssj.co.jp.obis.utils.Logger;
import wssj.co.jp.obis.utils.Utils;

/**
 * Created by HieuPT on 3/21/2017.
 */

public abstract class BaseFragment<V extends IFragmentView, P extends FragmentPresenter<V>>
        extends Fragment implements IFragmentView {

    public static final String KEY_SET_BOTTOM_NAVIGATION_ID = "KEY_SET_BOTTOM_NAVIGATION_ID";

    private String TAG = "BaseFragment";

    protected static final String KEY_SET_GLOBAL = "BaseFragment" + "_KEY_SET_GLOBAL";

    private IActivityCallback mActivityCallback;

    private P mPresenter;

    private Activity mActivity;

    private ProgressDialog mProgressDialog;

    private View mRootView;

    private boolean mIsAttached;

    private TextView mTextNoItem;

    private boolean isRefreshWhenBackFragment = false;

    @Override
    public Context getViewContext() {
        return mActivity;
    }

    @Override
    public void onAttach(Activity activity) {
        TAG = getLogTag();
        Logger.i(TAG, "#onAttach" + getInternalLogTag());
        super.onAttach(activity);
        if (activity instanceof IActivityCallback) {
            mActivity = activity;
            mActivityCallback = (IActivityCallback) activity;
            mPresenter = onCreatePresenter(onCreateView());
            mPresenter.onFragmentAttach();
            mIsAttached = true;
        } else {
            throw new RuntimeException("Activity must implement IFragmentCallback");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.i(TAG, "#onCreate" + getInternalLogTag());
        super.onCreate(savedInstanceState);
        mPresenter.onFragmentCreate();
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.i(TAG, "#onCreateView" + getInternalLogTag());
        mPresenter.onFragmentCreateView();
        mActivityCallback.onFragmentResumed(this);
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setCancelable(false);
        if (isRetainState()) {
            if (mRootView == null) {
                mRootView = onCreateViewInternal(inflater, container);
            }
        } else {
            mRootView = onCreateViewInternal(inflater, container);
        }
        if (getArguments() != null) {
            Bundle bundle = getArguments().getBundle(FragmentBackStackManager.KEY_RETURNED_EXTRA_BUNDLE);
            if (bundle != null) {
                onReceiverWhenBackFragment(bundle);
            }
        }
        if (isRefreshWhenBackFragment) {
            isRefreshWhenBackFragment = false;
            onRefreshWhenBackFragment();
        }
        Utils.setupUI(mRootView, mActivity);
        return mRootView;
    }

    private View onCreateViewInternal(LayoutInflater inflater, @Nullable ViewGroup container) {
        View rootView = null;
        int resId = getResourceLayout();
        if (resId > 0) {
            rootView = inflater.inflate(resId, container, false);
            initViews(rootView);
            mTextNoItem = (TextView) rootView.findViewById(R.id.textNoItem);
            initAction();
        }
        initData();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Logger.i(TAG, "#onViewCreated" + getInternalLogTag());
        super.onViewCreated(view, savedInstanceState);
        mPresenter.onFragmentViewCreated();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Logger.i(TAG, "#onActivityCreated" + getInternalLogTag());
        super.onActivityCreated(savedInstanceState);
        mPresenter.onActivityCreated();
    }

    @Override
    public void onStart() {
        Logger.i(TAG, "#onStart" + getInternalLogTag());
        super.onStart();
        mPresenter.onFragmentStart();
    }

    @Override
    public void onResume() {
        Logger.i(TAG, "#onResume" + getInternalLogTag());
        super.onResume();
        mPresenter.onFragmentResume();
    }

    @Override
    public void onPause() {
        Logger.i(TAG, "#onPause" + getInternalLogTag());
        super.onPause();
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (inputManager != null && getActivity().getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
        mPresenter.onFragmentPause();
    }

    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onStop() {
        Logger.i(TAG, "#onStop" + getInternalLogTag());
        super.onStop();
        mPresenter.onFragmentStop();
    }

    @Override
    public void onDestroyView() {
        Logger.i(TAG, "#onDestroyView" + getInternalLogTag());
        super.onDestroyView();
        mPresenter.onFragmentDestroyView();
    }

    @Override
    public void onDestroy() {
        Logger.i(TAG, "#onDestroy" + getInternalLogTag());
        super.onDestroy();
        mPresenter.onFragmentDestroy();
    }

    @Override
    public void onDetach() {
        Logger.i(TAG, "#onDetach" + getInternalLogTag());
        super.onDetach();
        mIsAttached = false;
        mPresenter.onFragmentDetach();
    }

    public boolean isAttached() {
        return mIsAttached;
    }

    @Override
    public final void backToPreviousScreen() {
        Logger.d(TAG, "#backToPreviousScreen");
        mActivity.onBackPressed();
    }

    @Override
    public void backToPreviousScreen(Bundle bundle) {
        Logger.d(TAG, "#backToPreviousScreenWithBundle");
        getActivityCallback().onBackPressed(bundle);
    }

    @Override
    public void showProgress() {
        if (mProgressDialog != null && mPresenter.isViewAttached()) {
            if (getUserVisibleHint()) {
                Logger.i(getLogTag(), "#showProgress");
                mProgressDialog.show();
            }
        }
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && mPresenter.isViewAttached()) {
            Logger.i(TAG, "#hideProgress");
            mProgressDialog.dismiss();
        }
    }

    public String getString(Context context, @StringRes int resId) {
        if (context != null) {
            return context.getString(resId);
        }
        return Constants.EMPTY_STRING;
    }

    @Override
    public void showToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showDialog(String message) {
        DialogMessage dialogMessage = new DialogMessage(getActivityContext(), null);
        dialogMessage.initData(message, getString(getViewContext(), R.string.OK));
        dialogMessage.show();
    }

    public void print(String message) {
        Logger.d(getLogTag(), message);
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

    public boolean isDisplayBackButton() {
        return true;
    }

    public boolean isDisplayIconNotification() {
        return true;
    }

    public boolean isDisplayExtraNavigationButton() {
        return true;
    }

    public Runnable onBackButtonClickListener() {
        return null;
    }

    public int getNavigationBottomId() {
        return 0;
    }

    public int getNavigationMenuId() {
        return 0;
    }

    public boolean isEnableDrawableLayout() {
        return true;
    }

    public void showTextNoItem(boolean isShow, String content) {
//        getActivityCallback().showTextNoItem(isShow, content);
        if (mTextNoItem != null) {
            if (isShow) {
                mTextNoItem.setText(content);
                mTextNoItem.setVisibility(View.VISIBLE);
            } else {
                mTextNoItem.setVisibility(View.GONE);
            }
        }
    }

    protected boolean isRetainState() {
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

    protected void initViews(View view) {
    }

    protected void initAction() {
    }

    protected void initData() {
    }

    protected void onRefreshWhenBackFragment() {

    }

    public void onReceiverWhenBackFragment(Bundle bundle) {

    }

    @SuppressWarnings("unchecked")
    protected <T> T findViewById(View rootView, @IdRes int id) {
        return Utils.findViewById(rootView, id);
    }

    private String getInternalLogTag() {
//        String tag = getLogTag();
//        return tag != null ? tag : getClass().getSimpleName();
        return "";
    }

    protected abstract String getLogTag();

    public abstract int getFragmentId();

    protected abstract int getResourceLayout();

    protected abstract P onCreatePresenter(V view);

    protected abstract V onCreateView();

    private static final class ProgressDialog extends Dialog {

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

    public void flagRefreshWhenBackFragment() {
        isRefreshWhenBackFragment = true;
    }

    public MainActivity getMainActivity() {
        if (mActivity instanceof MainActivity) {
            return (MainActivity) mActivity;
        }
        return null;
    }
}
