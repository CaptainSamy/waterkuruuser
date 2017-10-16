package jp.co.wssj.iungo.screens;

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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import jp.co.wssj.iungo.BuildConfig;
import jp.co.wssj.iungo.DialogNotification;
import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.model.database.DBManager;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.screens.about.AboutFragment;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.base.PagedFragment;
import jp.co.wssj.iungo.screens.changepassword.ChangePasswordByCodeFragment;
import jp.co.wssj.iungo.screens.changepassword.ChangePasswordFragment;
import jp.co.wssj.iungo.screens.chat.chatdetail.ChatFragment;
import jp.co.wssj.iungo.screens.comment.CommentFragment;
import jp.co.wssj.iungo.screens.contact.ContactUsFragment;
import jp.co.wssj.iungo.screens.howtouse.HowToUserFragment;
import jp.co.wssj.iungo.screens.introduction.IntroductionFragment;
import jp.co.wssj.iungo.screens.listcard.ListCardFragmentDetail;
import jp.co.wssj.iungo.screens.liststorecheckedin.ListStoreCheckedInFragment;
import jp.co.wssj.iungo.screens.login.LoginFragment;
import jp.co.wssj.iungo.screens.memomanager.MemoManagerFragment;
import jp.co.wssj.iungo.screens.note.UserMemoFragment;
import jp.co.wssj.iungo.screens.polycy.PolicyFragment;
import jp.co.wssj.iungo.screens.primary.PrimaryFragment;
import jp.co.wssj.iungo.screens.pushnotification.PushNotificationListFragment;
import jp.co.wssj.iungo.screens.pushnotification.detail.PushNotificationDetailFragment;
import jp.co.wssj.iungo.screens.pushnotificationforstore.PushNotificationForServiceCompanyFragment;
import jp.co.wssj.iungo.screens.pushobject.ObjectPush;
import jp.co.wssj.iungo.screens.qa.QAFragment;
import jp.co.wssj.iungo.screens.qadetail.QADetailFragment;
import jp.co.wssj.iungo.screens.registeraccount.RegisterAccountFragment;
import jp.co.wssj.iungo.screens.resetpassword.ResetPasswordFragment;
import jp.co.wssj.iungo.screens.splash.SplashFragment;
import jp.co.wssj.iungo.screens.termofservice.fragment.TermOfServiceFragment;
import jp.co.wssj.iungo.screens.termofservice.fragment.TermOfServiceNoMenuBottom;
import jp.co.wssj.iungo.screens.timeline.TimeLineFragment;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.FragmentBackStackManager;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.utils.ReflectionUtils;
import jp.co.wssj.iungo.utils.VolleySequence;
import jp.co.wssj.iungo.widget.CenterTitleToolbar;

