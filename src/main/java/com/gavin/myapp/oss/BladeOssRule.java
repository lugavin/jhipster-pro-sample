package com.gavin.myapp.oss;

import java.util.UUID;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;

/**
 * 默认存储桶生成规则
 *
 */
public class BladeOssRule implements OssRule {

    /**
     * 租户模式
     */
    private final Boolean tenantMode;

    @Override
    public String bucketName(String bucketName) {
        return bucketName;
    }

    @Override
    public String fileName(String originalFilename) {
        return (
            "upload" +
            "/" +
            DateTime.now().toString("yyyyMMdd") +
            "/" +
            UUID.randomUUID().toString() +
            "." +
            FilenameUtils.getExtension(originalFilename)
        );
    }

    public BladeOssRule(Boolean tenantMode) {
        this.tenantMode = tenantMode;
    }
}
