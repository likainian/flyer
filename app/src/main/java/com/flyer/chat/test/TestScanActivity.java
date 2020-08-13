package com.flyer.chat.test;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.flyer.chat.R;
import com.flyer.chat.activity.common.PhotoPlayActivity;
import com.flyer.chat.activity.common.VideoPlayActivity;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.listener.CaptureListener;
import com.flyer.chat.listener.SurfaceHolderCallback;
import com.flyer.chat.util.CameraManager;
import com.flyer.chat.util.DeviceUtil;
import com.flyer.chat.util.FileUtil;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.TimeUtil;
import com.flyer.chat.util.ToastUtil;
import com.flyer.chat.widget.CaptureButton;
import com.flyer.chat.widget.FocusView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static android.view.View.VISIBLE;

/**
 * Created by mike.li on 2020/6/30.
 */
public class TestScanActivity extends BaseActivity implements View.OnTouchListener, View.OnClickListener {
    private String TAG = "TestScanActivity";
    private Camera mCamera;
    private CaptureButton mCaptureView;
    private MediaRecorder mediaRecorder;
    private String fullPath;
    private FocusView mFocusView;
    private VideoView mVideoView;
    private boolean singleTouch = true;
    private ImageView mIvBack;
    private ImageView mIvFlash;
    private ImageView mIvSwitch;

    //闪关灯状态
    private static final int TYPE_FLASH_AUTO = 0x021;
    private static final int TYPE_FLASH_ON = 0x022;
    private static final int TYPE_FLASH_OFF = 0x023;
    private int type_flash = TYPE_FLASH_OFF;

