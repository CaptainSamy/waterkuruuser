package wssj.co.jp.obis.model.entities;

import wssj.co.jp.obis.utils.Utils;

/**
 * Created by HieuPT on 6/5/2017.
 */

public class UpdateMemoPhotoData {

    public static final int FLAG_DELETE = 0;

    public static final int FLAG_NOT_MODIFY = 1;

    public static final int FLAG_MODIFIED = 2;

    public static final String PARAM_DELETE = "DELETE";

    private int mUpdateFlag;

    private String mImagePath;

    private final int mFlagOnDelete;

    public UpdateMemoPhotoData(int flagOnDelete) {
        mFlagOnDelete = flagOnDelete;
        mUpdateFlag = FLAG_NOT_MODIFY;
    }


    public void setImagePath(String imgPath) {
        mImagePath = imgPath;
    }

    public void setUpdateFlag(int updateFlag) {
        mUpdateFlag = updateFlag;
    }

    public void setToFlagOnDelete() {
        mUpdateFlag = mFlagOnDelete;
    }

    public byte[] getImage() {
        return Utils.getByteArrayFromFile(mImagePath);
    }

    public int getUpdateFlag() {
        return mUpdateFlag;
    }
}
