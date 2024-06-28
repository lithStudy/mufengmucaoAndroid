package com.mufengmucao.remind.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.mufengmucao.remind.helper.MedicalStateDataUtil;

public class MedicalRemindAlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        //判断今日是否已用药
        if (!MedicalStateDataUtil.readDoneStateForToday(context)) {
            //发送通知消息，提醒用药
            MedicalRemindSender.send(context);
        }
    }
}
