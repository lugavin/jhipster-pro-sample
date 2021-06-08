package com.gavin.myapp.oss.model;

import java.util.Date;
import java.util.Objects;

/**
 * OssFile
 *
 */
public class OssFile {

    /**
     * 文件地址
     */
    private String link;
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件hash值
     */
    public String hash;
    /**
     * 文件大小
     */
    private long length;
    /**
     * 文件上传时间
     */
    private Date putTime;
    /**
     * 文件contentType
     */
    private String contentType;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public Date getPutTime() {
        return putTime;
    }

    public void setPutTime(Date putTime) {
        this.putTime = putTime;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OssFile ossFile = (OssFile) o;
        return (
            length == ossFile.length &&
            Objects.equals(link, ossFile.link) &&
            Objects.equals(name, ossFile.name) &&
            Objects.equals(hash, ossFile.hash) &&
            Objects.equals(putTime, ossFile.putTime) &&
            Objects.equals(contentType, ossFile.contentType)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, name, hash, length, putTime, contentType);
    }

    @Override
    public String toString() {
        return (
            "OssFile{" +
            "link='" +
            link +
            '\'' +
            ", name='" +
            name +
            '\'' +
            ", hash='" +
            hash +
            '\'' +
            ", length=" +
            length +
            ", putTime=" +
            putTime +
            ", contentType='" +
            contentType +
            '\'' +
            '}'
        );
    }
}
