package com.huawei.mymusicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.hms.audiokit.player.manager.HwAudioStatusListener;
import com.huawei.mymusicplayer.fragment.PlayHelper;
import com.huawei.mymusicplayer.fragment.nowplaying.NowPlayingFragment;
import com.huawei.mymusicplayer.fragment.playbutton.PlayControlButtonFragment;
import com.huawei.mymusicplayer.ui.seek.SeekBarFragment;
import com.huawei.mymusicplayer.utils.PlayModeUtils;
import com.huawei.mymusicplayer.utils.ViewUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "CategoryActivity";

    private ImageView mPlayModeView;

    private PlayControlButtonFragment mPlayControlButtonFragment;

    private SeekBarFragment mSeekBarFragment;

    private NowPlayingFragment mNowPlayingFragment;

    private TextView mSongName;

    private TextView mSingerName;

    private LinearLayout mSongImage;

    private HwAudioStatusListener mPlayListener = new HwAudioStatusListener() {
        @Override
        public void onSongChange(HwAudioPlayItem song) {
            updateSongName(song);
            if (mNowPlayingFragment != null) {
                mNowPlayingFragment.updatePlayingPos();
            }
            if (mNowPlayingFragment != null) {
                mNowPlayingFragment.initIfEmpty();
            }
        }

        @Override
        public void onQueueChanged(List<HwAudioPlayItem> infos) {
            Log.i(TAG, "onQueueChanged");
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
            Toast.makeText(CategoryActivity.this, R.string.can_not_play, Toast.LENGTH_SHORT).show();
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
          PlayModeUtils.getInstance().updatePlayMode(CategoryActivity.this, mPlayModeView);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        // huawei
        PlayHelper.getInstance().addListener(mPlayListener);
        Bundle extras  = getIntent().getExtras();
        if (extras  != null) {
            String category = extras .getString("category");
            initViews(category);
            // get file json xử lý json ở đây do function loadJSONFromAsset dưới chỉ nhận ở Activity k nhận ở class
            ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
            try {
                JSONObject obj = new JSONObject(loadJSONFromAsset(CategoryActivity.this));
                JSONArray m_jArry = obj.getJSONArray(category);
                HashMap<String, String> m_li;
                for (int i = 0; i < m_jArry.length(); i++) {
                    JSONObject jo_inside = m_jArry.getJSONObject(i);
                    Log.d("Details-->", jo_inside.getString("name"));
                    String id_value = jo_inside.getString("id");
                    String name_value = jo_inside.getString("name");
                    String artist_value = jo_inside.getString("artist");
                    String link_value = jo_inside.getString("link");
                    String avatar_value = jo_inside.getString("avatar");
                    //Add your values in your `ArrayList` as below:
                    m_li = new HashMap<String, String>();
                    m_li.put("id", id_value);
                    m_li.put("name", name_value);
                    m_li.put("artist", artist_value);
                    m_li.put("link", link_value);
                    m_li.put("avatar", avatar_value);
                    songsList.add(m_li);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            int lengthMyList = songsList.size();
            Log.i(TAG, "my length list: "+lengthMyList);
            PlayHelper.getInstance().builtCategory(CategoryActivity.this, songsList);

//            switch (category){
//                case "":
//                    Log.i(TAG,"category none");
//                    PlayHelper.getInstance().buildLocal(CategoryActivity.this);
//                break;
//                default:
//                    Log.i(TAG,"category default");
//                    PlayHelper.getInstance().builtCategory(CategoryActivity.this, songsList);
//                    break;
//            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
       PlayModeUtils.getInstance().updatePlayMode(this, mPlayModeView);
    }
    private void initViews(String codeCategory) {
        mSongName = ViewUtils.findViewById(this, R.id.songName);
        mSingerName = ViewUtils.findViewById(this, R.id.singerName);
        mSongImage = ViewUtils.findViewById(this, R.id.songImage);
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

        ImageView mSettingMenu = ViewUtils.findViewById(this, R.id.setting_content_layout);
        mSettingMenu.setOnClickListener(this);
        mPlayModeView = ViewUtils.findViewById(this, R.id.playmode_imagebutton);
        mPlayModeView.setOnClickListener(this);
            ImageView imageView = (ImageView) findViewById(R.id.img_rotation);
            switch (codeCategory){
                case "kpop":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.list_top100_kpop));
                break;
                case "vpop":
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.list_top100_nhactre));
                    break;
                default:
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.playbg));
                    break;
            }

    }

    private void addFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(id, fragment).commitAllowingStateLoss();
    }

    private void updateSongName(HwAudioPlayItem playItem) {
        if (playItem == null) {
            return;
        }
       if (mSongName != null) {
            mSongName.setText(playItem.getAudioTitle());
        }
        if (mSingerName != null) {
            mSingerName.setText(playItem.getSinger());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playmode_imagebutton:
                PlayModeUtils.getInstance().changePlayMode(this, mPlayModeView);
                break;

            default:
                break;
        }
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("musics.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

}