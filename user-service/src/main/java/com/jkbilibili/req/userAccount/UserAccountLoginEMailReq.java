package com.jkbilibili.req.userAccount;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserAccountLoginEMailReq {
    @NotBlank(message = "【邮箱】不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$",message = "【邮箱】格式错误")
    private String email;

    @NotBlank(message = "【邮箱验证码】不能为空")
    private String code;





}
