package wssj.co.jp.olioa.model;

import com.google.gson.annotations.SerializedName;

import wssj.co.jp.olioa.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 10/5/2017.
 */

public class ErrorMessage {

    @SerializedName("error_code")
    private String mCode;

    @SerializedName("message")
    private final String mMessage;

    public ErrorMessage(String message) {
        this("", message);
    }

    public ErrorMessage(String code, String message) {
        mCode = code;
        mMessage = message != null ? message : Constants.EMPTY_STRING;
    }

    public String getCode() {
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
