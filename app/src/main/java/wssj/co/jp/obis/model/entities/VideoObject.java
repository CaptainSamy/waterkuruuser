package wssj.co.jp.obis.model.entities;

import android.text.TextUtils;

import wssj.co.jp.obis.utils.Constants;

/**
 * Created by Nguyen Huu Ta on 11/10/2017.
 */

public class VideoObject {

    public VideoObject(String urlVideo, String frameVideo, String duration) {
        mUrlVideo = urlVideo;
        mFrameVideo = frameVideo;
        mDuration = duration;
    }

    private String mUrlVideo;

    private String mFrameVideo;

    private String mDuration;

    public String getUrlVideo() {
        return TextUtils.isEmpty(mUrlVideo) ? Constants.EMPTY_STRING : mUrlVideo;
    }

    public void setUrlVideo(String mUrlVideo) {
        this.mUrlVideo = mUrlVideo;
    }

    public String getFrameVideo() {
        return TextUtils.isEmpty(mFrameVideo) ? Constants.EMPTY_STRING : mFrameVideo;
    }

    public void setUrlFramgVideo(String frameVideo) {
        this.mFrameVideo = frameVideo;
    }

    public String getDuration() {
        return mDuration;
    }
}
