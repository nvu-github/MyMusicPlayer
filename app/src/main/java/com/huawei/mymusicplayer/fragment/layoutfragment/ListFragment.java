package com.huawei.mymusicplayer.fragment.layoutfragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.list.ItemList;
import com.huawei.mymusicplayer.list.ListCategory;
import com.huawei.mymusicplayer.list.ListCategoryAdapter;
import com.huawei.mymusicplayer.home.ItemCategory;
import com.huawei.mymusicplayer.home.ItemHome;

import java.util.ArrayList;
import java.util.List;

// class cũ
//public class ListFragment extends BaseUIFragment {
//    private String TAG = "ListFragment";
//    protected View view;
//    private List<HwAudioPlayItem> mSongs = null;
//    public static ListFragment newInstance() {
//        return new ListFragment();
//    }
//    @Override
//    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Log.i(TAG, "Activity created !");
//    }
//    @Override
//    protected int getContentViewLayout() {
//        return R.layout.fragment_list;
//    }
//    @Override
//    protected void initViews(View view) {
//        CardView kPopCategory = ViewUtils.findViewById(view, R.id.kPopCategory);
//        CardView vPopCategory = ViewUtils.findViewById(view, R.id.vPopCategory);
//        vPopCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "Pop Category started");
//                Intent intent = new Intent(v.getContext(), CategoryActivity.class);
//                intent.putExtra("category", "vpop");
//                startActivity(intent);
//            }
//        });
//        kPopCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "Pop Category started");
//                Intent intent = new Intent(v.getContext(), CategoryActivity.class);
//                intent.putExtra("category", "kpop");
//                startActivity(intent);
//            }
//        });
//    }
//}
public class ListFragment extends Fragment {
    private RecyclerView rvc_list;
    private ListCategoryAdapter mListCategory;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        rvc_list = view.findViewById(R.id.rvc_list);
        mListCategory = new ListCategoryAdapter(getActivity());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rvc_list.setLayoutManager(linearLayoutManager);
        mListCategory.setData(getListCategory());
        rvc_list.setAdapter(mListCategory);
        return view;
    }


    private List<ListCategory> getListCategory(){
        List<ListCategory> list = new ArrayList<>();

        List<ItemList> lists = new ArrayList<>();
        lists.add(new ItemList(1,"",R.drawable.list_top100_rapviet,"TOP 100 nhạc Rap Việt",1,true));
        lists.add(new ItemList(2,"kpop",R.drawable.list_top100_kpop,"TOP 100 nhạc Hàn",1,true));
        lists.add(new ItemList(3,"",R.drawable.list_top100_edm,"TOP 100 nhạc EDM",1,true));
        lists.add(new ItemList(4,"usuk",R.drawable.list_top100_usuk,"TOP 100 nhạc POP Âu Mỹ",1,true));
        lists.add(new ItemList(5,"vpop",R.drawable.list_top100_nhactre,"TOP 100 nhạc trẻ",1,true));
        lists.add(new ItemList(7,"",R.drawable.showall,"",1,true));
        list.add(new ListCategory("Hôm nay nghe gì",lists));

        List<ItemList> lists1 = new ArrayList<>();
        lists1.add(new ItemList(1,"",R.drawable.list_category_focus,"Tập chung",2,true));
        lists1.add(new ItemList(2,"",R.drawable.list_category_cafe,"Cafe",2,true));
        lists1.add(new ItemList(3,"",R.drawable.list_category_motivation,"Motivation",2,true));
        lists1.add(new ItemList(4,"",R.drawable.list_category_gaming,"Gaming",2,true));
        list.add(new ListCategory("Tâm trạng và hoạt động",lists1));

        List<ItemList> lists2 = new ArrayList<>();
        lists2.add(new ItemList(1,"",R.drawable.list_daily_chill,"Playlist Nhạc này chill phết",3,true));
        lists2.add(new ItemList(2,"",R.drawable.list_daily_edm,"Everyday EDM",3,true));
        lists2.add(new ItemList(3,"",R.drawable.list_daily_indie,"Indie Việt",3,true));
        lists2.add(new ItemList(5,"",R.drawable.list_daily_cbiz,"Nhạc phim hoa ngữ mới nhất",3,true));
        list.add(new ListCategory("Nhạc hay nghe ngay",lists2));

        return list;
    }
}

