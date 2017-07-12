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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
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
import wssj.co.jp.point.model.entities.StatusMemoData;
import wssj.co.jp.point.model.entities.UpdateMemoPhotoData;
import wssj.co.jp.point.model.memo.ListServiceResponse;
import wssj.co.jp.point.model.memo.MemoDynamicResponse;
import wssj.co.jp.point.model.memo.UserMemoResponse;
import wssj.co.jp.point.screens.IMainView;
import wssj.co.jp.point.screens.base.BaseFragment;
import wssj.co.jp.point.screens.dialogchoosen.DialogChoose;
import wssj.co.jp.point.screens.dialogphoto.PhotoDialog;
import wssj.co.jp.point.screens.memomanager.adapter.ImageAdapter;
import wssj.co.jp.point.utils.Constants;
import wssj.co.jp.point.utils.ImmediateResultCameraModule;
import wssj.co.jp.point.utils.Logger;
import wssj.co.jp.point.utils.Utils;
import wssj.co.jp.point.widget.ExpandableHeightGridView;

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

    private ViewGroup mParentViewMemoConfig;

    private Button mButtonShow;

    private Spinner mSpinnerServices;

    private EditText mInputNote;

    private ImageView mPhoto1, mPhoto2, mPhoto3, mPhoto4;

    private TextView mButtonSave;

    private PhotoDialog mPhotoDialog;

    private DialogChoose mDialogChoose;

    private final StatusMemoData[] mListPhoto = new StatusMemoData[4];

    private List<ListServiceResponse.Service> mListService;

    private int mCurrentServiceId;

    private String mOriginNote = Constants.EMPTY_STRING;

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
    public int getMenuBottomID() {
        return MENU_MY_REQUEST;
    }

    @Override
    protected IMemoManagerView onCreateView() {
        return this;
    }

    @Override
    protected void initViews(View rootView) {
        mParentViewMemoConfig = (ViewGroup) rootView.findViewById(R.id.parentViewMemoConfig);
        mButtonShow = (Button) rootView.findViewById(R.id.showContent);
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
    public void onGetMemoConfigSuccess(MemoDynamicResponse.UserMemoData userConfig) {
        if (userConfig == null) return;
        List<MemoDynamicResponse.UserMemoData.UserMemoConfig> memoConfig = userConfig.getListMemoConfig();

        if (memoConfig != null && memoConfig.size() > 0) {
            onCreateMemoDynamic(memoConfig);
        }
    }

    @Override
    public void onGetMemoConfigFailure(String message) {

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
//            getPresenter().getUserMemoByServiceId(mCurrentServiceId);

            getPresenter().getMemoConfigByServiceId(mCurrentServiceId);
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
                String newNote = mInputNote.getText().toString().trim();
                boolean isChangeNote = !TextUtils.equals(mOriginNote, newNote);
                boolean isChangeImage = false;
                for (StatusMemoData data : mListPhoto) {
                    if (data != null && data.getStatus() != 1) {
                        isChangeImage = true;
                        break;
                    }
                }
                if (mCurrentServiceId != 0 && (isChangeNote || isChangeImage)) {
                    getPresenter().updateUserMemo(mCurrentServiceId, mInputNote.getText().toString().trim(), mListPhoto);
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
        mOriginNote = mInputNote.getText().toString().trim();
        for (StatusMemoData data : mListPhoto) {
            if (data != null && data.getStatus() != 1) {
                data.setStatus(UpdateMemoPhotoData.FLAG_NOT_MODIFY);
                data.setUrlOriginImage(data.getPathNewImage());
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
        ImageView imageView = null;
        switch (requestCode) {
            case REQUEST_CODE_PICKER_PHOTO_1:
            case REQUEST_CODE_CAMERA_PHOTO_1:
                imageView = mPhoto1;
                mListPhoto[0].setPathNewImage(imagePath);
                mListPhoto[0].setStatus(StatusMemoData.FLAG_MODIFIED);
                break;
            case REQUEST_CODE_PICKER_PHOTO_2:
            case REQUEST_CODE_CAMERA_PHOTO_2:
                imageView = mPhoto2;
                mListPhoto[1].setPathNewImage(imagePath);
                mListPhoto[1].setStatus(StatusMemoData.FLAG_MODIFIED);
                break;
            case REQUEST_CODE_PICKER_PHOTO_3:
            case REQUEST_CODE_CAMERA_PHOTO_3:
                imageView = mPhoto3;
                mListPhoto[2].setPathNewImage(imagePath);
                mListPhoto[2].setStatus(StatusMemoData.FLAG_MODIFIED);
                break;
            case REQUEST_CODE_PICKER_PHOTO_4:
            case REQUEST_CODE_CAMERA_PHOTO_4:
                imageView = mPhoto4;
                mListPhoto[3].setPathNewImage(imagePath);
                mListPhoto[3].setStatus(StatusMemoData.FLAG_MODIFIED);
                break;
        }
        if (!TextUtils.isEmpty(imagePath) && imageView != null) {
            fillImage(imagePath, imageView, true);
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

    private void fillImage(String imgPath, final ImageView imageView, boolean fromLocal) {
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
    public void onGetUserMemoSuccess(UserMemoResponse.UserMemo response) {
        if (response != null) {
            if (response.getNote() != null) {
                mOriginNote = response.getNote();
                mInputNote.setText(mOriginNote);
            }
            UserMemoResponse.Photo photo = response.getPhoto();
            if (photo != null) {
                fillImage(photo.getPhoto1(), mPhoto1, false);
                if (!TextUtils.isEmpty(photo.getPhoto1())) {
                    mListPhoto[0] = new StatusMemoData(photo.getPhoto1());
                } else {
                    mListPhoto[0] = new StatusMemoData(Constants.EMPTY_STRING);
                }
                fillImage(photo.getPhoto2(), mPhoto2, false);
                if (!TextUtils.isEmpty(photo.getPhoto2())) {
                    mListPhoto[1] = new StatusMemoData(photo.getPhoto2());
                } else {
                    mListPhoto[1] = new StatusMemoData(Constants.EMPTY_STRING);
                }
                fillImage(photo.getPhoto3(), mPhoto3, false);
                if (!TextUtils.isEmpty(photo.getPhoto3())) {
                    mListPhoto[2] = new StatusMemoData(photo.getPhoto3());
                } else {
                    mListPhoto[2] = new StatusMemoData(Constants.EMPTY_STRING);
                }
                fillImage(photo.getPhoto4(), mPhoto4, false);
                if (!TextUtils.isEmpty(photo.getPhoto4())) {
                    mListPhoto[3] = new StatusMemoData(photo.getPhoto4());
                } else {
                    mListPhoto[3] = new StatusMemoData(Constants.EMPTY_STRING);
                }
            }
        } else {
            mListPhoto[0] = new StatusMemoData(Constants.EMPTY_STRING);
            mListPhoto[1] = new StatusMemoData(Constants.EMPTY_STRING);
            mListPhoto[2] = new StatusMemoData(Constants.EMPTY_STRING);
            mListPhoto[3] = new StatusMemoData(Constants.EMPTY_STRING);
        }
    }

    @Override
    public void onGetUserMemoFailure(String message) {
        showToast(message);
    }

    @Override
    public void onDeleteClicked(int imageCode) {
        ImageView imageView = null;
        switch (imageCode) {
            case REQUEST_CODE_PICKER_PHOTO_1:
                imageView = mPhoto1;
                mListPhoto[0].setPathNewImage(Constants.EMPTY_STRING);
                mListPhoto[0].setStatus(StatusMemoData.FLAG_MODIFIED);
                break;
            case REQUEST_CODE_PICKER_PHOTO_2:
                imageView = mPhoto2;
                mListPhoto[1].setPathNewImage(Constants.EMPTY_STRING);
                mListPhoto[1].setStatus(StatusMemoData.FLAG_MODIFIED);
                break;
            case REQUEST_CODE_PICKER_PHOTO_3:
                imageView = mPhoto3;
                mListPhoto[2].setPathNewImage(Constants.EMPTY_STRING);
                mListPhoto[2].setStatus(StatusMemoData.FLAG_MODIFIED);
                break;
            case REQUEST_CODE_PICKER_PHOTO_4:
                imageView = mPhoto4;
                mListPhoto[3].setPathNewImage(Constants.EMPTY_STRING);
                mListPhoto[3].setStatus(StatusMemoData.FLAG_MODIFIED);
                break;
        }
        if (imageView != null) {
            imageView.setImageDrawable(ContextCompat.getDrawable(getActivityContext(), R.drawable.ic_add_image));
            imageView.setTag(R.id.shared_drawable, null);
        }
    }


    /* Handle create memo dynamic*/

    public void onCreateMemoDynamic(List<MemoDynamicResponse.UserMemoData.UserMemoConfig> mListMemoConfig) {
        for (MemoDynamicResponse.UserMemoData.UserMemoConfig memoConfig : mListMemoConfig) {
            switch (memoConfig.getType()) {
                case Constants.MemoConfig.TYPE_SHORT_TEXT:
                    handleCreateEditText(memoConfig);
                    break;
                case Constants.MemoConfig.TYPE_LONG_TEXT:
                    handleCreateSwitch(memoConfig);
                    break;
                case Constants.MemoConfig.TYPE_IMAGE:
                    handleCreateImage(memoConfig);
                    break;
                case Constants.MemoConfig.TYPE_SPINNER:
                    handleCreateDrop(memoConfig);
                    break;
                case Constants.MemoConfig.TYPE_RADIO:
                    handleCreateDrop(memoConfig);
                    break;
                case Constants.MemoConfig.TYPE_LEVEL:
                    handleCreateDrop(memoConfig);
                    break;
            }
        }
        mButtonShow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListEditText != null && mListEditText.size() > 0) {
                    String text = "";
                    for (EditText editText : mListEditText) {
                        text = text + editText.getText().toString() + "/";
                    }
                    showToast(text);
                }


                if (mListRadioGroup != null && mListRadioGroup.size() > 0) {
                    String radio = "";
                    for (RadioGroup radioGroup : mListRadioGroup) {
                        int radioButtonID = radioGroup.getCheckedRadioButtonId();
                        RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
                        if (radioButton != null) {
                            radio = radio + radioButton.getText().toString() + ",";
                        }
                    }
                    showToast(radio);
                }


                if (mListSpinner != null && mListSpinner.size() > 0) {
                    String spinnerChoose = "";
                    for (Spinner spinner : mListSpinner) {
                        spinnerChoose = spinnerChoose + spinner.getSelectedItem().toString();
                    }
                    showToast(spinnerChoose);
                }
            }
        });
    }

    private List<EditText> mListEditText;

    private List<Switch> mListRadioGroup;

    private List<Spinner> mListSpinner;

    private List<GridView> mListGridView;

    /*Handle create edit text*/

    public void handleCreateEditText(MemoDynamicResponse.UserMemoData.UserMemoConfig memoConfig) {
        if (memoConfig == null) return;
        if (mListEditText == null) {
            mListEditText = new ArrayList<>();
        }
        TextView title;
        EditText editText;
        View viewEditText = LayoutInflater.from(getContext()).inflate(R.layout.memo_config_edittext, null);
        title = (TextView) viewEditText.findViewById(R.id.titleEditText);
        editText = (EditText) viewEditText.findViewById(R.id.content);


        title.setText(memoConfig.getTitle());
        if (memoConfig.getType() == 2) {
            editText.setLines(Constants.MemoConfig.NUMBER_LINE);
        }
        mListEditText.add(editText);
        mParentViewMemoConfig.addView(viewEditText);
    }

//    /*
//    Handle create Radio
//    */
//    public void handleCreateRadio(MemoDynamicResponse.UserMemoData.UserMemoConfig memoConfig) {
//        if (memoConfig == null) return;
//        if (mListRadioGroup == null) {
//            mListRadioGroup = new ArrayList<>();
//        }
//        TextView title;
//        RadioGroup radioGroup;
//        View viewRadio = LayoutInflater.from(getContext()).inflate(R.layout.memo_config_radio, null);
//        title = (TextView) viewRadio.findViewById(R.id.titleRadio);
//        radioGroup = (RadioGroup) viewRadio.findViewById(R.id.radioGroup);
//        MemoDynamicResponse.UserMemoData.ServiceList.ServiceMemoConfig.Config config = memoConfig.getConfig();
//        if (config != null && config.getData() != null) {
//            for (String string : config.getData()) {
//                RadioButton radioButton = new RadioButton(getContext());
//                radioButton.setText(string);
//                radioGroup.addView(radioButton);
//            }
//        }
//        title.setText(memoConfig.getName());
//        mListRadioGroup.add(radioGroup);
//        mParentViewMemoConfig.addView(viewRadio);
//    }

    /*
   Handle create Radio
   */
    public void handleCreateSwitch(MemoDynamicResponse.UserMemoData.UserMemoConfig memoConfig) {
        if (memoConfig == null) return;
        if (mListRadioGroup == null) {
            mListRadioGroup = new ArrayList<>();
        }
        TextView title;
        Switch switchView;
        View viewSwitch = LayoutInflater.from(getContext()).inflate(R.layout.memo_config_switch, null);
        title = (TextView) viewSwitch.findViewById(R.id.titleSwitch);
        switchView = (Switch) viewSwitch.findViewById(R.id.switchView);
        title.setText(memoConfig.getTitle());
        mParentViewMemoConfig.addView(viewSwitch);
    }

    /*
       Handle create Image
     */
    public void handleCreateImage(MemoDynamicResponse.UserMemoData.UserMemoConfig memoConfig) {
        if (memoConfig == null) return;

        TextView title;
        ExpandableHeightGridView gridView;
        if (mListGridView == null) {
            mListGridView = new ArrayList<>();
        }
        List<String> listImage = new ArrayList<>();
        listImage.add("1");
        listImage.add("1");
        listImage.add("1");
        listImage.add("1");
        listImage.add("1");

        View viewGrid = LayoutInflater.from(getContext()).inflate(R.layout.memo_config_images, null);
        title = (TextView) viewGrid.findViewById(R.id.titleImages);
        gridView = (ExpandableHeightGridView) viewGrid.findViewById(R.id.gridView);
        gridView.setExpanded(true);
        title.setText(memoConfig.getTitle());
        ImageAdapter imageAdapter = new ImageAdapter(getContext(), listImage);
        gridView.setAdapter(imageAdapter);
        mParentViewMemoConfig.addView(viewGrid);
    }

    /*
       Handle create Drop
     */
    public void handleCreateDrop(MemoDynamicResponse.UserMemoData.UserMemoConfig memoConfig) {
        if (memoConfig == null) return;

        TextView title;
        Spinner spinner;
        if (mListSpinner == null) {
            mListSpinner = new ArrayList<>();
        }
        View viewSpinner = LayoutInflater.from(getContext()).inflate(R.layout.menu_config_spinner, null);
        title = (TextView) viewSpinner.findViewById(R.id.titleSpinner);
        spinner = (Spinner) viewSpinner.findViewById(R.id.spServices);

        List<String> list = new ArrayList<>();
//        if (list != null && list.size() > 0) {
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
        spinner.setAdapter(mAdapterSpNumWord);
        title.setText(memoConfig.getTitle());
        mListSpinner.add(spinner);
        mParentViewMemoConfig.addView(viewSpinner);
    }
}

