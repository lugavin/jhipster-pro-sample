package com.gavin.myapp.oss.builder;

import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.domain.OssConfig;
import com.gavin.myapp.oss.OssRule;
import com.gavin.myapp.oss.OssTemplate;
import com.gavin.myapp.oss.minio.MinioTemplate;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;

/**
 * Minio云存储构建类
 *
 */
public class MinioOssBuilder {

    public static OssTemplate template(OssConfig oss, OssRule ossRule) throws InvalidPortException, InvalidEndpointException {
        // 创建配置类
        ApplicationProperties applicationProperties = new ApplicationProperties();
        applicationProperties.getOss().setEndpoint(oss.getEndpoint());
        applicationProperties.getOss().setAccessKey(oss.getAccessKey());
        applicationProperties.getOss().setSecretKey(oss.getSecretKey());
        applicationProperties.getOss().setBucketName(oss.getBucketName());
        // 创建客户端
        MinioClient minioClient = new MinioClient(oss.getEndpoint(), oss.getAccessKey(), oss.getSecretKey());
        return new MinioTemplate(minioClient, ossRule, applicationProperties);
    }
}
