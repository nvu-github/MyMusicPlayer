package com.huawei.mymusicplayer.home;

public class ItemBanner {
    private int resourceId;
    private int keyimage;

    public ItemBanner(int resourceId, int keyimage) {
        this.resourceId = resourceId;
        this.keyimage = keyimage;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getKeyimage() {
        return keyimage;
    }

    public void setKeyimage(int keyimage) {
        this.keyimage = keyimage;
    }
}
