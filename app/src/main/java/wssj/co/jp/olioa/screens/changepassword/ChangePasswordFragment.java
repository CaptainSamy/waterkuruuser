package wssj.co.jp.olioa.screens.changepassword;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.util.Arrays;
import java.util.List;

import wssj.co.jp.olioa.R;
import wssj.co.jp.olioa.screens.dialogchoosen.DialogChoose;
import wssj.co.jp.olioa.screens.dialogphoto.PhotoDialog;
import wssj.co.jp.olioa.model.auth.InfoUserResponse;
import wssj.co.jp.olioa.screens.IMainView;
import wssj.co.jp.olioa.screens.base.BaseFragment;
import wssj.co.jp.olioa.utils.Constants;
import wssj.co.jp.olioa.utils.ImmediateResultCameraModule;
import wssj.co.jp.olioa.utils.Logger;
import wssj.co.jp.olioa.utils.Utils;

/**
 * Created by Nguyen Huu Ta on 5/7/2017.
 */

public class ChangePasswordFragment extends BaseFragment<IChangePasswordView, ChangePasswordPresenter>
        implements AdapterView.OnItemSelectedListener, IChangePasswordView, View.OnClickListener, PhotoDialog.IOnDeleteButtonClickListener {

    private static final String TAG = "ChangePasswordFragment";

    private static final int REQUEST_CODE_PICKER_PHOTO_1 = 100;

    private static final int REQUEST_CODE_CAMERA_PHOTO_1 = 200;

    private String[] mArrayAge = Constants.mArrayAge;

    private EditText mInputUserName, mInputEmail;

    private EditText mInputCurrentPassword, mInputNewPassword, mInputConfirmPassword;

    private TextView mButtonChangePassword;

    private TextView mTextChangePassword;

    private Spinner mSpinnerAge;

    private RadioGroup mRadioGroupSex;

    private RadioButton mRadioMale, mRadioFemale;

    private LinearLayout mLayoutChangePassword;

    private ImageView mImageAvatar;

    private int mAgeChoose, mSex;

    private DialogChoose mDialogChoose;

    private PhotoDialog mPhotoDialog;

    private ImmediateResultCameraModule mCameraModule;

    private InfoUserResponse.InfoUser mInfoUser;

    private String mPathAvatar;

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_CHANGE_PASSWORD;
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.fragment_change_password;
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
    protected ChangePasswordPresenter onCreatePresenter(IChangePasswordView view) {
        return new ChangePasswordPresenter(view);
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
    protected IChangePasswordView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        super.initViews(rootView);
        mInputCurrentPassword = (EditText) rootView.findViewById(R.id.inputCurrentPassword);
        mInputNewPassword = (EditText) rootView.findViewById(R.id.inputNewPassword);
        mInputConfirmPassword = (EditText) rootView.findViewById(R.id.inputConfirmPassword);
        mButtonChangePassword = (TextView) rootView.findViewById(R.id.buttonChangePassword);
        mInputUserName = (EditText) rootView.findViewById(R.id.etUserName);
        mInputEmail = (EditText) rootView.findViewById(R.id.etEmail);
        mSpinnerAge = (Spinner) rootView.findViewById(R.id.spAge);
        mRadioGroupSex = (RadioGroup) rootView.findViewById(R.id.radioGroupSex);
        mRadioMale = (RadioButton) rootView.findViewById(R.id.radioMale);
        mRadioFemale = (RadioButton) rootView.findViewById(R.id.radioFeMale);
        mTextChangePassword = (TextView) rootView.findViewById(R.id.tvChangePassword);
        mLayoutChangePassword = (LinearLayout) rootView.findViewById(R.id.layoutChangePassword);
        mImageAvatar = (ImageView) rootView.findViewById(R.id.ivAvatar);
        mDialogChoose = new DialogChoose(getActivityContext());

    }

    @Override
    protected void initAction() {
        super.initAction();
        mPhotoDialog = new PhotoDialog(getActivityContext(), this);
        mInfoUser = new InfoUserResponse.InfoUser();
        mTextChangePassword.setOnClickListener(this);
        mButtonChangePassword.setOnClickListener(this);
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
        mSpinnerAge.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mAgeChoose = position + 1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void initData() {
        super.initData();
        getPresenter().onGetInfoUser();

        List<String> list = Arrays.asList(mArrayAge);
        ArrayAdapter<String> mAdapterSpNumWord = new ArrayAdapter<String>(getActivityContext(), android.R.layout.simple_list_item_1, list) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (v instanceof TextView) {
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                }
                return v;
            }
        };
        mSpinnerAge.setAdapter(mAdapterSpNumWord);
    }

    @Override
    public void onGetInfoUserSuccess(InfoUserResponse.InfoUser infoUser) {
        if (infoUser != null) {
            mInfoUser = infoUser;
            mInputUserName.setText(infoUser.getName());
            mInputEmail.setText(infoUser.getEmail());
            if (infoUser.getSex() == Constants.Register.MALE) {
                mRadioMale.setChecked(true);
            } else {
                mRadioFemale.setChecked(true);
            }
            mAgeChoose = infoUser.getAvg() / Constants.Register.MIN_AGE - 1;
            mPathAvatar = infoUser.getAvatar();
            mSpinnerAge.setSelection(mAgeChoose);
            fillImage(mPathAvatar);

        }
    }

    @Override
    public void onGetInfoUserFailure(String message) {

    }

    @Override
    public void onValidateFailure(String message) {
        showToast(message);
    }

    @Override
    public void onChangePasswordSuccess(String message) {
        showToast(message);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                getActivityCallback().displayScreen(IMainView.FRAGMENT_TIMELINE, true, false);
            }
        }, 1000);
    }

    @Override
    public void onChangePasswordFailure(String message) {
        showToast(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvChangePassword:
                if (mLayoutChangePassword.isShown()) {
                    mLayoutChangePassword.setVisibility(View.GONE);
                } else {
                    mLayoutChangePassword.setVisibility(View.VISIBLE);
                }
                mInfoUser.setChangePassword(mLayoutChangePassword.isShown());
                break;
            case R.id.buttonChangePassword:
                String nickName = mInputUserName.getText().toString().trim();
                String email = mInputEmail.getText().toString().trim();
                String currentPassword = mInputCurrentPassword.getText().toString().trim();
                String newPassword = mInputNewPassword.getText().toString().trim();
                String confirmPassword = mInputConfirmPassword.getText().toString().trim();

                InfoUserResponse.InfoUser newInfoUser = new InfoUserResponse.InfoUser();
                newInfoUser.setName(nickName);
                newInfoUser.setEmail(email);
                newInfoUser.setAvatar(mPathAvatar);
                newInfoUser.setAvg(mAgeChoose * Constants.Register.MIN_AGE);
                newInfoUser.setSex(mSex);
                if (mInfoUser.isChangePassword()) {
                    newInfoUser.setChangePassword(mInfoUser.isChangePassword());
                    newInfoUser.setCurrentPassword(currentPassword);
                    newInfoUser.setNewPassword(newPassword);
                    newInfoUser.setConfirmPassword(confirmPassword);
                }
                if (isChangeData(newInfoUser) || newInfoUser.isChangePassword()) {
                    getPresenter().validateInfoUser(newInfoUser);
                }
                break;
            case R.id.ivAvatar:
                getPresenter().onImageViewClicked((Drawable) v.getTag(R.id.shared_drawable), REQUEST_CODE_PICKER_PHOTO_1, REQUEST_CODE_CAMERA_PHOTO_1);
                break;
        }
    }

    private boolean isChangeData(InfoUserResponse.InfoUser newInfoUser) {
        boolean isChange = false;
        if (!TextUtils.equals(newInfoUser.getName(), mInfoUser.getName())) {
            isChange = true;
        }
        if (!TextUtils.equals(newInfoUser.getEmail(), mInfoUser.getEmail())) {
            isChange = true;
        }
        if (!TextUtils.equals(newInfoUser.getAvatar(), mInfoUser.getAvatar())) {
            isChange = true;
        }
        if (newInfoUser.getAvg() != mInfoUser.getAvg()) {
            isChange = true;
        }
        if (newInfoUser.getSex() != mInfoUser.getSex()) {
            isChange = true;
        }
        return isChange;
    }

    @Override
    public void onOnUpdateInfoUserSuccess() {
        showToast(getString(R.string.success));
    }

    @Override
    public void onOnUpdateInfoUserFailure(String message) {
        showToast(message);
    }

    @Override
    public void showPhotoDialog(Drawable drawable, int requestCode) {
        mPhotoDialog.showImage(drawable, requestCode);
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

    @Override
    public void onDeleteClicked(int imageCode) {
        mPathAvatar = Constants.EMPTY_STRING;
        mImageAvatar.setImageDrawable(ContextCompat.getDrawable(getActivityContext(), R.drawable.ic_add_image));
        mImageAvatar.setTag(R.id.shared_drawable, null);

    }
}
