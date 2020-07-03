package com.flyer.chat.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.test.adapter.TestPictureAdapter;

import java.util.ArrayList;

/**
 * Created by mike.li on 2020/6/23.
 */
public class TestPictureActivity extends ToolbarActivity {
    public static void startActivity(Context context){
        context.startActivity(new Intent(context,TestPictureActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_picture);
        setToolbarMiddleText("图片处理");
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        TestPictureAdapter testItemAdapter = new TestPictureAdapter(this);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("原图片");
        strings.add("灰图片");
        strings.add("模糊图片");
        strings.add("圆角图片");
        strings.add("上圆角图片");
        strings.add("圆形图片");
        strings.add("椭圆图片");
        strings.add("描边图片");
        testItemAdapter.setNewData(strings);
        mRecyclerView.setAdapter(testItemAdapter);
    }
}
