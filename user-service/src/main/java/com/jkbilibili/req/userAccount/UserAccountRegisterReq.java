package com.jkbilibili.req.userAccount;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserAccountRegisterReq {

        @NotBlank(message = "【手机号】不能为空")
        //首字母为1的11为 字符
        @Pattern(regexp = "^1\\d{10}$",message = "【手机号】格式错误")
        private String mobile;

}
