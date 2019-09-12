package wssj.co.jp.olioa.screens;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import wssj.co.jp.olioa.R;

/**
 * Created by tuanle on 8/8/17.
 */

public class ImageDetailActivity extends AppCompatActivity {
    public static final String ARG_IMAGE = "arg_url_image";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_activity);

        ImageView imageDetail = (ImageView) findViewById(R.id.imageContent);
        String urlImage = "";
        if (getIntent() != null && getIntent().getExtras() != null) {
            urlImage = getIntent().getExtras().getString(ARG_IMAGE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageDetail.setTransitionName(getString(R.string.image_transaction_name));
        }
        ImageView buttonBack = (ImageView) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        Glide.with(this)
                .load(urlImage)
                .dontAnimate()
                .listener(new RequestListener<String, GlideDrawable>() {

                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startPostponedEnterTransition();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startPostponedEnterTransition();
                        }
                        return false;
                    }
                })
                .into(imageDetail);

    }
}
