package com.gavin.myapp.sms.qiniu;

import com.gavin.myapp.config.ApplicationProperties;
import com.qiniu.sms.SmsManager;
import com.qiniu.util.Auth;
import javax.cache.CacheManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云短信配置类
 *
 */
@Configuration
@ConditionalOnProperty(value = "application.sms.name", havingValue = "qiniu")
public class QiniuSmsConfiguration {

    private final CacheManager cacheManager;

    @Bean
    public QiniuSmsTemplate qiniuSmsTemplate(ApplicationProperties applicationProperties) {
        Auth auth = Auth.create(applicationProperties.getSms().getAccessKey(), applicationProperties.getSms().getSecretKey());
        SmsManager smsManager = new SmsManager(auth);
        return new QiniuSmsTemplate(applicationProperties, smsManager, cacheManager);
    }

    public QiniuSmsConfiguration(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
