package com.huawei.mymusicplayer.fragment.playbutton;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.fragment.PlayHelper;
import com.huawei.mymusicplayer.utils.ViewUtils;

//import static com.huawei.mymusicplayer.AccountActivity.TAG;

public class PlayControlButtonFragment extends Fragment implements View.OnClickListener {

    private ImageView mPlay;

    private View rootView;

    public static PlayControlButtonFragment newInstance() {
        return new PlayControlButtonFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = R.layout.playcontorlbutton_fragment_layout;
        rootView = inflater.inflate(layoutId, null);
        PlayHelper.getInstance().pause();
        initView();
        setPauseButtonImage();
        return rootView;
    }

    private void initView() {
        ImageView mPre = ViewUtils.findViewById(rootView, R.id.pre_imagebutton);
        mPre.setOnClickListener(this);
        ImageView mNext = ViewUtils.findViewById(rootView, R.id.next_iamgebutton);
        mNext.setOnClickListener(this);
        mPlay = ViewUtils.findViewById(rootView, R.id.play_imagebutton);
        RelativeLayout mPlayLayout = ViewUtils.findViewById(rootView, R.id.play_button_layout);
        mPlayLayout.setOnClickListener(this);
        ImageView mPlayBackground = ViewUtils.findViewById(rootView, R.id.play_background);
        mPlayBackground.setAlpha(0.4f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre_imagebutton:
                PlayHelper.getInstance().prev();
                break;
            case R.id.next_iamgebutton:
                PlayHelper.getInstance().next();
                break;
            case R.id.play_button_layout:
                if (PlayHelper.getInstance().isPlaying()) {
                    PlayHelper.getInstance().pause();
                } else {
                    PlayHelper.getInstance().play();
                }
                break;
            default:
                break;
        }
    }

    public void setPauseButtonImage() {
        if (null == mPlay) {
//            Log.i(TAG, "setPauseButtonImage err");
            return;
        }
        boolean isPlaying = PlayHelper.getInstance().isPlaying();
        ViewUtils.setVisibility(mPlay, true);
        if (isPlaying) {
            mPlay.setImageResource(R.drawable.btn_playback_pause_normal);
        } else {
            mPlay.setImageResource(R.drawable.btn_playback_play_normal);
        }
    }
}

