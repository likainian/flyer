package com.flyer.mapsdk.api;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by mike.li on 2018/7/12.
 */

public interface MapFace {
    View getMapView(Context context);
    void onStart(Bundle savedInstanceState);
    void onStop();
    void onDestroy();
    void addMarker(double latitude, double longitude, View view,Object object);
    void removeMarkers();
    void setLocationChangeListener(LocationChangeListener locationChangeListener);
}
