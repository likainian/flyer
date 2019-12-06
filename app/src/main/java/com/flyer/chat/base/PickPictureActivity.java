package com.flyer.chat.base;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
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
import com.flyer.chat.base.adapter.GridPictureAdapter;
import com.flyer.chat.base.adapter.ListPictureAdapter;
import com.flyer.chat.base.bean.PictureMap;
import com.flyer.chat.util.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by mike.li on 2018/8/21.
 */

public class PickPictureActivity extends ToolbarActivity implements View.OnClickListener {
    private static Disposable disposable;
    public static final int REQUEST_CODE = 100;
    public static final int RESULT_CODE = 101;
    public static final String PICK_PATH = "PICK_PATH";
    public static final String NEED_COUNT = "NEED_COUNT";
    private int needCount;
    private RecyclerView mRecyclerGrid;
    private RecyclerView mRecyclerList;
    private FrameLayout mFlList;
    private TextView mTvGroupName;
    private ImageView mIvGroupImg;
    private TextView mTvFinish;
    private List<PictureMap> pictureMaps = new ArrayList<>();
    private GridPictureAdapter gridPictureAdapter;
    private ListPictureAdapter listPictureAdapter;

    public static void startActivityForResult(final FragmentActivity context, final int needCount) {
        disposable = new RxPermissions(context).requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission){
                if(permission.granted){
                    Intent intent = new Intent(context, PickPictureActivity.class);
                    intent.putExtra(NEED_COUNT, needCount);
                    context.startActivityForResult(intent,REQUEST_CODE);
                }else {
                    ToastUtil.showToast("请开启读写权限");
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_picture);
        needCount = getIntent().getIntExtra(NEED_COUNT, 9);
        initView();
        initAdapter();
        initData();
    }

    @Override
    protected void onDestroy() {
        if(disposable!=null)disposable.dispose();
        super.onDestroy();
    }

    private void initView() {
        mRecyclerGrid = findViewById(R.id.recycler_grid);
        mRecyclerList = findViewById(R.id.recycler_list);
        mFlList = findViewById(R.id.fl_list);
        LinearLayout mLlGroup = findViewById(R.id.ll_group);
        mTvGroupName = findViewById(R.id.tv_group_name);
        mIvGroupImg = findViewById(R.id.iv_group_img);
        mTvFinish = findViewById(R.id.tv_finish);
        mFlList.setVisibility(View.GONE);
        mIvGroupImg.setImageResource(R.drawable.file_down);
        mLlGroup.setOnClickListener(this);
        mFlList.setOnClickListener(this);
        mTvFinish.setText(String.format(getString(R.string.finish_of), 0, needCount));
        mTvFinish.setOnClickListener(this);
    }

    private void initAdapter() {
        mRecyclerGrid.setLayoutManager(new GridLayoutManager(this,4));
        gridPictureAdapter = new GridPictureAdapter(this,needCount);
        mRecyclerGrid.setAdapter(gridPictureAdapter);
        gridPictureAdapter.setOnPictureClickListener(new GridPictureAdapter.OnPictureClickListener() {
            @Override
            public void onPictureClick(int selectCount) {
                mTvFinish.setText(String.format(getString(R.string.finish_of), selectCount, needCount));
            }
        });

        mRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        listPictureAdapter = new ListPictureAdapter(this);
        mRecyclerList.setAdapter(listPictureAdapter);
        listPictureAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mFlList.setVisibility(View.GONE);
                mIvGroupImg.setImageResource(R.drawable.file_down);
                PictureMap pictureMap = pictureMaps.get(position);
                mTvGroupName.setText(pictureMap.getName());
                setToolbarMiddleText(pictureMap.getName());
                gridPictureAdapter.setNewData(pictureMap.getFiles());
            }
        });

    }

    private void initData() {
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
                mTvGroupName.setVisibility(View.VISIBLE);
            }else {
                mTvGroupName.setVisibility(View.GONE);
            }
            Set<Map.Entry<String, List<File>>> entries = imgGroup.entrySet();
            for (Map.Entry<String, List<File>> entry:entries){
                PictureMap pictureMap = new PictureMap(entry.getKey(), entry.getValue());
                pictureMaps.add(pictureMap);
            }
            PictureMap pictureMap = new PictureMap("所有图片", files);
            pictureMaps.add(0,pictureMap);
            listPictureAdapter.setNewData(pictureMaps);
            mTvGroupName.setText(pictureMap.getName());
            setToolbarMiddleText(pictureMap.getName());
            gridPictureAdapter.setNewData(pictureMap.getFiles());
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_group:
                if(mFlList.isShown()){
                    mFlList.setVisibility(View.GONE);
                    mIvGroupImg.setImageResource(R.drawable.file_down);
                }else {
                    mFlList.setVisibility(View.VISIBLE);
                    mIvGroupImg.setImageResource(R.drawable.file_up);
                }
                break;
            case R.id.fl_list:
                mFlList.setVisibility(View.GONE);
                mIvGroupImg.setImageResource(R.drawable.file_down);
                break;
            case R.id.tv_finish:
                Intent intent = new Intent();
                intent.putStringArrayListExtra(PICK_PATH,gridPictureAdapter.getSelect());
                setResult(RESULT_CODE,intent);
                onBackPressed();
                break;
        }
    }
}
