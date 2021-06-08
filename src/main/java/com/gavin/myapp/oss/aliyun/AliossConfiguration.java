package com.gavin.myapp.oss.aliyun;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.oss.BladeOssRule;
import com.gavin.myapp.oss.OssRule;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Alioss配置类
 *
 */
@Configuration
@ConditionalOnProperty(value = "application.oss.name", havingValue = "alioss")
public class AliossConfiguration {

    private final ApplicationProperties applicationProperties;

    public AliossConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    @ConditionalOnMissingBean(OssRule.class)
    public OssRule ossRule() {
        return new BladeOssRule(applicationProperties.getOss().getTenantMode());
    }

    @Bean
    @ConditionalOnMissingBean(OSSClient.class)
    public OSSClient ossClient() {
        // 创建ClientConfiguration。ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
        ClientConfiguration conf = new ClientConfiguration();
        // 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
        conf.setMaxConnections(1024);
        // 设置Socket层传输数据的超时时间，默认为50000毫秒。
        conf.setSocketTimeout(50000);
        // 设置建立连接的超时时间，默认为50000毫秒。
        conf.setConnectionTimeout(50000);
        // 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
        conf.setConnectionRequestTimeout(1000);
        // 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
        conf.setIdleConnectionTime(60000);
        // 设置失败请求重试次数，默认为3次。
        conf.setMaxErrorRetry(5);
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(
            applicationProperties.getOss().getAccessKey(),
            applicationProperties.getOss().getSecretKey()
        );
        return new OSSClient(applicationProperties.getOss().getEndpoint(), credentialsProvider, conf);
    }

    @Bean
    @ConditionalOnMissingBean(AliossTemplate.class)
    @ConditionalOnBean({ OSSClient.class, OssRule.class })
    public AliossTemplate aliossTemplate(OSSClient ossClient, OssRule ossRule) {
        return new AliossTemplate(ossClient, applicationProperties, ossRule);
    }
}
