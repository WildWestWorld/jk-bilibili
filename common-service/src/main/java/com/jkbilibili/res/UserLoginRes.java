package com.jkbilibili.res;

import lombok.Data;

//该实体 是与表结构一一对应
@Data
public class UserLoginRes {
    private Long id;

    private String mobile;

    private String Email;

    private String username;

    private String token;



}