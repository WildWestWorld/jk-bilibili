package com.jkbilibili.res;


import lombok.Data;

@Data
public class CommonRes<T> {

    /**
     * 业务上的成功或失败
     */
    private boolean success = true;


    private Integer code=0;


    /**
     * 返回信息
     */
    private String message="";

    /**
     * 返回泛型数据，自定义类型
     */
    private T result;

    public CommonRes(T result) {
        this.result = result;
    }

    public CommonRes() {
    }

    public CommonRes(boolean success, Integer code, String message, T result) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
