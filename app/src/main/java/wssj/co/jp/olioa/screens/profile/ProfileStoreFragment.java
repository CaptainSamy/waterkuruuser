package wssj.co.jp.olioa.screens.profile;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import de.hdodenhof.circleimageview.CircleImageView;
import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.timeline.ProfileResponse;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.timeline.timelinedetail.TimeLineDetailFragment;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 6/11/2017.
 */

public class ProfileStoreFragment extends BaseFragment<IProfileStoreView, ProfileStorePresenter> implements IProfileStoreView {

    private static final String TAG = "ProfileStoreFragment";

    private CircleImageView mAvatarStore;

    private ImageView mHeaderPicture;

    private TextView mStoreName;

    private WebView mWebInfoStore;

    public static ProfileStoreFragment newInstance(Bundle args) {
        ProfileStoreFragment fragment = new ProfileStoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_PROFILE;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    protected ProfileStorePresenter onCreatePresenter(IProfileStoreView view) {
        return new ProfileStorePresenter(view);
    }

    @Override
    protected IProfileStoreView onCreateView() {
        return this;
    }

    @Override
    public String getAppBarTitle() {
        if (getArguments() != null) {
            return getArguments().getString(TimeLineDetailFragment.KEY_STORE_NAME);
        }
        return Constants.EMPTY_STRING;
    }

    @Override
    protected void initViews(View rootView) {
        mHeaderPicture = (ImageView) rootView.findViewById(R.id.header_picture);
        mAvatarStore = (CircleImageView) rootView.findViewById(R.id.avatarStore);
        mStoreName = (TextView) rootView.findViewById(R.id.storeName);
        mWebInfoStore = (WebView) rootView.findViewById(R.id.webInfoStore);

    }

    @Override
    protected void initAction() {
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            int managerId = getArguments().getInt(TimeLineDetailFragment.KEY_USER_MANAGER_ID);
            getPresenter().getProfileStore(managerId);
        }
    }

    @Override
    public void getProfileSuccess(ProfileResponse.Profile profile) {
        if (profile != null) {
            mStoreName.setText(profile.getStoreName());
            if (!TextUtils.isEmpty(profile.getLogoMain())) {
                Glide.with(getActivityContext())
                        .load(profile.getLogoMain())
                        .into(new SimpleTarget<GlideDrawable>() {

                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                mHeaderPicture.setImageResource(R.drawable.loading);
                            }

                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                mHeaderPicture.setImageDrawable(resource);
                            }
                        });
            } else {
                mHeaderPicture.setImageResource(R.drawable.no_image_background);
            }
            Utils.fillImage(getActivityContext(), profile.getLogo(), mAvatarStore, R.drawable.icon_user);
            mWebInfoStore.loadDataWithBaseURL("", profile.getInformation(), "text/html", "UTF-8", "");
        }
    }

    @Override
    public void getProfileFailure(String message) {

    }
}
