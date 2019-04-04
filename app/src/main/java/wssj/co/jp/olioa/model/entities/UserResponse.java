package wssj.co.jp.olioa.model.entities;

/**
 * Created by DaiKySy on 4/4/2019.
 */
public class UserResponse {

    private int id;

    private String name;

    private String avatar;

    private String newAvatar;

    private String email;

    private int sex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNewAvatar(String newAvatar) {
        this.newAvatar = newAvatar;
    }

    public String getNewAvatar() {
        return newAvatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
