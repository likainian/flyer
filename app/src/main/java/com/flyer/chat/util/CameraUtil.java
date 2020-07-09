package com.flyer.chat.util;

import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mike.li on 2020/7/6.
 */
public class CameraUtil {
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            ToastUtil.showToast(e.getMessage());
        }
        return c; // returns null if camera is unavailable
    }
    //can only be 0, 90, 180 or 270.
    public static void setParametersRotation(Camera camera,int rotation){
        Camera.Parameters parameters = camera.getParameters();
        parameters.setRotation(rotation);
        camera.setParameters(parameters);
    }

    public static void setAutoFocus(final Camera mCamera, final Camera.AutoFocusCallback callback){
        Disposable ss = Observable.interval(0, 3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong){
                        mCamera.autoFocus(callback);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable){
                        ToastUtil.showToast(throwable.getMessage());
                    }
                });
    }
    public static void addCallback(final Camera mCamera, final SurfaceHolder mHolder){
        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // The Surface has been created, now tell the camera where to draw the preview.
                try {
                    mCamera.setPreviewDisplay(holder);
                    mCamera.setDisplayOrientation(90);
                    mCamera.startPreview();
                } catch (IOException e) {
                    ToastUtil.showToast(e.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // stop preview before making changes
                try {
                    mCamera.stopPreview();
                } catch (Exception e){
                    // ignore: tried to stop a non-existent preview
                }

                // set preview size and make any resize, rotate or
                // reformatting changes here

                // start preview with new settings
                try {
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();

                } catch (Exception e){
                    ToastUtil.showToast(e.getMessage());
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

}
