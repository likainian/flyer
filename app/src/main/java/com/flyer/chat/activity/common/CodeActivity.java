package com.flyer.chat.activity.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyer.chat.R;
import com.flyer.chat.activity.account.bean.User;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.CodeUtil;

import cn.bmob.v3.BmobUser;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/8/25.
 */

public class CodeActivity extends ToolbarActivity{
    private ImageView codeView;

    public static void startActivity(Context context){
        context.startActivity(new Intent(context, CodeActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        setToolbarMiddleText("二维码");
        codeView = findViewById(R.id.code_view);
        TextView tvName = findViewById(R.id.tv_name);
        final User user = BmobUser.getCurrentUser(User.class);
        tvName.setText(user.getMobilePhoneNumber());
        if(CheckUtil.isNotEmpty(user.getNikeName())){
            tvName.append("("+user.getNikeName()+")");
        }
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Bitmap>() {

            @Override
            public void subscribe(ObservableEmitter<Bitmap> emitter){
                Bitmap code = CodeUtil.encode("user："+user.getObjectId());
                emitter.onNext(code);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap){
                codeView.setImageBitmap(bitmap);
            }
        });
    }

    //添加二维码中心logo
    private Bitmap addLogo(Bitmap qrBitmap, Bitmap logoBitmap) {
        if (null==logoBitmap){
            Toast.makeText(CodeActivity.this,"所选图片为空!",Toast.LENGTH_SHORT).show();
            return null;
        }
        int qrBitmapWidth = qrBitmap.getWidth();
        int qrBitmapHeight = qrBitmap.getHeight();
        int logoBitmapWidth = logoBitmap.getWidth();
        int logoBitmapHeight = logoBitmap.getHeight();
        Bitmap blankBitmap = Bitmap.createBitmap(qrBitmapWidth, qrBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(blankBitmap);
        canvas.drawBitmap(qrBitmap, 0, 0, null);
        canvas.save();
        float scaleSize = 1.0f;
        while ((logoBitmapWidth / scaleSize) > (qrBitmapWidth / 5) || (logoBitmapHeight / scaleSize) > (qrBitmapHeight / 5)) {
            scaleSize *= 2;
        }
        float sx = 1.0f / scaleSize;
        canvas.scale(sx, sx, qrBitmapWidth / 2, qrBitmapHeight / 2);
        canvas.drawBitmap(logoBitmap, (qrBitmapWidth - logoBitmapWidth) / 2, (qrBitmapHeight - logoBitmapHeight) / 2, null);
        canvas.restore();
        return blankBitmap;
    }

}
