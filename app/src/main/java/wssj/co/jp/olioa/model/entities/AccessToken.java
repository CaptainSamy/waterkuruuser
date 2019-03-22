package wssj.co.jp.olioa.model.entities;

/**
 * Created by DaiKySy on 3/15/2019.
 */
public class AccessToken {

    private String accessToken;

    private String refreshToken;

    private long expired;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpired() {
        return expired;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }
}
