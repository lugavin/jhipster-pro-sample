package com.gavin.myapp.service.dto;

import com.gavin.myapp.domain.enumeration.SmsProvider;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.gavin.myapp.domain.SmsConfig}的DTO。
 */
@ApiModel(description = "短信配置")
public class SmsConfigDTO implements Serializable {

    private Long id;

    /**
     * 提供商
     */
    @ApiModelProperty(value = "提供商")
    private SmsProvider provider;

    /**
     * 资源编号
     */
    @ApiModelProperty(value = "资源编号")
    private String smsCode;

    /**
     * 模板ID
     */
    @ApiModelProperty(value = "模板ID")
    private String templateId;

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
     * regionId
     */
    @ApiModelProperty(value = "regionId")
    private String regionId;

    /**
     * 短信签名
     */
    @ApiModelProperty(value = "短信签名")
    private String signName;

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

    public SmsProvider getProvider() {
        return provider;
    }

    public void setProvider(SmsProvider provider) {
        this.provider = provider;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
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

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
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
        if (!(o instanceof SmsConfigDTO)) {
            return false;
        }

        SmsConfigDTO smsConfigDTO = (SmsConfigDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, smsConfigDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsConfigDTO{" +
            "id=" + getId() +
            ", provider='" + getProvider() + "'" +
            ", smsCode='" + getSmsCode() + "'" +
            ", templateId='" + getTemplateId() + "'" +
            ", accessKey='" + getAccessKey() + "'" +
            ", secretKey='" + getSecretKey() + "'" +
            ", regionId='" + getRegionId() + "'" +
            ", signName='" + getSignName() + "'" +
            ", remark='" + getRemark() + "'" +
            ", enabled='" + getEnabled() + "'" +
            "}";
    }
}
