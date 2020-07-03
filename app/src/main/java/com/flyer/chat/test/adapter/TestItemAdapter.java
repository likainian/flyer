package com.flyer.chat.test.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.util.ToastUtil;

/**
 * Created by mike.li on 2020/6/24.
 */
public class TestItemAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public TestItemAdapter() {
        super(R.layout.item_test_item_view);
    }

    @Override
    protected void convert(BaseViewHolder helper, final String item) {
        helper.setText(R.id.tv_name,item);
        View view = helper.getView(R.id.tv_name);
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(item);
            }
        });
    }
}
