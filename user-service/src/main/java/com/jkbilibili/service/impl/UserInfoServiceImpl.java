package com.jkbilibili.service.impl;

import cn.hutool.core.bean.BeanUtil;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jkbilibili.constant.UserInfoConstant;
import com.jkbilibili.domain.UserInfo;
import com.jkbilibili.domain.UserInfoExample;
import com.jkbilibili.mapper.UserInfoMapper;

import com.jkbilibili.req.userInfo.UserInfoQueryReq;
import com.jkbilibili.req.userInfo.UserInfoSaveReq;
import com.jkbilibili.res.PageRes;
import com.jkbilibili.res.userInfo.UserInfoQueryRes;

import com.jkbilibili.service.UserInfoService;

import com.jkbilibili.utils.SnowUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final static Logger LOG = LoggerFactory.getLogger(UserInfoService.class);

    @Resource
    UserInfoMapper userInfoMapper;

    @Override
    public void saveUserInfo(UserInfoSaveReq req) {


        DateTime nowTime  = DateTime.now();


        UserInfo userInfo = BeanUtil.copyProperties(req, UserInfo.class);


        if(ObjectUtil.isNull(userInfo.getId())){
            //        从 线程中获取数据
//          userInfo.setMemberId(LoginMemberContext.getId());
            userInfo.setId(SnowUtil.getSnowflakeNextId());
            userInfo.setCreateTime(nowTime);
            userInfo.setUpdateTime(nowTime);

            userInfoMapper.insert(userInfo);
        }else{
            userInfo.setUpdateTime(nowTime);
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
        }



    }

    @Override
    public PageRes<UserInfoQueryRes> queryUserInfoList(UserInfoQueryReq req) {
        UserInfoExample userInfoExample = new UserInfoExample();
        UserInfoExample.Criteria criteria = userInfoExample.createCriteria();
//        if(ObjectUtil.isNotNull(req.getMemberId())){
//            criteria.andMemberIdEqualTo(req.getMemberId());
//        }

        // 分页处理
        PageHelper.startPage(req.getPage(), req.getSize());
        List<UserInfo> userInfoList = userInfoMapper.selectByExample(userInfoExample);


        PageInfo<UserInfo> userInfoPageInfo = new PageInfo<>(userInfoList);

        LOG.info("总行数：{}", userInfoPageInfo.getTotal());
        LOG.info("总页数：{}", userInfoPageInfo.getPages());

//  转成Controller的传输类
        List<UserInfoQueryRes> userInfoQueryResList = BeanUtil.copyToList(userInfoList, UserInfoQueryRes.class);

        PageRes<UserInfoQueryRes> pageRes = new PageRes<>();
        pageRes.setList(userInfoQueryResList);
        pageRes.setTotal(userInfoPageInfo.getTotal());
        return pageRes;
    }


    @Override
    public void deleteById(Long id) {
        userInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void saveDefaultUserInfo(Long userId) {
        UserInfo userInfo = new UserInfo();

        //雪花算法生成id
        userInfo.setId(SnowUtil.getSnowflakeNextId());
        userInfo.setUserId(userId);
        userInfo.setNickName(UserInfoConstant.DEFAULT_NICK);

        //设置独一无二的默认名字
        String uniqueID = RandomUtil.randomString(10);
        userInfo.setNickName(UserInfoConstant.DEFAULT_NICK+"_"+uniqueID);
        userInfo.setGender(UserInfoConstant.DEFAULT_GENDER);
        userInfo.setBirth(UserInfoConstant.DEFAULT_BIRTH);


        //设置创建时间和更新时间
        DateTime now = DateTime.now();
        userInfo.setCreateTime(now);
        userInfo.setUpdateTime(now);

        userInfoMapper.insert(userInfo);

    }
}
