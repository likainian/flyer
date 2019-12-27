package com.flyer.chat.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.flyer.chat.R;
import com.flyer.chat.activity.account.bean.User;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.util.BitmapUtil;
import com.flyer.chat.util.GlideEngine;
import com.flyer.chat.util.ToastUtil;
import com.flyer.chat.widget.PhotoView;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.setting.Setting;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by mike.li on 2018/8/23.
 */

public class UserHeadActivity extends ToolbarActivity {
    private PhotoView mPhotoView;


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, UserHeadActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_head);
        initView();
        showHead();
    }

    private void initView() {
        mPhotoView = findViewById(R.id.photo_view);
        setToolbarMiddleText("个人头像");
        setToolbarRightText("修改");
        setToolbarRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyPhotos.createAlbum(UserHeadActivity.this, true, GlideEngine.getInstance())
                        .setFileProviderAuthority("cn.bmob.v3.util.BmobContentProvider")
                        .setCameraLocation(Setting.LIST_FIRST)
                        .setCount(1)
                        .start(new SelectCallback() {
                            @Override
                            public void onResult(ArrayList<Photo> photos, ArrayList<String> paths, boolean isOriginal) {
                                upLoadHead(paths.get(0));
                            }
                        });
            }
        });
    }

    private void upLoadHead(String file){
        showLoadingDialog();
        String image = BitmapUtil.bitmapToBase64(BitmapFactory.decodeFile(file));
        final User user = BmobUser.getCurrentUser(User.class);
        user.setImg(image);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                closeLoadingDialog();
                if (e == null) {
                    ToastUtil.showToast("更新用户信息成功");
                    showHead();
                } else {
                    ToastUtil.showToast("更新用户信息失败：" + e.getMessage());
                }
            }
        });
    }

    private void showHead(){
        User user = BmobUser.getCurrentUser(User.class);
        Bitmap bitmap = BitmapUtil.base64ToBitmap(user.getImg());
        if(bitmap!=null){
            mPhotoView.setImageBitmap(bitmap);
        }else {
            mPhotoView.setImageResource(R.drawable.default_head);
        }
    }
}
