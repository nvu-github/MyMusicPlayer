package com.huawei.mymusicplayer.dialog;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.model.FavoriteSong;

import java.util.concurrent.ThreadLocalRandom;

public class CustomDialogAddSong extends Dialog {
    public static final String TAG = "DialogAddSong";
    public static final String PROFILE_INFORMATION = "profile";
    public Context context;
    private EditText ed_nameSong, ed_urlSong;
    private Button btn_dialog_add, btn_dialog_cancel;
    private String userID = "";

    DatabaseReference databaseSongs;

    public CustomDialogAddSong(Context context){
        super(context);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_song);

        this.ed_nameSong = (EditText)findViewById(R.id.ed_nameSong);
        this.ed_urlSong = (EditText)findViewById(R.id.ed_urlSong);

        this.btn_dialog_add = findViewById(R.id.btn_dialog_add_song);
        this.btn_dialog_cancel = findViewById(R.id.btn_dialog_cancel);


        this.btn_dialog_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadSong();
            }
        });

        this.btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelProcess();
            }
        });
    }
    private void uploadSong(){
        SharedPreferences prefs = getContext().getSharedPreferences(PROFILE_INFORMATION, MODE_PRIVATE);
        userID = prefs.getString("union_id","union_id");
        String nameSong  = this.ed_nameSong.getText().toString();
        String urlSong = this.ed_urlSong.getText().toString();
        if (userID != "") {
            databaseSongs = FirebaseDatabase.getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("favorite_song");
            int randomNum = ThreadLocalRandom.current().nextInt(1, 99999 + 1);
            String id = String.valueOf(randomNum);
            FavoriteSong favoriteSong = new FavoriteSong(id, nameSong, urlSong, userID);
            databaseSongs.child(id).setValue(favoriteSong);
        }
    }
    // User click "Cancel" button.
    private void cancelProcess()  {
        this.dismiss();
    }
}


