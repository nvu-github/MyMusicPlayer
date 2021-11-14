package com.huawei.mymusicplayer.fragment.layoutfragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.home.BannerAdapter;
import com.huawei.mymusicplayer.home.HomeCategoryAdapter;
import com.huawei.mymusicplayer.home.ItemBanner;
import com.huawei.mymusicplayer.home.ItemCategory;
import com.huawei.mymusicplayer.home.ItemHome;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {

    private ViewPager mViewBanner;
    private CircleIndicator mCircleIndicator;
    private BannerAdapter mBannerAdapter;
    private RecyclerView rvc_home;
    private HomeCategoryAdapter mHomCategory;
    private List<ItemBanner> mListItemBanner;
    private Timer mTimer;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rvc_home = view.findViewById(R.id.rcv_home);

        mViewBanner = view.findViewById(R.id.view_banner);
        mCircleIndicator = view.findViewById(R.id.circle_banner);

        mHomCategory = new HomeCategoryAdapter(getActivity());

        mListItemBanner = getListBanner();
        mBannerAdapter = new BannerAdapter(getActivity(),mListItemBanner);
        mViewBanner.setAdapter(mBannerAdapter);

        mCircleIndicator.setViewPager(mViewBanner);
        mBannerAdapter.registerDataSetObserver(mCircleIndicator.getDataSetObserver());
        autoSlideImage();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        rvc_home.setLayoutManager(linearLayoutManager);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        rvc_home.addItemDecoration(itemDecoration);

        mHomCategory.setData(getListCategory());
        rvc_home.setAdapter(mHomCategory);

        return view;
    }

    private List<ItemCategory> getListCategory(){
        List<ItemCategory> list = new ArrayList<>();

        List<ItemHome> homes = new ArrayList<>();
        homes.add(new ItemHome("id12",R.drawable.moiphathanh1,"Độ tộc 2 - Độ mixi",1,true));
        homes.add(new ItemHome("id13",R.drawable.moiphathanh2,"Cứ nói yêu lần này - Lil Z",1,true));
        homes.add(new ItemHome("id14",R.drawable.moiphathanh3,"Yêu là cưới - Phát hồ",1,true));
        homes.add(new ItemHome("id15",R.drawable.moiphathanh4,"Sài gòn - Hannie",1,true));
        homes.add(new ItemHome("id16",R.drawable.moiphathanh6,"Lựa chọn thích hợp - VAT",1,true));
        list.add(new ItemCategory("Mới phát hành",homes));

        List<ItemHome> homes1 = new ArrayList<>();
        homes1.add(new ItemHome("1",R.drawable.sobin,"SOOBIN",2,false));
        homes1.add(new ItemHome("2",R.drawable.sontung,"MTP",2,false));
        homes1.add(new ItemHome("3",R.drawable.mrsiro,"Mr.Siro",2,false));
        homes1.add(new ItemHome("4",R.drawable.j97,"Jack",2,false));
        list.add(new ItemCategory("Ca sĩ",homes1));

        List<ItemHome> homes2 = new ArrayList<>();
        homes2.add(new ItemHome("1",R.drawable.nhacv,"Nhạc Việt",3,true));
        homes2.add(new ItemHome("2",R.drawable.nhacviet,"Âu Mỹ",3,true));
        homes2.add(new ItemHome("3",R.drawable.nhachan,"Nhạc Hàn",3,true));
        homes2.add(new ItemHome("4",R.drawable.nhachoa,"Nhạc Hoa",3,true));
        list.add(new ItemCategory("Thể loại",homes2));

        return list;
    }

    private List<ItemBanner> getListBanner(){
        List<ItemBanner> itemBanners = new ArrayList<>();
        itemBanners.add(new ItemBanner(R.drawable.banner2,1));
        itemBanners.add(new ItemBanner(R.drawable.banner1,2));
        itemBanners.add(new ItemBanner(R.drawable.banner5,3));
        itemBanners.add(new ItemBanner(R.drawable.banner0,4));
        return  itemBanners;
    }

    private void autoSlideImage(){
        if (mListItemBanner == null || mListItemBanner.isEmpty() || mViewBanner == null){
            return;
        }

//        init timer
        if (mTimer == null){
            mTimer = new Timer();
        }

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem  = mViewBanner.getCurrentItem();
                        int totalItem = mListItemBanner.size() - 1;
                        if (currentItem < totalItem){
                            currentItem++;
                            mViewBanner.setCurrentItem(currentItem);
                        } else {
                            mViewBanner.setCurrentItem(0);
                        }
                    }
                });
            }
        },500,5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }
}