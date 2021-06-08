package com.gavin.myapp.oss.minio;

import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.oss.OssRule;
import com.gavin.myapp.oss.OssTemplate;
import com.gavin.myapp.oss.model.BladeFile;
import com.gavin.myapp.oss.model.OssFile;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

/**
 * MinIOTemplate
 *
 */
public class MinioTemplate implements OssTemplate {

    /**
     * MinIO客户端
     */
    private final MinioClient client;

    /**
     * 存储桶命名规则
     */
    private final OssRule ossRule;

    /**
     * 配置类
     */
    private final ApplicationProperties applicationProperties;

    public MinioTemplate(MinioClient client, OssRule ossRule, ApplicationProperties applicationProperties) {
        this.client = client;
        this.ossRule = ossRule;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void makeBucket(String bucketName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidObjectPrefixException {
        if (!client.bucketExists(getBucketName(bucketName))) {
            client.makeBucket(getBucketName(bucketName));
            client.setBucketPolicy(getBucketName(bucketName), getPolicyType(getBucketName(bucketName), PolicyType.READ));
        }
    }

    public Bucket getBucket()
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        return getBucket(getBucketName());
    }

    public Bucket getBucket(String bucketName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        Optional<Bucket> bucketOptional = client
            .listBuckets()
            .stream()
            .filter(bucket -> bucket.name().equals(getBucketName(bucketName)))
            .findFirst();
        return bucketOptional.orElse(null);
    }

    public List<Bucket> listBuckets()
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return client.listBuckets();
    }

    @Override
    public void removeBucket(String bucketName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        client.removeBucket(getBucketName(bucketName));
    }

