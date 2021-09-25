package com.huawei.mymusicplayer.listsong;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.mymusicplayer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.huawei.mymusicplayer.AccountActivity.TAG;

public class SampleData {

    public List<HwAudioPlayItem> getOnlinePlaylist() {
        List<HwAudioPlayItem> playItemList = new ArrayList<>();
        HwAudioPlayItem audioPlayItem1 = new HwAudioPlayItem();
        audioPlayItem1.setAudioId("1000");
        audioPlayItem1.setSinger("Taoge");
        audioPlayItem1.setOnlinePath("https://lfmusicservice.hwcloudtest.cn:18084/HMS/audio/Taoge-chengshilvren.mp3");
        audioPlayItem1.setOnline(1);
        audioPlayItem1.setAudioTitle("chengshilvren");
        playItemList.add(audioPlayItem1);

        HwAudioPlayItem audioPlayItem2 = new HwAudioPlayItem();
        audioPlayItem2.setAudioId("1001");
        audioPlayItem2.setSinger("Taoge");
        audioPlayItem2.setOnlinePath("https://lfmusicservice.hwcloudtest.cn:18084/HMS/audio/Taoge-dayu.mp3");
        audioPlayItem2.setOnline(1);
        audioPlayItem2.setAudioTitle("dayu");
        playItemList.add(audioPlayItem2);

        HwAudioPlayItem audioPlayItem3 = new HwAudioPlayItem();
        audioPlayItem3.setAudioId("1002");
        audioPlayItem3.setSinger("Taoge");
        audioPlayItem3.setOnlinePath("https://lfmusicservice.hwcloudtest.cn:18084/HMS/audio/Taoge-wangge.mp3");
        audioPlayItem3.setOnline(1);
        audioPlayItem3.setAudioTitle("wangge");
        playItemList.add(audioPlayItem3);
        return playItemList;
    }

    public List<HwAudioPlayItem> getLocalPlayList(Context context) {
        List<HwAudioPlayItem> playItemList = new ArrayList<>();
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            if (contentResolver == null) {
                return playItemList;
            }
            cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            HwAudioPlayItem songItem;
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    if (new File(path).exists()) {
                        songItem = new HwAudioPlayItem();
                        songItem.setAudioTitle(
                                cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                        songItem
                                .setAudioId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)) + "");
                        songItem.setFilePath(path);
                        songItem
                                .setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                        playItemList.add(songItem);
                    }
                }
            }
            songItem = new HwAudioPlayItem();
            songItem.setAudioTitle("2 Phút Hơn (KAIZ Remix)");
            songItem.setAudioId("twozero".hashCode() + "");
            songItem.setFilePath("hms_res://twozero");
            songItem.setSinger("Pháo");
            playItemList.add(songItem);

            songItem = new HwAudioPlayItem();
            songItem.setAudioTitle("BBoom BBoom");
            songItem.setAudioId("bboom".hashCode() + "");
            songItem.setFilePath("hms_res://bboom");
            songItem.setSinger("MOMOLAND");
            playItemList.add(songItem);

            songItem = new HwAudioPlayItem();
            songItem.setAudioTitle("BAAM");
            songItem.setAudioId("baam".hashCode() + "");
            songItem.setFilePath("hms_res://baam");
            songItem.setSinger("MOMOLAND");
            playItemList.add(songItem);

            songItem = new HwAudioPlayItem();
            songItem.setAudioTitle("Bùa Yêu");
            songItem.setAudioId("buayeu".hashCode() + "");
            songItem.setFilePath("hms_res://buayeu");
            songItem.setSinger("Bích Phương");
            playItemList.add(songItem);

            songItem = new HwAudioPlayItem();
            songItem.setAudioTitle("Đi đu đưa đi");
            songItem.setAudioId("diduduadi".hashCode() + "");
            songItem.setFilePath("hms_res://diduduadi");
            songItem.setSinger("Bích Phương");
            playItemList.add(songItem);

            songItem = new HwAudioPlayItem();
            songItem.setAudioTitle("Trên tình bạn dưới tình yêu");
            songItem.setAudioId("upfriendshipdownlove".hashCode() + "");
            songItem.setFilePath("hms_res://upfriendshipdownlove");
            songItem.setSinger("Min");
            playItemList.add(songItem);


        } catch (Exception e) {
            Log.e(TAG, TAG, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return playItemList;
    }
    public List<HwAudioPlayItem> getLocalPlaylistCategory(Context context, ArrayList<HashMap<String, String>> dataJson) {
        List<HwAudioPlayItem> playItemList = new ArrayList<>();
        Cursor cursor = null;
        HwAudioPlayItem songItem;
        int lengthMyList = dataJson.toArray().length;
        for(Map<String, String> map : dataJson)
        {
            String tagName = map.get("name");
            Log.i("DataJson", tagName);
        }
        try {
            for(Map<String, String> map : dataJson)
            {
                String id_value = map.get("id");
                String name_value = map.get("name");
                String artist_value = map.get("artist");
                String link_value = map.get("link");
                String avatar_value = map.get("avatar");

                songItem = new HwAudioPlayItem();
                songItem.setAudioTitle(name_value);
                songItem.setAudioId(id_value.hashCode() + "");
                songItem.setFilePath(link_value);
                songItem.setSinger(artist_value);
                songItem.setMidImageURL(avatar_value);
                playItemList.add(songItem);
            }
        } catch (Exception e) {
            Log.e(TAG, TAG, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return playItemList;
    }
}
