package jp.co.wssj.iungo.screens.splash;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import jp.co.wssj.iungo.App;
import jp.co.wssj.iungo.model.ErrorMessage;
import jp.co.wssj.iungo.model.auth.AuthModel;
import jp.co.wssj.iungo.model.auth.CheckVersionAppResponse;
import jp.co.wssj.iungo.model.auth.LoginResponse;
import jp.co.wssj.iungo.model.chat.ChatModel;
import jp.co.wssj.iungo.model.firebase.FirebaseModel;
import jp.co.wssj.iungo.model.preference.SharedPreferencesModel;
import jp.co.wssj.iungo.model.stamp.StampModel;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.MainActivity;
import jp.co.wssj.iungo.screens.base.FragmentPresenter;

/**
 * Created by Nguyen Huu Ta on 5/6/2017.
 */

public class SplashPresenter extends FragmentPresenter<ISplashView> {

    protected SplashPresenter(ISplashView view) {
        super(view);
        registerModel(new SharedPreferencesModel(view.getViewContext()));
        registerModel(new AuthModel(view.getViewContext()));
        registerModel(new FirebaseModel(view.getViewContext()));
        registerModel(new StampModel(view.getViewContext()));
        registerModel(new ChatModel(view.getViewContext()));
    }

    private SharedPreferencesModel getShareModel() {
        return getModel(SharedPreferencesModel.class);
    }

    public void onCreate(final int versionCode) {
        long expireDate = getModel(SharedPreferencesModel.class).getExpireDate();
        if (expireDate <= System.currentTimeMillis()) {
            String userId = getModel(SharedPreferencesModel.class).getUserId();
            String password = getModel(SharedPreferencesModel.class).getPassword();
            getModel(AuthModel.class).loginAWS(userId, password, new AuthModel.ILoginCallback() {

                @Override
                public void onLoginSuccess(LoginResponse.LoginData data) {
                    getShareModel().putToken(data.getToken());
                    getShareModel().putExpireDate(data.getExpireDate());
                    getModel(FirebaseModel.class).uploadDeviceToken(data.getToken(), null);
                    switchScreen(versionCode);
                }

                @Override
                public void onLoginFailure(ErrorMessage errorMessage) {
                    Intent intent = new Intent(MainActivity.ACTION_LOGOUT);
                    LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(intent);
                }
            });
        } else {
            switchScreen(versionCode);
        }

    }

    public void switchScreen(final int versionCode) {
        final String token = getModel(SharedPreferencesModel.class).getToken();
        if (!TextUtils.isEmpty(token)) {
            getModel(AuthModel.class).checkVersionApp(token, versionCode, new AuthModel.IOnCheckVersionAppCallback() {

                @Override
                public void onCheckVersionAppSuccess(CheckVersionAppResponse.CheckVersionAppData response) {
                    getView().showDialog(response);
                }

                @Override
                public void onCheckVersionAppFailure() {
                    getView().displayScreen(IMainView.FRAGMENT_TIMELINE);
                }
            });
//            getView().displayScreen(IMainView.FRAGMENT_HOME);
        } else {
            getView().displayScreen(IMainView.FRAGMENT_INTRODUCTION_SCREEN);
        }

    }
//
//    void getCompanyList() {
//        String token = getModel(SharedPreferencesModel.class).getToken();
//        getModel(StampModel.class).getListCompany(token, new StampModel.IGetListCompanyResponse() {
//
//            @Override
//            public void onSuccess(List<ListCompanyResponse.ListCompanyData.CompanyData> companyDataList) {
//                Bundle bundle = new Bundle();
//                if (companyDataList != null && companyDataList.size() == 1) {
//                    bundle.putInt(Constants.KEY_SERVICE_COMPANY_ID, companyDataList.get(0).getServiceCompanyId());
//                }
//                getListStoreFollow(bundle);
//
//            }
//
//            @Override
//            public void onFailure(ErrorMessage errorMessage) {
//                getView().displayScreen(IMainView.FRAGMENT_TIMELINE, null);
//            }
//        });
//    }
//
//    public void getListStoreFollow(final Bundle bundle) {
//        String token = getModel(SharedPreferencesModel.class).getToken();
//        getModel(ChatModel.class).getListStoreFollow(token, new ChatModel.OnGetListStoreFollowCallback() {
//
//            @Override
//            public void onGetListStoreFollowSuccess(List<StoreFollowResponse.StoreChatData.StoreFollow> storeFollows) {
//                if (storeFollows != null && storeFollows.size() == 1) {
//                    StoreFollowResponse.StoreChatData.StoreFollow store = storeFollows.get(0);
//                    bundle.putInt(ChatFragment.KEY_STORE_ID, store.getId());
//                    bundle.putString(ChatFragment.KEY_STORE_NAME, store.getStoreName());
//                    bundle.putString(ChatFragment.KEY_IMAGE_STORE, store.getImageStore());
//                }
//                getView().displayScreen(IMainView.FRAGMENT_PRIMARY, bundle);
//            }
//
//            @Override
//            public void onGetListStoreFollowFailure(String message) {
//                getView().displayScreen(IMainView.FRAGMENT_PRIMARY, bundle);
//            }
//        });
//
//    }
}
