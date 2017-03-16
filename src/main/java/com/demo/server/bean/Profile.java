package com.demo.server.bean;

/**
 * Created by Vonderland on 2017/3/12.
 */
public class Profile {
    private long uid;
    private String email;
    private String nickName;
    private boolean gender;
    private String avatar;
    private long loverId;
    private boolean isBlack;

    public long getLoverId() {
        return loverId;
    }

    public void setLoverId(long loverId) {
        this.loverId = loverId;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public void setBlack(boolean black) {
        isBlack = black;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Profile(User user) {
        uid = user.getUid();
        email = user.getEmail();
        nickName = user.getNickName();
        gender = user.isGender();
        avatar = user.getAvatar();
    }
}
