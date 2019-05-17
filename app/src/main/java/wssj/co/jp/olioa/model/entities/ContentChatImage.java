package wssj.co.jp.olioa.model.entities;

import com.google.gson.annotations.SerializedName;

public class ContentChatImage {
    @SerializedName("type")
    private String type;
    @SerializedName("originalContentUrl")
    private String originalContentUrl;
    @SerializedName("previewImageUrl")
    private String previewImageUrl;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOriginalContentUrl() {
        return originalContentUrl;
    }

    public void setOriginalContentUrl(String originalContentUrl) {
        this.originalContentUrl = originalContentUrl;
    }

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }

    public void setPreviewImageUrl(String previewImageUrl) {
        this.previewImageUrl = previewImageUrl;
    }
}
