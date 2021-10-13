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
        TextView textViewArtist = (TextView) listViewItem.findViewById(R.id.tv_artist);
        TextView textViewCategory = (TextView) listViewItem.findViewById(R.id.tv_category);
        TextView textViewAlbum = (TextView) listViewItem.findViewById(R.id.tv_album);

        Song song = songList.get(position);
        textViewName.setText("Song name: "+song.getName());
        textViewArtist.setText("Artist: "+song.getArtist());
        textViewCategory.setText("Category: "+song.getCategory());
        textViewAlbum.setText("Album: "+song.getAlbum());

        return listViewItem;
    }
}
