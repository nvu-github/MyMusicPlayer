package com.huawei.mymusicplayer.home;

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

import com.huawei.mymusicplayer.MainActivity;
import com.huawei.mymusicplayer.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ItemHome> mHome;
    private Context mContext;

    private static int TYPE_HOME1_FEATURED = 1;
    private static int TYPE_HOME2_FEATURED = 2;

    public void setData(Context context,List<ItemHome> homes){
        this.mHome = homes;
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
        final ItemHome itemHome = mHome.get(position);
        if (itemHome == null){
            return;
        }

        if (TYPE_HOME1_FEATURED == holder.getItemViewType()){
            HomeViewHolder homeViewHolder = (HomeViewHolder) holder;
            homeViewHolder.imgCasi.setImageResource(itemHome.getResoucerid());
            homeViewHolder.txtCasi.setText(itemHome.getTitle());
            homeViewHolder.mCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemHome.getType() == 1){
                        changeintent(itemHome);
                    } else if (itemHome.getType() == 3) {
                        Toast.makeText(mContext, "type2 " + itemHome.getType(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else if (TYPE_HOME2_FEATURED == holder.getItemViewType()){
            HomeViewHolder1 homeViewHolder1 = (HomeViewHolder1) holder;
            homeViewHolder1.imgCasi1.setImageResource(itemHome.getResoucerid());
            homeViewHolder1.txtCasi1.setText(itemHome.getTitle());
            homeViewHolder1.mCard1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeintent(itemHome);
                }
            });
        }
    }

    private void changeintent(ItemHome itemHome){
        Intent intent = new Intent(mContext, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object",itemHome);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    public void refresh(){
        mContext = null;
    }

    @Override
    public int getItemCount() {
        if (mHome != null){
            return mHome.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        ItemHome itemHome = mHome.get(position);
        if (itemHome.isFeatured()){
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
