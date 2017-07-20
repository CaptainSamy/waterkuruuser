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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.michael.easydialog.EasyDialog;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.firebase.FirebaseMsgService;
import jp.co.wssj.iungo.model.firebase.NotificationMessage;
import jp.co.wssj.iungo.screens.about.AboutFragment;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.changepassword.ChangePasswordByCodeFragment;
import jp.co.wssj.iungo.screens.changepassword.ChangePasswordFragment;
import jp.co.wssj.iungo.screens.checkin.ManageStampFragment;
import jp.co.wssj.iungo.screens.contact.ContactUsFragment;
import jp.co.wssj.iungo.screens.home.HomeFragment;
import jp.co.wssj.iungo.screens.howtouse.HowToUserFragment;
import jp.co.wssj.iungo.screens.introduction.IntroductionFragment;
import jp.co.wssj.iungo.screens.listcard.ListCardFragment;
import jp.co.wssj.iungo.screens.listservicecompany.ListServiceCompanyFragment;
import jp.co.wssj.iungo.screens.liststorecheckedin.ListStoreCheckedInFragment;
import jp.co.wssj.iungo.screens.login.LoginFragment;
import jp.co.wssj.iungo.screens.memomanager.MemoManagerFragment;
import jp.co.wssj.iungo.screens.note.UserMemoFragment;
import jp.co.wssj.iungo.screens.polycy.PolicyFragment;
import jp.co.wssj.iungo.screens.pushnotification.PushNotificationAdapter;
import jp.co.wssj.iungo.screens.pushnotification.PushNotificationListFragment;
import jp.co.wssj.iungo.screens.pushnotification.detail.PushNotificationDetailFragment;
import jp.co.wssj.iungo.screens.qa.QAFragment;
import jp.co.wssj.iungo.screens.registeraccount.RegisterAccountFragment;
import jp.co.wssj.iungo.screens.resetpassword.ResetPasswordFragment;
import jp.co.wssj.iungo.screens.scanner.ScannerFragment;
import jp.co.wssj.iungo.screens.splash.SplashFragment;
import jp.co.wssj.iungo.screens.termofservice.fragment.TermOfServiceFragment;
import jp.co.wssj.iungo.screens.termofservice.fragment.TermOfServiceNoMenuBottom;
import jp.co.wssj.iungo.screens.waitstoreconfirm.WaitStoreConfirmFragment;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.FragmentBackStackManager;
import jp.co.wssj.iungo.utils.Logger;
import jp.co.wssj.iungo.utils.Utils;
import jp.co.wssj.iungo.widget.CenterTitleToolbar;

