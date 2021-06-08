package com.gavin.myapp.sms.builder;

import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.domain.SmsConfig;
import com.gavin.myapp.sms.SmsTemplate;
import com.gavin.myapp.sms.yunpian.YunpianSmsTemplate;
import com.yunpian.sdk.YunpianClient;
import javax.cache.CacheManager;

/**
 * 云片短信构建类
 *
 */
public class YunpianSmsBuilder {

    public static SmsTemplate template(SmsConfig sms, CacheManager cacheManager) {
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.getSms().setTemplateId(sms.getTemplateId());
        applicationProperties.getSms().setAccessKey(sms.getAccessKey());
        applicationProperties.getSms().setSignName(sms.getSignName());
        YunpianClient client = new YunpianClient(applicationProperties.getSms().getAccessKey()).init();
        return new YunpianSmsTemplate(applicationProperties, client, cacheManager);
    }
}
