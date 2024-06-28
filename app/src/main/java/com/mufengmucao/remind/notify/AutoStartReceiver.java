package com.mufengmucao.remind.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.mufengmucao.remind.MainActivity;
import com.mufengmucao.remind.tile.MedicalRemindTileService;

/**
 * 开机启动监听事件，用于自启动服务
 */
public class AutoStartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // 应用程序在开机后执行的逻辑
            // 例如启动服务或者Activity
            Intent serviceIntent = new Intent(context, MedicalRemindTileService.class);
            context.startService(serviceIntent);
            // 或者
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);
        }
    }
}