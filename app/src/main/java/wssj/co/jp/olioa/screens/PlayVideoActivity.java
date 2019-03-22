package wssj.co.jp.olioa.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;

import wssj.co.jp.olioa.R;

/**
 * Created by Nguyen Huu Ta on 6/10/2017.
 */

public class PlayVideoActivity extends AppCompatActivity implements EasyVideoCallback {

    public static final String KEY_URL_VIDEO = "url_video";

    private EasyVideoPlayer mPlayVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_video_activity);
        mPlayVideo = (EasyVideoPlayer) findViewById(R.id.player);
        mPlayVideo.setThemeColorRes(R.color.colorAccent);
        mPlayVideo.setCallback(this);
        Intent intent = getIntent();
        String url = intent.getStringExtra(KEY_URL_VIDEO);
        if (!TextUtils.isEmpty(url)) {
            mPlayVideo.setSource(Uri.parse(url));
        } else {
            finish();
        }
    }

    @Override
    public void onStarted(EasyVideoPlayer player) {
    }

    @Override
    public void onPaused(EasyVideoPlayer player) {
        player.pause();
    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {

    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }
}
