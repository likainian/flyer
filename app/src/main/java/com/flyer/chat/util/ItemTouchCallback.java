package com.flyer.chat.util;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.flyer.chat.activity.feedback.GridPhotoAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by mike.li on 2019/11/7.
 */
public class ItemTouchCallback extends ItemTouchHelper.Callback {
    private RecyclerView.Adapter adapter;
    private List<String> pictures;
    private TextView tvDelete;
    private boolean flaying;

    public ItemTouchCallback(RecyclerView.Adapter adapter, List<String> pictures, TextView tvDelete) {
        this.adapter = adapter;
        this.pictures = pictures;
        this.tvDelete = tvDelete;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //首先回调的方法 返回int表示是否监听该方向
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;//拖拽
        int swipeFlags = 0;//侧滑删除
        int fromPosition = viewHolder.getAdapterPosition();
        if(pictures.get(fromPosition).equals(GridPhotoAdapter.DEFAULT)){
            return 0;
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //滑动事件
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        //判断，第一个不会进行拖拽。如果想全部都能动，去掉该判断即可
        if (!pictures.get(fromPosition).equals(GridPhotoAdapter.DEFAULT) && !pictures.get(toPosition).equals(GridPhotoAdapter.DEFAULT)) {
            Collections.swap(pictures, fromPosition, toPosition);
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(pictures, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(pictures, i, i - 1);
                }
            }
            adapter.notifyItemMoved(fromPosition, toPosition);
        }
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    //当长按选中item的时候（拖拽开始的时候）调用
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //获取系统震动服务
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            int fromPosition = viewHolder.getAdapterPosition();
            if(pictures.get(fromPosition).equals(GridPhotoAdapter.DEFAULT)){
                return;
            }
            VibratorUtil.start();
            viewHolder.itemView.setScaleX(1.08f);
            viewHolder.itemView.setScaleY(1.08f);
            tvDelete.setVisibility(View.VISIBLE);
        }

    }

    //当手指松开的时候（拖拽完成的时候）调用
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setScaleX(1f);
        viewHolder.itemView.setScaleY(1f);
        tvDelete.setVisibility(View.GONE);
        flaying = false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if(dY+recyclerView.getTop()+viewHolder.itemView.getBottom()>tvDelete.getBottom()&&flaying){
            pictures.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        flaying = true;
        return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
    }
}
