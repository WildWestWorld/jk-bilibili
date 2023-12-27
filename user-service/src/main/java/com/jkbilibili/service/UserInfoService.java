package com.jkbilibili.service;



import com.jkbilibili.domain.UserInfo;
import com.jkbilibili.req.userInfo.UserInfoQueryReq;
import com.jkbilibili.req.userInfo.UserInfoSaveReq;
import com.jkbilibili.res.PageRes;
import com.jkbilibili.res.userInfo.UserInfoQueryRes;

import java.util.List;

public interface UserInfoService {
   void saveUserInfo(UserInfoSaveReq req);
   PageRes<UserInfoQueryRes> queryUserInfoList(UserInfoQueryReq req);

   void deleteById(Long id);

   //传入userId创建默认的UserInfo
   void saveDefaultUserInfo(Long userId);


}