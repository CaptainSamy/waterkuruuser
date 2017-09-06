package jp.co.wssj.iungo.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import jp.co.wssj.iungo.R;

/**
 * Created by Nguyen Huu Ta on 6/9/2017.
 */

public class QuestionNaireActivity extends AppCompatActivity {

    public static final String KEY_QUESTION_NAIRE = "question_naire";

    private String mUrl;

    private WebView mWebViewQuestionNaire;

    private TextView mTextFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_naire_activity);
        if (getIntent().getExtras() != null) {
            mUrl = getIntent().getExtras().getString(KEY_QUESTION_NAIRE);
        }
        mTextFinish = (TextView) findViewById(R.id.tvFinish);
        mWebViewQuestionNaire = (WebView) findViewById(R.id.webViewQuestionNaire);
        mWebViewQuestionNaire.getSettings().setJavaScriptEnabled(true);
        mWebViewQuestionNaire.getSettings().setBuiltInZoomControls(true);
        mWebViewQuestionNaire.getSettings().setDisplayZoomControls(false);

        mWebViewQuestionNaire.getSettings().setLoadWithOverviewMode(true);
        mWebViewQuestionNaire.getSettings().setUseWideViewPort(true);
        mWebViewQuestionNaire.setWebViewClient(new WebViewClient());
        mWebViewQuestionNaire.loadUrl(mUrl);
        mTextFinish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
