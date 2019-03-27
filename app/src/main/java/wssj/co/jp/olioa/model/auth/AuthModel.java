package wssj.co.jp.olioa.model.auth;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.BaseModel;
import wssj.co.jp.olioa.model.ErrorMessage;
import wssj.co.jp.olioa.model.ErrorResponse;
import wssj.co.jp.olioa.model.ResponseData;
import wssj.co.jp.olioa.model.baseapi.APICallback;
import wssj.co.jp.olioa.model.entities.AccessToken;
import wssj.co.jp.olioa.model.entities.User;
import wssj.co.jp.olioa.utils.AmazonS3Utils;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Logger;
import wssj.co.jp.olioa.utils.Utils;
import wssj.co.jp.olioa.utils.VolleySequence;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

public class AuthModel extends BaseModel {

    public interface IValidateLoginCallback {

        void validateSuccess(String userId, String password);

        void validateFailure(ErrorMessage errorMessage);
    }

    public interface IValidateRegisterCallback {

        void validateSuccess(String userName, String password, String name, String email, int age, int sex);

        void validateFailure(ErrorMessage errorMessage, int code);

    }

    public interface IRegisterCallback {

        void onRegisterSuccess(RegisterData data, String message);

        void onRegisterFailure(ErrorMessage errorMessage);
    }

    public interface ILoginCallback {

        void onLoginSuccess(LoginResponse.LoginData data);

        void onLoginFailure(ErrorMessage errorMessage);
    }

    public interface IOnResetPasswordCallback {

        void onValidateFailure(String message);

        void onResetSuccess(String message);

        void onResetFailure(String message);

    }

    public interface IOnChangePasswordCallback {

        void onValidateFailure(String message);

        void onChangePasswordSuccess(RegisterData data, String message);

        void onChangePasswordFailure(String message);

    }

    public interface IOnCheckVersionAppCallback {

        void onCheckVersionAppSuccess(CheckVersionAppResponse.CheckVersionAppData response);

        void onCheckVersionAppFailure();
    }

    public interface IOnGetInfoUserCallback {

        void onGetInfoUserSuccess(InfoUserResponse.InfoUser infoUser);

        void onGetInfoUserFailure(String message);
    }

    public interface IOnUpdateInfoUserCallback {

        void onOnUpdateInfoUserSuccess();

        void onOnUpdateInfoUserFailure(String message);
    }

    public interface IOnUploadImage {

        void onSuccess();

        void onFailure();
    }

    public interface IOnValidate {

        void onSuccess();

        void onFailure(String message);
    }

    public interface IOnGetInitUserCallback {

        void onGetInitUserSuccess(InitUserResponse.InitUserData initUser);

        void onGetInitUserFailure(String message);
    }

    private static final String TAG = "AuthModel";

    public AuthModel(Context context) {
        super(context);
    }

    public void validateLogin(String userId, String password, IValidateLoginCallback callback) {
        ErrorMessage errorMessage = null;
        if (TextUtils.isEmpty(userId)) {
            errorMessage = new ErrorMessage(getContext().getString(R.string.user_login_not_null));
        } else if (TextUtils.isEmpty(password)) {
            errorMessage = new ErrorMessage(getContext().getString(R.string.password_login_not_null));
        }
        if (errorMessage != null && !TextUtils.isEmpty(errorMessage.getMessage())) {
            callback.validateFailure(errorMessage);
        } else {
            callback.validateSuccess(userId, password);
        }
    }

    public void validateRegisterAccount(String userName, String userId, String password, String confirmPassword, String email, int age, int sex, IValidateRegisterCallback callback) {
        ErrorMessage errorMessage = null;
        int code = 0;
        if (TextUtils.isEmpty(userName)) {
            errorMessage = new ErrorMessage(getContext().getString(R.string.user_name_not_null));
            code = 1;
        } else if (TextUtils.isEmpty(userId)) {
            errorMessage = new ErrorMessage(getContext().getString(R.string.user_not_null));
            code = 2;
        } else if (TextUtils.isEmpty(email)) {
            errorMessage = new ErrorMessage(getContext().getString(R.string.email_not_null));
            code = 3;
        } else if (!isEmailValid(email)) {
            errorMessage = new ErrorMessage(getContext().getString(R.string.email_validate));
            code = 3;
        } else if (TextUtils.isEmpty(password)) {
            errorMessage = new ErrorMessage(getContext().getString(R.string.password_not_null));
            code = 4;
        } else if (password.length() < 8 || password.length() > 14) {
            errorMessage = new ErrorMessage(getContext().getString(R.string.password_length));
            code = 4;
        } else if (TextUtils.isEmpty(confirmPassword)) {
            errorMessage = new ErrorMessage(getContext().getString(R.string.confirm_password_not_null));
            code = 5;
        } else if (!TextUtils.equals(password, confirmPassword)) {
            errorMessage = new ErrorMessage(getContext().getString(R.string.different_password));
            code = 5;
        }


        if (errorMessage != null && !TextUtils.isEmpty(errorMessage.getMessage())) {
            callback.validateFailure(errorMessage, code);
        } else {
            callback.validateSuccess(userId, password, userName, email, age, sex);
        }

    }

