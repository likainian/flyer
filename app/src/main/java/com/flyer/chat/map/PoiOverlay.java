package com.flyer.chat.map;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.flyer.chat.activity.account.bean.User;
import com.flyer.chat.bean.MapUser;
import com.flyer.chat.util.LogUtil;

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
    }
    private void addMarker(View view,MapUser user) {
        LogUtil.i("ttt",user.toString());
        String location = user.getLocation();
        String[] split = location.split(",");
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(Double.parseDouble(split[0]),Double.parseDouble(split[1])));
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
