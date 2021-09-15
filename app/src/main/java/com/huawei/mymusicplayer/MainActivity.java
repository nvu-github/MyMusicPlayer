package com.huawei.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.hms.audiokit.player.manager.HwAudioStatusListener;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.mymusicplayer.fragment.PlayHelper;
import com.huawei.mymusicplayer.fragment.nowplaying.NowPlayingFragment;
import com.huawei.mymusicplayer.fragment.playbutton.PlayControlButtonFragment;
import com.huawei.mymusicplayer.ui.seek.SeekBarFragment;
//import com.huawei.mymusicplayer.utils.PlayModeUtils;
import com.huawei.mymusicplayer.utils.ViewUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

//    private ImageView mPlayModeView;

    private PlayControlButtonFragment mPlayControlButtonFragment;

    private SeekBarFragment mSeekBarFragment;

    private NowPlayingFragment mNowPlayingFragment;

    private TextView mSongName;

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
        PlayHelper.getInstance().addListener(mPlayListener);
        initViews();
        PlayHelper.getInstance().buildLocal(MainActivity.this);
    }

    private void initViews() {
        mSongName = ViewUtils.findViewById(this, R.id.songName);
//        mSingerName = ViewUtils.findViewById(this, R.id.singerName);
        mSeekBarFragment = SeekBarFragment.newInstance();
        addFragment(R.id.pro_layout, mSeekBarFragment);
        mPlayControlButtonFragment = PlayControlButtonFragment.newInstance();
        addFragment(R.id.playback_play_layout, mPlayControlButtonFragment);
        mNowPlayingFragment = NowPlayingFragment.newInstance(true);
        addFragment(R.id.playlist_layout, mNowPlayingFragment);

//        ImageView mVolumeSilent = ViewUtils.findViewById(this, R.id.volume_silent);
//        mVolumeSilent.setOnClickListener(this);
//        SeekBar mVolumeSeekBar = ViewUtils.findViewById(this, R.id.volume_seekbar);
//        ViewUtils.setVisibility(mVolumeSeekBar, false);
//        ViewUtils.setVisibility(mVolumeSilent, false);
//        ImageView mSettingMenu = ViewUtils.findViewById(this, R.id.setting_content_layout);
//        mSettingMenu.setOnClickListener(this);

//        mPlayModeView = ViewUtils.findViewById(this, R.id.playmode_imagebutton);
//        mPlayModeView.setOnClickListener(this);
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
//        switch (v.getId()) {
//            case R.id.playmode_imagebutton:
////                PlayModeUtils.getInstance().changePlayMode(this, mPlayModeView);
//                break;
////            case R.id.setting_content_layout:
////                showMenuDialog();
////                break;
//            default:
//                break;
//        }
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

}
