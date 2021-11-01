package com.huawei.mymusicplayer.fragment.layoutfragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.hms.hwid.B;
import com.huawei.mymusicplayer.MainActivity;
import com.huawei.mymusicplayer.Playlist;
import com.huawei.mymusicplayer.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{
    private List<Playlist> mList;
    private Context mContext;
    private IClickListener mIClickListener;
    public interface IClickListener{
        void onClickDeleteItem(Playlist playlist);
    }

    public CustomAdapter(Context context, List<Playlist> mList, IClickListener iClickListener) {
        this.mContext = context;
        this.mList = mList;
        this.mIClickListener = iClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Playlist playlist = mList.get(position);
        if(playlist == null){
            return;
        }
        holder.tenPlaylist.setText(playlist.getName());
        holder.delete_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickListener.onClickDeleteItem(playlist);
            }
        });
        holder.layout_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDetail(playlist);
            }
        });
    }
    private void onClickDetail(Playlist playlist)
    {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("playlist_detail", 1);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(mList != null){
            return mList.size();
        }
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tenPlaylist;
        ImageView delete_playlist;
        LinearLayout layout_item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tenPlaylist = itemView.findViewById(R.id.tenPlaylist);
            delete_playlist = itemView.findViewById(R.id.delete_playlist);
            layout_item = itemView.findViewById(R.id.layout_item);
        }
    }


}
