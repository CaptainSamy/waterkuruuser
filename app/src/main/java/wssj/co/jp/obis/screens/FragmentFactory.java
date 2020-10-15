package wssj.co.jp.obis.screens;

import android.os.Bundle;
import android.util.SparseArray;

import wssj.co.jp.obis.screens.about.AboutFragment;
import wssj.co.jp.obis.screens.base.BaseFragment;
import wssj.co.jp.obis.screens.blockchat.BlockChatFragment;
import wssj.co.jp.obis.screens.changeaccount.ChangeAccountFragment;
import wssj.co.jp.obis.screens.changepassword.ChangePasswordByCodeFragment;
import wssj.co.jp.obis.screens.changepassword.ChangeUserInfoFragment;
import wssj.co.jp.obis.screens.chat.chatdetail.ChatFragment;
import wssj.co.jp.obis.screens.chatrealtime.conversations.ConversationsFragment;
import wssj.co.jp.obis.screens.groupchat.groupchatdetail.GroupChatDetailFragment;
import wssj.co.jp.obis.screens.login.LoginFragment;
import wssj.co.jp.obis.screens.pushnotification.detail.PushNotificationDetailFragment;
import wssj.co.jp.obis.screens.pushnotification.pushlist.PushNotificationFragment;
import wssj.co.jp.obis.screens.scanner.ScannerFragment;
import wssj.co.jp.obis.screens.splash.SplashFragment;
//import wssj.co.jp.obis.screens.termofservice.fragment.TermOfServiceFragment;
//import wssj.co.jp.obis.screens.termofservice.fragment.TermOfServiceNoMenuBottom;

/**
 * Created by HieuPT on 10/18/2017.
 */

public final class FragmentFactory {

    public static final String KEY_NEED_OVERRIDE_RETAIN_GLOBAL = "KEY_NEED_OVERRIDE_RETAIN_GLOBAL";

    public static final String KEY_VALUE_OVERRIDE_RETAIN_GLOBAL = "KEY_VALUE_OVERRIDE_RETAIN_GLOBAL";

    private static SparseArray<BaseFragment> sFragmentMap;

    public static void init() {
        sFragmentMap = new SparseArray<>();
    }

    public static void destroy() {
        sFragmentMap.clear();
    }

    public static BaseFragment getFragment(int screenId, Bundle bundle) {
        BaseFragment fragment = null;
        switch (screenId) {
            case IMainView.FRAGMENT_SPLASH_SCREEN:
                fragment = SplashFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_LOGIN:
                fragment = new LoginFragment();
                break;
            case IMainView.FRAGMENT_CHANGE_PASSWORD_CODE:
                fragment = new ChangePasswordByCodeFragment();
                break;
            case IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL:
                fragment = PushNotificationDetailFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_CHANGE_PASSWORD:
                fragment = ChangeUserInfoFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_ABOUT:
                fragment = AboutFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_CHAT:
                fragment = ChatFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_HOME:
                fragment = new ScannerFragment();
                break;
            case IMainView.FRAGMENT_PUSH_NOTIFICATION_LIST:
                break;
            case IMainView.FRAGMENT_CONVERSATION:
                fragment = ConversationsFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_PUSH_NOTIFICATION:
                fragment = new PushNotificationFragment();
                break;
            case IMainView.FRAGMENT_SCANNER:
                fragment = new ScannerFragment();
                break;
            case IMainView.FRAGMENT_CHANGE_ACCOUNT:
                fragment = new ChangeAccountFragment();
                break;
            case IMainView.FRAGMENT_GROUP_CHAT_DETAIL:
                fragment = GroupChatDetailFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_BLOCK_CHAT:
                fragment = new BlockChatFragment();
                break;
        }
        if (fragment != null) {
            sFragmentMap.append(screenId, fragment);
        }
        return fragment;
    }

    private FragmentFactory() {
        //no instance
    }
}
