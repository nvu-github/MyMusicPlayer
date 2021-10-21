package com.huawei.mymusicplayer.fragment.layoutfragment;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.mymusicplayer.AddSongActivity;
import com.huawei.mymusicplayer.MainActivity;
import com.huawei.mymusicplayer.Playlist;
import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.Song;
import com.huawei.mymusicplayer.SongList;

import java.util.ArrayList;

public class AccountFragment extends Fragment {
    TextView add_playlist;
    ImageView mySong;
    RecyclerView listPlaylist;
    ArrayList<Playlist> arrPlaylist;
    CustomAdapter myAdapter;
    DatabaseReference database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        add_playlist = view.findViewById(R.id.add_playlist);
        add_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(Gravity.CENTER);
            }

        });
        mySong = view.findViewById(R.id.mySong);
        mySong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("playlist", 1);
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Playlist");

        listPlaylist = view.findViewById(R.id.listPlaylist);
        listPlaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
        arrPlaylist = new ArrayList<>();
        myAdapter = new CustomAdapter(arrPlaylist);

        showdata();



        return view;

    }

    public void showdata() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()){
                    Playlist pl = Snapshot.getValue(Playlist.class);
                    arrPlaylist.add(pl);
                }
                listPlaylist.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void openDialog(int gravity)
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add);

        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowattributes = window.getAttributes();
        windowattributes.gravity = gravity;
        window.setAttributes(windowattributes);

        if(Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        }else{
            dialog.setCancelable(false);
        }

        EditText editText = dialog.findViewById(R.id.playlist_name);
        Button button = dialog.findViewById(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_add:
                        String name = editText.getText().toString().trim();
                        if(!TextUtils.isEmpty(name)){
                            String key = database.push().getKey();
                            Playlist playlist = new Playlist(key,name);
                            database.child(key).setValue(playlist);
                            Toast.makeText(getActivity(), "Thêm playlist thành công", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(getActivity(), "Thêm playlist thất bại", Toast.LENGTH_LONG).show();
                        }
                }
            }
        });
        dialog.show();
    }
}