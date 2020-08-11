package com.flyer.chat.test.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.activity.WXScanActivity;
import com.flyer.chat.activity.feedback.QuestionActivity;
import com.flyer.chat.test.TestBannerActivity;
import com.flyer.chat.test.TestCalendarActivity;
import com.flyer.chat.test.TestCanvasActivity;
import com.flyer.chat.test.TestDialogActivity;
import com.flyer.chat.test.TestLiveActivity;
import com.flyer.chat.test.TestNotifyActivity;
import com.flyer.chat.test.TestPictureActivity;
import com.flyer.chat.test.TestPlayActivity;
import com.flyer.chat.test.TestRefreshActivity;
import com.flyer.chat.test.TestScanActivity;
import com.flyer.chat.test.TestSwipeActivity;
import com.flyer.chat.test.TestTimeActivity;
import com.flyer.chat.test.bean.TestBean;
import com.flyer.chat.zxing.android.CaptureActivity;

import java.util.List;

/**
 * Created by mike.li on 2020/6/18.
 */
public class TestAdapter extends BaseQuickAdapter<TestBean,BaseViewHolder> {
    private Context context;
    public TestAdapter(Context context,List<TestBean> data) {
        super(R.layout.item_test_view,data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final TestBean item) {
        helper.setText(R.id.tv_test_name,item.getName());
        ImageView tvTestImage = helper.getView(R.id.tv_test_image);
        tvTestImage.setImageResource(item.getResourceId());
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (item.getName()){
                    case "banner组件":
                        TestBannerActivity.startActivity(context);
                        break;
                    case "日期组件":
                        TestTimeActivity.startActivity(context);
                        break;
                    case "日历组件":
                        TestCalendarActivity.startActivity(context);
                        break;
                    case "图片处理":
                        TestPictureActivity.startActivity(context);
                        break;
                    case "自绘组件":
                        TestCanvasActivity.startActivity(context);
                        break;
                    case "朋友圈拖拽":
                        QuestionActivity.startActivity(context);
                        break;
                    case "侧滑组件":
                        TestSwipeActivity.startActivity(context);
                        break;
                    case "上下拉刷新组件":
                        TestRefreshActivity.startActivity(context);
                        break;
                    case "弹窗":
                        TestDialogActivity.startActivity(context);
                        break;
                    case "直播":
                        TestLiveActivity.startActivity(context);
                        break;
                    case "相机":
//                        TestCaptureActivity.startActivity((FragmentActivity) context);
                        TestScanActivity.startActivity((FragmentActivity) context);
                        break;
                    case "扫一扫":
                        Intent intent = new Intent(context, CaptureActivity.class);
                        context.startActivity(intent);
                        break;
                    case "全屏扫一扫":
                        Intent intent2 = new Intent(context, WXScanActivity.class);
                        context.startActivity(intent2);
                        break;
                    case "通知和推送":
                        TestNotifyActivity.startActivity(context);
                        break;
                    case "视频播放":
                        TestPlayActivity.startActivity(context);
                        break;
                }

            }
        });
    }
}