    public void loginAWS(String username, String password, final ILoginCallback callback) {
        Request loginRequest = APICreator.getLoginAWSRequest(username, password,
                new Response.Listener<LoginResponse>() {

                    @Override
                    public void onResponse(LoginResponse response) {
                        Logger.d(TAG, "#loginAWS => onResponse");
                        if (response.isSuccess()) {
                            callback.onLoginSuccess(response.getData());
                        } else {
                            callback.onLoginFailure(new ErrorMessage(response.getMessage()));
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#loginAWS => onErrorResponse");
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                        if (errorResponse != null) {
                            callback.onLoginFailure(new ErrorMessage(errorResponse.getMessage()));
                        } else {
                            callback.onLoginFailure(new ErrorMessage(getStringResource(R.string.network_error)));
                        }
                    }
                });
        VolleySequence.getInstance().addRequestToFrontQueue(loginRequest);
    }

    public void loginUser(String username, String password, APICallback<AccessToken> callback) {
        User user = new User(username, password);
        getApi().loginUser(user).getAsyncResponse(callback);
    }

    public void autoLogin(final ILoginCallback callback) {
        Request loginRequest = APICreator.autoLogin(new Response.Listener<LoginResponse>() {

            @Override
            public void onResponse(LoginResponse response) {
                Logger.d(TAG, "#autoLogin => onResponse");
                if (response.isSuccess()) {
                    callback.onLoginSuccess(response.getData());
                } else {
                    callback.onLoginFailure(new ErrorMessage(response.getMessage()));
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d(TAG, "#autoLogin => onResponse");
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onLoginFailure(new ErrorMessage(errorResponse.getMessage()));
                } else {
                    callback.onLoginFailure(new ErrorMessage(getStringResource(R.string.network_error)));
                }
            }
        });
        loginRequest.setRetryPolicy(new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySequence.getInstance().addRequestToFrontQueue(loginRequest);
    }

    public void registerAccount(String userId, String password, String name, String email, int sex, APICallback<AccessToken> callback) {
        User user = new User(userId, password);
        user.setName(name);
        user.setEmail(email);
        user.setSex(sex);
        user.setType(1);
        getApi().registerUser(user).getAsyncResponse(callback);
    }

    public void validateResetPassword(String userId, IOnResetPasswordCallback callback) {
        String message = Constants.EMPTY_STRING;
        if (TextUtils.isEmpty(userId.trim())) {
            message = getStringResource(R.string.user_id_reset_not_null);
        }
        int position = userId.indexOf("@");
        if (position != -1) {
            if (!isEmailValid(userId)) {
                message = getStringResource(R.string.email_not_format);
            }
        }
        if (TextUtils.isEmpty(message)) {
            resetPassword(userId, callback);
        } else {
            callback.onValidateFailure(message);
        }

    }

    void resetPassword(String userId, final IOnResetPasswordCallback callback) {
        Request resetPassword = APICreator.resetPassword(userId,
                new Response.Listener<ResponseData>() {

                    @Override
                    public void onResponse(ResponseData response) {
                        Logger.d(TAG, "#login => onResponse");
                        if (response.isSuccess()) {
                            if (TextUtils.isEmpty(response.getMessage())) {
                                String message = getStringResource(R.string.reset_success);
                                callback.onResetSuccess(message);
                            } else {
                                callback.onResetSuccess(response.getMessage());
                            }
                        } else {
                            callback.onResetFailure(response.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#login => onErrorResponse");
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                        if (errorResponse != null) {
                            callback.onResetFailure(errorResponse.getMessage());
                        } else {
                            callback.onResetFailure(getStringResource(R.string.network_error));
                        }
                    }
                });
        VolleySequence.getInstance().addRequest(resetPassword);
    }

    public String validateChangePasswordByCode(String code, String newPassword, String confirmNewPassword) {
        String message = Constants.EMPTY_STRING;
        if (TextUtils.isEmpty(code.trim())) {
            message = getStringResource(R.string.code_not_null);
        } else if (TextUtils.isEmpty(newPassword.trim())) {
            message = getStringResource(R.string.new_password_by_code_not_null);
        } else if ((TextUtils.isEmpty(confirmNewPassword.trim()))) {
            message = getStringResource(R.string.confirm_new_password_by_code_not_null);
        } else if (!TextUtils.equals(newPassword, confirmNewPassword)) {
            message = getStringResource(R.string.confirm_different_new_password_by_code);
        } else if (newPassword.length() < 8 || newPassword.length() > 14) {
            message = getStringResource(R.string.password_length);
        }

        return message;
    }

    public void changePasswordByCode(String code, String newPassword, String confirmNewPassword, final IOnChangePasswordCallback callback) {
        String isValid = validateChangePasswordByCode(code, newPassword, confirmNewPassword);
        if (TextUtils.isEmpty(isValid)) {
            Request resetPassword = APICreator.changePasswordByCode(code, newPassword,
                    new Response.Listener<RegisterResponse>() {

                        @Override
                        public void onResponse(RegisterResponse response) {
                            Logger.d(TAG, "#changePasswordByCode => onResponse");
                            if (response.isSuccess()) {
                                if (TextUtils.isEmpty(response.getMessage())) {
                                    String message = getStringResource(R.string.change_password_success);
                                    callback.onChangePasswordSuccess(response.getData(), message);
                                } else {
                                    callback.onChangePasswordSuccess(response.getData(), response.getMessage());
                                }
                            } else {
                                callback.onChangePasswordFailure(response.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Logger.d(TAG, "#changePasswordByCode => onErrorResponse");
                            ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                            if (errorResponse != null) {
                                callback.onChangePasswordFailure(errorResponse.getMessage());
                            } else {
                                callback.onChangePasswordFailure(getStringResource(R.string.network_error));
                            }
                        }
                    });
            VolleySequence.getInstance().addRequest(resetPassword);
        } else {
            callback.onValidateFailure(isValid);
        }
    }

    public void validateInfoUser(InfoUserResponse.InfoUser infoUser, IOnValidate callback) {
        String message = Constants.EMPTY_STRING;
        if (TextUtils.isEmpty(infoUser.getName())) {
            message = getStringResource(R.string.user_name_not_null);
        } else if (!isEmailValid(infoUser.getEmail())) {
            message = getStringResource(R.string.email_validate);
        } else if (infoUser.isChangePassword()) {
            if (TextUtils.isEmpty(infoUser.getCurrentPassword())) {
                message = getStringResource(R.string.current_password_not_null);
            } else if (TextUtils.isEmpty(infoUser.getNewPassword())) {
                message = getStringResource(R.string.new_password_not_null);
            } else if ((TextUtils.isEmpty(infoUser.getConfirmPassword()))) {
                message = getStringResource(R.string.confirm_new_password_not_null);
            } else if (!TextUtils.equals(infoUser.getNewPassword(), infoUser.getConfirmPassword())) {
                message = getStringResource(R.string.confirm_different_new_password);
            } else if (infoUser.getNewPassword().length() < 8 || infoUser.getNewPassword().length() > 14) {
                message = getStringResource(R.string.password_length);
            }
        }
        if (TextUtils.isEmpty(message)) {
            callback.onSuccess();
        } else {
            callback.onFailure(message);
        }
    }

//    public void changePassword(String token, String currentPassword, String newPassword, String confirmNewPassword, final IOnChangePasswordCallback callback) {
//        String isValid = validateInfoUser(currentPassword, newPassword, confirmNewPassword);
//        if (TextUtils.isEmpty(isValid)) {
//            Request resetPassword = APICreator.changePassword(token, currentPassword, newPassword,
//                    new Response.Listener<RegisterResponse>() {
//
//                        @Override
//                        public void onResponse(RegisterResponse response) {
//                            Logger.d(TAG, "#changePassword => onResponse");
//                            if (response.isSuccess()) {
//                                if (TextUtils.isEmpty(response.getMessage())) {
//                                    String message = getStringResource(R.string.change_password_success);
//                                    callback.onChangePasswordSuccess(response.getData(), message);
//                                } else {
//                                    callback.onChangePasswordSuccess(response.getData(), response.getMessage());
//                                }
//                            } else {
//                                callback.onChangePasswordFailure(response.getMessage());
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Logger.d(TAG, "#changePassword => onErrorResponse");
//                            ErrorResponse errorResponse = Utils.parseErrorResponse(error);
//                            if (errorResponse != null) {
//                                callback.onChangePasswordFailure(errorResponse.getMessage());
//                            } else {
//                                callback.onChangePasswordFailure(getStringResource(R.string.network_error));
//                            }
//                        }
//                    });
//            VolleySequence.getInstance().addRequest(resetPassword);
//        } else {
//            callback.onValidateFailure(isValid);
//        }
//    }

    public void removeDeviceToken(String token) {
        String androidId = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Request removeDeviceToken = APICreator.removeDeviceToken(token, androidId,
                new Response.Listener<ResponseData>() {

                    @Override
                    public void onResponse(ResponseData response) {
                        if (response.isSuccess()) {
                            Logger.d(TAG, "#removeDeviceToken => success");
                        } else {
                            Logger.d(TAG, "#removeDeviceToken => failure");
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.d(TAG, "#removeDeviceToken => onErrorResponse");
                        ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                    }
                });
        VolleySequence.getInstance().addRequest(removeDeviceToken);
    }

    public void checkVersionApp(String token, int currentVersion, final IOnCheckVersionAppCallback callback) {
        String deviceId = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Request requestCheckVersionApp = APICreator.checkVersionApp(token, currentVersion, deviceId, new Response.Listener<CheckVersionAppResponse>() {

            @Override
            public void onResponse(CheckVersionAppResponse response) {
                if (response.isSuccess()) {
                    callback.onCheckVersionAppSuccess(response.getData());
                } else {
                    callback.onCheckVersionAppFailure();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onCheckVersionAppFailure();
            }
        });
        VolleySequence.getInstance().addRequest(requestCheckVersionApp);
    }

    public void onGetInfoUser(String token, final IOnGetInfoUserCallback callback) {
        Request resetPassword = APICreator.onGetInfoUser(token, new Response.Listener<InfoUserResponse>() {

            @Override
            public void onResponse(InfoUserResponse response) {
                Logger.d(TAG, "#onGetInfoUser => onErrorResponse");
                if (response != null && response.isSuccess()) {
                    callback.onGetInfoUserSuccess(response.getData());
                } else {
                    callback.onGetInfoUserFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d(TAG, "#onGetInfoUser => onErrorResponse");
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onGetInfoUserFailure(errorResponse.getMessage());
                } else {
                    callback.onGetInfoUserFailure(getStringResource(R.string.network_error));
                }
            }
        });
        VolleySequence.getInstance().addRequest(resetPassword);
    }

    public void uploadImageUser(final InfoUserResponse.InfoUser infoUser, final IOnUploadImage uploadImage) {
        String url = infoUser.getAvatar();
        if (!TextUtils.isEmpty(url)) {
            File file = new File(url);
            if (file.exists()) {
                final String fileName = Utils.getFileName(url);
                AmazonS3Utils.getInstance().upload(file, fileName, new TransferListener() {

                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        if (state == TransferState.COMPLETED) {
                            infoUser.setAvatar(AmazonS3Utils.BASE_IMAGE_URL + fileName);
                            uploadImage.onSuccess();
                        }
                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        Logger.d(TAG, "#onProgressChanged " + bytesCurrent + "/" + bytesTotal);
                    }

                    @Override
                    public void onError(int id, Exception ex) {
                        Logger.d(TAG, "#onError " + ex);
                        infoUser.setAvatar(Constants.EMPTY_STRING);
                        uploadImage.onFailure();

                    }
                });
            } else {
                uploadImage.onSuccess();
            }
        } else {
            uploadImage.onSuccess();
        }

    }

    public void onUpdateInfoUser(String token, InfoUserResponse.InfoUser infoUser, final IOnUpdateInfoUserCallback callback) {
        Request resetPassword = APICreator.onUpdateInfoUser(token, infoUser, new Response.Listener<ResponseData>() {

            @Override
            public void onResponse(ResponseData response) {
                Logger.d(TAG, "#onUpdateInfoUser => onUpdateInfoUser");
                if (response != null && response.isSuccess()) {
                    callback.onOnUpdateInfoUserSuccess();
                } else {
                    callback.onOnUpdateInfoUserFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d(TAG, "#onUpdateInfoUser => onErrorResponse");
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onOnUpdateInfoUserFailure(errorResponse.getMessage());
                } else {
                    callback.onOnUpdateInfoUserFailure(getStringResource(R.string.network_error));
                }
            }
        });
        VolleySequence.getInstance().addRequest(resetPassword);
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void checkInitUser(String token, final IOnGetInitUserCallback callback) {
        Request resetPassword = APICreator.onCheckInitUser(token, new Response.Listener<InitUserResponse>() {

            @Override
            public void onResponse(InitUserResponse response) {
                Logger.d(TAG, "#onCheckInitUser => onResponse");
                if (response != null && response.isSuccess()) {
                    callback.onGetInitUserSuccess(response.getData());
                } else {
                    callback.onGetInitUserFailure(response.getMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d(TAG, "#onCheckInitUser => onErrorResponse");
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onGetInitUserFailure(errorResponse.getMessage());
                } else {
                    callback.onGetInitUserFailure(getStringResource(R.string.network_error));
                }
            }
        });
        VolleySequence.getInstance().addRequest(resetPassword);
    }
}
