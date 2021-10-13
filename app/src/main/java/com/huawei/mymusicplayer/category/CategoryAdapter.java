package com.huawei.mymusicplayer.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.hms.api.bean.HwAudioPlayItem;
import com.huawei.mymusicplayer.MainActivity;
import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.Song;
import com.huawei.mymusicplayer.home.ItemHome;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder>{
    private Context mContext;
    List<Category> mCategory;

    public CategoryAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(Context context, List<Category> mCategory) {
        this.mContext = context;
        this.mCategory = mCategory;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_category, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        final Category category = mCategory.get(position);
        if(category == null){
            return ;
        }
        holder.imv_category.setImageResource(category.getResourceImg());
        holder.tv_category.setText(category.getName());
        List<Song> songList = new ArrayList<>();
        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getCategory = category.getCategory();
                String getAlbum = category.getAlbum();

                changeIntent(getCategory, getAlbum);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mCategory!= null){
            return  mCategory.size();
        }
        return 0;
    }

    public class CategoryHolder extends RecyclerView.ViewHolder{
        private ImageView imv_category;
        private TextView tv_category;
        private CardView mCard;
        public CategoryHolder(@NonNull View itemView) {
            super(itemView);

            imv_category = itemView.findViewById(R.id.imv_category);
            tv_category = itemView.findViewById(R.id.tv_category);
            mCard = itemView.findViewById(R.id.card_category);
        }
    }

    private void changeIntent(String category, String album){
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("catgory", category);
        intent.putExtra("album", album);
        mContext.startActivity(intent);
    }
}
