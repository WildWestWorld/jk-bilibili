package com.jkbilibili.context;

import com.jkbilibili.res.UserLoginRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//用于存取 Member的id
public class LoginUserContext {
    private static final Logger LOG = LoggerFactory.getLogger(LoginUserContext.class);

    private static final ThreadLocal<UserLoginRes> user = new ThreadLocal<>();

    public static UserLoginRes getUser() {
        return user.get();
    }

    public static void setUser(UserLoginRes user) {
        LoginUserContext.user.set(user);
    }

    public static Long getId() {
        try {
            return user.get().getId();
        } catch (Exception e) {
            LOG.error("获取登录会员信息异常", e);
            throw e;
        }
    }

}
