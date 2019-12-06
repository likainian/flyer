package com.flyer.chat.activity.feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.listener.EditTextWatcher;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.GlideEngine;
import com.flyer.chat.util.ItemTouchCallback;
import com.flyer.chat.util.LogUtil;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.setting.Setting;

import java.io.File;
import java.util.ArrayList;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by mike.li on 2019/10/29.
 */
public class QuestionActivity extends ToolbarActivity{
    private EditText mEtMessage;
    private TextView mTvCommit;
    private ArrayList<String> gridPaths = new ArrayList<>();
    private GridPhotoAdapter gridPhotoAdapter;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, QuestionActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initView();
        setToolbarMiddleText("遇到问题");
    }

    private void initView() {
        mEtMessage = (EditText) findViewById(R.id.et_message);
        mTvCommit = (TextView) findViewById(R.id.tv_commit);
        TextView mTvDelete = (TextView) findViewById(R.id.tv_delete);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mTvDelete.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        gridPhotoAdapter = new GridPhotoAdapter(this,9);
        new ItemTouchHelper(new ItemTouchCallback(gridPhotoAdapter,gridPaths, mTvDelete)).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(gridPhotoAdapter);
        gridPhotoAdapter.setNewData(gridPaths);
        gridPhotoAdapter.setOnClickAddListener(new GridPhotoAdapter.OnClickAddListener() {
            @Override
            public void OnClickAdd() {
                addPicture();
            }
        });

        mEtMessage.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mTvCommit.setEnabled(CheckUtil.isNotEmpty(mEtMessage.getText().toString().trim()));
            }
        });
        mTvCommit.setEnabled(CheckUtil.isNotEmpty(mEtMessage.getText().toString().trim()));
        mTvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit();
            }
        });
    }

    private void addPicture(){
        EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                .setFileProviderAuthority("cn.bmob.v3.util.BmobContentProvider")
                .setCameraLocation(Setting.LIST_FIRST)
                .setCount(9)
                .setSelectedPhotoPaths(gridPaths)
                .start(new SelectCallback() {
                    @Override
                    public void onResult(ArrayList<Photo> photos, ArrayList<String> paths, boolean isOriginal) {
                        gridPaths.clear();
                        gridPaths.addAll(paths);
                        gridPhotoAdapter.setNewData(gridPaths);
                    }
                });
    }

    private void commit(){
        final BmobFile bmobFile = new BmobFile(new File(gridPaths.get(0)));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    LogUtil.i("ttt",bmobFile.getFileUrl());
                }else{
                    LogUtil.i("ttt",e.getMessage());
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }
}
