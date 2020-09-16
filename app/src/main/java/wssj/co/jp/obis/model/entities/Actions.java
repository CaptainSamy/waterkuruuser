package wssj.co.jp.obis.model.entities;

import com.google.gson.annotations.SerializedName;

public class Actions {
    @SerializedName("linkUri")
    private String linkUri;

    @SerializedName("area")
    private Area area;

    public String getLinkUri() {
        return linkUri;
    }

    public void setLinkUri(String linkUri) {
        this.linkUri = linkUri;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

}
