package com.flyer.chat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.adapter.NearAdapter;
import com.flyer.chat.base.BaseFragment;
import com.flyer.chat.bean.User;
import com.flyer.chat.network.CallBack;
import com.flyer.chat.network.RetrofitService;
import com.flyer.chat.util.HttpParseUtil;
import com.flyer.chat.util.SharedPreferencesHelper;

import java.util.List;

/**
 * Created by mike.li on 2018/7/9.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    public static final String MAP_MODE = "map_mode";
    public static final String LIST_MODE = "list_mode";
    private TextView mToolbarLeft;
    private TextView mToolbarMiddle;
    private TextView mToolbarRight;
    private RelativeLayout mToolbar;
    private RecyclerView mRecyclerView;
    private FrameLayout mHomeMap;
    private NearAdapter adapter;
    private List<User> data;
    private MapFragment mapFragment;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initAdapter();
        initData();
        switchMode();
    }

    private void initData() {
        RetrofitService.getInstance().requestGet("allUser", new CallBack<String>() {
            @Override
            public void onResponse(String response) {
                data = HttpParseUtil.parseArray(response, "data", User.class);
                switchMode();
            }
        });
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NearAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);

        mapFragment = MapFragment.newInstance();
        getChildFragmentManager().beginTransaction().add(R.id.home_map,mapFragment).commit();
    }
    private void switchMode(){
        if(MAP_MODE.equals(SharedPreferencesHelper.getInstance().getHomeMode())){
            mToolbarLeft.setText("列表");
            mHomeMap.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mapFragment.setNewData(data);
        }else {
            mToolbarLeft.setText("地图");
            mHomeMap.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            adapter.setNewData(data);
        }
    }

    private void initView(View rootView) {
        this.mToolbarLeft = rootView.findViewById(R.id.toolbar_left);
        this.mToolbarMiddle = rootView.findViewById(R.id.toolbar_middle);
        this.mToolbarRight = rootView.findViewById(R.id.toolbar_right);
        this.mToolbar = rootView.findViewById(R.id.toolbar);
        this.mRecyclerView = rootView.findViewById(R.id.recycler_view);
        this.mHomeMap = rootView.findViewById(R.id.home_map);
        mToolbarMiddle.setText("附近的人");
        mToolbarLeft.setOnClickListener(this);
        mToolbarRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_left:
                if(MAP_MODE.equals(SharedPreferencesHelper.getInstance().getHomeMode())){
                    SharedPreferencesHelper.getInstance().putHomeMode(LIST_MODE);
                }else {
                    SharedPreferencesHelper.getInstance().putHomeMode(MAP_MODE);
                }
                switchMode();
                break;

            case R.id.toolbar_right:

                break;
        }
    }
}
