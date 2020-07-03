package com.flyer.chat.activity.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.flyer.chat.R;
import com.flyer.chat.activity.home.bean.SignEntity;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.base.BaseFragment;
import com.flyer.chat.map.LocationSdk;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.SharedPreferencesUtil;
import com.flyer.chat.util.ToastUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by mike.li on 2018/7/9.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener, AMapLocationListener {

    private SignEntity signEntity = new SignEntity();

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
    }


    private void initView(View rootView) {
        FrameLayout toolbarLeft = rootView.findViewById(R.id.toolbar_left);
        TextView toolbarMiddle = rootView.findViewById(R.id.toolbar_middle);
        toolbarMiddle.setText("考勤");
        toolbarLeft.setVisibility(View.INVISIBLE);
        TextView tvSign = rootView.findViewById(R.id.tv_sign);
        tvSign.setOnClickListener(this);
        LocationSdk.startLocation(ChatApplication.getInstance(), (AMapLocationListener) this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign:
                doSign();
                break;
        }
    }

    private void doSign(){
        LogUtil.i("ttt",signEntity.toString());
        signEntity.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(aMapLocation.getErrorCode()==0){
            signEntity.setPhoneNumber(SharedPreferencesUtil.getInstance().getPhoneNumber());
            signEntity.setLatitude(aMapLocation.getLatitude());
            signEntity.setLongitude(aMapLocation.getLongitude());
            signEntity.setLocation(aMapLocation.getAddress());
        }else {
            ToastUtil.showToast(aMapLocation.getErrorInfo()+aMapLocation.getErrorCode());
        }
    }
}