    @Override
    public boolean bucketExists(String bucketName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return client.bucketExists(getBucketName(bucketName));
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        client.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName));
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        client.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName), destFileName);
    }

    @Override
    public OssFile statFile(String fileName)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        return statFile(applicationProperties.getOss().getBucketName(), fileName);
    }

    @Override
    public OssFile statFile(String bucketName, String fileName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        ObjectStat stat = client.statObject(getBucketName(bucketName), fileName);
        OssFile ossFile = new OssFile();
        ossFile.setName(StringUtils.isEmpty(stat.name()) ? fileName : stat.name());
        ossFile.setLink(fileLink(ossFile.getName()));
        ossFile.setHash(String.valueOf(stat.hashCode()));
        ossFile.setLength(stat.length());
        ossFile.setPutTime(stat.createdTime());
        ossFile.setContentType(stat.contentType());
        return ossFile;
    }

    @Override
    public String filePath(String fileName) {
        return getBucketName().concat("/").concat(fileName);
    }

    @Override
    public String filePath(String bucketName, String fileName) {
        return getBucketName(bucketName).concat("/").concat(fileName);
    }

    @Override
    public String fileLink(String fileName) {
        return applicationProperties.getOss().getEndpoint().concat("/").concat(getBucketName()).concat("/").concat(fileName);
    }

    @Override
    public String fileLink(String bucketName, String fileName) {
        return applicationProperties.getOss().getEndpoint().concat("/").concat(getBucketName(bucketName)).concat("/").concat(fileName);
    }

    @Override
    public BladeFile putFile(MultipartFile file)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException {
        return putFile(applicationProperties.getOss().getBucketName(), file.getOriginalFilename(), file);
    }

    @Override
    public BladeFile putFile(String fileName, MultipartFile file)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException {
        return putFile(applicationProperties.getOss().getBucketName(), fileName, file);
    }

    @Override
    public BladeFile putFile(String bucketName, String fileName, MultipartFile file)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, ErrorResponseException {
        return putFile(bucketName, file.getOriginalFilename(), file.getInputStream());
    }

    @Override
    public BladeFile putFile(String fileName, InputStream stream)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, ErrorResponseException {
        return putFile(applicationProperties.getOss().getBucketName(), fileName, stream);
    }

    @Override
    public BladeFile putFile(String bucketName, String fileName, InputStream stream)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidBucketNameException, InvalidObjectPrefixException, ErrorResponseException, NoResponseException, InsufficientDataException, InternalException, RegionConflictException, InvalidArgumentException {
        return putFile(bucketName, fileName, stream, "application/octet-stream");
    }

    public BladeFile putFile(String bucketName, String fileName, InputStream stream, String contentType)
        throws IOException, InvalidObjectPrefixException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, InvalidArgumentException {
        makeBucket(bucketName);
        String originalName = fileName;
        fileName = getFileName(fileName);
        client.putObject(getBucketName(bucketName), fileName, stream, (long) stream.available(), null, null, contentType);
        BladeFile file = new BladeFile();
        file.setOriginalName(originalName);
        file.setName(fileName);
        file.setDomain(getOssHost(bucketName));
        file.setLink(fileLink(bucketName, fileName));
        return file;
    }

    @Override
    public void removeFile(String fileName)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        removeFile(applicationProperties.getOss().getBucketName(), fileName);
    }

    @Override
    public void removeFile(String bucketName, String fileName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        client.removeObject(getBucketName(bucketName), fileName);
    }

    @Override
    public void removeFiles(List<String> fileNames) {
        removeFiles(applicationProperties.getOss().getBucketName(), fileNames);
    }

    @Override
    public void removeFiles(String bucketName, List<String> fileNames) {
        client.removeObjects(getBucketName(bucketName), fileNames);
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

    /**
     * 获取存储桶策略
     *
     * @param policyType 策略枚举
     * @return String
     */
    public String getPolicyType(PolicyType policyType) {
        return getPolicyType(getBucketName(), policyType);
    }

    /**
     * 获取存储桶策略
     *
     * @param bucketName 存储桶名称
     * @param policyType 策略枚举
     * @return String
     */
    public static String getPolicyType(String bucketName, PolicyType policyType) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("    \"Statement\": [\n");
        builder.append("        {\n");
        builder.append("            \"Action\": [\n");

        switch (policyType) {
            case WRITE:
                builder.append("                \"s3:GetBucketLocation\",\n");
                builder.append("                \"s3:ListBucketMultipartUploads\"\n");
                break;
            case READ_WRITE:
                builder.append("                \"s3:GetBucketLocation\",\n");
                builder.append("                \"s3:ListBucket\",\n");
                builder.append("                \"s3:ListBucketMultipartUploads\"\n");
                break;
            default:
                builder.append("                \"s3:GetBucketLocation\"\n");
                break;
        }

        builder.append("            ],\n");
        builder.append("            \"Effect\": \"Allow\",\n");
        builder.append("            \"Principal\": \"*\",\n");
        builder.append("            \"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("\"\n");
        builder.append("        },\n");
        if (PolicyType.READ.equals(policyType)) {
            builder.append("        {\n");
            builder.append("            \"Action\": [\n");
            builder.append("                \"s3:ListBucket\"\n");
            builder.append("            ],\n");
            builder.append("            \"Effect\": \"Deny\",\n");
            builder.append("            \"Principal\": \"*\",\n");
            builder.append("            \"Resource\": \"arn:aws:s3:::");
            builder.append(bucketName);
            builder.append("\"\n");
            builder.append("        },\n");
        }
        builder.append("        {\n");
        builder.append("            \"Action\": ");

        switch (policyType) {
            case WRITE:
                builder.append("[\n");
                builder.append("                \"s3:AbortMultipartUpload\",\n");
                builder.append("                \"s3:DeleteObject\",\n");
                builder.append("                \"s3:ListMultipartUploadParts\",\n");
                builder.append("                \"s3:PutObject\"\n");
                builder.append("            ],\n");
                break;
            case READ_WRITE:
                builder.append("[\n");
                builder.append("                \"s3:AbortMultipartUpload\",\n");
                builder.append("                \"s3:DeleteObject\",\n");
                builder.append("                \"s3:GetObject\",\n");
                builder.append("                \"s3:ListMultipartUploadParts\",\n");
                builder.append("                \"s3:PutObject\"\n");
                builder.append("            ],\n");
                break;
            default:
                builder.append("\"s3:GetObject\",\n");
                break;
        }

        builder.append("            \"Effect\": \"Allow\",\n");
        builder.append("            \"Principal\": \"*\",\n");
        builder.append("            \"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("/*\"\n");
        builder.append("        }\n");
        builder.append("    ],\n");
        builder.append("    \"Version\": \"2012-10-17\"\n");
        builder.append("}\n");
        return builder.toString();
    }

    /**
     * 获取域名
     *
     * @param bucketName 存储桶名称
     * @return String
     */
    public String getOssHost(String bucketName) {
        return applicationProperties.getOss().getEndpoint() + "/" + getBucketName(bucketName);
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
