package com.huawei.mymusicplayer.model;

public class FavoriteSong {


    private String id;
    private String name;
    private String url;
    private String userID;

    public FavoriteSong() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public FavoriteSong(String id, String name, String url, String userID) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.userID = userID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
