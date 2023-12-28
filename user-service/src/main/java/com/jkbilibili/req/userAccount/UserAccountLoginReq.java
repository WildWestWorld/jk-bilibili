package com.jkbilibili.req.userAccount;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserAccountLoginReq {
    @NotBlank(message = "【手机号】不能为空")
    private String mobile;

    @NotBlank(message = "【短信验证码】不能为空")
    private String code;

}
