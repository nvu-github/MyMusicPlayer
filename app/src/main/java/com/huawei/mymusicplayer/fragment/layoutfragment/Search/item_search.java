package com.huawei.mymusicplayer.fragment.layoutfragment.Search;

import java.io.Serializable;

public class item_search implements Serializable {
    private String idbaihat;
    private int id_imgSearch;
    private String namebaihat_search;
    private String namecasi_search;

    public item_search(String idbaihat, int id_imgSearch, String namebaihat_search, String namecasi_search) {
        this.idbaihat = idbaihat;
        this.id_imgSearch = id_imgSearch;
        this.namebaihat_search = namebaihat_search;
        this.namecasi_search = namecasi_search;
    }

    public String getIdbaihat() {
        return idbaihat;
    }

    public void setIdbaihat(String idbaihat) {
        this.idbaihat = idbaihat;
    }

    public int getId_imgSearch() {
        return id_imgSearch;
    }

    public void setId_imgSearch(int id_imgSearch) {
        this.id_imgSearch = id_imgSearch;
    }

    public String getNamebaihat_search() {
        return namebaihat_search;
    }

    public void setNamebaihat_search(String namebaihat_search) {
        this.namebaihat_search = namebaihat_search;
    }

    public String getNamecasi_search() {
        return namecasi_search;
    }

    public void setNamecasi_search(String namecasi_search) {
        this.namecasi_search = namecasi_search;
    }
}
