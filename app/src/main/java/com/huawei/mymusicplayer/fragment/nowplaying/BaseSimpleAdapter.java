package com.huawei.mymusicplayer.fragment.nowplaying;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseSimpleAdapter<E> extends BaseAdapter {
    List<E> mDataSource = new ArrayList<>();

    LayoutInflater mInflater;

    BaseSimpleAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return null == mDataSource ? 0 : mDataSource.size();
    }

    @Override
    public E getItem(int position) {
        if (position < 0 || position >= mDataSource.size()) {
            return null;
        }
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    void setDataSource(List<E> dataSource) {
        mDataSource.clear();
        if (dataSource != null) {
            mDataSource.addAll(dataSource);
        }
    }
}