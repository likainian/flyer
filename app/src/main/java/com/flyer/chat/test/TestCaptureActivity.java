package com.flyer.chat.test;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.SurfaceView;

import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.util.CameraManager;
import com.flyer.chat.util.CodeUtil;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.ToastUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Vector;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by mike.li on 2020/7/6.
 */
public class TestCaptureActivity extends ToolbarActivity implements Camera.PreviewCallback {
    private Camera mCamera;
    private SurfaceView mPreview;
    public static void startActivity(final FragmentActivity activity){
        Disposable subscribe = new RxPermissions(activity).requestEach(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission){
                        if(permission.granted){
                            activity.startActivity(new Intent(activity, TestCaptureActivity.class));
                        }else {
                            ToastUtil.showToast("没有相机权限");
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i("ttt","TestCaptureActivity"+"onCreate");
        setContentView(R.layout.activity_test_capture);
        mPreview = findViewById(R.id.preview_view);

        mCamera = CameraManager.getCameraInstance();
        CameraManager.addCallback(mCamera,mPreview.getHolder());
        CameraManager.setAutoFocus(mCamera, new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                mCamera.setOneShotPreviewCallback(TestCaptureActivity.this);
            }
        });
        CameraManager.setParametersRotation(mCamera,90);

        Bitmap any = CodeUtil.encode("随便一句话");
        if (any != null) {
            CodeUtil.decode2(any);
        }


//        mCamera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
//            @Override
//            public void onPreviewFrame(byte[] data, Camera camera) {
//                LogUtil.i("ttt",":"+mPreview.getWidth()+mPreview.getHeight());
//                PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, mPreview.getWidth(), mPreview.getHeight(), 0,0, mPreview.getWidth(), mPreview.getHeight(), false);
//                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//                MultiFormatReader multiFormatReader = new MultiFormatReader();
//                try {
//                    Result rawResult = multiFormatReader.decodeWithState(bitmap);
//                    LogUtil.i("ttt",rawResult.getText());
//                } catch (ReaderException re) {
//                    ToastUtil.showToast(re.getMessage());
//                } finally {
//                    multiFormatReader.reset();
//                }
//            }
//        });

//        Button button_picture = (Button) findViewById(R.id.button_picture);
//        button_picture.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // get an image from the camera
//                        LogUtil.i("ttt","照片"+ TimeUtil.longToYearMonthDayTimeSecond(System.currentTimeMillis()));
//                        mCamera.takePicture(null, null, new Camera.PictureCallback() {
//                            @Override
//                            public void onPictureTaken(byte[] data, Camera camera) {
//                                final String fullPath = FileUtil.getSavePicturePath()+System.currentTimeMillis()+".jpg";
//                                FileUtil.saveByteFile(fullPath,data);
//                            }
//                        });
//                    }
//                }
//        );

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        LogUtil.i("ttt",":"+mPreview.getWidth()+":"+mPreview.getHeight());
        PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, mPreview.getWidth(), mPreview.getHeight(), 0,0, mPreview.getWidth(), mPreview.getHeight(), false);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        Hashtable<DecodeHintType, Object> hints = new Hashtable<>();
        Vector<BarcodeFormat> decodeFormats = new Vector<>();
        decodeFormats.addAll(EnumSet.of(BarcodeFormat.UPC_A,
                BarcodeFormat.UPC_E,
                BarcodeFormat.EAN_13,
                BarcodeFormat.EAN_8,
                BarcodeFormat.RSS_14,
                BarcodeFormat.RSS_EXPANDED));
        decodeFormats.addAll(EnumSet.of(BarcodeFormat.QR_CODE));
        decodeFormats.addAll(EnumSet.of(BarcodeFormat.DATA_MATRIX));
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        multiFormatReader.setHints(hints);
        try {
            Result rawResult = multiFormatReader.decodeWithState(bitmap);
            LogUtil.i("ttt",rawResult.getText());
        } catch (ReaderException re) {
            LogUtil.i("ttt","没有");
            ToastUtil.showToast(re.getMessage());
        } finally {
            multiFormatReader.reset();
        }
    }
}
