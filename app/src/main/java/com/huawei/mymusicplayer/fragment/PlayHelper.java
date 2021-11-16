package com.huawei.mymusicplayer.fragment;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.hms.audiokit.player.callback.HwAudioConfigCallBack;
import com.huawei.hms.audiokit.player.manager.HwAudioConfigManager;
import com.huawei.hms.audiokit.player.manager.HwAudioEffectManager;
import com.huawei.hms.audiokit.player.manager.HwAudioManager;
import com.huawei.hms.audiokit.player.manager.HwAudioManagerFactory;
import com.huawei.hms.audiokit.player.manager.HwAudioPlayerConfig;
import com.huawei.hms.audiokit.player.manager.HwAudioPlayerManager;
import com.huawei.hms.audiokit.player.manager.HwAudioQueueManager;
import com.huawei.hms.audiokit.player.manager.HwAudioStatusListener;
import com.huawei.mymusicplayer.model.FavoriteSong;
import com.huawei.mymusicplayer.model.Song;
import com.huawei.mymusicplayer.home.ItemSongHome;
import com.huawei.mymusicplayer.listsong.SampleData;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayHelper {

    private static final String TAG = "PlayHelper";

    private static final int SIZE_M = 1024 * 1024;

    private static final PlayHelper INSTANCE = new PlayHelper();

    private HwAudioPlayerManager mHwAudioPlayerManager;

    private HwAudioQueueManager mHwAudioQueueManager;

    private HwAudioConfigManager mHwAudioConfigManager;

    private HwAudioManager mHwAudioManager;

    private List<HwAudioStatusListener> mTempListeners = new CopyOnWriteArrayList<>();

    private SampleData sampleData = new SampleData();

    private HwAudioEffectManager effectManager;

    private PlayHelper() {
    }

    public static PlayHelper getInstance() {
        return INSTANCE;
    }

    @SuppressLint("StaticFieldLeak")
    public void init(final Context context) {
        Log.i(TAG, "init start");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                HwAudioPlayerConfig hwAudioPlayerConfig = new HwAudioPlayerConfig(context);
                HwAudioManagerFactory.createHwAudioManager(hwAudioPlayerConfig, new HwAudioConfigCallBack() {
                    @Override
                    public void onSuccess(HwAudioManager hwAudioManager) {
                        try {
                            Log.i(TAG, "createHwAudioManager onSuccess");
                            mHwAudioManager = hwAudioManager;
                            mHwAudioPlayerManager = hwAudioManager.getPlayerManager();
                            mHwAudioQueueManager = hwAudioManager.getQueueManager();
                            mHwAudioConfigManager = hwAudioManager.getConfigManager();
                            effectManager = hwAudioManager.getEffectManager();
                            doRestInit(context);
                        } catch (Exception e) {
                            Log.e(TAG, "player init fail", e);
                        }
                    }

                    @Override
                    public void onError(int errorCode) {
                        Log.e(TAG, "init err:" + errorCode);
                    }
                });
                return null;
            }
        }.execute();
    }


    private void doRestInit(final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (HwAudioStatusListener listener : mTempListeners) {
                    try {
                        mHwAudioManager.addPlayerStatusListener(listener);
                    } catch (RemoteException e) {
                        Log.e(TAG, TAG, e);
                    }
                }
                mHwAudioConfigManager.setSaveQueue(true);
                mHwAudioConfigManager.setNotificationFactory(
                        new SubINotificationFactory((Application) context.getApplicationContext(), mHwAudioManager));
            }
        });
    }

    public void addListener(HwAudioStatusListener listener) {
        if (mHwAudioManager != null) {
            try {
                mHwAudioManager.addPlayerStatusListener(listener);
            } catch (RemoteException e) {
                Log.e(TAG, TAG, e);
            }
        } else {
            mTempListeners.add(listener);
        }
    }

    public void removeListener(HwAudioStatusListener listener) {
        if (mHwAudioManager != null) {
            try {
                mHwAudioManager.removePlayerStatusListener(listener);
            } catch (RemoteException e) {
                Log.e(TAG, TAG, e);
            }
        }
        mTempListeners.remove(listener);
    }

    public void buildLocal(Context context, List<ItemSongHome> itemSongHomes) {
        if (context != null && mHwAudioPlayerManager != null) {
            mHwAudioPlayerManager.playList(sampleData.getLocalPlayList(context,itemSongHomes), 0, 0);
        }else{
            Log.i(TAG, "Can't not buld local");
        }
    }

    public void seek(long pos) {
        Log.i(TAG, "seek: " + pos);
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "seek fail ");
            return;
        }
        mHwAudioPlayerManager.seekTo((int) pos);
    }

