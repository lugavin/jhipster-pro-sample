package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.gavin.myapp.domain.enumeration.OssProvider;
import java.io.Serializable;

/**
 * 对象存储配置
 */

@TableName(value = "oss_config")
public class OssConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 提供商
     */
    @TableField(value = "provider")
    private OssProvider provider;

    /**
     * 资源编号
     */
    @TableField(value = "oss_code")
    private String ossCode;

    /**
     * 资源地址
     */
    @TableField(value = "endpoint")
    private String endpoint;

    /**
     * accessKey
     */
    @TableField(value = "access_key")
    private String accessKey;

    /**
     * secretKey
     */
    @TableField(value = "secret_key")
    private String secretKey;

    /**
     * 空间名
     */
    @TableField(value = "bucket_name")
    private String bucketName;

    /**
     * 应用ID
     */
    @TableField(value = "app_id")
    private String appId;

    /**
     * 地域简称
     */
    @TableField(value = "region")
    private String region;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 启用
     */
    @TableField(value = "enabled")
    private Boolean enabled;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OssConfig id(Long id) {
        this.id = id;
        return this;
    }

    public OssProvider getProvider() {
        return this.provider;
    }

    public OssConfig provider(OssProvider provider) {
        this.provider = provider;
        return this;
    }

    public void setProvider(OssProvider provider) {
        this.provider = provider;
    }

    public String getOssCode() {
        return this.ossCode;
    }

    public OssConfig ossCode(String ossCode) {
        this.ossCode = ossCode;
        return this;
    }

    public void setOssCode(String ossCode) {
        this.ossCode = ossCode;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    public OssConfig endpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return this.accessKey;
    }

    public OssConfig accessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public OssConfig secretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public OssConfig bucketName(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getAppId() {
        return this.appId;
    }

    public OssConfig appId(String appId) {
        this.appId = appId;
        return this;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRegion() {
        return this.region;
    }

    public OssConfig region(String region) {
        this.region = region;
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRemark() {
        return this.remark;
    }

    public OssConfig remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public OssConfig enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OssConfig)) {
            return false;
        }
        return id != null && id.equals(((OssConfig) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OssConfig{" +
            "id=" + getId() +
            ", provider='" + getProvider() + "'" +
            ", ossCode='" + getOssCode() + "'" +
            ", endpoint='" + getEndpoint() + "'" +
            ", accessKey='" + getAccessKey() + "'" +
            ", secretKey='" + getSecretKey() + "'" +
            ", bucketName='" + getBucketName() + "'" +
            ", appId='" + getAppId() + "'" +
            ", region='" + getRegion() + "'" +
            ", remark='" + getRemark() + "'" +
            ", enabled='" + getEnabled() + "'" +
            "}";
    }
}
