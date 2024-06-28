package com.mufengmucao.remind.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationManagerCompat;
import com.mufengmucao.remind.constants.MyConstant;
import com.mufengmucao.remind.helper.MedicalStateDataUtil;

public class MedicalRemindMessageDealReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if ("done".equals(intent.getAction())) {
            //将用药更新为完成状态
            MedicalStateDataUtil.writeDoneStateForToday(context, true);

            // 清除消息通知
            int notificationId = intent.getIntExtra("NOTIFICATION_ID", 0); // 默认值0，如果没有提供
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(notificationId);


            // 通知磁贴更新状态
            Intent tileIntent = new Intent(MyConstant.ACTION_MESSAGE_UPDATE);
            context.sendBroadcast(tileIntent);
        } else if ("cancel".equals(intent.getAction())) {
            // 执行按钮2相关的逻辑
            // ...
        }
    }
}
