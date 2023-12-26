package com.jkbilibili.service;


import com.jkbilibili.domain.UserAccount;
import com.jkbilibili.req.userAccount.UserAccountQueryReq;
import com.jkbilibili.req.userAccount.UserAccountRegisterReq;
import com.jkbilibili.req.userAccount.UserAccountRegisterUserNameReq;
import com.jkbilibili.req.userAccount.UserAccountSaveReq;
import com.jkbilibili.res.PageRes;
import com.jkbilibili.res.userAccount.UserAccountQueryRes;

import java.util.List;

public interface UserAccountService {
    void saveUserAccount(UserAccountSaveReq req);

    PageRes<UserAccountQueryRes> queryUserAccountList(UserAccountQueryReq req);

    void deleteById(Long id);

    //注册
    long registerByMobile(UserAccountRegisterReq req);

    //注册用户
    long registerByUserName(UserAccountRegisterUserNameReq req);

    //加密密码
    UserAccount encryptPasswordUserAccount(UserAccount userAccount);


}