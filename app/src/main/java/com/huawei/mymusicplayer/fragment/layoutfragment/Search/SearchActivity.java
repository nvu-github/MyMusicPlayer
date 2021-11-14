package com.huawei.mymusicplayer.fragment.layoutfragment.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.huawei.mymusicplayer.R;
import com.huawei.mymusicplayer.Song;
import com.huawei.mymusicplayer.fragment.PlayHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ImageView mImgback;
    private SearchView mSearch;
    private RecyclerView mRecyclerview;
    private SearchAdapter mSearchAdapter;
    List<item_search> mList;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mImgback = (ImageView) findViewById(R.id.backActivities);
        mSearch = (SearchView) findViewById(R.id.searchInp);
        mRecyclerview = (RecyclerView) findViewById(R.id.listSearch);
        ref = FirebaseDatabase
                .getInstance("https://mymusicplayer-5e719-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("songs");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(linearLayoutManager);



        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        mRecyclerview.addItemDecoration(itemDecoration);

        mImgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ref != null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    songList.clear();
                    mList = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Song song = ds.getValue(Song.class);
                        mList.add(new item_search(song.getId(),R.drawable.app_icon1,song.getName(),song.getArtist()));
                    }
                    mSearchAdapter = new SearchAdapter(SearchActivity.this,mList);
                    mRecyclerview.setAdapter(mSearchAdapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        if (mSearch != null) {
            mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void search(String textSearch){
        List<item_search> item = new ArrayList<>();
        for (item_search ds : mList){
            if (ds.getNamebaihat_search().toLowerCase().contains(textSearch.toLowerCase())){
                item.add(new item_search(ds.getIdbaihat(),R.drawable.app_icon1, ds.getNamebaihat_search(), ds.getNamecasi_search()));
            }
        }
        mSearchAdapter = new SearchAdapter(SearchActivity.this,item);
        mRecyclerview.setAdapter(mSearchAdapter);
    }
}