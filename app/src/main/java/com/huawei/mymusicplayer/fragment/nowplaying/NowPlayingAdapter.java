package com.huawei.mymusicplayer.fragment.nowplaying;

import static android.content.Context.MODE_PRIVATE;
import static com.huawei.mymusicplayer.MyApplication.getContext;
import static com.huawei.mymusicplayer.account.AccountActivity.PROFILE_INFORMATION;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.mymusicplayer.Playlist;
import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.fragment.layoutfragment.loveSong.LoveSong;
import com.huawei.mymusicplayer.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class NowPlayingAdapter extends BaseSimpleAdapter<HwAudioPlayItem> {

    private static class ViewHolder {

        private TextView mTrackName = null;

        private TextView mArtistName = null;

        private ImageView love_song;

        private View lineImage = null;
    }

    private int listItemMargin;

    //    private static class DeleteViewClickListener implements View.OnClickListener {
//        int pos;
//
//        DeleteViewClickListener(int pos) {
//            this.pos = pos;
//        }
//
//        @Override
//        public void onClick(View v) {
//            PlayHelper.getInstance().deleteItem(pos);
//        }
//    }

    NowPlayingAdapter(List<HwAudioPlayItem> netSongInfos, Activity activity) {
        super(activity);
        listItemMargin = activity.getResources().getDimensionPixelSize(R.dimen.layout_margin_left_and_right);
        if (netSongInfos != null) {
            mDataSource.addAll(netSongInfos);
        }
    }

    @Override
    public View getView(int pos, View contentView, ViewGroup arg2) {
        contentView = getContentView(contentView);

        HwAudioPlayItem showBean = mDataSource.get(pos);
        if (null == showBean) {
            return contentView;
        }

        initItemView(showBean, contentView, pos);

        return contentView;
    }

    private View getContentView(View contentView) {
        ViewHolder viewholder;
        if (contentView == null) {
            viewholder = new ViewHolder();
            contentView = mInflater.inflate(R.layout.simple_song_list_item, null);
            viewholder.mTrackName = ViewUtils.findViewById(contentView, R.id.line1);
            viewholder.mArtistName = ViewUtils.findViewById(contentView, R.id.line2);
            viewholder.lineImage = ViewUtils.findViewById(contentView, R.id.simple_line);
            viewholder.love_song = ViewUtils.findViewById(contentView, R.id.love_song);

            contentView.setTag(viewholder);
        }
        return contentView;
    }

    private void initItemView(HwAudioPlayItem songBean, View contentView, final int pos) {
        ViewHolder viewholder = (ViewHolder) contentView.getTag();
        contentView.setPadding(listItemMargin, 0, listItemMargin, 0);
        viewholder.mTrackName.setText(songBean.getAudioTitle());
        viewholder.mArtistName.setText(songBean.getSinger());
//        viewholder.mDeleteView.setOnClickListener(new DeleteViewClickListener(pos));


        SharedPreferences prefs = getContext().getSharedPreferences(PROFILE_INFORMATION, MODE_PRIVATE);
        String userID = prefs.getString("union_id", "0");
        if (userID != "0") {
            viewholder.love_song.setVisibility(View.VISIBLE);
            viewholder.love_song.setOnClickListener(new OptionViewClick(pos, songBean));

//            database = FirebaseDatabase.getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("LoveSong");
//            database.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Log.i("test_snapshot", snapshot.toString());
//                    List<LoveSong> songList = new ArrayList<>();
//                    for (DataSnapshot songSnapshot : snapshot.getChildren()){
//                        LoveSong lovesong = songSnapshot.getValue(LoveSong.class);
//                        if(lovesong.getSongID().equals(songBean.getAudioId())){
//                            songList.add(lovesong);
//                        }
//                        if(lovesong.getSongID().equals(songBean.getAudioId())){
//                            Log.i("TAG", "onDataChange1");
//                            viewholder.love_song.setImageResource(R.drawable.ic_baseline_favorite_24);
//                        }else{
//                            Log.i("TAG", "onDataChange2");
//                            viewholder.love_song.setImageResource(R.drawable.ic_baseline_favorite_border_24);
//                        }
//                    }
//                    Log.i("TAG", "onDataChange: " + songList.size());
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
        }



        ViewUtils.setVisibility(viewholder.lineImage, (getCount() - 1) != pos);
    }


    private static class OptionViewClick implements View.OnClickListener {
        int pos;
        HwAudioPlayItem songBean;
        private DatabaseReference database_song;
        SharedPreferences prefs = getContext().getSharedPreferences(PROFILE_INFORMATION, MODE_PRIVATE);
        String userID = prefs.getString("union_id", "union_id");

        OptionViewClick(int pos, HwAudioPlayItem songBean) {
            this.pos = pos;
            this.songBean = songBean;
        }

        @Override
        public void onClick(View v) {
            database_song = FirebaseDatabase.getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("LoveSong");
            String key = database_song.push().getKey();
            LoveSong loveSong = new LoveSong(songBean.getSinger(), songBean.getAudioTitle(), key, songBean.getAudioId(), userID);
            database_song.child(key).setValue(loveSong);
        }
    }
}
