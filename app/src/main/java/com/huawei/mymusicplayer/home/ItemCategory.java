package com.huawei.mymusicplayer.home;

import java.util.List;

public class ItemCategory {
    private String name_category;
    private List<ItemHome> itemHomes;

    public ItemCategory(String name_category, List<ItemHome> itemHomes) {
        this.name_category = name_category;
        this.itemHomes = itemHomes;
    }

    public String getName_category() {
        return name_category;
    }

    public void setName_category(String name_category) {
        this.name_category = name_category;
    }

    public List<ItemHome> getItemHomes() {
        return itemHomes;
    }

    public void setItemHomes(List<ItemHome> itemHomes) {
        this.itemHomes = itemHomes;
    }
}
