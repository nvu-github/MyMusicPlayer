package com.huawei.mymusicplayer.utils;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationUtils {

    public static final String NOTIFY_CHANNEL_ID_PLAY = "music_notify_channel_id_play";

    @TargetApi(Build.VERSION_CODES.O)
    public static void addChannel(Application application, String channelId, NotificationCompat.Builder builder) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        createNotificationChannel(application, channelId, builder);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(Application application, String channelId,
        NotificationCompat.Builder builder) {

        NotificationChannel notificationChannel =
            new NotificationChannel(channelId, "Play", NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableVibration(false);
        notificationChannel.setSound(null, null);
        NotificationManager notificationManager =
            (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(notificationChannel);
        }

        builder.setGroup(channelId);
        builder.setChannelId(channelId);
    }
}
