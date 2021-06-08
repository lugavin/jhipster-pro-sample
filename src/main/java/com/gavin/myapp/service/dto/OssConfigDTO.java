package com.gavin.myapp.service.dto;

import com.gavin.myapp.domain.enumeration.OssProvider;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.gavin.myapp.domain.OssConfig}的DTO。
 */
@ApiModel(description = "对象存储配置")
public class OssConfigDTO implements Serializable {

    private Long id;

    /**
     * 提供商
     */
    @ApiModelProperty(value = "提供商")
    private OssProvider provider;

    /**
     * 资源编号
     */
    @ApiModelProperty(value = "资源编号")
    private String ossCode;

    /**
     * 资源地址
     */
    @ApiModelProperty(value = "资源地址")
    private String endpoint;

    /**
     * accessKey
     */
    @ApiModelProperty(value = "accessKey")
    private String accessKey;

    /**
     * secretKey
     */
    @ApiModelProperty(value = "secretKey")
    private String secretKey;

    /**
     * 空间名
     */
    @ApiModelProperty(value = "空间名")
    private String bucketName;

    /**
     * 应用ID
     */
    @ApiModelProperty(value = "应用ID")
    private String appId;

    /**
     * 地域简称
     */
    @ApiModelProperty(value = "地域简称")
    private String region;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 启用
     */
    @ApiModelProperty(value = "启用")
    private Boolean enabled;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OssProvider getProvider() {
        return provider;
    }

    public void setProvider(OssProvider provider) {
        this.provider = provider;
    }

    public String getOssCode() {
        return ossCode;
    }

    public void setOssCode(String ossCode) {
        this.ossCode = ossCode;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OssConfigDTO)) {
            return false;
        }

        OssConfigDTO ossConfigDTO = (OssConfigDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ossConfigDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OssConfigDTO{" +
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
