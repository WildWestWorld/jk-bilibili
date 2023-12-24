package com.jkbilibili.service;



import com.jkbilibili.req.${domain}.${Domain}QueryReq;
import com.jkbilibili.req.${domain}.${Domain}SaveReq;
import com.jkbilibili.res.PageRes;
import com.jkbilibili.res.${domain}.${Domain}QueryRes;

import java.util.List;

public interface ${Domain}Service {
   void save${Domain}(${Domain}SaveReq req);
   PageRes<${Domain}QueryRes> query${Domain}List(${Domain}QueryReq req);

   void deleteById(Long id);
}