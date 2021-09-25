package com.huawei.mymusicplayer.home;

public class ItemSongHome {
    private String AudioTitle;
    private String AudioId;
    private String FilePath;
    private String Singer;

    public ItemSongHome(String audioTitle, String audioId, String filePath, String singer) {
        AudioTitle = audioTitle;
        AudioId = audioId;
        FilePath = filePath;
        Singer = singer;
    }

    public String getAudioTitle() {
        return AudioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        AudioTitle = audioTitle;
    }

    public String getAudioId() {
        return AudioId;
    }

    public void setAudioId(String audioId) {
        AudioId = audioId;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public String getSinger() {
        return Singer;
    }

    public void setSinger(String singer) {
        Singer = singer;
    }
}
