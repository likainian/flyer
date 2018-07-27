package com.flyer.pushsdk;

import android.app.Application;

/**
 * Created by mike.li on 2018/5/2.
 */

public interface PushFace {
    void init(Application application);
    void stopService(Application application);
}
