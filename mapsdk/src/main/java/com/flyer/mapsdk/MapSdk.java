package com.flyer.mapsdk;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.flyer.mapsdk.api.LocationChangeListener;
import com.flyer.mapsdk.api.MapFace;
import com.flyer.mapsdk.api.MarkerClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike.li on 2018/7/12.
 */

public class MapSdk implements MapFace{
    private LocationChangeListener locationChangeListener;
    private MarkerClickListener markerClickListener;
    private MapView mMapView;
    private AMap aMap;
    private List<Marker> markers = new ArrayList<>();

    @Override
    public View getMapView(Context context) {
        mMapView = new MapView(context);
        initSetting();
        return mMapView;
    }
    void initSetting(){
        aMap = mMapView.getMap();
        UiSettings uiSettings = aMap.getUiSettings();
        //缩放按钮
        uiSettings.setZoomControlsEnabled(false);
        //定位按钮是否显示
        uiSettings.setMyLocationButtonEnabled(true);
        //比例尺
        uiSettings.setScaleControlsEnabled(true);
        //定位
        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        //定位时间间隔
//        myLocationStyle.interval(2000);
        //定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(locationChangeListener!=null)locationChangeListener.onLocationChange(location.getLatitude(),location.getLongitude());
            }
        });
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(markerClickListener!=null)markerClickListener.onMarkerClickListener(marker.getObject());
                return false;
            }
        });
    }

    @Override
    public void onStart(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
    }

    @Override
    public void addMarker(double latitude, double longitude, View view,Object object) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latitude,longitude));
        markerOptions.icon(BitmapDescriptorFactory.fromView(view));
        Marker marker = aMap.addMarker(markerOptions);
        marker.setObject(object);
        markers.add(marker);
    }

    @Override
    public void removeMarkers() {
        for (Marker marker:markers){
            if(marker!=null){
                marker.remove();
                marker.destroy();
            }
        }
    }

    @Override
    public void setLocationChangeListener(LocationChangeListener locationChangeListener) {
        this.locationChangeListener = locationChangeListener;
    }
}
