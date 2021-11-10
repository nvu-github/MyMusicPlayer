package com.huawei.mymusicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddSongActivity extends AppCompatActivity {
    private static final String TAG = "AddSongActivity";
    EditText txtName, txtArtist, txtLink;
    Button btnAdd;
    ScrollView scrollViewListSong;
    Spinner sp_category, sp_album;
    DatabaseReference databaseSongs;
    ListView listViewSongs;
    List<Song> songList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);
        databaseSongs = FirebaseDatabase.getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("songs");
        txtName     = (EditText)findViewById(R.id.et_name);
        txtArtist   = (EditText)findViewById(R.id.et_artist);
        txtLink     = (EditText)findViewById(R.id.et_link);
        sp_category = (Spinner)findViewById(R.id.sp_category);
        sp_album    = (Spinner)findViewById(R.id.sp_album);
        btnAdd      = (Button) findViewById(R.id.btn_addSong);
        scrollViewListSong = (ScrollView)findViewById(R.id.scrollViewListSong);
        listViewSongs = (ListView) findViewById(R.id.lv_listSongs);

        songList = new ArrayList<>();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addSong();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseSongs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                songList.clear();
                for (DataSnapshot songSnapshot : snapshot.getChildren()){
                    Song song = songSnapshot.getValue(Song.class);
                    songList.add(song);
                }
                Log.i("test", "onDataChange: " +songList.toString());
                SongList adapter = new SongList(AddSongActivity.this, songList);
                listViewSongs.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addSong(){
        String name = txtName.getText().toString().trim();
        String artist = txtArtist.getText().toString().trim();
        String category = sp_category.getSelectedItem().toString();
        String album = sp_album.getSelectedItem().toString();
        String link = txtLink.getText().toString().trim();
        if(!TextUtils.isEmpty(name)){
            String id = databaseSongs.push().getKey();
            Song song = new Song(id, name, artist, category, album, link);
            databaseSongs.child(id).setValue(song);
            Log.i(TAG, "addSong: " + song.toString());
            Toast.makeText(this, "Add success !!!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "You should enter a name song", Toast.LENGTH_LONG).show();
        }
    }
}