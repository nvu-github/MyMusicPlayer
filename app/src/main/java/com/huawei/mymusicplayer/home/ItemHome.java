package com.huawei.mymusicplayer.home;

import java.io.Serializable;

public class ItemHome implements Serializable {
    private String key;
    private int resoucerid;
    private String title;
    private int type;
    private boolean isFeatured;

    public ItemHome(String key, int resoucerid, String title, int type, boolean isFeatured) {
        this.key = key;
        this.resoucerid = resoucerid;
        this.title = title;
        this.type = type;
        this.isFeatured = isFeatured;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getResoucerid() {
        return resoucerid;
    }

    public void setResoucerid(int resoucerid) {
        this.resoucerid = resoucerid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }
}
