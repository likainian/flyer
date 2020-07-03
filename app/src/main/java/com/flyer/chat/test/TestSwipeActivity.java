package com.flyer.chat.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.test.adapter.TestItemAdapter;
import com.flyer.chat.util.CommonUtil;
import com.flyer.chat.util.DeviceUtil;
import com.flyer.chat.util.ToastUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;

/**
 * Created by mike.li on 2020/6/29.
 */
public class TestSwipeActivity extends ToolbarActivity {
    public static void startActivity(Context context){
        context.startActivity(new Intent(context,TestSwipeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_swipe);
        setToolbarMiddleText("侧滑组件");
        SwipeMenuRecyclerView swipeRecyclerView = findViewById(R.id.swipe_recycler_view);
        swipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);
        // 设置监听器。
        swipeRecyclerView.setSwipeMenuCreator(new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                SwipeMenuItem editItem = new SwipeMenuItem(TestSwipeActivity.this)
                        .setText("编辑")
                        .setWidth(DeviceUtil.dip2px(TestSwipeActivity.this,50))
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                        .setBackgroundColor(CommonUtil.getColor(R.color.colorPrimary));
                leftMenu.addMenuItem(editItem); // 在Item左侧添加一个菜单。

                SwipeMenuItem deleteItem = new SwipeMenuItem(TestSwipeActivity.this)
                        .setText("删除")
                        .setTextSize(16)
                        .setImage(R.drawable.delete_all)
                        .setWidth(DeviceUtil.dip2px(TestSwipeActivity.this,50))
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                        .setBackgroundColor(CommonUtil.getColor(R.color.red));
                rightMenu.addMenuItem(deleteItem); // 在Item右侧添加一个菜单。

                // 注意：哪边不想要菜单，那么不要添加即可。
            }
        });
        TestItemAdapter testItemAdapter = new TestItemAdapter();
        swipeRecyclerView.setAdapter(testItemAdapter);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            strings.add("测试"+i);
        }
        testItemAdapter.setNewData(strings);
        testItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtil.showToast("点击"+position);
            }
        });
    }
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                ToastUtil.showToast("点击"+adapterPosition+"右侧"+menuPosition);
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                ToastUtil.showToast("点击"+adapterPosition+"左侧"+menuPosition);
            }
        }
    };
}
