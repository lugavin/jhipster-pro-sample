package com.gavin.myapp.service.criteria;

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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.gavin.myapp.domain.UReportFile} entity. This class is used
 * in {@link com.gavin.myapp.web.rest.UReportFileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /u-report-files?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UReportFileCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private ZonedDateTimeFilter createAt;

    private ZonedDateTimeFilter updateAt;

    private LongFilter commonTableId;

    private StringFilter commonTableName;

    public UReportFileCriteria() {}

    public UReportFileCriteria(UReportFileCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.createAt = other.createAt == null ? null : other.createAt.copy();
        this.updateAt = other.updateAt == null ? null : other.updateAt.copy();
        this.commonTableId = other.commonTableId == null ? null : other.commonTableId.copy();
        this.commonTableName = other.commonTableName == null ? null : other.commonTableName.copy();
    }

    @Override
    public UReportFileCriteria copy() {
        return new UReportFileCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public ZonedDateTimeFilter getCreateAt() {
        return createAt;
    }

    public ZonedDateTimeFilter createAt() {
        if (createAt == null) {
            createAt = new ZonedDateTimeFilter();
        }
        return createAt;
    }

    public void setCreateAt(ZonedDateTimeFilter createAt) {
        this.createAt = createAt;
    }

    public ZonedDateTimeFilter getUpdateAt() {
        return updateAt;
    }

    public ZonedDateTimeFilter updateAt() {
        if (updateAt == null) {
            updateAt = new ZonedDateTimeFilter();
        }
        return updateAt;
    }

    public void setUpdateAt(ZonedDateTimeFilter updateAt) {
        this.updateAt = updateAt;
    }

    public LongFilter getCommonTableId() {
        return commonTableId;
    }

    public LongFilter commonTableId() {
        if (commonTableId == null) {
            commonTableId = new LongFilter();
        }
        return commonTableId;
    }

    public void setCommonTableId(LongFilter commonTableId) {
        this.commonTableId = commonTableId;
    }

    public StringFilter getCommonTableName() {
        return commonTableName;
    }

    public StringFilter commonTableName() {
        if (commonTableName == null) {
            commonTableName = new StringFilter();
        }
        return commonTableName;
    }

    public void setCommonTableName(StringFilter commonTableName) {
        this.commonTableName = commonTableName;
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
        final UReportFileCriteria that = (UReportFileCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(createAt, that.createAt) &&
            Objects.equals(updateAt, that.updateAt) &&
            Objects.equals(commonTableId, that.commonTableId) &&
            Objects.equals(commonTableName, that.commonTableName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createAt, updateAt, commonTableId, commonTableName);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UReportFileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (createAt != null ? "createAt=" + createAt + ", " : "") +
                (updateAt != null ? "updateAt=" + updateAt + ", " : "") +
                (commonTableId != null ? "commonTableId=" + commonTableId + ", " : "") +
                (commonTableName != null ? "commonTableName=" + commonTableName + ", " : "") +
            "}";
    }
}
