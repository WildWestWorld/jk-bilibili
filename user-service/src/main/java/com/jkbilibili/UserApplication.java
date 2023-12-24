package com.jkbilibili;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;


@SpringBootApplication
@MapperScan("com.jkbilibili.mapper")
public class UserApplication {

    private static final Logger LOG = LoggerFactory.getLogger(UserApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UserApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        //参数1:配置文件里的port 参数2:配置文件的前缀名
        LOG.info("测试地址: \thttp://127.0.0.1:{}{}/hello", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));    }

}
