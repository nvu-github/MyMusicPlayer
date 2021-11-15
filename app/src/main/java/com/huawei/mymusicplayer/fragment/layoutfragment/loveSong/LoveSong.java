package com.huawei.mymusicplayer.fragment.layoutfragment.loveSong;

public class LoveSong {

    String artist;
    String name;
    String id;
    String songID;
    String userID;

    public LoveSong() {
    }

    public LoveSong(String artist, String name, String id, String songID, String userID) {
        this.artist = artist;
        this.name = name;
        this.id = id;
        this.songID = songID;
        this.userID = userID;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
