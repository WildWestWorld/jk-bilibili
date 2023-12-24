package com.jkbilibili.service;



import com.jkbilibili.req.userAccount.UserAccountQueryReq;
import com.jkbilibili.req.userAccount.UserAccountSaveReq;
import com.jkbilibili.res.PageRes;
import com.jkbilibili.res.userAccount.UserAccountQueryRes;

import java.util.List;

public interface UserAccountService {
   void saveUserAccount(UserAccountSaveReq req);
   PageRes<UserAccountQueryRes> queryUserAccountList(UserAccountQueryReq req);

   void deleteById(Long id);
}