public class MainActivity extends AppCompatActivity
        implements IMainView, IActivityCallback, BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    public static final String ACTION_LOGOUT = BuildConfig.APPLICATION_ID + ".LOGOUT";

    private static final String TAG = "MainActivity";

    private BottomNavigationView mBottomNavigationView;

    private BaseFragment mCurrentFragment;

    private CenterTitleToolbar mToolbar;

    private NavigationView mNavigationView;

    private DrawerLayout mDrawerLayout;

    private Window mWindow;

    private ImageView imageNotification, mImageUser;

    private MainPresenter mPresenter;

    private FragmentBackStackManager mFragmentBackStackManager;

    private PrimaryFragment mPrimaryFragment;

    private TextView mTextNoItem;

    private DialogNotification mDialogNotification;

    private int mTotalNotificationUnRead;

    boolean isRequestFirstNotification = true;

    private TextView mTextUserName;

    private LogoutReceiver mLogoutReceiver;

    private boolean mIsAppStart;

    private boolean mIsNewIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWindow = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWindow.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        mPresenter = new MainPresenter(this);
        setupFragmentBackStackManager();
        initView();
        initAction();
        mIsNewIntent = false;
        checkStartNotification(getIntent());
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
            mPresenter.getListPushNotificationUnRead(Constants.INIT_PAGE, Constants.LIMIT);
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
        mImageUser = (ImageView) header.findViewById(R.id.imageMenu);
        mNavigationView.setItemIconTintList(null);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNavigationView.setItemIconTintList(null);
        ReflectionUtils.setBottomNavigationViewShiftingMode(mBottomNavigationView, false);
        mToolbar = (CenterTitleToolbar) findViewById(R.id.tool_bar);
        mToolbar.setExtraNavigationIcon(R.drawable.ic_menu);
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setShowIconNotificationButton(false);
        setSupportActionBar(mToolbar);
        mTextNoItem = (TextView) findViewById(R.id.textNoItem);
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

        mToolbar.setIconNotificationClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mDialogNotification != null) {
                    mDialogNotification.show();
                }
            }
        });
    }

    @Override
    public void showListPushNotificationUnRead(List<NotificationMessage> list, final int page, final int totalPage, final int totalNotificationUnRead) {
        mTotalNotificationUnRead = totalNotificationUnRead;
        mToolbar.setNumberNotificationUnRead(mTotalNotificationUnRead);
        Logger.d(TAG, "showListPushNotificationUnRead " + totalNotificationUnRead);
        if (mDialogNotification == null) {
            mDialogNotification = new DialogNotification(MainActivity.this, list, imageNotification);
        }
        mDialogNotification.setmCallback(new DialogNotification.IOnItemClick() {

            @Override
            public void onItemClick(NotificationMessage message) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(PushNotificationDetailFragment.NOTIFICATION_ARG, message);
                switchScreen(IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL, true, true, bundle);
            }

            @Override
            public void endOfListView(int page, int limit) {
                mPresenter.getListPushNotificationUnRead(page, Constants.LIMIT);
            }
        });
        mDialogNotification.addListNotification(page, totalPage, list);
        if (list != null && list.size() > 0 && mDialogNotification.getDialogShowing()) {
            List<Long> listPushId = new ArrayList<>();
            for (NotificationMessage notificationMessage : list) {
                if (notificationMessage.getStatusRead() != Constants.STATUS_VIEW && notificationMessage.getStatusRead() != Constants.STATUS_READ) {
                    listPushId.add(notificationMessage.getPushId());
                }
            }
            if (listPushId != null && listPushId.size() > 0) {
                mPresenter.setListPushUnRead(listPushId, Constants.STATUS_VIEW);
            }
        }

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
                case R.id.navigation_stamp:
