package com.flyer.chat.base;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.flyer.chat.dialog.SelectDialog;
import com.flyer.chat.util.DeviceUtil;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.ToastHelper;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;

/**
 * Created by mike.li on 2018/8/21.
 */

public abstract class MediaFragment extends BaseFragment{
    private static final int PICK_PICTURE = 11;
    private static final int TAKE_PICTURE = 12;
    protected ArrayList<String> imgPathList = new ArrayList<>();
    private String addPicturePath;

    protected void showPopMenuDialogWindow(final int maxCount) {
        ArrayList<String> list = new ArrayList<>();
        list.add("拍照（"+imgPathList.size()+"/"+maxCount+"）");
        list.add("相册（"+imgPathList.size()+"/"+maxCount+"）");
        new SelectDialog(getActivity()).setList(list).setOnSelectListener(new SelectDialog.OnSelectListener() {
            @Override
            public void OnSelect(int position) {
                switch (position){
                    case 0:
                        takePicture();
                        break;
                    case 1:
                        pickPicture();
                        break;
                }
            }
        }).show();
    }

    protected abstract void takePictureReturn(String path);
    //拍照
    protected void takePicture() {
        new RxPermissions(this).requestEach(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if(permission.granted){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // 下面这句指定调用相机拍照后的照片存储的路径
                            addPicturePath = DeviceUtil.getCameraPhonePath(String.valueOf(System.currentTimeMillis()));
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(addPicturePath)));
                            startActivityForResult(intent, TAKE_PICTURE);
                        }else {
                            ToastHelper.showToast("请开启相机权限");
                        }
                    }
                });
    }
    //相册
    protected void pickPicture() {
        new RxPermissions(this).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if(permission.granted){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            // 下面这句指定调用相机拍照后的照片存储的路径
                            addPicturePath = DeviceUtil.getCameraPhonePath(String.valueOf(System.currentTimeMillis()));
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(addPicturePath)));
                            startActivityForResult(intent, TAKE_PICTURE);
                        }else {
                            ToastHelper.showToast("请开启读写权限");
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK != resultCode)return;
        switch (requestCode){
            case TAKE_PICTURE:
                imgPathList.add(addPicturePath);
                takePictureReturn(addPicturePath);
                LogUtil.i(imgPathList.toString());
                break;
            case PICK_PICTURE:
                if(data==null)return;
                takePictureReturn("xuzhe");
                LogUtil.i(imgPathList.toString());
                break;
        }
    }
}
