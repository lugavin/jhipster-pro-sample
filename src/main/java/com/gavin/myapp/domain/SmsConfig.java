package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.gavin.myapp.domain.enumeration.SmsProvider;
import java.io.Serializable;

/**
 * 短信配置
 */

@TableName(value = "sms_config")
public class SmsConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 提供商
     */
    @TableField(value = "provider")
    private SmsProvider provider;

    /**
     * 资源编号
     */
    @TableField(value = "sms_code")
    private String smsCode;

    /**
     * 模板ID
     */
    @TableField(value = "template_id")
    private String templateId;

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
     * regionId
     */
    @TableField(value = "region_id")
    private String regionId;

    /**
     * 短信签名
     */
    @TableField(value = "sign_name")
    private String signName;

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

    public SmsConfig id(Long id) {
        this.id = id;
        return this;
    }

    public SmsProvider getProvider() {
        return this.provider;
    }

    public SmsConfig provider(SmsProvider provider) {
        this.provider = provider;
        return this;
    }

    public void setProvider(SmsProvider provider) {
        this.provider = provider;
    }

    public String getSmsCode() {
        return this.smsCode;
    }

    public SmsConfig smsCode(String smsCode) {
        this.smsCode = smsCode;
        return this;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public SmsConfig templateId(String templateId) {
        this.templateId = templateId;
        return this;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getAccessKey() {
        return this.accessKey;
    }

    public SmsConfig accessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public SmsConfig secretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegionId() {
        return this.regionId;
    }

    public SmsConfig regionId(String regionId) {
        this.regionId = regionId;
        return this;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getSignName() {
        return this.signName;
    }

    public SmsConfig signName(String signName) {
        this.signName = signName;
        return this;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getRemark() {
        return this.remark;
    }

    public SmsConfig remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public SmsConfig enabled(Boolean enabled) {
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
        if (!(o instanceof SmsConfig)) {
            return false;
        }
        return id != null && id.equals(((SmsConfig) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsConfig{" +
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
