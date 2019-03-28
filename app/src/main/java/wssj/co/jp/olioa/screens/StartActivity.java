package wssj.co.jp.olioa.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import wssj.co.jp.olioa.R;

/**
 * Created by tuanle on 8/8/17.
 */

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_fragment);
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}