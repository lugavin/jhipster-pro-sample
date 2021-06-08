package com.gavin.myapp.oss;

import com.gavin.myapp.oss.model.BladeFile;
import com.gavin.myapp.oss.model.OssFile;
import io.minio.errors.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

/**
 * OssTemplate抽象API
 *
 */
public interface OssTemplate {
    /**
     * 创建 存储桶
     *
     * @param bucketName 存储桶名称
     */
    void makeBucket(String bucketName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, RegionConflictException, InvalidObjectPrefixException;

    /**
     * 删除 存储桶
     *
     * @param bucketName 存储桶名称
     */
    void removeBucket(String bucketName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException;

    /**
     * 存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return boolean
     */
    boolean bucketExists(String bucketName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException;

    /**
     * 拷贝文件
     *
     * @param bucketName     存储桶名称
     * @param fileName       存储桶文件名称
     * @param destBucketName 目标存储桶名称
     */
    void copyFile(String bucketName, String fileName, String destBucketName)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException;

    /**
     * 拷贝文件
     *
     * @param bucketName     存储桶名称
     * @param fileName       存储桶文件名称
     * @param destBucketName 目标存储桶名称
     * @param destFileName   目标存储桶文件名称
     */
    void copyFile(String bucketName, String fileName, String destBucketName, String destFileName)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException;

    /**
     * 获取文件信息
     *
     * @param fileName 存储桶文件名称
     * @return InputStream
     */
    OssFile statFile(String fileName)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException;

    /**
     * 获取文件信息
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶文件名称
     * @return InputStream
     */
    OssFile statFile(String bucketName, String fileName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException;

    /**
     * 获取文件相对路径
     *
     * @param fileName 存储桶对象名称
     * @return String
     */
    String filePath(String fileName);

    /**
     * 获取文件相对路径
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     * @return String
     */
    String filePath(String bucketName, String fileName);

    /**
     * 获取文件地址
     *
     * @param fileName 存储桶对象名称
     * @return String
     */
    String fileLink(String fileName);

    /**
     * 获取文件地址
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     * @return String
     */
    String fileLink(String bucketName, String fileName);

    /**
     * 上传文件
     *
     * @param file 上传文件类
     * @return BladeFile
     */
    BladeFile putFile(MultipartFile file)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException;

    /**
     * 上传文件
     *
     * @param file     上传文件类
     * @param fileName 上传文件名
     * @return BladeFile
     */
    BladeFile putFile(String fileName, MultipartFile file)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidArgumentException, ErrorResponseException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, RegionConflictException;

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   上传文件名
     * @param file       上传文件类
     * @return BladeFile
     */
    BladeFile putFile(String bucketName, String fileName, MultipartFile file)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, ErrorResponseException;

    /**
     * 上传文件
     *
     * @param fileName 存储桶对象名称
     * @param stream   文件流
     * @return BladeFile
     */
    BladeFile putFile(String fileName, InputStream stream)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, RegionConflictException, InvalidKeyException, InvalidArgumentException, InvalidObjectPrefixException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException, ErrorResponseException;

    /**
     * 上传文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     * @param stream     文件流
     * @return BladeFile
     */
    BladeFile putFile(String bucketName, String fileName, InputStream stream)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, XmlPullParserException, InvalidBucketNameException, InvalidObjectPrefixException, ErrorResponseException, NoResponseException, InsufficientDataException, InternalException, RegionConflictException, InvalidArgumentException;

    /**
     * 删除文件
     *
     * @param fileName 存储桶对象名称
     */
    void removeFile(String fileName)
        throws IOException, XmlPullParserException, NoSuchAlgorithmException, InvalidKeyException, InvalidArgumentException, ErrorResponseException, NoResponseException, InvalidBucketNameException, InsufficientDataException, InternalException;

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   存储桶对象名称
     */
    void removeFile(String bucketName, String fileName)
        throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException;

    /**
     * 批量删除文件
     *
     * @param fileNames 存储桶对象名称集合
     */
    void removeFiles(List<String> fileNames);

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileNames  存储桶对象名称集合
     */
    void removeFiles(String bucketName, List<String> fileNames);
}
