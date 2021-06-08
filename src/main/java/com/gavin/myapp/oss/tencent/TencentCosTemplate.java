package com.gavin.myapp.oss.tencent;

import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.oss.OssRule;
import com.gavin.myapp.oss.OssTemplate;
import com.gavin.myapp.oss.model.BladeFile;
import com.gavin.myapp.oss.model.OssFile;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 腾讯云 COS 操作
 * </p>
 *
 */
public class TencentCosTemplate implements OssTemplate {

    private final COSClient cosClient;
    private final ApplicationProperties applicationProperties;
    private final OssRule ossRule;

    public TencentCosTemplate(COSClient cosClient, ApplicationProperties applicationProperties, OssRule ossRule) {
        this.cosClient = cosClient;
        this.applicationProperties = applicationProperties;
        this.ossRule = ossRule;
    }

    @Override
    public void makeBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            cosClient.createBucket(getBucketName(bucketName));
            // TODO: 权限是否需要修改为私有，当前为 公有读、私有写
            cosClient.setBucketAcl(getBucketName(bucketName), CannedAccessControlList.PublicRead);
        }
    }

    @Override
    public void removeBucket(String bucketName) {
        cosClient.deleteBucket(getBucketName(bucketName));
    }

    @Override
    public boolean bucketExists(String bucketName) {
        return cosClient.doesBucketExist(getBucketName(bucketName));
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName) {
        cosClient.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName), fileName);
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
        cosClient.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName), destFileName);
    }

    @Override
    public OssFile statFile(String fileName) {
        return statFile(applicationProperties.getOss().getBucketName(), fileName);
    }

    @Override
    public OssFile statFile(String bucketName, String fileName) {
        ObjectMetadata stat = cosClient.getObjectMetadata(getBucketName(bucketName), fileName);
        OssFile ossFile = new OssFile();
        ossFile.setName(fileName);
        ossFile.setLink(fileLink(ossFile.getName()));
        ossFile.setHash(stat.getContentMD5());
        ossFile.setLength(stat.getContentLength());
        ossFile.setPutTime(stat.getLastModified());
        ossFile.setContentType(stat.getContentType());
        return ossFile;
    }

    @Override
    public String filePath(String fileName) {
        return getOssHost().concat("/").concat(fileName);
    }

    @Override
    public String filePath(String bucketName, String fileName) {
        return getOssHost(bucketName).concat("/").concat(fileName);
    }

    @Override
    public String fileLink(String fileName) {
        return getOssHost().concat("/").concat(fileName);
    }

    @Override
    public String fileLink(String bucketName, String fileName) {
        return getOssHost(bucketName).concat("/").concat(fileName);
    }

    /**
     * 文件对象
     *
     * @param file 上传文件类
     * @return
     */
    @Override
    public BladeFile putFile(MultipartFile file) throws IOException {
        return putFile(applicationProperties.getOss().getBucketName(), file.getOriginalFilename(), file);
    }

    /**
     * @param fileName 上传文件名
     * @param file     上传文件类
     * @return
     */
    @Override
    public BladeFile putFile(String fileName, MultipartFile file) throws IOException {
        return putFile(applicationProperties.getOss().getBucketName(), fileName, file);
    }

    @Override
    public BladeFile putFile(String bucketName, String fileName, MultipartFile file) throws IOException {
        return putFile(bucketName, fileName, file.getInputStream());
    }

    @Override
    public BladeFile putFile(String fileName, InputStream stream) {
        return putFile(applicationProperties.getOss().getBucketName(), fileName, stream);
    }

    @Override
    public BladeFile putFile(String bucketName, String fileName, InputStream stream) {
        return put(bucketName, stream, fileName, false);
    }

    public BladeFile put(String bucketName, InputStream stream, String key, boolean cover) {
        makeBucket(bucketName);
        String originalName = key;
        key = getFileName(key);
        // 覆盖上传
        if (cover) {
            cosClient.putObject(getBucketName(bucketName), key, stream, null);
        } else {
            PutObjectResult response = cosClient.putObject(getBucketName(bucketName), key, stream, null);
            int retry = 0;
            int retryCount = 5;
            while (StringUtils.isEmpty(response.getETag()) && retry < retryCount) {
                response = cosClient.putObject(getBucketName(bucketName), key, stream, null);
                retry++;
            }
        }
        BladeFile file = new BladeFile();
        file.setOriginalName(originalName);
        file.setName(key);
        file.setDomain(getOssHost(bucketName));
        file.setLink(fileLink(bucketName, key));
        return file;
    }

    @Override
    public void removeFile(String fileName) {
        cosClient.deleteObject(getBucketName(), fileName);
    }

    @Override
    public void removeFile(String bucketName, String fileName) {
        cosClient.deleteObject(getBucketName(bucketName), fileName);
    }

    @Override
    public void removeFiles(List<String> fileNames) {
        fileNames.forEach(this::removeFile);
    }

    @Override
    public void removeFiles(String bucketName, List<String> fileNames) {
        fileNames.forEach(fileName -> removeFile(getBucketName(bucketName), fileName));
    }

    /**
     * 根据规则生成存储桶名称规则
     *
     * @return String
     */
    private String getBucketName() {
        return getBucketName(applicationProperties.getOss().getBucketName());
    }

    /**
     * 根据规则生成存储桶名称规则
     *
     * @param bucketName 存储桶名称
     * @return String
     */
    private String getBucketName(String bucketName) {
        return ossRule.bucketName(bucketName).concat("-").concat(applicationProperties.getOss().getAppId());
    }

    /**
     * 根据规则生成文件名称规则
     *
     * @param originalFilename 原始文件名
     * @return string
     */
    private String getFileName(String originalFilename) {
        return ossRule.fileName(originalFilename);
    }

    /**
     * 获取域名
     *
     * @param bucketName 存储桶名称
     * @return String
     */
    public String getOssHost(String bucketName) {
        return "http://" + cosClient.getClientConfig().getEndpointBuilder().buildGeneralApiEndpoint(getBucketName(bucketName));
    }

    /**
     * 获取域名
     *
     * @return String
     */
    public String getOssHost() {
        return getOssHost(applicationProperties.getOss().getBucketName());
    }
}
