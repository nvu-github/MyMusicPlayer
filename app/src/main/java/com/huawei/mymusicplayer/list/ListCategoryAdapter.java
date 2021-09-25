package com.huawei.mymusicplayer.list;

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

public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.ListCategoryViewHolder> {

    private Context mContext;
    private List<ListCategory> mListCategory;

    public ListCategoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<ListCategory> itemCategories){
        this.mListCategory = itemCategories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new ListCategoryAdapter.ListCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListCategoryAdapter.ListCategoryViewHolder holder, int position) {
        ListCategory listCategory = mListCategory.get(position);
        if (listCategory == null){
            return;
        }
        holder.txtCategory.setText(listCategory.getName_category());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        holder.rvCategory.setLayoutManager(linearLayoutManager);

        ListAdapter listAdapter = new ListAdapter();
        listAdapter.setData(mContext,listCategory.getItemHomes());
        holder.rvCategory.setAdapter(listAdapter);
    }

    @Override
    public int getItemCount() {
        if (mListCategory != null){
            return mListCategory.size();
        }
        return 0;
    }

    public class ListCategoryViewHolder extends RecyclerView.ViewHolder{

        private TextView txtCategory;
        private RecyclerView rvCategory;
        public ListCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCategory = itemView.findViewById(R.id.txt_category);
            rvCategory = itemView.findViewById(R.id.rev_category);
        }
    }
}
