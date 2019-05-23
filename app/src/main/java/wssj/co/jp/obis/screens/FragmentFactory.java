package wssj.co.jp.obis.screens;

import android.os.Bundle;
import android.util.SparseArray;

import wssj.co.jp.obis.screens.about.AboutFragment;
import wssj.co.jp.obis.screens.base.BaseFragment;
import wssj.co.jp.obis.screens.blockchat.BlockChatFragment;
import wssj.co.jp.obis.screens.changeaccount.ChangeAccountFragment;
import wssj.co.jp.obis.screens.changepassword.ChangePasswordByCodeFragment;
import wssj.co.jp.obis.screens.changepassword.ChangeUserInfoFragment;
import wssj.co.jp.obis.screens.chat.StoreFollowFragment;
import wssj.co.jp.obis.screens.chat.chatdetail.ChatFragment;
import wssj.co.jp.obis.screens.chatrealtime.conversations.ConversationsFragment;
import wssj.co.jp.obis.screens.chatwrapper.ChatWrapperFragment;
import wssj.co.jp.obis.screens.comment.CommentFragment;
import wssj.co.jp.obis.screens.contact.ContactUsFragment;
import wssj.co.jp.obis.screens.coupone.CouponeFragment;
import wssj.co.jp.obis.screens.coupone.detailunused.UnusedDetailCouponeFragment;
import wssj.co.jp.obis.screens.coupone.detailused.UsedDetailCouponeFragment;
import wssj.co.jp.obis.screens.groupchat.groupchatdetail.GroupChatDetailFragment;
import wssj.co.jp.obis.screens.howtouse.HowToUserFragment;
import wssj.co.jp.obis.screens.introduction.IntroductionFragment;
import wssj.co.jp.obis.screens.listcard.ListCardFragmentDetail;
import wssj.co.jp.obis.screens.login.LoginFragment;
import wssj.co.jp.obis.screens.memomanager.MemoManagerFragment;
import wssj.co.jp.obis.screens.note.UserMemoFragment;
import wssj.co.jp.obis.screens.polycy.PolicyFragment;
import wssj.co.jp.obis.screens.profile.ProfileStoreFragment;
import wssj.co.jp.obis.screens.pushnotification.detail.PushNotificationDetailFragment;
import wssj.co.jp.obis.screens.pushnotification.pushlist.PushNotificationFragment;
import wssj.co.jp.obis.screens.qa.QAFragment;
import wssj.co.jp.obis.screens.qadetail.QADetailFragment;
import wssj.co.jp.obis.screens.registeraccount.RegisterAccountFragment;
import wssj.co.jp.obis.screens.resetpassword.ResetPasswordFragment;
import wssj.co.jp.obis.screens.scanner.ScannerFragment;
import wssj.co.jp.obis.screens.screentest.ScreenTestFragment;
import wssj.co.jp.obis.screens.splash.SplashFragment;
import wssj.co.jp.obis.screens.termofservice.fragment.TermOfServiceFragment;
import wssj.co.jp.obis.screens.termofservice.fragment.TermOfServiceNoMenuBottom;
import wssj.co.jp.obis.screens.timeline.timelinedetail.TimeLineDetailFragment;
import wssj.co.jp.obis.screens.waitstoreconfirm.WaitStoreConfirmFragment;

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
            case IMainView.FRAGMENT_INTRODUCTION_SCREEN:
                fragment = new IntroductionFragment();
                break;
            case IMainView.FRAGMENT_LOGIN:
                fragment = new LoginFragment();
                break;
            case IMainView.FRAGMENT_RESET_PASSWORD:
                fragment = new ResetPasswordFragment();
                break;
            case IMainView.FRAGMENT_CHANGE_PASSWORD_CODE:
                fragment = new ChangePasswordByCodeFragment();
                break;
            case IMainView.FRAGMENT_REGISTER_ACCOUNT:
                fragment = new RegisterAccountFragment();
                break;
            case IMainView.FRAGMENT_MEMO_MANAGER:
                fragment = new MemoManagerFragment();
                break;
            case IMainView.FRAGMENT_USER_MEMO:
                fragment = UserMemoFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_LIST_CARD:
                fragment = ListCardFragmentDetail.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_TERM_OF_SERVICE:
                fragment = new TermOfServiceFragment();
                break;
            case IMainView.FRAGMENT_TERM_OF_SERVICE_N0_BOTTOM:
                fragment = new TermOfServiceNoMenuBottom();
                break;
            case IMainView.FRAGMENT_PUSH_NOTIFICATION_DETAIL:
                fragment = PushNotificationDetailFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_HOW_TO_USE:
                fragment = new HowToUserFragment();
                break;
            case IMainView.FRAGMENT_QA:
                fragment = new QAFragment();
                break;
            case IMainView.FRAGMENT_CONTACT_US:
                fragment = new ContactUsFragment();
                break;
            case IMainView.FRAGMENT_CHANGE_PASSWORD:
                fragment = ChangeUserInfoFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_POLICY:
                fragment = new PolicyFragment();
                break;
            case IMainView.FRAGMENT_ABOUT:
                fragment = AboutFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_QA_DETAIL:
                fragment = QADetailFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_CHAT:
                fragment = ChatFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_COMMENT:
                fragment = CommentFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_HOME:
                fragment = new ScannerFragment();
                break;
            case IMainView.FRAGMENT_CHAT_WRAPPER:
                fragment = new ChatWrapperFragment();
                break;
            case IMainView.FRAGMENT_STORE_FOLLOW:
                fragment = StoreFollowFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_WAIT_STORE_CONFIRM:
                fragment = WaitStoreConfirmFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_PUSH_NOTIFICATION_LIST:
                break;
            case IMainView.FRAGMENT_TIMELINE_DETAIL:
                fragment = TimeLineDetailFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_PROFILE:
                fragment = ProfileStoreFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_COUPONE:
                fragment = CouponeFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_USED_DETAIL_COUPONE:
                fragment = UsedDetailCouponeFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_UNUSED_DETAIL_COUPONE:
                fragment = UnusedDetailCouponeFragment.newInstance(bundle);
                break;
            case IMainView.FRAGMENT_SCREEN_TEST:
                fragment = new ScreenTestFragment();
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
