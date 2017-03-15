package com.demo.server.bean;

/**
 * Created by Vonderland on 2017/3/15.
 */
public class Couple {
    private long id;
    private long loverAId;
    private long loverBId;
    private boolean isBroken;
    private long togetherTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLoverAId() {
        return loverAId;
    }

    public void setLoverAId(long loverAId) {
        this.loverAId = loverAId;
    }

    public long getLoverBId() {
        return loverBId;
    }

    public void setLoverBId(long loverBId) {
        this.loverBId = loverBId;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public void setBroken(boolean broken) {
        isBroken = broken;
    }

    public long getTogetherTime() {
        return togetherTime;
    }

    public void setTogetherTime(long togetherTime) {
        this.togetherTime = togetherTime;
    }
}
