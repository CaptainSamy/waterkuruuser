package wssj.co.jp.olioa.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContentImageMap {
    @SerializedName("type")
    private String type;

    @SerializedName("altText")
    private String altText;

    @SerializedName("baseUrl")
    private String baseUrl;

    @SerializedName("actions")
    private List<Actions> actions;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<Actions> getActions() {
        return actions;
    }

    public void setActions(List<Actions> actions) {
        this.actions = actions;
    }
}