//    public void buildOnlineList() {
//        Log.i(TAG, "buildOnlineList");
//        List<HwAudioPlayItem> playItemList = sampleData.getOnlinePlaylist();
//        if (mHwAudioPlayerManager != null) {
//            mHwAudioPlayerManager.playList(playItemList, 0, 0);
//        }
//    }
    public void buildOnlineList(List<Song> songs) {
        Log.i(TAG, "buildOnlineList");
        List<HwAudioPlayItem> playItemList = sampleData.getOnlinePlaylist(songs);
        if (mHwAudioPlayerManager != null) {
            Log.i(TAG, "buildOnlineList true");

            mHwAudioPlayerManager.playList(playItemList, 0, 0);
        }else{
            Log.i(TAG, "buildOnlineList fail");

        }
    }
    public void setPlayMode(int mode) {
        Log.i(TAG, "setPlayMode: " + mode);
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "play err");
            return;
        }
        mHwAudioPlayerManager.setPlayMode(mode);
    }
    public int getPlayMode() {
        if (mHwAudioPlayerManager == null) {
            Log.w(TAG, "getPlayMode err");
            return 0;
        }
        return mHwAudioPlayerManager.getPlayMode();
    }

    public int getBufferPercentage() {
        if (mHwAudioPlayerManager == null) {
            return 0;
        }
        return mHwAudioPlayerManager.getBufferPercent();
    }

    public long getPosition() {
        if (mHwAudioPlayerManager == null) {
            return 0;
        }
        return mHwAudioPlayerManager.getOffsetTime();
    }

    public void stop() {
        if (mHwAudioPlayerManager == null) {
            return;
        }
        mHwAudioPlayerManager.stop();
    }

    public long getDuration() {
        if (mHwAudioPlayerManager == null) {
            return 0;
        }
        return mHwAudioPlayerManager.getDuration();
    }

    public boolean isPlaying() {
        return mHwAudioPlayerManager != null && mHwAudioPlayerManager.isPlaying();
    }

    public void next() {
        Log.i(TAG, "next");
        if (mHwAudioPlayerManager == null) {
            return;
        }
        mHwAudioPlayerManager.playNext();
    }

    public void prev() {
        Log.i(TAG, "prev");
        if (mHwAudioPlayerManager == null) {
            return;
        }
        mHwAudioPlayerManager.playPre();
    }

    public void play() {
        Log.i(TAG, "play");
        if (mHwAudioPlayerManager == null) {
            return;
        }
        mHwAudioPlayerManager.play();
    }

    public void pause() {
        Log.i(TAG, "pause");
        if (mHwAudioPlayerManager == null) {
            return;
        }
        mHwAudioPlayerManager.pause();
    }

//    public void setPlayMode(int mode) {
//        Log.i(TAG, "setPlayMode: " + mode);
//        if (mHwAudioPlayerManager == null) {
//            Log.w(TAG, "play err");
//            return;
//        }
//        mHwAudioPlayerManager.setPlayMode(mode);
//    }
//
//    public int getPlayMode() {
//        if (mHwAudioPlayerManager == null) {
//            Log.w(TAG, "getPlayMode err");
//            return 0;
//        }
//        return mHwAudioPlayerManager.getPlayMode();
//    }
//
//    public HwAudioPlayItem getCurrentPlayItem() {
//        Log.i(TAG, "getCurrentPlayItem");
//        if (mHwAudioQueueManager == null) {
//            Log.w(TAG, "getCurrentPlayItem err");
//            return null;
//        }
//        return mHwAudioQueueManager.getCurrentPlayItem();
//    }

    public int getCurrentIndex() {
        if (mHwAudioQueueManager == null) {
            return 0;
        }
        return mHwAudioQueueManager.getCurrentIndex();
    }

    public List<HwAudioPlayItem> getAllPlaylist() {
        Log.i(TAG, "getAllPlaylist");
        if (mHwAudioQueueManager == null) {
            return null;
        }
        return mHwAudioQueueManager.getAllPlaylist();
    }

    public void playAt(int pos) {
        Log.i(TAG, "setQueuePosition,pos: " + pos);
        if (mHwAudioPlayerManager == null) {
            return;
        }
        mHwAudioPlayerManager.play(pos);
    }

    public boolean isBuffering() {
        return mHwAudioPlayerManager != null && mHwAudioPlayerManager.isBuffering();
    }

    public boolean isQueueEmpty() {
        return mHwAudioQueueManager != null && mHwAudioQueueManager.isQueueEmpty();
    }

//    public HwAudioEffectManager getEffectManager() {
//        return effectManager;
//    }
//
//    public void deleteItem(int pos) {
//        Log.i(TAG, "deleteItem,pos: " + pos);
//        if (mHwAudioQueueManager == null) {
//            Log.w(TAG, "deleteItem err");
//            return;
//        }
//        mHwAudioQueueManager.removeListByIndex(pos);
//    }
//
//    public String getCacheSize() {
//        Log.i(TAG, "getCacheSize ");
//        if (mHwAudioConfigManager == null) {
//            Log.w(TAG, "getCacheSize err");
//            return "";
//        }
//        long size = mHwAudioConfigManager.getPlayCacheSize() / SIZE_M;
//        return size + "M";
//    }

//    public String getUsedCacheSize() {
//        Log.i(TAG, "getUsedCacheSize ");
//        if (mHwAudioConfigManager == null) {
//            Log.w(TAG, "getUsedCacheSize err");
//            return "";
//        }
//        long size = mHwAudioConfigManager.getUsedCacheSize() / SIZE_M;
//        return size + "M";
//    }

    public void clearCache() {
        Log.i(TAG, "clearCache ");
        if (mHwAudioConfigManager == null) {
            Log.w(TAG, "clearCache err");
            return;
        }
        mHwAudioConfigManager.clearPlayCache();
    }

//
//    public void setCacheSize(long size) {
//        Log.i(TAG, "setCacheSize ,size：" + size);
//        if (mHwAudioConfigManager == null || size < 0) {
//            Log.w(TAG, "setCacheSize err");
//            return;
//        }
//        size = Math.min(size, 200 * SIZE_M);
//        mHwAudioConfigManager.setPlayCacheSize(size * SIZE_M);
//    }
}

