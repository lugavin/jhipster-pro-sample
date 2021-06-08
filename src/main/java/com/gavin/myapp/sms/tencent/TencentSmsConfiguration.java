package com.gavin.myapp.sms.tencent;

import com.gavin.myapp.config.ApplicationProperties;
import com.github.qcloudsms.SmsMultiSender;
import javax.cache.CacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云短信配置类
 *
 */
@Configuration
@ConditionalOnProperty(value = "application.sms.name", havingValue = "tencent")
public class TencentSmsConfiguration {

    private final CacheManager cacheManager;

    @Bean
    public TencentSmsTemplate tencentSmsTemplate(ApplicationProperties applicationProperties) {
        SmsMultiSender smsSender = new SmsMultiSender(
            Integer.parseInt(applicationProperties.getSms().getAccessKey()),
            applicationProperties.getSms().getSecretKey()
        );
        return new TencentSmsTemplate(applicationProperties, smsSender, cacheManager);
    }

    public TencentSmsConfiguration(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
