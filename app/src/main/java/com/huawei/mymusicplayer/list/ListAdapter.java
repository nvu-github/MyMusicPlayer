package com.huawei.mymusicplayer.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.mymusicplayer.CategoryActivity;
import com.huawei.mymusicplayer.MainActivity;
import com.huawei.mymusicplayer.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ItemList> mList;
    private Context mContext;

    private static int TYPE_HOME1_FEATURED = 1;
    private static int TYPE_HOME2_FEATURED = 2;

    public void setData(Context context,List<ItemList> homes){
        this.mList = homes;
        this.mContext = context;

//        load data to adapter
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_HOME1_FEATURED == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home,parent,false);
            return new HomeViewHolder(view);
        } else if (TYPE_HOME2_FEATURED == viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home1,parent,false);
            return new HomeViewHolder1(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemList itemList = mList.get(position);
        if (itemList == null){
            return;
        }

        if (TYPE_HOME1_FEATURED == holder.getItemViewType()){
            HomeViewHolder homeViewHolder = (HomeViewHolder) holder;
            homeViewHolder.imgCasi.setImageResource(itemList.getResoucerid());
            homeViewHolder.txtCasi.setText(itemList.getTitle());
            homeViewHolder.mCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemList.getType() == 1){
                        changeintent(itemList, itemList.getCodeCategory());
                        Toast.makeText(mContext, "Hôm nay nghe gì " + itemList.getKey(), Toast.LENGTH_SHORT).show();

                    } else if (itemList.getType() == 3) {
                        changeintent(itemList, itemList.getCodeCategory());
                        Toast.makeText(mContext, "Nhạc hay nghe ngay " + itemList.getKey(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (TYPE_HOME2_FEATURED == holder.getItemViewType()){
            HomeViewHolder1 homeViewHolder1 = (HomeViewHolder1) holder;
            homeViewHolder1.imgCasi1.setImageResource(itemList.getResoucerid());
            homeViewHolder1.txtCasi1.setText(itemList.getTitle());
            homeViewHolder1.mCard1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeintent(itemList, itemList.getCodeCategory());
                    Toast.makeText(mContext, "Trạng thái và hoạt động" + itemList.getKey(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void changeintent(ItemList itemList, String codeCategory){
        Intent intent = new Intent(mContext, CategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object",itemList);
        intent.putExtras(bundle);
        intent.putExtra("category", codeCategory);
        mContext.startActivity(intent);
    }

    public void refresh(){
        mContext = null;
    }

    @Override
    public int getItemCount() {
        if (mList != null){
            return mList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        ItemList itemList = mList.get(position);
        if (itemList.isFeatured()){
            return TYPE_HOME1_FEATURED;
        } else {
            return TYPE_HOME2_FEATURED;
        }
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder{

        private CardView mCard;
        private ImageView imgCasi;
        private TextView txtCasi;

        public HomeViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
//            Ánh xạ
            imgCasi = itemView.findViewById(R.id.image_casi);
            txtCasi = itemView.findViewById(R.id.title_casi);
            mCard = itemView.findViewById(R.id.card_id);
        }
    }

    public class HomeViewHolder1 extends RecyclerView.ViewHolder{

        private CardView mCard1;
        private ImageView imgCasi1;
        private TextView txtCasi1;

        public HomeViewHolder1(@NonNull @NotNull View itemView) {
            super(itemView);
//            Ánh xạ
            imgCasi1 = itemView.findViewById(R.id.image_casi1);
            txtCasi1 = itemView.findViewById(R.id.title_casi1);
            mCard1 = itemView.findViewById(R.id.card_id1);
        }
    }
}
