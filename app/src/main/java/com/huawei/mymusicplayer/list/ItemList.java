package com.huawei.mymusicplayer.list;

import java.io.Serializable;

public class ItemList implements Serializable {
    private int key;
    private int resoucerid;
    private String title;
    private int type;
    private boolean isFeatured;
    private String codeCategory;

    public ItemList(int key, String codeCategory ,int resoucerid, String title, int type, boolean isFeatured) {
        this.key = key;
        this.codeCategory = codeCategory;
        this.resoucerid = resoucerid;
        this.title = title;
        this.type = type;
        this.isFeatured = isFeatured;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
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

    public void setCodeCategory(String codeCategory){
        this.codeCategory = codeCategory;
    }
    public String getCodeCategory(){
        return codeCategory;
    }
}
