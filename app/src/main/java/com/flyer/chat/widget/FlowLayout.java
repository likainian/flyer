package com.flyer.chat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike.li on 2018/5/23.
 */

public class FlowLayout extends ViewGroup {
    private Context context;
    //所有的tab
    private List<String> allFlowTab = new ArrayList<>();
    //选中的tab
    private List<String> selectFlowTab = new ArrayList<>();

    public FlowLayout(Context context) {
        super(context);
        this.context = context;
    }
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // wrap_content
        int width = 0;
        int height = 0;

        int lineWidth = 0;
        //行高
        int lineHeight = 0;

        int cCount = getChildCount();
        //循环子类的宽度
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                if (i == cCount - 1) {
                    width = Math.max(lineWidth, width);
                    height += lineHeight;
                }
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            if (i == cCount - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }
        setMeasuredDimension(
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom()
        );
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int myWidth = r - l;

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();

        int childLeft = paddingLeft;
        int childTop = paddingTop;

        int lineHeight = 0;

        //根据子控件的宽高，计算子控件应该出现的位置。
        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View childView = getChildAt(i);

            if (childView.getVisibility() == View.GONE) {
                continue;
            }

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > myWidth) {
                childLeft = paddingLeft;
                childTop += lineHeight;
                lineHeight = childHeight;
            }
            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childLeft += childWidth;
        }
    }
    //设置tab的TextView的id
    public void setAllFlowTab(List<String> allTab){
        if(allTab == null)return;
        removeAllViews();
        this.allFlowTab = allTab;
        for (int i = 0; i < allFlowTab.size(); i++) {
            View ch = LayoutInflater.from(context).inflate(R.layout.item_tag_filter_layout,null);
            final ImageView imageView = (ImageView) ch.findViewById(R.id.iv_selected);
            final TextView textView = (TextView) ch.findViewById(R.id.tv_type_name);
            textView.setText(allFlowTab.get(i));
            final int finalI = i;
            //标签点击
            ch.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectFlowTab.contains(allFlowTab.get(finalI))){
                        selectFlowTab.remove(allFlowTab.get(finalI));
                        textView.setBackgroundResource(R.drawable.bg_r10_f0f0f0);
                        textView.setTextColor(CommonUtil.getColor(R.color.color_666666));
                        imageView.setVisibility(View.GONE);
                    }else {
                        selectFlowTab.add(allFlowTab.get(finalI));
                        textView.setBackgroundResource(R.drawable.bg_r10_e1faf4);
                        textView.setTextColor(CommonUtil.getColor(R.color.colorPrimary));
                        imageView.setVisibility(View.VISIBLE);
                    }
                    if(onFlowClickListener!=null)onFlowClickListener.onTagClick(allFlowTab.get(finalI));
                }
            });
            addView(ch);
        }
    }
    //设置选中的
    public void setSelectFlowTab(ArrayList<String> selectTab){
        if(selectTab ==null)return;
        this.selectFlowTab = selectTab;
        for (int i = 0; i < allFlowTab.size(); i++) {
            View ch = getChildAt(i);
            final ImageView imageView = (ImageView) ch.findViewById(R.id.iv_selected);
            final TextView textView = (TextView) ch.findViewById(R.id.tv_type_name);
            if(selectFlowTab.contains(allFlowTab.get(i))){
                textView.setBackgroundResource(R.drawable.bg_r10_e1faf4);
                textView.setTextColor(CommonUtil.getColor(R.color.colorPrimary));
                imageView.setVisibility(View.VISIBLE);
            }else {
                textView.setBackgroundResource(R.drawable.bg_r10_f0f0f0);
                textView.setTextColor(CommonUtil.getColor(R.color.color_666666));
                imageView.setVisibility(View.GONE);
            }
        }
    }

    //获取选中的
    public List<String> getSelectedList() {
        return selectFlowTab;
    }

    private OnFlowClickListener onFlowClickListener;

    public void setOnFlowClickListener(OnFlowClickListener onFlowClickListener) {
        this.onFlowClickListener = onFlowClickListener;
    }

    public interface OnFlowClickListener{
        void onTagClick(String mapEntity);
    }

}
