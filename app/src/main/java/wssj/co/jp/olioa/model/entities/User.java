package wssj.co.jp.olioa.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DaiKySy on 3/15/2019.
 */
public class User {

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("name")
    private String name;

    @SerializedName("sex")
    private int sex;

    @SerializedName("type")
    private int type;

    @SerializedName("appId")
    private int appId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public User(String username, String email, String password, String name, int sex, int type, int appId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.sex = sex;
        this.type = type;
        this.appId = appId;
    }
}
