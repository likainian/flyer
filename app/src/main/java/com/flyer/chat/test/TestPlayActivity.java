package com.flyer.chat.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.test.adapter.TestPlayAdapter;
import com.flyer.chat.util.DataUtil;

/**
 * Created by mike.li on 2020/7/8.
 */
public class TestPlayActivity extends ToolbarActivity {
    public static void startActivity(Context context){
        context.startActivity(new Intent(context,TestPlayActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_play);
        setToolbarMiddleText("视频播放");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        TestPlayAdapter testPlayAdapter = new TestPlayAdapter(this);
        recyclerView.setAdapter(testPlayAdapter);
        testPlayAdapter.setNewData(DataUtil.getVideoList());
    }
}
