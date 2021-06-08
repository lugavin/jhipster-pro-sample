package com.gavin.myapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.diboot.core.binding.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

/**
 * 通用条件条目
 */

@TableName(value = "common_condition_item")
public class CommonConditionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 前置符号
     */
    @TableField(value = "prefix")
    private String prefix;

    /**
     * 字段名称
     */
    @TableField(value = "field_name")
    private String fieldName;

    /**
     * 字段类型
     */
    @TableField(value = "field_type")
    private String fieldType;

    /**
     * 运算符号
     */
    @TableField(value = "operator")
    private String operator;

    /**
     * 比较值
     */
    @TableField(value = "value")
    private String value;

    /**
     * 后缀
     */
    @TableField(value = "suffix")
    private String suffix;

    /**
     * 顺序
     */
    @TableField(value = "`order`")
    private Integer order;

    @TableField(value = "common_condition_id")
    private Long commonConditionId;

    /**
     * 查询
     */
    @TableField(exist = false)
    @BindEntity(entity = CommonCondition.class, condition = "this.common_condition_id=id")
    @JsonIgnoreProperties(value = { "items", "commonTable" }, allowSetters = true)
    private CommonCondition commonCondition;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CommonConditionItem id(Long id) {
        this.id = id;
        return this;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public CommonConditionItem prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public CommonConditionItem fieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return this.fieldType;
    }

    public CommonConditionItem fieldType(String fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getOperator() {
        return this.operator;
    }

    public CommonConditionItem operator(String operator) {
        this.operator = operator;
        return this;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return this.value;
    }

    public CommonConditionItem value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public CommonConditionItem suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Integer getOrder() {
        return this.order;
    }

    public CommonConditionItem order(Integer order) {
        this.order = order;
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public CommonCondition getCommonCondition() {
        return this.commonCondition;
    }

    public CommonConditionItem commonCondition(CommonCondition commonCondition) {
        this.setCommonCondition(commonCondition);
        return this;
    }

    public void setCommonCondition(CommonCondition commonCondition) {
        this.commonCondition = commonCondition;
    }

    public Long getCommonConditionId() {
        return commonConditionId;
    }

    public void setCommonConditionId(Long commonConditionId) {
        this.commonConditionId = commonConditionId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommonConditionItem)) {
            return false;
        }
        return id != null && id.equals(((CommonConditionItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonConditionItem{" +
            "id=" + getId() +
            ", prefix='" + getPrefix() + "'" +
            ", fieldName='" + getFieldName() + "'" +
            ", fieldType='" + getFieldType() + "'" +
            ", operator='" + getOperator() + "'" +
            ", value='" + getValue() + "'" +
            ", suffix='" + getSuffix() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
