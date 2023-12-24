package com.jkbilibili.service.impl;

import cn.hutool.core.bean.BeanUtil;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jkbilibili.context.LoginMemberContext;
import com.jkbilibili.domain.${Domain};
import com.jkbilibili.domain.${Domain}Example;
import com.jkbilibili.mapper.${Domain}Mapper;

import com.jkbilibili.req.${domain}.${Domain}QueryReq;
import com.jkbilibili.req.${domain}.${Domain}SaveReq;
import com.jkbilibili.res.PageRes;
import com.jkbilibili.res.${domain}.${Domain}QueryRes;

import com.jkbilibili.service.${Domain}Service;

import com.jkbilibili.utils.SnowUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ${Domain}ServiceImpl implements ${Domain}Service {

    private final static Logger LOG = LoggerFactory.getLogger(${Domain}Service.class);

    @Resource
    ${Domain}Mapper ${domain}Mapper;

    @Override
    public void save${Domain}(${Domain}SaveReq req) {


        DateTime nowTime  = DateTime.now();


        ${Domain} ${domain} = BeanUtil.copyProperties(req, ${Domain}.class);


        if(ObjectUtil.isNull(${domain}.getId())){
            //        从 线程中获取数据
//          ${domain}.setMemberId(LoginMemberContext.getId());
            ${domain}.setId(SnowUtil.getSnowflakeNextId());
            ${domain}.setCreateTime(nowTime);
            ${domain}.setUpdateTime(nowTime);

            ${domain}Mapper.insert(${domain});
        }else{
            ${domain}.setUpdateTime(nowTime);
            ${domain}Mapper.updateByPrimaryKeySelective(${domain});
        }



    }

    @Override
    public PageRes<${Domain}QueryRes> query${Domain}List(${Domain}QueryReq req) {
        ${Domain}Example ${domain}Example = new ${Domain}Example();
        ${Domain}Example.Criteria criteria = ${domain}Example.createCriteria();
//        if(ObjectUtil.isNotNull(req.getMemberId())){
//            criteria.andMemberIdEqualTo(req.getMemberId());
//        }

        // 分页处理
        PageHelper.startPage(req.getPage(), req.getSize());
        List<${Domain}> ${domain}List = ${domain}Mapper.selectByExample(${domain}Example);


        PageInfo<${Domain}> ${domain}PageInfo = new PageInfo<>(${domain}List);

        LOG.info("总行数：{}", ${domain}PageInfo.getTotal());
        LOG.info("总页数：{}", ${domain}PageInfo.getPages());

//  转成Controller的传输类
        List<${Domain}QueryRes> ${domain}QueryResList = BeanUtil.copyToList(${domain}List, ${Domain}QueryRes.class);

        PageRes<${Domain}QueryRes> pageRes = new PageRes<>();
        pageRes.setList(${domain}QueryResList);
        pageRes.setTotal(${domain}PageInfo.getTotal());
        return pageRes;
    }


    @Override
    public void deleteById(Long id) {
        ${domain}Mapper.deleteByPrimaryKey(id);
    }
}
