package com.mufengmucao.remind;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import com.mufengmucao.remind.helper.AlarmHelper;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        timePicker = findViewById(R.id.timePicker);
        Button saveButton = findViewById(R.id.button_save);

        // 设置TimePicker为24小时视图
        timePicker.setIs24HourView(true);
        Calendar calendar = AlarmHelper.loadSavedTime(SettingsActivity.this);
        timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinute(calendar.get(Calendar.MINUTE));

        // 保存按钮点击事件
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmHelper.saveTime(SettingsActivity.this,timePicker.getHour(), timePicker.getMinute());
                AlarmHelper.resetAlarm(SettingsActivity.this);
            }
        });
    }


}