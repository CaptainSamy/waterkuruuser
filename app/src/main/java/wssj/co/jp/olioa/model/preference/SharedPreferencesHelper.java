package wssj.co.jp.olioa.model.preference;

import android.content.Context;
import android.content.SharedPreferences;

import wssj.co.jp.olioa.App;
import wssj.co.jp.olioa.utils.Constants;

/**
 * Created by HieuPT on 5/17/2017.
 */

final class SharedPreferencesHelper {

    private static final String SHARED_PREFERENCES = "preferences";

    private static SharedPreferencesHelper sInstance;

    synchronized static SharedPreferencesHelper getInstance() {
        if (sInstance == null) {
            sInstance = new SharedPreferencesHelper();
        }
        return sInstance;
    }

    private final SharedPreferences mSharedPreferences;

    private SharedPreferencesHelper() {
        mSharedPreferences = App.getInstance().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return mSharedPreferences.edit();
    }

    String getString(String key) {
        return mSharedPreferences.getString(key, Constants.EMPTY_STRING);
    }

    long getLong(String key) {
        return mSharedPreferences.getLong(key, 0);
    }

    int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    void put(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    void put(String key, long value) {
        getEditor().putLong(key, value).commit();
    }

    void put(String key, int value) {
        getEditor().putInt(key, value).commit();
    }

    void put(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();
    }

    void remove(String key) {
        getEditor().remove(key).commit();
    }

    public void clear() {
        getEditor().clear().commit();
    }

}
