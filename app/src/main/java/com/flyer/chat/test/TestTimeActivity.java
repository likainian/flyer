package com.flyer.chat.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.test.adapter.TestTimeAdapter;
import com.flyer.chat.util.TimeUtil;

import java.util.ArrayList;

/**
 * Created by mike.li on 2020/6/18.
 */
public class TestTimeActivity extends ToolbarActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, TestTimeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_time);
        setToolbarMiddleText("日期组件");
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<String> strings = new ArrayList<>();
        strings.add(TimeUtil.FORMAT_YEAR_MONTH);
        strings.add(TimeUtil.FORMAT_YEAR_MONTH_DAY);
        strings.add(TimeUtil.FORMAT_YEAR_MONTH_DAY_APM);
        strings.add(TimeUtil.FORMAT_YEAR_MONTH_DAY_TIME);
        strings.add(TimeUtil.FORMAT_TIME);
        mRecyclerView.setAdapter(new TestTimeAdapter(this,strings));
    }

}
