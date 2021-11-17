package com.huawei.mymusicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
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
    public static final String PROFILE_INFORMATION = "profile";
    Button btn_addSong;
    ImageView backActivity;
    ScrollView scrollViewListSong;
    DatabaseReference databaseSongs;
    RecyclerView listViewSongs;
    FavoriteSongList favoriteSongList;
    List<FavoriteSong> favoriteSongLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);
        scrollViewListSong = findViewById(R.id.scrollViewListSong);
        listViewSongs = findViewById(R.id.lv_FavoriteSong);
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listViewSongs.setLayoutManager(linearLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        listViewSongs.addItemDecoration(itemDecoration);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseSongs = FirebaseDatabase.getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("favorite_song");
        SharedPreferences prefs = getSharedPreferences(PROFILE_INFORMATION, MODE_PRIVATE);
        String userID = prefs.getString("union_id", "has none");

        databaseSongs.orderByChild("userID").equalTo(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteSongLists.clear();
                for (DataSnapshot songSnapshot : snapshot.getChildren()){
                    FavoriteSong song = songSnapshot.getValue(FavoriteSong.class);
                    if(userID.equals(song.getUserID())){
                        favoriteSongLists.add(new FavoriteSong(song.getId(), song.getName(), song.getArtist(), song.getUrl(), song.getUserID()));
                    }
                }
                favoriteSongList = new FavoriteSongList(AddSongActivity.this, favoriteSongLists);
                listViewSongs.setAdapter(favoriteSongList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}