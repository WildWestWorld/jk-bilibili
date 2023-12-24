package com.jkbilibili.controller;


import cn.hutool.core.util.ObjectUtil;
import com.jkbilibili.req.userInfo.UserInfoQueryReq;
import com.jkbilibili.req.userInfo.UserInfoSaveReq;
import com.jkbilibili.res.CommonRes;
import com.jkbilibili.res.PageRes;
import com.jkbilibili.res.userInfo.UserInfoQueryRes;
import com.jkbilibili.service.UserInfoService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Resource
    UserInfoService userInfoService;

    @PostMapping("/save")
    public CommonRes<Object> saveUserInfo(@Valid @RequestBody UserInfoSaveReq req) {

        userInfoService.saveUserInfo(req);
       if(ObjectUtil.isNull(req.getId())){
           return new CommonRes<>("添加UserInfo成功");
       }else{
           return new CommonRes<>("编辑UserInfo成功");
       }
    }

    @GetMapping("/queryList")
    public CommonRes<PageRes<UserInfoQueryRes>> queryUserInfoList(@Valid UserInfoQueryReq req) {

        //       获取当前用户的MemberID
        //req.setMemberId(LoginMemberContext.getId());
        PageRes<UserInfoQueryRes> userInfoQueryResList = userInfoService.queryUserInfoList(req);
        return new CommonRes<>(userInfoQueryResList);
    }


    @DeleteMapping("/delete/{id}")
    public CommonRes<Object> deleteById(@PathVariable Long id) {
            userInfoService.deleteById(id);
        return new CommonRes<>("删除UserInfo成功");

    }

}
