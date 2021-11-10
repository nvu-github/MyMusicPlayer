package com.huawei.mymusicplayer;

import java.util.HashMap;
import java.util.Map;

public class Playlist {
    String key;
    String name;
    public Playlist() {

    }
    public Playlist(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", name);
        return result;
    }
}
