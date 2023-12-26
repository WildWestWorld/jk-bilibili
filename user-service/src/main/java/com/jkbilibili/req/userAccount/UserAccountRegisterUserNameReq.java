package com.jkbilibili.req.userAccount;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserAccountRegisterUserNameReq {

        @NotBlank(message = "【用户名】不能为空")
        private String username;
        @NotBlank(message = "【密码】不能为空")
        private String password;

}
