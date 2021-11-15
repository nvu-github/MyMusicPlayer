package com.huawei.mymusicplayer.fragment.layoutfragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huawei.hms.hwid.I;
import com.huawei.mymusicplayer.AddSongActivity;
import com.huawei.mymusicplayer.MainActivity;
import com.huawei.mymusicplayer.Playlist;
import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.account.AccountActivity;
import com.huawei.mymusicplayer.dialog.CustomDialog;

import java.io.InputStream;
import java.util.ArrayList;

public class AccountFragment extends Fragment{
    private static final String TAG = "AccountFragment";
    LinearLayout mySongs, favoriteSongs, signInPlease;
    TextView add_playlist, profile, tv_addSong, tv_signIn_out;
    ImageView mySong, profile_avatar, imv_signIn_out;
    RecyclerView listPlaylist;
    ArrayList<Playlist> arrPlaylist;
    CustomAdapter myAdapter;
    DatabaseReference database;
    public static final String PROFILE_INFORMATION = "profile";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        // get id
        profile = view.findViewById(R.id.profile);
        profile_avatar = view.findViewById(R.id.profile_avatar);
        add_playlist = view.findViewById(R.id.add_playlist);
        tv_addSong = view.findViewById(R.id.tv_addSong);
        tv_signIn_out = view.findViewById(R.id.tv_signIn_out);
        imv_signIn_out = view.findViewById(R.id.imv_signIn_out);
        mySongs = view.findViewById(R.id.mySongs);
        favoriteSongs = view.findViewById(R.id.favoriteSongs);
        signInPlease = view.findViewById(R.id.signInPlease);
        // create click event
        add_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(Gravity.CENTER);
            }
        });
        tv_addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddSongActivity = new Intent(getActivity(), AddSongActivity.class);
                startActivity(toAddSongActivity);
            }
        });
        //
        String account_id = "";
        // check logged ?
        Boolean hasAccount =  hasAccount();
        if(!hasAccount){
            // module sẽ bị ẩn đi khi chưa đăng nhập
            mySongs.setVisibility(View.GONE);
            favoriteSongs.setVisibility(View.GONE);
            signInPlease.setVisibility(View.VISIBLE);
            tv_signIn_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent toAccountActivity = new Intent(getActivity(), AccountActivity.class);
                    startActivity(toAccountActivity);
                }
            });
        }else{
            SharedPreferences prefs = getContext().getSharedPreferences(PROFILE_INFORMATION, MODE_PRIVATE);
            String display_name = prefs.getString("display_name", "profile");//"No name defined" is the default value.
            String avatar = prefs.getString("avatar","avatar"); //0 is the default value.
            account_id = prefs.getString("union_id","union_id");
            // change text
            profile.setText(display_name);
            tv_signIn_out.setText("Đăng xuất");
            imv_signIn_out.setImageResource(R.drawable.sign_out);
            new DownloadImageTask(view.findViewById(R.id.profile_avatar))
                    .execute(avatar);
            // check logout
            tv_signIn_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomDialog customDialog = new CustomDialog(getContext());
                    customDialog.show();
                }
            });

        }
        listPlaylist = view.findViewById(R.id.listPlaylist);
        listPlaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
        arrPlaylist = new ArrayList<>();
        myAdapter = new CustomAdapter(getActivity(), arrPlaylist, new CustomAdapter.IClickListener() {
            @Override
            public void onClickDeleteItem(Playlist playlist) {
                onClickDeleteData(playlist);
            }
        });
        showdata(account_id);
        listPlaylist.setAdapter(myAdapter);
        return view;
    }
    private Boolean hasAccount(){
        SharedPreferences sharedPrefs = getContext().getSharedPreferences(PROFILE_INFORMATION, MODE_PRIVATE);
        if(sharedPrefs.contains("union_id")){
            return true;
        }else{
            return false;
        }
    }
    public void showdata(String account_id)
    {
        database = FirebaseDatabase.getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Playlist");
        database.orderByChild("account_id").equalTo(account_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Playlist pl = snapshot.getValue(Playlist.class);
                if(pl != null)
                {
                    arrPlaylist.add(pl);
                    myAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Playlist pl = snapshot.getValue(Playlist.class);
                if(pl == null || arrPlaylist == null || arrPlaylist.isEmpty()){
                    return;
                }
                for(int i = 0; i<arrPlaylist.size(); i++)
                {
                    if(pl.getKey() == arrPlaylist.get(i).getKey())
                    {
                        arrPlaylist.remove(arrPlaylist.get(i));
                        break;
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onClickDeleteData(Playlist playlist){
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc muốn xoá playlist này không?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.child(String.valueOf(playlist.getKey())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(getActivity(), "Delete playlist success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
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


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> { // class lấy link avatar từ profile huawei convert to bitmap và hiển thị trên imageview
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
