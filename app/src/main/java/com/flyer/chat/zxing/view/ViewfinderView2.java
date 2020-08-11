
package com.flyer.chat.zxing.view;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.flyer.chat.R;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.zxing.bean.ZxingConfig;
import com.flyer.chat.zxing.camera.CameraManager;
import com.google.zxing.ResultPoint;

import java.util.ArrayList;
import java.util.List;

public final class ViewfinderView2 extends View {

    /*界面刷新间隔时间*/
    private static final long ANIMATION_DELAY = 80L;
    private static final int CURRENT_POINT_OPACITY = 0xA0;
    private static final int MAX_RESULT_POINTS = 20;
    private static final int POINT_SIZE = 6;

    private CameraManager cameraManager;
    private Paint paint, scanLinePaint, reactPaint, frameLinePaint;
    private Bitmap resultBitmap;
    private int maskColor; // 取景框外的背景颜色
    private int resultColor;// result Bitmap的颜色
    private int resultPointColor; // 特征点的颜色
    private int reactColor;//四个角的颜色
    private int scanLineColor;//扫描线的颜色
    private int frameLineColor = -1;//边框线的颜色


    private List<ResultPoint> possibleResultPoints;
    private List<ResultPoint> lastPossibleResultPoints;
    // 扫描线移动的y
    private int scanLineTop;

    private ZxingConfig config;
    private ValueAnimator valueAnimator;
    private Rect frame;


    public ViewfinderView2(Context context) {
        this(context, null);

    }

    public ViewfinderView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);


    }


    public void setZxingConfig(ZxingConfig config) {
        this.config = config;
        reactColor = ContextCompat.getColor(getContext(), config.getReactColor());

        if (config.getFrameLineColor() != -1) {
            frameLineColor = ContextCompat.getColor(getContext(), config.getFrameLineColor());
        }

        scanLineColor = ContextCompat.getColor(getContext(), config.getScanLineColor());
        initPaint();

    }


    public ViewfinderView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        maskColor = ContextCompat.getColor(getContext(), R.color.half_transparent);
        resultColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        resultPointColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);

        possibleResultPoints = new ArrayList<ResultPoint>(10);
        lastPossibleResultPoints = null;


    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        /*四个角的画笔*/
        reactPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        reactPaint.setColor(reactColor);
        reactPaint.setStyle(Paint.Style.FILL);

        /*边框线画笔*/

        if (frameLineColor != -1) {
            frameLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            frameLinePaint.setColor(ContextCompat.getColor(getContext(), config.getFrameLineColor()));
            frameLinePaint.setStrokeWidth(dp2px(1));
            frameLinePaint.setStyle(Paint.Style.STROKE);
        }



        /*扫描线画笔*/
        scanLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scanLinePaint.setStrokeWidth(dp2px(2));
        scanLinePaint.setStyle(Paint.Style.FILL);
        scanLinePaint.setDither(true);
        scanLinePaint.setColor(scanLineColor);

    }

    private void initAnimator() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(200, getHeight()-200);
            valueAnimator.setDuration(3000);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    scanLineTop = (int) animation.getAnimatedValue();
                    invalidate();

                }
            });

            valueAnimator.start();
        }


    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;


    }

    public void stopAnimator() {
        if (valueAnimator != null) {
            valueAnimator.end();
            valueAnimator.cancel();
            valueAnimator = null;
        }

    }

    @Override
    public void onDraw(Canvas canvas) {
        LogUtil.i("ttt","onDraw");
        if (cameraManager == null) {
            return;
        }

        // frame为取景框
        frame = cameraManager.getFramingRect();
        Rect previewFrame = cameraManager.getFramingRectInPreview();
        if (frame == null || previewFrame == null) {
            return;
        }
        initAnimator();

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            // 如果有二维码结果的Bitmap，在扫取景框内绘制不透明的result Bitmap
            LogUtil.i("ttt","resultBitmap != null");
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, frame, paint);
        } else {

            /*绘制扫描线*/
            drawScanLight(canvas);

            /*绘制闪动的点*/
            // drawPoint(canvas, frame, previewFrame);
        }
    }


    /**
     * 绘制移动扫描线
     *
     * @param canvas
     */
    private void drawScanLight(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scan_light);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect dst = new Rect(0, scanLineTop, getWidth(), scanLineTop+bitmap.getHeight());
        canvas.drawBitmap(bitmap,rect,dst,scanLinePaint);
    }

    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        LogUtil.i("ttt","添加："+point.getX()+":"+point.getY());
        List<ResultPoint> points = possibleResultPoints;
        points.add(point);
        int size = points.size();
        if (size > MAX_RESULT_POINTS) {
            // trim it
            points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
        }
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
