package wssj.co.jp.obis.screens;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import io.fabric.sdk.android.Fabric;
import wssj.co.jp.obis.App;
import wssj.co.jp.obis.BuildConfig;
import wssj.co.jp.obis.R;
import wssj.co.jp.obis.firebase.FireBaseMsgService;
import wssj.co.jp.obis.model.baseapi.APICallback;
import wssj.co.jp.obis.model.database.DBManager;
import wssj.co.jp.obis.model.entities.StoreInfo;
import wssj.co.jp.obis.model.firebase.NotificationMessage;
import wssj.co.jp.obis.screens.base.BaseFragment;
import wssj.co.jp.obis.screens.changepassword.ChangeUserInfoFragment;
import wssj.co.jp.obis.screens.chat.chatdetail.ChatFragment;
//import wssj.co.jp.obis.screens.comment.CommentFragment;
import wssj.co.jp.obis.screens.dialogerror.DialogMessage;
import wssj.co.jp.obis.screens.groupchat.ListGroupChatFragment;
import wssj.co.jp.obis.screens.groupchat.groupchatdetail.GroupChatDetailFragment;
import wssj.co.jp.obis.screens.liststorecheckedin.ListStoreChatFragment;
import wssj.co.jp.obis.screens.pushnotification.detail.PushNotificationDetailFragment;
import wssj.co.jp.obis.screens.pushnotification.pushlist.PushNotificationFragment;
import wssj.co.jp.obis.screens.scanner.dialog.ConfirmCheckInDialog;
import wssj.co.jp.obis.screens.splash.SplashFragment;
import wssj.co.jp.obis.utils.Constants;
import wssj.co.jp.obis.utils.FragmentBackStackManager;
import wssj.co.jp.obis.utils.Logger;
import wssj.co.jp.obis.utils.NetworkUtils;
import wssj.co.jp.obis.utils.ReflectionUtils;
import wssj.co.jp.obis.utils.VolleySequence;
import wssj.co.jp.obis.widget.CenterTitleToolbar;
import wssj.co.jp.obis.widget.EnhancedBottomNavigationView;

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

    private MainPresenter mPresenter;

    private FragmentBackStackManager mFragmentBackStackManager;

    boolean isRequestFirstNotification = true;

    private TextView mTextUserName;

    private LogoutReceiver mLogoutReceiver;

    private PushNotificationFragment mListPushFragment;

    private ListGroupChatFragment mGroupChat;

    private ListStoreChatFragment mListStoreChatFragment;


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d(TAG, "broadcastReceiver");
            if (mCurrentFragment == null) {
                return;
            }
            Bundle bundle = intent.getBundleExtra(FireBaseMsgService.KEY_NOTIFICATION);
            if (bundle == null) {
                return;
            }
            NotificationMessage notification = bundle.getParcelable(FireBaseMsgService.KEY_NOTIFICATION);
            if (notification == null) {
                return;
            }

            switch (notification.getAction()) {
                case FireBaseMsgService.ACTION_PUSH_CHAT:
                    boolean fragmentChat = mCurrentFragment.getFragmentId() == IMainView.FRAGMENT_LIST_STORE_CHAT || mCurrentFragment.getFragmentId() == IMainView.FRAGMENT_CHAT;
                    if (fragmentChat) {
                        onReloadFragment(mCurrentFragment.getFragmentId());
                    }
                    break;
                case FireBaseMsgService.ACTION_PUSH_GROUP:
                    boolean fragmentGroupChat = mCurrentFragment.getFragmentId() == IMainView.FRAGMENT_GROUP_CHAT || mCurrentFragment.getFragmentId() == IMainView.FRAGMENT_GROUP_CHAT_DETAIL;
                    if (fragmentGroupChat) {
                        onReloadFragment(mCurrentFragment.getFragmentId());
                    }
                    break;
                default:
                    if (mCurrentFragment.getFragmentId() == IMainView.FRAGMENT_PUSH_NOTIFICATION) {
                        onReloadFragment(mCurrentFragment.getFragmentId());
                    }
                    break;
            }

        }
    };

    private BroadcastReceiver broadcastCheckChangeNetwork = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = NetworkUtils.getConnectivityStatusString(context);

            networkNotConnected = status == NetworkUtils.NETWORK_STATUS_NOT_CONNECTED;
            Logger.d(TAG, "#STATUS NETWORK " + networkNotConnected);
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                if (status == NetworkUtils.NETWORK_STATUS_NOT_CONNECTED) {
                    showDialogNetwork();
                } else {
                    if (dialogMessage != null) {
                        dialogMessage.dismissDialogView();
                    }
                    if (mCurrentFragment != null) {
                        onReloadFragment(mCurrentFragment.getFragmentId());
                    }
                }
            }
        }
    };

    private DialogMessage dialogMessage;
    public boolean networkNotConnected;

    private void showDialogNetwork() {
        if (dialogMessage == null) {
            dialogMessage = new DialogMessage(this, new DialogMessage.IOnClickListener() {
                @Override
                public void buttonYesClick() {
                    showDialogNetwork();
                }

                @Override
                public void buttonCancelClick() {

                }
            });
            dialogMessage.setCanceledOnTouchOutside(false);
            dialogMessage.initData("インターネットは接続できませんでした", getString(R.string.OK));
        }
        dialogMessage.show();
    }


    /*
     *
     * case app opened
     * */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Logger.d(TAG, "#onNewIntent " + intent);
        if (intent != null && intent.getExtras() != null) {
            String type = intent.getExtras().getString(FireBaseMsgService.KEY_NOTIFICATION);
            if (type != null) {
                switch (type) {
                    case FireBaseMsgService.ACTION_PUSH_CHAT:
                        displayScreen(IMainView.FRAGMENT_LIST_STORE_CHAT, false, false);
                        break;
                    default:
                        displayScreen(IMainView.FRAGMENT_PUSH_NOTIFICATION, false, false);
                        break;
                }
            }
        }

        if (intent != null && intent.getDataString() != null) {
            String urlScheme = intent.getDataString();
            String[] split = urlScheme.split("url_add_friend=");
            if (split.length == 2) {
                mCodeAutoCheckIn = split[1];
                if (mCodeAutoCheckIn != null && !mCodeAutoCheckIn.isEmpty()) {
                    autoCheckIn();
                }
            }
        }
    }

    private String mCodeAutoCheckIn = Constants.EMPTY_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d(TAG, "#onCreate");
        setContentView(R.layout.activity_main);
        FragmentFactory.init();
        Fabric.with(this, new Crashlytics());
        DBManager.getInstance().init(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION_REFRESH_LIST_PUSH));
        registerReceiver(broadcastCheckChangeNetwork, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        mLogoutReceiver = new LogoutReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mLogoutReceiver, new IntentFilter(ACTION_LOGOUT));
        mWindow = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        App.getInstance().setActivity(this);
        mPresenter = new MainPresenter(this);
        setupFragmentBackStackManager();
        initView();
        initAction();
        String type = null;
        int storeId = -1, groupId = -1, pushId = -1;


        if (getIntent() != null && getIntent().getDataString() != null) {
            //case app not open
            String urlScheme = getIntent().getDataString();
            String[] split = urlScheme.split("url_add_friend=");
            if (split.length == 2) {
                String code = split[1];
                mCodeAutoCheckIn = code;
                //autoCheckIn(code);
            }
        }


        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            type = bundle.getString("type");
            try {
                pushId = Integer.parseInt(bundle.getString("push_id"));
            } catch (Exception e) {
                pushId = -1;
            }
            try {
                storeId = Integer.parseInt(bundle.getString("store_id"));
            } catch (Exception e) {
                storeId = -1;
            }

            try {
                groupId = Integer.parseInt(bundle.getString("group_id"));
            } catch (Exception e) {
                groupId = -1;
            }
        }

        Logger.d(TAG, "#TYPE 4 = " + type);
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(type)) {
            bundle.putInt(SplashFragment.ARG_FRAGMENT_ID, IMainView.FRAGMENT_GROUP_CHAT);
        } else {
            switch (type) {
                case FireBaseMsgService.ACTION_PUSH_CHAT:
                    bundle.putInt(SplashFragment.ARG_FRAGMENT_ID, IMainView.FRAGMENT_LIST_STORE_CHAT);
                    bundle.putInt(SplashFragment.ARG_ID, storeId);
                    break;
                case FireBaseMsgService.ACTION_PUSH_GROUP:
                    bundle.putInt(SplashFragment.ARG_FRAGMENT_ID, IMainView.FRAGMENT_GROUP_CHAT);
                    bundle.putInt(SplashFragment.ARG_ID, groupId);
                    break;
                default:
                    bundle.putInt(SplashFragment.ARG_FRAGMENT_ID, IMainView.FRAGMENT_PUSH_NOTIFICATION);
                    bundle.putInt(SplashFragment.ARG_ID, pushId);
                    break;
            }
        }
        mPresenter.displaySplashScreen(bundle);
    }

    private void autoCheckIn() {
        if (mCurrentFragment instanceof SplashFragment) {
            return;
        }
        String token = mPresenter.getToken();
        if (token == null || token.isEmpty()) {
            return;
        }


        mCurrentFragment.showProgress();
        mPresenter.checkInCode(mCodeAutoCheckIn, new APICallback<StoreInfo>() {
            @Override
            public void onSuccess(StoreInfo storeInfo) {
                mCurrentFragment.hideProgress();
                showDialogStoreCheckin(storeInfo);
            }

            @Override
            public void onFailure(String errorMessage) {
                mCodeAutoCheckIn = Constants.EMPTY_STRING;
                mCurrentFragment.hideProgress();
                showDialogMessage(errorMessage);
            }
        });
    }


    private void showDialogStoreCheckin(StoreInfo storeInfo) {
        ConfirmCheckInDialog mDialog = new ConfirmCheckInDialog(getViewContext(), new ConfirmCheckInDialog.IListenerDismissDialog() {

            @Override
            public void onConfirm(String code) {
                onRequestConfirm(code);
            }

            @Override
            public void onDismissDialog() {
                mCodeAutoCheckIn = Constants.EMPTY_STRING;
            }
        });
        mDialog.initData(storeInfo);
        mDialog.show();
    }

    private void onRequestConfirm(String code) {
        mCurrentFragment.showProgress();
        mPresenter.onConfirm(code, new APICallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                mCurrentFragment.hideProgress();
                mCodeAutoCheckIn = Constants.EMPTY_STRING;
                onReloadFragment(mCurrentFragment.getFragmentId());
                showDialogMessage(getString(R.string.check_in_success));
            }

            @Override
            public void onFailure(String errorMessage) {
                mCurrentFragment.hideProgress();
                mCodeAutoCheckIn = Constants.EMPTY_STRING;
                showDialogMessage(errorMessage);
            }
        });
    }

    private void showDialogMessage(String message) {
        DialogMessage dialogMessage = new DialogMessage(getViewContext(), null);
        dialogMessage.setTitle(getString(R.string.title_dialog_check_in));
        dialogMessage.initData(message, getString(R.string.dialog_button_ok));
        dialogMessage.show();
    }


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
    }

    private void initAction() {
        mNavigationView.setNavigationItemSelectedListener(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCurrentFragment != null) {
                    Runnable topNavigationClickListener = mCurrentFragment.onBackButtonClickListener();
                    if (topNavigationClickListener != null) {
                        topNavigationClickListener.run();
                        return;
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
                    mPresenter.onBottomNavigationButtonClicked(FRAGMENT_PUSH_NOTIFICATION, null);
                    return true;
                case R.id.navigation_group_chat:
                    mPresenter.onBottomNavigationButtonClicked(FRAGMENT_GROUP_CHAT, null);
                    return true;
                case R.id.navigation_chat:
                    mPresenter.onBottomNavigationButtonClicked(FRAGMENT_LIST_STORE_CHAT, null);
                    return true;
//                case R.id.menu_memo:
//                    mPresenter.onCloseDrawableLayout(FRAGMENT_MEMO_MANAGER, true, true, null, menuId);
//                    return true;
                case R.id.menu_push_notification:
                    mPresenter.onCloseDrawableLayout(FRAGMENT_PUSH_NOTIFICATION, true, true, null, menuId);
                    return true;
                case R.id.menu_change_password:
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(ChangeUserInfoFragment.ARG_FROM_MENU, true);
                    mPresenter.onCloseDrawableLayout(FRAGMENT_CHANGE_PASSWORD, true, true, bundle, menuId);
                    return true;
                case R.id.menu_how_to_use:
                    mNavigationView.setCheckedItem(R.id.menu_how_to_use);
                    mPresenter.onCloseDrawableLayout(FRAGMENT_HOW_TO_USE, true, true, null, menuId);
                    return true;
//                case R.id.menu_question_answer:
//                    mPresenter.onCloseDrawableLayout(FRAGMENT_QA, true, true, null, menuId);
//                    return true;
                case R.id.menu_contact_us:
                    mPresenter.onCloseDrawableLayout(FRAGMENT_CONTACT_US, true, true, null, menuId);
                    return true;
                case R.id.menu_term_of_service:
                    mPresenter.onCloseDrawableLayout(FRAGMENT_TERM_OF_SERVICE, true, true, null, menuId);
                    return true;
//                case R.id.menu_privacy_policy:
//                    mPresenter.onCloseDrawableLayout(FRAGMENT_POLICY, true, true, null, menuId);
//                    return true;
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
                case R.id.menu_block_chat:
                    mPresenter.onCloseDrawableLayout(FRAGMENT_BLOCK_CHAT, true, true, null, menuId);
                    break;
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
            case FRAGMENT_PUSH_NOTIFICATION:
                if (mListPushFragment == null) {
                    mListPushFragment = PushNotificationFragment.newInstance(bundle);
                }
                clearBackStack();
                replaceFragment(mListPushFragment, hasAnimation, addToBackStack);
                return;
            case FRAGMENT_GROUP_CHAT:
                if (mGroupChat == null) {
                    mGroupChat = ListGroupChatFragment.newInstance(bundle);
                }
                clearBackStack();
                replaceFragment(mGroupChat, hasAnimation, addToBackStack);
                return;
            case FRAGMENT_LIST_STORE_CHAT:
                if (mListStoreChatFragment == null) {
                    mListStoreChatFragment = ListStoreChatFragment.newInstance(bundle);
                }
                clearBackStack();
                replaceFragment(mListStoreChatFragment, hasAnimation, addToBackStack);
                return;
        }
        replaceFragment(FragmentFactory.getFragment(screenId, bundle), hasAnimation, addToBackStack);
    }

    @Override
    public void switchScreen(int screenId, boolean hasAnimation,
                             boolean addToBackStack, Bundle bundle, View sharedElement) {
        if (sharedElement == null) {
            switchScreen(screenId, hasAnimation, addToBackStack, bundle);
        } else {
//            switch (screenId) {
//                case FRAGMENT_COMMENT:
//                    replaceFragment(CommentFragment.newInstance(bundle), sharedElement);
//                    break;
//            }
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
    public void displayScreen(final int screenId, final boolean hasAnimation,
                              final boolean addToBackStack) {
        displayScreen(screenId, hasAnimation, addToBackStack, null);
    }

    @Override
    public void displayScreen(final int screenId, final boolean hasAnimation,
                              final boolean addToBackStack, final Bundle bundle) {
        switchScreen(screenId, hasAnimation, addToBackStack, bundle);
    }

    @Override
    public void displayScreen(final int screenId, final boolean hasAnimation,
                              final boolean addToBackStack, final Bundle bundle, final View sharedElement) {
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
        mListStoreChatFragment = null;
        mListPushFragment = null;
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
                mToolbar.setShowExtraNavigationButton(true);
                enableDrawerLayout();
            } else {
                mToolbar.setShowExtraNavigationButton(false);
                disableDrawerLayout();
            }
            mBottomNavigationView.getMenu().setGroupEnabled(R.id.navigation_bottom_group, fragment.isEnableBottomNavigationMenu());
            boolean isShowNavigationButton = fragment.isDisplayBackButton();
            if (isShowNavigationButton) {
                mToolbar.setNavigationIcon(R.drawable.ic_back);
            } else {
                mToolbar.setNavigationIcon(null);
            }
//            mToolbar.setShowExtraNavigationButton(fragment.isDisplayExtraNavigationButton());
            mToolbar.setTitleActionBar(fragment.getAppBarTitle());
            mToolbar.setVisibility(fragment.isDisplayActionBar() ? View.VISIBLE : View.GONE);
            mToolbar.setBackgroundColor(fragment.getActionBarColor());
            if (fragment.getNavigationMenuId() == 0) {
                mNavigationView.setCheckedItem(R.id.menu_visible);
            } else {
                mNavigationView.setCheckedItem(fragment.getNavigationMenuId());
            }

            if (!mCodeAutoCheckIn.isEmpty()) {
                autoCheckIn();
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

    private void replaceFragment(BaseFragment fragment, boolean hasAnimation,
                                 boolean addToBackStack) {
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
        FragmentFactory.destroy();
        VolleySequence.getInstance().release();
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(mLogoutReceiver);
        unregisterReceiver(broadcastCheckChangeNetwork);


    }

    public void onReloadFragment(int fragmentId) {
        if (!isAppOnTop()) {
            return;
        }
        switch (fragmentId) {
            case IMainView.FRAGMENT_PUSH_NOTIFICATION:
                if (mListPushFragment != null) {
                    mListPushFragment.onRefresh();
                }
                break;
            case IMainView.FRAGMENT_GROUP_CHAT:
                if (mGroupChat != null) {
                    mGroupChat.onRefresh();
                }
                break;
            case IMainView.FRAGMENT_GROUP_CHAT_DETAIL:
                if (mCurrentFragment instanceof GroupChatDetailFragment) {
                    ((GroupChatDetailFragment) mCurrentFragment).onRefresh();
                }
                break;
            case IMainView.FRAGMENT_LIST_STORE_CHAT:
                if (mListStoreChatFragment != null) {
                    mListStoreChatFragment.onRefresh();
                }
                break;
            case IMainView.FRAGMENT_CHAT:
                if (mCurrentFragment instanceof ChatFragment) {
                    ((ChatFragment) mCurrentFragment).onRefresh();
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

    protected boolean isAppOnTop() {
        ActivityManager am = (ActivityManager) this.getSystemService(Service.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        ActivityManager.RunningTaskInfo running = tasks.get(0);
        String packageName = running.topActivity.getPackageName();
        return packageName.equals(BuildConfig.APPLICATION_ID);
    }
}
