package com.gavin.myapp.oss.local;

import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.oss.BladeOssRule;
import com.gavin.myapp.oss.OssRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "application.oss.name", havingValue = "local")
public class LocalOssConfiguration {

    private final ApplicationProperties applicationProperties;

    public LocalOssConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    @ConditionalOnMissingBean(OssRule.class)
    public OssRule ossRule() {
        return new BladeOssRule(applicationProperties.getOss().getTenantMode());
    }

    @Bean
    @ConditionalOnMissingBean(LocalOssTemplate.class)
    @ConditionalOnBean({ OssRule.class })
    public LocalOssTemplate localOssTemplate(OssRule ossRule) {
        return new LocalOssTemplate(applicationProperties, ossRule);
    }
}
