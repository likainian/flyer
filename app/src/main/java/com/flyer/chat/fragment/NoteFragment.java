package com.flyer.chat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseFragment;

/**
 * Created by mike.li on 2018/7/9.
 */

public class NoteFragment extends BaseFragment {
    public static NoteFragment newInstance() {
        return new NoteFragment();
    }
    public ImageView mToolbarLeft;
    public TextView mToolbarMiddle;
    public TextView mToolbarRight;
    public RelativeLayout mToolbar;
    public RecyclerView mRecyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initAdapter();
    }
    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initView(View rootView) {
        this.mToolbarLeft = rootView.findViewById(R.id.toolbar_left);
        this.mToolbarMiddle = rootView.findViewById(R.id.toolbar_middle);
        this.mToolbarRight = rootView.findViewById(R.id.toolbar_right);
        this.mToolbar = rootView.findViewById(R.id.toolbar);
        this.mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mToolbarMiddle.setText("热帖");
    }
}
