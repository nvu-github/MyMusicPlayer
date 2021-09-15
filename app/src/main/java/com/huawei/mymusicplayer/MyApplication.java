package com.huawei.mymusicplayer;

import android.app.Application;

import com.huawei.mymusicplayer.fragment.PlayHelper;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PlayHelper.getInstance().init(this);
    }
}
