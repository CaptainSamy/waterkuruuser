package wssj.co.jp.olioa.screens.changepassword;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.model.entities.UserResponse;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.screens.dialogchoosen.DialogChoose;
import wssj.co.jp.olioa.screens.dialogerror.DialogMessage;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.ImmediateResultCameraModule;
import wssj.co.jp.olioa.utils.Logger;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 5/7/2017.
 */

public class ChangeUserInfoFragment extends BaseFragment<IChangeUserInfoView, ChangeUserInfoPresenter>
        implements IChangeUserInfoView, View.OnClickListener {

    private static final String TAG = "ChangeAccountFragment";

    private static final int REQUEST_CODE_PICKER_PHOTO_1 = 100;

    private static final int REQUEST_CODE_CAMERA_PHOTO_1 = 200;
    public static final String ARG_FROM_MENU = "arg_check_in";

    private EditText mInputUserName, mInputEmail;

    private EditText mInputCurrentPassword, mInputNewPassword, mInputConfirmPassword;

    private TextView mButtonChangePassword, mButtonSkip, mButtonLogin, mButtonChangeAccount;

    private TextView mTextChangePassword;

    private RadioGroup mRadioGroupSex;

    private RadioButton mRadioMale, mRadioFemale;

    private LinearLayout mLayoutChangePassword;

    private ImageView mImageAvatar;

    private int mSex;

    private DialogChoose mDialogChoose;

    private ImmediateResultCameraModule mCameraModule;

    private UserResponse mInfoUser;

    private String mPathAvatar;

    private boolean isFromMenu;

    public static ChangeUserInfoFragment newInstance(Bundle args) {

        ChangeUserInfoFragment fragment = new ChangeUserInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_CHANGE_PASSWORD;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_change_password;
    }

    @Override
    public boolean isDisplayActionBar() {
        if (getArguments() != null) {
            isFromMenu = getArguments().getBoolean(ARG_FROM_MENU);
        }
        return isFromMenu;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_change_password);
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return false;
    }

    @Override
    protected ChangeUserInfoPresenter onCreatePresenter(IChangeUserInfoView view) {
        return new ChangeUserInfoPresenter(view);
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    public int getNavigationMenuId() {
        return R.id.menu_change_password;
    }

    @Override
    protected IChangeUserInfoView onCreateView() {
        return this;
    }

    @Override
    protected boolean isRetainState() {
        return false;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        mInputCurrentPassword = (EditText) rootView.findViewById(R.id.inputCurrentPassword);
        mInputNewPassword = (EditText) rootView.findViewById(R.id.inputNewPassword);
        mButtonChangeAccount = rootView.findViewById(R.id.buttonChangeAccount);
        mInputConfirmPassword = (EditText) rootView.findViewById(R.id.inputConfirmPassword);
        mButtonChangePassword = (TextView) rootView.findViewById(R.id.buttonChangePassword);
        mButtonSkip = (TextView) rootView.findViewById(R.id.buttonSkip);
        mButtonLogin = (TextView) rootView.findViewById(R.id.buttonLogin);
        mInputUserName = (EditText) rootView.findViewById(R.id.etUserName);
        mInputEmail = (EditText) rootView.findViewById(R.id.etEmail);
        mRadioGroupSex = (RadioGroup) rootView.findViewById(R.id.radioGroupSex);
        mRadioMale = (RadioButton) rootView.findViewById(R.id.radioMale);
        mRadioFemale = (RadioButton) rootView.findViewById(R.id.radioFeMale);
        mTextChangePassword = (TextView) rootView.findViewById(R.id.tvChangePassword);
        mLayoutChangePassword = (LinearLayout) rootView.findViewById(R.id.layoutChangePassword);
        mImageAvatar = (ImageView) rootView.findViewById(R.id.ivAvatar);
        mDialogChoose = new DialogChoose(getActivityContext());
        if (isFromMenu) {
            mButtonSkip.setVisibility(View.GONE);
            mButtonLogin.setVisibility(View.GONE);
        } else {
            mButtonSkip.setVisibility(View.VISIBLE);
            mButtonLogin.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void initAction() {
        super.initAction();
        mButtonChangeAccount.setOnClickListener(this);
        mTextChangePassword.setOnClickListener(this);
        mButtonChangePassword.setOnClickListener(this);
        mButtonSkip.setOnClickListener(this);
        mButtonLogin.setOnClickListener(this);
        mImageAvatar.setOnClickListener(this);
        mRadioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == mRadioMale.getId()) {
                    mSex = Constants.Register.MALE;
                } else {
                    mSex = Constants.Register.FE_MALE;
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        if (isFromMenu) {
            getPresenter().onGetInfoUser();
        } else {
            mInfoUser = new UserResponse();
            mButtonChangeAccount.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetInfoUserSuccess(UserResponse infoUser) {
        if (infoUser != null) {
            if (infoUser.getChangeAccount() == 0) {
                mButtonChangeAccount.setVisibility(View.VISIBLE);
            }
            mInfoUser = infoUser;
            mInputUserName.setText(infoUser.getName());
            mInputEmail.setText(infoUser.getEmail());
            if (infoUser.getSex() == Constants.Register.MALE) {
                mRadioMale.setChecked(true);
            } else if (infoUser.getSex() == Constants.Register.FE_MALE) {
                mRadioFemale.setChecked(true);
            }
            if (!TextUtils.isEmpty(infoUser.getAvatar())) {
                mPathAvatar = infoUser.getAvatar();
                fillImage(mPathAvatar);
            }
        } else {
            mInfoUser = new UserResponse();
        }
    }

    @Override
    public void onValidateFailure(String message) {
        showToast(message);
    }

    @Override
    public void onChangePasswordSuccess(String message) {
        DialogMessage dialogMessage = new DialogMessage(getActivityContext(), new DialogMessage.IOnClickListener() {
            @Override
            public void buttonYesClick() {
                backToPreviousScreen();
            }

            @Override
            public void buttonCancelClick() {

            }
        });
        dialogMessage.initData(message, getString(R.string.OK));
        dialogMessage.show();
    }

    @Override
    public void onChangePasswordFailure(String message) {
        showToast(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonChangeAccount:
                getActivityCallback().displayScreen(IMainView.FRAGMENT_CHANGE_ACCOUNT, true, true);
                break;
            case R.id.tvChangePassword:
                if (mLayoutChangePassword.isShown()) {
                    mLayoutChangePassword.setVisibility(View.GONE);
                } else {
                    mLayoutChangePassword.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.buttonChangePassword:
                String nickName = mInputUserName.getText().toString().trim();
                String email = mInputEmail.getText().toString().trim();

                mInfoUser.setName(nickName);
                mInfoUser.setEmail(email);
                mInfoUser.setNewAvatar(mPathAvatar);
                mInfoUser.setSex(mSex);
                if (isFromMenu) {
                    getPresenter().onUpdateInfoUser(mInfoUser);
                } else {
                    long time = System.currentTimeMillis();
                    String username = Constants.Register.ACCOUNT + time;
                    String password = Constants.Register.PASSWORD + time;
                    getPresenter().autoRegister(username, password, mInfoUser);
                }
                break;
            case R.id.buttonSkip:
                long time = System.currentTimeMillis();
                String username = Constants.Register.ACCOUNT + time;
                String password = Constants.Register.PASSWORD + time;
                mInfoUser.setEmail(Constants.Register.EMAIL);
                mInfoUser.setName(Constants.Register.NAME);
                getPresenter().register(username, password, mInfoUser);
                break;
            case R.id.buttonLogin:
                getActivityCallback().displayScreen(IMainView.FRAGMENT_LOGIN, true, true);
                break;
            case R.id.ivAvatar:
                getPresenter().onImageViewClicked(REQUEST_CODE_PICKER_PHOTO_1, REQUEST_CODE_CAMERA_PHOTO_1);
                break;
        }
    }

    @Override
    public void onRegisterSuccess() {
        getActivityCallback().clearBackStack();
        getActivityCallback().displayScreen(IMainView.FRAGMENT_GROUP_CHAT, false, false);
    }

    @Override
    public void onOnUpdateInfoUserFailure(String message) {
        showToast(message);
    }

    @Override
    public void showDialogChoose(final int requestCodePicker, final int requestCodeCamera) {
        mDialogChoose.show(new DialogChoose.IListenerChooseCallback() {

            @Override
            public void onTakePhoto() {
                getPresenter().onPickPhotoFromCameraButtonClicked(requestCodeCamera);
            }

            @Override
            public void onChooseGallery() {
                getPresenter().onPickPhotoFromGalleryButtonClicked(requestCodePicker);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Logger.d(TAG, "#onRequestPermissionsResult");
        boolean isRequestPicker = requestCode == REQUEST_CODE_PICKER_PHOTO_1;
        boolean isRequestCamera = requestCode == REQUEST_CODE_CAMERA_PHOTO_1;
        if (isRequestPicker && Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[0]) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImageFromPicker(requestCode);
        } else if (isRequestCamera && permissions.length > 0) {
            boolean isGranted = true;
            for (int permissionGrantResult : grantResults) {
                isGranted = permissionGrantResult == PackageManager.PERMISSION_GRANTED;
                if (!isGranted) {
                    break;
                }
            }
            if (isGranted) {
                pickImageFromCamera(requestCode);
            } else {
                Toast.makeText(getActivityContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivityContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean isRequestPicker = requestCode == REQUEST_CODE_PICKER_PHOTO_1;
        boolean isRequestCamera = requestCode == REQUEST_CODE_CAMERA_PHOTO_1;
        if ((isRequestPicker || isRequestCamera) && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri imageUri = UCrop.getOutput(data);
                if (imageUri == null) {
                    if (isRequestPicker) {
                        handleImagePickerResult(data, requestCode);
                    } else {
                        handleImageCameraResult(mCameraModule.getImages(), requestCode);
                    }
                } else {
                    handleCroppedImage(imageUri, requestCode);
                }
            } else {
                handleImageCameraResult(mCameraModule.getImages(), requestCode);
            }
        }
    }

    private void handleCroppedImage(Uri imageUri, int requestCode) {
        mPathAvatar = imageUri.getPath();
        if (!TextUtils.isEmpty(mPathAvatar)) {
            fillImage(mPathAvatar);
        }
    }

    private void handleImagePickerResult(Intent data, int requestCode) {
        List<Image> images = ImagePicker.getImages(data);
        if (images != null && !images.isEmpty()) {
            Image image = images.get(0);
            if (image != null) {
                startCropActivity(image, requestCode);
            }
        }
    }

    private void handleImageCameraResult(List<Image> images, int requestCode) {
        if (images != null && !images.isEmpty()) {
            Image image = images.get(0);
            if (image != null) {
                startCropActivity(image, requestCode);
            }
        }
    }

    private void startCropActivity(Image image, int requestCode) {
        UCrop uCrop = UCrop.of(Uri.fromFile(new File(image.getPath())), Utils.createImageFile(getActivityContext(), String.valueOf(System.currentTimeMillis())));
        uCrop = uCrop.useSourceImageAspectRatio();
        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(false);
        options.setHideBottomControls(true);
        options.withAspectRatio(getResources().getDimensionPixelSize(R.dimen.aspect_image_crop),
                getResources().getDimensionPixelSize(R.dimen.aspect_image_crop));
        uCrop = uCrop.withOptions(options);
        uCrop.start(getActivityContext(), this, requestCode);
    }

    @Override
    public void openCamera(int requestCode) {
        pickImageFromCamera(requestCode);
    }

    @Override
    public void requestCameraAndWriteStoragePermission(int requestCode) {
        requestPermissions(new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, requestCode);
    }

    @Override
    public void openChooseImageScreen(int requestCode) {
        pickImageFromPicker(requestCode);
    }

    @Override
    public void requestStoragePermission(int requestCode) {
        requestPermissions(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, requestCode);
    }

    private void pickImageFromCamera(int requestCode) {
        if (mCameraModule == null) {
            mCameraModule = new ImmediateResultCameraModule();
        }
        startActivityForResult(mCameraModule.getCameraIntent(getActivityContext()), requestCode);
    }

    void pickImageFromPicker(int requestCode) {
        ImagePicker.create(this)
                .single()
                .returnAfterFirst(false)
                .folderMode(true)
                .showCamera(false)
                .start(requestCode);
    }

    private void fillImage(String imgPath) {
        Glide.with(getActivityContext().getApplicationContext())
                .load(imgPath)
                .fitCenter()
                .placeholder(R.drawable.ic_add_image)
                .error(R.drawable.ic_add_image)
                .into(new SimpleTarget<GlideDrawable>() {

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        Logger.d(TAG, "onLoadStarted");
                        mImageAvatar.setEnabled(false);
                        mImageAvatar.setImageDrawable(placeholder);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        Logger.d(TAG, "onLoadFailed");
                        mImageAvatar.setEnabled(true);
                        mImageAvatar.setImageDrawable(errorDrawable);
                        mImageAvatar.setTag(R.id.shared_drawable, null);
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        Logger.d(TAG, "onResourceReady");
                        mImageAvatar.setEnabled(true);
                        mImageAvatar.setImageDrawable(resource);
                        mImageAvatar.setTag(R.id.shared_drawable, resource);
                    }
                });
    }
}
