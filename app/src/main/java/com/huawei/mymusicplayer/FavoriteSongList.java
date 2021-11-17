package com.huawei.mymusicplayer;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.mymusicplayer.dialog.CustomDialogAddSong;
import com.huawei.mymusicplayer.fragment.layoutfragment.Search.SearchAdapter;
import com.huawei.mymusicplayer.fragment.layoutfragment.Search.item_search;
import com.huawei.mymusicplayer.model.FavoriteSong;

import java.util.List;
public class FavoriteSongList extends  RecyclerView.Adapter<FavoriteSongList.FavoriteSongHolder>{
    List<FavoriteSong> songList;
    EditText mNamebaihat, mNamecasi, mUrl;
    Context context;
    DatabaseReference databaseSongs;
    public FavoriteSongList(Context context, List<FavoriteSong> songList) {
        this.songList = songList;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteSongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("TAG", "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_layout, parent, false);
        return new FavoriteSongHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteSongHolder holder, int position) {
        FavoriteSong song = songList.get(position);
        if (song == null){
            return;
        }
        holder.txtNameSong.setText(song.getName());
        holder.txtNameSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteSong favoriteSong = song;
                Intent toMainActivity = new Intent(context, MainActivity.class);
                toMainActivity.putExtra("songID",favoriteSong.getId());
                toMainActivity.putExtra("status","favorite_song");
                context.startActivity(toMainActivity);
            }
        });

        holder.imvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("My Music Player")
                        .setMessage("Bạn có chắc muốn xoá bài hát này không?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseSongs = FirebaseDatabase.getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("favorite_song");
                                FavoriteSong favoriteSong =song;
                                databaseSongs.child(String.valueOf(favoriteSong.getId())).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        Toast.makeText(context, "Delete song success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        holder.update_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_update_song);

                Window window = dialog.getWindow();
                if(window == null){
                    return;
                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);

                EditText edit_name = dialog.findViewById(R.id.ed_nameSong);
                EditText ed_artist = dialog.findViewById(R.id.ed_artist);
                EditText ed_urlSong = dialog.findViewById(R.id.ed_urlSong);

                Button btn_edit = dialog.findViewById(R.id.btn_dialog_update_song);
                Button btn_cancel = dialog.findViewById(R.id.btn_dialog_cancel);

                edit_name.setText(song.getName());
                ed_artist.setText(song.getArtist());
                ed_urlSong.setText(song.getUrl());

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newName = edit_name.getText().toString().trim();
                        String newArtist = ed_artist.getText().toString().trim();
                        String newUrl = ed_urlSong.getText().toString().trim();
                        song.setName(newName);
                        song.setName(newArtist);
                        song.setName(newUrl);
//                        databaseSongs.child(String.valueOf(song.getId())).updateChildren(song.toMap(), new DatabaseReference.CompletionListener() {
//                            @Override
//                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                                Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
//                                dialog.dismiss();
//                            }
//                        });
                    }
                });
                dialog.show();
            }
        });

    }
    @Override
    public int getItemCount() {
        if (songList != null){
            return songList.size();
        }
        return 0;
    }
    protected class FavoriteSongHolder extends  RecyclerView.ViewHolder{
        private TextView txtNameSong;
        private ImageView imvDelete, update_song;

        public FavoriteSongHolder(@NonNull View itemView) {
            super(itemView);
            txtNameSong = itemView.findViewById(R.id.tv_nameSong);
            imvDelete = itemView.findViewById(R.id.imv_deleteSong);
            update_song = itemView.findViewById(R.id.update_song);
        }
    }
}

