package com.huawei.mymusicplayer;

public class Playlist {
    String tenPlaylist;
    int imgPlaylist;

    public Playlist(String tenPlaylist, int imgPlaylist) {
        this.tenPlaylist = tenPlaylist;
        this.imgPlaylist = imgPlaylist;
    }

    public String getTenPlaylist() {
        return tenPlaylist;
    }

    public void setTenPlaylist(String tenPlaylist) {
        this.tenPlaylist = tenPlaylist;
    }

    public int getImgPlaylist() {
        return imgPlaylist;
    }

    public void setImgPlaylist(int imgPlaylist) {
        this.imgPlaylist = imgPlaylist;
    }
}
