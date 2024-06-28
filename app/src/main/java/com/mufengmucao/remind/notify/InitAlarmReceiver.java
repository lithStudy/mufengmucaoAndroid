package com.mufengmucao.remind.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.mufengmucao.remind.constants.MyConstant;
import com.mufengmucao.remind.helper.MedicalStateDataUtil;

public class InitAlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        //初始化当日用药记录
        MedicalStateDataUtil.writeDoneStateForToday(context,false);

        //广播消息：初始化
        Intent tileIntent = new Intent(MyConstant.ACTION_INIT);
        context.sendBroadcast(tileIntent);
    }
}
