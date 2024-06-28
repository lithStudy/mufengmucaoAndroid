package com.mufengmucao.remind_test.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.mufengmucao.remind_test.notify.MedicalRemindAlarmReceiver;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

public class AlarmHelper {
    public static void resetAlarm(Context context) {
        // 取消之前的闹钟（如果有）
        cancelExistingAlarm(context);

        // 然后根据最新的设置重新设置闹钟
        alarm(context);
    }

    public static void cancelExistingAlarm(Context context) {
        Intent intent = new Intent(context, MedicalRemindAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE);
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }


    public static void alarm(Context context) {
        // 设置触发时间（比如今天晚上11点）
        Calendar remindCalendar = loadSavedTime(context);
        // 如果当前时间已经过了今天设置的时间，则设置明天
        if (Calendar.getInstance().after(remindCalendar)) {
            remindCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        //初次提醒
        alarmIntent(context, remindCalendar,0);
        // 设置提醒时间间隔（比如每分钟提醒一次）
        int remindInterval = 15;
        // 设置重复提醒次数
        int maxRemindTimes = 3;
        for (int remindTime = 1; remindTime < maxRemindTimes; remindTime++) {
            remindCalendar.add(Calendar.MINUTE, remindInterval);
            alarmIntent(context, remindCalendar,remindTime);
        }
    }

    private static void alarmIntent(Context context,final Calendar remindCalendar,int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // 创建Intent和PendingIntent用于第二个闹钟
        Intent intentSecondAlarm = new Intent(context, MedicalRemindAlarmReceiver.class); // 或者定义不同的Receiver处理不同事件
        PendingIntent pendingIntentSecondAlarm = PendingIntent.getBroadcast(context, requestCode, intentSecondAlarm, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        // 在原始时间点后15分钟设置第二个闹钟
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, remindCalendar.getTimeInMillis(), pendingIntentSecondAlarm);
    }


    public static Calendar loadSavedTime(Context context) {
        Calendar calendar = Calendar.getInstance();

        SharedPreferences sharedPreferences = context.getSharedPreferences("AppSettings", MODE_PRIVATE);
        int savedHour = sharedPreferences.getInt("Hour", 22); // 使用22:30作为默认值
        int savedMinute = sharedPreferences.getInt("Minute", 20);

        calendar.set(Calendar.HOUR_OF_DAY, savedHour);
        calendar.set(Calendar.MINUTE, savedMinute);
        calendar.set(Calendar.SECOND, 0);
        // 成功从SharedPreferences中加载了时间，可以在这里根据需要使用这两个值
        Log.d("TimeLoad", "Loaded time: " + savedHour + ":" + savedMinute);

        return calendar;
    }

    /**
     * 保存提醒时间
     * @param context
     * @param hourOfDay
     * @param minute
     */
    public static void saveTime(Context context,int hourOfDay, int minute) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Hour", hourOfDay);
        editor.putInt("Minute", minute);
        editor.apply();

        // 显示保存成功信息或者做其他事情
        Toast.makeText(context, "时间保存成功", Toast.LENGTH_SHORT).show();
    }
}
