package com.flyer.chat.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyer.chat.R;
import com.flyer.chat.adapter.GridPictureAdapter;
import com.flyer.chat.adapter.ListPictureAdapter;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.bean.PictureMap;
import com.flyer.chat.util.ToastHelper;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.functions.Consumer;

/**
 * Created by mike.li on 2018/8/21.
 */

public class PickPictureActivity extends BaseActivity implements View.OnClickListener {
    public static final String PICK_PATH = "select";
    private int needCount;
    private ImageView mToolbarLeft;
    private TextView mToolbarMiddle;
    private RecyclerView mGridPicture;
    private RecyclerView mListPicture;
    private FrameLayout mListLayout;
    private LinearLayout mListGroup;
    private TextView mGroupName;
    private ImageView mListImg;
    private TextView mFinish;
    private List<PictureMap> pictureMaps = new ArrayList<>();
    private GridPictureAdapter gridPictureAdapter;
    private ListPictureAdapter listPictureAdapter;

    public static void startActivity(Activity context, int needCount,int request) {
        Intent intent = new Intent(context, PickPictureActivity.class);
        intent.putExtra("needCount", needCount);
        context.startActivityForResult(intent,request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_picture);
        needCount = getIntent().getIntExtra("needCount", 9);
        initView();
        initAdapter();
        initData();
    }

    private void initAdapter() {
        mGridPicture.setLayoutManager(new GridLayoutManager(this,3));
        gridPictureAdapter = new GridPictureAdapter(this,needCount);
        mGridPicture.setAdapter(gridPictureAdapter);
        gridPictureAdapter.setOnPictureClickListener(new GridPictureAdapter.OnPictureClickListener() {
            @Override
            public void onPictureClick(int selectCount) {
                mFinish.setText("完成（"+selectCount+"/"+needCount+"）");
            }
        });

        mListPicture.setLayoutManager(new LinearLayoutManager(this));
        listPictureAdapter = new ListPictureAdapter(this);
        mListPicture.setAdapter(listPictureAdapter);
        listPictureAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mListLayout.setVisibility(View.GONE);
                mListImg.setImageResource(R.drawable.file_down);
                PictureMap pictureMap = pictureMaps.get(position);
                mGroupName.setText(pictureMap.getName());
                mToolbarMiddle.setText(pictureMap.getName());
                gridPictureAdapter.setNewData(pictureMap.getFiles());
            }
        });

    }

    private void initData() {
        new RxPermissions(this).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if(permission.granted){
                    Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
                    Map<String, List<File>> imgGroup = new HashMap<>();
                    List<File> files = new ArrayList<>();
                    if (cursor != null) {
                        while (cursor.moveToNext()){
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            File file = new File(path);
                            files.add(file);
                            File parentFile = file.getParentFile();
                            if (parentFile == null) {
                                continue;
                            }
                            String parentName = parentFile.getName();
                            if (!imgGroup.containsKey(parentName)) {
                                List<File> childList = new ArrayList<>();
                                childList.add(file);
                                imgGroup.put(parentName, childList);
                            } else {
                                imgGroup.get(parentName).add(file);
                            }
                        }
                        cursor.close();
                        if(imgGroup.size()>1){
                            mGroupName.setVisibility(View.VISIBLE);
                        }else {
                            mGroupName.setVisibility(View.GONE);
                        }
                        Set<Map.Entry<String, List<File>>> entries = imgGroup.entrySet();
                        for (Map.Entry<String, List<File>> entry:entries){
                            PictureMap pictureMap = new PictureMap(entry.getKey(), entry.getValue());
                            pictureMaps.add(pictureMap);
                        }
                        PictureMap pictureMap = new PictureMap("所有图片", files);
                        pictureMaps.add(0,pictureMap);
                        listPictureAdapter.setNewData(pictureMaps);
                        mGroupName.setText(pictureMap.getName());
                        mToolbarMiddle.setText(pictureMap.getName());
                        gridPictureAdapter.setNewData(pictureMap.getFiles());
                    }
                }else {
                    ToastHelper.showToast("请开启读写权限");
                }
            }
        });

    }

    private void initView() {
        mToolbarLeft = findViewById(R.id.toolbar_left);
        mToolbarMiddle = findViewById(R.id.toolbar_middle);
        mGridPicture = findViewById(R.id.grid_picture);
        mListPicture = findViewById(R.id.list_picture);
        mListLayout = findViewById(R.id.list_layout);
        mListGroup = findViewById(R.id.list_group);
        mGroupName = findViewById(R.id.group_name);
        mListImg = findViewById(R.id.group_img);
        mFinish = findViewById(R.id.finish);
        mToolbarLeft.setOnClickListener(this);
        mListLayout.setVisibility(View.GONE);
        mListImg.setImageResource(R.drawable.file_down);
        mListGroup.setOnClickListener(this);
        mListLayout.setOnClickListener(this);
        mFinish.setText("完成（0/"+needCount+"）");
        mFinish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_left:
                onBackPressed();
                break;
            case R.id.list_group:
                if(mListLayout.isShown()){
                    mListLayout.setVisibility(View.GONE);
                    mListImg.setImageResource(R.drawable.file_down);
                }else {
                    mListLayout.setVisibility(View.VISIBLE);
                    mListImg.setImageResource(R.drawable.file_up);
                }
                break;
            case R.id.list_layout:
                mListLayout.setVisibility(View.GONE);
                mListImg.setImageResource(R.drawable.file_down);
                break;
            case R.id.finish:
                Intent intent = new Intent();
                intent.putStringArrayListExtra(PICK_PATH,gridPictureAdapter.getSelect());
                setResult(Activity.RESULT_OK,intent);
                onBackPressed();
                break;
        }
    }
}
