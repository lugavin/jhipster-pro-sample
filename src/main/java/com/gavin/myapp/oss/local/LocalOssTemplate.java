package com.gavin.myapp.oss.local;

import com.gavin.myapp.SpringBootUtil;
import com.gavin.myapp.config.ApplicationProperties;
import com.gavin.myapp.oss.OssRule;
import com.gavin.myapp.oss.OssTemplate;
import com.gavin.myapp.oss.model.BladeFile;
import com.gavin.myapp.oss.model.OssFile;
import io.minio.errors.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

public class LocalOssTemplate implements OssTemplate {

    private final String uploadPath = SpringBootUtil.getApplicationPath();
    private final ApplicationProperties applicationProperties;
    private final OssRule ossRule;

    public LocalOssTemplate(ApplicationProperties applicationProperties, OssRule ossRule) {
        this.applicationProperties = applicationProperties;
        this.ossRule = ossRule;
    }

    @Override
    public void makeBucket(String bucketName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidObjectPrefixException {
        File bucket = new File(uploadPath + File.separator + bucketName);
        if (!bucket.exists()) {
            bucket.mkdir();
        }
    }

    @Override
    public void removeBucket(String bucketName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        FileUtils.deleteDirectory(new File(uploadPath + File.separator + bucketName));
    }

    @Override
    public boolean bucketExists(String bucketName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return new File(uploadPath + File.separator + bucketName).exists();
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        FileUtils.copyFileToDirectory(
            new File(uploadPath + File.separator + bucketName + File.separator + fileName),
            new File(uploadPath + File.separator + destBucketName + File.separator + fileName)
        );
    }

    @Override
    public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException {
        FileUtils.copyFileToDirectory(
            new File(uploadPath + File.separator + bucketName + File.separator + fileName),
            new File(uploadPath + File.separator + destBucketName + File.separator + destFileName)
        );
    }

    @Override
    public OssFile statFile(String fileName)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        return null;
    }

    @Override
    public OssFile statFile(String bucketName, String fileName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return null;
    }

    @Override
    public String filePath(String fileName) {
        return null;
    }

    @Override
    public String filePath(String bucketName, String fileName) {
        return null;
    }

    @Override
    public String fileLink(String fileName) {
        return null;
    }

    @Override
    public String fileLink(String bucketName, String fileName) {
        return null;
    }

    @Override
    public BladeFile putFile(MultipartFile file)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException {
        return putFile(ossRule.fileName(file.getOriginalFilename()), file);
    }

    @Override
    public BladeFile putFile(String fileName, MultipartFile file)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException {
        return putFile(applicationProperties.getOss().getBucketName(), fileName, file);
    }

    @Override
    public BladeFile putFile(String bucketName, String fileName, MultipartFile file)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, ErrorResponseException {
        makeBucket(bucketName);
        File dest = new File(uploadPath + File.separator + bucketName + File.separator + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
        FilenameUtils.getName(dest.getName());
        BladeFile result = new BladeFile();
        result.setName(dest.getName());
        result.setDomain("");
        result.setLink("/" + fileName);
        result.setOriginalName(file.getOriginalFilename());
        return result;
    }

    @Override
    public BladeFile putFile(String fileName, InputStream stream)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, ErrorResponseException {
        return putFile(applicationProperties.getOss().getBucketName(), fileName, stream);
    }

    @Override
    public BladeFile putFile(String bucketName, String fileName, InputStream stream)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidBucketNameException, InvalidObjectPrefixException, ErrorResponseException, NoResponseException, InsufficientDataException, InternalException, RegionConflictException, InvalidArgumentException {
        String ruleFileName = ossRule.fileName(fileName);
        Path dest = Path.of(uploadPath, bucketName, ruleFileName);
        Files.createDirectories(dest.getParent());
        FileCopyUtils.copy(stream, Files.newOutputStream(dest));
        BladeFile result = new BladeFile();
        result.setName(fileName);
        result.setDomain("");
        result.setLink("/" + ruleFileName);
        result.setOriginalName(fileName);
        return result;
    }

    @Override
    public void removeFile(String fileName)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException {
        new File(uploadPath + File.separator + fileName).deleteOnExit();
    }

    @Override
    public void removeFile(String bucketName, String fileName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        new File(uploadPath + File.separator + bucketName + File.separator + fileName).deleteOnExit();
    }

    @Override
    public void removeFiles(List<String> fileNames) {
        if (fileNames == null || fileNames.isEmpty()) {
            return;
        }
        fileNames.forEach(
            fileName -> {
                try {
                    FileUtils.forceDeleteOnExit(new File(uploadPath + File.separator + fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        );
    }

    @Override
    public void removeFiles(String bucketName, List<String> fileNames) {
        if (fileNames == null || fileNames.isEmpty()) {
            return;
        }
        fileNames.forEach(
            fileName -> {
                try {
                    FileUtils.forceDeleteOnExit(new File(uploadPath + File.separator + bucketName + File.separator + fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        );
    }
}
