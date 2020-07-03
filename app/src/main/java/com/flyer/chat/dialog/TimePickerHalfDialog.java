package com.flyer.chat.dialog;

import android.content.Context;
import android.support.annotation.StringDef;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.flyer.chat.R;
import com.flyer.chat.util.CommonUtil;
import com.flyer.chat.util.TimeUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mike.li on 2020/6/22.
 */
public class TimePickerHalfDialog extends TimePickerDialog{
    public final static String FORMAT_YEAR_MONTH_DAY_APM = "yyyy-MM-dd a";

    @StringDef({FORMAT_YEAR_MONTH_DAY_APM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TimeHalfUnit {}
    public TimePickerHalfDialog(Context context) {
        super(context);
    }

    public TimePickerDialog setHalfFormat(@TimeHalfUnit String format){
        setAmPm(Calendar.AM);
        if(FORMAT_YEAR_MONTH_DAY_APM.equals(format)){
            setType(new boolean[]{true, true, true, false, false, false});
        }
        setSubmitColor(CommonUtil.getColor(R.color.colorPrimary));//确定按钮文字颜色
        setCancelColor(CommonUtil.getColor(R.color.colorPrimary));//取消按钮文字颜色
        setTimeSelectChangeListener(null);
        setLayoutRes(R.layout.view_time_picker_half, new CustomListener() {
            @Override
            public void customLayout(View v) {
                TextView tvFinish = v.findViewById(R.id.tv_finish);
                tvTime = v.findViewById(R.id.tv_time);
                tvTime.setText(TimeUtil.longToYearMonthDayWeek(dateTime.getTime()));
                tvFinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(onTimeSelectListener !=null){
                            onTimeSelectListener.onTimeSelect(dateTime.getTime());
                        }
                    }
                });

                WheelView half = v.findViewById(R.id.half);
                half.setVisibility(View.VISIBLE);
                final ArrayList<String> halfTypes = new ArrayList<>();
                halfTypes.add(CommonUtil.getString(R.string.text_am));
                halfTypes.add(CommonUtil.getString(R.string.text_pm));
                half.setCyclic(false);
                half.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(int index) {
                        if(index==0){
                            setAmPm(Calendar.AM);
                        }else {
                            setAmPm(Calendar.PM);
                        }
                    }
                });
                half.setAdapter(new WheelAdapter() {
                    @Override
                    public int getItemsCount() {
                        return 2;
                    }

                    @Override
                    public Object getItem(int index) {
                        return halfTypes.get(index);
                    }

                    @Override
                    public int indexOf(Object o) {
                        return 0;
                    }
                });
            }
        });

        setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                dateTime = date;
                if(tvTime!=null)tvTime.setText(TimeUtil.longToYearMonthDayWeek(dateTime.getTime()));
            }
        });
        return this;
    }

    public void setAmPm(int amPm){
        Calendar instance = Calendar.getInstance();
        instance.setTime(dateTime);
        instance.set(Calendar.AM_PM,amPm);
        dateTime = instance.getTime();
    }

}
