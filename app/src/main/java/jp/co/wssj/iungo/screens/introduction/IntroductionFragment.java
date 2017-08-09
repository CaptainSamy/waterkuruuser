package jp.co.wssj.iungo.screens.introduction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.IMainView;
import jp.co.wssj.iungo.screens.base.BaseFragment;
import jp.co.wssj.iungo.utils.Constants;
import jp.co.wssj.iungo.utils.Logger;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

public class IntroductionFragment extends BaseFragment<IIntroductionView, IntroductionPresenter> implements IIntroductionView, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "IntroductionFragment";

    private static final int CODE_LOGIN_FACEBOOK = 1000;

    private static final int CODE_LOGIN_TWITTER = 2000;

    private static final int CODE_SIGN_IN_GOOGLE = 3000;

    private TextView mButtonLogin, mButtonRegister, mButtonHowToUsed;

    private ImageView mImageLoginFacebook, mImageLoginTwitter, mImageSignInGoogle;

    private GoogleApiClient mGoogleApiClient;

    private TwitterLoginButton mButtonLoginTwitter;

    private LoginButton mButtonLoginFacebook;

    private CallbackManager mCallbackManager;

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_INTRODUCTION_SCREEN;
    }

    @Override
    public String getAppBarTitle() {
        return "";
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public boolean isDisplayActionBar() {
        return false;
    }

    @Override
    public boolean isDisplayIconNotification() {
        return false;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return false;
    }

    @Override
    public int getActionBarColor() {
        return Color.TRANSPARENT;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.introduction_fragment;
    }

    @Override
    protected IntroductionPresenter onCreatePresenter(IIntroductionView view) {
        return new IntroductionPresenter(view);
    }

    @Override
    protected IIntroductionView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mButtonLogin = (TextView) rootView.findViewById(R.id.tvLogin);
        mButtonRegister = (TextView) rootView.findViewById(R.id.tvRegisterAccount);
        mButtonHowToUsed = (TextView) rootView.findViewById(R.id.tvHowToUsed);
        mImageLoginFacebook = (ImageView) rootView.findViewById(R.id.imageLoginFacebook);
        mImageLoginTwitter = (ImageView) rootView.findViewById(R.id.imageLoginTwitter);
        mImageSignInGoogle = (ImageView) rootView.findViewById(R.id.imageSignInGoogle);
        mButtonLoginTwitter = (TwitterLoginButton) rootView.findViewById(R.id.buttonLoginTwitter);
        mButtonLoginFacebook = (LoginButton) rootView.findViewById(R.id.buttonLoginFacebook);
        mButtonLoginFacebook.setFragment(this);

    }

    @Override
    protected void initAction() {
        mButtonLogin.setOnClickListener(this);
        mButtonRegister.setOnClickListener(this);
        mButtonHowToUsed.setOnClickListener(this);
        mImageLoginTwitter.setOnClickListener(this);
        mImageSignInGoogle.setOnClickListener(this);
        mImageLoginFacebook.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        String text = "スマホでかんたん<font color='#D9594C'>ツナグ</font>アプリ";
        mButtonHowToUsed.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        onLoginTwitter();
        onLoginGoogle();
        onLoginFacebook();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLogin:
                getPresenter().onLoginButtonClicked();
                break;
            case R.id.tvRegisterAccount:
                getPresenter().onRegisterButtonClicked();
                break;
            case R.id.tvHowToUsed:
                break;
            case R.id.imageLoginFacebook:
                mButtonLoginFacebook.callOnClick();
                break;
            case R.id.imageLoginTwitter:
                mButtonLoginTwitter.callOnClick();
                break;
            case R.id.imageSignInGoogle:
                signIn();
                break;
        }
    }

    @Override
    public void displayLoginScreen() {
        getActivityCallback().displayScreen(IMainView.FRAGMENT_LOGIN, true, true);
    }

    @Override
    public void displayRegisterScreen() {
        getActivityCallback().displayScreen(IMainView.FRAGMENT_REGISTER_ACCOUNT, true, true);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void displayHomeScreen() {
        getActivityCallback().clearBackStack();
        getActivityCallback().displayScreen(IMainView.FRAGMENT_HOME, false, false);
    }

    /*
     * Handle Login Facebook
     */
    private void onLoginFacebook() {
//        Check login facebook
//        Profile profile = Profile.getCurrentProfile();
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        mCallbackManager = CallbackManager.Factory.create();
        mButtonLoginFacebook.setReadPermissions(Arrays.asList("email", "public_profile"));
        mButtonLoginFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Logger.d("onLoginFacebook", "onSuccess");

                AccessToken accessToken = loginResult.getAccessToken();
                getPresenter().onLoginSocialNetwork(Constants.Introduction.TYPE_FACEBOOK, accessToken.getToken());
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                        getPhotoUrlFacebook(user);
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Logger.d("onLoginFacebook", "onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Logger.d("onLoginFacebook", "onError");
                showToast(error.getMessage());
            }
        });
    }

    private void getPhotoUrlFacebook(JSONObject user) {
        try {
            if (user != null && user.has("picture")) {
                String objectPicture = user.getString("picture");
                if (!TextUtils.isEmpty(objectPicture)) {
                    JSONObject jsonPicture = new JSONObject(objectPicture);
                    if (jsonPicture != null && jsonPicture.has("data")) {
                        String data = jsonPicture.getString("data");
                        JSONObject jsonData = new JSONObject(data);
                        if (jsonData != null && jsonData.has("url")) {
                            String url = jsonData.getString("url");
                            getPresenter().savePhotoUrl(url);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    * Handle Login Twitter
    * */

    private void onLoginTwitter() {
        mButtonLoginTwitter.setCallback(new Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> result) {

            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }

    /*
    * Handle Sign In Google
    * */

    public void onLoginGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestIdToken(getString(R.string.server_client_id_dev))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivityContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        /*
        * Check signed in google
        *
        * */
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, CODE_SIGN_IN_GOOGLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_LOGIN_FACEBOOK:
                break;
            case CODE_LOGIN_TWITTER:
                break;
            case CODE_SIGN_IN_GOOGLE:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                break;
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            getPresenter().savePhotoUrl(acct.getPhotoUrl().toString());
            getPresenter().onLoginSocialNetwork(Constants.Introduction.TYPE_GOOGLE, acct.getIdToken());
        } else {
            //TODO sign out
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {
                        //TODO sign out
                        Logger.d(TAG, "signOut");
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {
                        //TODO disconnect account
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(getActivity());
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
    }
}