//                case R.id.navigation_home:
                case R.id.navigation_another:
                case R.id.navigation_timeline:
                    mPresenter.onBottomNavigationButtonClicked(menuId);
                    replaceFragment(mPrimaryFragment, true, true);
                    return true;
                case R.id.menu_memo:
                    mPresenter.onCloseDrawableLayout(IMainView.FRAGMENT_MEMO_MANAGER, true, true, null, menuId);
                    return true;
                case R.id.menu_push_notification:
                    mPresenter.onCloseDrawableLayout(IMainView.FRAGMENT_PUSH_NOTIFICATION_LIST, true, true, null, menuId);
                    return true;
                case R.id.menu_change_password:
                    mPresenter.onCloseDrawableLayout(IMainView.FRAGMENT_CHANGE_PASSWORD, true, true, null, menuId);
                    return true;
                case R.id.menu_how_to_use:
                    mNavigationView.setCheckedItem(R.id.menu_how_to_use);
                    mPresenter.onCloseDrawableLayout(IMainView.FRAGMENT_HOW_TO_USE, true, true, null, menuId);
                    return true;
                case R.id.menu_question_answer:
                    mPresenter.onCloseDrawableLayout(IMainView.FRAGMENT_QA, true, true, null, menuId);
                    return true;
                case R.id.menu_contact_us:
                    mPresenter.onCloseDrawableLayout(IMainView.FRAGMENT_CONTACT_US, true, true, null, menuId);
                    return true;
                case R.id.menu_term_of_service:
                    mPresenter.onCloseDrawableLayout(IMainView.FRAGMENT_TERM_OF_SERVICE, true, true, null, menuId);
                    return true;
                case R.id.menu_privacy_policy:
                    mPresenter.onCloseDrawableLayout(IMainView.FRAGMENT_POLICY, true, true, null, menuId);
                    return true;
                case R.id.menu_version:
                    mPresenter.onCloseDrawableLayout(IMainView.FRAGMENT_ABOUT, true, true, null, menuId);
                    return true;
                case R.id.menu_logout:
                    mDrawerLayout.closeDrawer(GravityCompat.END);
                    mPresenter.onLogout();
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
    public void onEnableDrawerLayout() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onDisableDrawerLayout() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void switchScreen(int screenId, boolean hasAnimation,
                             boolean addToBackStack, Bundle bundle) {
        switch (screenId) {
            case FRAGMENT_SPLASH_SCREEN:
                replaceFragment(new SplashFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_INTRODUCTION_SCREEN:
                replaceFragment(new IntroductionFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_LOGIN:
                replaceFragment(new LoginFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_RESET_PASSWORD:
                replaceFragment(new ResetPasswordFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_CHANGE_PASSWORD_CODE:
                replaceFragment(new ChangePasswordByCodeFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_PRIMARY:
                mPrimaryFragment = PrimaryFragment.newInstance(bundle);
                replaceFragment(mPrimaryFragment, hasAnimation, addToBackStack);
                break;
            case FRAGMENT_REGISTER_ACCOUNT:
                replaceFragment(new RegisterAccountFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_MEMO_MANAGER:
                replaceFragment(new MemoManagerFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_USER_MEMO:
                replaceFragment(UserMemoFragment.newInstance(bundle), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_LIST_CARD:
                replaceFragment(ListCardFragmentDetail.newInstance(bundle), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_TERM_OF_SERVICE:
                replaceFragment(new TermOfServiceFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_TERM_OF_SERVICE_N0_BOTTOM:
                replaceFragment(new TermOfServiceNoMenuBottom(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_PUSH_NOTIFICATION_LIST:
                replaceFragment(new PushNotificationListFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_PUSH_NOTIFICATION_DETAIL:
                replaceFragment(PushNotificationDetailFragment.newInstance(bundle), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_LIST_STORE_CHECKED_IN:
                replaceFragment(ListStoreCheckedInFragment.newInstance(bundle), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_HOW_TO_USE:
                replaceFragment(new HowToUserFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_QA:
                replaceFragment(new QAFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_CONTACT_US:
                replaceFragment(new ContactUsFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_CHANGE_PASSWORD:
                replaceFragment(new ChangePasswordFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_POLICY:
                replaceFragment(new PolicyFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_ABOUT:
                replaceFragment(new AboutFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_NOTIFICATION_FOR_SERVICE_COMPANY:
                replaceFragment(PushNotificationForServiceCompanyFragment.newInstance(bundle), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_QA_DETAIL:
                replaceFragment(QADetailFragment.newInstance(bundle), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_CHAT:
                replaceFragment(ChatFragment.newInstance(bundle), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_COMMENT:
                replaceFragment(CommentFragment.newInstance(bundle), hasAnimation, addToBackStack);
                break;
        }
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
    public void setSelectedPage(int itemId) {
        if (mPrimaryFragment != null) {
            List<PagedFragment> fragments = mPrimaryFragment.getFragments();
            if (fragments != null) {
                for (int i = 0; i < fragments.size(); i++) {
                    PagedFragment fragment = fragments.get(i);
                    if (fragment.getNavigationBottomId() == itemId) {
                        mPrimaryFragment.setSelectedPage(i);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void displayScreen(final int screenId, final boolean hasAnimation, final boolean addToBackStack) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                displayScreen(screenId, hasAnimation, addToBackStack, null);
            }
        }, Constants.DELAY_TIME_TRANSFER_FRAGMENT);
    }

    @Override
    public void displayScreen(final int screenId, final boolean hasAnimation,
                              final boolean addToBackStack, final Bundle bundle) {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                switchScreen(screenId, hasAnimation, addToBackStack, bundle);
            }
        }, Constants.TIME_DELAY_CLOSED_NAVIGATION_MENU);
    }

    @Override
    public void displayScreen(final int screenId, final boolean hasAnimation, final boolean addToBackStack, final Bundle bundle, final View sharedElement) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                switchScreen(screenId, hasAnimation, addToBackStack, bundle, sharedElement);
            }
        }, Constants.TIME_DELAY_CLOSED_NAVIGATION_MENU);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mIsNewIntent = true;
        checkStartNotification(intent);
    }

    private void checkStartNotification(Intent intent) {
        if (intent != null) {
            Bundle b = intent.getExtras();
            if (b != null && !TextUtils.isEmpty(b.getString("push_id"))) {
                NotificationMessage notificationMessage = getItemPush(b);
                if (mDialogNotification != null) {
                    mDialogNotification.addNotification(notificationMessage);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(PushNotificationDetailFragment.NOTIFICATION_ARG, notificationMessage);
                bundle.putBoolean(PushNotificationDetailFragment.FLAG_FROM_ACTIVITY, true);
                switch (notificationMessage.getAction()) {
                    case Constants.PushNotification.TYPE_TIME_LINE:
                    case Constants.PushNotification.TYPE_STEP_PUSH:
                        switchScreen(IMainView.FRAGMENT_PRIMARY, true, true, null);
                        break;
                    default:
                        switchScreen(IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL, true, true, bundle);
                }

            } else if (intent.getScheme() != null && intent.getScheme().equals("iungo")) {
                final String storeCode = intent.getData().getQueryParameter("code");
                if (storeCode.contains(Constants.KEY_FAST)) {
                    String code = storeCode.replace(Constants.KEY_FAST, Constants.EMPTY_STRING);
                    mPresenter.mappingUserWithStoreFast(code);
                } else {
                    ObjectPush objectPush = new ObjectPush(storeCode);
                    Gson gson = new Gson();
                    String json = gson.toJson(objectPush);
                    mPresenter.savePush(json);
                    if (mPresenter.isLogin()) {
                        mPresenter.mappingUserWithStore();
                    } else {
                        switchScreen(IMainView.FRAGMENT_INTRODUCTION_SCREEN, true, true, null);
                    }
                }
            } else if (!mIsAppStart) {
                mPresenter.displaySplashScreen();
            }
        }
    }

    public NotificationMessage getItemPush(Bundle b) {
        String pushId = b.getString("push_id");
        String title = b.getString("title");
        String content = b.getString("body");
        String action = b.getString("type");
        int stampId = 0;
        if (!TextUtils.isEmpty(action)) {
            String[] splitAction = action.split(Constants.SPLIT);
            if (splitAction != null) {
                action = splitAction[0];
                if (splitAction.length == 2) {
                    try {
                        stampId = Integer.parseInt(splitAction[1]);
                    } catch (NumberFormatException e) {
                        Logger.d(TAG, "NumberFormatException");
                    }
                }
            }
        }
        return new NotificationMessage(Long.parseLong(pushId), title, content, action, stampId);
    }

    @Override
    public void onMappingUserStoreFastSuccess() {
        if (mIsNewIntent) {
            setSelectedPage(R.id.navigation_stamp);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(PrimaryFragment.KEY_SCREEN_ID, PrimaryFragment.SCREEN_LIST_SERVICE_COMPANY);
            switchScreen(IMainView.FRAGMENT_PRIMARY, true, true, bundle);
        }
    }

    @Override
    public void onMappingUserStoreFastFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayScanCodeScreen() {
        Bundle bundle = new Bundle();
        bundle.putInt(PrimaryFragment.KEY_SCREEN_ID, PrimaryFragment.SCREEN_TIMELINE);
        displayScreen(FRAGMENT_PRIMARY, true, false, bundle);
    }

    @Override
    public void clearBackStack() {
        mFragmentBackStackManager.clearBackStack();
    }

    @Override
    public void onLogout() {
        clearBackStack();
        displayScreen(IMainView.FRAGMENT_INTRODUCTION_SCREEN, true, false);
        if (mDialogNotification != null) {
            mDialogNotification.clearData();
        }
        isRequestFirstNotification = true;
        mToolbar.setNumberNotificationUnRead(0);
        mTextUserName.setText(Constants.EMPTY_STRING);
    }

    @Override
    public void onFragmentResumed(BaseFragment fragment) {
        Logger.d(TAG, "#onFragmentResumed");
        showTextNoItem(false, null);
        if (fragment != null) {
            if (fragment instanceof TimeLineFragment && isRequestFirstNotification) {
                isRequestFirstNotification = false;
                mPresenter.getListPushNotificationUnRead(Constants.INIT_PAGE, Constants.LIMIT);
            }
            disablePushUpView();
            if (fragment.isGlobal()) {
                if (fragment.isEnableDrawableLayout()) {
                    mPresenter.onEnableDrawerLayout();
                } else {
                    mPresenter.onDisableDrawerLayout();
                }
                mCurrentFragment = fragment;
                mBottomNavigationView.setVisibility(fragment.isDisplayBottomNavigationMenu() ? View.VISIBLE : View.GONE);
                int navigationBottomId = fragment.getNavigationBottomId();
                if (navigationBottomId != 0) {
                    mBottomNavigationView.setSelectedItemId(navigationBottomId);
                }
                mBottomNavigationView.getMenu().setGroupEnabled(R.id.navigation_bottom_group, fragment.isEnableBottomNavigationMenu());
                boolean isShowNavigationButton = fragment.isDisplayNavigationButton();
                if (isShowNavigationButton) {
                    mToolbar.setNavigationIcon(R.drawable.ic_back);
                } else {
                    mToolbar.setNavigationIcon(null);
                }
                mToolbar.setShowExtraNavigationButton(fragment.isDisplayExtraNavigationButton());
                mToolbar.setShowIconNotificationButton(fragment.isDisplayIconNotification());
                mToolbar.setTitleActionBar(fragment.getAppBarTitle());
                mToolbar.setVisibility(fragment.isDisplayActionBar() ? View.VISIBLE : View.GONE);
                int actionBarColor = fragment.getActionBarColor();
                mToolbar.setBackgroundColor(actionBarColor);
                if (fragment.getNavigationMenuId() == 0) {
                    mNavigationView.setCheckedItem(R.id.menu_visible);
                } else {
                    mNavigationView.setCheckedItem(fragment.getNavigationMenuId());
                }
            }
        }
    }

    @Override
    public void setSelectedNavigationBottom(int id) {
        mBottomNavigationView.setSelectedItemId(id);
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
    public void showTextNoItem(boolean isShow, String content) {
        if (isShow) {
            mTextNoItem.setText(TextUtils.isEmpty(content) ? Constants.EMPTY_STRING : content);
            mTextNoItem.setVisibility(View.VISIBLE);
        } else {
            mTextNoItem.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsAppStart = false;
        VolleySequence.getInstance().release();
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(broadcastReceiver);
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(mLogoutReceiver);

    }

    private class LogoutReceiver extends BroadcastReceiver {

        private static final String TAG = "LogoutReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d(TAG, "#onReceive");
            if (ACTION_LOGOUT.equals(intent.getAction())) {
                onLogout();
            }
        }
    }

}
