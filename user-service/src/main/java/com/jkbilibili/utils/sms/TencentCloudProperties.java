package com.jkbilibili.utils.sms;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
@Data
//@PropertySource("classpath:tencentCloud1.properties")
//@ConfigurationProperties(prefix = "tencent.cloud")
//获取tencentCloud.properties 里面 tencent.cloud的 secretId 和secretKey属性
public class TencentCloudProperties {

    private String secretId;
    private String secretKey;


    @PostConstruct
    public void init() {
        try {
            // Read the configuration file
            Properties properties = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("tencentCloud.properties");
            properties.load(inputStream);
            // Get the property values
            secretId = properties.getProperty("tencent.cloud.secretId");
            secretKey = properties.getProperty("tencent.cloud.secretKey");
        } catch (Exception e) {
            // Catch the exception and use a default value
            secretId = "default_secret_id";
            secretKey = "default_secret_key";
        }
    }
}

