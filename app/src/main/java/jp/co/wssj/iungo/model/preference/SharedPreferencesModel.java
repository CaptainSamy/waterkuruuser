package jp.co.wssj.iungo.model.preference;

import android.content.Context;
import android.text.TextUtils;

import jp.co.wssj.iungo.model.BaseModel;

/**
 * Created by HieuPT on 5/17/2017.
 */

public class SharedPreferencesModel extends BaseModel {

    private static final String KEY_TOKEN = "token";

    private static final String KEY_EXPIRE_DATE = "expire_date";

    private static final String KEY_SESSION = "session";

    private static final String KEY_SERVICE_ID = "service_id";

    private static final String KEY_SERVICE_COMPANY_ID = "service_company_id";

    private static final String KEY_USER_ID = "user_id";

    private static final String KEY_PASSWORD = "password";

    private static final String KEY_USER_NAME = "user_name";

    private static final String KEY_EMAIL = "email";

    private static final String KEY_LAST_SERVICE_ID = "last_service_company_id";

    private static final String TYPE_LOGIN = "type_login";

    private static final String PHOTO_URL = "photo_user";

    private final SharedPreferencesHelper mSharedPreference;

    public SharedPreferencesModel(Context context) {
        super(context);
        mSharedPreference = SharedPreferencesHelper.getInstance();
    }

    public void putToken(String token) {
        if (!TextUtils.isEmpty(token)) {
            mSharedPreference.put(KEY_TOKEN, token);
        }
    }

    public String getToken() {
        return SharedPreferencesHelper.getInstance().getString(KEY_TOKEN);
    }

    public void putExpireDate(long expire) {
        mSharedPreference.put(KEY_EXPIRE_DATE, expire);
    }

    public long getExpireDate() {
        return mSharedPreference.getLong(KEY_EXPIRE_DATE);
    }

    public void putSession(int session) {
        mSharedPreference.put(KEY_SESSION, session);
    }

    public int getSession() {
        return mSharedPreference.getInt(KEY_SESSION);
    }

    public void putServiceId(int serviceId) {
        mSharedPreference.put(KEY_SERVICE_ID, serviceId);
    }

    public int getServiceId() {
        return mSharedPreference.getInt(KEY_SERVICE_ID);
    }

    public void putServiceCompanyId(int serviceCompanyId) {
        mSharedPreference.put(KEY_SERVICE_COMPANY_ID, serviceCompanyId);
    }

    public int getServiceCompanyId() {
        return mSharedPreference.getInt(KEY_SERVICE_COMPANY_ID);
    }

    public void putUserName(String userName) {
        mSharedPreference.put(KEY_USER_NAME, userName);
    }

    public String getUserName() {
        return mSharedPreference.getString(KEY_USER_NAME);
    }

    public void putEmail(String userName) {
        mSharedPreference.put(KEY_EMAIL, userName);
    }

    public String getEmail() {
        return mSharedPreference.getString(KEY_EMAIL);
    }

    public void putLastServiceId(int serviceId) {
        mSharedPreference.put(KEY_LAST_SERVICE_ID, serviceId);
    }

    public int getLastServiceId() {
        return mSharedPreference.getInt(KEY_LAST_SERVICE_ID);
    }

    public void putUserId(String userId) {
        if (!TextUtils.isEmpty(userId)) {
            mSharedPreference.put(KEY_USER_ID, userId);
        }
    }

    public String getUserId() {
        return mSharedPreference.getString(KEY_USER_ID);

    }

    public void putPassword(String password) {
        if (!TextUtils.isEmpty(password)) {
            mSharedPreference.put(KEY_PASSWORD, password);
        }
    }

    public String getPassword() {
        return mSharedPreference.getString(KEY_PASSWORD);
    }

    public void putTypeLogin(int typeLogin) {
        mSharedPreference.put(TYPE_LOGIN, typeLogin);
    }

    public int getTypeLogin() {
        return mSharedPreference.getInt(TYPE_LOGIN);
    }

    public void putPhotoUrl(String photo) {
        mSharedPreference.put(PHOTO_URL, photo);
    }

    public String getPhotoUrl() {
        return mSharedPreference.getString(PHOTO_URL);
    }

    public void clearSession() {
        mSharedPreference.remove(KEY_SESSION);
        mSharedPreference.remove(KEY_SERVICE_ID);
        mSharedPreference.remove(KEY_SERVICE_COMPANY_ID);
        mSharedPreference.remove(KEY_USER_NAME);
        mSharedPreference.remove(KEY_EMAIL);
        mSharedPreference.remove(KEY_LAST_SERVICE_ID);
        mSharedPreference.remove(KEY_USER_ID);
        mSharedPreference.remove(KEY_PASSWORD);
        mSharedPreference.remove(TYPE_LOGIN);
        mSharedPreference.remove(PHOTO_URL);

    }

    public void clearAll() {
        mSharedPreference.clear();
    }
}
