package com.mufengmucao.remind_test.helper;

import android.content.Context;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MedicalStateDataUtil {

    private static final String FILE_NAME = "medical_state_data.txt";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    /**
     * 读取用药状态存储文件
     * @param context
     * @return
     */
    public static String readAllData(Context context) {
        StringBuilder data = new StringBuilder();
        try (FileInputStream fis = context.openFileInput(FILE_NAME);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }
    /**
     * 读取当日用药状态
     * @param context
     * @return
     */
    public static boolean readDoneStateForToday(Context context) {
        return readDoneStateForDate(context, new Date());
    }
    public static boolean readDoneStateForDate(Context context, Date date) {
        String today = dateFormat.format(date);
        try (FileInputStream fis = context.openFileInput(FILE_NAME);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fis))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(today)) {
                    return Boolean.parseBoolean(parts[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Default value if not found
    }

    /**
     * 更新当日用药状态
     * @param context
     * @return
     */
    public static void writeDoneStateForToday(Context context, boolean state) {
        writeDoneStateForDate(context, new Date(), state);
    }
    public static void writeDoneStateForDate(Context context, Date date, boolean state) {
        // 将指定日期转换为字符串
        String dateString = dateFormat.format(date);

        // 读取现有数据到Map中
        Map<String, Boolean> dateStatusMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(context.getFileStreamPath(FILE_NAME)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    dateStatusMap.put(parts[0], Boolean.parseBoolean(parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 更新或添加指定日期的状态
        dateStatusMap.put(dateString, state);

        // 将Map内容写回文件
        try (FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
            for (Map.Entry<String, Boolean> entry : dateStatusMap.entrySet()) {
                fos.write((entry.getKey() + ":" + entry.getValue()).getBytes());
                fos.write("\n".getBytes()); // 每条记录后换行
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
