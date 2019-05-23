package wssj.co.jp.obis.screens.chat.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import de.hdodenhof.circleimageview.CircleImageView;
import wssj.co.jp.obis.R;
import wssj.co.jp.obis.model.timeline.ProfileResponse;
import wssj.co.jp.obis.screens.IActivityCallback;
import wssj.co.jp.obis.screens.IMainView;
import wssj.co.jp.obis.screens.base.BaseDialog;
import wssj.co.jp.obis.screens.timeline.timelinedetail.TimeLineDetailFragment;
import wssj.co.jp.obis.utils.Constants;
import wssj.co.jp.obis.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 7/11/2017.
 */

public class DialogProfile extends BaseDialog<IDialogProfileView, DialogProfilePresenter> implements IDialogProfileView, View.OnClickListener {

    private ImageView mImageBackground, mButtonCancel;

    private CircleImageView mAvatar;

    private TextView mButtonProfile, mButtonHome, mNickName, mDescription;

    private int mManagerId;

    private IActivityCallback mMainCallback;

    public DialogProfile(@NonNull Context context, int managerId, IActivityCallback mainCallback) {
        super(context);
        mManagerId = managerId;
        mMainCallback = mainCallback;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_profile);
        initView();
        initAction();
        initData();
    }

    @Override
    protected DialogProfilePresenter onCreatePresenter(IDialogProfileView view) {
        return new DialogProfilePresenter(view);
    }

    @Override
    protected IDialogProfileView onCreateView() {
        return this;
    }

    private void initView() {
        mImageBackground = (ImageView) findViewById(R.id.imageBackground);
        mButtonCancel = (ImageView) findViewById(R.id.buttonCancel);
        mAvatar = (CircleImageView) findViewById(R.id.imageAvatar);
        mButtonProfile = (TextView) findViewById(R.id.buttonProfile);
        mButtonHome = (TextView) findViewById(R.id.buttonHome);
        mNickName = (TextView) findViewById(R.id.nickName);
        mDescription = (TextView) findViewById(R.id.textDescription);

    }

    private void initAction() {
        mButtonCancel.setOnClickListener(this);
        mButtonHome.setOnClickListener(this);
        mButtonProfile.setOnClickListener(this);
    }

    public void initData() {
        getPresenter().getProfileStore(mManagerId);
    }

    public void requestApi(int managerId) {
        if (mManagerId != managerId) {
            clearData();
            mManagerId = managerId;
            getPresenter().getProfileStore(mManagerId);
        }
        show();
    }

    private void clearData() {
        mUrlBackground = mStoreName = mUrlAvatar = Constants.EMPTY_STRING;
        mNickName.setText(mStoreName);
        mImageBackground.setImageResource(R.drawable.no_image_background);
        mAvatar.setImageResource(R.drawable.no_image_background);
        mDescription.setText(Constants.EMPTY_STRING);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCancel:
                dismiss();
                break;
            case R.id.buttonHome:
                Bundle bundle = new Bundle();
                bundle.putInt(TimeLineDetailFragment.KEY_USER_MANAGER_ID, mManagerId);
                bundle.putString(TimeLineDetailFragment.KEY_IMAGE_STORE, mUrlAvatar);
                bundle.putString(TimeLineDetailFragment.KEY_STORE_NAME, mStoreName);
                mMainCallback.displayScreen(IMainView.FRAGMENT_TIMELINE_DETAIL, true, true, bundle);
                dismiss();
                break;
            case R.id.buttonProfile:
                Bundle bundleProfile = new Bundle();
                bundleProfile.putInt(TimeLineDetailFragment.KEY_USER_MANAGER_ID, mManagerId);
                bundleProfile.putString(TimeLineDetailFragment.KEY_STORE_NAME, mStoreName);
                mMainCallback.displayScreen(IMainView.FRAGMENT_PROFILE, true, true, bundleProfile);
                dismiss();
                break;
        }

    }

    private String mUrlAvatar, mStoreName, mUrlBackground;

    @Override
    public void getProfileSuccess(ProfileResponse.Profile profile) {
        if (profile != null) {
            mUrlAvatar = profile.getLogo();
            mStoreName = profile.getStoreName();
            mUrlBackground = profile.getLogoMain();
            mNickName.setText(mStoreName);
            if (TextUtils.isEmpty(mUrlBackground)) {
                mImageBackground.setImageResource(R.drawable.no_image_background);
            } else {
                Glide.with(getContext())
                        .load(mUrlBackground)
                        .into(new SimpleTarget<GlideDrawable>() {

                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                mImageBackground.setImageResource(R.drawable.loading);
                            }

                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                mImageBackground.setImageDrawable(resource);
                            }
                        });
            }
            Utils.fillImage(getContext(), profile.getLogo(), mAvatar, R.drawable.icon_user);
            mDescription.setText(profile.getDescription());
        }
    }

    @Override
    public void getProfileFailure(String message) {
        dismiss();
    }
}
