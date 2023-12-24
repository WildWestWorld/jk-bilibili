package com.jkbilibili.config;


import com.jkbilibili.interceptor.LogInterceptor;
import com.jkbilibili.interceptor.UserInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//开启拦截器
//一般开启拦截器 都是拦截token 然后存取 token中的payload
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

//日志拦截器
    @Resource
    LogInterceptor logInterceptor;

//   @Resource
   UserInterceptor userInterceptor;

   @Override
   public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(logInterceptor);

//       不要加入context-path
//       registry.addInterceptor(userInterceptor)
//               .addPathPatterns("/**")
//               .excludePathPatterns(
//                       "/hello",
//                       "/member/member/send-code",
//                       "/member/member/login"
//               );
   }
}
