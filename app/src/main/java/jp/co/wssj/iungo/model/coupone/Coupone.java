package jp.co.wssj.iungo.model.coupone;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import jp.co.wssj.iungo.model.GsonSerializable;

/**
 * Created by thanghn on 1/2/2018.
 */

public class Coupone implements GsonSerializable, Serializable {
    @SerializedName("id")
    private int idUserCoupon;

    @SerializedName("coupon_id")
    private int couponId;

    @SerializedName("code_coupon")
    private String codeCoupon;

    @SerializedName("time_end_coupon")
    private long timeEndCoupon;

    @SerializedName("time_start_coupon")
    private long timeStartCoupon;

    @SerializedName("is_used")
    private int isUse;

    @SerializedName("is_use_again")
    private int isUseAgain;

    @SerializedName("img_coupon")
    private String imgCoupon;

    @SerializedName("name_coupon")
    private String nameCoupon;

    @SerializedName("policy")
    private String policy;

    @SerializedName("key_gen")
    private String keyGen;

    @SerializedName("store_img")
    private String storeImg;

    @SerializedName("store_name")
    private String storeName;

    @SerializedName("type_coupon_name")
    private String typeCouponName;


    private int count;
    private int  isLike;

    public Coupone() {
    }

    public int getIdUserCoupon() {
        return idUserCoupon;
    }

    public void setIdUserCoupon(int idUserCoupon) {
        this.idUserCoupon = idUserCoupon;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getCodeCoupon() {
        return codeCoupon;
    }

    public void setCodeCoupon(String codeCoupon) {
        this.codeCoupon = codeCoupon;
    }

    public long getTimeEndCoupon() {
        return timeEndCoupon;
    }

    public void setTimeEndCoupon(long timeEndCoupon) {
        this.timeEndCoupon = timeEndCoupon;
    }

    public long getTimeStartCoupon() {
        return timeStartCoupon;
    }

    public void setTimeStartCoupon(long timeStartCoupon) {
        this.timeStartCoupon = timeStartCoupon;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }

    public int getIsUseAgain() {
        return isUseAgain;
    }

    public void setIsUseAgain(int isUseAgain) {
        this.isUseAgain = isUseAgain;
    }

    public String getImgCoupon() {
        return imgCoupon;
    }

    public void setImgCoupon(String imgCoupon) {
        this.imgCoupon = imgCoupon;
    }

    public String getNameCoupon() {
        return nameCoupon;
    }

    public void setNameCoupon(String nameCoupon) {
        this.nameCoupon = nameCoupon;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getKeyGen() {
        return keyGen;
    }

    public void setKeyGen(String keyGen) {
        this.keyGen = keyGen;
    }

    public String getStoreImg() {
        return storeImg;
    }

    public void setStoreImg(String storeImg) {
        this.storeImg = storeImg;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getTypeCouponName() {
        return typeCouponName;
    }

    public void setTypeCouponName(String typeCouponName) {
        this.typeCouponName = typeCouponName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }
}
