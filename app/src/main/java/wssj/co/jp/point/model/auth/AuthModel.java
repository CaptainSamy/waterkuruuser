package wssj.co.jp.point.model.auth;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.BaseModel;
import wssj.co.jp.point.model.ErrorMessage;
import wssj.co.jp.point.model.ResponseData;
import wssj.co.jp.point.utils.Constants;
import wssj.co.jp.point.utils.Logger;
import wssj.co.jp.point.model.ErrorResponse;
import wssj.co.jp.point.utils.Utils;
import wssj.co.jp.point.utils.VolleySequence;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

public class AuthModel extends BaseModel {

    public interface IValidateLoginCallback {

        void validateSuccess(String userId, String password);

        void validateFailure(ErrorMessage errorMessage);
    }

    public interface IRegisterCallback {

        void validateSuccess(String userName, String password, String name, String email);

        void validateFailure(ErrorMessage errorMessage, int code);

        void onRegisterSuccess(RegisterData data);

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

        void onChangePasswordSuccess(String message);

    }

    private static final String TAG = "AuthModel";

    private static final int MIN_EMAIL_LENGTH = 3;

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

    public void validateRegisterAccount(String userName, String userId, String password, String confirmPassword, String email, IRegisterCallback callback) {
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
        } else if (password.length() < 8 || password.length() > 16) {
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
            callback.validateSuccess(userId, password, userName, email);
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
        VolleySequence.getInstance().addRequest(loginRequest);
    }

    public void registerAWS(String userName, String password, String name, String email, final IRegisterCallback callback) {
        Request registerRequest = APICreator.getRegisterAWSRequest(userName, password, name, email, new Response.Listener<RegisterResponse>() {

            @Override
            public void onResponse(RegisterResponse response) {
                Logger.d(TAG, "#register onResponse ");
                if (response.isSuccess()) {
                    callback.onRegisterSuccess(response.getData());
                } else {
                    ErrorMessage errorMessage = new ErrorMessage(response.getMessage());
                    callback.onRegisterFailure(errorMessage);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d(TAG, "#register onErrorResponse");
                ErrorResponse errorResponse = Utils.parseErrorResponse(error);
                if (errorResponse != null) {
                    callback.onRegisterFailure(new ErrorMessage(errorResponse.getMessage()));
                } else {
                    callback.onRegisterFailure(new ErrorMessage(getStringResource(R.string.network_error)));
                }
            }
        });
        VolleySequence.getInstance().addRequest(registerRequest);
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
                            String message = getStringResource(R.string.reset_success);
                            callback.onResetSuccess(message);
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

    public String validateChangePassword(String code, String newPassword, String confirmNewPassword) {
        String message = Constants.EMPTY_STRING;
        if (TextUtils.isEmpty(code.trim())) {
            message = getStringResource(R.string.code_not_null);
        } else if (TextUtils.isEmpty(newPassword.trim())) {
            message = getStringResource(R.string.new_password_not_null);
        } else if ((TextUtils.isEmpty(confirmNewPassword.trim()))) {
            message = getStringResource(R.string.confirm_password_not_null);
        } else if (!TextUtils.equals(newPassword, confirmNewPassword)) {
            message = getStringResource(R.string.confirm_different_new_password);
        }
        return message;
    }

    public void changePassword(String code, String newPassword, String confirmNewPassword, IOnChangePasswordCallback callback) {
        String isValid = validateChangePassword(code, newPassword, confirmNewPassword);
        if (TextUtils.isEmpty(isValid)) {
            //TODO call API change password
            callback.onChangePasswordSuccess(getStringResource(R.string.change_password_success));
        } else {
            callback.onValidateFailure(isValid);
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
