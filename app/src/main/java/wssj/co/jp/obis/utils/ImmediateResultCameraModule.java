package wssj.co.jp.obis.utils;

import android.net.Uri;

import com.esafirm.imagepicker.features.camera.ImmediateCameraModule;
import com.esafirm.imagepicker.model.Image;
import com.esafirm.imagepicker.model.ImageFactory;

import java.util.List;

/**
 * Created by HieuPT on 6/9/2017.
 */

public class ImmediateResultCameraModule extends ImmediateCameraModule {

    public List<Image> getImages() {
        if (currentImagePath == null) {
            return null;
        }
        Uri uri = Uri.parse(currentImagePath);
        return ImageFactory.singleListFromPath(uri.getPath());
    }
}
