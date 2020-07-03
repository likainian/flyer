package com.flyer.chat.dialog;

import android.content.Context;
import android.support.annotation.StringDef;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.flyer.chat.R;
import com.flyer.chat.util.CommonUtil;
import com.flyer.chat.util.TimeUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mike.li on 2020/6/22.
 */
public class TimePickerDialog extends TimePickerBuilder {
    public final static String FORMAT_YEAR_MONTH = "yyyy-MM";
    public final static String FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";
    public final static String FORMAT_YEAR_MONTH_DAY_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_TIME = "HH:mm";
    public TextView tvTime;
    public Date dateTime = new Date();

    @StringDef({FORMAT_YEAR_MONTH, FORMAT_YEAR_MONTH_DAY, FORMAT_YEAR_MONTH_DAY_TIME, FORMAT_TIME})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TimeUnit {}

    public OnTimeSelectListener onTimeSelectListener;
    public interface OnTimeSelectListener {
        void onTimeSelect(long time);
    }

    public TimePickerDialog(Context context) {
        super(context, null);
    }

    @Override
    public TimePickerBuilder setDate(Calendar date) {
        dateTime = date.getTime();
        if(tvTime!=null)tvTime.setText(TimeUtil.longToYearMonthDayWeek(dateTime.getTime()));
        return super.setDate(date);
    }

    public TimePickerDialog setFormat(@TimeUnit String format){
        if(FORMAT_YEAR_MONTH.equals(format)){
            setType(new boolean[]{true, true, false, false, false, false});
        }else if(FORMAT_YEAR_MONTH_DAY.equals(format)){
            setType(new boolean[]{true, true, true, false, false, false});
        }else if(FORMAT_YEAR_MONTH_DAY_TIME.equals(format)){
            setType(new boolean[]{true, true, true, true, true, false});
        }else if(FORMAT_TIME.equals(format)){
            setType(new boolean[]{false, false, false, true, true, false});
        }else {
            setType(new boolean[]{true, true, true, true, true, true});
        }
        setSubmitColor(CommonUtil.getColor(R.color.colorPrimary));//确定按钮文字颜色
        setCancelColor(CommonUtil.getColor(R.color.colorPrimary));//取消按钮文字颜色
        setLayoutRes(R.layout.view_time_picker, new CustomListener() {
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

    public TimePickerDialog setOnTimeSelectListener(OnTimeSelectListener onTimeSelectListener) {
        this.onTimeSelectListener = onTimeSelectListener;
        return this;
    }

    @Override
    public TimePickerDialog setTimeSelectChangeListener(final OnTimeSelectChangeListener listener) {
        super.setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                dateTime = date;
                if(tvTime!=null)tvTime.setText(TimeUtil.longToYearMonthDayWeek(dateTime.getTime()));
                if(listener!=null)listener.onTimeSelectChanged(date);
            }
        });
        return this;
    }
}