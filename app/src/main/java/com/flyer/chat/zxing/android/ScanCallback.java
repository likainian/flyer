package com.flyer.chat.zxing.android;

import android.os.Handler;

import com.flyer.chat.zxing.camera.CameraManager;
import com.google.zxing.Result;

/**
 * Created by mike.li on 2020/8/11.
 */
public interface ScanCallback {
    void handleDecode(Result rawResult);
    void switchFlashImg(int flashState);
    Handler getHandler();
    CameraManager getCameraManager();
    void drawViewfinder();
}
