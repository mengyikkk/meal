package com.meal.common.utils;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.meal.common.config.AliyunProperties;
import com.meal.common.config.AliyunSmsProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 阿里云发送短信的服务
 * @author ajie
 * @createTime 2021年09月17日 21:02:00
 */
public final class SmsUtils {
    private static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);


    /**
     * 构建发送短信的连接
     * @return 短信的连接
     */
    public static Client createClient(AliyunSmsProperties aliyunSmsProperties) throws Exception {
        Config config = new Config()
                .setAccessKeyId(aliyunSmsProperties.getAliyun().getAccessKeyId())
                .setAccessKeySecret(aliyunSmsProperties.getAliyun().getAccessKeySecret());
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

    /**
     * 传入电话号码， 发送短信
     * @param phoneNumbers
     * @return
     */
    public static void sendSms(String phoneNumbers,String templateCode, int code,AliyunSmsProperties aliyunSmsProperties) {
        try {
            Client client = createClient(aliyunSmsProperties);
            SendSmsRequest request = new SendSmsRequest()
                    .setPhoneNumbers(phoneNumbers)
                    .setSignName(aliyunSmsProperties.getSign())
                    .setTemplateCode(templateCode)
                    .setTemplateParam("{ code: " + code + " }");
            SendSmsResponse response = client.sendSms(request);
            logger.info("短信发送结果--> {}", response.getBody().code + "----------" + response.getBody().message);
        } catch (Exception e) {
            logger.error("短信发送失败--> {}", e.getMessage());
        }
    }

}
