package wssj.co.jp.obis.model.apiTest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 12/19/2017.
 */

public class PostResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;

    public PostResponse() {
    }

    public PostResponse(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

