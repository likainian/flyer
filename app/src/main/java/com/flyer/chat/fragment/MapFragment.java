package com.flyer.chat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.flyer.chat.R;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.base.BaseFragment;
import com.flyer.chat.bean.User;
import com.flyer.chat.util.SharedPreferencesHelper;
import com.flyer.mapsdk.MapSdk;
import com.flyer.mapsdk.api.LocationChangeListener;

/**
 * Created by mike.li on 2018/7/12.
 */

public class MapFragment extends BaseFragment implements LocationChangeListener {

    private MapSdk mapSdk;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FrameLayout frameLayout = view.findViewById(R.id.frame_layout);
        mapSdk = new MapSdk();
        frameLayout.addView(mapSdk.getMapView(getActivity()));
        mapSdk.setLocationChangeListener(this);
        mapSdk.onStart(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        mapSdk.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapSdk.onDestroy();
    }

    @Override
    public void onLocationChange(double Latitude, double Longitude) {
        Log.i("ttt", "onLocationChange: "+Latitude+":"+Longitude);
        User user = SharedPreferencesHelper.getInstance().getUser();
        user.setLatitude(Latitude);
        user.setLongitude(Longitude);
        SharedPreferencesHelper.getInstance().setUser(user);
        ChatApplication.updateUser();
    }
}
