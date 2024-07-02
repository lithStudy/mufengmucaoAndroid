package com.mufengmucao.remind.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.style.ImageSpan;
import androidx.core.content.ContextCompat;
import com.mufengmucao.remind.R;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
        Date date = new Date();
//        // 获取今天的LocalDate
//        LocalDate today = LocalDate.now();
//        // 计算昨天的日期
//        LocalDate yesterday = today.minusDays(1);
//        // 将LocalDate转换为Date对象
//        date = Date.from(yesterday.atStartOfDay(ZoneId.systemDefault()).toInstant());

        writeDoneStateForDate(context, date, state);
    }
    public static void writeDoneStateForDate(Context context, Date date, boolean state) {
        // 将指定日期转换为字符串
        String dateString = dateFormat.format(date);

        Map<String, Boolean> dateStatusMap = getDateStateMap(context);

        // 更新或添加指定日期的状态
        dateStatusMap.put(dateString, state);
        //根据key倒序排序
        dateStatusMap = dateStatusMap.entrySet().stream()
                .sorted(Map.Entry.<String, Boolean>comparingByKey().reversed())
                .collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);
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

    @NotNull
    public static Map<String, Boolean> getDateStateMap(Context context) {
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
        return dateStatusMap;
    }


    public static SpannableStringBuilder showState(Context context) {
        SpannableStringBuilder data = new SpannableStringBuilder();
        Map<String, Boolean> dateStateMap = getDateStateMap(context);
        if (dateStateMap != null) {
            for (Map.Entry<String, Boolean> dataStateEntry : dateStateMap.entrySet()) {
                data.append(dataStateEntry.getKey()).append("：");

                // 获取对应图标的Drawable并转换为Bitmap
                Drawable drawable;
                if (dataStateEntry.getValue()) {
                    drawable = ContextCompat.getDrawable(context, R.drawable.baseline_insert_emoticon_24);
                } else {
                    drawable = ContextCompat.getDrawable(context, R.drawable.baseline_mood_bad_24);
                }
                assert drawable != null; // 确保Drawable不为空
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight()); // 设置边界
                ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM); // 创建ImageSpan对象

                // 正确地将图标插入到SpannableStringBuilder中的指定位置
                int start = data.length(); // 图标开始的位置
                data.append(" \n"); // 添加一个空格和换行符，图标将替换空格位置
                int end = data.length(); // 图标结束的位置，实际上是换行符的位置
                data.setSpan(imageSpan, start, start + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 插入图标

                // 这里不需要删除添加的空格或换行符，因为它们正是我们想要的效果
            }
        }
        return data;
    }
}
