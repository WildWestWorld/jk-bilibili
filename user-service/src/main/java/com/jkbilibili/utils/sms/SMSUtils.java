package com.jkbilibili.utils.sms;

import com.jkbilibili.exception.BusinessException;
import com.jkbilibili.exception.BusinessExceptionEnum;
import com.jkbilibili.service.UserAccountService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;

import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SMSUtils {


    private final static Logger LOG = LoggerFactory.getLogger(SMSUtils.class);

    @Autowired
    private TencentCloudProperties tencentCloudProperties;

    public void sendSMS(String phone, String code)  {


        /* 必要步骤：
         * 实例化一个认证对象，入参需要传入腾讯云账户密钥对secretId，secretKey。
         * 这里采用的是从环境变量读取的方式，需要在环境变量中先设置这两个值。
         * 你也可以直接在代码中写死密钥对，但是小心不要将代码复制、上传或者分享给他人，
         * 以免泄露密钥对危及你的财产安全。
         * CAM密匙查询获取: https://console.cloud.tencent.com/cam/capi
         */
        String secretId = tencentCloudProperties.getSecretId();
        String secretKey = tencentCloudProperties.getSecretKey();
        Credential cred = new Credential(tencentCloudProperties.getSecretId(),
                tencentCloudProperties.getSecretKey());

        if(secretId.equals("default_secret_id")||secretKey.equals("default_secret_key")){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_SMS_KEY_NOT_EXIST);
        }


        // 实例化一个http选项，可选的，没有特殊需求可以跳过
        HttpProfile httpProfile = new HttpProfile();

        // httpProfile.setReqMethod("POST"); // 默认使用POST

        /*
         * SDK会自动指定域名。通常是不需要特地指定域名的，但是如果你访问的是金融区的服务
         * 则必须手动指定域名，例如sms的上海金融区域名： sms.ap-shanghai-fsi.tencentcloudapi.com
         */
        httpProfile.setEndpoint("sms.tencentcloudapi.com");

        // 实例化一个client选项
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        // 实例化要请求产品的client对象,clientProfile是可选的
        SmsClient client = new SmsClient(cred, "ap-nanjing", clientProfile);

        // 实例化一个请求对象,每个接口都会对应一个request对象
        SendSmsRequest req = new SendSmsRequest();
        String[] phoneNumberSetFormat = {
                "+86" + phone
        }; //电话号码
        req.setPhoneNumberSet(phoneNumberSetFormat);
        req.setSmsSdkAppId("1400835785"); // 短信应用ID: 短信SdkAppId在 [短信控制台] 添加应用后生成的实际SdkAppId
        req.setSignName("题库1901测试公众号"); // 签名
        req.setTemplateId("1852109"); // 模板id：必须填写已审核通过的模板 ID。模板ID可登录 [短信控制台] 查看

        /* 模板参数（自定义占位变量）: 若无模板参数，则设置为空 */
        String[] templateParamSet1 = {
                code
        };
        req.setTemplateParamSet(templateParamSet1);

        // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
        SendSmsResponse resp = null;
        try {
            resp = client.SendSms(req);
        } catch (TencentCloudSDKException e) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_SMS_ERROR);
//            throw new RuntimeException(e);
        }
        // 输出json格式的字符串回包
        // System.out.println(SendSmsResponse.toJsonString(resp));
        LOG.info("短信Resp:{}", SendSmsResponse.toJsonString(resp));
    }
}
