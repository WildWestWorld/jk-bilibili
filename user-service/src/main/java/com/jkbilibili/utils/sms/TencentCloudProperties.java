package com.jkbilibili.utils.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@PropertySource("classpath:tencentCloud.properties")
@ConfigurationProperties(prefix = "tencent.cloud")
//获取tencentCloud.properties 里面 tencent.cloud的 secretId 和secretKey属性
public class TencentCloudProperties {

    private String secretId;
    private String secretKey;

}

