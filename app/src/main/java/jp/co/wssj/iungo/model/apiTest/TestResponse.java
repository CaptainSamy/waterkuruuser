package jp.co.wssj.iungo.model.apiTest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by admin on 12/19/2017.
 */

public class TestResponse {
    @SerializedName("contacts")
    private List<PostResponse> array;

    public List<PostResponse> getArray() {
        return array;
    }
}
