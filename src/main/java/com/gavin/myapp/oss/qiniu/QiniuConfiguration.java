package com.gavin.myapp.oss.qiniu;

import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.oss.BladeOssRule;
import com.gavin.myapp.oss.OssRule;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Qiniu配置类
 *
 */
@Configuration
@ConditionalOnProperty(value = "application.oss.name", havingValue = "qiniu")
public class QiniuConfiguration {

    private final ApplicationProperties applicationProperties;

    public QiniuConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    @ConditionalOnMissingBean(OssRule.class)
    public OssRule ossRule() {
        return new BladeOssRule(applicationProperties.getOss().getTenantMode());
    }

    @Bean
    @ConditionalOnMissingBean(com.qiniu.storage.Configuration.class)
    public com.qiniu.storage.Configuration qnConfiguration() {
        return new com.qiniu.storage.Configuration(Zone.autoZone());
    }

    @Bean
    public Auth auth() {
        return Auth.create(applicationProperties.getOss().getAccessKey(), applicationProperties.getOss().getSecretKey());
    }

    @Bean
    @ConditionalOnBean(com.qiniu.storage.Configuration.class)
    public UploadManager uploadManager(com.qiniu.storage.Configuration cfg) {
        return new UploadManager(cfg);
    }

    @Bean
    @ConditionalOnBean(com.qiniu.storage.Configuration.class)
    public BucketManager bucketManager(com.qiniu.storage.Configuration cfg) {
        return new BucketManager(auth(), cfg);
    }

    @Bean
    @ConditionalOnMissingBean(QiniuTemplate.class)
    @ConditionalOnBean({ Auth.class, UploadManager.class, BucketManager.class, OssRule.class })
    public QiniuTemplate qiniuTemplate(Auth auth, UploadManager uploadManager, BucketManager bucketManager, OssRule ossRule) {
        return new QiniuTemplate(auth, uploadManager, bucketManager, applicationProperties, ossRule);
    }
}
