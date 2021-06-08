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
 * Criteria class for the {@link com.gavin.myapp.domain.CommonCondition} entity. This class is used
 * in {@link com.gavin.myapp.web.rest.CommonConditionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /common-conditions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommonConditionCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private ZonedDateTimeFilter lastModifiedTime;

    private LongFilter itemsId;

    private StringFilter itemsFieldName;

    private LongFilter commonTableId;

    private StringFilter commonTableName;

    private StringFilter commonTableClazzName;

    public CommonConditionCriteria() {}

    public CommonConditionCriteria(CommonConditionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.lastModifiedTime = other.lastModifiedTime == null ? null : other.lastModifiedTime.copy();
        this.itemsId = other.itemsId == null ? null : other.itemsId.copy();
        this.itemsFieldName = other.itemsFieldName == null ? null : other.itemsFieldName.copy();
        this.commonTableId = other.commonTableId == null ? null : other.commonTableId.copy();
        this.commonTableName = other.commonTableName == null ? null : other.commonTableName.copy();
        this.commonTableClazzName = other.commonTableClazzName == null ? null : other.commonTableClazzName.copy();
    }

    @Override
    public CommonConditionCriteria copy() {
        return new CommonConditionCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public ZonedDateTimeFilter getLastModifiedTime() {
        return lastModifiedTime;
    }

    public ZonedDateTimeFilter lastModifiedTime() {
        if (lastModifiedTime == null) {
            lastModifiedTime = new ZonedDateTimeFilter();
        }
        return lastModifiedTime;
    }

    public void setLastModifiedTime(ZonedDateTimeFilter lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public LongFilter getItemsId() {
        return itemsId;
    }

    public LongFilter itemsId() {
        if (itemsId == null) {
            itemsId = new LongFilter();
        }
        return itemsId;
    }

    public void setItemsId(LongFilter itemsId) {
        this.itemsId = itemsId;
    }

    public StringFilter getItemsFieldName() {
        return itemsFieldName;
    }

    public StringFilter itemsFieldName() {
        if (itemsFieldName == null) {
            itemsFieldName = new StringFilter();
        }
        return itemsFieldName;
    }

    public void setItemsFieldName(StringFilter itemsFieldName) {
        this.itemsFieldName = itemsFieldName;
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

    public StringFilter getCommonTableClazzName() {
        return commonTableClazzName;
    }

    public StringFilter commonTableClazzName() {
        if (commonTableClazzName == null) {
            commonTableClazzName = new StringFilter();
        }
        return commonTableClazzName;
    }

    public void setCommonTableClazzName(StringFilter commonTableClazzName) {
        this.commonTableClazzName = commonTableClazzName;
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
        final CommonConditionCriteria that = (CommonConditionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(lastModifiedTime, that.lastModifiedTime) &&
            Objects.equals(itemsId, that.itemsId) &&
            Objects.equals(itemsFieldName, that.itemsFieldName) &&
            Objects.equals(commonTableId, that.commonTableId) &&
            Objects.equals(commonTableName, that.commonTableName) &&
            Objects.equals(commonTableClazzName, that.commonTableClazzName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            lastModifiedTime,
            itemsId,
            itemsFieldName,
            commonTableId,
            commonTableName,
            commonTableClazzName
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonConditionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (lastModifiedTime != null ? "lastModifiedTime=" + lastModifiedTime + ", " : "") +
                (itemsId != null ? "itemsId=" + itemsId + ", " : "") +
                (itemsFieldName != null ? "itemsFieldName=" + itemsFieldName + ", " : "") +
                (commonTableId != null ? "commonTableId=" + commonTableId + ", " : "") +
                (commonTableName != null ? "commonTableName=" + commonTableName + ", " : "") +
                (commonTableClazzName != null ? "commonTableClazzName=" + commonTableClazzName + ", " : "") +
            "}";
    }
}
