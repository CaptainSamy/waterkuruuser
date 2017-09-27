package jp.co.wssj.iungo.screens.phototimeline;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.List;

import jp.co.wssj.iungo.R;
import jp.co.wssj.iungo.screens.base.BaseDialog;
import jp.co.wssj.iungo.utils.Utils;
import jp.co.wssj.iungo.widget.CirclePageIndicator;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

public class PhotoTimelineDialog extends BaseDialog<IPhotoTimelineView, PhotoTimelinePresenter>
        implements IPhotoTimelineView, View.OnClickListener {

    private ViewPager mViewPagePhoto;

    private PhotoTimelineAdapter mAdapter;

    private ImageView mButtonBack;

    private CirclePageIndicator mIndicator;

    public PhotoTimelineDialog(@NonNull Context context) {
        super(context, R.style.DialogTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.0f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setContentView(R.layout.photo_dialog_view_pager);
        initView();
        initAction();
    }

    private void initView() {
        mViewPagePhoto = (ViewPager) findViewById(R.id.vpPhoto);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mButtonBack = (ImageView) findViewById(R.id.ivBack);
        mIndicator.setFillColor(ContextCompat.getColor(getContext(), R.color.colorMain));
        mIndicator.setPageColor(ContextCompat.getColor(getContext(), R.color.colorTextDisable));
        mIndicator.setRadius(Utils.convertDpToPixel(getContext(), 4));
        mIndicator.setStrokeWidth(0);
    }

    private void initAction() {
        mButtonBack.setOnClickListener(this);
    }

    @Override
    protected IPhotoTimelineView onCreateView() {
        return this;
    }

    @Override
    protected PhotoTimelinePresenter onCreatePresenter(IPhotoTimelineView view) {
        return new PhotoTimelinePresenter(view);
    }

    public void showImages(List<String> urlImages, int positionClick) {
        if (urlImages.size() == 1) {
            mIndicator.setVisibility(View.GONE);
        } else {
            mIndicator.setVisibility(View.VISIBLE);
        }
        if (mAdapter == null) {
            mAdapter = new PhotoTimelineAdapter(getContext(), urlImages);
            mViewPagePhoto.setAdapter(mAdapter);
            mIndicator.setViewPager(mViewPagePhoto);
        } else {
            mAdapter.refreshPhoto(urlImages);
        }
        mViewPagePhoto.setCurrentItem(positionClick);
        show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                getPresenter().dismissDialog();
                break;
        }
    }
}

