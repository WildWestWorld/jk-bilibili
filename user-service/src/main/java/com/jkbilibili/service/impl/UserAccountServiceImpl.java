package com.jkbilibili.service.impl;

import cn.hutool.core.bean.BeanUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jkbilibili.constant.UserAccountConstant;
import com.jkbilibili.domain.UserAccount;
import com.jkbilibili.domain.UserAccountExample;
import com.jkbilibili.exception.BusinessException;
import com.jkbilibili.exception.BusinessExceptionEnum;
import com.jkbilibili.mapper.UserAccountMapper;

import com.jkbilibili.req.userAccount.*;
import com.jkbilibili.res.PageRes;
import com.jkbilibili.res.UserLoginRes;
import com.jkbilibili.res.userAccount.UserAccountQueryRes;

import com.jkbilibili.service.UserAccountService;

import com.jkbilibili.service.UserInfoService;
import com.jkbilibili.utils.JwtUtil;
import com.jkbilibili.utils.encrypt.MD5Util;
import com.jkbilibili.utils.SnowUtil;
import com.jkbilibili.utils.sms.SMSUtils;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final static Logger LOG = LoggerFactory.getLogger(UserAccountService.class);

    @Resource
    UserAccountMapper userAccountMapper;

    @Resource
    UserInfoService userInfoService;

    @Resource
    SMSUtils smsUtils;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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


    @Transactional
    @Override
    public long registerByMobile(UserAccountRegisterMobileReq req) {
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

        userInfoService.saveDefaultUserInfo(userAccount.getId());


        return userAccount.getId();
    }

    @Transactional
    @Override
    public long registerByUserName(UserAccountRegisterUserNameReq req) {
        String userName = req.getUsername();
        UserAccountExample userAccountExample = new UserAccountExample();
        //查询是否有相同 用户名
        userAccountExample.createCriteria().andUsernameEqualTo(userName);
        List<UserAccount> userAccounts = userAccountMapper.selectByExample(userAccountExample);
        //如果有相同的用户名就报错
        if (CollUtil.isNotEmpty(userAccounts)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_USER_EXIST);
        }


        //如果没有这个手机号就让他正常注册
        UserAccount userAccount = new UserAccount();
        userAccount.setId(SnowUtil.getSnowflakeNextId());
        userAccount.setUsername(userName);
        UserAccount userAccountFormat = encryptPasswordUserAccount(userAccount);

        userAccountMapper.insert(userAccountFormat);
        userInfoService.saveDefaultUserInfo(userAccountFormat.getId());

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

    @Transactional
    @Override
    public UserLoginRes loginByMobile(UserAccountLoginReq req) {
        String mobile = req.getMobile();
        String code = req.getCode();

        //查询我们的数据库是否有这个手机号
        UserAccount userAccountDB = SelectMemberByMobile(mobile);
        //如果没有这个手机号,我们直接给他提示，请先发送验证码，因为他发送验证码，我们就给他注册了
        if (ObjectUtil.isNull(userAccountDB)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }


        //有手机号我们判别验证码是否正确
        //如果验证码错误我们就报错
//        if (!code.equals("6666")) {
//            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
//        }
        verifySmsCode(req);
        //先把查询出来的用户类变成可返回结果的类
        UserLoginRes userLoginRes = BeanUtil.copyProperties(userAccountDB, UserLoginRes.class);

        //验证码正确我们就给他发送token

        String token = JwtUtil.createToken(userLoginRes);
        userLoginRes.setToken(token);


        return userLoginRes;
    }


    @Override
    public UserAccount SelectMemberByMobile(String mobile) {
        UserAccountExample userAccountExample = new UserAccountExample();
        userAccountExample.createCriteria().andMobileEqualTo(mobile);
        List<UserAccount> userAccountList = userAccountMapper.selectByExample(userAccountExample);

        //如果不是空的我们就返回数据,是空的我们就返回null
        if (CollUtil.isNotEmpty(userAccountList)) {
            return userAccountList.get(0);
        } else {
            return null;
        }

    }


    @Override
    public void sendCode(UserAccountRegisterMobileReq req) {
        String mobile = req.getMobile();
        //查询 手机号 是否已经注册
        UserAccount userAccountDB = SelectMemberByMobile(mobile);
        //如果没有注册就是先给他注册
        if (ObjectUtil.isNull(userAccountDB)) {
            LOG.info("手机号不存在,则插入数据");
            registerByMobile(req);
        } else {
            LOG.info("手机号存在,不插入数据");
        }

        //        生成验证码
        //        随机四位数的字符串
        String code = RandomUtil.randomNumbers(4);

        LOG.info("保存短信记录表");
        saveSmsRedis(mobile, code);
        smsUtils.sendSMS(mobile, code);

    }

    @Override
    public void saveSmsRedis(String mobile, String code) {

        //手机发送时间 用于Redis 键值
        String mobileLastSendTime = mobile + "-lastSendTime";

        //校验手机是否允许发送短信
        //先获取下最后发送的时间
        String lastSendTime = stringRedisTemplate.opsForValue().get(mobileLastSendTime);


        //如果最小时间不是空的就直接就报错 发送频率过快，因为我们设置了过期时间是一分钟
        if (lastSendTime != null) {
            throw new BusinessException(BusinessExceptionEnum.SMS_TOO_QUICK);
        }


        LOG.info("生成短信验证码:{}", code);

        // 保存手机号与验证码
        stringRedisTemplate.opsForValue().set(mobile, code, UserAccountConstant.SMS_EXPIRE_TIME, TimeUnit.SECONDS);

        // Send the SMS
        LOG.info("生成短信验证码:{}给{}", code, mobile);

        // Update the last send time
        stringRedisTemplate.opsForValue().set(mobileLastSendTime, String.valueOf(System.currentTimeMillis()), UserAccountConstant.SMS_INTERVAL_TIME, TimeUnit.SECONDS);
    }


    @Override
    public void verifySmsCode(UserAccountLoginReq req) {
        String mobile = req.getMobile();
        String code = req.getCode();
        String value = stringRedisTemplate.opsForValue().get(mobile);
        //equalsIgnoreCase 不区分大小写
        if (value != null && value.equalsIgnoreCase(code)) {
            //验证成功就删除对应的key
            stringRedisTemplate.delete(mobile);
        } else {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);

        }
    }
}