    public static void startActivity(final FragmentActivity activity){
        Disposable subscribe = new RxPermissions(activity).requestEach(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if(permission.granted){
                            activity.startActivity(new Intent(activity,TestScanActivity.class));
                        }else {
                            ToastUtil.showToast("没有相机权限");
                        }
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scan);
        mCaptureView = (CaptureButton)findViewById(R.id.capture_view);
        mVideoView = (VideoView) findViewById(R.id.video_view);
        mFocusView = (FocusView) findViewById(R.id.focus_view);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvFlash = (ImageView) findViewById(R.id.iv_flash);
        mIvSwitch = (ImageView) findViewById(R.id.iv_switch);
        mIvBack.setOnClickListener(this);
        mIvSwitch.setOnClickListener(this);
        mIvFlash.setOnClickListener(this);
        mCaptureView.setCaptureLisenter(new CaptureListener() {
            @Override
            public void takePictures() {
                mCamera.takePicture(null, null, new Camera.PictureCallback() {

                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        LogUtil.i("ttt","拿到数据"+ TimeUtil.longToYearMonthDayTimeSecond(System.currentTimeMillis()));
                        final String fullPath = FileUtil.getSavePicturePath()+System.currentTimeMillis()+".jpg";
                        FileUtil.saveByteFile(fullPath,data);
                        mCaptureView.resetState();
                        mCamera.startPreview();
                        PhotoPlayActivity.startActivity(TestScanActivity.this,fullPath);
                    }
                });
            }

            @Override
            public void recordShort(long time) {

            }

            @Override
            public void recordStart() {
                prepareVideoRecorder();
            }

            @Override
            public void recordEnd(long time) {
                // stop recording and release camera
                mediaRecorder.stop();  // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
                mCamera.lock();         // take camera access back from MediaRecorder
                mCaptureView.resetState();
                VideoPlayActivity.startActivity(TestScanActivity.this,fullPath);
            }

            @Override
            public void recordZoom(float zoom) {

            }

            @Override
            public void recordError() {

            }
        });
        //camera
        mCamera = CameraManager.getCameraInstance();
        initCamera();
    }
    private void initCamera(){
        CameraManager.setAutoFocus(mCamera, new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                mFocusView.setVisibility(View.INVISIBLE);
            }
        });
        CameraManager.setParametersRotation(mCamera,90);
        mVideoView.getHolder().addCallback(new SurfaceHolderCallback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCamera.setPreviewDisplay(holder);
                    mCamera.setDisplayOrientation(90);
                    mCamera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mVideoView.setOnTouchListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_flash:
                type_flash++;
                if (type_flash > 0x023)
                    type_flash = TYPE_FLASH_AUTO;
                setFlashRes();
                break;
            case R.id.iv_switch:
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
                mCamera = CameraManager.switchCamera();
                initCamera();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }
    //释放资源
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            try {
                mCamera.setPreviewDisplay(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mCamera.release();
            mCamera = null;
        }
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }


    private void prepareVideoRecorder(){

        mediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        fullPath = FileUtil.getSavePicturePath()+System.currentTimeMillis()+".mp4";
        mediaRecorder.setOutputFile(fullPath);

        // Step 5: Set the preview output
        mediaRecorder.setPreviewDisplay(mVideoView.getHolder().getSurface());
        mediaRecorder.setOrientationHint(90);//视频旋转90度
        // Step 6: Prepare configured MediaRecorder
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException e) {
        } catch (IOException e) {
        }
    }

    private float oldDist = 1f;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mFocusView.setVisibility(View.INVISIBLE);
                if (event.getPointerCount() == 2) {
                    singleTouch = false;
                    oldDist = getFingerSpacing(event);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 2) {
                    singleTouch = false;
                    float newDist = getFingerSpacing(event);
                    if (newDist > oldDist) {
                        handleZoom(true, mCamera);
                    } else if (newDist < oldDist) {
                        handleZoom(false, mCamera);
                    }
                    oldDist = newDist;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (event.getPointerCount() == 1 && singleTouch) {
                    handleFocusMetering(event);
                    handlerFocusView(event.getX(),event.getY());
                }
                singleTouch = true;
                break;
        }
        return true;
    }

    public boolean handlerFocusView(float x, float y) {
        int cxy = mFocusView.getWidth() >> 1;
        int width = DeviceUtil.getDisplayWidth(this);
        int height = DeviceUtil.getDisplayHeight(this);
        if (x < cxy) {
            x = cxy;
        }
        if (x > width - cxy) {
            x = width - cxy;
        }
        if (y < cxy) {
            y = cxy;
        }
        if (y > height - cxy) {
            y = height - cxy;
        }
        mFocusView.setX(x - cxy);
        mFocusView.setY(y - cxy);
        mFocusView.setVisibility(VISIBLE);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mFocusView, "scaleX", 1, 0.6f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mFocusView, "scaleY", 1, 0.6f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mFocusView, "alpha", 1f, 0.4f, 1f, 0.4f, 1f, 0.4f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleX).with(scaleY).before(alpha);
        animSet.setDuration(400);
        animSet.start();
        return true;
    }
    private void handleZoom(boolean isZoomIn, Camera camera) {
        Camera.Parameters params = camera.getParameters();
        if (params.isZoomSupported()) {
            int maxZoom = params.getMaxZoom();
            int zoom = params.getZoom();
            if (isZoomIn && zoom < maxZoom-2) {
                zoom = zoom+2;
            } else if (zoom > 2) {
                zoom = zoom-2;
            }
            params.setZoom(zoom);
            camera.setParameters(params);
        } else {
            LogUtil.i(TAG, "zoom not supported");
        }
    }
    private static float getFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
    private void handleFocusMetering(MotionEvent event) {
        int viewWidth = mVideoView.getWidth();
        int viewHeight = mVideoView.getHeight();
        Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f, viewWidth, viewHeight);
        Rect meteringRect = calculateTapArea(event.getX(), event.getY(), 1.5f, viewWidth, viewHeight);
        mCamera.cancelAutoFocus();
        Camera.Parameters params = mCamera.getParameters();
        if (params.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<>();
            focusAreas.add(new Camera.Area(focusRect, 600));
            params.setFocusAreas(focusAreas);
        } else {
            LogUtil.i(TAG, "focus areas not supported");
        }
        if (params.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meteringAreas = new ArrayList<>();
            meteringAreas.add(new Camera.Area(meteringRect, 600));
            params.setMeteringAreas(meteringAreas);
        } else {
            LogUtil.i(TAG, "metering areas not supported");
        }
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
        mCamera.setParameters(params);
        CameraManager.setAutoFocus(mCamera, new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                mFocusView.setVisibility(View.INVISIBLE);
            }
        });
    }
    private Rect calculateTapArea(float x, float y, float coefficient, int width, int height) {
        float focusAreaSize = mFocusView.getWidth() ;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / width * 2000 - 1000);
        int centerY = (int) (y / height * 2000 - 1000);

        int halfAreaSize = areaSize / 2;
        return new Rect(clamp(centerX-halfAreaSize), clamp(centerY-halfAreaSize)
                , clamp(centerX+halfAreaSize), clamp(centerY+halfAreaSize));
    }
    private static int clamp(int x) {
        if (x > 1000) {
            return 1000;
        }
        if (x < -1000) {
            return -1000;
        }
        return x;
    }


    private void setFlashRes() {
        switch (type_flash) {
            case TYPE_FLASH_AUTO:
                mIvFlash.setImageResource(R.drawable.flash_auto);
                CameraManager.setParametersFlash(mCamera,Camera.Parameters.FLASH_MODE_AUTO);
                break;
            case TYPE_FLASH_ON:
                mIvFlash.setImageResource(R.drawable.flash_on);
                CameraManager.setParametersFlash(mCamera,Camera.Parameters.FLASH_MODE_ON);
                break;
            case TYPE_FLASH_OFF:
                mIvFlash.setImageResource(R.drawable.flash_off);
                CameraManager.setParametersFlash(mCamera,Camera.Parameters.FLASH_MODE_OFF);
                break;
        }
    }
}
