package com.huawei.mymusicplayer;

public class Song {
    private String id;
    private String name;
    private String artist;
    private String category;
    private String album;
    private String link;

    public Song(String id, String name, String artist, String category, String album, String link) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.category = category;
        this.album = album;
        this.link = link;
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

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", category='" + category + '\'' +
                ", album='" + album + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
