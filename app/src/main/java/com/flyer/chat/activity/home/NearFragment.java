package com.flyer.chat.activity.home;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.activity.account.bean.User;
import com.flyer.chat.activity.home.adapter.NearAdapter;
import com.flyer.chat.base.BaseFragment;
import com.flyer.chat.fragment.MapFragment;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.SharedPreferencesUtil;
import com.flyer.chat.util.ToastUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by mike.li on 2018/7/9.
 */

public class NearFragment extends BaseFragment implements View.OnClickListener {
    public static final String MAP_MODE = "map_mode";
    public static final String LIST_MODE = "list_mode";
    private NearAdapter adapter;
    private MapFragment mapFragment;
    private double latitude;
    private double longitude;

    public TextView mToolbarMiddle;
    public TextView mToolbarRightText;
    public RelativeLayout mRlToolbar;
    public RecyclerView mRecyclerView;
    public FrameLayout mFlHomeMap;
    private FrameLayout mToolbarLeft;
    private ImageView mIvReload;
    private FrameLayout mToolbarRight;

    public static NearFragment newInstance() {
        return new NearFragment();
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
        switchMode();
    }


    private void initView(View rootView) {
        this.mToolbarMiddle = (TextView) rootView.findViewById(R.id.toolbar_middle);
        this.mToolbarLeft = (FrameLayout) rootView.findViewById(R.id.toolbar_left);
        this.mToolbarRight = (FrameLayout) rootView.findViewById(R.id.toolbar_right);
        this.mToolbarRightText = (TextView) rootView.findViewById(R.id.toolbar_right_text);
        this.mRlToolbar = (RelativeLayout) rootView.findViewById(R.id.rl_toolbar);
        this.mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        this.mFlHomeMap = (FrameLayout) rootView.findViewById(R.id.fl_home_map);
        this.mIvReload = (ImageView) rootView.findViewById(R.id.iv_reload);

        mToolbarLeft.setVisibility(View.GONE);
        mToolbarMiddle.setText("附近的人");
        mIvReload.setOnClickListener(this);
        mToolbarRight.setOnClickListener(this);

    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NearAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);

        mapFragment = MapFragment.newInstance();
        mapFragment.setOnLocationChangeListener(new MapFragment.OnLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(latitude==0&&longitude==0&&location.getLatitude()!=0&&location.getLongitude()!=0){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    upLoadUser();
                    queryUser();
                }else {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
            }
        });
        getChildFragmentManager().beginTransaction().add(R.id.fl_home_map, mapFragment).commit();
    }

    private void switchMode() {
        if (MAP_MODE.equals(SharedPreferencesUtil.getInstance().getHomeMode())) {
            mToolbarRightText.setText("列表");
            mFlHomeMap.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mToolbarRightText.setText("地图");
            mFlHomeMap.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_reload:
                queryUser();
                break;
            case R.id.toolbar_right:
                if (MAP_MODE.equals(SharedPreferencesUtil.getInstance().getHomeMode())) {
                    SharedPreferencesUtil.getInstance().setHomeMode(LIST_MODE);
                } else {
                    SharedPreferencesUtil.getInstance().setHomeMode(MAP_MODE);
                }
                switchMode();
                break;
        }
    }

    private void queryUser(){
        User currentUser = BmobUser.getCurrentUser(User.class);
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereLessThan("latitude",latitude+10);
        query.addWhereGreaterThan("latitude",latitude-10);
        query.addWhereNotEqualTo("mobilePhoneNumber",currentUser.getMobilePhoneNumber());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    LogUtil.i("ttt",object.toString());
                    ToastUtil.showToast("查询成功"+object);
                    adapter.setNewData(object);
                    mapFragment.setNewData(object);
                } else {
                    ToastUtil.showToast("查询失败"+e.getMessage());
                }
            }
        });
    }

    private void upLoadUser(){
        User user = BmobUser.getCurrentUser(User.class);
        user.setLatitude(latitude);
        user.setLongitude(longitude);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                closeLoadingDialog();
                if (e == null) {
                    ToastUtil.showToast("更新用户信息成功");
                } else {
                    ToastUtil.showToast("更新用户信息失败：" + e.getMessage());
                }
            }
        });
    }
}
