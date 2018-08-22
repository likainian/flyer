package com.flyer.chat.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.flyer.chat.R;
import com.flyer.chat.bean.User;
import com.flyer.chat.util.CommonUtil;
import com.flyer.chat.util.GlideOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike.li on 2018/8/1.
 */

public class PoiOverlay {
    private Context context;
    private AMap aMap;
    private List<Marker> markers = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    public PoiOverlay(Context context,AMap aMap) {
        this.context = context;
        this.aMap = aMap;
    }

    public void removeUsers() {
        for (Marker marker:markers){
            if(marker!=null){
                marker.remove();
                marker.destroy();
            }
        }
    }
    public void addUsers(List<User> users){
        this.users = users;
        for (final User user:users){
            Glide.with(context).applyDefaultRequestOptions(GlideOptions.UserOptions())
                    .asBitmap().load(CommonUtil.getImageUrl(user.getImg())).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    View view = LayoutInflater.from(context).inflate(R.layout.map_user_info, null);
                    ImageView userImg = view.findViewById(R.id.user_img);
                    userImg.setImageBitmap(resource);
                    addMarker(user.getLatitude(),user.getLongitude(),view,user);
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    View view = LayoutInflater.from(context).inflate(R.layout.map_user_info, null);
                    ImageView userImg = view.findViewById(R.id.user_img);
                    userImg.setImageDrawable(errorDrawable);
                    addMarker(user.getLatitude(),user.getLongitude(),view,user);
                }
            });
        }
    }
    private void addMarker(double latitude, double longitude, View view,User user) {
        Log.d("ttt", "addMarker: "+latitude+":"+longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude,longitude));
        markerOptions.icon(BitmapDescriptorFactory.fromView(view));
        Marker marker = aMap.addMarker(markerOptions);
        marker.setObject(user);
        markers.add(marker);
        if(markers.size()==users.size())moveCamera();
    }
    private void moveCamera() {
        Log.d("ttt", "moveCamera: ");
        LatLngBounds latLngBounds = getLatLngBounds();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100));
    }
    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (Marker marker:markers){
            LatLng position = marker.getOptions().getPosition();
            b.include(position);
        }
        return b.build();
    }
}
