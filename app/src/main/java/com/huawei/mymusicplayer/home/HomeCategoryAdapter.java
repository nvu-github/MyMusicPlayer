package com.huawei.mymusicplayer.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.mymusicplayer.R;

import java.util.List;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.HomeCategoryViewHolder> {

    private Context mContext;
    private List<ItemCategory> mListCategory;

    public HomeCategoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<ItemCategory> itemCategories){
        this.mListCategory = itemCategories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCategoryAdapter.HomeCategoryViewHolder holder, int position) {
        ItemCategory itemCategory = mListCategory.get(position);
        if (itemCategory == null){
            return;
        }
        holder.txtCategory.setText(itemCategory.getName_category());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        holder.rvCategory.setLayoutManager(linearLayoutManager);

        HomeAdapter homeAdapter = new HomeAdapter();
        homeAdapter.setData(mContext,itemCategory.getItemHomes());
        holder.rvCategory.setAdapter(homeAdapter);
    }

    @Override
    public int getItemCount() {
        if (mListCategory != null){
            return mListCategory.size();
        }
        return 0;
    }

    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder{

        private TextView txtCategory;
        private RecyclerView rvCategory;
        public HomeCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCategory = itemView.findViewById(R.id.txt_category);
            rvCategory = itemView.findViewById(R.id.rev_category);
        }
    }

}
