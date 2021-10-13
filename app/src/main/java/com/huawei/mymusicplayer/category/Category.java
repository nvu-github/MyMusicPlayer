package com.huawei.mymusicplayer.category;

public class Category {
    private int id;
    private int resourceImg;
    private String name;
    private String category;
    private String album;

    public Category(){}

    public Category(int id, int resourceImg, String name, String category, String album) {
        this.id = id;
        this.resourceImg = resourceImg;
        this.name = name;
        this.category = category;
        this.album = album;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResourceImg() {
        return resourceImg;
    }

    public void setResourceImg(int resourceImg) {
        this.resourceImg = resourceImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
