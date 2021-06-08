package com.gavin.myapp.sms.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.gavin.myapp.config.ApplicationProperties;
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
@ConditionalOnProperty(value = "application.sms.name", havingValue = "aliyun")
public class AliSmsConfiguration {

    private final CacheManager cacheManager;

    @Bean
    public AliSmsTemplate aliSmsTemplate(ApplicationProperties applicationProperties) {
        IClientProfile profile = DefaultProfile.getProfile(
            applicationProperties.getSms().getRegionId(),
            applicationProperties.getSms().getAccessKey(),
            applicationProperties.getSms().getSecretKey()
        );
        IAcsClient acsClient = new DefaultAcsClient(profile);
        return new AliSmsTemplate(applicationProperties, acsClient, cacheManager);
    }

    public AliSmsConfiguration(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
