package com.huawei.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddSongActivity extends AppCompatActivity {
    EditText txtName, txtArtist;
    Button btnAdd;
    Spinner sp_category, sp_album;
    DatabaseReference databaseSongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);
        databaseSongs = FirebaseDatabase.getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("songs");
        txtName     = (EditText) findViewById(R.id.et_name);
        txtArtist   = (EditText) findViewById(R.id.et_artist);
        sp_category = (Spinner)findViewById(R.id.sp_category);
        sp_album    = (Spinner)findViewById(R.id.sp_album);
        btnAdd      = (Button) findViewById(R.id.btn_addSong);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               addSong();

            }
        });
    }
    private void addSong(){
        String name = txtName.getText().toString().trim();
        String artist = txtArtist.getText().toString().trim();
        String category = sp_category.getSelectedItem().toString();
        String album = sp_album.getSelectedItem().toString();
        String link = "";
        if(!TextUtils.isEmpty(name)){
            String id = databaseSongs.push().getKey();
            Song song = new Song(id, name, artist, category, album, link);
            databaseSongs.child(id).setValue(song);
            Toast.makeText(this, "Add success !!!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "You should enter a name song", Toast.LENGTH_LONG).show();
        }
    }
}