package com.huawei.mymusicplayer.fragment.layoutfragment.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.mymusicplayer.MainActivity;
import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.home.ItemHome;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private List<item_search> mListSearch;
    private Context mContext;

    public SearchAdapter(Context context, List<item_search> mListSearch) {
        this.mListSearch = mListSearch;
        this.mContext = context;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_searchview, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        item_search item = mListSearch.get(position);
        if (item == null){
            return;
        }

        holder.mImgSearch.setImageResource(item.getId_imgSearch());
        holder.txt_songSearch.setText(item.getNamebaihat_search());
        holder.txt_casiSearch.setText(item.getNamecasi_search());
        holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeintent(item);
            }
        });
    }

    private void changeintent(item_search item_search){
        Intent intent = new Intent(mContext, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("idbaihat",item_search.getIdbaihat());
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mListSearch != null){
            return mListSearch.size();
        }
        return 0;
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView mImgSearch;
        private TextView txt_songSearch;
        private TextView txt_casiSearch;
        private RelativeLayout mItemLayout;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            mImgSearch = itemView.findViewById(R.id.imgCasiSearch);
            txt_songSearch = itemView.findViewById(R.id.txt_baihatSearch);
            txt_casiSearch = itemView.findViewById(R.id.txt_casiSearch);
            mItemLayout = itemView.findViewById(R.id.idItemsearch);
        }
    }
}
