package com.flyer.chat.widget.calendarView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.flyer.chat.R;
import com.flyer.chat.util.CommonUtil;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

import java.util.List;

/**
 * 自定义布局
 */

public final class CustomMonthView extends MonthView {
    private int mPadding;

    public CustomMonthView(Context context) {
        super(context);
        mPadding = dipToPx(getContext(), 6);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setColor(0xff2AC3A4);
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mItemHeight/2-mPadding, mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        List<Calendar.Scheme> schemes = calendar.getSchemes();
        if(schemes!=null&&schemes.size()!=0){
            for (int i = 0; i < schemes.size(); i++) {
                Calendar.Scheme scheme = schemes.get(i);
                int itemRadius = dipToPx(getContext(), 3);
                int itemPadding = dipToPx(getContext(), 4);
                int left = mItemWidth/2-itemPadding*(schemes.size()-1)+itemPadding*i*2;
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(scheme.getShcemeColor());
                canvas.drawCircle(x+left,y+mItemHeight-itemPadding,itemRadius,paint);
            }
        }
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 6;
        //指定字体颜色，覆盖关系。异常>工作日>普通
        //普通灰色
        mCurMonthTextPaint.setColor(0xffB8B8B8);
        mCurMonthLunarTextPaint.setColor(0xffB8B8B8);
        //工作日黑色
        if(!calendar.isWeekend()){
            mCurMonthTextPaint.setColor(CommonUtil.getColor(R.color.color_666666));//日历字体颜色
            mCurMonthLunarTextPaint.setColor(calendar.getSchemeColor());//农历字体颜色
        }
        canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + y, mCurMonthTextPaint);
//        canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + y + mItemHeight, mCurMonthLunarTextPaint);
        //当前日画一个背景标记
        if (calendar.isCurrentDay()) {
            mSelectedPaint.setColor(0x402AC3A4);
            canvas.drawCircle(cx, y+mItemHeight/2,mItemHeight/2-mPadding, mSelectedPaint);
        }
    }

    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
