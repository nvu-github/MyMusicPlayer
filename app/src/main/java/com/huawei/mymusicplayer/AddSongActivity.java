package com.huawei.mymusicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.mymusicplayer.dialog.CustomDialogAddSong;
import com.huawei.mymusicplayer.model.FavoriteSong;

import java.util.ArrayList;
import java.util.List;

public class AddSongActivity extends AppCompatActivity {
    private static final String TAG = "AddSongActivity";
    Button btn_addSong;
    ImageView backActivity;
    ScrollView scrollViewListSong;
    DatabaseReference databaseSongs;
    ListView listViewSongs;
    List<FavoriteSong> favoriteSongLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);
        scrollViewListSong = (ScrollView)findViewById(R.id.scrollViewListSong);
        listViewSongs = (ListView) findViewById(R.id.lv_FavoriteSong);
        btn_addSong = findViewById(R.id.btn_addSong);
        backActivity = findViewById(R.id.backActivity);
        favoriteSongLists = new ArrayList<FavoriteSong>();
        btn_addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogAddSong dialogAddSong = new CustomDialogAddSong(AddSongActivity.this);
                dialogAddSong.show();
            }
        });
        backActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listViewSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ImageView deleteSong = parent.findViewById(R.id.imv_deleteSong);
                TextView targetSong = parent.findViewById(R.id.tv_nameSong);
                deleteSong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(AddSongActivity.this)
                                .setTitle(getString(R.string.app_name))
                                .setMessage("Bạn có chắc muốn xoá bài hát này không?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        databaseSongs = FirebaseDatabase.getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("favorite_song");
                                        FavoriteSong favoriteSong =(FavoriteSong) parent.getItemAtPosition(position);
                                        databaseSongs.child(String.valueOf(favoriteSong.getId())).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                Toast.makeText(AddSongActivity.this, "Delete song success", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                });
                targetSong.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FavoriteSong favoriteSong =(FavoriteSong) parent.getItemAtPosition(position);
                        Intent toMainActivity = new Intent(AddSongActivity.this, MainActivity.class);
                        toMainActivity.putExtra("songID",favoriteSong.getId());
                        toMainActivity.putExtra("status","favorite_song");
                        startActivity(toMainActivity);
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseSongs = FirebaseDatabase.getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("favorite_song");
        databaseSongs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteSongLists.clear();
                for (DataSnapshot songSnapshot : snapshot.getChildren()){
                    FavoriteSong favoriteSong = songSnapshot.getValue(FavoriteSong.class);
                    favoriteSongLists.add(favoriteSong);
                }
                FavoriteSongList adapter = new FavoriteSongList(AddSongActivity.this, favoriteSongLists);
                listViewSongs.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}