package com.huawei.mymusicplayer;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.huawei.mymusicplayer.fragment.PlayHelper;

public class MyApplication extends Application {

    private static final String TAG = "HwAudioApplication";

    private static Context context;

    private static class ClickEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.huawei.hms.mediacenter.cancel_notification".equals(action)) {
                Log.i(TAG, "onReceive----->cancelNotification");
                PlayHelper.getInstance().stop();
            }
        }
    }

    private BroadcastReceiver mClickEventReceiver = new ClickEventReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        IntentFilter intent = new IntentFilter();
        intent.addAction("com.huawei.hms.mediacenter.cancel_notification");
        registerReceiver(mClickEventReceiver, intent);
        PlayHelper.getInstance().init(this);
    }

    public static Context getContext() {
        return context;
    }
}
