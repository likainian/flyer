package com.flyer.chat.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.flyer.chat.R;
import com.flyer.chat.activity.common.BigPictureActivity;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.util.GlideEngine;
import com.flyer.chat.util.ToastUtil;
import com.flyer.chat.widget.FlowLayout;
import com.flyer.chat.widget.ProgressView;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.setting.Setting;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mike.li on 2020/6/24.
 */
public class TestCanvasActivity extends ToolbarActivity {
    private int progress;
    private ArrayList<String> paths;
    private ImageView imageView;
    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, TestCanvasActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_canvas);
        setToolbarMiddleText("自绘组件");
        FlowLayout flowLayout = findViewById(R.id.flow_layout);
        imageView = findViewById(R.id.iv_test);
        final ProgressView progressView = findViewById(R.id.progress_view);
        final ArrayList<String> strings = new ArrayList<>();
        strings.add("简单的测试");
        strings.add("试");
        strings.add("非常简单的测试");
        strings.add("这是");
        strings.add("这是简单的测试");
        strings.add("简单的测试");
        strings.add("简单的测试");
        strings.add("试");
        strings.add("非常简单的测试");
        strings.add("这是");
        strings.add("这是简单的测试");
        strings.add("简单的测试");
        flowLayout.setAllFlowTab(strings);
        Disposable ss = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong){
                        progress = progress+1;
                        if(progress<=100){
                            progressView.setProgress(progress);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable){
                        ToastUtil.showToast(throwable.getMessage());
                    }
                });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paths==null){
                    getphoto();
                }else {
                    BigPictureActivity.startActivity(TestCanvasActivity.this,paths,0);
                }
            }
        });

    }

    private void getphoto(){
        EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                .setFileProviderAuthority("cn.bmob.v3.util.BmobContentProvider")
                .setCameraLocation(Setting.LIST_FIRST)
                .setCount(1)
                .start(new SelectCallback() {
                    @Override
                    public void onResult(ArrayList<Photo> photos, ArrayList<String> paths2, boolean isOriginal) {
                        paths = paths2;
                        Glide.with(TestCanvasActivity.this).load(paths2.get(0)).into(imageView);
                    }
                });
    }

}
