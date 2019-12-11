package com.flyer.chat.fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyer.chat.R;
import com.flyer.chat.activity.common.ScanActivity;
import com.flyer.chat.adapter.NearAdapter;
import com.flyer.chat.base.BaseFragment;
import com.flyer.chat.activity.account.bean.User;
import com.flyer.chat.network.CallBack;
import com.flyer.chat.network.RetrofitService;
import com.flyer.chat.bean.LinkUser;
import com.flyer.chat.bean.MapUser;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.HttpParseUtil;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.SharedPreferencesUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

/**
 * Created by mike.li on 2018/7/9.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener, HomeContract.HomeView {
    public static final String MAP_MODE = "map_mode";
    public static final String LIST_MODE = "list_mode";
    private TextView mToolbarLeft;
    private RecyclerView mRecyclerView;
    private FrameLayout mHomeMap;
    private NearAdapter adapter;
    private MapFragment mapFragment;
    private HomePresenter mPresenter;

    private String mapId;
    private List<MapUser> mapUsers;
    private double latitude;
    private double longitude;

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
        mPresenter = new HomePresenter(this);
        initView(view);
        initAdapter();
        switchMode();
        mPresenter.getUserInfo();
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NearAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);

        mapFragment = MapFragment.newInstance();
        mapFragment.setOnLocationChangeListener(new MapFragment.OnLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                if(CheckUtil.isEmpty(mapId))return;
                mPresenter.updateMapUser(mapId,latitude,longitude);
                mPresenter.searchMapUser(latitude,longitude);
            }
        });
        getChildFragmentManager().beginTransaction().add(R.id.home_map,mapFragment).commit();
    }

    private void switchMode(){
        if(MAP_MODE.equals(SharedPreferencesUtil.getInstance().getHomeMode())){
            mToolbarLeft.setText("列表");
            mHomeMap.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mapFragment.setNewData(mapUsers);
        }else {
            mToolbarLeft.setText("地图");
            mHomeMap.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            adapter.setNewData(mapUsers);
        }
    }

    private void initView(View rootView) {
        this.mToolbarLeft = rootView.findViewById(R.id.toolbar_left);
        FrameLayout mToolbarRight = rootView.findViewById(R.id.toolbar_right);
        this.mRecyclerView = rootView.findViewById(R.id.recycler_view);
        this.mHomeMap = rootView.findViewById(R.id.home_map);
        mToolbarLeft.setOnClickListener(this);
        mToolbarRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_left:
                if(MAP_MODE.equals(SharedPreferencesUtil.getInstance().getHomeMode())){
                    SharedPreferencesUtil.getInstance().setHomeMode(LIST_MODE);
                }else {
                    SharedPreferencesUtil.getInstance().setHomeMode(MAP_MODE);
                }
                switchMode();
                break;

            case R.id.toolbar_right:
                IntentIntegrator.forSupportFragment(this).setCaptureActivity(ScanActivity.class).initiateScan();
                break;
        }
    }
    // Get the results:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                LogUtil.i("ttt",result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showUserInfo(com.mob.ums.User user) {
        LinkUser linkUser = HttpParseUtil.parseObject(user.nickname.get(), LinkUser.class);
        if(linkUser!=null){
            this.mapId = linkUser.getMapId();
            if(latitude==0&&longitude==0)return;
            mPresenter.updateMapUser(mapId,latitude,longitude);
            mPresenter.searchMapUser(latitude,longitude);
        }
    }

    @Override
    public void showMapUser(List<MapUser> mapUsers) {
        this.mapUsers = mapUsers;
        switchMode();
    }
}
