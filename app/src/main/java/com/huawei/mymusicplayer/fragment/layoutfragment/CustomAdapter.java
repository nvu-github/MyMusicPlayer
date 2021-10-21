package com.huawei.mymusicplayer.fragment.layoutfragment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.mymusicplayer.Playlist;
import com.huawei.mymusicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{
    private List<Playlist> mList;
    private AdapterView.OnItemClickListener itemClickListener;

    public interface OnItemClickListener{
        void onClickDeleteItem(Playlist playlist);
    }


    public CustomAdapter(List<Playlist> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Playlist playlist = mList.get(position);
        if(playlist == null){
            return;
        }
        holder.tenPlaylist.setText("Name: " + playlist.getName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                itemClickListener.onItemClick(playlist);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mList != null){
            return mList.size();
        }
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tenPlaylist, delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tenPlaylist = itemView.findViewById(R.id.tenPlaylist);
            delete = itemView.findViewById(R.id.delete);
        }
    }


}
