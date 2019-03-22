package wssj.co.jp.olioa.model;

import com.google.gson.annotations.SerializedName;

import wssj.co.jp.olioa.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

public class ErrorMessage {

    private final int mCode;
    @SerializedName("message")
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
