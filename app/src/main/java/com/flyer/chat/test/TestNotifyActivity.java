package com.flyer.chat.test;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.view.View;

import com.flyer.chat.R;
import com.flyer.chat.base.ToolbarActivity;

/**
 * Created by mike.li on 2020/7/3.
 */
public class TestNotifyActivity extends ToolbarActivity implements View.OnClickListener {
    private String CHANNEL_ID ="123";
    public static void startActivity(Context context){
        context.startActivity(new Intent(context,TestNotifyActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_notify);
        View tv_1 = findViewById(R.id.tv_1);
        tv_1.setOnClickListener(this);
        createNotificationChannel();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, TestNotifyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        switch (v.getId()){
            case R.id.tv_1:
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("标题")
                        .setContentText("内容")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notificationManager.notify(0, builder.build());
                break;
            case R.id.tv_2:
                NotificationCompat.Builder builder2 = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("标题")
                        .setContentText("内容")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Much longer text that cannot fit one line..."))
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                notificationManager.notify(1, builder2.build());
                break;
            case R.id.tv_3:
                String replyLabel = getResources().getString(R.string.app_name);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
                    RemoteInput remoteInput = new RemoteInput.Builder("KEY_TEXT_REPLY")
                            .setLabel(replyLabel)
                            .build();
                    NotificationCompat.Action action =
                            new NotificationCompat.Action.Builder(R.drawable.ic_launcher,
                                    getString(R.string.app_name), pendingIntent)
                                    .addRemoteInput(remoteInput)
                                    .build();
                    NotificationCompat.Builder builder3 = new NotificationCompat.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_launcher)
                            .setContentTitle("标题")
                            .setContentText("内容")
                            .addAction(action)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    notificationManager.notify(3, builder3.build());
                }
                break;
            case R.id.tv_4:
                Intent fullScreenIntent = new Intent(this, TestNotifyActivity.class);
                PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                        fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder4 = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setFullScreenIntent(fullScreenPendingIntent, true);
                notificationManager.notify(3, builder4.build());
                break;
        }
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
