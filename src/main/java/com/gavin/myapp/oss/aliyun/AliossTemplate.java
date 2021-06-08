package com.gavin.myapp.oss.aliyun;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.oss.OssRule;
import com.gavin.myapp.oss.OssTemplate;
import com.gavin.myapp.oss.model.BladeFile;
import com.gavin.myapp.oss.model.OssFile;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * AliossTemplate
 *
 */
public class AliossTemplate implements OssTemplate {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private final OSSClient ossClient;
    private final ApplicationProperties applicationProperties;
    private final OssRule ossRule;

    public AliossTemplate(OSSClient ossClient, ApplicationProperties applicationProperties, OssRule ossRule) {
        this.ossClient = ossClient;
        this.applicationProperties = applicationProperties;
        this.ossRule = ossRule;
    }

    @Override
    public void makeBucket(String bucketName) {
        if (!bucketExists(bucketName)) {
            ossClient.createBucket(getBucketName(bucketName));
        }
    }

    @Override
    public void removeBucket(String bucketName) {
        ossClient.deleteBucket(getBucketName(bucketName));
    }

    @Override
    public boolean bucketExists(String bucketName) {
        return ossClient.doesBucketExist(getBucketName(bucketName));
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName) {
        ossClient.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName), fileName);
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
        ossClient.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName), destFileName);
    }

    @Override
    public OssFile statFile(String fileName) {
        return statFile(applicationProperties.getOss().getBucketName(), fileName);
    }

    @Override
    public OssFile statFile(String bucketName, String fileName) {
        ObjectMetadata stat = ossClient.getObjectMetadata(getBucketName(bucketName), fileName);
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
            ossClient.putObject(getBucketName(bucketName), key, stream);
        } else {
            PutObjectResult response = ossClient.putObject(getBucketName(bucketName), key, stream);
            int retry = 0;
            int retryCount = 5;
            while (StringUtils.isEmpty(response.getETag()) && retry < retryCount) {
                response = ossClient.putObject(getBucketName(bucketName), key, stream);
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
        ossClient.deleteObject(getBucketName(), fileName);
    }

    @Override
    public void removeFile(String bucketName, String fileName) {
        ossClient.deleteObject(getBucketName(bucketName), fileName);
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
        return ossRule.bucketName(bucketName);
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

    public String getUploadToken() {
        return getUploadToken(applicationProperties.getOss().getBucketName());
    }

    /**
     * TODO 过期时间
     * <p>
     * 获取上传凭证，普通上传
     */
    public String getUploadToken(String bucketName) {
        // 默认过期时间2小时
        return getUploadToken(bucketName, 3600L);
    }

    /**
     * TODO 上传大小限制、基础路径
     * <p>
     * 获取上传凭证，普通上传
     */
    public String getUploadToken(String bucketName, long expireTime) {
        String baseDir = "upload";

        long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
        Date expiration = new Date(expireEndTime);

        PolicyConditions policyConds = new PolicyConditions();
        // 默认大小限制10M
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576);
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, baseDir);

        String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);

        Map<String, String> respMap = new LinkedHashMap<>(16);
        respMap.put("accessid", applicationProperties.getOss().getAccessKey());
        respMap.put("policy", encodedPolicy);
        respMap.put("signature", postSignature);
        respMap.put("dir", baseDir);
        respMap.put("host", getOssHost(bucketName));
        respMap.put("expire", String.valueOf(expireEndTime / 1000));
        try {
            return objectMapper.writeValueAsString(respMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取域名
     *
     * @param bucketName 存储桶名称
     * @return String
     */
    public String getOssHost(String bucketName) {
        String prefix = applicationProperties.getOss().getEndpoint().contains("https://") ? "https://" : "http://";
        return prefix + getBucketName(bucketName) + "." + applicationProperties.getOss().getEndpoint().replaceFirst(prefix, "");
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
