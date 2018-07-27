package com.flyer.chat.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flyer.chat.R;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.base.BaseFragment;
import com.flyer.chat.bean.User;
import com.flyer.chat.util.ConstantUtil;
import com.flyer.chat.util.GlideOptions;
import com.flyer.chat.util.SharedPreferencesHelper;
import com.flyer.mapsdk.MapSdk;
import com.flyer.mapsdk.api.LocationChangeListener;

import java.util.List;

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

    public void setNewData(List<User> users){
        if(users==null)return;
        Log.d("ttt", "setNewData: "+users.size());
        mapSdk.removeMarkers();
        for (final User user:users){
            Glide.with(this).applyDefaultRequestOptions(GlideOptions.UserOptions())
                    .asBitmap().load(ConstantUtil.getImageUrl(user.getImg())).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.map_user_info, null);
                    ImageView userImg = view.findViewById(R.id.user_img);
                    userImg.setImageBitmap(resource);
                    mapSdk.addMarker(user.getLatitude(),user.getLongitude(),view,user);
                }
            });
        }
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
