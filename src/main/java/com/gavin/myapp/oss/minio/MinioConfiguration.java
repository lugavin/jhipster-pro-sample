package com.gavin.myapp.oss.minio;

import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.oss.BladeOssRule;
import com.gavin.myapp.oss.OssRule;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio配置类
 *
 */
@Configuration
@ConditionalOnProperty(value = "application.oss.name", havingValue = "minio")
public class MinioConfiguration {

    private final ApplicationProperties applicationProperties;

    public MinioConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    @ConditionalOnMissingBean(OssRule.class)
    public OssRule ossRule() {
        return new BladeOssRule(applicationProperties.getOss().getTenantMode());
    }

    @Bean
    @ConditionalOnMissingBean(MinioClient.class)
    public MinioClient minioClient() throws InvalidPortException, InvalidEndpointException {
        return new MinioClient(
            applicationProperties.getOss().getEndpoint(),
            applicationProperties.getOss().getAccessKey(),
            applicationProperties.getOss().getSecretKey()
        );
    }

    @Bean
    @ConditionalOnBean({ MinioClient.class, OssRule.class })
    @ConditionalOnMissingBean(MinioTemplate.class)
    public MinioTemplate minioTemplate(MinioClient minioClient, OssRule ossRule) {
        return new MinioTemplate(minioClient, ossRule, applicationProperties);
    }
}
