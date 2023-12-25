package com.jkbilibili.controller;


import cn.hutool.core.util.ObjectUtil;
import com.jkbilibili.req.userAccount.UserAccountRegisterReq;
import com.jkbilibili.req.userAccount.UserAccountSaveReq;
import com.jkbilibili.res.CommonRes;
import com.jkbilibili.service.UserAccountService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userAccount")
public class UserAccountController {
    @Resource
    UserAccountService userAccountService;

    @PostMapping("/save")
    public CommonRes<Object> saveUserAccount(@Valid @RequestBody UserAccountSaveReq req) {

        userAccountService.saveUserAccount(req);
        if (ObjectUtil.isNull(req.getId())) {
            return new CommonRes<>("添加UserAccount成功");
        } else {
            return new CommonRes<>("编辑UserAccount成功");
        }
    }

//    @GetMapping("/queryList")
//    public CommonRes<PageRes<UserAccountQueryRes>> queryUserAccountList(@Valid UserAccountQueryReq req) {
//
//        //       获取当前用户的MemberID
//        //req.setMemberId(LoginMemberContext.getId());
//        PageRes<UserAccountQueryRes> userAccountQueryResList = userAccountService.queryUserAccountList(req);
//        return new CommonRes<>(userAccountQueryResList);
//    }


    @DeleteMapping("/delete/{id}")
    public CommonRes<Object> deleteById(@PathVariable Long id) {
        userAccountService.deleteById(id);
        return new CommonRes<>("删除UserAccount成功");

    }


    @PostMapping("/register")
//    @RequestBody 用于请求 用JSON
    public CommonRes<Long> registerByMobile(@Valid @RequestBody UserAccountRegisterReq req) {
        long mobile = userAccountService.registerByMobile(req);

        return new CommonRes<>(mobile);
    }

}
