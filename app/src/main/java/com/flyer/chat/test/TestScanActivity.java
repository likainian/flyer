package com.flyer.chat.test;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.flyer.chat.R;
import com.flyer.chat.activity.common.BigPictureActivity;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.util.CameraUtil;
import com.flyer.chat.util.FileUtil;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.TimeUtil;
import com.flyer.chat.util.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by mike.li on 2020/6/30.
 */
public class TestScanActivity extends ToolbarActivity implements View.OnTouchListener {
    private String TAG = "TestScanActivity";
    private Camera mCamera;
    private SurfaceView mPreview;
    private ImageView mIvImage;
    private Camera.Parameters mParameters;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;
    private Button button_picture;
    private Button button_video;
    private String fullPath;

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
        setToolbarMiddleText("相机");
        mIvImage = findViewById(R.id.iv_image);
        mPreview = (SurfaceView) findViewById(R.id.preview_view);
        mPreview.setOnTouchListener(this);
        final VideoView videoView = (VideoView) findViewById(R.id.video_view);

        button_video = (Button) findViewById(R.id.button_video);

        mCamera = CameraUtil.getCameraInstance();
        CameraUtil.addCallback(mCamera,mPreview.getHolder());
        CameraUtil.setAutoFocus(mCamera, new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {

            }
        });
        CameraUtil.setParametersRotation(mCamera,90);

        button_picture = (Button) findViewById(R.id.button_picture);
        button_picture.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        LogUtil.i("ttt","照片"+ TimeUtil.longToYearMonthDayTimeSecond(System.currentTimeMillis()));
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );


        button_video.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isRecording) {
                            // stop recording and release camera
                            mediaRecorder.stop();  // stop the recording
                            releaseMediaRecorder(); // release the MediaRecorder object
                            mCamera.lock();         // take camera access back from MediaRecorder

                            // inform the user that recording has stopped
                            setCaptureButtonText("开始");
                            isRecording = false;
                            videoView.setVideoPath(fullPath);
//                            videoView.setRotation(90);
                            videoView.start();
                        }else {
                            // initialize video camera
                            if (prepareVideoRecorder()) {
                                // Camera is available and unlocked, MediaRecorder is prepared,
                                // now you can start recording
                                mediaRecorder.start();

                                // inform the user that recording has started
                                setCaptureButtonText("停止");
                                isRecording = true;
                            } else {
                                // prepare didn't work, release the camera
                                releaseMediaRecorder();
                                // inform user
                            }
                        }
                    }
                }
        );
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.start();
            }
        });

    }

    private void setCaptureButtonText(String stop) {
        button_video.setText(stop);
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            LogUtil.i("ttt","拿到数据"+ TimeUtil.longToYearMonthDayTimeSecond(System.currentTimeMillis()));
            final String fullPath = FileUtil.getSavePicturePath()+System.currentTimeMillis()+".jpg";
            mIvImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add(fullPath);
                    BigPictureActivity.startActivity(TestScanActivity.this,strings,0);
                }
            });
            Glide.with(TestScanActivity.this).load(data).into(mIvImage);
            FileUtil.saveByteFile(fullPath,data);
            mCamera.startPreview();
        }
    };

    private boolean prepareVideoRecorder(){

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
        mediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
        mediaRecorder.setOrientationHint(90);//视频旋转90度
        // Step 6: Prepare configured MediaRecorder
        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private float oldDist = 1f;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getPointerCount() == 1) {
            handleFocusMetering(event, mCamera);
        } else {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = getFingerSpacing(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float newDist = getFingerSpacing(event);
                    if (newDist > oldDist) {
                        handleZoom(true, mCamera);
                    } else if (newDist < oldDist) {
                        handleZoom(false, mCamera);
                    }
                    oldDist = newDist;
                    break;
            }
        }
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
    private void handleFocusMetering(MotionEvent event, Camera camera) {
        int viewWidth = mPreview.getWidth();
        int viewHeight = mPreview.getHeight();
        Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f, viewWidth, viewHeight);
        Rect meteringRect = calculateTapArea(event.getX(), event.getY(), 1.5f, viewWidth, viewHeight);

        camera.cancelAutoFocus();
        Camera.Parameters params = camera.getParameters();
        if (params.getMaxNumFocusAreas() > 0) {
            List<Camera.Area> focusAreas = new ArrayList<>();
            focusAreas.add(new Camera.Area(focusRect, 800));
            params.setFocusAreas(focusAreas);
        } else {
            LogUtil.i(TAG, "focus areas not supported");
        }
        if (params.getMaxNumMeteringAreas() > 0) {
            List<Camera.Area> meteringAreas = new ArrayList<>();
            meteringAreas.add(new Camera.Area(meteringRect, 800));
            params.setMeteringAreas(meteringAreas);
        } else {
            LogUtil.i(TAG, "metering areas not supported");
        }
        final String currentFocusMode = params.getFocusMode();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
        camera.setParameters(params);

        camera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                Camera.Parameters params = camera.getParameters();
                params.setFocusMode(currentFocusMode);
                camera.setParameters(params);
            }
        });
    }
    private static Rect calculateTapArea(float x, float y, float coefficient, int width, int height) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / width * 2000 - 1000);
        int centerY = (int) (y / height * 2000 - 1000);

        int halfAreaSize = areaSize / 2;
        RectF rectF = new RectF(clamp(centerX - halfAreaSize, -1000, 1000)
                , clamp(centerY - halfAreaSize, -1000, 1000)
                , clamp(centerX + halfAreaSize, -1000, 1000)
                , clamp(centerY + halfAreaSize, -1000, 1000));
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }
    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }
}
