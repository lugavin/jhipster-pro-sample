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

/**
 * Criteria class for the {@link com.gavin.myapp.domain.CommonConditionItem} entity. This class is used
 * in {@link com.gavin.myapp.web.rest.CommonConditionItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /common-condition-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommonConditionItemCriteria implements Serializable, Criteria {

    private String jhiCommonSearchKeywords;

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter prefix;

    private StringFilter fieldName;

    private StringFilter fieldType;

    private StringFilter operator;

    private StringFilter value;

    private StringFilter suffix;

    private IntegerFilter order;

    private LongFilter commonConditionId;

    private StringFilter commonConditionName;

    public CommonConditionItemCriteria() {}

    public CommonConditionItemCriteria(CommonConditionItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.prefix = other.prefix == null ? null : other.prefix.copy();
        this.fieldName = other.fieldName == null ? null : other.fieldName.copy();
        this.fieldType = other.fieldType == null ? null : other.fieldType.copy();
        this.operator = other.operator == null ? null : other.operator.copy();
        this.value = other.value == null ? null : other.value.copy();
        this.suffix = other.suffix == null ? null : other.suffix.copy();
        this.order = other.order == null ? null : other.order.copy();
        this.commonConditionId = other.commonConditionId == null ? null : other.commonConditionId.copy();
        this.commonConditionName = other.commonConditionName == null ? null : other.commonConditionName.copy();
    }

    @Override
    public CommonConditionItemCriteria copy() {
        return new CommonConditionItemCriteria(this);
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

    public StringFilter getPrefix() {
        return prefix;
    }

    public StringFilter prefix() {
        if (prefix == null) {
            prefix = new StringFilter();
        }
        return prefix;
    }

    public void setPrefix(StringFilter prefix) {
        this.prefix = prefix;
    }

    public StringFilter getFieldName() {
        return fieldName;
    }

    public StringFilter fieldName() {
        if (fieldName == null) {
            fieldName = new StringFilter();
        }
        return fieldName;
    }

    public void setFieldName(StringFilter fieldName) {
        this.fieldName = fieldName;
    }

    public StringFilter getFieldType() {
        return fieldType;
    }

    public StringFilter fieldType() {
        if (fieldType == null) {
            fieldType = new StringFilter();
        }
        return fieldType;
    }

    public void setFieldType(StringFilter fieldType) {
        this.fieldType = fieldType;
    }

    public StringFilter getOperator() {
        return operator;
    }

    public StringFilter operator() {
        if (operator == null) {
            operator = new StringFilter();
        }
        return operator;
    }

    public void setOperator(StringFilter operator) {
        this.operator = operator;
    }

    public StringFilter getValue() {
        return value;
    }

    public StringFilter value() {
        if (value == null) {
            value = new StringFilter();
        }
        return value;
    }

    public void setValue(StringFilter value) {
        this.value = value;
    }

    public StringFilter getSuffix() {
        return suffix;
    }

    public StringFilter suffix() {
        if (suffix == null) {
            suffix = new StringFilter();
        }
        return suffix;
    }

    public void setSuffix(StringFilter suffix) {
        this.suffix = suffix;
    }

    public IntegerFilter getOrder() {
        return order;
    }

    public IntegerFilter order() {
        if (order == null) {
            order = new IntegerFilter();
        }
        return order;
    }

    public void setOrder(IntegerFilter order) {
        this.order = order;
    }

    public LongFilter getCommonConditionId() {
        return commonConditionId;
    }

    public LongFilter commonConditionId() {
        if (commonConditionId == null) {
            commonConditionId = new LongFilter();
        }
        return commonConditionId;
    }

    public void setCommonConditionId(LongFilter commonConditionId) {
        this.commonConditionId = commonConditionId;
    }

    public StringFilter getCommonConditionName() {
        return commonConditionName;
    }

    public StringFilter commonConditionName() {
        if (commonConditionName == null) {
            commonConditionName = new StringFilter();
        }
        return commonConditionName;
    }

    public void setCommonConditionName(StringFilter commonConditionName) {
        this.commonConditionName = commonConditionName;
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
        final CommonConditionItemCriteria that = (CommonConditionItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(prefix, that.prefix) &&
            Objects.equals(fieldName, that.fieldName) &&
            Objects.equals(fieldType, that.fieldType) &&
            Objects.equals(operator, that.operator) &&
            Objects.equals(value, that.value) &&
            Objects.equals(suffix, that.suffix) &&
            Objects.equals(order, that.order) &&
            Objects.equals(commonConditionId, that.commonConditionId) &&
            Objects.equals(commonConditionName, that.commonConditionName)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, prefix, fieldName, fieldType, operator, value, suffix, order, commonConditionId, commonConditionName);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonConditionItemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (prefix != null ? "prefix=" + prefix + ", " : "") +
                (fieldName != null ? "fieldName=" + fieldName + ", " : "") +
                (fieldType != null ? "fieldType=" + fieldType + ", " : "") +
                (operator != null ? "operator=" + operator + ", " : "") +
                (value != null ? "value=" + value + ", " : "") +
                (suffix != null ? "suffix=" + suffix + ", " : "") +
                (order != null ? "order=" + order + ", " : "") +
                (commonConditionId != null ? "commonConditionId=" + commonConditionId + ", " : "") +
                (commonConditionName != null ? "commonConditionName=" + commonConditionName + ", " : "") +
            "}";
    }
}
