package com.flyer.chat.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;
import com.haibin.calendarview.CalendarView;

/**
 * Created by mike.li on 2020/6/23.
 */
public class TestCalendarActivity extends ToolbarActivity {
    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, TestCalendarActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_calendar);
        setToolbarMiddleText("日历组件");
        CalendarView calendarView = findViewById(R.id.calendar_view);
    }
}
