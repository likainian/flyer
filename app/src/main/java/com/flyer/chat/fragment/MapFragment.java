package com.flyer.chat.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.flyer.chat.R;
import com.flyer.chat.base.BaseFragment;
import com.flyer.chat.bean.MapUser;
import com.flyer.chat.map.PoiOverlay;
import com.flyer.chat.util.LogUtil;

import java.util.List;

/**
 * Created by mike.li on 2018/7/12.
 */

public class MapFragment extends BaseFragment implements AMap.OnMyLocationChangeListener, AMap.OnMarkerClickListener {

    private MapView mMapView;
    private AMap aMap;
    private PoiOverlay poiOverlay;
    public OnLocationChangeListener onLocationChangeListener;
    public interface OnLocationChangeListener{
        void onMyLocationChange(Location location);
    }

    public void setOnLocationChangeListener(OnLocationChangeListener onLocationChangeListener) {
        this.onLocationChangeListener = onLocationChangeListener;
    }

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
        mMapView = view.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        poiOverlay = new PoiOverlay(getActivity(),aMap);
        initMap();
    }
    private void initMap(){
        UiSettings uiSettings = aMap.getUiSettings();
        //缩放按钮
        uiSettings.setZoomControlsEnabled(false);
        //定位按钮是否显示
        uiSettings.setMyLocationButtonEnabled(true);
        //比例尺
        uiSettings.setScaleControlsEnabled(true);
        //定位
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(this);
        aMap.setOnMarkerClickListener(this);
    }

    public void setNewData(List<MapUser> users){
        if(users==null)return;
        poiOverlay.removeUsers();
        poiOverlay.addUsers(users);
    }
    @Override
    public void onStop() {
        super.onStop();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onMyLocationChange(Location location) {
        LogUtil.i("ttt","定位成功："+location.toString());
        if(onLocationChangeListener!=null){
            onLocationChangeListener.onMyLocationChange(location);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
