package com.flyer.chat.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;

/**
 * Created by mike.li on 2018/8/21.
 */

public class PickPictureActivity extends BaseActivity implements View.OnClickListener {
    public static final String PICK_PATH = "select";
    private int needCount;
    private TextView mToolbarMiddle;
    private RecyclerView mGridPicture;
    private RecyclerView mListPicture;
    private FrameLayout mListLayout;
    private TextView mGroupName;
    private ImageView mListImg;
    private TextView mFinish;

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
    }

    @Override
    public void onClick(View view) {

    }
}
