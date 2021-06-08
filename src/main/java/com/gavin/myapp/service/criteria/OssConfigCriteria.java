package com.gavin.myapp.service.criteria;

import com.gavin.myapp.domain.enumeration.OssProvider;
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
 * Criteria class for the {@link com.gavin.myapp.domain.OssConfig} entity. This class is used
 * in {@link com.gavin.myapp.web.rest.OssConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /oss-configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OssConfigCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    /**
     * Class for filtering OssProvider
     */
    public static class OssProviderFilter extends Filter<OssProvider> {

        public OssProviderFilter() {}

        public OssProviderFilter(OssProviderFilter filter) {
            super(filter);
        }

        @Override
        public OssProviderFilter copy() {
            return new OssProviderFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private OssProviderFilter provider;

    private StringFilter ossCode;

    private StringFilter endpoint;

    private StringFilter accessKey;

    private StringFilter secretKey;

    private StringFilter bucketName;

    private StringFilter appId;

    private StringFilter region;

    private StringFilter remark;

    private BooleanFilter enabled;

    public OssConfigCriteria() {}

    public OssConfigCriteria(OssConfigCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.provider = other.provider == null ? null : other.provider.copy();
        this.ossCode = other.ossCode == null ? null : other.ossCode.copy();
        this.endpoint = other.endpoint == null ? null : other.endpoint.copy();
        this.accessKey = other.accessKey == null ? null : other.accessKey.copy();
        this.secretKey = other.secretKey == null ? null : other.secretKey.copy();
        this.bucketName = other.bucketName == null ? null : other.bucketName.copy();
        this.appId = other.appId == null ? null : other.appId.copy();
        this.region = other.region == null ? null : other.region.copy();
        this.remark = other.remark == null ? null : other.remark.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
    }

    @Override
    public OssConfigCriteria copy() {
        return new OssConfigCriteria(this);
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

    public OssProviderFilter getProvider() {
        return provider;
    }

    public OssProviderFilter provider() {
        if (provider == null) {
            provider = new OssProviderFilter();
        }
        return provider;
    }

    public void setProvider(OssProviderFilter provider) {
        this.provider = provider;
    }

    public StringFilter getOssCode() {
        return ossCode;
    }

    public StringFilter ossCode() {
        if (ossCode == null) {
            ossCode = new StringFilter();
        }
        return ossCode;
    }

    public void setOssCode(StringFilter ossCode) {
        this.ossCode = ossCode;
    }

    public StringFilter getEndpoint() {
        return endpoint;
    }

    public StringFilter endpoint() {
        if (endpoint == null) {
            endpoint = new StringFilter();
        }
        return endpoint;
    }

    public void setEndpoint(StringFilter endpoint) {
        this.endpoint = endpoint;
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

    public StringFilter getBucketName() {
        return bucketName;
    }

    public StringFilter bucketName() {
        if (bucketName == null) {
            bucketName = new StringFilter();
        }
        return bucketName;
    }

    public void setBucketName(StringFilter bucketName) {
        this.bucketName = bucketName;
    }

    public StringFilter getAppId() {
        return appId;
    }

    public StringFilter appId() {
        if (appId == null) {
            appId = new StringFilter();
        }
        return appId;
    }

    public void setAppId(StringFilter appId) {
        this.appId = appId;
    }

    public StringFilter getRegion() {
        return region;
    }

    public StringFilter region() {
        if (region == null) {
            region = new StringFilter();
        }
        return region;
    }

    public void setRegion(StringFilter region) {
        this.region = region;
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
        final OssConfigCriteria that = (OssConfigCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(provider, that.provider) &&
            Objects.equals(ossCode, that.ossCode) &&
            Objects.equals(endpoint, that.endpoint) &&
            Objects.equals(accessKey, that.accessKey) &&
            Objects.equals(secretKey, that.secretKey) &&
            Objects.equals(bucketName, that.bucketName) &&
            Objects.equals(appId, that.appId) &&
            Objects.equals(region, that.region) &&
            Objects.equals(remark, that.remark) &&
            Objects.equals(enabled, that.enabled)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, provider, ossCode, endpoint, accessKey, secretKey, bucketName, appId, region, remark, enabled);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OssConfigCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (provider != null ? "provider=" + provider + ", " : "") +
                (ossCode != null ? "ossCode=" + ossCode + ", " : "") +
                (endpoint != null ? "endpoint=" + endpoint + ", " : "") +
                (accessKey != null ? "accessKey=" + accessKey + ", " : "") +
                (secretKey != null ? "secretKey=" + secretKey + ", " : "") +
                (bucketName != null ? "bucketName=" + bucketName + ", " : "") +
                (appId != null ? "appId=" + appId + ", " : "") +
                (region != null ? "region=" + region + ", " : "") +
                (remark != null ? "remark=" + remark + ", " : "") +
                (enabled != null ? "enabled=" + enabled + ", " : "") +
            "}";
    }
}
