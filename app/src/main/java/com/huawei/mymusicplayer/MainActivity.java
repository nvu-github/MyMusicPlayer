package com.huawei.mymusicplayer;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.hms.audiokit.player.manager.HwAudioStatusListener;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.mymusicplayer.Database.Database;
import com.huawei.mymusicplayer.fragment.PlayHelper;
import com.huawei.mymusicplayer.fragment.nowplaying.NowPlayingFragment;
import com.huawei.mymusicplayer.fragment.playbutton.PlayControlButtonFragment;
import com.huawei.mymusicplayer.home.ItemHome;
import com.huawei.mymusicplayer.home.ItemSongHome;
import com.huawei.mymusicplayer.ui.seek.SeekBarFragment;
import com.huawei.mymusicplayer.utils.ViewUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

//import com.huawei.mymusicplayer.utils.PlayModeUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

//    private ImageView mPlayModeView;

    private PlayControlButtonFragment mPlayControlButtonFragment;

    private SeekBarFragment mSeekBarFragment;

    private NowPlayingFragment mNowPlayingFragment;

    private TextView mSongName;

    private CircleImageView mCircleImageView;

    private Database database;

    DatabaseReference databaseSongs;

//    private TextView mSingerName;

    private AccountAuthService mAuthManager;


    private HwAudioStatusListener mPlayListener = new HwAudioStatusListener() {
        @Override
        public void onSongChange(HwAudioPlayItem song) {
            updateSongName(song);
            if (mNowPlayingFragment != null) {
                mNowPlayingFragment.updatePlayingPos();
                mNowPlayingFragment.initIfEmpty();
            }
        }

        @Override
        public void onQueueChanged(List<HwAudioPlayItem> infos) {
            if (mNowPlayingFragment != null) {
                mNowPlayingFragment.initNowPlayingList();
            }
        }

        @Override
        public void onBufferProgress(int percent) {

        }

        @Override
        public void onPlayProgress(long currPos, long duration) {

        }

        @Override
        public void onPlayCompleted(boolean isStopped) {

        }

        @Override
        public void onPlayError(int errorCode, boolean isUserForcePlay) {
            Toast.makeText(MainActivity.this, R.string.can_not_play, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "errorCode: " + errorCode + "isUserForcePlay: " + isUserForcePlay);
        }

        @Override
        public void onPlayStateChange(boolean isPlaying, boolean isBuffering) {
            Log.i(TAG, "onPlayStateChange isPlaying:" + isPlaying + ", isBuffering:" + isBuffering);
            if (mPlayControlButtonFragment != null) {
                mPlayControlButtonFragment.setPauseButtonImage();
            }
            if (mSeekBarFragment != null) {
                mSeekBarFragment.refresh();
            }
            if (mNowPlayingFragment != null) {
                mNowPlayingFragment.updatePlayingPos();
            }
//            PlayModeUtils.getInstance().updatePlayMode(MainActivity.this, mPlayModeView);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
//        PlayModeUtils.getInstance().updatePlayMode(this, mPlayModeView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startanimation();
        databaseSongs = FirebaseDatabase.getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("songs");

        Bundle bundle = getIntent().getExtras();

        if (bundle == null){
            return;
        }else if(bundle.getString("album") != null){
            String album    = bundle.getString("album").toLowerCase();
            Log.i(TAG, "onCreate: album"+album);
            List<Song> songList = new ArrayList<>();
            databaseSongs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    songList.clear();
                    for (DataSnapshot songSnapshot : snapshot.getChildren()){
                        Song song = songSnapshot.getValue(Song.class);
                        Log.i(TAG, "onDataChange: song album"+song.getAlbum().toLowerCase());
                        if(album.equals(song.getAlbum().toLowerCase())){
                            songList.add(song);
                        }
                    }
                    PlayHelper.getInstance().buildOnlineList(songList);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            List<Song> songList = new ArrayList<>();
            databaseSongs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    songList.clear();
                    for (DataSnapshot songSnapshot : snapshot.getChildren()){
                        Song song = songSnapshot.getValue(Song.class);
                        songList.add(song);
                    }
                    PlayHelper.getInstance().buildOnlineList(songList);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        PlayHelper.getInstance().addListener(mPlayListener);
        initViews();
    }

    private void initViews() {
        mSongName = ViewUtils.findViewById(this, R.id.songName);
        mCircleImageView = ViewUtils.findViewById(this,R.id.img_rotation);

        mSeekBarFragment = SeekBarFragment.newInstance();
        addFragment(R.id.pro_layout, mSeekBarFragment);
        mPlayControlButtonFragment = PlayControlButtonFragment.newInstance();
        addFragment(R.id.playback_play_layout, mPlayControlButtonFragment);
        mNowPlayingFragment = NowPlayingFragment.newInstance(true);
        addFragment(R.id.playlist_layout, mNowPlayingFragment);

        ImageView mSettingMenu = ViewUtils.findViewById(this, R.id.setting_content_layout);
        mSettingMenu.setOnClickListener(this);

//        mPlayModeView = ViewUtils.findViewById(this, R.id.playmode_imagebutton);
//        mPlayModeView.setOnClickListener(this);
    }

    public void startanimation(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mCircleImageView.animate().rotationBy(360).withEndAction(this).setDuration(5000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        mCircleImageView.animate().rotationBy(360).withEndAction(runnable).setDuration(5000)
                .setInterpolator(new LinearInterpolator()).start();
    }

    public void stopanimation(){
        mCircleImageView.animate().cancel();
    }

    private void addFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(id, fragment).commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PlayHelper.getInstance().removeListener(mPlayListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.playmode_imagebutton:
//                PlayModeUtils.getInstance().changePlayMode(this, mPlayModeView);
//                break;
            case R.id.setting_content_layout:
                addAlldata();
//                showMenuDialog();
                break;
            default:
                break;
        }
    }

    private void addAlldata(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("list_singer");

        ItemSongHome itemSongHome = new ItemSongHome(0,"Daydream","1000","https://firebasestorage.googleapis.com/v0/b/mymusicplayer-6fff9.appspot.com/o/singer%2Fsoobin1.mp3?alt=media&token=c51f4cf4-4c9e-461a-8a89-ffcb6cb3151a","soobin");
        reference.child(String.valueOf(0)).setValue(itemSongHome, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                Toast.makeText(MainActivity.this, "add success", Toast.LENGTH_SHORT).show();
            }
        });
//        List<ItemSongHome> list = new ArrayList<>();
//        list.add(new ItemSongHome(0,"Daydream","1000","https://firebasestorage.googleapis.com/v0/b/mymusicplayer-6fff9.appspot.com/o/singer%2Fsoobin1.mp3?alt=media&token=c51f4cf4-4c9e-461a-8a89-ffcb6cb3151a","Soobin Hoàng Sơn"));
//        list.add(new ItemSongHome(1,"Đẹp nhất là em","1002","https://firebasestorage.googleapis.com/v0/b/mymusicplayer-6fff9.appspot.com/o/singer%2Fsoobin2.mp3?alt=media&token=0a16c795-9925-4a19-87d2-b602f1ed6b18","Soobin Hoàng Sơn"));
//        list.add(new ItemSongHome(2,"Đi để trở về","1003","https://firebasestorage.googleapis.com/v0/b/mymusicplayer-6fff9.appspot.com/o/singer%2Fsoobin3.mp3?alt=media&token=21f34184-e606-4a4f-929c-69c86591ab2c","Soobin Hoàng Sơn"));
//        list.add(new ItemSongHome(3,"Vài lần đón đưa","1004","https://firebasestorage.googleapis.com/v0/b/mymusicplayer-6fff9.appspot.com/o/singer%2Fsoobin4.mp3?alt=media&token=d7c2239a-d019-4d68-9f14-c279b51360f0","Soobin Hoàng Sơn"));
//        list.add(new ItemSongHome(4,"Vinh quang đang chờ ta","1005","https://firebasestorage.googleapis.com/v0/b/mymusicplayer-6fff9.appspot.com/o/singer%2Fsoobin5.mp3?alt=media&token=57375cce-9ac2-4ae7-a7d8-0f86d3e98cf4","Soobin Hoàng Sơn"));
//
//        Log.i(TAG, "addAlldata: " + list.size());
//        reference.setValue(list, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
//                Toast.makeText(MainActivity.this, "add successfull", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void updateSongName(HwAudioPlayItem playItem) {
        if (playItem == null) {
            return;
        }
        if (mSongName != null) {
            mSongName.setText(playItem.getAudioTitle());
        }
    }

