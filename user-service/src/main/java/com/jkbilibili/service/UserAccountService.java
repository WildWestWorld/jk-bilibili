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
    //邮箱注册用户
    long registerByEMail(UserAccountRegisterEMailReq req);

    //加密密码
    UserAccount encryptPasswordUserAccount(UserAccount userAccount);

    //手机号登录
    UserLoginRes loginByMobile(UserAccountLoginMobileReq req);
    //用户名登录
     UserLoginRes loginByUserName(UserAccountLoginUserNameReq req);

    UserLoginRes loginByEMail(UserAccountLoginEMailReq req);

    //根据手机号查询用户
    UserAccount SelectMemberByMobile(String mobile);

    //发送短信
    void sendCode(UserAccountRegisterMobileReq req);

    //发送邮箱信息
    void sendEMailCode(UserAccountRegisterEMailReq req);

    //保存短信到Redis
    void saveSmsRedis(String mobile,String code);
    //保存EMail到Redis
    void saveEMailCodeRedis(String email,String code);

    //验证手机验证码
     void verifySmsCode(UserAccountLoginMobileReq req);
    //验证手机验证码
    void verifyEMailCode(UserAccountLoginEMailReq req);

    //根据手机号查询用户
    UserAccount SelectMemberByUserName(String username);
    UserAccount SelectMemberByEMail(String email);

}