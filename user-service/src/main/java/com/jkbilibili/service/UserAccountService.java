package com.jkbilibili.service;


import com.jkbilibili.domain.UserAccount;
import com.jkbilibili.req.userAccount.*;
import com.jkbilibili.res.PageRes;
import com.jkbilibili.res.UserLoginRes;
import com.jkbilibili.res.userAccount.UserAccountQueryRes;

public interface UserAccountService {
    void saveUserAccount(UserAccountSaveReq req);

    PageRes<UserAccountQueryRes> queryUserAccountList(UserAccountQueryReq req);

    void deleteById(Long id);

    //注册
    long registerByMobile(UserAccountRegisterMobileReq req);

    //注册用户
    long registerByUserName(UserAccountRegisterUserNameReq req);

    //加密密码
    UserAccount encryptPasswordUserAccount(UserAccount userAccount);

    //手机号登录
    UserLoginRes loginByMobile(UserAccountLoginReq req);

    //根据手机号查询用户
    UserAccount SelectMemberByMobile(String mobile);

    //发送短信
    void sendCode(UserAccountRegisterMobileReq req);

    //保存发送的短信
    void saveSmsRedis(String mobile,String code);
    //验证手机验证码
     void verifySmsCode(UserAccountLoginReq req);
     //发送短信(腾讯)


}