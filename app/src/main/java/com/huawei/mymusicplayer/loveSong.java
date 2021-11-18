package com.huawei.mymusicplayer;

import static com.huawei.mymusicplayer.account.AccountActivity.PROFILE_INFORMATION;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.mymusicplayer.fragment.layoutfragment.CustomAdapter;
import com.huawei.mymusicplayer.fragment.layoutfragment.loveSong.LoveSong;
import com.huawei.mymusicplayer.fragment.layoutfragment.loveSong.LoveSongAdapter;

import java.util.ArrayList;

public class loveSong extends AppCompatActivity {
    ImageView backActivity;
    RecyclerView rcv_love_song;
    ArrayList<LoveSong> arrLoveSong;
    LoveSongAdapter loveSongAdapter;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_song);

        backActivity = findViewById(R.id.backActivity);
        backActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rcv_love_song = findViewById(R.id.list_love_song);
        rcv_love_song.setLayoutManager(new LinearLayoutManager(this));
        arrLoveSong = new ArrayList<>();
        loveSongAdapter = new LoveSongAdapter(arrLoveSong, this, new LoveSongAdapter.IClickListener() {
            @Override
            public void onClickDeleteItem(LoveSong loveSong) {
                onClickDeleteData(loveSong);
            }
        });
        rcv_love_song.setAdapter(loveSongAdapter);

        SharedPreferences prefs = this.getSharedPreferences(PROFILE_INFORMATION, MODE_PRIVATE);
        String account_id = prefs.getString("union_id","union_id");
        showdata(account_id);
    }
    public void showdata(String account_id)
    {
        database = FirebaseDatabase.getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("LoveSong");
        database.orderByChild("userID").equalTo(account_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LoveSong loveSong = snapshot.getValue(LoveSong.class);
                if(loveSong != null)
                {
                    arrLoveSong.add(loveSong);
                    loveSongAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                LoveSong loveSong = snapshot.getValue(LoveSong.class);
                if(loveSong == null || arrLoveSong == null || arrLoveSong.isEmpty()){
                    return;
                }
                for(int i = 0; i<arrLoveSong.size(); i++)
                {
                    if(loveSong.getId() == arrLoveSong.get(i).getId())
                    {
                        arrLoveSong.remove(arrLoveSong.get(i));
                        break;
                    }
                }
                loveSongAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void onClickDeleteData(LoveSong loveSong){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc muốn xoá bài hát này không?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.child(String.valueOf(loveSong.getId())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(com.huawei.mymusicplayer.loveSong.this, "Xóa bài hát khỏi danh sách yêu thích thành công", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

}