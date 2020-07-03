package com.flyer.chat.test.adapter;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.dialog.TimePickerDialog;
import com.flyer.chat.dialog.TimePickerHalfDialog;
import com.flyer.chat.util.TimeUtil;

import java.util.List;

/**
 * Created by mike.li on 2020/6/19.
 */
public class TestTimeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Context context;
    private TimePickerView dialog;
    public TestTimeAdapter(Context context, List<String> data) {
        super(R.layout.item_test_time_view, data);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        helper.setText(R.id.tv_time, TimeUtil.longToString(System.currentTimeMillis(),item));
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (item){
                    case TimeUtil.FORMAT_YEAR_MONTH:
                        dialog = new TimePickerDialog(context)
                                .setFormat(TimePickerDialog.FORMAT_YEAR_MONTH)
                                .setOnTimeSelectListener(new TimePickerDialog.OnTimeSelectListener() {
                                    @Override
                                    public void onTimeSelect(long time) {
                                        helper.setText(R.id.tv_time, TimeUtil.longToString(time,item));
                                        dialog.dismiss();
                                    }
                                })
                                .build();
                        dialog.show();
                        break;
                    case TimeUtil.FORMAT_YEAR_MONTH_DAY:
                        dialog = new TimePickerDialog(context)
                                .setFormat(TimePickerDialog.FORMAT_YEAR_MONTH_DAY)
                                .setOnTimeSelectListener(new TimePickerDialog.OnTimeSelectListener() {
                                    @Override
                                    public void onTimeSelect(long time) {
                                        helper.setText(R.id.tv_time, TimeUtil.longToString(time,item));
                                        dialog.dismiss();
                                    }
                                })
                                .build();
                        dialog.show();
                        break;
                    case TimeUtil.FORMAT_YEAR_MONTH_DAY_TIME:
                        dialog = new TimePickerDialog(context)
                                .setFormat(TimePickerDialog.FORMAT_YEAR_MONTH_DAY_TIME)
                                .setOnTimeSelectListener(new TimePickerDialog.OnTimeSelectListener() {
                                    @Override
                                    public void onTimeSelect(long time) {
                                        helper.setText(R.id.tv_time, TimeUtil.longToString(time,item));
                                        dialog.dismiss();
                                    }
                                })
                                .build();
                        dialog.show();
                        break;
                    case TimeUtil.FORMAT_TIME:
                        dialog = new TimePickerDialog(context)
                                .setFormat(TimePickerDialog.FORMAT_TIME)
                                .setOnTimeSelectListener(new TimePickerDialog.OnTimeSelectListener() {
                                    @Override
                                    public void onTimeSelect(long time) {
                                        helper.setText(R.id.tv_time, TimeUtil.longToString(time,item));
                                        dialog.dismiss();
                                    }
                                })
                                .build();
                        dialog.show();
                        break;
                    case TimeUtil.FORMAT_YEAR_MONTH_DAY_APM:
                        dialog = new TimePickerHalfDialog(context)
                                .setHalfFormat(TimePickerHalfDialog.FORMAT_YEAR_MONTH_DAY_APM)
                                .setOnTimeSelectListener(new TimePickerDialog.OnTimeSelectListener() {
                                    @Override
                                    public void onTimeSelect(long time) {
                                        helper.setText(R.id.tv_time, TimeUtil.longToString(time,item));
                                        dialog.dismiss();
                                    }
                                })
                                .build();
                        dialog.show();
                        break;
                }
            }
        });
    }
}
