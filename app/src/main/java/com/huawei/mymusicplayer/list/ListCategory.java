package com.huawei.mymusicplayer.list;

import com.huawei.mymusicplayer.list.ItemList;

import java.util.List;

public class ListCategory {
    private String name_category;
    private List<ItemList> itemHomes;

    public ListCategory(String name_category, List<ItemList> itemHomes) {
        this.name_category = name_category;
        this.itemHomes = itemHomes;
    }

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }

    public List<ItemList> getItemHomes() {
        return itemHomes;
    }

    public void setItemHomes(List<ItemList> itemHomes) {
        this.itemHomes = itemHomes;
    }
}
