package com.jkbilibili.exception;

public enum BusinessExceptionEnum {

    MEMBER_MOBILE_EXIST("手机号已被注册"),
    MEMBER_MOBILE_NOT_EXIST("请先获取短信验证码"),
    MEMBER_MOBILE_CODE_ERROR("短信验证码错误/过期"),


    MEMBER_USER_EXIST("用户名已被注册"),
    PASSWORD_DECRYPT_ERROR("密码解密失败"),


    SMS_TOO_QUICK("发送短信过快,请稍后重试"),

    BUSINESS_STATION_NAME_UNIQUE_ERROR("车站已存在"),
    BUSINESS_TRAIN_CODE_UNIQUE_ERROR("车次编号已存在"),
    BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR("同车次站序已存在"),
    BUSINESS_TRAIN_STATION_NAME_UNIQUE_ERROR("同车次站名已存在"),
    BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR("同车次厢号已存在"),


    CONFIRM_ORDER_TICKET_COUNT_ERROR("余票不足"),
    CONFIRM_ORDER_LOCK_FAIL("当前抢票人数多，请稍后重试"),

    CONFIRM_TOKEN_LOCK_FAIL("当前抢令牌人数多，请稍后重试"),
    CONFIRM_ORDER_FLOW_EXCEPTION("当前抢票人数很多，请稍后重试"),
    CONFIRM_ORDER_EXCEPTION("服务器忙,请稍后重试/下单错误"),
    CONFIRM_ORDER_SK_TOKEN_FAIL("当前抢票人数很多，请5s后重试"),

    ;

    private String desc;

    BusinessExceptionEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "BusinessExceptionEnum{" +
                "desc='" + desc + '\'' +
                '}';
    }
}
