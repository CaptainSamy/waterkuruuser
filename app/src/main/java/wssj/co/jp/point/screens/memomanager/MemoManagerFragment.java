package wssj.co.jp.point.screens.memomanager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import wssj.co.jp.point.R;
import wssj.co.jp.point.model.entities.UpdateMemoPhotoData;
import wssj.co.jp.point.model.memo.ListServiceResponse;
import wssj.co.jp.point.model.memo.UserMemoResponse;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;
import wssj.co.jp.point.screens.dialogchoosen.DialogChoose;
import wssj.co.jp.point.screens.dialogphoto.PhotoDialog;
import wssj.co.jp.point.utils.Constants;
import wssj.co.jp.point.utils.ImmediateResultCameraModule;
import wssj.co.jp.point.utils.Logger;
import wssj.co.jp.point.utils.Utils;

/**
 * Created by HieuPT on 17/5/2017.
 */

public class MemoManagerFragment extends BaseFragment<IMemoManagerView, MemoManagerPresenter>
        implements IMemoManagerView, View.OnClickListener, AdapterView.OnItemSelectedListener, PhotoDialog.IOnDeleteButtonClickListener {

    private static final String TAG = "MemoManagerFragment";

    private static final int REQUEST_CODE_PICKER_PHOTO_1 = 100;

    private static final int REQUEST_CODE_PICKER_PHOTO_2 = 101;

    private static final int REQUEST_CODE_PICKER_PHOTO_3 = 102;

    private static final int REQUEST_CODE_PICKER_PHOTO_4 = 103;

    private static final int REQUEST_CODE_CAMERA_PHOTO_1 = 200;

    private static final int REQUEST_CODE_CAMERA_PHOTO_2 = 201;

    private static final int REQUEST_CODE_CAMERA_PHOTO_3 = 203;

    private static final int REQUEST_CODE_CAMERA_PHOTO_4 = 204;

    private Spinner mSpinnerServices;

    private EditText mInputNote;

    private ImageView mPhoto1, mPhoto2, mPhoto3, mPhoto4;

    private TextView mButtonSave;

    private PhotoDialog mPhotoDialog;

    private DialogChoose mDialogChoose;

    private final UpdateMemoPhotoData[] mListPhoto = new UpdateMemoPhotoData[4];

    private final String[] mListPathImage = new String[4];

    private List<ListServiceResponse.Service> mListService;

    private int mCurrentServiceId;

    private String mNoteOld = Constants.EMPTY_STRING;

    private ImmediateResultCameraModule mCameraModule;

    @Override
    public int getFragmentId() {
        return IMainView.FRAGMENT_MEMO_MANAGER;
    }

    @Override
    public boolean isDisplayBottomNavigationMenu() {
        return true;
    }

    @Override
    public int getNavigationBottomId() {
        return R.id.navigation_another;
    }

    @Override
    public String getAppBarTitle() {
        return getString(R.string.title_screen_another);
    }

    @Override
    protected int getResourceLayout() {
        return R.layout.memo_manager_fragment;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected MemoManagerPresenter onCreatePresenter(IMemoManagerView view) {
        return new MemoManagerPresenter(view);
    }

    @Override
    protected IMemoManagerView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mSpinnerServices = (Spinner) rootView.findViewById(R.id.spServices);
        mInputNote = (EditText) rootView.findViewById(R.id.etNote);
        mPhoto1 = (ImageView) rootView.findViewById(R.id.ivPhoto1);
        mPhoto2 = (ImageView) rootView.findViewById(R.id.ivPhoto2);
        mPhoto3 = (ImageView) rootView.findViewById(R.id.ivPhoto3);
        mPhoto4 = (ImageView) rootView.findViewById(R.id.ivPhoto4);
        mPhotoDialog = new PhotoDialog(getContext(), this);
        mDialogChoose = new DialogChoose(getContext());
        mButtonSave = (TextView) rootView.findViewById(R.id.tvSave);
    }

    @Override
    protected void initAction() {
        mPhoto1.setOnClickListener(this);
        mPhoto2.setOnClickListener(this);
        mPhoto3.setOnClickListener(this);
        mPhoto4.setOnClickListener(this);
        mSpinnerServices.setOnItemSelectedListener(this);
        mButtonSave.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mListService = new ArrayList<>();
        getPresenter().getListService();
    }

    @Override
    public void onGetListServicesSuccess(ListServiceResponse.ServiceData data) {
        List<String> list = new ArrayList<>();
        if (data != null && data.getListService() != null && data.getListService().size() > 0) {
            mListService = data.getListService();
            for (ListServiceResponse.Service services : mListService) {
                list.add(services.getServiceName());
            }
        }
        ArrayAdapter<String> mAdapterSpNumWord = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list) {

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
        mSpinnerServices.setAdapter(mAdapterSpNumWord);
    }

    @Override
    public void onGetListServicesFailure(String message) {
        showToast(message);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        fillImage(Constants.EMPTY_STRING, mPhoto1, false);
        fillImage(Constants.EMPTY_STRING, mPhoto2, false);
        fillImage(Constants.EMPTY_STRING, mPhoto3, false);
        fillImage(Constants.EMPTY_STRING, mPhoto4, false);
        mInputNote.setText(Constants.EMPTY_STRING);
        if (mListService != null && mListService.size() > 0) {
            mCurrentServiceId = mListService.get(position).getId();
            getPresenter().getUserMemoByServiceId(mCurrentServiceId);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void openChooseImageScreen(int requestCode) {
        pickImageFromPicker(requestCode);
    }

    void pickImageFromPicker(int requestCode) {
        ImagePicker.create(this)
                .single()
                .returnAfterFirst(false)
                .folderMode(true)
                .showCamera(false)
                .start(requestCode);
    }

    @Override
    public void requestStoragePermission(int requestCode) {
        requestPermissions(new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, requestCode);
    }

    @Override
    public void requestCameraAndWriteStoragePermission(int requestCode) {
        requestPermissions(new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Logger.d(TAG, "#onRequestPermissionsResult");
        boolean isRequestPicker = requestCode == REQUEST_CODE_PICKER_PHOTO_1
                || requestCode == REQUEST_CODE_PICKER_PHOTO_2
                || requestCode == REQUEST_CODE_PICKER_PHOTO_3
                || requestCode == REQUEST_CODE_PICKER_PHOTO_4;
        boolean isRequestCamera = requestCode == REQUEST_CODE_CAMERA_PHOTO_1
                || requestCode == REQUEST_CODE_CAMERA_PHOTO_2
                || requestCode == REQUEST_CODE_CAMERA_PHOTO_3
                || requestCode == REQUEST_CODE_CAMERA_PHOTO_4;
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
                Toast.makeText(getContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPhoto1:
                getPresenter().onImageViewClicked((Drawable) v.getTag(R.id.shared_drawable), REQUEST_CODE_PICKER_PHOTO_1, REQUEST_CODE_CAMERA_PHOTO_1);
                break;
            case R.id.ivPhoto2:
                getPresenter().onImageViewClicked((Drawable) v.getTag(R.id.shared_drawable), REQUEST_CODE_PICKER_PHOTO_2, REQUEST_CODE_CAMERA_PHOTO_2);
                break;
            case R.id.ivPhoto3:
                getPresenter().onImageViewClicked((Drawable) v.getTag(R.id.shared_drawable), REQUEST_CODE_PICKER_PHOTO_3, REQUEST_CODE_CAMERA_PHOTO_3);
                break;
            case R.id.ivPhoto4:
                getPresenter().onImageViewClicked((Drawable) v.getTag(R.id.shared_drawable), REQUEST_CODE_PICKER_PHOTO_4, REQUEST_CODE_CAMERA_PHOTO_4);
                break;
            case R.id.tvSave:
                boolean isChangeNote = !mNoteOld.equals(mInputNote.getText().toString().trim());
                boolean isChangeImage = false;
                for (UpdateMemoPhotoData data : mListPhoto) {
                    if (data != null && data.getUpdateFlag() != 1) {
                        isChangeImage = true;
                        break;
                    }
                }
                if (mCurrentServiceId != 0 && (isChangeNote || isChangeImage)) {
                    getPresenter().updateUserMemo(mCurrentServiceId, mInputNote.getText().toString().trim(), mListPathImage);
                }
                break;
        }

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
    public void onUpdateUserMemoSuccess(String message) {
        mNoteOld = mInputNote.getText().toString().trim();
        for (UpdateMemoPhotoData data : mListPhoto) {
            if (data != null && data.getUpdateFlag() != 1) {
                data.setUpdateFlag(UpdateMemoPhotoData.FLAG_NOT_MODIFY);
            }
        }
        showToast(message);
    }

    @Override
    public void onUpdateUserMemoFailure(String message) {
        showToast(message);
    }

    @Override
    public void showPhotoDialog(Drawable drawable, int requestCode) {
        mPhotoDialog.showImage(drawable, requestCode);
    }

    @Override
    public void openCamera(int requestCode) {
        pickImageFromCamera(requestCode);
    }

    private void pickImageFromCamera(int requestCode) {
        if (mCameraModule == null) {
            mCameraModule = new ImmediateResultCameraModule();
        }
        startActivityForResult(mCameraModule.getCameraIntent(getActivityContext()), requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean isRequestPicker = requestCode == REQUEST_CODE_PICKER_PHOTO_1
                || requestCode == REQUEST_CODE_PICKER_PHOTO_2
                || requestCode == REQUEST_CODE_PICKER_PHOTO_3
                || requestCode == REQUEST_CODE_PICKER_PHOTO_4;
        boolean isRequestCamera = requestCode == REQUEST_CODE_CAMERA_PHOTO_1
                || requestCode == REQUEST_CODE_CAMERA_PHOTO_2
                || requestCode == REQUEST_CODE_CAMERA_PHOTO_3
                || requestCode == REQUEST_CODE_CAMERA_PHOTO_4;
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
        String imagePath = imageUri.getPath();
        UpdateMemoPhotoData photoData = null;
        ImageView imageView = null;
        switch (requestCode) {
            case REQUEST_CODE_PICKER_PHOTO_1:
            case REQUEST_CODE_CAMERA_PHOTO_1:
                photoData = mListPhoto[0];
                imageView = mPhoto1;
                mListPathImage[0] = imagePath;
                break;
            case REQUEST_CODE_PICKER_PHOTO_2:
            case REQUEST_CODE_CAMERA_PHOTO_2:
                photoData = mListPhoto[1];
                imageView = mPhoto2;
                mListPathImage[1] = imagePath;
                break;
            case REQUEST_CODE_PICKER_PHOTO_3:
            case REQUEST_CODE_CAMERA_PHOTO_3:
                photoData = mListPhoto[2];
                imageView = mPhoto3;
                mListPathImage[2] = imagePath;
                break;
            case REQUEST_CODE_PICKER_PHOTO_4:
            case REQUEST_CODE_CAMERA_PHOTO_4:
                photoData = mListPhoto[3];
                imageView = mPhoto4;
                mListPathImage[3] = imagePath;
                break;
        }
        if (!TextUtils.isEmpty(imagePath) && imageView != null) {
            fillImage(imagePath, imageView, true);
            if (photoData != null) {
                photoData.setImagePath(imagePath);
                photoData.setUpdateFlag(UpdateMemoPhotoData.FLAG_MODIFIED);
            }
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
        UCrop uCrop = UCrop.of(Uri.fromFile(new File(image.getPath())), Utils.createImageFile(getActivityContext(), String.valueOf(requestCode)));
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
    public void onGetUserMemoSuccess(UserMemoResponse.UserMemo response) {
        if (response != null) {
            if (response.getNote() != null) {
                mNoteOld = response.getNote();
                mInputNote.setText(mNoteOld);
            }
            UserMemoResponse.Photo photo = response.getPhoto();
            if (photo != null) {
                fillImage(photo.getPhoto1(), mPhoto1, false);
                if (!TextUtils.isEmpty(photo.getPhoto1())) {
                    mListPhoto[0] = new UpdateMemoPhotoData(UpdateMemoPhotoData.FLAG_DELETE);
                } else {
                    mListPhoto[0] = new UpdateMemoPhotoData(UpdateMemoPhotoData.FLAG_NOT_MODIFY);
                }
                fillImage(photo.getPhoto2(), mPhoto2, false);
                if (!TextUtils.isEmpty(photo.getPhoto2())) {
                    mListPhoto[1] = new UpdateMemoPhotoData(UpdateMemoPhotoData.FLAG_DELETE);
                } else {
                    mListPhoto[1] = new UpdateMemoPhotoData(UpdateMemoPhotoData.FLAG_NOT_MODIFY);
                }
                fillImage(photo.getPhoto3(), mPhoto3, false);
                if (!TextUtils.isEmpty(photo.getPhoto3())) {
                    mListPhoto[2] = new UpdateMemoPhotoData(UpdateMemoPhotoData.FLAG_DELETE);
                } else {
                    mListPhoto[2] = new UpdateMemoPhotoData(UpdateMemoPhotoData.FLAG_NOT_MODIFY);
                }
                fillImage(photo.getPhoto4(), mPhoto4, false);
                if (!TextUtils.isEmpty(photo.getPhoto4())) {
                    mListPhoto[3] = new UpdateMemoPhotoData(UpdateMemoPhotoData.FLAG_DELETE);
                } else {
                    mListPhoto[3] = new UpdateMemoPhotoData(UpdateMemoPhotoData.FLAG_NOT_MODIFY);
                }
            } else {
                mListPhoto[0] = new UpdateMemoPhotoData(UpdateMemoPhotoData.FLAG_NOT_MODIFY);
                mListPhoto[1] = new UpdateMemoPhotoData(UpdateMemoPhotoData.FLAG_NOT_MODIFY);
                mListPhoto[2] = new UpdateMemoPhotoData(UpdateMemoPhotoData.FLAG_NOT_MODIFY);
                mListPhoto[3] = new UpdateMemoPhotoData(UpdateMemoPhotoData.FLAG_NOT_MODIFY);
            }
        }
    }

    private void fillImage(String photo, final ImageView imageView, boolean fromLocal) {
        String imgPath = fromLocal ? photo : photo;
        Glide.with(getActivityContext().getApplicationContext())
                .load(imgPath)
                .placeholder(R.drawable.ic_add_image)
                .skipMemoryCache(fromLocal)
                .diskCacheStrategy(fromLocal ? DiskCacheStrategy.NONE : DiskCacheStrategy.ALL)
                .error(R.drawable.ic_add_image)
                .into(new SimpleTarget<GlideDrawable>() {

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        Logger.d(TAG, "onLoadStarted");
                        imageView.setEnabled(false);
                        imageView.setImageDrawable(placeholder);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        Logger.d(TAG, "onLoadFailed");
                        imageView.setEnabled(true);
                        imageView.setImageDrawable(errorDrawable);
                        imageView.setTag(R.id.shared_drawable, null);
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        Logger.d(TAG, "onResourceReady");
                        imageView.setEnabled(true);
                        imageView.setImageDrawable(resource);
                        imageView.setTag(R.id.shared_drawable, resource);
                    }
                });
    }

    @Override
    public void onGetUserMemoFailure(String message) {
        showToast(message);
    }

    @Override
    public void onDeleteClicked(int imageCode) {
        ImageView imageView = null;
        UpdateMemoPhotoData photoData = null;
        switch (imageCode) {
            case REQUEST_CODE_PICKER_PHOTO_1:
                imageView = mPhoto1;
                photoData = mListPhoto[0];
                mListPathImage[0] = Constants.EMPTY_STRING;
                break;
            case REQUEST_CODE_PICKER_PHOTO_2:
                imageView = mPhoto2;
                photoData = mListPhoto[1];
                mListPathImage[1] = Constants.EMPTY_STRING;
                break;
            case REQUEST_CODE_PICKER_PHOTO_3:
                imageView = mPhoto3;
                photoData = mListPhoto[2];
                break;
            case REQUEST_CODE_PICKER_PHOTO_4:
                imageView = mPhoto4;
                photoData = mListPhoto[3];
                mListPathImage[3] = Constants.EMPTY_STRING;
                break;
        }
        if (photoData != null) {
            photoData.setToFlagOnDelete();
        }
        if (imageView != null) {
            imageView.setImageDrawable(ContextCompat.getDrawable(getActivityContext(), R.drawable.ic_add_image));
            imageView.setTag(R.id.shared_drawable, null);
        }
    }

    @Override
    public int getMenuBottomID() {
        return MENU_MY_REQUEST;
    }
}
