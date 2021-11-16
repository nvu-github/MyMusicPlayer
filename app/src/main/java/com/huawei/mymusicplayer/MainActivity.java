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
import com.huawei.mymusicplayer.fragment.PlayHelper;
import com.huawei.mymusicplayer.fragment.nowplaying.NowPlayingFragment;
import com.huawei.mymusicplayer.fragment.playbutton.PlayControlButtonFragment;
import com.huawei.mymusicplayer.home.ItemHome;
import com.huawei.mymusicplayer.home.ItemSongHome;
import com.huawei.mymusicplayer.fragment.seek.SeekBarFragment;
import com.huawei.mymusicplayer.model.FavoriteSong;
import com.huawei.mymusicplayer.model.Song;
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
    private ImageView mImgback;
    DatabaseReference databaseSongs, databaseFavoriteSong;


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
            Toast.makeText(MainActivity.this, "can_not_play", Toast.LENGTH_SHORT).show();
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
        databaseSongs = FirebaseDatabase
                .getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("songs");

        Bundle bundle   = getIntent().getExtras();
        String album    = "";
        String idsearch = "";
        int type     = 0;
        String idHome   = "";
        String status = "";
        if (bundle == null){
            return;
        }else if(bundle.getString("album") != null){
            album       = bundle.getString("album").toLowerCase();
            getdataFirebase(album, idsearch, type, idHome);
        } else if (bundle.getString("idbaihat") != null) {
            idsearch    = bundle.getString("idbaihat").toLowerCase();
            getdataFirebase(album, idsearch, type, idHome);
        } else if(bundle.getString("songID")!= null && bundle.getString("status").equals("favorite_song")){
            Log.i(TAG, "onCreate: if");

            getFavoriteSong();
        }else {
            Log.i(TAG, "onCreate: else");
            ItemHome item = (ItemHome) bundle.getSerializable("typeHome");
            type          = item.getType();
            if (type == 2){
                idHome = item.getTitle().toLowerCase();
            } else {
                idHome        = item.getKey();
            }

            getdataFirebase(album, idsearch, type, idHome);
        }
        Log.i(TAG, "onCreate: always");
        PlayHelper.getInstance().addListener(mPlayListener);
        initViews();
    }

    private  void getFavoriteSong(){
        Log.i(TAG, "onCreate: getfavortite song");

        Bundle bundle   = getIntent().getExtras();
        String songID = bundle.getString("songID");
        List<Song> favoriteSongs = new ArrayList<>();
        databaseFavoriteSong = FirebaseDatabase
                .getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("favorite_song");
        databaseFavoriteSong.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteSongs.clear();
                for (DataSnapshot songSnapshot : snapshot.getChildren()){
                    FavoriteSong favoriteSong = songSnapshot.getValue(FavoriteSong.class);
                    Song song = new Song(favoriteSong.getId(), favoriteSong.getName(), "demo", "demo", "demo", favoriteSong.getUrl());
                    Log.i(TAG, "song model: "+song.getLink());
                   favoriteSongs.add(song);
                }
                PlayHelper.getInstance().buildOnlineList(favoriteSongs);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getdataFirebase(String album, String id, int type, String idHome){
        List<Song> songList = new ArrayList<>();
        databaseSongs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                songList.clear();
                for (DataSnapshot songSnapshot : snapshot.getChildren()){
                    Song song = songSnapshot.getValue(Song.class);
                    if(album != null && album.equals(song.getAlbum().toLowerCase())){
                        songList.add(song);
                    } else if (id != null && id.equals(song.getId().toLowerCase())) {
                        songList.add(song);
                    } else if ( id == "" && album == "") {
                        if (type == 1 && idHome.equals(song.getId().toLowerCase())){
                            songList.add(song);
                        } else if (type == 2 && idHome.equals(song.getArtist().toLowerCase())) {
                            songList.add(song);
                        } else if (type == 3 && idHome.equals(song.getCategory().toLowerCase())){
                            songList.add(song);
                        }
                    }
                }
                PlayHelper.getInstance().buildOnlineList(songList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initViews() {
        mSongName = ViewUtils.findViewById(this, R.id.songName);
        mCircleImageView = ViewUtils.findViewById(this,R.id.img_rotation);
        mImgback = ViewUtils.findViewById(this,R.id.backActivities);

        mSeekBarFragment = SeekBarFragment.newInstance();
        addFragment(R.id.pro_layout, mSeekBarFragment);
        mPlayControlButtonFragment = PlayControlButtonFragment.newInstance();
        addFragment(R.id.playback_play_layout, mPlayControlButtonFragment);
        mNowPlayingFragment = NowPlayingFragment.newInstance(true);
        addFragment(R.id.playlist_layout, mNowPlayingFragment);

        ImageView mSettingMenu = ViewUtils.findViewById(this, R.id.setting_content_layout);
        mSettingMenu.setOnClickListener(this);
        mImgback.setOnClickListener(this);

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
            case R.id.backActivities :
                finish();
                break;
//            case R.id.playmode_imagebutton:
//                PlayModeUtils.getInstance().changePlayMode(this, mPlayModeView);
//                break;
//            case R.id.setting_content_layout:
//                addAlldata();
//                showMenuDialog();
//                break;
            default:
                break;
        }
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

//    private List<ItemSongHome> databaseProcessing(int key){
////        create database
//        database = new Database(this);
////        create table
//        database.QueryData("CREATE TABLE IF NOT EXISTS CaSi(Id INTEGER PRIMARY KEY, AudioTitle VARCHAR(255), AudioId VARCHAR(255), FilePath VARCHAR(255), Singer VARCHAR(255), TypeCaSi INTEGER) ");
////        insert data
////        database.QueryData("INSERT INTO CaSi VALUES(1,'Trên tình bạn dưới tình yêu','upfriendshipdownlove','hms_res://upfriendshipdownlove','Min',1)");
////        database.QueryData("INSERT INTO CaSi VALUES(2,'Đi đu đưa đi','diduduadi','hms_res://diduduadi','Bích Phương',2)");
//        Cursor getData = database.GetData("SELECT * FROM CaSi WHERE TypeCaSi = " + key);
//
//        List<ItemSongHome> itemSongHomes = new ArrayList<>();
////        while (getData.moveToNext()){
////            String title        = getData.getString(1);
////            String audioId      = getData.getString(2);
////            String url          = getData.getString(3);
////            String singername   = getData.getString(4);
////            itemSongHomes.add(new ItemSongHome(title,audioId,url,singername));
//////            Toast.makeText(this, "Name: "+title + ", Url: "+url + ", singername: "+singername , Toast.LENGTH_LONG).show();
////        }
//        return itemSongHomes;
//    }
}
