package com.huawei.mymusicplayer.fragment.layoutfragment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.mymusicplayer.Playlist;
import com.huawei.mymusicplayer.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {
    Activity activity;
    int layout;
    ArrayList<Playlist> arrPlaylist;


    public CustomAdapter(@NonNull Activity activity, @LayoutRes int layout, @NonNull ArrayList<Playlist> arrPlaylist) {
        super(activity, layout, arrPlaylist);
        this.activity = activity;
        this.layout = layout;
        this.arrPlaylist = arrPlaylist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        convertView = layoutInflater.inflate(layout, null);

        ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
        TextView tenPlaylist = (TextView) convertView.findViewById(R.id.tenPlaylist);

        avatar.setImageResource(arrPlaylist.get(position).getImgPlaylist());
        tenPlaylist.setText(arrPlaylist.get(position).getTenPlaylist());

        return convertView;
    }
}
