package wssj.co.jp.olioa.model.memo;

import com.google.gson.annotations.SerializedName;

import wssj.co.jp.olioa.model.GsonSerializable;
import wssj.co.jp.olioa.model.ResponseData;

/**
 * Created by Nguyen Huu Ta on 19/5/2017.
 */

public class UserMemoResponse extends ResponseData<UserMemoResponse.UserMemoData> {

    public class UserMemoData implements GsonSerializable {

        @SerializedName("information")
        private UserMemo mUserMemo;

        public UserMemo getUserMemo() {
            return mUserMemo;
        }
    }

    public class UserMemo implements GsonSerializable {

        @SerializedName("note")
        private String mNote;

        @SerializedName("photo")
        Photo mPhoto;

        public String getNote() {
            return mNote;
        }

        public Photo getPhoto() {
            return mPhoto;
        }
    }

    public class Photo implements GsonSerializable {

        @SerializedName("photo_1")
        private String mPhoto1;

        @SerializedName("photo_2")
        private String mPhoto2;

        @SerializedName("photo_3")
        private String mPhoto3;

        @SerializedName("photo_4")
        private String mPhoto4;

        public String getPhoto1() {
            return mPhoto1;
        }

        public String getPhoto2() {
            return mPhoto2;
        }

        public String getPhoto3() {
            return mPhoto3;
        }

        public String getPhoto4() {
            return mPhoto4;
        }
    }

}
