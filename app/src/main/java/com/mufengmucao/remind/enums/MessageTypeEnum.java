package com.mufengmucao.remind.enums;

public enum MessageTypeEnum {
    MEDICAL(1, "用药提醒"),
    ;
    private int code;

    private String desc;

    MessageTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
