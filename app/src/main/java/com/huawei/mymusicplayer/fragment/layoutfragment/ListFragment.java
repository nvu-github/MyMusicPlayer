package com.huawei.mymusicplayer.fragment.layoutfragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.category.Category;
import com.huawei.mymusicplayer.category.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    private RecyclerView rcv_category;
    private CategoryAdapter mCategoryAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        rcv_category = view.findViewById(R.id.rcv_category);
        mCategoryAdapter = new CategoryAdapter(getActivity());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rcv_category.setLayoutManager(gridLayoutManager);
        mCategoryAdapter.setData(getContext(),getListCategory());
        rcv_category.setAdapter(mCategoryAdapter);
        return view;
    }

    private List<Category> getListCategory(){
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category(1, R.drawable.list_top100_kpop, "Top 100 Kpop", "Kpop", "Top 100 Kpop"));
        categoryList.add(new Category(2, R.drawable.list_top100_nhactre, "Top 100 Nhạc trẻ", "Vpop", "Top 100 Nhạc trẻ"));

        categoryList.add(new Category(3, R.drawable.list_top100_edm, "Top 100 EDM", "", "Top 100 EDM"));
        categoryList.add(new Category(4, R.drawable.list_top100_rapviet, "Top 100 Rap", "", "Top 100 Rap"));

        categoryList.add(new Category(5, R.drawable.list_top100_usuk, "Top 100 US UK", "Us_Uk", "Top 100 US UK"));
        categoryList.add(new Category(6, R.drawable.list_daily_cbiz, "Top 100 Cbiz", "Cpop", "Top 100 Cbiz"));

        return categoryList;
    }
}

