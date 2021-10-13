package com.huawei.mymusicplayer.listsong;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.mymusicplayer.Song;
import com.huawei.mymusicplayer.home.ItemSongHome;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;

//import static com.huawei.mymusicplayer.AccountActivity.TAG;

public class SampleData {
    DatabaseReference databaseSongs;

    public List<HwAudioPlayItem> getOnlinePlaylist(List<Song> songs) {
        List<HwAudioPlayItem> playItemList = new ArrayList<>();
        for (Song song : songs) {
            HwAudioPlayItem audioPlayItem = new HwAudioPlayItem();
            audioPlayItem.setAudioId(song.getId());
            audioPlayItem.setSinger(song.getArtist());
            audioPlayItem.setOnlinePath(song.getLink());
            audioPlayItem.setOnline(1);
            audioPlayItem.setAudioTitle(song.getName());
            playItemList.add(audioPlayItem);
        }
        return playItemList;
    }

    public List<HwAudioPlayItem> getLocalPlayList(Context context, List<ItemSongHome> itemSongHomes) {
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
            for (int i = 0 ; i < itemSongHomes.size() ; i++){
                songItem = new HwAudioPlayItem();
                songItem.setAudioTitle(itemSongHomes.get(i).getAudioTitle());
                songItem.setAudioId(itemSongHomes.get(i).getAudioId().hashCode() + "");
                songItem.setFilePath(itemSongHomes.get(i).getFilePath());
                songItem.setSinger(itemSongHomes.get(i).getSinger());
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
