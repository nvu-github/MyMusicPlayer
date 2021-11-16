package com.huawei.mymusicplayer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.mymusicplayer.model.FavoriteSong;
import com.huawei.mymusicplayer.model.Song;

import java.util.List;

public class FavoriteSongList extends ArrayAdapter<FavoriteSong> {
    private Activity context;
    private List<FavoriteSong> favoriteSongs;

    public FavoriteSongList(Activity context, List<FavoriteSong> favoriteSongs){
        super(context, R.layout.song_list_layout, favoriteSongs);
        this.context  = context;
        this.favoriteSongs = favoriteSongs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.song_list_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.tv_nameSong);
        FavoriteSong favoriteSong = favoriteSongs.get(position);
        textViewName.setText(favoriteSong.getName());
        return listViewItem;
    }
}
