package jp.co.wssj.iungo.screens;

import android.os.Bundle;
import android.util.SparseArray;

import jp.co.wssj.iungo.screens.about.AboutFragment;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.screens.changepassword.ChangePasswordByCodeFragment;
import jp.co.wssj.iungo.screens.changepassword.ChangePasswordFragment;
import jp.co.wssj.iungo.screens.chat.StoreFollowFragment;
import jp.co.wssj.iungo.screens.chat.chatdetail.ChatFragment;
import jp.co.wssj.iungo.screens.chatwrapper.ChatWrapperFragment;
import jp.co.wssj.iungo.screens.comment.CommentFragment;
import jp.co.wssj.iungo.screens.contact.ContactUsFragment;
import jp.co.wssj.iungo.screens.howtouse.HowToUserFragment;
import jp.co.wssj.iungo.screens.introduction.IntroductionFragment;
import jp.co.wssj.iungo.screens.listcard.ListCardFragmentDetail;
import jp.co.wssj.iungo.screens.listservicecompany.ListServiceCompanyFragment;
import jp.co.wssj.iungo.screens.listservicecompanywrapper.ListServiceCompanyWrapperFragment;
import jp.co.wssj.iungo.screens.liststorecheckedin.ListStoreCheckedInFragment;
import jp.co.wssj.iungo.screens.login.LoginFragment;
import jp.co.wssj.iungo.screens.memomanager.MemoManagerFragment;
import jp.co.wssj.iungo.screens.note.UserMemoFragment;
import jp.co.wssj.iungo.screens.polycy.PolicyFragment;
import jp.co.wssj.iungo.screens.pushnotification.PushNotificationPageFragment;
import jp.co.wssj.iungo.screens.pushnotification.detail.PushNotificationDetailFragment;
import jp.co.wssj.iungo.screens.pushnotificationforstore.PushNotificationForServiceCompanyFragment;
import jp.co.wssj.iungo.screens.qa.QAFragment;
import jp.co.wssj.iungo.screens.qadetail.QADetailFragment;
import jp.co.wssj.iungo.screens.registeraccount.RegisterAccountFragment;
import jp.co.wssj.iungo.screens.resetpassword.ResetPasswordFragment;
import jp.co.wssj.iungo.screens.splash.SplashFragment;
import jp.co.wssj.iungo.screens.termofservice.fragment.TermOfServiceFragment;
import jp.co.wssj.iungo.screens.termofservice.fragment.TermOfServiceNoMenuBottom;
import jp.co.wssj.iungo.screens.timeline.TimeLineFragment;
import jp.co.wssj.iungo.screens.waitstoreconfirm.WaitStoreConfirmFragment;

/**
 * Created by HieuPT on 10/18/2017.
 */

public final class FragmentFactory {

    private static SparseArray<BaseFragment> sFragmentMap;

    public static void init() {
        sFragmentMap = new SparseArray<>();
    }

    public static void destroy() {
        sFragmentMap.clear();
    }

    public static BaseFragment getFragment(int screenId, Bundle bundle, boolean retain) {
        BaseFragment fragment = null;
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
                case IMainView.FRAGMENT_PUSH_NOTIFICATION_LIST:
                    fragment = PushNotificationPageFragment.newInstance(bundle);
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
                    fragment = new AboutFragment();
                    break;
                case IMainView.FRAGMENT_NOTIFICATION_FOR_SERVICE_COMPANY:
                    fragment = PushNotificationForServiceCompanyFragment.newInstance(bundle);
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
                case IMainView.FRAGMENT_CHAT_WRAPPER:
                    fragment = new ChatWrapperFragment();
                    break;
                case IMainView.FRAGMENT_PUSH_NOTIFICATION_PAGER:
                    fragment = PushNotificationPageFragment.newInstance(bundle);
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
