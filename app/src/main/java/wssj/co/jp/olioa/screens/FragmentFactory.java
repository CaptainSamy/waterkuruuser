package wssj.co.jp.olioa.screens;

import android.os.Bundle;
import android.util.SparseArray;

import wssj.co.jp.olioa.screens.contact.ContactUsFragment;
import wssj.co.jp.olioa.screens.pushnotification.pushlist.PushNotificationFragment;
import wssj.co.jp.olioa.screens.pushnotificationforstore.PushNotificationForStoreAnnounce;
import wssj.co.jp.olioa.screens.about.AboutFragment;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.changepassword.ChangePasswordByCodeFragment;
import wssj.co.jp.olioa.screens.changepassword.ChangePasswordFragment;
import wssj.co.jp.olioa.screens.chat.StoreFollowFragment;
import wssj.co.jp.olioa.screens.chat.chatdetail.ChatFragment;
import wssj.co.jp.olioa.screens.chatrealtime.chatdeatail.ChatRealTimeFragment;
import wssj.co.jp.olioa.screens.chatrealtime.conversations.ConversationsFragment;
import wssj.co.jp.olioa.screens.chatwrapper.ChatWrapperFragment;
import wssj.co.jp.olioa.screens.comment.CommentFragment;
import wssj.co.jp.olioa.screens.coupone.CouponeFragment;
import wssj.co.jp.olioa.screens.coupone.detailunused.UnusedDetailCouponeFragment;
import wssj.co.jp.olioa.screens.coupone.detailused.UsedDetailCouponeFragment;
import wssj.co.jp.olioa.screens.howtouse.HowToUserFragment;
import wssj.co.jp.olioa.screens.introduction.IntroductionFragment;
import wssj.co.jp.olioa.screens.listcard.ListCardFragmentDetail;
import wssj.co.jp.olioa.screens.listservicecompany.ListServiceCompanyFragment;
import wssj.co.jp.olioa.screens.listservicecompanywrapper.ListServiceCompanyWrapperFragment;
import wssj.co.jp.olioa.screens.liststorecheckedin.ListStoreCheckedInFragment;
import wssj.co.jp.olioa.screens.login.LoginFragment;
import wssj.co.jp.olioa.screens.memomanager.MemoManagerFragment;
import wssj.co.jp.olioa.screens.note.UserMemoFragment;
import wssj.co.jp.olioa.screens.polycy.PolicyFragment;
import wssj.co.jp.olioa.screens.profile.ProfileStoreFragment;
import wssj.co.jp.olioa.screens.pushnotification.detail.PushNotificationDetailFragment;
import wssj.co.jp.olioa.screens.pushnotification.pushpagecontainer.PushNotificationPageFragment;
import wssj.co.jp.olioa.screens.pushnotification.pushpagecontainer.PushNotificationPageNavigationFragment;
import wssj.co.jp.olioa.screens.qa.QAFragment;
import wssj.co.jp.olioa.screens.qadetail.QADetailFragment;
import wssj.co.jp.olioa.screens.registeraccount.RegisterAccountFragment;
import wssj.co.jp.olioa.screens.resetpassword.ResetPasswordFragment;
import wssj.co.jp.olioa.screens.scanner.ScannerFragment;
import wssj.co.jp.olioa.screens.screentest.ScreenTestFragment;
import wssj.co.jp.olioa.screens.splash.SplashFragment;
import wssj.co.jp.olioa.screens.termofservice.fragment.TermOfServiceFragment;
import wssj.co.jp.olioa.screens.termofservice.fragment.TermOfServiceNoMenuBottom;
import wssj.co.jp.olioa.screens.timeline.timelinedetail.TimeLineDetailFragment;
import wssj.co.jp.olioa.screens.timeline.timelinetotal.TimeLineFragment;
import wssj.co.jp.olioa.screens.waitstoreconfirm.WaitStoreConfirmFragment;

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

    public static BaseFragment getFragment(int screenId, Bundle bundle, boolean retain) {
        BaseFragment fragment = null;
        if (bundle != null) {
            boolean needOverrideRetain = bundle.getBoolean(KEY_NEED_OVERRIDE_RETAIN_GLOBAL, false);
            if (needOverrideRetain) {
                retain = bundle.getBoolean(KEY_VALUE_OVERRIDE_RETAIN_GLOBAL, false);
            }
        }
        if (retain) {
            fragment = sFragmentMap.get(screenId);
        }
        if (fragment == null) {
            switch (screenId) {
                case IMainView.FRAGMENT_SPLASH_SCREEN:
                    fragment = new SplashFragment();
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
                case IMainView.FRAGMENT_LIST_STORE_CHECKED_IN:
                    fragment = ListStoreCheckedInFragment.newInstance(bundle);
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
                    fragment = new ChangePasswordFragment();
                    break;
                case IMainView.FRAGMENT_POLICY:
                    fragment = new PolicyFragment();
                    break;
                case IMainView.FRAGMENT_ABOUT:
                    fragment = AboutFragment.newInstance(bundle);
                    break;
                case IMainView.FRAGMENT_NOTIFICATION_FOR_SERVICE_COMPANY:
                    fragment = PushNotificationForStoreAnnounce.newInstance(bundle);
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
                case IMainView.FRAGMENT_TIMELINE:
                    fragment = new TimeLineFragment();
                    break;
                case IMainView.FRAGMENT_HOME:
                    fragment = new ScannerFragment();
                    break;
                case IMainView.FRAGMENT_CHAT_WRAPPER:
                    fragment = new ChatWrapperFragment();
                    break;
                case IMainView.FRAGMENT_PUSH_NOTIFICATION_PAGER:
                    fragment = PushNotificationPageFragment.newInstance(bundle);
                    break;
                case IMainView.FRAGMENT_PUSH_NOTIFICATION_PAGER_NAVIGATION:
                    fragment = PushNotificationPageNavigationFragment.newInstance(bundle);
                    break;
                case IMainView.FRAGMENT_LIST_SERVICE_COMPANY_WRAPPER:
                    fragment = new ListServiceCompanyWrapperFragment();
                    break;
                case IMainView.FRAGMENT_LIST_SERVICE_COMPANY:
                    fragment = ListServiceCompanyFragment.newInstance(bundle);
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
                case IMainView.FRAGMENT_CHAT_REALTIME:
                    fragment = ChatRealTimeFragment.newInstance(bundle);
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
            }
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
