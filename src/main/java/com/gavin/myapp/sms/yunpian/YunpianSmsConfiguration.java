package com.gavin.myapp.sms.yunpian;

import com.gavin.myapp.config.ApplicationProperties;
import com.yunpian.sdk.YunpianClient;
import javax.cache.CacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 云片短信配置类
 *
 */
@Configuration
@ConditionalOnProperty(value = "application.sms.name", havingValue = "yunpian")
public class YunpianSmsConfiguration {

    private final CacheManager cacheManager;

    @Bean
    public YunpianSmsTemplate yunpianSmsTemplate(ApplicationProperties applicationProperties) {
        YunpianClient client = new YunpianClient(applicationProperties.getSms().getAccessKey()).init();
        return new YunpianSmsTemplate(applicationProperties, client, cacheManager);
    }

    public YunpianSmsConfiguration(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
