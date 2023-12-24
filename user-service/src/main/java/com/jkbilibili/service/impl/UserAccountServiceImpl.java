package com.jkbilibili.service.impl;

import cn.hutool.core.bean.BeanUtil;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jkbilibili.domain.UserAccount;
import com.jkbilibili.domain.UserAccountExample;
import com.jkbilibili.mapper.UserAccountMapper;

import com.jkbilibili.req.userAccount.UserAccountQueryReq;
import com.jkbilibili.req.userAccount.UserAccountSaveReq;
import com.jkbilibili.res.PageRes;
import com.jkbilibili.res.userAccount.UserAccountQueryRes;

import com.jkbilibili.service.UserAccountService;

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


        DateTime nowTime  = DateTime.now();


        UserAccount userAccount = BeanUtil.copyProperties(req, UserAccount.class);


        if(ObjectUtil.isNull(userAccount.getId())){
            //        从 线程中获取数据
//          userAccount.setMemberId(LoginMemberContext.getId());
            userAccount.setId(SnowUtil.getSnowflakeNextId());
            userAccount.setCreateTime(nowTime);
            userAccount.setUpdateTime(nowTime);

            userAccountMapper.insert(userAccount);
        }else{
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
}
