package com.gavin.myapp.oss.builder;

import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.domain.OssConfig;
import com.gavin.myapp.oss.OssRule;
import com.gavin.myapp.oss.OssTemplate;
import com.gavin.myapp.oss.local.LocalOssTemplate;

/**
 * 本地存储构建类
 *
 */
public class LocalOssBuilder {

    public static OssTemplate template(OssConfig ossConfig, OssRule ossRule) {
        // 创建配置类
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.getOss().setEndpoint(ossConfig.getEndpoint());
        applicationProperties.getOss().setAccessKey(ossConfig.getAccessKey());
        applicationProperties.getOss().setSecretKey(ossConfig.getSecretKey());
        applicationProperties.getOss().setBucketName(ossConfig.getBucketName());
        return new LocalOssTemplate(applicationProperties, ossRule);
    }
}
