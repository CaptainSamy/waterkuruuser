package jp.co.wssj.iungo.screens.pushobject;

/**
 * Created by Nguyen Huu Ta on 8/8/2017.
 */

public class ObjectPush {

    public ObjectPush(String code) {
        mCode = code;
        mSaveTime = System.currentTimeMillis() + 24 * 3600 * 1000;
    }

    private String mCode;

    private long mSaveTime;

    public String getCode() {
        return mCode;
    }

    public void setCode(String mCode) {
        this.mCode = mCode;
    }

    public long getSaveTime() {
        return mSaveTime;
    }

    public void setSaveTime(long mSaveTime) {
        this.mSaveTime = mSaveTime;
    }
}
