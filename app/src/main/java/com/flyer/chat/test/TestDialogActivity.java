package com.flyer.chat.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.dialog.NoticeDialog;
import com.flyer.chat.dialog.SelectDialog;
import com.flyer.chat.dialog.TitleSelectDialog;

import java.lang.reflect.Field;

/**
 * Created by mike.li on 2020/6/30.
 */
public class TestDialogActivity extends ToolbarActivity implements View.OnClickListener {
    private TextView mTvTop;
    private TextView mTvMid;
    private TextView mTvBottom;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, TestDialogActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dialog);
        setToolbarMiddleText("弹窗组件");
        initView();
        setToolbarRightText("菜单");
        setToolbarRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopPopUpMenu(v);
            }
        });
    }
    private void initView() {
        mTvTop = (TextView) findViewById(R.id.tv_top);
        mTvMid = (TextView) findViewById(R.id.tv_mid);
        mTvBottom = (TextView) findViewById(R.id.tv_bottom);
        mTvTop.setOnClickListener(this);
        mTvMid.setOnClickListener(this);
        mTvBottom.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_top:
                new TitleSelectDialog(this)
                        .setArray(new String[]{"选项1","选项1"})
                        .show();
                break;
            case R.id.tv_mid:
                new NoticeDialog(this).setTitle("标题")
                        .setMessage("内容")
                        .show();
                break;
            case R.id.tv_bottom:
                new SelectDialog(this)
                        .setArray(new String[]{"选项1","选项1"})
                        .show();
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    private void shopPopUpMenu(View view) {
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
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                onMenuItemSelected(item);
                return true;
            }
        });
    }

    private void onMenuItemSelected(MenuItem item) {
//        int menuItemId = item.getItemId();
//        if (menuItemId == R.id.menu1) {
//            mSmartRefresh.setRefreshHeader(new ClassicsHeader(this));
//            mSmartRefresh.setRefreshFooter(new ClassicsFooter(this));
//        } else if (menuItemId == R.id.menu2) {
//            mSmartRefresh.setRefreshHeader(new BezierRadarHeader(this));
//            mSmartRefresh.setRefreshFooter(new BallPulseFooter(this));
//        }
    }



}
