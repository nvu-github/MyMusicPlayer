package com.huawei.mymusicplayer.fragment.layoutfragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.mymusicplayer.CategoryActivity;
import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.ui.BaseUIFragment;
import com.huawei.mymusicplayer.utils.ViewUtils;

import java.util.List;

public class ListFragment extends BaseUIFragment {
    private String TAG = "ListFragment";
    protected View view;
    private List<HwAudioPlayItem> mSongs = null;
    public static ListFragment newInstance() {
        return new ListFragment();
    }
    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "Activity created !");
    }
    @Override
    protected int getContentViewLayout() {
        return R.layout.fragment_list;
    }
    @Override
    protected void initViews(View view) {
        CardView kPopCategory = ViewUtils.findViewById(view, R.id.kPopCategory);
        CardView vPopCategory = ViewUtils.findViewById(view, R.id.vPopCategory);
        vPopCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Pop Category started");
                Intent intent = new Intent(v.getContext(), CategoryActivity.class);
                intent.putExtra("category", "vpop");
                startActivity(intent);
            }
        });
        kPopCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Pop Category started");
                Intent intent = new Intent(v.getContext(), CategoryActivity.class);
                intent.putExtra("category", "kpop");
                startActivity(intent);
            }
        });
    }
}