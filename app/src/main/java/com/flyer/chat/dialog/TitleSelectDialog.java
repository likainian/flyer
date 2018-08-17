package com.flyer.chat.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mike.li on 2018/8/17.
 */

public class TitleSelectDialog extends AlertDialog {
    private Context context;
    private List<String> data;
    private OnSelectListener onSelectListener;
    public interface OnSelectListener{
        void OnSelect(int position);
    }
    public TitleSelectDialog(@NonNull Context context, List<String> data,OnSelectListener onSelectListener) {
        super(context,R.style.SelectDialog);
        this.context = context;
        this.data = data;
        this.onSelectListener =onSelectListener;
    }
    public TitleSelectDialog(@NonNull Context context,String[] data,OnSelectListener onSelectListener) {
        super(context,R.style.SelectDialog);
        this.data = Arrays.asList(data);
        this.context = context;
        this.onSelectListener =onSelectListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_title_layout);
        RecyclerView mSelectList = findViewById(R.id.select_list);
        if (mSelectList != null) {
            mSelectList.setLayoutManager(new LinearLayoutManager(context));
            SelectAdapter adapter = new SelectAdapter(data);
            mSelectList.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    if(onSelectListener!=null)onSelectListener.OnSelect(position);
                }
            });
        }
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.anim_y_bottom);
            window.setGravity(Gravity.BOTTOM);
        }
    }

    private class SelectAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

        private SelectAdapter(List<String> data) {
            super(R.layout.item_dialog_select,data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.btn_menu,item);
            helper.setVisible(R.id.select_line,helper.getAdapterPosition()!=getItemCount()-1);
        }
    }
}
