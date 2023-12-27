package com.jkbilibili.enums;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum GenderTypeEnum {


    FEMALE("0", "女"), MALE("1", "男"), UNKNOWN("3", "未知");
    public final String code;
    public final String desc;


}
