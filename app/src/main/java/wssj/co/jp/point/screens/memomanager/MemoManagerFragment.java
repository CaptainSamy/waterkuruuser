package wssj.co.jp.point.screens.memomanager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import wssj.co.jp.point.model.memo.ListServiceResponse;
import wssj.co.jp.point.model.memo.MemoDynamicResponse;
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
        implements IMemoManagerView, AdapterView.OnItemSelectedListener, PhotoDialog.IOnDeleteButtonClickListener {

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

    private Spinner mSpinnerServices;

    private PhotoDialog mPhotoDialog;

    private DialogChoose mDialogChoose;

    private List<ListServiceResponse.Service> mListService;

    private int mServiceId;

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
        mSpinnerServices = (Spinner) rootView.findViewById(R.id.spServices);
        mPhotoDialog = new PhotoDialog(getActivityContext(), this);
        mDialogChoose = new DialogChoose(getActivityContext());
    }

    @Override
    protected void initAction() {
        mSpinnerServices.setOnItemSelectedListener(this);
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
        List<MemoDynamicResponse.UserMemoData.UserMemoValue> valuesConfig = userConfig.getListMemoValue();
        if (memoConfig != null && memoConfig.size() > 0) {
//            if (isAdded()) {
            onCreateMemoDynamic(memoConfig, valuesConfig, 0);
//            }
        }
    }

    @Override
    public void onGetMemoConfigFailure(String message) {
        showToast(message);
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
        mSpinnerServices.setAdapter(mAdapterSpNumWord);
    }

    @Override
    public void onGetListServicesFailure(String message) {
        showToast(message);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (mListService != null && mListService.size() > 0) {
            mServiceId = mListService.get(position).getId();
            mParentViewMemoConfig.removeAllViews();
            getPresenter().getMemoConfigByServiceId(mServiceId);
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
                Toast.makeText(getActivityContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivityContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
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
        if (!TextUtils.isEmpty(imagePath) && mListImage != null) {
            fillImage(imagePath, mCurrentImageClicked, true);
            mListImage.get(mPositionImageClicked).setUrlImage(imagePath);
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
    public void onDeleteClicked(int imageCode) {

        if (mCurrentImageClicked != null) {
            mListImage.get(mPositionImageClicked).setUrlImage(Constants.EMPTY_STRING);
            mCurrentImageClicked.setImageDrawable(ContextCompat.getDrawable(getActivityContext(), R.drawable.ic_add_image));
            mCurrentImageClicked.setTag(R.id.shared_drawable, null);
        }
    }


    /* Handle create memo dynamic*/

    public void onCreateMemoDynamic(List<MemoDynamicResponse.UserMemoData.UserMemoConfig> listMemoConfig, final List<MemoDynamicResponse.UserMemoData.UserMemoValue> listValuesConfig, int position) {
        if (position < listMemoConfig.size()) {
            MemoDynamicResponse.UserMemoData.UserMemoConfig memoConfig = listMemoConfig.get(position);
            switch (memoConfig.getType()) {
                case Constants.MemoConfig.TYPE_SHORT_TEXT:
                    handleCreateEditText(listMemoConfig, listValuesConfig, position);
                    break;
                case Constants.MemoConfig.TYPE_LONG_TEXT:
                    handleCreateEditText(listMemoConfig, listValuesConfig, position);
                    break;
                case Constants.MemoConfig.TYPE_IMAGE:
                    handleCreateImage(listMemoConfig, listValuesConfig, position);
                    break;
                case Constants.MemoConfig.TYPE_COMBO_BOX:
                    //check box in android
                    handleCreateComboBox(listMemoConfig, listValuesConfig, position);
                    break;
                case Constants.MemoConfig.TYPE_SWITCH:
                    handleCreateSwitch(listMemoConfig, listValuesConfig, position);
                    break;
                case Constants.MemoConfig.TYPE_LEVEL:
//                    handleCreateLevel(listMemoConfig, listValuesConfig, position);
                    break;
                default:
                    onCreateMemoDynamic(listMemoConfig, listValuesConfig, position + 1);
                    break;
            }
        } else {
            View view = LayoutInflater.from(getActivityContext()).inflate(R.layout.layout_image_save, null);
            TextView mButtonSave = (TextView) view.findViewById(R.id.tvSave);
            mParentViewMemoConfig.addView(view);
            mButtonSave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    getPresenter().updateUserMemo(mServiceId, listValuesConfig);
                }
            });
        }
    }

    public void loopCreateMemoDynamic(View viewChild, final List<MemoDynamicResponse.UserMemoData.UserMemoConfig> listMemoConfig, final List<MemoDynamicResponse.UserMemoData.UserMemoValue> listValuesConfig, final int position) {
        Animation animation = AnimationUtils.loadAnimation(getActivityContext(), R.anim.slide_bottom_top);
        viewChild.startAnimation(animation);
        mParentViewMemoConfig.addView(viewChild);
        new Handler().

                postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        onCreateMemoDynamic(listMemoConfig, listValuesConfig, position + 1);
                    }
                }, Constants.MemoConfig.TIME_DELAY_SHOW_VIEW);
    }

    /*Handle create edit text*/
    public void handleCreateEditText(final List<MemoDynamicResponse.UserMemoData.UserMemoConfig> listMemoConfig, final List<MemoDynamicResponse.UserMemoData.UserMemoValue> listValuesConfig, final int position) {
        MemoDynamicResponse.UserMemoData.UserMemoConfig memoConfig = listMemoConfig.get(position);
        MemoDynamicResponse.UserMemoData.UserMemoValue memoValue = listValuesConfig.get(position);
        if (memoConfig == null) return;
        if (mListEditText == null) {
            mListEditText = new ArrayList<>();

        }
        TextView title;
        EditText editText;

        final MemoDynamicResponse.UserMemoData.UserMemoValue.Value value = memoValue.getValue();
        View viewEditText = LayoutInflater.from(getActivityContext()).inflate(R.layout.memo_config_edittext, null);
        title = (TextView) viewEditText.findViewById(R.id.titleEditText);
        editText = (EditText) viewEditText.findViewById(R.id.content);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                value.setValue(s.toString());
            }
        });

        title.setText(memoConfig.getTitle());
        if (memoConfig.getConfig() != null && memoConfig.getConfig().getPlaceholder() != null) {
            editText.setHint(memoConfig.getConfig().getPlaceholder());
        }
        if (memoValue.getValue() != null) {
            editText.setText(memoValue.getValue().getValue());
        }
        if (memoConfig.getType() == Constants.MemoConfig.TYPE_LONG_TEXT) {
            editText.setLines(Constants.MemoConfig.NUMBER_LINE);
        } else {
            editText.setSingleLine(true);
        }
        mListEditText.add(editText);
        loopCreateMemoDynamic(viewEditText, listMemoConfig, listValuesConfig, position);

    }

    /*
       Handle create Image
     */
    private ImageView mCurrentImageClicked;

    private int mPositionImageClicked;

    private List<ExpandableHeightGridView> mListGridView;

    List<MemoDynamicResponse.UserMemoData.UserMemoValue.Value.Image> mListImage;

    public void handleCreateImage(final List<MemoDynamicResponse.UserMemoData.UserMemoConfig> listMemoConfig, final List<MemoDynamicResponse.UserMemoData.UserMemoValue> listValuesConfig, final int position) {
        MemoDynamicResponse.UserMemoData.UserMemoConfig memoConfig = listMemoConfig.get(position);
        MemoDynamicResponse.UserMemoData.UserMemoValue memoValue = listValuesConfig.get(position);

        TextView title;
        final ExpandableHeightGridView gridView;
        if (mListGridView == null) {
            mListGridView = new ArrayList<>();
        }
        List<MemoDynamicResponse.UserMemoData.UserMemoValue.Value.Image> listImage = memoValue.getValue().getListImage();
        View viewGrid = LayoutInflater.from(getActivityContext()).inflate(R.layout.memo_config_images, null);
        title = (TextView) viewGrid.findViewById(R.id.titleImages);
        gridView = (ExpandableHeightGridView) viewGrid.findViewById(R.id.gridView);
        gridView.setExpanded(true);
        title.setText(memoConfig.getTitle());
        ImageAdapter imageAdapter = new ImageAdapter(getActivityContext(), listImage, memoConfig.getConfig().getNumberImages());
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPositionImageClicked = position;
                mCurrentImageClicked = (ImageView) gridView.getChildAt(position);
                getPresenter().onImageViewClicked((Drawable) view.getTag(R.id.shared_drawable), REQUEST_CODE_PICKER_PHOTO_1, REQUEST_CODE_CAMERA_PHOTO_1);
            }
        });
        mListImage = memoValue.getValue().getListImage();
        loopCreateMemoDynamic(viewGrid, listMemoConfig, listValuesConfig, position);
    }

    private List<EditText> mListEditText;

    /*
   Handle create Radio
   */
    private List<Switch> mListRadioGroup;

    public void handleCreateSwitch(final List<MemoDynamicResponse.UserMemoData.UserMemoConfig> listMemoConfig, final List<MemoDynamicResponse.UserMemoData.UserMemoValue> listValuesConfig, final int position) {
        MemoDynamicResponse.UserMemoData.UserMemoConfig memoConfig = listMemoConfig.get(position);
        final MemoDynamicResponse.UserMemoData.UserMemoValue memoValue = listValuesConfig.get(position);
        if (memoConfig == null) return;
        if (mListRadioGroup == null) {
            mListRadioGroup = new ArrayList<>();
        }
        TextView title;
        final Switch switchView;
        View viewSwitch = LayoutInflater.from(getActivityContext()).inflate(R.layout.memo_config_switch, null);
        title = (TextView) viewSwitch.findViewById(R.id.titleSwitch);
        switchView = (Switch) viewSwitch.findViewById(R.id.switchView);
        switchView.setChecked(memoValue.getValue().getStatus());
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                memoValue.getValue().setStatus(isChecked);
            }
        });
        title.setText(memoConfig.getTitle());
        loopCreateMemoDynamic(viewSwitch, listMemoConfig, listValuesConfig, position + 1);
    }

    /*
       Handle create Drop
     */
    private List<Spinner> mListSpinner;

    public void handleCreateComboBox(final List<MemoDynamicResponse.UserMemoData.UserMemoConfig> listMemoConfig, final List<MemoDynamicResponse.UserMemoData.UserMemoValue> listValuesConfig, final int position) {
        MemoDynamicResponse.UserMemoData.UserMemoConfig memoConfig = listMemoConfig.get(position);
        final MemoDynamicResponse.UserMemoData.UserMemoValue memoValue = listValuesConfig.get(position);
        if (memoConfig == null) return;
        if (mListSpinner == null) {
            mListSpinner = new ArrayList<>();
        }
        TextView title;
        Spinner spinner;
        View viewSpinner = LayoutInflater.from(getActivityContext()).inflate(R.layout.memo_config_spinner, null);
        title = (TextView) viewSpinner.findViewById(R.id.titleSpinner);
        spinner = (Spinner) viewSpinner.findViewById(R.id.spServices);
        final MemoDynamicResponse.UserMemoData.UserMemoValue.Value value = memoValue.getValue();
        final List<String> list = memoConfig.getConfig().getListComboBox();
        if (list != null && list.size() > 0) {
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
            spinner.setAdapter(mAdapterSpNumWord);
        }
        spinner.setSelection(value.getSelectedItem());
        title.setText(memoConfig.getTitle());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                value.setSelectedItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mListSpinner.add(spinner);
        loopCreateMemoDynamic(viewSpinner, listMemoConfig, listValuesConfig, position);
    }
}

