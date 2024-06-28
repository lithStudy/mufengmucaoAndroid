package com.mufengmucao.remind.helper;

import com.mufengmucao.remind.enums.MessageTypeEnum;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageUtil {
    private static SimpleDateFormat DATE_DAY = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
    public static int getMedicalRemind() {
        return getMedicalRemind(new Date());
    }

    public static int getMedicalRemind(Date date) {
        return Integer.valueOf(MessageTypeEnum.MEDICAL.getCode()+ DATE_DAY.format(date));
    }
}
