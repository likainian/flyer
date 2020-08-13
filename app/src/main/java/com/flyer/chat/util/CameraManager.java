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
public class CameraManager {
    private static int SELECTED_CAMERA = -1;
    private static int CAMERA_POST_POSITION = -1;
    private static int CAMERA_FRONT_POSITION = -1;
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
    public static void setParametersFlash(Camera camera,String flashMode){
        Camera.Parameters parameters = camera.getParameters();
        parameters.setFlashMode(flashMode);
        camera.setParameters(parameters);
    }
    public static void setAutoFocus(final Camera camera){
        setAutoFocus(camera, new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {

            }
        });
    }

    public static void setAutoFocus(final Camera camera, final Camera.AutoFocusCallback callback){
        Disposable ss = Observable.interval(0, 3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong){
                        camera.autoFocus(callback);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable){
                        ToastUtil.showToast(throwable.getMessage());
                    }
                });
    }

    public void switchFlashLight(Camera camera) {
        //  Log.i("打开闪光灯", "openFlashLight");
        Camera.Parameters parameters = camera.getParameters();
        String flashMode = parameters.getFlashMode();
        if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
            /*关闭闪光灯*/
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        } else {
            /*打开闪光灯*/
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        }
        camera.setParameters(parameters);
    }

    public static Camera switchCamera() {
        findAvailableCameras();
        if (SELECTED_CAMERA == CAMERA_POST_POSITION) {
            SELECTED_CAMERA = CAMERA_FRONT_POSITION;
        } else {
            SELECTED_CAMERA = CAMERA_POST_POSITION;
        }
        LogUtil.i("ttt",CAMERA_FRONT_POSITION+":CAMERA_FRONT_POSITION");
        LogUtil.i("ttt",CAMERA_POST_POSITION+":CAMERA_POST_POSITION");
        return Camera.open(SELECTED_CAMERA);
    }
    private static void findAvailableCameras() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        int cameraNum = Camera.getNumberOfCameras();
        for (int i = 0; i < cameraNum; i++) {
            Camera.getCameraInfo(i, info);
            switch (info.facing) {
                case Camera.CameraInfo.CAMERA_FACING_FRONT:
                    CAMERA_FRONT_POSITION = info.facing;
                    break;
                case Camera.CameraInfo.CAMERA_FACING_BACK:
                    CAMERA_POST_POSITION = info.facing;
                    break;
            }
        }
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
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
    }

}
