package wssj.co.jp.olioa.utils;

import android.util.Log;

import wssj.co.jp.olioa.BuildConfig;

/**
 * Created by HieuPT on 3/22/2017.
 */

public final class Logger {

    private static final int LOG_LEVEL = BuildConfig.DEBUG ? Log.VERBOSE : Log.INFO;

    public static void v(String tag, String message) {
        if (message == null)return;
        if (Log.VERBOSE >= LOG_LEVEL) {
            Log.v(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (message == null)return;
        if (Log.DEBUG >= LOG_LEVEL) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (message == null)return;
        if (Log.INFO >= LOG_LEVEL) {
            Log.i(tag, message);
        }
    }

    public static void w(String tag, String message) {
        if (message == null)return;
        if (Log.WARN >= LOG_LEVEL) {
            Log.w(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (message == null)return;
        if (Log.ERROR >= LOG_LEVEL) {
            Log.e(tag, message);
        }
    }

    private Logger() {
    }
}
