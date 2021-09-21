package com.huawei.mymusicplayer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public abstract class BaseUIFragment extends Fragment {

    protected View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getContentViewLayout(), container, false);
        initViews(view);
        return view;
    }
    protected abstract int getContentViewLayout();

    protected abstract void initViews(View view);
}