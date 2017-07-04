package wssj.co.jp.point.model;

import wssj.co.jp.point.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

public class ErrorMessage {

    private final int mCode;

    private final String mMessage;

    public ErrorMessage(String message) {
        this(0, message);
    }

    public ErrorMessage(int code) {
        this(code, null);
    }

    public ErrorMessage(int code, String message) {
        mCode = code;
        mMessage = message != null ? message : Constants.EMPTY_STRING;
    }

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    @Override
    public String toString() {
        return "" + mCode + " - " + mMessage;
    }
}
