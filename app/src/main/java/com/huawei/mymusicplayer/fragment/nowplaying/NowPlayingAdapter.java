package com.huawei.mymusicplayer.fragment.nowplaying;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;

import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.fragment.PlayHelper;
import com.huawei.mymusicplayer.utils.ViewUtils;

import java.util.List;

public class NowPlayingAdapter extends BaseSimpleAdapter<HwAudioPlayItem> {

    private static class ViewHolder {

        private TextView mTrackName = null;

        private TextView mArtistName = null;


        private ImageView menu_song;

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
            viewholder.menu_song = ViewUtils.findViewById(contentView, R.id.menu_song);
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

//        viewholder.menu_song.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ShowMenu();
//            }
//        });

        ViewUtils.setVisibility(viewholder.lineImage, (getCount() - 1) != pos);
    }
//    private void ShowMenu(){
//        PopupMenu popupMenu = new PopupMenu();
//        popupMenu.inflate(R.menu.menu);
//        popupMenu.setOnMenuItemClickListener(this);
//        popupMenu.show();
//    }
//    @Override
//    public void onMenuItemClick(MenuItem item)
//    {
//        switch (item.getItemId()){
//            case R.id.addToPlaylist:
//                Log.i("them playlits", "them");
//                break;
//            case R.id.delete_song:
//                Log.i("xoa playlits", "xoa");
//                break;
//            default:
//                break;
//        }
//    }
}
