package com.huawei.mymusicplayer;

import java.util.HashMap;
import java.util.Map;

public class Playlist {
    String key;
    String account_id;
    String name;
    public Playlist() {

    }

    public Playlist(String key, String account_id, String name) {
        this.key = key;
        this.account_id = account_id;
        this.name = name;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
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
        result.put("name", name);
        return result;
    }
}
