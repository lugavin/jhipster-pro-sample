package com.gavin.myapp.service.criteria;

import com.gavin.myapp.domain.enumeration.SmsProvider;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.gavin.myapp.domain.SmsConfig} entity. This class is used
 * in {@link com.gavin.myapp.web.rest.SmsConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sms-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SmsConfigCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    /**
     * Class for filtering SmsProvider
     */
    public static class SmsProviderFilter extends Filter<SmsProvider> {

        public SmsProviderFilter() {}

        public SmsProviderFilter(SmsProviderFilter filter) {
            super(filter);
        }

        @Override
        public SmsProviderFilter copy() {
            return new SmsProviderFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private SmsProviderFilter provider;

    private StringFilter smsCode;

    private StringFilter templateId;

    private StringFilter accessKey;

    private StringFilter secretKey;

    private StringFilter regionId;

    private StringFilter signName;

    private StringFilter remark;

    private BooleanFilter enabled;

    public SmsConfigCriteria() {}

    public SmsConfigCriteria(SmsConfigCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.provider = other.provider == null ? null : other.provider.copy();
        this.smsCode = other.smsCode == null ? null : other.smsCode.copy();
        this.templateId = other.templateId == null ? null : other.templateId.copy();
        this.accessKey = other.accessKey == null ? null : other.accessKey.copy();
        this.secretKey = other.secretKey == null ? null : other.secretKey.copy();
        this.regionId = other.regionId == null ? null : other.regionId.copy();
        this.signName = other.signName == null ? null : other.signName.copy();
        this.remark = other.remark == null ? null : other.remark.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
    }

    @Override
    public SmsConfigCriteria copy() {
        return new SmsConfigCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public SmsProviderFilter getProvider() {
        return provider;
    }

    public SmsProviderFilter provider() {
        if (provider == null) {
            provider = new SmsProviderFilter();
        }
        return provider;
    }

    public void setProvider(SmsProviderFilter provider) {
        this.provider = provider;
    }

    public StringFilter getSmsCode() {
        return smsCode;
    }

    public StringFilter smsCode() {
        if (smsCode == null) {
            smsCode = new StringFilter();
        }
        return smsCode;
    }

    public void setSmsCode(StringFilter smsCode) {
        this.smsCode = smsCode;
    }

    public StringFilter getTemplateId() {
        return templateId;
    }

    public StringFilter templateId() {
        if (templateId == null) {
            templateId = new StringFilter();
        }
        return templateId;
    }

    public void setTemplateId(StringFilter templateId) {
        this.templateId = templateId;
    }

    public StringFilter getAccessKey() {
        return accessKey;
    }

    public StringFilter accessKey() {
        if (accessKey == null) {
            accessKey = new StringFilter();
        }
        return accessKey;
    }

    public void setAccessKey(StringFilter accessKey) {
        this.accessKey = accessKey;
    }

    public StringFilter getSecretKey() {
        return secretKey;
    }

    public StringFilter secretKey() {
        if (secretKey == null) {
            secretKey = new StringFilter();
        }
        return secretKey;
    }

    public void setSecretKey(StringFilter secretKey) {
        this.secretKey = secretKey;
    }

    public StringFilter getRegionId() {
        return regionId;
    }

    public StringFilter regionId() {
        if (regionId == null) {
            regionId = new StringFilter();
        }
        return regionId;
    }

    public void setRegionId(StringFilter regionId) {
        this.regionId = regionId;
    }

    public StringFilter getSignName() {
        return signName;
    }

    public StringFilter signName() {
        if (signName == null) {
            signName = new StringFilter();
        }
        return signName;
    }

    public void setSignName(StringFilter signName) {
        this.signName = signName;
    }

    public StringFilter getRemark() {
        return remark;
    }

    public StringFilter remark() {
        if (remark == null) {
            remark = new StringFilter();
        }
        return remark;
    }

    public void setRemark(StringFilter remark) {
        this.remark = remark;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public BooleanFilter enabled() {
        if (enabled == null) {
            enabled = new BooleanFilter();
        }
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public String getJhiCommonSearchKeywords() {
        return jhiCommonSearchKeywords;
    }

    public void setJhiCommonSearchKeywords(String jhiCommonSearchKeywords) {
        this.jhiCommonSearchKeywords = jhiCommonSearchKeywords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SmsConfigCriteria that = (SmsConfigCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(provider, that.provider) &&
            Objects.equals(smsCode, that.smsCode) &&
            Objects.equals(templateId, that.templateId) &&
            Objects.equals(accessKey, that.accessKey) &&
            Objects.equals(secretKey, that.secretKey) &&
            Objects.equals(regionId, that.regionId) &&
            Objects.equals(signName, that.signName) &&
            Objects.equals(remark, that.remark) &&
            Objects.equals(enabled, that.enabled)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, provider, smsCode, templateId, accessKey, secretKey, regionId, signName, remark, enabled);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsConfigCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (provider != null ? "provider=" + provider + ", " : "") +
                (smsCode != null ? "smsCode=" + smsCode + ", " : "") +
                (templateId != null ? "templateId=" + templateId + ", " : "") +
                (accessKey != null ? "accessKey=" + accessKey + ", " : "") +
                (secretKey != null ? "secretKey=" + secretKey + ", " : "") +
                (regionId != null ? "regionId=" + regionId + ", " : "") +
                (signName != null ? "signName=" + signName + ", " : "") +
                (remark != null ? "remark=" + remark + ", " : "") +
                (enabled != null ? "enabled=" + enabled + ", " : "") +
            "}";
    }
}
