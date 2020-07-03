package com.flyer.chat.test;

import android.Manifest;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.flyer.chat.R;
import com.flyer.chat.activity.common.BigPictureActivity;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.util.FileUtil;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.TimeUtil;
import com.flyer.chat.util.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mike.li on 2020/6/30.
 */
public class TestScanActivity extends ToolbarActivity {
    private String TAG = "TestScanActivity";
    private Camera mCamera;
    private CameraPreview mPreview;
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
        mIvImage = findViewById(R.id.iv_image);
        mCamera = getCameraInstance();
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        final VideoView videoView = (VideoView) findViewById(R.id.video_view);
        preview.addView(mPreview);
        // Add a listener to the Capture button
        button_picture = (Button) findViewById(R.id.button_picture);
        button_video = (Button) findViewById(R.id.button_video);
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
        Disposable ss = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong){
                        mCamera.autoFocus(new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {

                            }
                        });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable){
                        ToastUtil.showToast(throwable.getMessage());
                    }
                });

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
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setRotation(90);
        mCamera.setParameters(parameters);
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
        }
    };

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
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            return false;
        }
        return true;
    }

}
