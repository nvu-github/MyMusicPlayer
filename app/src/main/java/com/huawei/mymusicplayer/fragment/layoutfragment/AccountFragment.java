package com.huawei.mymusicplayer.fragment.layoutfragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;

import com.huawei.mymusicplayer.MainActivity;
import com.huawei.mymusicplayer.Playlist;
import com.huawei.mymusicplayer.R;

import java.util.ArrayList;

public class AccountFragment extends Fragment {
    TextView add_playlist, username;
    ImageView account_avatar, mySong;
    ListView listPlaylist;
    ArrayList<Playlist> arrPlaylist;
    CustomAdapter myAdapter;
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
        listPlaylist = view.findViewById(R.id.listplaylist);
        arrPlaylist = new ArrayList<Playlist>();
        myAdapter = new CustomAdapter(getActivity(), R.layout.playlist_item, arrPlaylist);
        listPlaylist.setAdapter(myAdapter);

       mySong = view.findViewById(R.id.mySong);
       mySong.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), MainActivity.class);
               startActivity(intent);
           }
       });

       username = view.findViewById(R.id.username);

        return view;

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
                        String ten = editText.getText().toString();
                        Playlist temp = new Playlist(ten, R.drawable.app_icon1);
                        arrPlaylist.add(temp);
                        myAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Thêm playlist thành công", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        dialog.show();
    }


}