package com.jkbilibili.service.impl;

import cn.hutool.core.bean.BeanUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jkbilibili.domain.UserAccount;
import com.jkbilibili.domain.UserAccountExample;
import com.jkbilibili.exception.BusinessException;
import com.jkbilibili.exception.BusinessExceptionEnum;
import com.jkbilibili.mapper.UserAccountMapper;

import com.jkbilibili.req.userAccount.UserAccountQueryReq;
import com.jkbilibili.req.userAccount.UserAccountRegisterReq;
import com.jkbilibili.req.userAccount.UserAccountRegisterUserNameReq;
import com.jkbilibili.req.userAccount.UserAccountSaveReq;
import com.jkbilibili.res.PageRes;
import com.jkbilibili.res.userAccount.UserAccountQueryRes;

import com.jkbilibili.service.UserAccountService;

import com.jkbilibili.utils.MD5Util;
import com.jkbilibili.utils.RSAUtil;
import com.jkbilibili.utils.SnowUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final static Logger LOG = LoggerFactory.getLogger(UserAccountService.class);

    @Resource
    UserAccountMapper userAccountMapper;

    @Override
    public void saveUserAccount(UserAccountSaveReq req) {


        DateTime nowTime = DateTime.now();


        UserAccount userAccount = BeanUtil.copyProperties(req, UserAccount.class);


        if (ObjectUtil.isNull(userAccount.getId())) {
            //        从 线程中获取数据
//          userAccount.setMemberId(LoginMemberContext.getId());
            userAccount.setId(SnowUtil.getSnowflakeNextId());
            userAccount.setCreateTime(nowTime);
            userAccount.setUpdateTime(nowTime);

            userAccountMapper.insert(userAccount);
        } else {
            userAccount.setUpdateTime(nowTime);
            userAccountMapper.updateByPrimaryKeySelective(userAccount);
        }


    }

    @Override
    public PageRes<UserAccountQueryRes> queryUserAccountList(UserAccountQueryReq req) {
        UserAccountExample userAccountExample = new UserAccountExample();
        UserAccountExample.Criteria criteria = userAccountExample.createCriteria();
//        if(ObjectUtil.isNotNull(req.getMemberId())){
//            criteria.andMemberIdEqualTo(req.getMemberId());
//        }

        // 分页处理
        PageHelper.startPage(req.getPage(), req.getSize());
        List<UserAccount> userAccountList = userAccountMapper.selectByExample(userAccountExample);


        PageInfo<UserAccount> userAccountPageInfo = new PageInfo<>(userAccountList);

        LOG.info("总行数：{}", userAccountPageInfo.getTotal());
        LOG.info("总页数：{}", userAccountPageInfo.getPages());

//  转成Controller的传输类
        List<UserAccountQueryRes> userAccountQueryResList = BeanUtil.copyToList(userAccountList, UserAccountQueryRes.class);

        PageRes<UserAccountQueryRes> pageRes = new PageRes<>();
        pageRes.setList(userAccountQueryResList);
        pageRes.setTotal(userAccountPageInfo.getTotal());
        return pageRes;
    }


    @Override
    public void deleteById(Long id) {
        userAccountMapper.deleteByPrimaryKey(id);
    }


    @Override
    public long registerByMobile(UserAccountRegisterReq req) {
        String mobile = req.getMobile();

        UserAccountExample userAccountExample = new UserAccountExample();
        //查询是否有这个手机号
        userAccountExample.createCriteria().andMobileEqualTo(mobile);
        List<UserAccount> userAccounts = userAccountMapper.selectByExample(userAccountExample);
        //如果有了这个手机号,就报错
        if (CollUtil.isNotEmpty(userAccounts)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }
        //如果没有这个手机号就让他正常注册
        UserAccount userAccount = new UserAccount();
        userAccount.setId(SnowUtil.getSnowflakeNextId());
        userAccount.setMobile(mobile);

        //获取当前时间存入数据库
        DateTime now = DateTime.now();
        userAccount.setCreateTime(now);
        userAccount.setUpdateTime(now);

        userAccountMapper.insert(userAccount);

        return userAccount.getId();
    }


    @Override
    public long registerByUserName(UserAccountRegisterUserNameReq req) {
        String userName = req.getUsername();
        UserAccountExample userAccountExample = new UserAccountExample();
        //查询是否有相同 用户名
        userAccountExample.createCriteria().andUsernameEqualTo(userName);
        List<UserAccount> userAccounts = userAccountMapper.selectByExample(userAccountExample);
        //如果有相同的用户名就报错
        if(CollUtil.isNotEmpty(userAccounts)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_USER_EXIST);
        }


        //如果没有这个手机号就让他正常注册
        UserAccount userAccount = new UserAccount();
        userAccount.setId(SnowUtil.getSnowflakeNextId());
        userAccount.setUsername(userName);
        UserAccount userAccountFormat = encryptPasswordUserAccount(userAccount);

        userAccountMapper.insert(userAccountFormat);

        return userAccount.getId();
    }


    @Override
    public UserAccount encryptPasswordUserAccount(UserAccount userAccount) {
        //获取当前时间存入数据库
        DateTime now = DateTime.now();
        //MD5加密 作为盐值
        //把当前时间作为盐值
        String saltTime = String.valueOf(now.getTime());
        String password = userAccount.getPassword();
        //MD5加密(后面登录的时候，密码+盐值+md5 = 我们数据库存储的MD5密码)
        String md5Password = MD5Util.sign(password, saltTime, "UTF-8");
        userAccount.setSalt(saltTime);
        userAccount.setPassword(md5Password);


        userAccount.setCreateTime(now);
        userAccount.setUpdateTime(now);


        return userAccount;
    }
}
