package wssj.co.jp.point.model.entities;

/**
 * Created by Nguyen Huu Ta on 7/7/2017.
 */

public class StatusMemoData {

    public static final int FLAG_NOT_MODIFY = 1;

    public static final int FLAG_MODIFIED = 2;

    private String mUrlOriginImage;

    private String mPathNewImage;

    private int mStatus;

    private boolean isUploadAWSSuccess;

    public StatusMemoData(String originImage) {
        this.mUrlOriginImage = originImage;
        this.mPathNewImage = originImage;
        this.mStatus = FLAG_NOT_MODIFY;
    }

    public void setPathNewImage(String mPathNewImage) {
        this.mPathNewImage = mPathNewImage;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public void setUrlOriginImage(String mUrlOriginImage) {
        this.mUrlOriginImage = mUrlOriginImage;
    }

    public String getUrlOriginImage() {
        return mUrlOriginImage;
    }

    public String getPathNewImage() {
        return mPathNewImage;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setIsUploadAWSSuccess(boolean isUploadAWSSuccess) {
        this.isUploadAWSSuccess = isUploadAWSSuccess;
    }

    public boolean isUploadAWSSuccess() {
        return isUploadAWSSuccess;
    }

}
