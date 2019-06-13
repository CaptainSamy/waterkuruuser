package wssj.co.jp.olioa.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;

import wssj.co.jp.olioa.App;

/**
 * Created by HieuPT on 7/6/2017.
 */

public final class AmazonS3Utils {

//    private static final String BUCKET_NAME = "pointsolution.dev/img_memo_user";
//
//    public static final String BASE_IMAGE_URL = "https://s3-ap-northeast-1.amazonaws.com/" + BUCKET_NAME + "/";
//
//    private static final Region BUCKET_REGION = Region.getRegion(Regions.AP_NORTHEAST_1);
//
//    private static final String ACCESS_KEY = "AKIAI55HXF5OUGWJYN4A";
//
//    private static final String SECRET_KEY = "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";
//
//    private TransferUtility mTransferUtility;
//
//    private static AmazonS3Utils sInstance;
//
//    public static AmazonS3Utils getInstance() {
//        if (sInstance == null) {
//            sInstance = new AmazonS3Utils();
//        }
//        return sInstance;
//    }
//
//    public void upload(File file, String fileName, TransferListener transferListener) {
//        if (file != null && file.exists()) {
//            TransferObserver transferObserver = mTransferUtility.upload(BUCKET_NAME, fileName, file);
//            transferObserver.setTransferListener(transferListener);
//        } else {
//            Logger.d("AmazonS3Utils", "file not found");
//        }
//    }
//
//    private AmazonS3Utils() {
//        AmazonS3 s3 = new AmazonS3Client(new AWSCredentials() {
//
//            @Override
//            public String getAWSAccessKeyId() {
//                return ACCESS_KEY;
//            }
//
//            @Override
//            public String getAWSSecretKey() {
//                return SECRET_KEY;
//            }
//        });
//        s3.setRegion(BUCKET_REGION);
//        mTransferUtility = new TransferUtility(s3, App.getInstance());
//    }
}
