package com.huawei.mymusicplayer.fragment.nowplaying;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.fragment.PlayHelper;
import com.huawei.mymusicplayer.utils.ViewUtils;

import java.util.List;

public class NowPlayingFragment extends Fragment implements AbsListView.OnScrollListener {
    private static final String TAG = "NowPlayingFragment";

    private Activity mActivity = null;

    private View mContextView = null;

    private ListView mListView;

    private int mVisiblePos;

    private int mVisibleCount;

    private List<HwAudioPlayItem> mSongs = null;

    private NowPlayingAdapter mOnlineAdapter;

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            showResult();
        }
    };

    public static NowPlayingFragment newInstance(boolean serviceInit) {
        NowPlayingFragment fragment = new NowPlayingFragment();
        Bundle args = new Bundle();
        args.putBoolean("serviceInit", serviceInit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContextView = inflater.inflate(R.layout.nowplaying_content, container, false);
        initListView();
        return mContextView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initNowPlayingList();
    }

    private void initListView() {
        mListView = ViewUtils.findViewById(mContextView, R.id.nowplaying_view);
        mListView.setCacheColorHint(0);
        if (mOnlineAdapter == null) {
            mOnlineAdapter = new NowPlayingAdapter(mSongs, mActivity);
        }

        mListView.setAdapter(mOnlineAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlayHelper.getInstance().playAt(position - mListView.getHeaderViewsCount()); // chuyen bai khi click vao list view
            }
        });
        mListView.setOnScrollListener(this);
    }

    public void initNowPlayingList() {
        Log.i(TAG, "initNowPlayingList");
        if (mListView != null) {
            mSongs = PlayHelper.getInstance().getAllPlaylist();
            mHandler.sendMessage(mHandler.obtainMessage());
        }
    }

    public void initIfEmpty() {
        if (mSongs == null || mSongs.isEmpty()) {
            initNowPlayingList();
        }
    }

    private boolean hasSongs() {
        return mSongs != null && mSongs.size() > 0;
    }

    public void updatePlayingPos() {
        if (mListView != null) {
            int pos = PlayHelper.getInstance().getCurrentIndex();
            mListView.invalidateViews();
            if (pos > mVisiblePos && pos < mVisiblePos + mVisibleCount) {
                return;
            }
            mListView.setSelection(pos);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mVisiblePos = firstVisibleItem;
        mVisibleCount = visibleItemCount;
    }

    private void showResult() {
        if (hasSongs()) {
            mListView.setVisibility(View.VISIBLE);
            mContextView.findViewById(R.id.nowplaying_no_songs).setVisibility(View.GONE);
            mOnlineAdapter.setDataSource(mSongs);
        } else {
            mListView.setVisibility(View.GONE);
            mContextView.findViewById(R.id.nowplaying_no_songs).setVisibility(View.VISIBLE);
        }
        updatePlayingPos();
    }
}