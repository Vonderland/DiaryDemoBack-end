package com.demo.server.bean;

/**
 * Created by Vonderland on 2017/3/11.
 */
public class Authorization {
    private long id;
    private long uid;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String token;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