public class MainActivity extends AppCompatActivity
        implements IMainView, IActivityCallback, BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private BottomNavigationView mBottomNavigationView;

    private BaseFragment mCurrentFragment;

    private CenterTitleToolbar mToolbar;

    private NavigationView mNavigationView;

    private DrawerLayout mDrawerLayout;

    private Window mWindow;

    private MainPresenter mPresenter;

    private FragmentBackStackManager mFragmentBackStackManager;

    private PushNotificationAdapter mPushNotificationAdapter;

    private List<NotificationMessage> mListNotification;

    private EasyDialog mEasyDialog;

    private ListView mListViewNotification;

    private View mRootViewNotification;

    private TextView mTextNoItem;

    private int mTotalNotificationUnRead;

    boolean isRequestFirstNotification = true;

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

        checkStartNotification(getIntent());
        Fabric.with(this, new Crashlytics());
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION_SERVICE_ACTIVITY));

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Logger.d(TAG, "#onTokenRefresh " + refreshedToken);
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
        mNavigationView.setItemIconTintList(null);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNavigationView.setItemIconTintList(null);
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

        mToolbar.setIconNotificationClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showListNotification();
            }
        });
    }

    ImageView imageNotification;

    public void showListNotification() {
        mPresenter.getListPushNotificationUnRead(Constants.INIT_PAGE, Constants.LIMIT);
        if (mPushNotificationAdapter == null) return;
        if (mRootViewNotification == null) {
            mRootViewNotification = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tooltip, null);
            mTextNoItem = (TextView) mRootViewNotification.findViewById(R.id.textNoItem);
            mListViewNotification = (ListView) mRootViewNotification.findViewById(R.id.listView);
            mListViewNotification.setVerticalScrollBarEnabled(false);
            mListViewNotification.setAdapter(mPushNotificationAdapter);
            mEasyDialog = new EasyDialog(MainActivity.this)
                    .setLayout(mRootViewNotification)
                    .setGravity(EasyDialog.GRAVITY_BOTTOM)
                    .setBackgroundColor(MainActivity.this.getResources().getColor(android.R.color.white))
                    .setLocationByAttachedView(imageNotification)
                    .setAnimationTranslationShow(EasyDialog.DIRECTION_X, Constants.DURATION_DIALOG_NOTIFICATION, getResources().getDisplayMetrics().widthPixels, 0)
                    .setAnimationTranslationDismiss(EasyDialog.DIRECTION_X, Constants.DURATION_DIALOG_NOTIFICATION, 0, -getResources().getDisplayMetrics().widthPixels)
                    .setTouchOutsideDismiss(true)
                    .setMatchParent(false)
                    .setMargin(Utils.convertDpToPixel(MainActivity.this, Constants.MARGIN_LEFT), 0, Utils.convertDpToPixel(MainActivity.this, Constants.MARGIN_RIGHT), Utils.convertDpToPixel(MainActivity.this, Constants.MARGIN_BOTTOM))
                    .setOutsideColor(getResources().getColor(R.color.outside_color_trans));

        }
        mEasyDialog.show();
        mListViewNotification.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotificationMessage message = (NotificationMessage) parent.getAdapter().getItem(position);
                mToolbar.setNumberNotificationUnRead(--mTotalNotificationUnRead);
                mListNotification.remove(position);
                List<NotificationMessage> messageList = new ArrayList<>();
                messageList.add(message);
                mEasyDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PushNotificationDetailFragment.NOTIFICATION_ARG, message);
                displayScreen(IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL, true, true, bundle);
            }
        });
        if (mListNotification != null && mListNotification.size() > 0) {
            mListViewNotification.setVisibility(View.VISIBLE);
            mTextNoItem.setVisibility(View.GONE);
        } else {
            mTextNoItem.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showListPushNotificationUnRead(List<NotificationMessage> list, final int page, final int totalPage, final int totalNotificationUnRead) {
        mTotalNotificationUnRead = totalNotificationUnRead;
        mToolbar.setNumberNotificationUnRead(mTotalNotificationUnRead);
        if (list != null) {
            if (mPushNotificationAdapter == null) {
                mListNotification = new ArrayList<>();
                mPushNotificationAdapter = new PushNotificationAdapter(this, R.layout.item_push_notification, mListNotification);
            }
            if (page == 1) {
                mListNotification.clear();
                mListNotification.addAll(list);
            } else {
                mListNotification.addAll(list);
            }
            mPushNotificationAdapter.notifyDataSetChanged();
            mPushNotificationAdapter.setListenerEndOfListView(new PushNotificationAdapter.IEndOfListView() {

                @Override
                public void onEndOfListView() {
                    if (page < totalPage) {
                        mPresenter.getListPushNotificationUnRead(page + 1, Constants.LIMIT);
                    }
                }
            });
        }
        if (mListViewNotification != null && mTextNoItem != null) {
            if (mListNotification != null && mListNotification.size() > 0) {
                mListViewNotification.setVisibility(View.VISIBLE);
                mTextNoItem.setVisibility(View.GONE);
            } else {
                mTextNoItem.setVisibility(View.VISIBLE);
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
        mPresenter.onViewDetached();
    }

    @Override
    public void onBackPressed() {
        if (mNavigationView != null && mDrawerLayout != null && mNavigationView.isShown()) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            if (!mFragmentBackStackManager.popBackStackImmediate()) {
                finish();
            }

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Logger.d(TAG, "#onNavigationItemSelected");
        if (mCurrentFragment != null) {
            int menuId = item.getItemId();
            switch (menuId) {
                case R.id.navigation_stamp:
                    if (mCurrentFragment.getMenuBottomID() != BaseFragment.MENU_MY_STAMP) {
                        replaceFragment(new ListServiceCompanyFragment(), true, true);
                    }
                    return true;
                case R.id.navigation_home:
                    if (mCurrentFragment.getMenuBottomID() != BaseFragment.MENU_HOME) {
                        replaceFragment(new HomeFragment(), true, true);
                    }
                    return true;
                case R.id.navigation_another:
                    if (mCurrentFragment.getMenuBottomID() != BaseFragment.MENU_MY_REQUEST) {
                        replaceFragment(new MemoManagerFragment(), true, true);
                    }
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
    public void onEnableDrawableLayout() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onDisableDrawableLayout() {
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
            case FRAGMENT_REGISTER_ACCOUNT:
                replaceFragment(new RegisterAccountFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_STAMP:
                replaceFragment(new ListServiceCompanyFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_MEMO_MANAGER:
                replaceFragment(new MemoManagerFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_SCANNER:
                replaceFragment(new ScannerFragment(), hasAnimation, addToBackStack);
                break;
            case FRAGMENT_WAIT_STORE_CONFIRM:
                replaceFragment(WaitStoreConfirmFragment.newInstance(bundle), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_MANAGER_STAMP:
                replaceFragment(ManageStampFragment.newInstance(bundle), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_USER_MEMO:
                replaceFragment(UserMemoFragment.newInstance(bundle), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_LIST_CARD:
                replaceFragment(ListCardFragment.newInstance(bundle), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_TERM_OF_SERVICE:
                replaceFragment(new TermOfServiceFragment(), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_TERM_OF_SERVICE_N0_BOTTOM:
                replaceFragment(new TermOfServiceNoMenuBottom(), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_PUSH_NOTIFICATION_LIST:
                replaceFragment(new PushNotificationListFragment(), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL:
                replaceFragment(PushNotificationDetailFragment.newInstance(bundle), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_HOME:
                replaceFragment(new HomeFragment(), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_LIST_STORE_CHECKED_IN:
                replaceFragment(ListStoreCheckedInFragment.newInstance(bundle), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_HOW_TO_USE:
                replaceFragment(new HowToUserFragment(), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_QA:
                replaceFragment(new QAFragment(), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_CONTACT_US:
                replaceFragment(new ContactUsFragment(), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_CHANGE_PASSWORD:
                replaceFragment(new ChangePasswordFragment(), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_POLICY:
                replaceFragment(new PolicyFragment(), hasAnimation, addToBackStack);
                break;
            case IMainView.FRAGMENT_ABOUT:
                replaceFragment(new AboutFragment(), hasAnimation, addToBackStack);
                break;
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
    public void displayScreen(int screenId, boolean hasAnimation, boolean addToBackStack) {
        displayScreen(screenId, hasAnimation, addToBackStack, null);
    }

    @Override
    public void displayScreen(int screenId, boolean hasAnimation,
                              boolean addToBackStack, Bundle bundle) {
        switchScreen(screenId, hasAnimation, addToBackStack, bundle);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        checkStartNotification(intent);
    }

    private void checkStartNotification(Intent intent) {
        if (intent != null) {
            if (intent.getSerializableExtra(FirebaseMsgService.EXTRA_NOTIFICATION) != null) {
                NotificationMessage notificationMessage = (NotificationMessage) intent.getSerializableExtra(FirebaseMsgService.EXTRA_NOTIFICATION);
                if (mListNotification != null) {
                    mListNotification.add(0, notificationMessage);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(PushNotificationDetailFragment.NOTIFICATION_ARG, notificationMessage);
                bundle.putInt(PushNotificationDetailFragment.NOTIFICATION_SHOW_RATING, 1);
                switchScreen(IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL, true, true, bundle);
            } else {
                mPresenter.onCreate();
            }
        }
    }

    @Override
    public void clearBackStack() {
        mFragmentBackStackManager.clearBackStack();
    }

    @Override
    public void onLogout() {
        clearBackStack();
        displayScreen(IMainView.FRAGMENT_INTRODUCTION_SCREEN, true, false);
        if (mListNotification != null)
            mListNotification.clear();
        isRequestFirstNotification = true;
        mToolbar.setNumberNotificationUnRead(0);
    }

    @Override
    public void onFragmentResumed(BaseFragment fragment) {
        Logger.d(TAG, "#onFragmentResumed");
        if (fragment != null) {

            if (fragment instanceof HomeFragment && isRequestFirstNotification) {
                isRequestFirstNotification = false;
                mPresenter.getListPushNotificationUnRead(Constants.INIT_PAGE, Constants.LIMIT);
            }
            disablePushUpView();
            mCurrentFragment = fragment;
            mBottomNavigationView.setVisibility(fragment.isDisplayBottomNavigationMenu() ? View.VISIBLE : View.GONE);
            int navigationBottomId = fragment.getNavigationBottomId();
            if (navigationBottomId != 0) {
                mBottomNavigationView.setSelectedItemId(navigationBottomId);
            }
            mBottomNavigationView.getMenu().setGroupEnabled(R.id.navigation_bottom_group, fragment.isEnableBottomNavigationMenu());

            boolean isShowNavigationButton = fragment.isDisplayNavigationButton();
            mToolbar.setShowExtraNavigationButton(fragment.isDisplayExtraNavigationButton());
            mToolbar.setShowIconNotificationButton(fragment.isDisplayIconNotification());
            if (isShowNavigationButton) {
                mToolbar.setNavigationIcon(R.drawable.ic_back);
            } else {
                mToolbar.setNavigationIcon(null);
            }
            mToolbar.setTitleActionBar(fragment.getAppBarTitle());
            mToolbar.setVisibility(fragment.isDisplayActionBar() ? View.VISIBLE : View.GONE);
            int actionBarColor = fragment.getActionBarColor();
            mToolbar.setBackgroundColor(actionBarColor);
            int menuId = fragment.getMenuBottomID();
            switch (menuId) {
                case BaseFragment.MENU_HOME:
                    mBottomNavigationView.setSelectedItemId(R.id.navigation_home);
                    break;
                case BaseFragment.MENU_MY_REQUEST:
                    mBottomNavigationView.setSelectedItemId(R.id.navigation_another);
                    break;
                case BaseFragment.MENU_MY_STAMP:
                    mBottomNavigationView.setSelectedItemId(R.id.navigation_stamp);
                    break;
            }
            if (fragment.getNavigationMenuID() == 0) {
                mNavigationView.setCheckedItem(R.id.menu_visible);
            } else {
                mNavigationView.setCheckedItem(fragment.getNavigationMenuID());
            }
        }
    }

    void disablePushUpView() {
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void replaceFragment(BaseFragment fragment, boolean hasAnimation, boolean addToBackStack) {
        if (fragment != null && !isFinishing()) {
            if (mCurrentFragment == null || mCurrentFragment.getFragmentId() != fragment.getFragmentId()) {
                mFragmentBackStackManager.replaceFragment(fragment, hasAnimation, addToBackStack);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(broadcastReceiver);
    }
}