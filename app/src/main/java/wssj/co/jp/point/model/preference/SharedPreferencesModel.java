package wssj.co.jp.point.model.preference;

import android.content.Context;
import android.text.TextUtils;

import wssj.co.jp.point.model.BaseModel;

/**
 * Created by HieuPT on 5/17/2017.
 */

public class SharedPreferencesModel extends BaseModel {

    private static final String KEY_TOKEN = "token";

    private static final String KEY_TOKEN_AWS = "token_aws";

    private static final String KEY_EXPIRE_DATE = "expire_date";

    private static final String KEY_EXPIRE_DATE_AWS = "expire_date_aws";

    private static final String KEY_SESSION = "session";

    private static final String KEY_SESSION_AWS = "session_aws";

    private static final String KEY_SERVICE_ID = "service_id";

    private static final String KEY_SERVICE_ID_AWS = "service_id_aws";

    private static final String KEY_SERVICE_COMPANY_ID = "service_company_id";

    private static final String KEY_SERVICE_COMPANY_ID_AWS = "service_company_id_aws";

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

    public void clearSession() {
        mSharedPreference.remove(KEY_SESSION);
        mSharedPreference.remove(KEY_SERVICE_ID);
        mSharedPreference.remove(KEY_SERVICE_COMPANY_ID);

        mSharedPreference.remove(KEY_SESSION_AWS);
        mSharedPreference.remove(KEY_SERVICE_ID_AWS);
        mSharedPreference.remove(KEY_SERVICE_COMPANY_ID_AWS);
    }

    public void clearAll() {
        mSharedPreference.clear();
    }
}
