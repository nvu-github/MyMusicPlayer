package com.huawei.mymusicplayer.model;

import java.util.HashMap;
import java.util.Map;

public class FavoriteSong {


    private String id;
    private String name;
    private String artist;
    private String url;
    private String userID;

    public FavoriteSong() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public FavoriteSong(String id, String name, String artist, String url, String userID) {
        this.id = id;
        this.name = name;
        this.artist = artist;
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

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("artist", artist);
        result.put("url", url);
        result.put("userID", userID);
        return result;
    }

}
