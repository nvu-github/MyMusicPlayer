package com.huawei.mymusicplayer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SongList extends ArrayAdapter<Song> {
    private Activity context;
    private List<Song> songList;

    public SongList(Activity context, List<Song> songList){
        super(context, R.layout.song_list_layout, songList);
        this.context  = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.song_list_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.tv_nameSong);

        Song song = songList.get(position);
       // textViewName.setText(song.getNameSong());
        return listViewItem;
    }
}
