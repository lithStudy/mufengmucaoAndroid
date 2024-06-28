package com.mufengmucao.remind_test.notify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.mufengmucao.remind_test.R;
import com.mufengmucao.remind_test.constants.MyConstant;
import com.mufengmucao.remind_test.helper.MessageUtil;


public class MedicalRemindSender {
    public static void send(Context context) {
        // 1. 创建通知渠道（Android O及以上版本需要）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name); // 渠道名
            String description = context.getString(R.string.channel_description); // 渠道描述
            int importance = NotificationManager.IMPORTANCE_HIGH; // 重要性级别
            NotificationChannel channel = new NotificationChannel(MyConstant.MEDICAL_REMIND_CHANNEL_ID, name, importance); // 渠道ID
            channel.setDescription(description);
            // 注册渠道
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // 2. 构建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MyConstant.MEDICAL_REMIND_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground_color) // 通知图标
                .setContentTitle("用药提醒") // 通知标题
//                        .setContentText("Hello World!") // 通知内容
                .setPriority(NotificationCompat.PRIORITY_MAX); // 设置通知优先级


        int messageId = MessageUtil.getMedicalRemind();

        // 4. 添加第一个按钮
        Intent actionIntent1 = new Intent(context, MedicalRemindMessageDealReceiver.class);
        actionIntent1.setAction("done");
        actionIntent1.putExtra("NOTIFICATION_ID", messageId);
        PendingIntent actionPendingIntent1 = PendingIntent.getBroadcast(
                context,
                0,
                actionIntent1,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        );
        NotificationCompat.Action actionButton1 = new NotificationCompat.Action.Builder(
                R.drawable.ic_launcher_background, // 第一个按钮的图标
                "done", // 第一个按钮的文本
                actionPendingIntent1
        ).build();
        //4.2 添加按钮到通知
        builder.addAction(actionButton1);

        // 5. 触发通知
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(messageId, builder.build());
    }

}
