package com.huawei.mymusicplayer.fragment.layoutfragment.loveSong;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.huawei.mymusicplayer.MainActivity;
import com.huawei.mymusicplayer.Playlist;
import com.huawei.mymusicplayer.R;


import java.util.List;

public class LoveSongAdapter extends RecyclerView.Adapter<LoveSongAdapter.MyViewHolder>{
    private List<LoveSong> mList;
    private Context mContext;
    private IClickListener mIClickListener;
    public interface IClickListener{
        void onClickDeleteItem(LoveSong loveSong);
    }

    public LoveSongAdapter(List<LoveSong> mList, Context mContext, IClickListener mIClickListener) {
        this.mList = mList;
        this.mContext = mContext;
        this.mIClickListener = mIClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.lovesong_item, parent, false);
        return new LoveSongAdapter.MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final LoveSong loveSong = mList.get(position);
        if(loveSong == null){
            return;
        }
        holder.name_song.setText(loveSong.getName());
        holder.name_artist.setText(loveSong.getArtist());

        holder.delete_loveSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickListener.onClickDeleteItem(loveSong);
            }
        });
        holder.lovesong_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDetail(loveSong);
            }
        });
    }
    private void onClickDetail(LoveSong loveSong)
    {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("status", "love_song");
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
        TextView name_song, name_artist;
        ImageView delete_loveSong;
        LinearLayout lovesong_item;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_song = itemView.findViewById(R.id.name_song);
            name_artist = itemView.findViewById(R.id.name_artist);
            delete_loveSong = itemView.findViewById(R.id.delete_loveSong);
            lovesong_item = itemView.findViewById(R.id.lovesong_item);
        }
    }
}
