package com.gavin.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.gavin.myapp.domain.CommonConditionItem}的DTO。
 */
@ApiModel(description = "通用条件条目")
public class CommonConditionItemDTO implements Serializable {

    private Long id;

    /**
     * 前置符号
     */
    @ApiModelProperty(value = "前置符号")
    private String prefix;

    /**
     * 字段名称
     */
    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    /**
     * 字段类型
     */
    @ApiModelProperty(value = "字段类型")
    private String fieldType;

    /**
     * 运算符号
     */
    @ApiModelProperty(value = "运算符号")
    private String operator;

    /**
     * 比较值
     */
    @ApiModelProperty(value = "比较值")
    private String value;

    /**
     * 后缀
     */
    @ApiModelProperty(value = "后缀")
    private String suffix;

    /**
     * 顺序
     */
    @ApiModelProperty(value = "顺序")
    private Integer order;

    private CommonConditionDTO commonCondition;

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public CommonConditionDTO getCommonCondition() {
        return commonCondition;
    }

    public void setCommonCondition(CommonConditionDTO commonCondition) {
        this.commonCondition = commonCondition;
    }

    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommonConditionItemDTO)) {
            return false;
        }

        CommonConditionItemDTO commonConditionItemDTO = (CommonConditionItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commonConditionItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommonConditionItemDTO{" +
            "id=" + getId() +
            ", prefix='" + getPrefix() + "'" +
            ", fieldName='" + getFieldName() + "'" +
            ", fieldType='" + getFieldType() + "'" +
            ", operator='" + getOperator() + "'" +
            ", value='" + getValue() + "'" +
            ", suffix='" + getSuffix() + "'" +
            ", order=" + getOrder() +
            ", commonCondition=" + getCommonCondition() +
            "}";
    }
}
