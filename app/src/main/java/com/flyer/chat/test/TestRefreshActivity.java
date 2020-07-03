package com.flyer.chat.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.test.adapter.TestItemAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by mike.li on 2020/6/29.
 */
public class TestRefreshActivity extends ToolbarActivity {
    private TestItemAdapter testItemAdapter;
    private SmartRefreshLayout mSmartRefresh;
    public static void startActivity(Context context){
        context.startActivity(new Intent(context,TestRefreshActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_refresh);
        setToolbarMiddleText("刷新组件");
        setToolbarRightText("更多");
        setToolbarRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopPopUpMenu(v);
            }
        });
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSmartRefresh = (SmartRefreshLayout) findViewById(R.id.smart_refresh);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        testItemAdapter = new TestItemAdapter();
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strings.add("测试"+i);
        }
        testItemAdapter.setNewData(strings);
        mRecyclerView.setAdapter(testItemAdapter);
        mSmartRefresh.setEnableRefresh(true);
        mSmartRefresh.setEnableLoadMore(true);
        mSmartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                load();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });

        mSmartRefresh.setRefreshHeader(new ClassicsHeader(this));
        mSmartRefresh.setRefreshFooter(new ClassicsFooter(this));
    }

    public void refresh(){
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strings.add("刷新"+i);
        }
        testItemAdapter.addData(0,strings);
        mSmartRefresh.finishRefresh();
    }
    public void load(){
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strings.add("加载"+i);
        }
        testItemAdapter.addData(strings);
        mSmartRefresh.finishLoadMore();
    }

    @SuppressLint("RestrictedApi")
    private void shopPopUpMenu(View view){
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(this, view);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.text_menu, popupMenu.getMenu());
        popupMenu.show();
        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper helper = (MenuPopupHelper) field.get(popupMenu);
            helper.setForceShowIcon(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                onMenuItemSelected(item);
                return true;
            }
        });
    }

    private void onMenuItemSelected(MenuItem item) {
        int menuItemId = item.getItemId();
        if (menuItemId == R.id.menu1) {
            mSmartRefresh.setRefreshHeader(new ClassicsHeader(this));
            mSmartRefresh.setRefreshFooter(new ClassicsFooter(this));
        } else if (menuItemId == R.id.menu2) {
            mSmartRefresh.setRefreshHeader(new BezierRadarHeader(this));
            mSmartRefresh.setRefreshFooter(new BallPulseFooter(this));
        }
    }
}
