package com.flyer.chat.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.util.DeviceUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mike.li on 2018/8/17.
 */

public class TitleSelectDialog extends AlertDialog {
    private Context context;
    private OnSelectListener onSelectListener;
    private String title;
    private List<String> data;
    private String check;

    public interface OnSelectListener{
        void OnSelect(int position);
    }
    public TitleSelectDialog(@NonNull Context context) {
        super(context,R.style.SelectDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_title_layout);
        RecyclerView mSelectList = findViewById(R.id.select_list);
        TextView mTvTopText = findViewById(R.id.tv_top_text);
        if(mTvTopText !=null) mTvTopText.setText(title);
        if (mSelectList != null) {
            mSelectList.setLayoutManager(new LinearLayoutManager(context));
            SelectAdapter adapter = new SelectAdapter();
            adapter.setNewData(data);
            mSelectList.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    dismiss();
                    if(onSelectListener!=null)onSelectListener.OnSelect(position);
                }
            });
        }
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.windowAnimations = R.style.anim_y_bottom;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getWindow().getDecorView().setPadding(DeviceUtil.dip2px(context,20), 0,
                    DeviceUtil.dip2px(context,20), DeviceUtil.dip2px(context,20));
            getWindow().setAttributes(layoutParams);
        }
    }

    public TitleSelectDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public TitleSelectDialog setList(List<String> data){
        this.data = data;
        return this;
    }

    public TitleSelectDialog setArray(String[] data){
        this.data = Arrays.asList(data);
        return this;
    }

    public TitleSelectDialog setCheck(String check){
        this.check = check;
        return this;
    }

    public TitleSelectDialog setOnSelectListener(OnSelectListener onSelectListener){
        this.onSelectListener =onSelectListener;
        return this;
    }

    private class SelectAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

        private SelectAdapter() {
            super(R.layout.item_dialog_title_select);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.btn_menu,item);
            helper.setVisible(R.id.phone_check, TextUtils.equals(item,check));
            helper.setVisible(R.id.select_line,helper.getAdapterPosition()!=getItemCount()-1);
        }
    }
}