//    private void showMenuDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setItems(R.array.menu_items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Log.i(TAG, "onDialogClick,which: " + which);
//                switch (which) {
////                    case 0:
////                        Log.i(TAG, "dialog click online_list");
////                        PlayHelper.getInstance().buildOnlineList();
////                        break;
//                    case 0:
//                        Log.i(TAG, "dialog click local_list");
//                        PlayHelper.getInstance().buildLocal(MainActivity.this);
//                        break;
////                    case 2:
////                        Log.i(TAG, "dialog click log_out");
////                        signOut();
//                    default:
//                        break;
//                }
//
//            }
//        });
//        builder.create().show();
//    }

    private List<ItemSongHome> databaseProcessing(int key){
//        create database
        database = new Database(this);
//        create table
        database.QueryData("CREATE TABLE IF NOT EXISTS CaSi(Id INTEGER PRIMARY KEY, AudioTitle VARCHAR(255), AudioId VARCHAR(255), FilePath VARCHAR(255), Singer VARCHAR(255), TypeCaSi INTEGER) ");
//        insert data
//        database.QueryData("INSERT INTO CaSi VALUES(1,'Trên tình bạn dưới tình yêu','upfriendshipdownlove','hms_res://upfriendshipdownlove','Min',1)");
//        database.QueryData("INSERT INTO CaSi VALUES(2,'Đi đu đưa đi','diduduadi','hms_res://diduduadi','Bích Phương',2)");
        Cursor getData = database.GetData("SELECT * FROM CaSi WHERE TypeCaSi = " + key);

        List<ItemSongHome> itemSongHomes = new ArrayList<>();
//        while (getData.moveToNext()){
//            String title        = getData.getString(1);
//            String audioId      = getData.getString(2);
//            String url          = getData.getString(3);
//            String singername   = getData.getString(4);
//            itemSongHomes.add(new ItemSongHome(title,audioId,url,singername));
////            Toast.makeText(this, "Name: "+title + ", Url: "+url + ", singername: "+singername , Toast.LENGTH_LONG).show();
//        }
        return itemSongHomes;
    }
}
