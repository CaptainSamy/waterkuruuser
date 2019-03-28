package wssj.co.jp.olioa.screens;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import wssj.co.jp.olioa.App;
import wssj.co.jp.olioa.BuildConfig;
import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.database.DBManager;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.comment.CommentFragment;
import wssj.co.jp.olioa.screens.liststorecheckedin.ListStoreChatFragment;
import wssj.co.jp.olioa.screens.liststorecheckedin.ListStorePushFragment;
import wssj.co.jp.olioa.screens.pushnotification.detail.PushNotificationDetailFragment;
import wssj.co.jp.olioa.screens.timeline.timelinetotal.TimeLineFragment;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.FragmentBackStackManager;
import wssj.co.jp.olioa.utils.Logger;
import wssj.co.jp.olioa.utils.ReflectionUtils;
import wssj.co.jp.olioa.utils.VolleySequence;
import wssj.co.jp.olioa.widget.CenterTitleToolbar;
import wssj.co.jp.olioa.widget.EnhancedBottomNavigationView;

public class MainActivity extends AppCompatActivity
        implements IMainView, IActivityCallback, BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    public static final String ACTION_LOGOUT = BuildConfig.APPLICATION_ID + ".LOGOUT";

    private static final String TAG = "MainActivity";

    private EnhancedBottomNavigationView mBottomNavigationView;

    private View mLineBottom;

    private BaseFragment mCurrentFragment;

    private CenterTitleToolbar mToolbar;

    private NavigationView mNavigationView;

    private DrawerLayout mDrawerLayout;

    private Window mWindow;

    private ImageView imageNotification;

    private MainPresenter mPresenter;

    private FragmentBackStackManager mFragmentBackStackManager;

    boolean isRequestFirstNotification = true;

    private TextView mTextUserName;

    private LogoutReceiver mLogoutReceiver;

    private boolean mIsAppStart;

    private ListStorePushFragment mListStorePushFragment;

    private TimeLineFragment mTimelineFragment;

    private ListStoreChatFragment mListStoreChatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentFactory.init();
        mWindow = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        App.getInstance().setActivity(this);
        mPresenter = new MainPresenter(this);
        setupFragmentBackStackManager();
        mPresenter.displaySplashScreen();
        initView();
        initAction();
        Fabric.with(this, new Crashlytics());
        DBManager.getInstance().init(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION_REFRESH_LIST_PUSH));
        mLogoutReceiver = new LogoutReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mLogoutReceiver, new IntentFilter(ACTION_LOGOUT));
        mIsAppStart = true;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d(TAG, "broadcastReceiver");
            // mPresenter.getListPushNotification(Constants.INIT_PAGE, Constants.LIMIT);
        }
    };

    private void setupFragmentBackStackManager() {
        mFragmentBackStackManager = new FragmentBackStackManager(getSupportFragmentManager(), R.id.main_container);
        mFragmentBackStackManager.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = mNavigationView.getHeaderView(0);
        mTextUserName = (TextView) header.findViewById(R.id.textUserName);
        mNavigationView.setItemIconTintList(null);
        mBottomNavigationView = (EnhancedBottomNavigationView) findViewById(R.id.navigation);
        mLineBottom = findViewById(R.id.lineBelow);
        mBottomNavigationView.setItemIconTintList(null);
        ReflectionUtils.setBottomNavigationViewShiftingMode(mBottomNavigationView, false);
        mToolbar = (CenterTitleToolbar) findViewById(R.id.tool_bar);
        mToolbar.setExtraNavigationIcon(R.drawable.ic_menu);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setShowIconNotificationButton(false);
        setSupportActionBar(mToolbar);
        imageNotification = (ImageView) findViewById(R.id.iconTest);
    }

    private void initAction() {
        mNavigationView.setNavigationItemSelectedListener(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCurrentFragment != null) {
                    Runnable topNavigationClickListener = mCurrentFragment.onNavigationClickListener();
                    if (topNavigationClickListener != null) {
                        topNavigationClickListener.run();
                    }
                    mPresenter.onBackPress();

                }
            }
        });
        mToolbar.setExtraNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPresenter.onOpenDrawableLayout();
            }
        });
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayErrorMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onViewAttached();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d(TAG, "onStop");
        mPresenter.onViewDetached();
    }

    @Override
    public void onBackPressed() {
        Logger.i(TAG, "#onBackPressed");
        if (mNavigationView != null && mDrawerLayout != null && mNavigationView.isShown()) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            boolean isConsumed = false;
            if (mCurrentFragment != null) {
                isConsumed = mCurrentFragment.onBackPressed();
            }
            if (!isConsumed) {
                if (!mFragmentBackStackManager.popBackStackImmediate()) {
                    mPresenter.onDispatchFinishActivity();
                }
            }
        }
    }

    @Override
    public void onBackPressed(Bundle bundle) {
        Logger.i(TAG, "#onBackPressedWithBundle");
        if (!mFragmentBackStackManager.popBackStackImmediate(bundle)) {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Logger.d(TAG, "#onNavigationItemSelected");
        if (mCurrentFragment != null) {
            int menuId = item.getItemId();
            switch (menuId) {
                case R.id.navigation_push:
                    mPresenter.onBottomNavigationButtonClicked(FRAGMENT_LIST_STORE_PUSH, null);
                    return true;
                case R.id.navigation_timeline:
                    mPresenter.onBottomNavigationButtonClicked(FRAGMENT_TIMELINE, null);
                    return true;
                case R.id.navigation_chat:
                    mPresenter.onBottomNavigationButtonClicked(FRAGMENT_LIST_STORE_CHAT, null);
                    return true;
                case R.id.menu_memo:
                    mPresenter.onCloseDrawableLayout(FRAGMENT_MEMO_MANAGER, true, true, null, menuId);
                    return true;
                case R.id.menu_push_notification:
                    mPresenter.onCloseDrawableLayout(FRAGMENT_PUSH_NOTIFICATION, true, true, null, menuId);
                    return true;
                case R.id.menu_change_password:
                    mPresenter.onCloseDrawableLayout(FRAGMENT_CHANGE_PASSWORD, true, true, null, menuId);
                    return true;
                case R.id.menu_how_to_use:
                    mNavigationView.setCheckedItem(R.id.menu_how_to_use);
                    mPresenter.onCloseDrawableLayout(FRAGMENT_HOW_TO_USE, true, true, null, menuId);
                    return true;
                case R.id.menu_question_answer:
                    mPresenter.onCloseDrawableLayout(FRAGMENT_QA, true, true, null, menuId);
                    return true;
                case R.id.menu_contact_us:
                    mPresenter.onCloseDrawableLayout(FRAGMENT_CONTACT_US, true, true, null, menuId);
                    return true;
                case R.id.menu_term_of_service:
                    mPresenter.onCloseDrawableLayout(FRAGMENT_TERM_OF_SERVICE, true, true, null, menuId);
                    return true;
                case R.id.menu_privacy_policy:
                    mPresenter.onCloseDrawableLayout(FRAGMENT_POLICY, true, true, null, menuId);
                    return true;
                case R.id.menu_version:
                    mPresenter.onCloseDrawableLayout(FRAGMENT_ABOUT, true, true, null, menuId);
                    return true;
                case R.id.menu_scan:
                    mPresenter.onCloseDrawableLayout(FRAGMENT_SCANNER, true, true, null, menuId);
                    return true;
                case R.id.menu_logout:
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                    mPresenter.logout();
                    DBManager.getInstance().clearDatabase();
                    return true;
            }
        }
        return false;
    }

    @Override
    public void onOpenDrawableLayout() {
        Logger.i(TAG, "#onOpenDrawableLayout");
        mDrawerLayout.openDrawer(GravityCompat.END);

    }

    @Override
    public void onCloseDrawableLayout(final int screenId, final boolean hasAnimation,
                                      final boolean addToBackStack, final Bundle bundle, int navigationId) {
        Logger.i(TAG, "#onCloseDrawableLayout");
        mDrawerLayout.closeDrawer(GravityCompat.END);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                switchScreen(screenId, hasAnimation, addToBackStack, bundle);
            }
        }, Constants.TIME_DELAY_CLOSED_NAVIGATION_MENU);

    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void enableDrawerLayout() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void disableDrawerLayout() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void switchScreen(int screenId, boolean hasAnimation,
                             boolean addToBackStack, Bundle bundle) {

        switch (screenId) {
            case FRAGMENT_LIST_STORE_PUSH:
                if (mListStorePushFragment == null) {
                    mListStorePushFragment = new ListStorePushFragment();
                }
                clearBackStack();
                replaceFragment(mListStorePushFragment, hasAnimation, addToBackStack);
                return;
            case FRAGMENT_TIMELINE:
                if (mTimelineFragment == null) {
                    mTimelineFragment = new TimeLineFragment();
                }
                clearBackStack();
                replaceFragment(mTimelineFragment, hasAnimation, addToBackStack);
                return;
            case FRAGMENT_LIST_STORE_CHAT:
                if (mListStoreChatFragment == null) {
                    mListStoreChatFragment = new ListStoreChatFragment();
                }
                clearBackStack();
                replaceFragment(mListStoreChatFragment, hasAnimation, addToBackStack);
                return;
        }
        replaceFragment(FragmentFactory.getFragment(screenId, bundle), hasAnimation, addToBackStack);
    }

    @Override
    public void switchScreen(int screenId, boolean hasAnimation, boolean addToBackStack, Bundle bundle, View sharedElement) {
        if (sharedElement == null) {
            switchScreen(screenId, hasAnimation, addToBackStack, bundle);
        } else {
            switch (screenId) {
                case FRAGMENT_COMMENT:
                    replaceFragment(CommentFragment.newInstance(bundle), sharedElement);
                    break;
            }
        }
    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void displayScreen(final int screenId, final boolean hasAnimation, final boolean addToBackStack) {
        displayScreen(screenId, hasAnimation, addToBackStack, null);
    }

    @Override
    public void displayScreen(final int screenId, final boolean hasAnimation,
                              final boolean addToBackStack, final Bundle bundle) {
        switchScreen(screenId, hasAnimation, addToBackStack, bundle);
    }

    @Override
    public void displayScreen(final int screenId, final boolean hasAnimation, final boolean addToBackStack, final Bundle bundle, final View sharedElement) {
        switchScreen(screenId, hasAnimation, addToBackStack, bundle, sharedElement);
    }

    @Override
    public void displayScanCodeScreen() {
        displayScreen(FRAGMENT_TIMELINE, true, false);
    }

    @Override
    public void clearBackStack() {
        mFragmentBackStackManager.clearBackStack();
    }

    @Override
    public void logout() {
        FragmentFactory.destroy();
        clearBackStack();
        displayScreen(FRAGMENT_INTRODUCTION_SCREEN, true, false);
        isRequestFirstNotification = true;
        mToolbar.setNumberNotificationUnRead(0);
        mTextUserName.setText(Constants.EMPTY_STRING);
    }

    @Override
    public void onFragmentResumed(BaseFragment fragment) {
        Logger.d(TAG, "#onFragmentResumed");
        if (fragment != null) {
            disablePushUpView();
            if (fragment.isEnableDrawableLayout()) {
                mPresenter.onEnableDrawerLayout();
            } else {
                mPresenter.onDisableDrawerLayout();
            }
            mCurrentFragment = fragment;
            int isShowBottom = fragment.isDisplayBottomNavigationMenu() ? View.VISIBLE : View.GONE;
            mBottomNavigationView.setVisibility(isShowBottom);
            mLineBottom.setVisibility(isShowBottom);
            int navigationBottomId = fragment.getNavigationBottomId();
            if (navigationBottomId != 0) {
                mBottomNavigationView.setSelectedItemIdWithoutNotify(navigationBottomId);
            }
            mBottomNavigationView.getMenu().setGroupEnabled(R.id.navigation_bottom_group, fragment.isEnableBottomNavigationMenu());
            boolean isShowNavigationButton = fragment.isDisplayBackButton();
            if (isShowNavigationButton) {
                mToolbar.setNavigationIcon(R.drawable.ic_back);
            } else {
                mToolbar.setNavigationIcon(null);
            }
            mToolbar.setShowExtraNavigationButton(fragment.isDisplayExtraNavigationButton());
            mToolbar.setShowIconNotificationButton(fragment.isDisplayIconNotification());
            mToolbar.setTitleActionBar(fragment.getAppBarTitle());
            mToolbar.setVisibility(fragment.isDisplayActionBar() ? View.VISIBLE : View.GONE);
            mToolbar.setBackgroundColor(fragment.getActionBarColor());
            if (fragment.getNavigationMenuId() == 0) {
                mNavigationView.setCheckedItem(R.id.menu_visible);
            } else {
                mNavigationView.setCheckedItem(fragment.getNavigationMenuId());
            }
        }
    }

    @Override
    public void setAppBarTitle(String title) {
        mToolbar.setTitleActionBar(title);
    }

    private void disablePushUpView() {
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void replaceFragment(BaseFragment fragment, boolean hasAnimation, boolean addToBackStack) {
        if (fragment != null && !isFinishing()) {
            if (mCurrentFragment == null || mCurrentFragment.getFragmentId() != fragment.getFragmentId() || fragment instanceof PushNotificationDetailFragment) {
                mFragmentBackStackManager.replaceFragment(fragment, hasAnimation, addToBackStack);
            }
        }
    }

    private void replaceFragment(BaseFragment fragment, View sharedElement) {
        if (fragment != null && !isFinishing()) {
            if (mCurrentFragment == null || mCurrentFragment.getFragmentId() != fragment.getFragmentId()) {
                mFragmentBackStackManager.replaceFragment(fragment, sharedElement);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsAppStart = false;
        FragmentFactory.destroy();
        VolleySequence.getInstance().release();
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(mLogoutReceiver);

    }

    public void onReloadData(int fragmentId) {
        switch (fragmentId) {
            case IMainView.FRAGMENT_LIST_STORE_PUSH:
                if (mListStorePushFragment != null) {
                    mListStorePushFragment.setReloadData(true);
                }
                break;
        }
    }

    private class LogoutReceiver extends BroadcastReceiver {

        private static final String TAG = "LogoutReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d(TAG, "#onReceive");
            if (ACTION_LOGOUT.equals(intent.getAction())) {
                logout();
            }
        }
    }

}
